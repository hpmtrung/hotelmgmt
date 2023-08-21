package vn.lotusviet.hotelmgmt.model.entity.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TRANG_THAI_PHIEU_DAT")
@Immutable
@Cacheable
@Cache(region = ReservationStatus.CACHE, usage = CacheConcurrencyStrategy.READ_ONLY)
public class ReservationStatus implements Serializable {

  public static final String CACHE = "ReservationStatus";
  public static final String COL_MA_TTPD = "MA_TTPD";
  public static final String COL_TEN_TTPD = "TEN_TTPD";
  public static final String COL_CODE = "CODE";

  private static final long serialVersionUID = 8090438477607578722L;

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_TTPD)
  private Integer id;

  @NotBlank
  @Column(name = COL_TEN_TTPD)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = COL_CODE, unique = true)
  private ReservationStatusCode code;

  public ReservationStatusCode getCode() {
    return code;
  }

  public ReservationStatus setCode(final ReservationStatusCode code) {
    this.code = code;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public ReservationStatus setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ReservationStatus setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReservationStatus)) return false;
    ReservationStatus that = (ReservationStatus) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "ReservationStatus{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}