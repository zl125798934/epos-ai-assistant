package cn.zhang.eposaiassistant.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * 闲聊专用 AI 对话服务。
 */
public interface ChatOnlyAiChatService extends AiChatService {

  @Override
  @SystemMessage(fromResource = "system-prompt-chat.txt")
  String chat(final String userMessage);

  @Override
  @SystemMessage(fromResource = "system-prompt-chat.txt")
  Flux<String> chatStream(@MemoryId final int memoryId, @UserMessage final String userMessage);
}
