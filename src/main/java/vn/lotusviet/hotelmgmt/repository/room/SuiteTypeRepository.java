package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;

import java.util.List;

@Repository
@Cacheable(SuiteType.CACHE)
@Transactional(readOnly = true)
public interface SuiteTypeRepository extends JpaRepository<SuiteType, Integer> {

  boolean existsByName(String name);

}