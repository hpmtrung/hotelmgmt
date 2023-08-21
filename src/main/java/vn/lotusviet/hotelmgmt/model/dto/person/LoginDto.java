package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.password.PasswordConstraint;

import javax.validation.constraints.NotNull;

public class LoginDto {

  @EmailConstraint private String email;

  @PasswordConstraint private String password;

  private boolean rememberMe = false;

  @NotNull private App app;

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  @Override
  public String toString() {
    return "LoginDto{"
        + "email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", rememberMe="
        + rememberMe
        + ", app="
        + app
        + '}';
  }

  public enum App {
    CUSTOMER,
    PORTAL
  }
}