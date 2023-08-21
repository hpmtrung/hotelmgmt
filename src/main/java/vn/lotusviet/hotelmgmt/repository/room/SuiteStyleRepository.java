package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SuiteStyleRepository extends JpaRepository<SuiteStyle, Integer> {

  String FIND_BY_NAME_CACHE_NAME = "findSuiteStyleByNameCache";
  String EXIST_BY_NAME_CACHE_NAME = "existSuiteStyleByNameCache";

  @Cacheable(value = FIND_BY_NAME_CACHE_NAME)
  Optional<SuiteStyle> findByName(String name);

  @Cacheable(value = EXIST_BY_NAME_CACHE_NAME)
  boolean existsByName(String name);
}