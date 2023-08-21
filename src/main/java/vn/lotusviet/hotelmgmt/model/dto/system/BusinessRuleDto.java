package vn.lotusviet.hotelmgmt.model.dto.system;

import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

public class BusinessRuleDto {

  private Integer id;
  private String name;
  private String value;
  private EmployeeDto editedBy;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public EmployeeDto getEditedBy() {
    return editedBy;
  }

  public BusinessRuleDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public BusinessRuleDto setName(final String name) {
    this.name = name;
    return this;
  }

  public BusinessRuleDto setValue(final String value) {
    this.value = value;
    return this;
  }

  public BusinessRuleDto setEditedBy(final EmployeeDto editedBy) {
    this.editedBy = editedBy;
    return this;
  }

  @Override
  public String toString() {
    return "BusinessRuleDto{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", value='"
        + value
        + '\''
        + ", editedBy="
        + editedBy
        + '}';
  }
}