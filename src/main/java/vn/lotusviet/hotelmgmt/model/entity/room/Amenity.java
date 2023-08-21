package vn.lotusviet.hotelmgmt.model.entity.room;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@DynamicUpdate
@Entity
@Table(name = "TIEN_NGHI")
@Cacheable
@org.hibernate.annotations.Cache(
    region = Amenity.CACHE,
    usage = CacheConcurrencyStrategy.READ_WRITE)
public class Amenity extends VersionedEntity implements Serializable {

  public static final String CACHE = "Amentity";
  public static final String COL_MA_TIEN_NGHI = "MA_TIEN_NGHI";
  public static final String COL_TEN = "TEN";
  private static final long serialVersionUID = -6443692771663971631L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_TIEN_NGHI)
  private int id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN, unique = true)
  private String name;

  public int getId() {
    return id;
  }

  public Amenity setId(final int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Amenity setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Amenity)) return false;
    Amenity amenity = (Amenity) o;
    return Objects.equals(getName(), amenity.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Amenity{" + "id=" + id + ", name='" + name + '\'' + ", version=" + version + '}';
  }
}