package vn.lotusviet.hotelmgmt.model.entity.person;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BO_PHAN")
@Cacheable
@org.hibernate.annotations.Cache(
    region = Department.CACHE,
    usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department extends VersionedEntity implements Serializable {

  public static final String CACHE = "Department";
  public static final String CACHE_EMPLOYEES = "Department.employees";

  public static final String COL_MA_BP = "MA_BP";
  public static final String COL_TEN_BP = "TEN_BP";
  public static final String COL_HOTLINE = "HOTLINE";

  private static final long serialVersionUID = 8352283075478893000L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_BP)
  private Integer id;

  @NotBlank
  @Size(max = 100)
  @Column(name = COL_TEN_BP, unique = true)
  private String name;

  @PhoneConstraint
  @Column(name = COL_HOTLINE)
  private String hotline;

  @OneToMany(
      mappedBy = "department",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @org.hibernate.annotations.Cache(
      region = CACHE_EMPLOYEES,
      usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonManagedReference
  private Set<Employee> employees = new HashSet<>();

  public Integer getId() {
    return id;
  }

  public Department setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Department setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public String getHotline() {
    return hotline;
  }

  public Department setHotline(final String hotline) {
    if (hotline == null || hotline.isBlank()) throw new IllegalArgumentException();
    this.hotline = hotline;
    return this;
  }

  public Set<Employee> getEmployees() {
    return employees;
  }

  public Department setEmployees(final Set<Employee> employees) {
    this.employees = Objects.requireNonNull(employees);
    return this;
  }

  public Department addEmployee(Employee employee) {
    if (employee == null) throw new NullPointerException();
    if (!employees.contains(employee)) {
      employees.add(employee);
      employee.setDepartment(this);
    }
    return this;
  }

  public Department removeEmployee(Employee employee) {
    if (employee == null) throw new NullPointerException();
    if (employees.contains(employee)) {
      employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Department)) return false;
    Department that = (Department) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Department{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", hotline='"
        + hotline
        + '\''
        + '}';
  }
}