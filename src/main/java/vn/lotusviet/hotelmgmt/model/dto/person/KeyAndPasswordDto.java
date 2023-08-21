package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.password.PasswordConstraint;

import javax.validation.constraints.NotBlank;

public class KeyAndPasswordDto {

  @NotBlank private String key;

  @PasswordConstraint private String newPassword;

  public String getKey() {
    return key;
  }

  public KeyAndPasswordDto setKey(String key) {
    this.key = key;
    return this;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public KeyAndPasswordDto setNewPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  @Override
  public String toString() {
    return "KeyAndPasswordDto{" + "key='" + key + '\'' + ", newPassword='" + newPassword + '\'' + '}';
  }
}