package vn.lotusviet.hotelmgmt.repository.ads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;

@Repository
public interface PromotionRepository
    extends JpaRepository<Promotion, Long>,
        PromotionCustomRepository,
        JpaSpecificationExecutor<Promotion> {

  boolean existsByCode(String code);
}