package vn.lotusviet.hotelmgmt.core.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class AccountUnActivatedException extends AuthenticationException {

  private static final long serialVersionUID = -3978356522440738670L;

  public AccountUnActivatedException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public AccountUnActivatedException(String msg) {
    super(msg);
  }

  public String getTitle() {
    return this.getMessage();
  }
}