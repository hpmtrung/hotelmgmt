package vn.lotusviet.hotelmgmt.model.entity.rental;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "TRANG_THAI_PHIEU_THUE")
@Cacheable
@org.hibernate.annotations.Cache(
    region = RentalStatus.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class RentalStatus implements Serializable {

  public static final String CACHE = "RentalStatus";
  public static final String COL_MA_TRANG_THAI = "MA_TTPT";
  public static final String COL_TEN = "TEN_TTPT";
  public static final String COL_CODE = "CODE";
  private static final long serialVersionUID = -688293658832518035L;

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_TRANG_THAI)
  private Integer id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN, unique = true)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Enumerated(EnumType.STRING)
  @Column(name = COL_CODE, unique = true)
  private RentalStatusCode code;

  public RentalStatusCode getCode() {
    return code;
  }

  public RentalStatus setCode(RentalStatusCode code) {
    this.code = code;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public RentalStatus setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RentalStatus setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RentalStatus)) return false;
    RentalStatus that = (RentalStatus) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "RentalStatus{" + "id=" + id + ", name='" + name + '\'' + ", code=" + code + '}';
  }
}