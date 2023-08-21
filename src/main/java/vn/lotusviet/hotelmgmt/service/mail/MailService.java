package vn.lotusviet.hotelmgmt.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.MailConfig;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/**
 * Service for sending emails.
 *
 * <p>We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

  private static final String ACCOUNT = "account";
  private static final String BASE_URL = "baseUrl";

  private static final Logger log = LoggerFactory.getLogger(MailService.class);

  private final MailConfig mailConfig;

  private final JavaMailSender javaMailSender;

  private final MessageSource messageSource;

  private final SpringTemplateEngine templateEngine;

  public MailService(
      ApplicationProperties applicationProperties,
      JavaMailSender javaMailSender,
      MessageSource messageSource,
      SpringTemplateEngine templateEngine) {
    this.mailConfig = applicationProperties.getMail();
    this.javaMailSender = javaMailSender;
    this.messageSource = messageSource;
    this.templateEngine = templateEngine;
  }

  @Async
  public void sendEmail(
      String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    log.debug(
        "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart,
        isHtml,
        to,
        subject,
        content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      final MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
      message.setTo(to);
      message.setFrom(mailConfig.getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      log.debug("Sent email to User '{}'", to);
    } catch (MailException | MessagingException e) {
      log.warn("Email could not be sent to user '{}'", to, e);
    }
  }

  @Async
  public void sendEmailFromTemplate(Account account, String templateName, String titleKey) {
    final String accountEmail = account.getEmail();
    Objects.requireNonNull(accountEmail, "Account email should not be null");

    Locale locale = Locale.forLanguageTag(account.getLangKey());

    Context context = new Context(locale);
    context.setVariable(ACCOUNT, account);
    context.setVariable(BASE_URL, mailConfig.getBaseUrl());

    String content = templateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, locale);

    sendEmail(accountEmail, subject, content, false, true);
  }

  @Async
  public void sendActivationEmail(Account account) {
    log.debug("Sending activation email to '{}'", account.getEmail());
    sendEmailFromTemplate(account, "mail/activationEmail", "email.activation.title");
  }

  @Async
  public void sendPasswordResetMail(Account account) {
    log.debug("Sending password reset email to '{}'", account.getEmail());
    sendEmailFromTemplate(account, "mail/passwordResetEmail", "email.reset.title");
  }

  @Async
  public void sendWelcomeMail(Account account) {
    log.debug("Sending welcome email to '{}'", account.getEmail());
    sendEmailFromTemplate(account, "mail/welcome", "email.welcome.title");
  }

  @Async
  public void sendVerifySubcriptionMail(Subscriber subscriber) {
    log.debug("Sending verify subcription email to '{}'", subscriber.getEmail());

    Locale locale = Locale.forLanguageTag("vi");

    Context context = new Context(locale);
    context.setVariable("email", subscriber.getEmail());
    context.setVariable(BASE_URL, mailConfig.getBaseUrl());

    String content = templateEngine.process("mail/verifySubcription", context);
    String subject = messageSource.getMessage("email.verifySubcription.title", null, locale);

    sendEmail(subscriber.getEmail(), subject, content, false, true);
  }

  @Async
  public void sendNewsletter(Subscriber subscriber, Newsletter newsletter) {
    log.debug("Sending newsletter email to '{}'", subscriber.getEmail());

    Locale locale = Locale.forLanguageTag("vi");

    Context context = new Context(locale);
    context.setVariable("email", subscriber.getEmail());
    context.setVariable("title", newsletter.getTitle());
    context.setVariable("content", newsletter.getContent());
    context.setVariable(BASE_URL, mailConfig.getBaseUrl());

    String content = templateEngine.process("mail/newsletter", context);
    String subject = messageSource.getMessage("email.newsletter.title", null, locale);

    sendEmail(subscriber.getEmail(), subject, content, false, true);
  }
}