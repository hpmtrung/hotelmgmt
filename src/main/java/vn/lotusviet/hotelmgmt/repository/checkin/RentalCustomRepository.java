package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalBasicDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalFilterOption;

public interface RentalCustomRepository {
  Page<RentalBasicDto> findByFilterOption(RentalFilterOption rentalFilterOption, Pageable pageable);
}