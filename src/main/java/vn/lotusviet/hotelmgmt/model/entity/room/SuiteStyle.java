package vn.lotusviet.hotelmgmt.model.entity.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DynamicUpdate
@Entity
@Table(name = "KIEU_PHONG")
@Cacheable
@Cache(region = SuiteStyle.CACHE, usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuiteStyle extends VersionedEntity implements Serializable {

  public static final String CACHE = "SuiteStyle";
  public static final String CACHE_SUITES = "SuiteStyle.suites";
  public static final String COL_MA_KIEU_PHONG = "MA_KIEU_PHONG";
  public static final String COL_TEN_KIEU_PHONG = "TEN_KIEU_PHONG";
  public static final String COL_SO_NGUOI_LON = "SO_NGUOI_LON";
  private static final long serialVersionUID = -48209910981190638L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_KIEU_PHONG)
  private int id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_KIEU_PHONG, unique = true)
  private String name;

  @Positive
  @Column(name = COL_SO_NGUOI_LON)
  private int maxOccupation;

  @OneToMany(
      mappedBy = "suiteStyle",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @Cache(region = CACHE_SUITES, usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonIgnore
  private Set<Suite> suites = new HashSet<>();

  public int getId() {
    return id;
  }

  public SuiteStyle setId(final int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public SuiteStyle setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public int getMaxOccupation() {
    return maxOccupation;
  }

  public SuiteStyle setMaxOccupation(final int maxOccupation) {
    if (maxOccupation <= 0) throw new IllegalArgumentException();
    this.maxOccupation = maxOccupation;
    return this;
  }

  public Set<Suite> getSuites() {
    return suites;
  }

  public SuiteStyle setSuites(final Set<Suite> suites) {
    this.suites = Objects.requireNonNull(suites);
    return this;
  }

  public void addSuite(Suite suite) {
    if (suite == null) throw new NullPointerException();
    if (!suites.contains(suite)) {
      suite.setSuiteStyle(this);
      suites.add(suite);
    }
  }

  public void removeSuite(Suite suite) {
    if (suite == null) throw new NullPointerException();
    if (suites.contains(suite)) {
      suite.setSuiteStyle(null);
      suites.remove(suite);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SuiteStyle)) return false;
    SuiteStyle that = (SuiteStyle) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "SuiteStyle{"
        + "id="
        + id
        + ", name='"
        + name
        + ", maxOccupation: "
        + maxOccupation
        + ", version: "
        + version
        + '}';
  }
}