package vn.lotusviet.hotelmgmt.repository.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Integer> {}