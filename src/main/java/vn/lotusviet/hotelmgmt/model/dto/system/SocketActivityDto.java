package vn.lotusviet.hotelmgmt.model.dto.system;

import java.time.Instant;

public class SocketActivityDto {
  private String sessionId;
  private String principle;
  private String ipAddress;
  private String page;
  private Instant time;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getPrinciple() {
    return principle;
  }

  public void setPrinciple(String principle) {
    this.principle = principle;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "SocketActivityDto{"
        + "sessionId='"
        + sessionId
        + '\''
        + ", userLogin='"
        + principle
        + '\''
        + ", ipAddress='"
        + ipAddress
        + '\''
        + ", page='"
        + page
        + '\''
        + ", time="
        + time
        + '}';
  }
}