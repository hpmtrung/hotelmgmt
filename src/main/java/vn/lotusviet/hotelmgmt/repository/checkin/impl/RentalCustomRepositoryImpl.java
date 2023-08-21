package vn.lotusviet.hotelmgmt.repository.checkin.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalBasicDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalFilterOption;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalCustomRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Transactional(readOnly = true)
public class RentalCustomRepositoryImpl extends AbstractRepository
    implements RentalCustomRepository {

  private static RentalBasicDto getDtoFromRS(final ResultSet rs) throws SQLException {
    return new RentalBasicDto()
        .setId(rs.getLong(Rental.COL_MA_PHIEU_THUE))
        .setCreatedAt(getValueAsLocalDateTime(rs, Rental.COL_NGAYGIO_LAP))
        .setCheckInAt(getValueAsLocalDateTime(rs, Rental.COL_NGAYGIO_CHECKIN))
        .setCheckOutAt(getValueAsLocalDateTime(rs, Rental.COL_NGAYGIO_CHECKOUT))
        .setStatus(RentalStatusCode.valueOf(rs.getString(RentalStatus.COL_CODE)))
        .setOwner(
            new CustomerDto()
                .setPersonalId(rs.getString(Customer.COL_CMND))
                .setLastName(rs.getString("KH_HO"))
                .setFirstName(rs.getString("KH_TEN")))
        .setCreatedBy(
            new EmployeeDto()
                .setId(rs.getInt(Employee.COL_MA_NV))
                .setLastName("NV_HO")
                .setFirstName("NV_TEN"));
  }

  @Override
  public Page<RentalBasicDto> findByFilterOption(
      final RentalFilterOption rentalFilterOption, final Pageable pageable) {
    final ResultMap outputs =
        execProc(
            "Usp_LayDsPhieuThueTheoNgay",
            (RowMapper<RentalBasicDto>) (rs, rowNum) -> getDtoFromRS(rs),
            Map.of(
                "NGAY_BD",
                rentalFilterOption.getStartDate(),
                "NGAY_KT",
                rentalFilterOption.getEndDate(),
                "CODES_TRANG_THAI_PT",
                rentalFilterOption.getStatuses(),
                "CMND",
                rentalFilterOption.getPersonalId(),
                PAGE_INDEX,
                pageable.getPageNumber(),
                PAGE_SIZE,
                pageable.getPageSize()));
    return outputs.getAsPage(pageable);
  }
}