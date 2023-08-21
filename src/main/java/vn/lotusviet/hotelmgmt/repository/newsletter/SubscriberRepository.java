package vn.lotusviet.hotelmgmt.repository.newsletter;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;

@Transactional(readOnly = true)
public interface SubscriberRepository extends PagingAndSortingRepository<Subscriber, String> {

  boolean existsByEmail(String email);

  boolean existsByEmailAndVerifiedIsTrue(String email);

}