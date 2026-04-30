package cn.zhang.eposaiassistant;

import cn.zhang.eposaiassistant.service.AiChatService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 应用上下文启动测试。
 */
@SpringBootTest
class EposAiAssistantApplicationTests {

  @Resource
  private AiChatService aiChatService;

  @Test
  void chat() {
    String content = aiChatService.chat("什么是保全？");
    System.out.println(content);
  }

}
