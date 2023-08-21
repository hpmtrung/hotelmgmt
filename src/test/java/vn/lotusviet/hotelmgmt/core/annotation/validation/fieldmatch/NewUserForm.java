package vn.lotusviet.hotelmgmt.core.annotation.validation.fieldmatch;

@FieldsValueMatch.List({
    @FieldsValueMatch(field = "password", fieldMatch = "verifyPassword", message = "Password don't match!"),
    @FieldsValueMatch(field = "email", fieldMatch = "verifyEmail", message = "Email don't match!"),
})
public class NewUserForm {
  private String email;
  private String verifyEmail;
  private String password;
  private String verifyPassword;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getVerifyEmail() {
    return verifyEmail;
  }

  public void setVerifyEmail(String verifyEmail) {
    this.verifyEmail = verifyEmail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getVerifyPassword() {
    return verifyPassword;
  }

  public void setVerifyPassword(String verifyPassword) {
    this.verifyPassword = verifyPassword;
  }
}