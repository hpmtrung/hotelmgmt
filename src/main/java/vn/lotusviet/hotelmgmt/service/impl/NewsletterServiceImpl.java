package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterUpsertDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.SubscriberDto;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;
import vn.lotusviet.hotelmgmt.repository.newsletter.NewsletterRepository;
import vn.lotusviet.hotelmgmt.repository.newsletter.SubscriberRepository;
import vn.lotusviet.hotelmgmt.service.NewsletterService;
import vn.lotusviet.hotelmgmt.service.factory.NewsletterFactory;
import vn.lotusviet.hotelmgmt.service.mail.MailService;
import vn.lotusviet.hotelmgmt.service.mapper.NewsletterMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class NewsletterServiceImpl implements NewsletterService {

  private static final int SUBCRIBER_NUM_SEND_PER_PAGE = 10;
  private final NewsletterRepository newsletterRepository;
  private final SubscriberRepository subscriberRepository;
  private final NewsletterFactory newsletterFactory;
  private final NewsletterMapper newsletterMapper;
  private final MailService mailService;

  public NewsletterServiceImpl(
      NewsletterFactory newsletterFactory,
      NewsletterRepository newsletterRepository,
      SubscriberRepository subscriberRepository,
      NewsletterMapper newsletterMapper,
      MailService mailService) {
    this.newsletterFactory = newsletterFactory;
    this.newsletterRepository = newsletterRepository;
    this.subscriberRepository = subscriberRepository;
    this.newsletterMapper = newsletterMapper;
    this.mailService = mailService;
  }

  @Override
  @LogAround
  public SubscriberDto saveNewSubscriber(final String email) {
    if (subscriberRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Subscriber email exists");
    }

    final Subscriber newSubscriber =
        subscriberRepository.save(newsletterFactory.createNewSubcriber(email));
    mailService.sendVerifySubcriptionMail(newSubscriber);
    return newsletterMapper.toSubcriberDto(newSubscriber);
  }

  @Override
  @LogAround
  public void deleteSubscriber(final String email) {
    Objects.requireNonNull(email);

    if (!subscriberRepository.existsByEmailAndVerifiedIsTrue(email)) {
      throw new IllegalArgumentException("Delete subscriber action is illegal");
    }
    final Subscriber subscriber = subscriberRepository.findById(email).orElseThrow();
    subscriberRepository.delete(subscriber);
  }

  @Override
  @LogAround
  public SubscriberDto verifiedSubscriber(final String email) {
    Objects.requireNonNull(email);
    if (!subscriberRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Subscriber email doesn't exist");
    }

    if (subscriberRepository.existsByEmailAndVerifiedIsTrue(email)) {
      throw new IllegalArgumentException("Subscriber is already verified!");
    }

    final Subscriber subscriber = subscriberRepository.findById(email).orElseThrow();
    subscriber.setVerified(true);
    return newsletterMapper.toSubcriberDto(subscriberRepository.save(subscriber));
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<NewsletterDto> getNewsletters() {
    return newsletterMapper.toNewsletterDtoWithoutContent(newsletterRepository.findAll());
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public NewsletterDto getDetailOfNewsletter(final int newsletterId) {
    return newsletterMapper.toNewsletterDto(getNewsletterById(newsletterId));
  }

  @Override
  @LogAround
  public NewsletterDto saveNewNewsletter(final NewsletterUpsertDto newsletterUpsertDto) {
    Objects.requireNonNull(newsletterUpsertDto);

    final Newsletter newNewsletter = newsletterFactory.createNewNewsletter(newsletterUpsertDto);
    return newsletterMapper.toNewsletterDto(newsletterRepository.save(newNewsletter));
  }

  @Override
  @LogAround
  public NewsletterDto updateNewsletter(
      final int newsletterId, final NewsletterUpsertDto newsletterUpsertDto) {
    Objects.requireNonNull(newsletterUpsertDto);

    final Newsletter newsletter = getNewsletterById(newsletterId);
    if (newsletter.isDeleted()) {
      throw new IllegalArgumentException("Update deleted newsletter is not allowed");
    }
    newsletter.setTitle(newsletterUpsertDto.getTitle());
    newsletter.setContent(newsletterUpsertDto.getContent());
    newsletter.setModifiedAt(LocalDateTime.now());
    return newsletterMapper.toNewsletterDto(newsletterRepository.save(newsletter));
  }

  @Override
  @LogAround
  public NewsletterDto softDeleteNewsletter(final int newsletterId) {
    final Newsletter newsletter = getNewsletterById(newsletterId);
    newsletter.setDeleted(true);
    return newsletterMapper.toNewsletterDto(newsletterRepository.save(newsletter));
  }

  @Override
  @LogAround
  public void hardDeleteNewsletter(final int newsletterId) {
    final Newsletter newsletter = getNewsletterById(newsletterId);
    if (!newsletter.isDeleted()) {
      throw new IllegalArgumentException("Newsletter hard delete action is illegal");
    }
    newsletterRepository.delete(newsletter);
  }

  @Override
  @LogAround
  public NewsletterDto restoreDeletedNewsletter(final int newsletterId) {
    final Newsletter newsletter = getNewsletterById(newsletterId);
    if (!newsletter.isDeleted()) {
      throw new IllegalArgumentException("Restore non deleted newsletter is illegal");
    }
    newsletter.setDeleted(false);
    return newsletterMapper.toNewsletterDto(newsletterRepository.save(newsletter));
  }

  @Override
  @LogAround
  public NewsletterDto publishNewsletter(final int newsletterId) {
    final Newsletter newsletter = getNewsletterById(newsletterId);
    if (newsletter.isDeleted()) {
      throw new IllegalArgumentException("Publish deleted newsletter is not allowed");
    }
    newsletter.setPublished(true);
    sendNewsletterToSubcribers(newsletter);
    return newsletterMapper.toNewsletterDto(newsletterRepository.save(newsletter));
  }

  private void sendNewsletterToSubcribers(final Newsletter newsletter) {
    final int subcriberNum = (int) subscriberRepository.count();
    int pageNumber = 0;
    Page<Subscriber> subcribers;
    while (SUBCRIBER_NUM_SEND_PER_PAGE * pageNumber <= subcriberNum) {
      subcribers =
          subscriberRepository.findAll(
              Pageable.ofSize(SUBCRIBER_NUM_SEND_PER_PAGE).withPage(pageNumber));
      for (Subscriber subscriber : subcribers) {
        mailService.sendNewsletter(subscriber, newsletter);
      }
      pageNumber++;
    }
  }

  private Newsletter getNewsletterById(final int id) {
    return newsletterRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("newsletter", String.valueOf(id)));
  }
}