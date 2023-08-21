package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Parameter;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceType;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "HOA_DON_GOP")
@Immutable
@Cacheable
@org.hibernate.annotations.Cache(
    region = MergedInvoice.CACHE_MERGED_INVOICE_BY_ID,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class MergedInvoice implements Serializable {

  public static final String CACHE_MERGED_INVOICE_BY_ID = "cacheMergedInvoiceById";
  public static final String CACHE_MERGED_INVOICE_RENTALS = "cacheMergedInvoiceRentals";

  public static final String COL_MA_HOA_DON = "MA_HOA_DON_GOP";
  public static final String COL_NGAYGIO_LAP = "NGAYGIO_LAP";
  public static final String COL_TONG_TIEN = "TONG_TIEN";

  private static final String ID_GEN = "merged_invoice_id_gen";

  private static final long serialVersionUID = -1248286716127933018L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_HOA_DON_GOP"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_HOA_DON)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV, nullable = false)
  private Employee createdBy;

  @NotNull
  @Column(name = COL_NGAYGIO_LAP)
  private LocalDateTime createdAt;

  @PositiveOrZero
  @Column(name = COL_TONG_TIEN, nullable = false)
  private long total;

  @OneToMany(
      mappedBy = "mergedInvoice",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @org.hibernate.annotations.Cache(
      region = CACHE_MERGED_INVOICE_RENTALS,
      usage = CacheConcurrencyStrategy.READ_ONLY)
  @OrderBy("createdAt")
  private Set<Rental> rentals = new HashSet<>();

  public InvoiceType getInvoiceType() {
    return InvoiceType.MERGED;
  }

  public Long getId() {
    return id;
  }

  public MergedInvoice setId(Long id) {
    this.id = id;
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public MergedInvoice setCreatedBy(Employee createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @NotNull
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public MergedInvoice setCreatedAt(@NotNull LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public long getTotal() {
    return total;
  }

  public MergedInvoice setTotal(long total) {
    this.total = total;
    return this;
  }

  public Set<Rental> getRentals() {
    return rentals;
  }

  public MergedInvoice setRentals(Set<Rental> rentals) {
    this.rentals = rentals;
    return this;
  }

  public void addRentals(final Collection<Rental> rentals) {
    for (final Rental rental : rentals) {
      if (!this.rentals.contains(rental)) {
        this.rentals.add(rental);
        rental.setMergedInvoice(this);
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MergedInvoice)) return false;
    MergedInvoice that = (MergedInvoice) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass());
  }

  @Override
  public String toString() {
    return "MergedInvoice{" + "id=" + id + ", createdAt=" + createdAt + ", total=" + total + '}';
  }
}