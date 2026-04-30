package cn.zhang.eposaiassistant.config;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对话模型监听器配置类。
 */
@Configuration
@Slf4j
public class ChatModelListenerConfig {

  /**
   * 创建对话模型监听器 Bean。
   *
   * @return 监听器实例
   */
  @Bean
  ChatModelListener chatModelListener() {
    return new ChatModelListener() {
      @Override
      public void onRequest(final ChatModelRequestContext requestContext) {
        log.info("onRequest(): {}", requestContext.chatRequest());
      }

      @Override
      public void onResponse(final ChatModelResponseContext responseContext) {
        log.info("onResponse(): {}", responseContext.chatResponse());
      }

      @Override
      public void onError(final ChatModelErrorContext errorContext) {
        log.error("onError(): {}", errorContext.error().getMessage());
      }
    };
  }
}
