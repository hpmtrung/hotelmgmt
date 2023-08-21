package vn.lotusviet.hotelmgmt.model.entity.person;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.address.AddressConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.FIRST_NAME;
import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.LAST_NAME;

@DynamicUpdate
@Entity
@Table(name = "NHAN_VIEN")
@Cacheable
@org.hibernate.annotations.Cache(
    region = Employee.CACHE,
    usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee extends VersionedEntity implements Serializable {

  public static final String CACHE = "Employee";

  public static final String COL_MA_NV = "MA_NV";
  public static final String COL_HO = "HO";
  public static final String COL_TEN = "TEN";
  public static final String COL_DIA_CHI = "DIA_CHI";
  public static final String COL_SDT = "SDT";
  public static final String COL_LA_NAM = "LA_NAM";
  public static final String COL_NGAY_SINH = "NGAY_SINH";

  private static final String ID_GEN = "employee_id_gen";

  private static final long serialVersionUID = -4288309231311242007L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_NHAN_VIEN"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_NV)
  private Integer id;

  @PersonNameConstraint(type = LAST_NAME)
  @Column(name = COL_HO)
  private String lastName;

  @PersonNameConstraint(type = FIRST_NAME)
  @Column(name = COL_TEN)
  private String firstName;

  @AddressConstraint
  @Column(name = COL_DIA_CHI)
  private String address;

  @PhoneConstraint
  @Column(name = COL_SDT)
  private String phone;

  @Column(name = COL_LA_NAM)
  private boolean isMale;

  @NotNull
  @Column(name = COL_NGAY_SINH)
  private LocalDate birthDate;

  // Note: allow null
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = Account.COL_MA_TAI_KHOAN)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Department.COL_MA_BP, nullable = false)
  @JsonManagedReference
  private Department department;

  public String getFullName() {
    return this.lastName + " " + this.firstName;
  }

  public Integer getId() {
    return id;
  }

  public Employee setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Employee setLastName(final String lastName) {
    if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException();
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Employee setFirstName(final String firstName) {
    if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException();
    this.firstName = firstName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public Employee setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Employee setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public boolean getIsMale() {
    return isMale;
  }

  public Employee setIsMale(final boolean isMale) {
    this.isMale = isMale;
    return this;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public Employee setBirthDate(final LocalDate birthDate) {
    this.birthDate = Objects.requireNonNull(birthDate);
    return this;
  }

  public Account getAccount() {
    return account;
  }

  public Employee setAccount(final Account account) {
    this.account = account;
    return this;
  }

  public Department getDepartment() {
    return department;
  }

  public Employee setDepartment(final Department department) {
    this.department = department;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Employee)) return false;
    Employee employee = (Employee) o;
    return getId() != null && Objects.equals(getId(), employee.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Employee{"
        + "id="
        + id
        + ", lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", isMale="
        + isMale
        + ", birthDate="
        + birthDate
        + ", version="
        + version
        + '}';
  }

  public String printIdAndName() {
    return String.format("{id: %d, fullName: %s}", id, lastName);
  }
}