package vn.lotusviet.hotelmgmt.model.dto.ads;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class PromotionProgramDto implements Serializable {

  private static final long serialVersionUID = 5204711419162391376L;
  private int id;
  private String title;
  private String imageURL;
  private String description;
  private String content;
  private LocalDateTime createdAt;
  private boolean visible;
  private boolean deleted;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public PromotionProgramDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public boolean isVisible() {
    return visible;
  }

  public PromotionProgramDto setVisible(boolean visible) {
    this.visible = visible;
    return this;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public PromotionProgramDto setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  public int getId() {
    return id;
  }

  public PromotionProgramDto setId(final int id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public PromotionProgramDto setTitle(final String title) {
    this.title = title;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public PromotionProgramDto setImageURL(final String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PromotionProgramDto setDescription(final String description) {
    this.description = description;
    return this;
  }

  public String getContent() {
    return content;
  }

  public PromotionProgramDto setContent(final String content) {
    this.content = content;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PromotionProgramDto)) return false;
    PromotionProgramDto that = (PromotionProgramDto) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "PromotionProgramDto{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", imageURL='"
        + imageURL
        + '\''
        + ", description='"
        + description
        + '\''
        + ", content='"
        + content
        + '\''
        + ", createdAt="
        + createdAt
        + ", visible="
        + visible
        + ", deleted="
        + deleted
        + '}';
  }
}