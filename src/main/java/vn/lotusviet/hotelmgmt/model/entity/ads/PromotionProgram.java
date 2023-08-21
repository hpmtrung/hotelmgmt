package vn.lotusviet.hotelmgmt.model.entity.ads;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamicUpdate
@Entity
@Cacheable
@org.hibernate.annotations.Cache(
    region = PromotionProgram.CACHE,
    usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "CHUONG_TRINH_KM")
public class PromotionProgram implements Serializable {

  public static final String CACHE = "PromotionProgram";

  private static final String ID_GEN = "program_id_gen";

  private static final long serialVersionUID = -352136434085620668L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(
            name = "sequence_name",
            value = "ID_SEQ_CHUONG_TRINH_KM"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
        @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = "MA")
  private Integer id;

  @NotBlank
  @Column(name = "TIEU_DE")
  private String title;

  @NotBlank
  @Column(name = "HINH")
  private String imageURL;

  @Column(name = "MO_TA")
  private String description;

  @NotBlank
  @Column(name = "NOI_DUNG")
  private String content;

  @NotNull
  @Column(name = "NGAY_TAO")
  private LocalDateTime createdAt;

  @Column(name = "HIEN_THI")
  private boolean visible;

  @Column(name = "TRANG_THAI_XOA")
  private boolean deleted;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public PromotionProgram setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public boolean isVisible() {
    return visible;
  }

  public PromotionProgram setVisible(boolean visible) {
    this.visible = visible;
    return this;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public PromotionProgram setDeleted(boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PromotionProgram setDescription(final String description) {
    this.description = description;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public PromotionProgram setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public PromotionProgram setTitle(final String title) {
    if (title == null || title.isBlank()) throw new IllegalArgumentException();
    this.title = title;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public PromotionProgram setImageURL(final String imageURL) {
    if (imageURL == null || imageURL.isBlank()) throw new IllegalArgumentException();
    this.imageURL = imageURL;
    return this;
  }

  public String getContent() {
    return content;
  }

  public PromotionProgram setContent(final String description) {
    this.content = description;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PromotionProgram)) return false;
    PromotionProgram that = (PromotionProgram) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "PromotionProgram{"
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