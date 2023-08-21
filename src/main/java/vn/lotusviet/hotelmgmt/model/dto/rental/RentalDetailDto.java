package vn.lotusviet.hotelmgmt.model.dto.rental;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.adt.RentalDetailUpdatable;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageDetailDto;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class RentalDetailDto implements RentalDetailUpdatable {

  private Long id;
  private Long invoiceId;
  private RentalBasicDto rental;
  private RoomDto room;
  private LocalDateTime checkInAt;
  private LocalDateTime checkOutAt;
  private Integer discountAmount;
  private Integer extraAmount;
  private int subTotal;
  private Integer vat;
  private Integer roomPrice;
  private Boolean isPaid;
  private List<CustomerDto> customers;
  private List<PromotionDto> promotions;
  private List<ServiceUsageDetailDto> paidServiceUsageDetails = new ArrayList<>();
  private List<ServiceUsageDetailDto> notPaidServiceUsageDetails = new ArrayList<>();
  private Integer totalDiscountFromPromotion;

  public Integer getRoomPrice() {
    return roomPrice;
  }

  public RentalDetailDto setRoomPrice(Integer roomPrice) {
    this.roomPrice = roomPrice;
    return this;
  }

  public Long getInvoiceId() {
    return invoiceId;
  }

  public RentalDetailDto setInvoiceId(Long invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }

  public Integer getNightCount() {
    if (checkInAt == null || checkOutAt == null) return null;
    return Math.max(
        (int) (checkInAt.toLocalDate().until(checkOutAt.toLocalDate(), ChronoUnit.DAYS)), 1);
  }

  public Integer getTotalDiscountFromPromotion() {
    return totalDiscountFromPromotion;
  }

  public RentalDetailDto setTotalDiscountFromPromotion(Integer totalDiscountFromPromotion) {
    this.totalDiscountFromPromotion = totalDiscountFromPromotion;
    return this;
  }

  public Integer getSubTotalWithNotPaidService() {
    if (subTotal == 0) return null;
    return subTotal
        + notPaidServiceUsageDetails.stream().mapToInt(ServiceUsageDetailDto::getTotal).sum();
  }

  public int getNotPaidServiceSubTotal() {
    return this.notPaidServiceUsageDetails.stream().mapToInt(ServiceUsageDetailDto::getTotal).sum();
  }

  public int getPaidServiceSubTotal() {
    return this.paidServiceUsageDetails.stream().mapToInt(ServiceUsageDetailDto::getTotal).sum();
  }

  public void updateSubTotal() {
    if (room == null) throw new NullPointerException();

    this.totalDiscountFromPromotion =
        MathUtil.roundHalfThousand(
            this.promotions.stream()
                .mapToDouble(
                    p ->
                        room.getSuite().getOriginalPrice()
                            * p.getDuration()
                            * (p.getDiscountPercent() / 100.0))
                .sum());
    this.subTotal =
        roomPrice * getNightCount() - discountAmount + extraAmount - totalDiscountFromPromotion;
  }

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public RentalDetailDto setPromotions(List<PromotionDto> promotions) {
    this.promotions = promotions;
    return this;
  }

  @Override
  @JsonIgnore
  public int getSuiteId() {
    return getRoom().getSuite().getId();
  }

  public RentalBasicDto getRental() {
    return rental;
  }

  public void setRental(RentalBasicDto rental) {
    this.rental = rental;
  }

  public Long getId() {
    return id;
  }

  public RentalDetailDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public RoomDto getRoom() {
    return room;
  }

  public RentalDetailDto setRoom(final RoomDto room) {
    this.room = room;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public RentalDetailDto setCheckInAt(final LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public RentalDetailDto setCheckOutAt(final LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Integer getDiscountAmount() {
    return discountAmount;
  }

  public RentalDetailDto setDiscountAmount(final Integer discountAmount) {
    this.discountAmount = discountAmount;
    return this;
  }

  public Integer getExtraAmount() {
    return extraAmount;
  }

  public RentalDetailDto setExtraAmount(final Integer extraAmount) {
    this.extraAmount = extraAmount;
    return this;
  }

  public int getSubTotal() {
    return subTotal;
  }

  public RentalDetailDto setSubTotal(final Integer subTotal) {
    this.subTotal = subTotal;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public RentalDetailDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  public Boolean getIsPaid() {
    return isPaid;
  }

  public RentalDetailDto setIsPaid(final Boolean isPaid) {
    this.isPaid = isPaid;
    return this;
  }

  public List<CustomerDto> getCustomers() {
    return customers;
  }

  public RentalDetailDto setCustomers(final List<CustomerDto> customers) {
    this.customers = customers;
    return this;
  }

  public List<ServiceUsageDetailDto> getPaidServiceUsageDetails() {
    return paidServiceUsageDetails;
  }

  public void setPaidServiceUsageDetails(List<ServiceUsageDetailDto> paidServiceUsageDetails) {
    this.paidServiceUsageDetails = paidServiceUsageDetails;
  }

  public List<ServiceUsageDetailDto> getNotPaidServiceUsageDetails() {
    return notPaidServiceUsageDetails;
  }

  public void setNotPaidServiceUsageDetails(
      List<ServiceUsageDetailDto> notPaidServiceUsageDetails) {
    this.notPaidServiceUsageDetails = notPaidServiceUsageDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RentalDetailDto)) return false;
    RentalDetailDto that = (RentalDetailDto) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "RentalDetailDto{"
        + "id="
        + id
        + ", invoiceId="
        + invoiceId
        + ", rental="
        + rental
        + ", room="
        + room
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", discountAmount="
        + discountAmount
        + ", extraAmount="
        + extraAmount
        + ", subTotal="
        + subTotal
        + ", vat="
        + vat
        + ", roomPrice="
        + roomPrice
        + ", isPaid="
        + isPaid
        + ", customers="
        + customers
        + ", promotions="
        + promotions
        + ", paidServiceUsageDetails="
        + paidServiceUsageDetails
        + ", notPaidServiceUsageDetails="
        + notPaidServiceUsageDetails
        + ", totalDiscountFromPromotion="
        + totalDiscountFromPromotion
        + '}';
  }
}