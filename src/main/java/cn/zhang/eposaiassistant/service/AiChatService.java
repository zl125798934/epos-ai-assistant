package cn.zhang.eposaiassistant.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * @Author: zhanglun @Date: 2026/4/28 16:27 @Description:
 */
public interface AiChatService {

	@SystemMessage(fromResource = "system-prompt.txt")
	String chat(String userMessage);

	// 流式对话
	@SystemMessage(fromResource = "system-prompt.txt")
	Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
