package vn.lotusviet.hotelmgmt.model.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

import java.time.LocalDate;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {

  private Integer id;
  private String lastName;
  private String firstName;
  private String address;
  private String phone;
  private Boolean isMale;
  private LocalDate birthDate;
  private Integer departmentId;
  private AccountInfo account;

  public Integer getDepartmentId() {
    return departmentId;
  }

  public EmployeeDto setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
    return this;
  }

  public AccountInfo getAccount() {
    return account;
  }

  public EmployeeDto setAccount(AccountInfo account) {
    this.account = account;
    return this;
  }

  @JsonProperty("fullName")
  public String getFullName() {
    return this.lastName + " " + this.firstName;
  }

  public Integer getId() {
    return id;
  }

  public EmployeeDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public EmployeeDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public EmployeeDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public EmployeeDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public EmployeeDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public Boolean getIsMale() {
    return isMale;
  }

  public EmployeeDto setIsMale(final Boolean isMale) {
    this.isMale = isMale;
    return this;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public EmployeeDto setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EmployeeDto)) return false;
    EmployeeDto that = (EmployeeDto) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "EmployeeDto{"
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
        + '}';
  }

  public static class AccountInfo {

    private String email;
    private AuthorityName authority;

    public String getEmail() {
      return email;
    }

    public AccountInfo setEmail(String email) {
      this.email = email;
      return this;
    }

    public AuthorityName getAuthority() {
      return authority;
    }

    public AccountInfo setAuthority(AuthorityName authority) {
      this.authority = authority;
      return this;
    }
  }
}