package vn.lotusviet.hotelmgmt.model.entity.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedEntityGraph(
    name = Reservation.RESERVATION_DETAILS_FETCH_GRAPH,
    attributeNodes = {
      @NamedAttributeNode("details"),
    })
@DynamicUpdate
@Entity
@Table(name = "PHIEU_DAT")
public class Reservation extends VersionedEntity implements Serializable {

  public static final String RESERVATION_DETAILS_FETCH_GRAPH = "Reservation.Graph.Details";

  public static final String COL_MA_PHIEU_DAT = "MA_PHIEU_DAT";
  public static final String COL_NGAYGIO_LAP = "NGAYGIO_LAP";
  public static final String COL_YEU_CAU_DAC_BIET = "YEU_CAU_DAC_BIET";
  public static final String COL_TIEN_DAT_COC = "TIEN_DAT_COC";
  public static final String COL_NGAY_CHECKIN = "NGAY_CHECKIN";
  public static final String COL_NGAY_CHECKOUT = "NGAY_CHECKOUT";
  public static final String COL_TIEN_PHAT = "TIEN_PHAT";
  public static final String COL_MA_NV_XAC_NHAN = "MA_NV_XAC_NHAN";
  public static final String COL_THOI_GIAN_CHO = "THOI_GIAN_CHO";
  public static final String COL_TIEN_HOAN_LAI = "TIEN_HOAN_LAI";

  private static final long serialVersionUID = 999143806732123313L;

  private static final String ID_GEN = "reservation_id_gen";
  private static final String COL_THANH_TIEN = "THANH_TIEN";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_PHIEU_DAT"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_PHIEU_DAT)
  private Long id;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE})
  @JoinColumn(name = Customer.COL_CMND)
  private Customer owner;

  @Column(name = COL_NGAYGIO_LAP)
  private Instant createdAt;

  @NotNull
  @Column(name = COL_NGAY_CHECKIN)
  private LocalDate checkInAt;

  @NotNull
  @Column(name = COL_NGAY_CHECKOUT)
  private LocalDate checkOutAt;

  @Size(max = 300)
  @Column(name = COL_YEU_CAU_DAC_BIET)
  private String specialRequirements;

  @Positive
  @Column(name = COL_TIEN_DAT_COC)
  private int depositAmount;

  @Transient @PercentConstaint private Integer depositPercent;

  @PositiveOrZero
  @Column(name = COL_TIEN_PHAT)
  private int fee = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = ReservationStatus.COL_MA_TTPD, nullable = false)
  private ReservationStatus status;

  // Note: allow null
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = COL_MA_NV_XAC_NHAN)
  private Employee createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = PaymentMethod.COL_MA_PTTT)
  private PaymentMethod paymentMethod;

  @Positive
  @Column(name = COL_THANH_TIEN)
  private int total;

  @Positive
  @Column(name = COL_THOI_GIAN_CHO)
  private int timeElapsedMins;

  @PositiveOrZero
  @Column(name = COL_TIEN_HOAN_LAI)
  private int refund;

  @OneToMany(
      mappedBy = "id.reservation",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<ReservationDetail> details = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reservation")
  @JsonIgnore
  private Rental rental;

  public int getRefund() {
    return refund;
  }

  public Reservation setRefund(int refund) {
    this.refund = refund;
    return this;
  }

  public Rental getRental() {
    return rental;
  }

  public Reservation setRental(Rental rental) {
    this.rental = rental;
    return this;
  }

  public Integer getDepositPercent() {
    return depositPercent;
  }

  public Reservation setDepositPercent(Integer depositPercent) {
    this.depositPercent = depositPercent;
    return this;
  }

  public int getNightCount() {
    if (checkInAt == null || checkOutAt == null) throw new IllegalStateException();
    return Math.max((int) this.getCheckInAt().until(this.getCheckOutAt(), ChronoUnit.DAYS), 1);
  }

  public int getTimeElapsedMins() {
    return timeElapsedMins;
  }

  public Reservation setTimeElapsedMins(int timeElapsedMins) {
    this.timeElapsedMins = timeElapsedMins;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public Reservation setTotal(int total) {
    this.total = total;
    return this;
  }

  public Reservation updateTotal() {
    this.total = details.stream().mapToInt(ReservationDetail::getSubTotal).sum() - fee;
    if (this.depositPercent != null && this.depositAmount == 0) {
      this.depositAmount = MathUtil.roundHalfThousand(this.total * this.depositPercent / 100.0);
    }
    return this;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public Reservation setPaymentMethod(final PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  public LocalDate getCheckOutAt() {
    return checkOutAt;
  }

  public Reservation setCheckOutAt(LocalDate checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Reservation setId(final Long id) {
    this.id = id;
    return this;
  }

  public Customer getOwner() {
    return owner;
  }

  public Reservation setOwner(final Customer customer) {
    this.owner = customer;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Reservation setCreatedAt(final Instant createdAt) {
    // Note: allow set created at equals check in at
    if (checkInAt != null && checkInAt.isBefore(createdAt.atZone(ZoneOffset.UTC).toLocalDate()))
      throw new IllegalArgumentException();
    this.createdAt = createdAt;
    return this;
  }

  public String getSpecialRequirements() {
    return specialRequirements;
  }

  public Reservation setSpecialRequirements(final String specialRequirements) {
    this.specialRequirements = specialRequirements;
    return this;
  }

  public int getDepositAmount() {
    return depositAmount;
  }

  public Reservation setDepositAmount(final int deposit) {
    if (deposit < 0) throw new IllegalArgumentException();
    this.depositAmount = deposit;
    return this;
  }

  public LocalDate getCheckInAt() {
    return checkInAt;
  }

  public Reservation setCheckInAt(LocalDate checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public int getFee() {
    return fee;
  }

  public Reservation setFee(final int fee) {
    if (fee < 0) throw new IllegalArgumentException();
    this.fee = fee;
    return this;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public Reservation setStatus(final ReservationStatus status) {
    if (status == null) throw new NullPointerException();
    this.status = status;
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public Reservation setCreatedBy(final Employee createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public List<ReservationDetail> getDetails() {
    return details;
  }

  public Reservation setDetails(final List<ReservationDetail> details) {
    this.details = details;
    return this;
  }

  public Reservation addDetail(ReservationDetail detail) {
    if (detail == null) throw new NullPointerException();
    if (!details.contains(detail)) {
      detail.setReservation(this);
      details.add(detail);
    }
    return this;
  }

  public Reservation removeDetail(ReservationDetail detail) {
    if (detail == null) throw new NullPointerException();
    details.remove(detail);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Reservation)) return false;
    Reservation that = (Reservation) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Reservation{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", specialRequirements='"
        + specialRequirements
        + '\''
        + ", depositAmount="
        + depositAmount
        + ", depositPercent="
        + depositPercent
        + ", fee="
        + fee
        + ", total="
        + total
        + ", timeElapsedMins="
        + timeElapsedMins
        + ", refund="
        + refund
        + "} "
        + super.toString();
  }
}