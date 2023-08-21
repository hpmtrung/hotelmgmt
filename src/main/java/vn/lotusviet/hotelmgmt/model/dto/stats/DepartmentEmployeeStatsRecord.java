package vn.lotusviet.hotelmgmt.model.dto.stats;

import java.util.Objects;

public class DepartmentEmployeeStatsRecord {

  private String departmentName;
  private int numEmployee;

  public DepartmentEmployeeStatsRecord() {}

  public DepartmentEmployeeStatsRecord(String departmentName, int numEmployee) {
    this.departmentName = Objects.requireNonNull(departmentName);
    this.numEmployee = numEmployee;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public DepartmentEmployeeStatsRecord setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
    return this;
  }

  public int getNumEmployee() {
    return numEmployee;
  }

  public DepartmentEmployeeStatsRecord setNumEmployee(int numEmployee) {
    this.numEmployee = numEmployee;
    return this;
  }

  @Override
  public String toString() {
    return "DepartmentEmployeeStatsRecord{"
        + "departmentName='"
        + departmentName
        + '\''
        + ", numEmployee="
        + numEmployee
        + '}';
  }
}