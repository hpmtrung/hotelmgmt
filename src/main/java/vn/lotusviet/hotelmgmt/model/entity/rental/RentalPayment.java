package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment.CACHE_BY_ID;

@Entity
@Table(name = "PHIEU_CHI")
@Immutable
@Cacheable
@org.hibernate.annotations.Cache(region = CACHE_BY_ID, usage = CacheConcurrencyStrategy.READ_ONLY)
public class RentalPayment implements Serializable {

  public static final String CACHE_BY_ID = "RentalPaymentCacheById";

  private static final long serialVersionUID = -1174761923107062843L;

  private static final String ID_GEN = "rental_payment_id_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(
            name = "sequence_name",
            value = "ID_SEQ_PHIEU_CHI"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
        @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = "MA_PHIEU_CHI")
  private Long id;

  @Column(name = "NGAYGIO_LAP")
  @NotNull
  private LocalDateTime createdAt;

  @Column(name = "TIEN_CHI")
  @Positive
  private int money;

  @Column(name = "NOI_DUNG")
  @Size(max = 200)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Rental.COL_MA_PHIEU_THUE)
  private Rental rental;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV)
  private Employee createdBy;

  public Long getId() {
    return id;
  }

  public RentalPayment setId(Long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RentalPayment setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public int getMoney() {
    return money;
  }

  public RentalPayment setMoney(int money) {
    this.money = money;
    return this;
  }

  public String getContent() {
    return content;
  }

  public RentalPayment setContent(String content) {
    this.content = content;
    return this;
  }

  public Rental getRental() {
    return rental;
  }

  public RentalPayment setRental(Rental rental) {
    this.rental = rental;
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public RentalPayment setCreatedBy(Employee createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RentalPayment)) return false;
    RentalPayment that = (RentalPayment) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "RentalPayment{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", money="
        + money
        + ", content='"
        + content
        + '\''
        + '}';
  }
}