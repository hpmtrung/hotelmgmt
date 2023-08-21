package vn.lotusviet.hotelmgmt.model.dto.newsletter;

import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

import java.time.LocalDateTime;

public class NewsletterDto {

  private int id;
  private String title;
  private String content;
  private LocalDateTime modifiedAt;
  private boolean published;
  private boolean deleted;
  private EmployeeDto createdBy;

  public boolean isDeleted() {
    return deleted;
  }

  public NewsletterDto setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  public int getId() {
    return id;
  }

  public NewsletterDto setId(int id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public NewsletterDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getContent() {
    return content;
  }

  public NewsletterDto setContent(String content) {
    this.content = content;
    return this;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public NewsletterDto setModifiedAt(LocalDateTime modifiedAt) {
    this.modifiedAt = modifiedAt;
    return this;
  }

  public boolean isPublished() {
    return published;
  }

  public NewsletterDto setPublished(boolean published) {
    this.published = published;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public NewsletterDto setCreatedBy(EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @Override
  public String toString() {
    return "NewsletterDto{"
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
        + '}';
  }
}