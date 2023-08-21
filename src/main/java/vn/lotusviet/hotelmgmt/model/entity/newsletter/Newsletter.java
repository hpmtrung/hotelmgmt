package vn.lotusviet.hotelmgmt.model.entity.newsletter;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "NEWSLETTER")
public class Newsletter extends VersionedEntity implements Serializable {

  private static final long serialVersionUID = 2666259513185221446L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletterIdGen")
  @GenericGenerator(
      name = "newsletterIdGen",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ID_SEQ_NEWSLETTER"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
        @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = "ID", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "TITLE", nullable = false)
  private String title;

  @NotNull
  @Column(name = "CONTENT", nullable = false)
  private String content;

  @NotNull
  @Column(name = "MODIFIED_AT", nullable = false)
  private LocalDateTime modifiedAt;

  @Column(name = "PUBLISHED")
  private boolean published;

  @Column(name = "DELETED")
  private boolean deleted;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CREATED_BY", referencedColumnName = "MA_NV", nullable = false)
  private Employee createdBy;

  public boolean isDeleted() {
    return deleted;
  }

  public Newsletter setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public Newsletter setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Newsletter setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getContent() {
    return content;
  }

  public Newsletter setContent(String content) {
    this.content = content;
    return this;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public Newsletter setModifiedAt(LocalDateTime createdAt) {
    this.modifiedAt = createdAt;
    return this;
  }

  public boolean isPublished() {
    return published;
  }

  public Newsletter setPublished(boolean published) {
    this.published = published;
    return this;
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public Newsletter setCreatedBy(Employee createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Newsletter)) return false;
    Newsletter that = (Newsletter) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Newsletter{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", content='"
        + content
        + '\''
        + ", modifiedAt="
        + modifiedAt
        + ", published="
        + published
        + ", deleted="
        + deleted
        + ", createdBy="
        + createdBy
        + "} "
        + super.toString();
  }
}