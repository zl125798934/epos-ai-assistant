package cn.zhang.eposaiassistant.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * RAG 知识问答专用 AI 对话服务。
 */
public interface RagAiChatService extends AiChatService {

  @Override
  @SystemMessage(fromResource = "system-prompt-rag.txt")
  String chat(final String userMessage);

  @Override
  @SystemMessage(fromResource = "system-prompt-rag.txt")
  Flux<String> chatStream(@MemoryId final int memoryId, @UserMessage final String userMessage);
}
