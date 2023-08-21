package vn.lotusviet.hotelmgmt.core.exception.auth;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;

public class SessionUnavailableException extends SessionAuthenticationException {
  
  private static final long serialVersionUID = -4397380746400820083L;

  public SessionUnavailableException() {
    super("Unauthorized client.");
  }
  
}