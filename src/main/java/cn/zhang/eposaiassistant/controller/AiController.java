package cn.zhang.eposaiassistant.controller;


import cn.zhang.eposaiassistant.service.AiChatService;
import jakarta.annotation.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: zhanglun @Date: 2026/4/28 16:26 @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/api/ai")
public class AiController {


	@Resource
	private AiChatService aiChatService;

	/**
	 * 聊天
	 * @param memoryId
	 * @param message
	 * @return
	 */
	@GetMapping("/chat")
	public Flux<ServerSentEvent<String>> chat(int memoryId, String message) {
		return aiChatService.chatStream(memoryId, message)
				.map(chunk -> ServerSentEvent.<String>builder()
						.data(chunk)
						.build())
				.concatWith(Flux.just(ServerSentEvent.<String>builder()
						.data("[DONE]")
						.build()));
	}

}
