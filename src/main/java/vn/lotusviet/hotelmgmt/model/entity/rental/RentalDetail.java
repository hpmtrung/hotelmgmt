package vn.lotusviet.hotelmgmt.model.entity.rental;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.adt.RentalDetailUpdatable;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@DynamicUpdate
@Entity
@Table(name = "CT_PHIEU_THUE")
public class RentalDetail extends VersionedEntity implements Serializable, RentalDetailUpdatable {

  public static final String COL_MA_CT_PHIEU_THUE = "MA_CT_PHIEU_THUE";
  public static final String COL_GIA_PHONG = "GIA_PHONG";
  public static final String COL_NGAYGIO_CHECKIN = "NGAYGIO_CHECKIN";
  public static final String COL_NGAYGIO_CHECKOUT = "NGAYGIO_CHECKOUT";
  public static final String COL_SO_TIEN_GIAM = "TIEN_GIAM";
  public static final String COL_TIEN_PHU_THU = "TIEN_PHU_THU";
  public static final String COL_THANH_TIEN = "THANH_TIEN";
  public static final String COL_VAT = "VAT";
  private static final String ID_GEN = "rentalDetail_id_gen";
  private static final long serialVersionUID = -4680414281040813928L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_CT_PHIEU_THUE"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_CT_PHIEU_THUE)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Room.COL_MA_PHONG, nullable = false)
  private Room room;

