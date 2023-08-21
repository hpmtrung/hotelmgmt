package vn.lotusviet.hotelmgmt.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

@Transactional(propagation = Propagation.NEVER)
public class BatchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements BatchRepository<T, ID> {

  private static final Logger log = LoggerFactory.getLogger(BatchRepositoryImpl.class);

  private final EntityManager entityManager;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize;

  public BatchRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  public <S extends T> void saveInBatch(Iterable<S> entities) {

    if (entities == null) {
      throw new IllegalArgumentException("The given Iterable of entities cannot be null!");
    }

    EntityTransaction entityTransaction = entityManager.getTransaction();
    try {
      entityTransaction.begin();
      int i = 0;
      for (S entity : entities) {
        if (i % batchSize == 0 && i > 0) {
          log.info("Flushing the EntityManager containing {} entities ...", batchSize);
          entityTransaction.commit();
          entityTransaction.begin();
          entityManager.clear();
        }
        entityManager.persist(entity);
        i++;
      }
      log.info("Flushing the remaining entities ...");
      entityTransaction.commit();
    } catch (RuntimeException e) {
      if (entityTransaction.isActive()) {
        entityTransaction.rollback();
      }
      throw e;
    } finally {
      entityManager.close();
    }
  }
}