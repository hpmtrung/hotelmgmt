package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RoomRepository
    extends PagingAndSortingRepository<Room, Integer>,
        RoomCustomRepository,
        JpaSpecificationExecutor<Room> {

  @Query("select r from Room r join fetch r.suite where r.id = ?1")
  Optional<Room> findRoomWithSuiteInfoById(int id);

  @Query(
      "select r from Room r join fetch RoomStatus rs on r.status.id=rs.id "
          + "where r.suite.id=?1 and (rs.code='EMPTY' or rs.code='CLEANY') "
          + "order by r.floor asc")
  List<Room> findRoomsCanRentBySuiteId(int suiteId);

  boolean existsByName(String name);

  boolean existsByIdAndSuiteId(int roomId, int suiteId);

  List<Room> findBySuite(Suite suite);

  @Query("select distinct r.floor from Room r")
  List<Integer> getFloorNumbers();
}