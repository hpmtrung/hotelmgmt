package vn.lotusviet.hotelmgmt.model.entity.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@DynamicUpdate
@Entity
@Table(name = "DICH_VU")
@Cacheable
@org.hibernate.annotations.Cache(
    region = Service.CACHE,
    usage = CacheConcurrencyStrategy.READ_WRITE)
public class Service extends VersionedEntity implements Serializable {

  public static final String CACHE = "Service";
  public static final String COL_MA_DICH_VU = "MA_DICH_VU";
  public static final String COL_TEN_DV = "TEN_DICH_VU";
  public static final String COL_DON_GIA = "DON_GIA";
  public static final String COL_HOAT_DONG = "HOAT_DONG";
  public static final String COL_VAT = "VAT";
  private static final long serialVersionUID = 6097158302339387584L;
  private static final String ID_GEN = "service_id_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_DICH_VU"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_DICH_VU)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = ServiceType.COL_MA_LOAI_DV, nullable = false)
  @JsonBackReference
  private ServiceType serviceType;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_DV, unique = true)
  private String name;

  @Column(name = COL_DON_GIA)
  private Integer unitPrice;

  @Column(name = COL_HOAT_DONG)
  private boolean isActive = true;

  @PercentConstaint
  @Column(name = COL_VAT)
  private int vat;

  public Integer getId() {
    return id;
  }

  public Service setId(final Integer id) {
    this.id = id;
    return this;
  }

  public ServiceType getServiceType() {
    return serviceType;
  }

  public Service setServiceType(final ServiceType serviceType) {
    this.serviceType = serviceType;
    return this;
  }

  public String getName() {
    return name;
  }

  public Service setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public Service setUnitPrice(final Integer price) {
    if (price != null && price < 0) throw new IllegalArgumentException();
    this.unitPrice = price;
    return this;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public Service setIsActive(final boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public Service setActive(boolean active) {
    isActive = active;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public Service setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Service)) return false;
    Service service = (Service) o;
    return Objects.equals(getName(), service.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Service{"
        + "id="
        + id
        + ", serviceType="
        + (serviceType != null ? serviceType.getName() : "NULL")
        + ", name='"
        + name
        + '\''
        + ", unitPrice="
        + unitPrice
        + ", isActive="
        + isActive
        + ", vat="
        + vat
        + ", version="
        + version
        + '}';
  }
}