package vn.lotusviet.hotelmgmt.model.entity.newsletter;

import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "SUBSCRIBER")
public class Subscriber extends VersionedEntity implements Serializable {

  private static final long serialVersionUID = -318960369921046021L;

  @Id
  @EmailConstraint
  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "VERIFIED")
  private boolean verified;

  @NotNull
  @Column(name = "CREATED_AT", nullable = false)
  private LocalDateTime createdAt;

  public String getEmail() {
    return email;
  }

  public Subscriber setEmail(String email) {
    this.email = email;
    return this;
  }

  public boolean isVerified() {
    return verified;
  }

  public Subscriber setVerified(boolean verified) {
    this.verified = verified;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Subscriber setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Subscriber)) return false;
    Subscriber subscriber = (Subscriber) o;
    return getEmail().equals(subscriber.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmail());
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
        + "} "
        + super.toString();
  }
}