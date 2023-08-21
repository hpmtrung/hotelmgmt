package vn.lotusviet.hotelmgmt.model.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.core.annotation.validation.address.AddressConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

import javax.validation.constraints.NotBlank;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.FIRST_NAME;
import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.LAST_NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

  private Long id;

  @PersonNameConstraint(type = FIRST_NAME)
  private String firstName;

  @PersonNameConstraint(type = LAST_NAME)
  private String lastName;

  @EmailConstraint @NotBlank private String email;

  @AddressConstraint private String address;

  @PhoneConstraint private String phone;

  private String langKey;

  private boolean activated;

  private String imageURL;

  private AuthorityName authority;

  public Long getId() {
    return id;
  }

  public AccountDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public AccountDto setFirstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public AccountDto setLastName(final String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public AccountDto setEmail(final String email) {
    this.email = email;
    return this;
  }

  public String getLangKey() {
    return langKey;
  }

  public AccountDto setLangKey(final String langKey) {
    this.langKey = langKey;
    return this;
  }

  public boolean isActivated() {
    return activated;
  }

  public AccountDto setActivated(final boolean activated) {
    this.activated = activated;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public AccountDto setImageURL(final String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public AuthorityName getAuthority() {
    return authority;
  }

  public AccountDto setAuthority(final AuthorityName authority) {
    this.authority = authority;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public AccountDto setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public AccountDto setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  @Override
  public String toString() {
    return "AccountDto{"
        + "id="
        + id
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", address='"
        + address
        + '\''
        + ", langKey='"
        + langKey
        + '\''
        + ", activated="
        + activated
        + ", imageURL='"
        + imageURL
        + '\''
        + ", authority='"
        + authority
        + '\''
        + '}';
  }
}