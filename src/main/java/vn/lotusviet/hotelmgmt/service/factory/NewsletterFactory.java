package vn.lotusviet.hotelmgmt.service.factory;

import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterUpsertDto;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;

public interface NewsletterFactory {

  Subscriber createNewSubcriber(String email);

  Newsletter createNewNewsletter(NewsletterUpsertDto newsletterUpsertDto);

}