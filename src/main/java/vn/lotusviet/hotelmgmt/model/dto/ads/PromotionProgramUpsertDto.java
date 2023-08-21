package vn.lotusviet.hotelmgmt.model.dto.ads;

public class PromotionProgramUpsertDto {
  private String title;
  private String content;
  private String description;
  private Boolean visible;

  public Boolean getVisible() {
    return visible;
  }

  public PromotionProgramUpsertDto setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public PromotionProgramUpsertDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getContent() {
    return content;
  }

  public PromotionProgramUpsertDto setContent(String content) {
    this.content = content;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PromotionProgramUpsertDto setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public String toString() {
    return "PromotionProgramUpsertDto{"
        + "title='"
        + title
        + '\''
        + ", content='"
        + content
        + '\''
        + ", description='"
        + description
        + '\''
        + ", visible="
        + visible
        + '}';
  }
}