package vn.lotusviet.hotelmgmt.model.entity.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TRANG_THAI_PHONG")
@Immutable
@Cacheable
@org.hibernate.annotations.Cache(
    region = RoomStatus.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class RoomStatus implements Serializable {

  public static final String CACHE = "RoomStatus";

  public static final String COL_MA_TTP = "MA_TRANG_THAI_PHONG";
  public static final String COL_CODE = "CODE";
  public static final String COL_TEN_TTP = "TEN_TRANG_THAI_PHONG";

  private static final long serialVersionUID = 3770598023442050170L;

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_TTP)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = COL_CODE, unique = true)
  private RoomStatusCode code;

  @NotBlank
  @Size(max = 50)
  @Column(name = COL_TEN_TTP, unique = true)
  private String name;

  public RoomStatusCode getCode() {
    return code;
  }

  public RoomStatus setCode(final RoomStatusCode code) {
    this.code = code;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public RoomStatus setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RoomStatus setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoomStatus)) return false;
    RoomStatus that = (RoomStatus) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "RoomStatus{" + "id=" + id + ", code='" + code + '\'' + ", name='" + name + '\'' + '}';
  }
}