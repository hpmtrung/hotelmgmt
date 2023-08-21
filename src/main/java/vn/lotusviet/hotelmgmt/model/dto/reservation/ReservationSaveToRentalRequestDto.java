package vn.lotusviet.hotelmgmt.model.dto.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationSaveToRentalRequestDto {

  protected int rentalDiscountAmount;

  @NotNull
  @Size(min = 1)
  protected List<SuiteMapping> suiteMappings;

  @JsonIgnore
  public boolean isCustomersAllocatedValid() {
    final Set<String> personalIdSet = new HashSet<>();
    for (var suiteMapping : suiteMappings) {
      for (var roomMapping : suiteMapping.getRoomMappings()) {
        for (var customer : roomMapping.getCustomers()) {
          if (personalIdSet.contains(customer.getPersonalId())) return false;
          personalIdSet.add(customer.getPersonalId());
        }
      }
    }
    return true;
  }

  @JsonIgnore
  public List<CustomerDto> getAllCustomers() {
    List<CustomerDto> customers = new ArrayList<>();
    suiteMappings.stream()
        .map(SuiteMapping::getRoomMappings)
        .forEach(
            roomMappings ->
                roomMappings.stream().map(RoomMapping::getCustomers).forEach(customers::addAll));
    return customers;
  }

  public int getRentalDiscountAmount() {
    return rentalDiscountAmount;
  }

  public ReservationSaveToRentalRequestDto setRentalDiscountAmount(final int rentalDiscountAmount) {
    this.rentalDiscountAmount = rentalDiscountAmount;
    return this;
  }

  public List<SuiteMapping> getSuiteMappings() {
    return suiteMappings;
  }

  public ReservationSaveToRentalRequestDto setSuiteMappings(
      final List<SuiteMapping> suiteMappings) {
    this.suiteMappings = suiteMappings;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationSaveToRentalRequestDto{"
        + "discountGroup="
        + rentalDiscountAmount
        + ", details="
        + suiteMappings
        + '}';
  }

  public static class SuiteMapping implements Serializable {
    private static final long serialVersionUID = 3563397430864109713L;
    @NotNull private Integer suiteId;

    @Size(min = 1)
    @NotNull
    private List<RoomMapping> roomMappings;

    public Integer getSuiteId() {
      return suiteId;
    }

    public SuiteMapping setSuiteId(final Integer suiteId) {
      this.suiteId = suiteId;
      return this;
    }

    public List<RoomMapping> getRoomMappings() {
      return roomMappings;
    }

    public SuiteMapping setRoomMappings(final List<RoomMapping> roomMappings) {
      this.roomMappings = roomMappings;
      return this;
    }

    @Override
    public String toString() {
      return "SuiteMapping{" + "suiteId=" + suiteId + ", roomMappings=" + roomMappings + '}';
    }
  }

  public static class RoomMapping {

    @NotNull private Integer roomId;

    @NotNull
    @Size(min = 1)
    private List<CustomerDto> customers;

    public Integer getRoomId() {
      return roomId;
    }

    public RoomMapping setRoomId(final Integer roomId) {
      this.roomId = roomId;
      return this;
    }

    public List<CustomerDto> getCustomers() {
      return customers;
    }

    public RoomMapping setCustomers(final List<CustomerDto> customers) {
      this.customers = customers;
      return this;
    }

    @Override
    public String toString() {
      return "RoomMapping{" + "roomId=" + roomId + ", customers=" + customers + '}';
    }
  }
}