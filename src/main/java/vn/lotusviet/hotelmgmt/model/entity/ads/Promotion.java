package vn.lotusviet.hotelmgmt.model.entity.ads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.*;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DynamicUpdate
@Entity
@Table(name = "KHUYEN_MAI")
@Cacheable
@Cache(region = Promotion.CACHE, usage = CacheConcurrencyStrategy.READ_WRITE)
public class Promotion extends VersionedEntity implements Serializable {

  public static final String CACHE = "Promotion";
  public static final String CACHE_DETAILS = "Promotion.appliedSuites";
  public static final String COL_MA_KHUYEN_MAI = "MA_KHUYEN_MAI";
  public static final String COL_MA_CODE = "MA_CODE";
  public static final String COL_NGAY_BD = "NGAY_BD";
  public static final String COL_NGAY_KT = "NGAY_KT";
  public static final String COL_TRANG_THAI_HD = "TRANG_THAI_HD";
  private static final long serialVersionUID = -1895304856733021319L;
  private static final String ID_GEN = "promotion_id_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_KHUYEN_MAI"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_KHUYEN_MAI)
  private Long id;

  @NotBlank
  @Size(max = 5)
  @Column(name = COL_MA_CODE, unique = true)
  private String code;

  @NotNull
  @Column(name = COL_NGAY_BD)
  private LocalDate startAt;

  @NotNull
  @Column(name = COL_NGAY_KT)
  private LocalDate endAt;

  @OneToMany(
      mappedBy = "id.promotion",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Cache(region = CACHE_DETAILS, usage = CacheConcurrencyStrategy.READ_ONLY)
  @JsonIgnore
  private Set<PromotionDetail> appliedSuites = new HashSet<>();

  @Column(name = COL_TRANG_THAI_HD)
  private boolean available = true;

  public boolean isAvailable() {
    return available;
  }

  public Promotion setAvailable(boolean available) {
    this.available = available;
    return this;
  }

  public Set<PromotionDetail> getAppliedSuites() {
    return appliedSuites;
  }

  public Promotion setAppliedSuites(Set<PromotionDetail> details) {
    this.appliedSuites = details;
    return this;
  }

  public Promotion addDetail(final PromotionDetail detail) {
    Objects.requireNonNull(detail);
    if (!this.appliedSuites.contains(detail)) {
      detail.setPromotion(this);
      this.appliedSuites.add(detail);
    }
    return this;
  }

  public Long getId() {
    return id;
  }

  public Promotion setId(final Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public Promotion setCode(final String code) {
    if (code == null || code.isBlank()) throw new NullPointerException();
    this.code = code;
    return this;
  }

  public @NotNull LocalDate getStartAt() {
    return startAt;
  }

  public Promotion setStartAt(final LocalDate startAt) {
    if (startAt == null) throw new NullPointerException();
    if (endAt != null && !endAt.isAfter(startAt)) throw new IllegalArgumentException();
    this.startAt = startAt;
    return this;
  }

  public @NotNull LocalDate getEndAt() {
    return endAt;
  }

  public Promotion setEndAt(final LocalDate endAt) {
    if (endAt == null) throw new NullPointerException();
    if (startAt != null && !startAt.isBefore(endAt)) throw new IllegalArgumentException();
    this.endAt = endAt;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Promotion)) return false;
    Promotion promotion = (Promotion) o;
    return Objects.equals(getCode(), promotion.getCode());
  }

  @Override
  public int hashCode() {
    return getCode().hashCode();
  }

  @Override
  public String toString() {
    return "Promotion{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", startAt="
        + startAt
        + ", endAt="
        + endAt
        + ", available="
        + available
        + "} "
        + super.toString();
  }
}