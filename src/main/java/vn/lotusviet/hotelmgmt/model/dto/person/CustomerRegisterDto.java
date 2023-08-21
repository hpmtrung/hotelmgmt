package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.langkey.LangKeyConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.password.PasswordConstraint;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.FIRST_NAME;
import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type.LAST_NAME;

public class CustomerRegisterDto {

  public static final String DOMAIN_NAME = "customerRegister";

  @EmailConstraint(message = "customerRegister.email.invalid")
  private String email;

  @PersonNameConstraint(type = LAST_NAME, message = "customerRegister.lastName.invalid")
  private String lastName;

  @PersonNameConstraint(type = FIRST_NAME, message = "customerRegister.firstName.invalid")
  private String firstName;

  @PasswordConstraint(message = "customerRegister.password.invalid")
  private String password;

  @LangKeyConstraint private String langKey;

  public String getEmail() {
    return email;
  }

  public CustomerRegisterDto setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public CustomerRegisterDto setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public CustomerRegisterDto setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public CustomerRegisterDto setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getLangKey() {
    return langKey;
  }

  public CustomerRegisterDto setLangKey(String langKey) {
    this.langKey = langKey;
    return this;
  }

  @Override
  public String toString() {
    return "CustomerRegisterDto{"
        + "email='"
        + email
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}