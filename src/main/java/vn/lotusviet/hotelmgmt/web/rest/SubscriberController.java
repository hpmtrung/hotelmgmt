package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.SubscriberDto;
import vn.lotusviet.hotelmgmt.service.NewsletterService;

import javax.persistence.OptimisticLockException;

@RestController
@RequestMapping("/api/v1/subscriber")
public class SubscriberController {

  private final NewsletterService newsletterService;

  public SubscriberController(NewsletterService newsletterService) {
    this.newsletterService = newsletterService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/subscribe")
  public void subscribeNewsletter(final @RequestParam String email) {
    newsletterService.saveNewSubscriber(email);
  }

  @Retry(on = OptimisticLockException.class)
  @DeleteMapping("/unsubscribe")
  public void unsubscribeNewsletter(final @RequestParam String email) {
    newsletterService.deleteSubscriber(email);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/verify_subscription")
  public SubscriberDto verifySubscription(final @RequestParam String email) {
    return newsletterService.verifiedSubscriber(email);
  }
}