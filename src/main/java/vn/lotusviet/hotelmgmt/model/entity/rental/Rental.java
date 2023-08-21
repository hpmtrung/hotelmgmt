package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@DynamicUpdate
@Entity
@Table(name = "PHIEU_THUE")
public class Rental extends VersionedEntity implements Serializable {

  public static final String COL_MA_PHIEU_THUE = "MA_PHIEU_THUE";
  public static final String COL_NGAYGIO_LAP = "NGAYGIO_LAP";
  public static final String COL_NGAYGIO_CHECKIN = "NGAYGIO_CHECKIN";
  public static final String COL_NGAYGIO_CHECKOUT = "NGAYGIO_CHECKOUT";
  public static final String COL_SO_TIEN_GIAM = "TIEN_GIAM";

  private static final String ID_GEN = "rental_id_gen";

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_PHIEU_THUE"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_PHIEU_THUE)
  private Long id;

  @NotNull
  @Column(name = COL_NGAYGIO_LAP)
  private LocalDateTime createdAt;

  @NotNull
  @Column(name = COL_NGAYGIO_CHECKIN)
  private LocalDateTime checkInAt;

  @NotNull
  @Column(name = COL_NGAYGIO_CHECKOUT)
  private LocalDateTime checkOutAt;

  @PositiveOrZero
  @Column(name = COL_SO_TIEN_GIAM)
  private int discountAmount = 0;

  // Phieu thue co the khong co ma phieu dat neu khach thue truc tiep
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = Reservation.COL_MA_PHIEU_DAT)
  private Reservation reservation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV, nullable = false)
  private Employee createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Customer.COL_CMND, nullable = false)
  private Customer owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = MergedInvoice.COL_MA_HOA_DON)
  private MergedInvoice mergedInvoice;

  @Size(min = 1)
  @OneToMany(
      mappedBy = "rental",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<RentalDetail> details = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = RentalStatus.COL_MA_TRANG_THAI, nullable = false)
  private RentalStatus status;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rental")
  @OrderBy("createdAt")
  private Set<Invoice> invoices = new HashSet<>();

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      mappedBy = "rental")
  @OrderBy("createdAt")
  private Set<RentalPayment> rentalPayments;

  public Set<Invoice> getInvoices() {
    return invoices;
  }

  public Rental setInvoices(Set<Invoice> invoices) {
    this.invoices = invoices;
    return this;
  }

  public Set<RentalPayment> getRentalPayments() {
    return rentalPayments;
  }

  public Rental setRentalPayments(Set<RentalPayment> rentalPayments) {
    this.rentalPayments = rentalPayments;
    return this;
  }

  public MergedInvoice getMergedInvoice() {
    return mergedInvoice;
  }

  public Rental setMergedInvoice(MergedInvoice mergedInvoice) {
    this.mergedInvoice = mergedInvoice;
    return this;
  }

  public RentalStatus getStatus() {
    return status;
  }

  public Rental setStatus(RentalStatus status) {
    this.status = status;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Rental setId(final Long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Rental setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public Rental setCheckInAt(final LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public Rental setCheckOutAt(final LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public Rental setDiscountAmount(final int discount) {
    if (discount < 0) throw new IllegalArgumentException();
    this.discountAmount = discount;
    return this;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public Rental setReservation(final Reservation reservation) {
    this.reservation = reservation;
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public Rental setCreatedBy(final Employee createdBy) {
    if (createdBy == null) throw new NullPointerException();
    this.createdBy = createdBy;
    return this;
  }

  public Customer getOwner() {
    return owner;
  }

  public Rental setOwner(final Customer customer) {
    if (customer == null) throw new NullPointerException();
    this.owner = customer;
    return this;
  }

  public List<RentalDetail> getDetails() {
    return details;
  }

  public Rental setDetails(final List<RentalDetail> details) {
    this.details = details;
    return this;
  }

  public Rental addDetail(final RentalDetail detail) {
    if (detail == null) throw new NullPointerException();
    if (detail.getId() == null || !details.contains(detail)) {
      detail.setRental(this);
      details.add(detail);
    }
    return this;
  }

  public Rental removeDetail(RentalDetail detail) {
    if (detail == null) throw new NullPointerException();
    if (details.contains(detail)) {
      detail.setRental(null);
      details.remove(detail);
    }
    return this;
  }

  public Rental addInvoice(Invoice invoice) {
    invoice.setRental(this);
    this.invoices.add(invoice);
    return this;
  }

  public Rental addPayment(RentalPayment payment) {
    Objects.requireNonNull(payment);
    if (!this.rentalPayments.contains(payment)) {
      this.rentalPayments.add(payment);
      payment.setRental(this);
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Rental)) return false;
    Rental that = (Rental) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Rental{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", discountAmount="
        + discountAmount
        + "} "
        + super.toString();
  }
}