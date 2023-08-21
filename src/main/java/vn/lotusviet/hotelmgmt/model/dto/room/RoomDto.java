package vn.lotusviet.hotelmgmt.model.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDetailDto;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class RoomDto {

  private Integer id;
  private String name;
  private RoomStatusCode statusCode;
  private SuiteDto suite;
  private Integer floor;
  private RentalDetailDto rentalDetail;
  private Boolean available;

  public Boolean getAvailable() {
    return available;
  }

  public RoomDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public RentalDetailDto getRentalDetail() {
    return rentalDetail;
  }

  public RoomDto setRentalDetail(final RentalDetailDto rentalDetail) {
    this.rentalDetail = rentalDetail;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public RoomDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RoomDto setName(final String name) {
    this.name = name;
    return this;
  }

  public @NotNull RoomStatusCode getStatusCode() {
    return statusCode;
  }

  public RoomDto setStatusCode(final @NotNull RoomStatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public SuiteDto getSuite() {
    return suite;
  }

  public RoomDto setSuite(final SuiteDto suite) {
    this.suite = suite;
    return this;
  }

  public Integer getFloor() {
    return floor;
  }

  public RoomDto setFloor(final Integer floor) {
    this.floor = floor;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoomDto)) return false;
    RoomDto roomDto = (RoomDto) o;
    return Objects.equals(getId(), roomDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "RoomDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", statusId="
        + statusCode
        + ", suite="
        + suite
        + ", floor="
        + floor
        + ", available="
        + available
        + ", rentalDetail="
        + rentalDetail
        + '}';
  }
}