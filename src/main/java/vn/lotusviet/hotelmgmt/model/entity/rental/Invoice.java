package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.taxcode.ValidTaxCode;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceType;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "HOA_DON")
@Cacheable
@Immutable
@org.hibernate.annotations.Cache(region = Invoice.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class Invoice extends VersionedEntity implements Serializable {

  public static final String CACHE = "cacheInvoice";
  public static final String CACHE_RENTAL_DETAILS = "cacheInvoice.rentalDetails";
  public static final String CACHE_SERVICE_USAGE_DETAILS = "cacheInvoice.serviceUsageDetails";

  public static final String COL_MA_HOA_DON = "MA_HOA_DON";
  public static final String COL_NGAYGIO_LAP = "NGAYGIO_LAP";
  public static final String COL_TONG_TIEN = "TONG_TIEN";
  public static final String COL_HOAN_TIEN_COC = "HOAN_TIEN_COC";
  public static final String COL_GIAM_DOAN = "GIAM_DOAN";
  public static final String COL_TEN_KHACH_HANG = "TEN_KHACH_HANG";
  public static final String COL_MA_SO_THUE = "MA_SO_THUE";

  private static final String ID_GEN = "vat_invoice_id_gen";
  private static final long serialVersionUID = -7002783174933174375L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_HOA_DON"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_HOA_DON)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Rental.COL_MA_PHIEU_THUE, nullable = false)
  private Rental rental;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV, nullable = false)
  private Employee createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = PaymentMethod.COL_MA_PTTT, nullable = false)
  private PaymentMethod paymentMethod;

  @NotNull
  @Column(name = COL_NGAYGIO_LAP)
  private LocalDateTime createdAt;

  @PositiveOrZero
  @Column(name = COL_TONG_TIEN, nullable = false)
  private int total;

  @Column(name = COL_HOAN_TIEN_COC)
  private boolean depositUsed = false;

  @Column(name = COL_GIAM_DOAN)
  private boolean rentalDiscountUsed = false;

  @OneToMany(
      mappedBy = "invoice",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  @org.hibernate.annotations.Cache(
      region = CACHE_RENTAL_DETAILS,
      usage = CacheConcurrencyStrategy.READ_ONLY)
  @OrderBy("id")
  private Set<RentalDetail> rentalDetails = new HashSet<>();

  @OneToMany(
      mappedBy = "invoice",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  @org.hibernate.annotations.Cache(
      region = CACHE_SERVICE_USAGE_DETAILS,
      usage = CacheConcurrencyStrategy.READ_ONLY)
  @OrderBy("createdAt")
  private Set<ServiceUsageDetail> serviceUsageDetails = new HashSet<>();

  @Size(max = 250)
  @Column(name = COL_TEN_KHACH_HANG)
  private String customerName;

  @ValidTaxCode
  @Column(name = COL_MA_SO_THUE)
  private String taxCode;

  public String getCustomerName() {
    return customerName;
  }

  public Invoice setCustomerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public Invoice setTaxCode(String taxCode) {
    this.taxCode = taxCode;
    return this;
  }

  public boolean getDepositUsed() {
    return depositUsed;
  }

  public Invoice setDepositUsed(boolean depositUsed) {
    this.depositUsed = depositUsed;
    return this;
  }

  public boolean getRentalDiscountUsed() {
    return rentalDiscountUsed;
  }

  public Invoice setRentalDiscountUsed(boolean rentalDiscountUsed) {
    this.rentalDiscountUsed = rentalDiscountUsed;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Invoice setId(final Long id) {
    this.id = id;
    return this;
  }

  public Rental getRental() {
    return rental;
  }

  public Invoice setRental(final Rental rental) {
    this.rental = Objects.requireNonNull(rental);
    return this;
  }

  public InvoiceType getInvoiceType() {
    return taxCode != null ? InvoiceType.VAT : InvoiceType.NO_VAT;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public Invoice setCreatedBy(final Employee createdBy) {
    this.createdBy = Objects.requireNonNull(createdBy);
    return this;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public Invoice setPaymentMethod(final PaymentMethod paymentMethod) {
    this.paymentMethod = Objects.requireNonNull(paymentMethod);
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Invoice setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = Objects.requireNonNull(createdAt);
    return this;
  }

  public int getTotal() {
    return total;
  }

  public Invoice setTotal(final int total) {
    this.total = total;
    return this;
  }

  public Set<RentalDetail> getRentalDetails() {
    return rentalDetails;
  }

  public Invoice setRentalDetails(final Set<RentalDetail> rentalDetails) {
    this.rentalDetails = Objects.requireNonNull(rentalDetails);
    return this;
  }

  public Set<ServiceUsageDetail> getServiceUsageDetails() {
    return serviceUsageDetails;
  }

  public Invoice setServiceUsageDetails(final Set<ServiceUsageDetail> serviceUsageDetails) {
    this.serviceUsageDetails = Objects.requireNonNull(serviceUsageDetails);
    return this;
  }

  public Invoice addServiceUsage(final ServiceUsageDetail detail) {
    Objects.requireNonNull(detail);
    if (!serviceUsageDetails.contains(detail)) {
      detail.setInvoice(this);
      this.serviceUsageDetails.add(detail);
    }
    return this;
  }

  public Invoice addRentalDetail(final RentalDetail detail) {
    Objects.requireNonNull(detail);
    if (!rentalDetails.contains(detail)) {
      detail.setInvoice(this);
      this.rentalDetails.add(detail);
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Invoice)) return false;
    Invoice invoice = (Invoice) o;
    return Objects.equals(getId(), invoice.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", total="
        + total
        + ", depositUsed="
        + depositUsed
        + ", rentalDiscountUsed="
        + rentalDiscountUsed
        + ", customerName='"
        + customerName
        + '\''
        + ", taxCode='"
        + taxCode
        + '\''
        + "} "
        + super.toString();
  }
}