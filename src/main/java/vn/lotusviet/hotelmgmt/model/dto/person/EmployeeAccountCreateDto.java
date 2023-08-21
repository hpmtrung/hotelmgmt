package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

import javax.validation.constraints.NotNull;

public class EmployeeAccountCreateDto {

  @NotNull private AuthorityName authority;
  @EmailConstraint private String email;

  public AuthorityName getAuthority() {
    return authority;
  }

  public EmployeeAccountCreateDto setAuthority(final AuthorityName authority) {
    this.authority = authority;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public EmployeeAccountCreateDto setEmail(final String email) {
    this.email = email;
    return this;
  }

  @Override
  public String toString() {
    return "EmployeeAccountCreateDto{"
        + "authority="
        + authority
        + ", email='"
        + email
        + '\''
        + '}';
  }
}