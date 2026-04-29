package cn.zhang.eposaiassistant.config;

import cn.zhang.eposaiassistant.service.AiChatService;
import cn.zhang.eposaiassistant.service.AgentAiChatService;
import cn.zhang.eposaiassistant.service.ChatOnlyAiChatService;
import cn.zhang.eposaiassistant.service.EposAiTool;
import cn.zhang.eposaiassistant.service.RagAiChatService;
import cn.zhang.eposaiassistant.data.Intent;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class AiChatServiceFactory {

    @Resource
    private ChatModel myQwenChatModel;

    @Resource
    private StreamingChatModel qwenStreamingChatModel;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private EposAiTool eposAiTool;

    @Resource
    private IntentClassifier intentClassifier;

    /**
     * 会话记忆条数
     */
    private static final int CHAT_MEMORY_COUNT = 10;

    /**
     * 通用配置：同时启用 RAG + Tool（兼容旧逻辑，保留备用）
     */
    @Primary
    @Bean
    public AiChatService aiService() {
        return AiServices.builder(AiChatService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT))
                .contentRetriever(contentRetriever)
                .tools(eposAiTool)
                .build();
    }

    /**
     * 知识问答专用：只启用 RAG，不注入 Tools
     */
    @Bean("ragChatService")
    public RagAiChatService ragChatService() {
        return AiServices.builder(RagAiChatService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT))
                .contentRetriever(contentRetriever)
                .build();
    }

    /**
     * 操作办理专用：只启用 Tools，不执行 RAG
     */
    @Bean("agentChatService")
    public AgentAiChatService agentChatService() {
        return AiServices.builder(AgentAiChatService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT))
                .tools(eposAiTool)
                .build();
    }

    /**
     * 闲聊专用：纯 LLM 对话，无 RAG 无 Tools
     */
    @Bean("chatOnlyService")
    public ChatOnlyAiChatService chatOnlyService() {
        return AiServices.builder(ChatOnlyAiChatService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT))
                .build();
    }

    /**
     * 根据意图识别，返回对应的 AiService
     *
     * @param userMessage
     * @return
     */
    public AiChatService getTargetAiService(String userMessage) {
        Intent intent = intentClassifier.classifyByLlm(userMessage);
        log.info("用户意图识别结果: {}, 消息: {}", intent, userMessage);

        //兜底方案
        if (intent == null){
            return aiService();
        }

        AiChatService targetService =
            switch (intent) {
              case KNOWLEDGE -> ragChatService();
              case OPERATION -> agentChatService();
              case CHAT -> chatOnlyService();
            };
        return targetService;
    }

}
