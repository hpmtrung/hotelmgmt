package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterUpsertDto;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.factory.NewsletterFactory;

import java.time.LocalDateTime;

@Component
public final class DefaultNewsletterFactory implements NewsletterFactory {

  private final EmployeeService employeeService;

  public DefaultNewsletterFactory(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  public Subscriber createNewSubcriber(String email) {
    return new Subscriber().setCreatedAt(LocalDateTime.now()).setEmail(email).setVerified(false);
  }

  @Override
  public Newsletter createNewNewsletter(NewsletterUpsertDto newsletterUpsertDto) {
    return new Newsletter()
        .setTitle(newsletterUpsertDto.getTitle())
        .setContent(newsletterUpsertDto.getContent())
        .setCreatedBy(employeeService.getAuditedLogin())
        .setPublished(false)
        .setDeleted(false)
        .setModifiedAt(LocalDateTime.now());
  }
}