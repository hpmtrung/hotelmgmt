package vn.lotusviet.hotelmgmt.model.dto.person;

public class CustomerFilterDto {
  private String personalId;

  public String getPersonalId() {
    return personalId;
  }

  public CustomerFilterDto setPersonalId(String personalId) {
    this.personalId = personalId;
    return this;
  }

  @Override
  public String toString() {
    return "CustomerFilterDto{" + "personalId='" + personalId + '\'' + '}';
  }
}