package vn.lotusviet.hotelmgmt.model.entity.person;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/** An authority (a security role) used by Spring Security. */
@Entity
@Table(name = "NHOM_QUYEN")
@Cacheable
@org.hibernate.annotations.Cache(
    region = Authority.CACHE_BY_ENTITY_ID,
    usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
public class Authority implements Serializable {

  public static final String CACHE_BY_ENTITY_ID = "cacheById";

  public static final String COL_MA_NQ = "MA_NQ";
  public static final String COL_TEN_NQ = "TEN_NQ";

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_NQ)
  private Integer id;

  @NotBlank
  @Enumerated(EnumType.STRING)
  @Column(name = COL_TEN_NQ, unique = true)
  private AuthorityName name;

  public Integer getId() {
    return id;
  }

  public Authority setId(final Integer id) {
    this.id = id;
    return this;
  }

  public AuthorityName getName() {
    return name;
  }

  public Authority setName(final AuthorityName name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Authority)) return false;
    Authority authority = (Authority) o;
    return Objects.equals(getName(), authority.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Authority{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}