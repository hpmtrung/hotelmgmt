package vn.lotusviet.hotelmgmt.model.dto.person;

import vn.lotusviet.hotelmgmt.core.annotation.validation.password.PasswordConstraint;

public class PasswordChangeDto {

  @PasswordConstraint private String currentPassword;

  @PasswordConstraint private String newPassword;

  public String getCurrentPassword() {
    return currentPassword;
  }

  public PasswordChangeDto setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
    return this;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public PasswordChangeDto setNewPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  @Override
  public String toString() {
    return "PasswordChangeDto{"
        + "currentPassword='"
        + currentPassword
        + '\''
        + ", newPassword='"
        + newPassword
        + '\''
        + '}';
  }
}