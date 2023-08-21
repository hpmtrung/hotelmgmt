package vn.lotusviet.hotelmgmt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import vn.lotusviet.hotelmgmt.security.AuthorityConstants;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

  public static final String TRACKED_IP_ADDRESS = "IP_ADDRESS";

  private final CorsConfiguration corsConfiguration;

  public WebsocketConfig(CorsConfiguration corsConfiguration) {
    this.corsConfiguration = corsConfiguration;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    String[] allowedOrigins =
        Optional.ofNullable(corsConfiguration.getAllowedOrigins())
            .map(origins -> origins.toArray(new String[0]))
            .orElse(new String[0]);
    registry
        .addEndpoint("/websocket/tracker")
        .setHandshakeHandler(defaultHandshakeHandler())
        .setAllowedOrigins(allowedOrigins)
        // use sockjs fallback
        .withSockJS()
        .setInterceptors(httpSessionHandshakeInterceptor());
  }

  private HandshakeInterceptor httpSessionHandshakeInterceptor() {
    return new HandshakeInterceptor() {
      @Override
      public boolean beforeHandshake(
          ServerHttpRequest request,
          ServerHttpResponse response,
          WebSocketHandler wsHandler,
          Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
          ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
          attributes.put(TRACKED_IP_ADDRESS, servletServerHttpRequest.getRemoteAddress());
        }
        return true;
      }

      @Override
      public void afterHandshake(
          ServerHttpRequest request,
          ServerHttpResponse response,
          WebSocketHandler wsHandler,
          Exception exception) {
        // Do nothing
      }
    };
  }

  private DefaultHandshakeHandler defaultHandshakeHandler() {
    return new DefaultHandshakeHandler() {
      @Override
      protected Principal determineUser(
          ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();
        if (principal == null) {
          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          authorities.add(new SimpleGrantedAuthority(AuthorityConstants.ANONYMOUS));
          principal = new AnonymousAuthenticationToken("", "anonymous", authorities);
        }
        return principal;
      }
    };
  }
}