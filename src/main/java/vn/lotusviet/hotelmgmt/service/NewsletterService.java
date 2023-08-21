package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterUpsertDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.SubscriberDto;

import java.util.List;

public interface NewsletterService {

  SubscriberDto saveNewSubscriber(String email);

  void deleteSubscriber(String email);

  SubscriberDto verifiedSubscriber(String email);

  List<NewsletterDto> getNewsletters();

  NewsletterDto getDetailOfNewsletter(int newsletterId);

  NewsletterDto saveNewNewsletter(NewsletterUpsertDto newsletterUpsertDto);

  NewsletterDto updateNewsletter(int newsletterId, NewsletterUpsertDto newsletterUpsertDto);

  NewsletterDto softDeleteNewsletter(int newsletterId);

  void hardDeleteNewsletter(int newsletterId);

  NewsletterDto restoreDeletedNewsletter(int newsletterId);

  NewsletterDto publishNewsletter(int newsletterId);
}