  @Column(name = Room.COL_MA_PHONG, insertable = false, updatable = false)
  private Integer roomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Rental.COL_MA_PHIEU_THUE, nullable = false)
  private Rental rental;

  @Positive
  @Column(name = COL_GIA_PHONG)
  private int roomPrice;

  @NotNull
  @Column(name = COL_NGAYGIO_CHECKIN)
  private LocalDateTime checkInAt;

  @NotNull
  @Column(name = COL_NGAYGIO_CHECKOUT)
  private LocalDateTime checkOutAt;

  @PositiveOrZero
  @Column(name = COL_SO_TIEN_GIAM)
  private int discountAmount;

  @PositiveOrZero
  @Column(name = COL_TIEN_PHU_THU)
  private int extraAmount;

  /**
   * Gia tri thanh tien bao gom khuyen mai, khong tinh dich vu Thanh tien = so dem * tien phong +
   * discount - extra
   */
  @Positive
  @Column(name = COL_THANH_TIEN)
  private int subTotal;

  @PercentConstaint
  @Column(name = COL_VAT)
  private int vat;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "KHACH_PHONG",
      joinColumns = @JoinColumn(name = RentalDetail.COL_MA_CT_PHIEU_THUE),
      inverseJoinColumns = @JoinColumn(name = Customer.COL_CMND))
  private Set<Customer> customers = new HashSet<>();

  @OneToMany(
      mappedBy = "rentalDetail",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @JsonManagedReference
  @OrderBy("createdAt")
  private Set<ServiceUsageDetail> serviceUsageDetails = new LinkedHashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Invoice.COL_MA_HOA_DON)
  private Invoice invoice;

  @Transient private List<PromotionDto> promotions = new ArrayList<>();

  public Integer getRoomId() {
    return roomId;
  }

  public RentalDetail setRoomId(Integer roomId) {
    this.roomId = roomId;
    return this;
  }

  @JsonIgnore
  public Long getInvoiceId() {
    if (getInvoice() == null) return null;
    return getInvoice().getId();
  }

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public RentalDetail setPromotions(List<PromotionDto> promotions) {
    this.promotions = Objects.requireNonNull(promotions);
    return this;
  }

  @Override
  @JsonIgnore
  public int getSuiteId() {
    return getRoom().getSuiteId();
  }

  public void updateSubTotal() {
    this.subTotal =
        roomPrice * getNightCount()
            - discountAmount
            + extraAmount
            - getTotalDiscountFromPromotion();
  }

  public List<ServiceUsageDetail> getServiceUsageDetailsOfInvoice() {
    if (getInvoice() == null) throw new IllegalStateException();
    return serviceUsageDetails.stream()
        .filter(ServiceUsageDetail::hasInvoice)
        .collect(Collectors.toList());
  }

  public List<ServiceUsageDetail> getPaidServiceUsageDetails() {
    return serviceUsageDetails.stream()
        .filter(ServiceUsageDetail::getIsPaid)
        .collect(Collectors.toList());
  }

  public List<ServiceUsageDetail> getNotPaidServiceUsageDetails() {
    return getServiceUsageDetails().stream()
        .filter(s -> !s.getIsPaid())
        .collect(Collectors.toList());
  }

  public int getNightCount() {
    return Math.max(
        (int) (checkInAt.toLocalDate().until(checkOutAt.toLocalDate(), ChronoUnit.DAYS)), 1);
  }

  public int getTotalDiscountFromPromotion() {
    return MathUtil.roundHalfThousand(
        this.promotions.stream()
            .mapToDouble(p -> roomPrice * p.getDuration() * (p.getDiscountPercent() / 100.0))
            .sum());
  }

  public Invoice getInvoice() {
    return invoice;
  }

  public RentalDetail setInvoice(Invoice invoice) {
    this.invoice = invoice;
    return this;
  }

  public Long getId() {
    return id;
  }

  public RentalDetail setId(final Long id) {
    this.id = id;
    return this;
  }

  public Room getRoom() {
    return room;
  }

  public RentalDetail setRoom(final Room room) {
    if (room == null) throw new NullPointerException();
    this.room = room;
    return this;
  }

  public Rental getRental() {
    return rental;
  }

  public RentalDetail setRental(final Rental rental) {
    this.rental = rental;
    return this;
  }

  public int getRoomPrice() {
    return roomPrice;
  }

  public RentalDetail setRoomPrice(final int roomPrice) {
    if (roomPrice <= 0) throw new IllegalArgumentException();
    this.roomPrice = roomPrice;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public RentalDetail setCheckInAt(final LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public RentalDetail setCheckOutAt(final LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public RentalDetail setDiscountAmount(final int discount) {
    if (discount < 0) throw new IllegalArgumentException();
    this.discountAmount = discount;
    return this;
  }

  public int getExtraAmount() {
    return extraAmount;
  }

  public RentalDetail setExtraAmount(final int extraAmount) {
    if (extraAmount < 0) throw new IllegalArgumentException();
    this.extraAmount = extraAmount;
    return this;
  }

  public int getSubTotal() {
    return subTotal;
  }

  public RentalDetail setSubTotal(final int total) {
    if (total <= 0) throw new IllegalArgumentException();
    this.subTotal = total;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public RentalDetail setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  public boolean getIsPaid() {
    return this.invoice != null;
  }

  public Set<Customer> getCustomers() {
    return customers;
  }

  public RentalDetail setCustomers(final Set<Customer> customers) {
    this.customers = customers;
    return this;
  }

  public RentalDetail addCustomer(Customer customer) {
    if (customer == null) throw new NullPointerException();
    this.customers.add(customer);
    return this;
  }

  public RentalDetail removeCustomer(Customer customer) {
    if (customer == null) throw new NullPointerException();
    this.customers.remove(customer);
    return this;
  }

  public Set<ServiceUsageDetail> getServiceUsageDetails() {
    return serviceUsageDetails;
  }

  public RentalDetail setServiceUsageDetails(final Set<ServiceUsageDetail> serviceUsages) {
    this.serviceUsageDetails = serviceUsages;
    return this;
  }

  public RentalDetail addServiceUsage(ServiceUsageDetail serviceUsage) {
    if (serviceUsage == null) throw new NullPointerException();
    serviceUsage.setRentalDetail(this);
    serviceUsageDetails.add(serviceUsage);
    return this;
  }

  public RentalDetail removeServiceUsage(ServiceUsageDetail serviceUsage) {
    if (serviceUsage == null) throw new NullPointerException();
    serviceUsage.setRentalDetail(null);
    serviceUsageDetails.remove(serviceUsage);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RentalDetail)) return false;
    RentalDetail that = (RentalDetail) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "RentalDetail{"
        + "id="
        + id
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
        + ", vat="
        + vat
        + ", promotions="
        + promotions
        + "} ";
  }
}