package vn.lotusviet.hotelmgmt.model.entity.service;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "LOAI_DICH_VU")
@Immutable
@Cacheable
@org.hibernate.annotations.Cache(
    region = ServiceType.CACHE,
    usage = CacheConcurrencyStrategy.READ_ONLY)
public class ServiceType extends VersionedEntity implements Serializable {

  public static final String CACHE = "ServiceType";
  public static final String CACHE_SERVICES = "ServiceType.services";

  public static final String COL_MA_LOAI_DV = "MA_LOAI_DV";
  public static final String COL_TEN_LOAI_DV = "TEN_LOAI_DV";

  private static final long serialVersionUID = -7209025040443615201L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_LOAI_DV)
  private Integer id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_LOAI_DV, unique = true)
  private String name;

  @OneToMany(
      mappedBy = "serviceType",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @org.hibernate.annotations.Cache(
      region = CACHE_SERVICES,
      usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonManagedReference
  private Set<Service> services = new HashSet<>();

  public Integer getId() {
    return id;
  }

  public ServiceType setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ServiceType setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public Set<Service> getServices() {
    return services;
  }

  public ServiceType setServices(final Set<Service> services) {
    this.services = Objects.requireNonNull(services);
    return this;
  }

  public ServiceType addService(Service service) {
    if (service == null) throw new NullPointerException();
    if (!services.contains(service)) {
      service.setServiceType(this);
      services.add(service);
    }
    return this;
  }

  public ServiceType removeService(Service service) {
    if (service == null) throw new NullPointerException();
    if (this.services.contains(service)) {
      service.setServiceType(null);
      services.remove(service);
    }
    return this;
  }

  public ServiceType removeAllService() {
    Iterator<Service> it = this.services.iterator();
    while (it.hasNext()) {
      it.next().setServiceType(null);
      it.remove();
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServiceType)) return false;
    ServiceType that = (ServiceType) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "ServiceType{" + "id=" + id + ", name='" + name + '\'' + ", version=" + version + '}';
  }
}