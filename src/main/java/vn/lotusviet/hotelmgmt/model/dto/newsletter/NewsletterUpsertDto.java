package vn.lotusviet.hotelmgmt.model.dto.newsletter;

public class NewsletterUpsertDto {

  private String title;
  private String content;

  public String getTitle() {
    return title;
  }

  public NewsletterUpsertDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getContent() {
    return content;
  }

  public NewsletterUpsertDto setContent(String content) {
    this.content = content;
    return this;
  }

  @Override
  public String toString() {
    return "NewsletterUpsertDto{" + "title='" + title + '\'' + ", content='" + content + '\'' + '}';
  }
}