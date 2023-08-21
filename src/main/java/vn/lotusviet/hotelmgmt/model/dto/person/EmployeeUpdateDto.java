package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;

import java.time.LocalDate;

public class EmployeeUpdateDto {

  private String firstName;
  private String lastName;
  private String address;
  private Boolean isMale;
  @PhoneConstraint private String phone;
  private LocalDate birthDate;
  private Integer departmentId;
  private EmployeeAccountCreateDto accountCreate;
  private EmployeeAccountUpdateDto accountUpdate;

  public Boolean getIsMale() {
    return isMale;
  }

  public void setMale(Boolean male) {
    isMale = male;
  }

  public String getFirstName() {
    return firstName;
  }

  public EmployeeUpdateDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public EmployeeUpdateDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public EmployeeUpdateDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public EmployeeUpdateDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public EmployeeUpdateDto setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public EmployeeUpdateDto setDepartmentId(final Integer departmentId) {
    this.departmentId = departmentId;
    return this;
  }

  public EmployeeAccountCreateDto getAccountCreate() {
    return accountCreate;
  }

  public EmployeeUpdateDto setAccountCreate(final EmployeeAccountCreateDto accountCreate) {
    this.accountCreate = accountCreate;
    return this;
  }

  public EmployeeAccountUpdateDto getAccountUpdate() {
    return accountUpdate;
  }

  public EmployeeUpdateDto setAccountUpdate(final EmployeeAccountUpdateDto accountUpdate) {
    this.accountUpdate = accountUpdate;
    return this;
  }

  @Override
  public String toString() {
    return "EmployeeUpdateDto{"
        + "firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", birthdate='"
        + birthDate
        + '\''
        + ", departmentId="
        + departmentId
        + ", accountCreate="
        + accountCreate
        + ", accountUpdate="
        + accountUpdate
        + '}';
  }
}