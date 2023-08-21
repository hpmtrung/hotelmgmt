package vn.lotusviet.hotelmgmt.model.entity.tracking;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "BANG_GIA_HANG_PHONG")
@Cacheable
@Immutable
@org.hibernate.annotations.Cache(
    region = SuitePriceHistory.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class SuitePriceHistory implements Serializable {

  public static final String CACHE = "SuitePriceHistory";

  private static final long serialVersionUID = 2702892399235757248L;

  @EmbeddedId private final SuitePriceHistoryId id = new SuitePriceHistoryId();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV, nullable = false)
  private Employee editedBy;

  @Positive
  @Column(name = "GIA")
  private int price;

  @PercentConstaint
  @Column(name = "VAT")
  private int vat;

  public Suite getSuite() {
    return this.id.getSuite();
  }

  public SuitePriceHistory setSuite(Suite suite) {
    this.id.setSuite(suite);
    return this;
  }

  public LocalDate getEditedAt() {
    return this.id.getEditedAt();
  }

  public SuitePriceHistory setEditedAt(LocalDate editedAt) {
    this.id.setEditedAt(editedAt);
    return this;
  }

  public Employee getEditedBy() {
    return editedBy;
  }

  public SuitePriceHistory setEditedBy(final Employee editedBy) {
    this.editedBy = Objects.requireNonNull(editedBy);
    return this;
  }

  public int getPrice() {
    return price;
  }

  public SuitePriceHistory setPrice(final int price) {
    if (price <= 0) throw new IllegalArgumentException();
    this.price = price;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public SuitePriceHistory setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return ("SuitePriceHistory{"
        + "id="
        + id
        + ", editedBy="
        + editedBy.getAccount().getEmail()
        + ", price="
        + price
        + ", vat="
        + vat
        + '}');
  }

  @Embeddable
  public static class SuitePriceHistoryId implements Serializable {

    private static final long serialVersionUID = 6974029470657442535L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Suite.COL_MA_HANG_PHONG, referencedColumnName = "MA_HANG_PHONG")
    private Suite suite;

    @NotNull
    @Column(name = "NGAY_AP_DUNG")
    private LocalDate editedAt;

    public SuitePriceHistoryId() {}

    public SuitePriceHistoryId(Suite suite, LocalDate editedAt) {
      this.suite = suite;
      this.editedAt = editedAt;
    }

    public Suite getSuite() {
      return suite;
    }

    public SuitePriceHistoryId setSuite(Suite suite) {
      this.suite = suite;
      return this;
    }

    public @NotNull LocalDate getEditedAt() {
      return editedAt;
    }

    public SuitePriceHistoryId setEditedAt(@NotNull LocalDate editedAt) {
      this.editedAt = editedAt;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof SuitePriceHistoryId)) return false;
      SuitePriceHistoryId that = (SuitePriceHistoryId) o;
      return Objects.equals(getSuite(), that.getSuite())
          && Objects.equals(getEditedAt(), that.getEditedAt());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getSuite(), getEditedAt());
    }

    @Override
    public String toString() {
      return "SuitePriceHistoryId{" + "suiteId=" + suite.getId() + ", editedAt=" + editedAt + '}';
    }
  }
}