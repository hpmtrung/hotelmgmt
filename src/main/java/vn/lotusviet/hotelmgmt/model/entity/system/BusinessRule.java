package vn.lotusviet.hotelmgmt.model.entity.system;

import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "BUSINESS_RULE")
public class BusinessRule extends VersionedEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private Integer id;

  @Size(min = 1, max = 250)
  @Column(name = "Name")
  private String name;

  @Size(min = 1, max = 200)
  @Column(name = "Value")
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EditedBy", referencedColumnName = Employee.COL_MA_NV, nullable = false)
  private Employee editedBy;

  public String getName() {
    return name;
  }

  public BusinessRule setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public BusinessRule setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public BusinessRule setValue(final String value) {
    this.value = value;
    return this;
  }

  public Employee getEditedBy() {
    return editedBy;
  }

  public BusinessRule setEditedBy(final Employee editedBy) {
    this.editedBy = editedBy;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BusinessRule)) return false;
    BusinessRule that = (BusinessRule) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "BusinessRule{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", value='"
        + value
        + "'} "
        + super.toString();
  }
}