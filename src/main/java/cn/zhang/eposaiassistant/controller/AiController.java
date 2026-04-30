package cn.zhang.eposaiassistant.controller;


import cn.zhang.eposaiassistant.config.AiChatServiceFactory;
import cn.zhang.eposaiassistant.service.AiChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * AI 对话控制器。
 *
 * <p>支持意图识别 + 路由分发。</p>
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/ai")
public class AiController {

  @Resource
  private AiChatServiceFactory aiChatServiceFactory;

  /**
   * 聊天接口（带意图识别路由）。
   *
   * @param memoryId 会话记忆 ID
   * @param message  用户消息
   * @return SSE 流式响应
   */
  @GetMapping("/chat")
  public Flux<ServerSentEvent<String>> chat(final int memoryId, final String message) {
    // 意图识别
    AiChatService targetService = aiChatServiceFactory.getTargetAiService(message);

    return targetService.chatStream(memoryId, message)
        .map(chunk -> ServerSentEvent.<String>builder()
            .data(chunk)
            .build())
        .concatWith(Flux.just(ServerSentEvent.<String>builder()
            .data("[DONE]")
            .build()));
  }

}
