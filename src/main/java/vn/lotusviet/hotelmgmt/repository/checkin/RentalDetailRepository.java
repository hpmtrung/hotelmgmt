package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RentalDetailRepository
    extends JpaRepository<RentalDetail, Long>, RentalDetailCustomRepository {

  @Query("select d from RentalDetail d where d.invoice.id is null and d.room.id = ?1")
  Optional<RentalDetail> findCurrentRentalDetailOfRoomById(int roomId);

  @Query(
      nativeQuery = true,
      value =
          "SELECT CAST(COUNT(*) AS BIT) "
              + "FROM dbo.KHACH_PHONG AS KP "
              + "JOIN ( SELECT MA_CT_PHIEU_THUE FROM dbo.CT_PHIEU_THUE WHERE MA_HOA_DON IS NULL "
              + ") AS CTPT ON CTPT.MA_CT_PHIEU_THUE = KP.MA_CT_PHIEU_THUE WHERE KP.CMND IN ?1")
  boolean isCustomersRented(List<String> personalIds);
}