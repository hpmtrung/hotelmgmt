package vn.lotusviet.hotelmgmt.model.entity.paying;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "PHUONG_THUC_THANH_TOAN")
@Cacheable
@Cache(region = PaymentMethod.CACHE, usage = CacheConcurrencyStrategy.READ_ONLY)
public class PaymentMethod implements Serializable {

  public static final String CACHE = "PaymentMethod";

  public static final String COL_MA_PTTT = "MA_PTTT";
  public static final String COL_TEN_PTTT = "TEN_PTTT";
  public static final String COL_CODE = "CODE";

  private static final long serialVersionUID = -4511188140909147475L;

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_PTTT)
  private Integer id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_PTTT, unique = true)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Enumerated(EnumType.STRING)
  @Column(name = COL_CODE, unique = true)
  private PaymentMethodCode code;

  public PaymentMethod setCode(final PaymentMethodCode code) {
    this.code = code;
    return this;
  }

  public PaymentMethodCode getCode() {
    return code;
  }

  public Integer getId() {
    return id;
  }

  public PaymentMethod setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public PaymentMethod setName(String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PaymentMethod)) return false;
    PaymentMethod that = (PaymentMethod) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "PaymentMethod{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", code=" + code +
        '}';
  }
}