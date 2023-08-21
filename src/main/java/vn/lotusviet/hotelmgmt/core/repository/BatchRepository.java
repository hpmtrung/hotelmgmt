package vn.lotusviet.hotelmgmt.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BatchRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  <S extends T> void saveInBatch(Iterable<S> entities);
}