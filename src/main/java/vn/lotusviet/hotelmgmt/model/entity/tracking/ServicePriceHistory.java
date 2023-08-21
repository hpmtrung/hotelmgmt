package vn.lotusviet.hotelmgmt.model.entity.tracking;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "BANG_GIA_DICH_VU")
@Cacheable
@org.hibernate.annotations.Cache(
    region = ServicePriceHistory.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class ServicePriceHistory implements Serializable {

  public static final String CACHE = "ServicePriceHistor";

  private static final long serialVersionUID = 1818149551123740209L;

  @EmbeddedId private final ServicePriceHistoryId id = new ServicePriceHistoryId();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Employee.COL_MA_NV, nullable = false)
  private Employee editedBy;

  @PositiveOrZero
  @Column(name = "GIA_DICH_VU")
  private int price;

  @PercentConstaint
  @Column(name = "VAT")
  private int vat;

  public Service getService() {
    return this.id.getService();
  }

  public ServicePriceHistory setService(Service service) {
    this.id.setService(service);
    return this;
  }

  public LocalDate getEditedAt() {
    return this.id.getEditedAt();
  }

  public ServicePriceHistory setEditedAt(LocalDate date) {
    this.id.setEditedAt(date);
    return this;
  }

  public Employee getEditedBy() {
    return editedBy;
  }

  public ServicePriceHistory setEditedBy(final Employee editedBy) {
    this.editedBy = editedBy;
    return this;
  }

  public int getPrice() {
    return price;
  }

  public ServicePriceHistory setPrice(final int price) {
    if (price < 0) throw new IllegalArgumentException();
    this.price = price;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public ServicePriceHistory setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServicePriceHistory)) return false;
    ServicePriceHistory that = (ServicePriceHistory) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return ("ServicePriceHistory{" + "id=" + id + ", price=" + price + ", vat=" + vat + '}');
  }

  @Embeddable
  public static class ServicePriceHistoryId implements Serializable {

    private static final long serialVersionUID = -4373669460412703883L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Service.COL_MA_DICH_VU)
    private Service service;

    @NotNull
    @Column(name = "NGAY_AP_DUNG")
    private LocalDate editedAt;

    public ServicePriceHistoryId() {}

    public ServicePriceHistoryId(Service service, @NotNull LocalDate editedAt) {
      this.service = service;
      this.editedAt = editedAt;
    }

    public Service getService() {
      return service;
    }

    public void setService(Service service) {
      this.service = service;
    }

    public LocalDate getEditedAt() {
      return editedAt;
    }

    public void setEditedAt(LocalDate editedAt) {
      this.editedAt = editedAt;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ServicePriceHistoryId that = (ServicePriceHistoryId) o;
      return service.equals(that.service) && editedAt.equals(that.editedAt);
    }

    @Override
    public int hashCode() {
      return Objects.hash(service, editedAt);
    }

    @Override
    public String toString() {
      return "ServicePriceHistoryId{"
          + "serviceName="
          + service.getName()
          + ", appliedAt="
          + editedAt
          + '}';
    }
  }
}