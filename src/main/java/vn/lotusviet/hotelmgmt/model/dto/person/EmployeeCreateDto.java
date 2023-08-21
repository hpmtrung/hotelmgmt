package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EmployeeCreateDto {

  @PersonNameConstraint(type = PersonNameConstraint.Type.FIRST_NAME)
  private String firstName;

  @PersonNameConstraint(type = PersonNameConstraint.Type.LAST_NAME)
  private String lastName;

  @NotNull private Boolean isMale;

  @NotNull private LocalDate birthDate;

  private String address;

  private String phone;

  @NotNull private Integer departmentId;

  private EmployeeAccountCreateDto accountCreate;

  public EmployeeAccountCreateDto getAccountCreate() {
    return accountCreate;
  }

  public EmployeeCreateDto setAccountCreate(EmployeeAccountCreateDto accountCreate) {
    this.accountCreate = accountCreate;
    return this;
  }

  public EmployeeCreateDto setIsMale(final Boolean isMale) {
    this.isMale = isMale;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public EmployeeCreateDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public EmployeeCreateDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public Boolean getIsMale() {
    return isMale;
  }

  public void setMale(Boolean male) {
    isMale = male;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public EmployeeCreateDto setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public EmployeeCreateDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public EmployeeCreateDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public EmployeeCreateDto setDepartmentId(final Integer departmentId) {
    this.departmentId = departmentId;
    return this;
  }

  @Override
  public String toString() {
    return "EmployeeCreateDto{"
        + "firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", isMale="
        + isMale
        + ", birthDate="
        + birthDate
        + ", address='"
        + address
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", departmentId="
        + departmentId
        + '}';
  }
}