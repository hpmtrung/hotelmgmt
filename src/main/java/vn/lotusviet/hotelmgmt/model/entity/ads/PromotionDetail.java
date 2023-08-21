package vn.lotusviet.hotelmgmt.model.entity.ads;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Immutable;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

@Immutable
@Entity
@Table(name = "HANG_PHONG_KHUYEN_MAI")
public class PromotionDetail implements Serializable {

  private static final String COL_PHAN_TRAM_KM = "PHAN_TRAM_KM";

  private static final long serialVersionUID = 5920675024476795L;

  @EmbeddedId private final SuitePromotionId id = new SuitePromotionId();

  @Positive
  @Column(name = COL_PHAN_TRAM_KM)
  private int discountPercent;

  public PromotionDetail setPromotion(Promotion promotion) {
    this.id.setPromotion(promotion);
    return this;
  }

  public PromotionDetail setSuite(Suite suite) {
    this.id.setSuite(suite);
    return this;
  }

  public int getDiscountPercent() {
    return discountPercent;
  }

  public PromotionDetail setDiscountPercent(int discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  public Promotion getPromotion() {
    return this.id.getPromotion();
  }

  public Suite getSuite() {
    return this.id.getSuite();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PromotionDetail)) return false;
    PromotionDetail that = (PromotionDetail) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "PromotionDetail{" + "id=" + id + ", discountPercent=" + discountPercent + '}';
  }

  @Embeddable
  public static class SuitePromotionId implements Serializable {
    private static final long serialVersionUID = -47968076679037648L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Promotion.COL_MA_KHUYEN_MAI, nullable = false)
    @JsonBackReference
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Suite.COL_MA_HANG_PHONG, nullable = false)
    private Suite suite;

    public Promotion getPromotion() {
      return promotion;
    }

    public void setPromotion(Promotion promotion) {
      this.promotion = promotion;
    }

    public Suite getSuite() {
      return suite;
    }

    public void setSuite(Suite suite) {
      this.suite = suite;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof SuitePromotionId)) return false;
      SuitePromotionId that = (SuitePromotionId) o;
      return Objects.equals(getPromotion(), that.getPromotion())
          && Objects.equals(getSuite(), that.getSuite());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getPromotion(), getSuite());
    }

    @Override
    public String toString() {
      return "SuitePromotionId{" + "promotion=" + promotion + ", suite=" + suite + '}';
    }
  }
}