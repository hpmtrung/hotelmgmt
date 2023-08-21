package vn.lotusviet.hotelmgmt.model.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDetailDto {

  private long rentalDetailId;
  private String roomName;
  private int roomPrice;
  private LocalDateTime checkInAt;
  private LocalDateTime checkOutAt;
  private int discountAmount;
  private int extraAmount;
  private int subTotal;
  private List<CustomerDto> customers;
  private List<InvoiceServiceUsageDetailDto> serviceUsageDetails;
  private List<PromotionDto> promotions;
  private int totalDiscountFromPromotion;

  public int getTotalServiceUsageDetails() {
    return serviceUsageDetails.stream().mapToInt(InvoiceServiceUsageDetailDto::getTotal).sum();
  }

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public InvoiceDetailDto setPromotions(List<PromotionDto> promotions) {
    this.promotions = promotions;
    return this;
  }

  @JsonIgnore
  public boolean getHasPromotions() {
    return promotions != null && !promotions.isEmpty();
  }

  @JsonIgnore
  public boolean getHasServiceUsageDetails() {
    return serviceUsageDetails != null && !serviceUsageDetails.isEmpty();
  }

  public int getTotalDiscountFromPromotion() {
    return totalDiscountFromPromotion;
  }

  public InvoiceDetailDto setTotalDiscountFromPromotion(int totalDiscountFromPromotion) {
    this.totalDiscountFromPromotion = totalDiscountFromPromotion;
    return this;
  }

  @JsonIgnore
  public JRDataSource getServiceUsageDetailsJRDataSource() {
    return new JRBeanCollectionDataSource(this.serviceUsageDetails);
  }

  @JsonIgnore
  public JRDataSource getPromotionsJRDataSource() {
    return new JRBeanCollectionDataSource(this.promotions);
  }

  @JsonIgnore
  public String getCheckInAtString() {
    return DatetimeUtil.formatLocalDateTime(checkInAt);
  }

  @JsonIgnore
  public String getCheckOutAtString() {
    return DatetimeUtil.formatLocalDateTime(checkOutAt);
  }

  public String getRoomName() {
    return roomName;
  }

  public InvoiceDetailDto setRoomName(String roomName) {
    this.roomName = roomName;
    return this;
  }

  public int getRoomPrice() {
    return roomPrice;
  }

  public InvoiceDetailDto setRoomPrice(int roomPrice) {
    this.roomPrice = roomPrice;
    return this;
  }

  public int getSubTotalWithNotPaidService() {
    return subTotal
        + serviceUsageDetails.stream().mapToInt(InvoiceServiceUsageDetailDto::getTotal).sum();
  }

  public long getRentalDetailId() {
    return rentalDetailId;
  }

  public void setRentalDetailId(long rentalDetailId) {
    this.rentalDetailId = rentalDetailId;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public void setCheckInAt(LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public void setCheckOutAt(LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(int discountAmount) {
    this.discountAmount = discountAmount;
  }

  public int getExtraAmount() {
    return extraAmount;
  }

  public void setExtraAmount(int extraAmount) {
    this.extraAmount = extraAmount;
  }

  public int getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(int subTotal) {
    this.subTotal = subTotal;
  }

  public List<CustomerDto> getCustomers() {
    return customers;
  }

  public void setCustomers(List<CustomerDto> customers) {
    this.customers = customers;
  }

  public List<InvoiceServiceUsageDetailDto> getServiceUsageDetails() {
    return serviceUsageDetails;
  }

  public void setServiceUsageDetails(List<InvoiceServiceUsageDetailDto> serviceUsageDetails) {
    this.serviceUsageDetails = serviceUsageDetails;
  }

  @Override
  public String toString() {
    return "InvoiceDetailDto{"
        + "rentalDetailId="
        + rentalDetailId
        + ", roomName='"
        + roomName
        + '\''
        + ", roomPrice="
        + roomPrice
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
        + ", customers="
        + customers
        + ", promotions="
        + promotions
        + ", serviceUsageDetails="
        + serviceUsageDetails
        + ", totalDiscountFromPromotion="
        + totalDiscountFromPromotion
        + '}';
  }
}