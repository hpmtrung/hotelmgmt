package vn.lotusviet.hotelmgmt.web.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.lotusviet.hotelmgmt.model.dto.system.SocketActivityDto;

import java.security.Principal;
import java.time.Instant;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.config.WebsocketConfig.TRACKED_IP_ADDRESS;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

  private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

  private final SimpMessageSendingOperations messageTemplate;

  public ActivityService(SimpMessageSendingOperations messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  @MessageMapping("/topic/activity")
  @SendTo("/topic/tracker")
  public SocketActivityDto sendActivity(
      @Payload SocketActivityDto activityDto,
      StompHeaderAccessor stompHeaderAccessor,
      Principal principal) {
    activityDto.setPrinciple(principal.getName());
    activityDto.setSessionId(stompHeaderAccessor.getSessionId());
    activityDto.setIpAddress(
        Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes().get(TRACKED_IP_ADDRESS))
            .toString());
    activityDto.setTime(Instant.now());
    log.debug("Sending user tracking data {}", activityDto);
    return activityDto;
  }

  @Override
  public void onApplicationEvent(SessionDisconnectEvent event) {
    SocketActivityDto activityDto = new SocketActivityDto();
    activityDto.setSessionId(event.getSessionId());
    activityDto.setPage("logout");
    messageTemplate.convertAndSend("/topic/tracker", activityDto);
  }
}