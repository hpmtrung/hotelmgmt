package vn.lotusviet.hotelmgmt.core.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AthenticationProcessingException extends AuthenticationException {
  private static final long serialVersionUID = -800018501862168440L;

  public OAuth2AthenticationProcessingException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public OAuth2AthenticationProcessingException(String msg) {
    super(msg);
  }
}