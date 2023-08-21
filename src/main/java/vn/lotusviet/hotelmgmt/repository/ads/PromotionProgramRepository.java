package vn.lotusviet.hotelmgmt.repository.ads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionProgram;

@Repository
@Transactional(readOnly = true)
public interface PromotionProgramRepository
    extends JpaRepository<PromotionProgram, Integer>, JpaSpecificationExecutor<PromotionProgram> {}