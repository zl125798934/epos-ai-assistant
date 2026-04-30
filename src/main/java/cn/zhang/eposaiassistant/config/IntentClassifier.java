package cn.zhang.eposaiassistant.config;

import cn.zhang.eposaiassistant.data.Intent;
import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户意图识别分类器。
 *
 * <p>优先使用 LLM 进行意图识别。</p>
 */
@Slf4j
@Service
public class IntentClassifier {

  @Resource
  private ChatModel myQwenChatModel;

  /**
   * 意图识别系统提示词。
   */
  private static final String INTENT_SYSTEM_PROMPT = """
      你是一名意图识别专家。请分析用户的输入，判断其意图属于以下三类之一：

      KNOWLEDGE：知识问答类。用户询问保险保全相关知识、概念、定义、流程等。
      例如：什么是保全？犹豫期是什么意思？有哪些保全项目？退保和减额交清有什么区别？

      OPERATION：操作办理类。用户想要查询保单信息、办理保全业务、查看进度等。
      例如：查询我的保单、我要退保、修改手机号、查一下进度、取消退保。

      CHAT：闲聊类。用户的输入是问候、感谢、寒暄、无明确意图的对话。
      例如：你好、谢谢、再见、在吗、今天天气怎么样。

      规则：
      1. 只输出一个单词：KNOWLEDGE、OPERATION 或 CHAT
      2. 不要输出任何解释、标点或额外内容
      3. 如果用户输入同时涉及多种意图，优先判断为 OPERATION > KNOWLEDGE > CHAT
      """;

  /**
   * 使用 LLM 进行意图识别。
   *
   * @param message 用户消息
   * @return 识别出的意图，无法识别时返回 {@code null}
   */
  public Intent classifyByLlm(final String message) {
    dev.langchain4j.model.chat.response.ChatResponse chatResponse = myQwenChatModel.chat(
        dev.langchain4j.data.message.UserMessage.from(
            INTENT_SYSTEM_PROMPT + "\n\n用户输入：" + message + "\n\n意图："
        )
    );

    String response = chatResponse.aiMessage().text();

    if (response == null || response.trim().isEmpty()) {
      return null;
    }

    String result = response.trim().toUpperCase();

    // 处理可能的额外内容，提取关键单词
    if (result.contains("KNOWLEDGE")) {
      return Intent.KNOWLEDGE;
    } else if (result.contains("OPERATION")) {
      return Intent.OPERATION;
    } else if (result.contains("CHAT")) {
      return Intent.CHAT;
    }

    log.warn("LLM 返回无法识别的意图: {}", response);
    return null;
  }

}
