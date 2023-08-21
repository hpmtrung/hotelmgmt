package vn.lotusviet.hotelmgmt.model.dto.stats;

public class SuiteTurnOverStatsRecord {
  private String suiteTypeName;
  private String suiteStyleName;
  private int occupiedNum;
  private int total;
  private long turnOver;

  public String getSuiteTypeName() {
    return suiteTypeName;
  }

  public SuiteTurnOverStatsRecord setSuiteTypeName(String suiteTypeName) {
    this.suiteTypeName = suiteTypeName;
    return this;
  }

  public String getSuiteStyleName() {
    return suiteStyleName;
  }

  public SuiteTurnOverStatsRecord setSuiteStyleName(String suiteStyleName) {
    this.suiteStyleName = suiteStyleName;
    return this;
  }

  public int getOccupiedNum() {
    return occupiedNum;
  }

  public SuiteTurnOverStatsRecord setOccupiedNum(int occupiedNum) {
    this.occupiedNum = occupiedNum;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public SuiteTurnOverStatsRecord setTotal(int total) {
    this.total = total;
    return this;
  }

  public long getTurnOver() {
    return turnOver;
  }

  public SuiteTurnOverStatsRecord setTurnOver(long turnOver) {
    this.turnOver = turnOver;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteTurnOverStatsRecord{"
        + "suiteTypeName='"
        + suiteTypeName
        + '\''
        + ", suiteStyleName='"
        + suiteStyleName
        + '\''
        + ", occupiedNum="
        + occupiedNum
        + ", total="
        + total
        + ", turnOver="
        + turnOver
        + '}';
  }
}