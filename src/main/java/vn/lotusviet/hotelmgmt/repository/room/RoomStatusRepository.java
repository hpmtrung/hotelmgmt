package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;

import java.util.Optional;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Integer> {

  Optional<RoomStatus> findByCode(RoomStatusCode code);

}