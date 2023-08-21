package vn.lotusviet.hotelmgmt.model.dto.rental;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalPayingReviewResponseDto {

  private RentalBasicDto rental;
  private EmployeeDto createdBy;
  private List<RentalDetailDto> details;
  private Integer depositAmount;
  private UseMode depositUsedMode = UseMode.ABLE;
  private UseMode rentalDiscountUsedMode = UseMode.ABLE;

  public UseMode getDepositUsedMode() {
    return depositUsedMode;
  }

  public RentalPayingReviewResponseDto setDepositUsedMode(UseMode depositUsedMode) {
    this.depositUsedMode = depositUsedMode;
    return this;
  }

  public UseMode getRentalDiscountUsedMode() {
    return rentalDiscountUsedMode;
  }

  public RentalPayingReviewResponseDto setRentalDiscountUsedMode(UseMode rentalDiscountUsedMode) {
    this.rentalDiscountUsedMode = rentalDiscountUsedMode;
    return this;
  }

  public Integer getDepositAmount() {
    return depositAmount;
  }

  public RentalPayingReviewResponseDto setDepositAmount(Integer depositAmount) {
    this.depositAmount = depositAmount;
    return this;
  }

  public Integer getPromotionDiscountTotal() {
    return details.stream().mapToInt(RentalDetailDto::getTotalDiscountFromPromotion).sum();
  }

  public Integer getServiceUsageTotal() {
    return details.stream().mapToInt(RentalDetailDto::getNotPaidServiceSubTotal).sum();
  }

  public RentalBasicDto getRental() {
    return rental;
  }

  public RentalPayingReviewResponseDto setRental(final RentalBasicDto rental) {
    this.rental = rental;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public RentalPayingReviewResponseDto setCreatedBy(final EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public Integer getTotal() {
    return details.stream().mapToInt(RentalDetailDto::getSubTotalWithNotPaidService).sum();
  }

  public List<RentalDetailDto> getDetails() {
    return details;
  }

  public RentalPayingReviewResponseDto setDetails(final List<RentalDetailDto> details) {
    this.details = details;
    return this;
  }

  @Override
  public String toString() {
    return "RentalPayingReviewResponseDto{"
        + "rental="
        + rental
        + ", depositAmount="
        + depositAmount
        + ", depositUsedMode="
        + depositUsedMode
        + ", rentalDiscountUsedMode="
        + rentalDiscountUsedMode
        + '}';
  }

  public enum UseMode {
    UNABLE,
    ABLE,
    FORCE,
  }
}