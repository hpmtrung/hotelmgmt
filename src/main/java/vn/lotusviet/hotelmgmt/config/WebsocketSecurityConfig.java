package vn.lotusviet.hotelmgmt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import vn.lotusviet.hotelmgmt.security.AuthorityConstants;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
        .nullDestMatcher()
        .authenticated()
        .simpDestMatchers("/topic/tracker")
        .hasAnyRole(AuthorityConstants.ADMIN)
        // matches any destination that starts with /topic/
        // (i.e. cannot send messages directly to /topic/)
        // (i.e. cannot subcribe to /topic/message/* to get messages sent to
        // /topic/message-user<id>)
        .simpDestMatchers("/topic/**")
        .authenticated()
        // message types other than MESSAGE and SUBCRIBE
        .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE)
        .denyAll()
        // catch all
        .anyMessage()
        .denyAll();
  }

  /** Disable CSRF for websockets. */
  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }
}