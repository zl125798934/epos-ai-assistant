package cn.zhang.eposaiassistant.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * AI 对话服务接口。
 *
 * <p>提供通用对话、流式对话能力。具体 prompt 由实现类通过 {@link SystemMessage} 指定。</p>
 */
public interface AiChatService {

  /**
   * 执行单轮对话。
   *
   * @param userMessage 用户消息
   * @return AI 回复内容
   */
  String chat(final String userMessage);

  /**
   * 执行流式对话。
   *
   * @param memoryId    会话记忆 ID
   * @param userMessage 用户消息
   * @return 流式响应
   */
  Flux<String> chatStream(@MemoryId final int memoryId, @UserMessage final String userMessage);
}
