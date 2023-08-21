package vn.lotusviet.hotelmgmt.model.dto.newsletter;

import java.time.LocalDateTime;

public class SubscriberDto {
  private String email;
  private boolean verified;
  private LocalDateTime createdAt;

  public String getEmail() {
    return email;
  }

  public SubscriberDto setEmail(String email) {
    this.email = email;
    return this;
  }

  public boolean isVerified() {
    return verified;
  }

  public SubscriberDto setVerified(boolean verified) {
    this.verified = verified;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public SubscriberDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  @Override
  public String toString() {
    return "SubscriberDto{"
        + "email='"
        + email
        + '\''
        + ", verified="
        + verified
        + ", createdAt="
        + createdAt
        + '}';
  }
}