package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

public class EmployeeAccountUpdateDto {

  private AuthorityName authority;

  @EmailConstraint(nullable = true)
  private String email;

  public AuthorityName getAuthority() {
    return authority;
  }

  public EmployeeAccountUpdateDto setAuthority(final AuthorityName authority) {
    this.authority = authority;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public EmployeeAccountUpdateDto setEmail(final String email) {
    this.email = email;
    return this;
  }

  @Override
  public String toString() {
    return "EmployeeAccountUpdateDto{"
        + "authority="
        + authority
        + ", email='"
        + email
        + '\''
        + '}';
  }
}