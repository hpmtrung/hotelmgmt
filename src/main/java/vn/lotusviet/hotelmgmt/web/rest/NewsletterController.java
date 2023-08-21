package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterUpsertDto;
import vn.lotusviet.hotelmgmt.service.NewsletterService;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@AdminSecured
@RestController
@RequestMapping("/api/v1/newsletter")
public class NewsletterController {

  private final NewsletterService newsletterService;

  public NewsletterController(NewsletterService newsletterService) {
    this.newsletterService = newsletterService;
  }

  @GetMapping
  public List<NewsletterDto> getNewsletters() {
    return newsletterService.getNewsletters();
  }

  @GetMapping("/{newsletterId}")
  public NewsletterDto getDetailOfNewsletter(@PathVariable int newsletterId) {
    return newsletterService.getDetailOfNewsletter(newsletterId);
  }

  @PostMapping("/create")
  public NewsletterDto saveNewNewsletter(
      @Valid @RequestBody NewsletterUpsertDto newsletterUpsertDto) {
    return newsletterService.saveNewNewsletter(newsletterUpsertDto);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{newsletterId}/update")
  public NewsletterDto updateNewsletter(
      @PathVariable int newsletterId, @Valid @RequestBody NewsletterUpsertDto newsletterUpsertDto) {
    return newsletterService.updateNewsletter(newsletterId, newsletterUpsertDto);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{newsletterId}/soft_delete")
  public NewsletterDto softDeleteNewsletter(@PathVariable int newsletterId) {
    return newsletterService.softDeleteNewsletter(newsletterId);
  }

  @Retry(on = OptimisticLockException.class)
  @DeleteMapping("/{newsletterId}/hard_delete")
  public void hardDeleteNewsletter(@PathVariable int newsletterId) {
    newsletterService.hardDeleteNewsletter(newsletterId);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{newsletterId}/restore")
  public NewsletterDto restoreDeletedNewsletter(@PathVariable int newsletterId) {
    return newsletterService.restoreDeletedNewsletter(newsletterId);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{newsletterId}/publish")
  public NewsletterDto publishNewsletter(@PathVariable int newsletterId) {
    return newsletterService.publishNewsletter(newsletterId);
  }
}