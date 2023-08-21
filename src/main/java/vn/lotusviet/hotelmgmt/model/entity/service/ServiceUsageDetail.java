package vn.lotusviet.hotelmgmt.model.entity.service;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamicUpdate
@Entity
@Table(name = "SU_DUNG_THEM_DICH_VU")
public class ServiceUsageDetail extends VersionedEntity implements Serializable {

  public static final String COL_MA_SDDV = "MA_SDDV";
  public static final String COL_NGAYGIO_DK = "NGAYGIO_DK";
  public static final String COL_SO_LUONG = "SO_LUONG";
  public static final String COL_GIA_DICH_VU = "GIA_DICH_VU";
  public static final String COL_VAT = "VAT";
  public static final String COL_THANH_TIEN = "THANH_TIEN";
  public static final String COL_DA_THANH_TOAN = "DA_THANH_TOAN";
  private static final long serialVersionUID = -1692070958420751610L;
  private static final String ID_GEN = "serviceUsageDetails_id_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_SDTDV"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_SDDV)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = RentalDetail.COL_MA_CT_PHIEU_THUE, nullable = false)
  private RentalDetail rentalDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Service.COL_MA_DICH_VU, nullable = false)
  private Service service;

  // Khong bat buoc co nhan vien lap phieu
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV)
  private Employee createdBy;

  @Column(name = COL_NGAYGIO_DK)
  private @NotNull LocalDateTime createdAt;

  @Positive
  @Column(name = COL_SO_LUONG)
  private int quantity;

  @Positive
  @Column(name = COL_GIA_DICH_VU)
  private int servicePrice;

  @PercentConstaint
  @Column(name = COL_VAT)
  private int vat;

  @Positive
  @Column(name = COL_THANH_TIEN)
  private int total;

  @Column(name = COL_DA_THANH_TOAN)
  private boolean isPaid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Invoice.COL_MA_HOA_DON)
  private Invoice invoice;

  public Invoice getInvoice() {
    return invoice;
  }

  public ServiceUsageDetail setInvoice(final Invoice invoice) {
    if (this.isPaid && invoice != null) {
      throw new IllegalStateException("Paid service usage detail could not be added to invoice");
    }
    this.invoice = invoice;
    this.isPaid = true;
    return this;
  }

  public Long getId() {
    return id;
  }

  public ServiceUsageDetail setId(final Long id) {
    this.id = id;
    return this;
  }

  public boolean hasInvoice() {
    return this.invoice != null;
  }

  public RentalDetail getRentalDetail() {
    return rentalDetail;
  }

  public ServiceUsageDetail setRentalDetail(final RentalDetail rentalDetail) {
    this.rentalDetail = rentalDetail;
    return this;
  }

  public Service getService() {
    return service;
  }

  public ServiceUsageDetail setService(final Service service) {
    this.service = Objects.requireNonNull(service);
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public ServiceUsageDetail setCreatedBy(final Employee createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public ServiceUsageDetail setCreatedAt(final LocalDateTime registeredAt) {
    this.createdAt = Objects.requireNonNull(registeredAt);
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public ServiceUsageDetail setQuantity(final int quantity) {
    if (quantity <= 0) throw new IllegalArgumentException();
    this.quantity = quantity;
    return this;
  }

  public int getServicePrice() {
    return servicePrice;
  }

  public ServiceUsageDetail setServicePrice(final int servicePrice) {
    if (servicePrice < 0) throw new IllegalArgumentException();
    this.servicePrice = servicePrice;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public ServiceUsageDetail setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public ServiceUsageDetail setTotal(final int total) {
    if (total <= 0) throw new IllegalArgumentException();
    this.total = total;
    return this;
  }

  public boolean getIsPaid() {
    return isPaid;
  }

  public ServiceUsageDetail setIsPaid(final boolean isPaid) {
    this.isPaid = isPaid;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServiceUsageDetail)) return false;
    ServiceUsageDetail that = (ServiceUsageDetail) o;
    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "ServiceUsageDetail{"
        + "id="
        + id
        + ", registerAt="
        + createdAt
        + ", quantity="
        + quantity
        + ", servicePrice="
        + servicePrice
        + ", vat="
        + vat
        + ", total="
        + total
        + ", isPaid="
        + isPaid
        + ", version="
        + version
        + '}';
  }
}