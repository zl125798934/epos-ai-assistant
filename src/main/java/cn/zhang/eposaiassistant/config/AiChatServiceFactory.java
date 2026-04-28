package cn.zhang.eposaiassistant.config;

import cn.zhang.eposaiassistant.service.AiChatService;
import cn.zhang.eposaiassistant.service.EposAiTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * 会话记忆条数
     */
    private int CHAT_MEMORY_COUNT = 10;
    
    @Bean
    public AiChatService aiService() {
        // 会话记忆
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT);
        // 构造 AI Service
        AiChatService aiCodeHelperService = AiServices.builder(AiChatService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemory(chatMemory)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_COUNT)) // 每个会话独立存储
                .contentRetriever(contentRetriever) // RAG 检索增强生成
                .tools(eposAiTool) // 工具调用
                .build();
        return aiCodeHelperService;
    }
}
