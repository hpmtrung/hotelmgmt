package vn.lotusviet.hotelmgmt.model.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.FIRST_NAME;
import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.LAST_NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {

  @NotBlank
  @Size(min = 12, max = 12)
  private String personalId;

  @PersonNameConstraint(type = LAST_NAME)
  private String lastName;

  @PersonNameConstraint(type = FIRST_NAME)
  private String firstName;

  private String address;

  @PhoneConstraint private String phone;

  @Email private String email;

  private String taxCode;

  private LocalDateTime modifiedAt;

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public CustomerDto setModifiedAt(LocalDateTime modifiedAt) {
    this.modifiedAt = modifiedAt;
    return this;
  }

  public String getFullName() {
    return this.lastName + " " + this.firstName;
  }

  public String getEmail() {
    return email;
  }

  public CustomerDto setEmail(final String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public CustomerDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public String getPersonalId() {
    return personalId;
  }

  public CustomerDto setPersonalId(final String personalId) {
    this.personalId = personalId;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public CustomerDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public CustomerDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public CustomerDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public CustomerDto setTaxCode(final String taxCode) {
    this.taxCode = taxCode;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CustomerDto)) return false;
    CustomerDto that = (CustomerDto) o;
    return getPersonalId().equals(that.getPersonalId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPersonalId());
  }

  @Override
  public String toString() {
    return "CustomerDto{"
        + "personalId='"
        + personalId
        + '\''
        + ", email='"
        + email
        + '\''
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
        + ", modifiedAt='"
        + modifiedAt
        + ", taxCode='"
        + taxCode
        + '\''
        + '}';
  }
}