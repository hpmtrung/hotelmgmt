package vn.lotusviet.hotelmgmt.model.entity.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DynamicUpdate
@Entity
@Table(name = "LOAI_PHONG")
@Cacheable
@Cache(region = SuiteType.CACHE, usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuiteType extends VersionedEntity implements Serializable {

  public static final String CACHE = "SuiteStyle";
  public static final String CACHE_SUITES = "SuiteStyle.suites";
  public static final String CACHE_AMENITIES = "SuiteStyle.amenities";

  public static final String COL_MA_LOAI_PHONG = "MA_LOAI_PHONG";
  public static final String COL_TEN_LOAI_PHONG = "TEN_LOAI_PHONG";

  private static final long serialVersionUID = -181460764975327490L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_LOAI_PHONG)
  private Integer id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_LOAI_PHONG, unique = true)
  private String name;

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @JoinTable(
      name = "TIEN_NGHI_LOAI_PHONG",
      joinColumns = @JoinColumn(name = SuiteType.COL_MA_LOAI_PHONG),
      inverseJoinColumns = @JoinColumn(name = Amenity.COL_MA_TIEN_NGHI))
  @Cache(region = CACHE_AMENITIES, usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonIgnore
  private Set<Amenity> amenities = new HashSet<>();

  @OneToMany(
      mappedBy = "suiteType",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @Cache(region = CACHE_SUITES, usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonIgnore
  private Set<Suite> suites = new HashSet<>();

  public Integer getId() {
    return id;
  }

  public SuiteType setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public SuiteType setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public Set<Amenity> getAmenities() {
    return amenities;
  }

  public SuiteType setAmenities(final Set<Amenity> amenities) {
    this.amenities = Objects.requireNonNull(amenities);
    return this;
  }

  public Set<Suite> getSuites() {
    return suites;
  }

  public SuiteType setSuites(final Set<Suite> suites) {
    this.suites = Objects.requireNonNull(suites);
    return this;
  }

  public SuiteType addAmenities(Collection<Amenity> amenities) {
    this.amenities.addAll(amenities);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SuiteType)) return false;
    SuiteType suiteType = (SuiteType) o;
    return Objects.equals(getName(), suiteType.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "SuiteType{" + "id=" + id + ", name='" + name + '\'' + ", version='" + version + '}';
  }
}