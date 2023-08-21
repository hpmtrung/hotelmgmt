package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.room.Amenity;

import java.util.Collection;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface AmenityRepository
    extends JpaRepository<Amenity, Integer>, JpaSpecificationExecutor<Amenity> {

  Set<Amenity> findByIdIn(Collection<Integer> ids);

}