package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.address.AddressConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.FIRST_NAME;
import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.LAST_NAME;

/**
 * Dto used to update account profile (user with any authorities) This contains only fields allowed
 * to update by himself
 */
public class AccountProfileUpdateDto {

  @PersonNameConstraint(type = FIRST_NAME, message = "firstName.invalid")
  private String firstName;

  @PersonNameConstraint(type = LAST_NAME, message = "lastName.invalid")
  private String lastName;

  @AddressConstraint(message = "address.invalid")
  private String address;

  @PhoneConstraint(message = "phone.invalid")
  private String phone;

  public String getFirstName() {
    return firstName;
  }

  public AccountProfileUpdateDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public AccountProfileUpdateDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public AccountProfileUpdateDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public AccountProfileUpdateDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  @Override
  public String toString() {
    return "AccountProfileUpdateDto{"
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
        + '}';
  }
}