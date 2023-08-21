package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

@Repository
@Transactional(readOnly = true)
public interface SuiteRepository
    extends JpaRepository<Suite, Integer>, SuiteCustomRepository, JpaSpecificationExecutor<Suite> {

  // @Query("select 1 from Suite s where s.suiteType.id = ?1 and s.suiteStyle.id = ?2")
  boolean existsBySuiteStyleIdAndSuiteTypeId(int suiteTypeId, int suiteStyleId);
}