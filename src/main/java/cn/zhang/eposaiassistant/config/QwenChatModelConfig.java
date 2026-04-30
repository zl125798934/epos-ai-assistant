package cn.zhang.eposaiassistant.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通义千问对话模型配置类。
 */
@Configuration
@ConfigurationProperties(prefix = "langchain4j.community.dashscope.chat-model")
@Getter
@Setter
public class QwenChatModelConfig {

  private String modelName;

  private String apiKey;

  @Resource
  private ChatModelListener chatModelListener;

  /**
   * 创建对话模型 Bean。
   *
   * @return 对话模型实例
   */
  @Bean
  public ChatModel myQwenChatModel() {
    return QwenChatModel.builder()
        .apiKey(apiKey)
        .modelName(modelName)
        .listeners(List.of(chatModelListener))
        .build();
  }
}
