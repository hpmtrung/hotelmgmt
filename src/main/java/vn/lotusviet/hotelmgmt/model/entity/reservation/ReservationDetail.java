package vn.lotusviet.hotelmgmt.model.entity.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamicUpdate
@Entity
@Table(name = "CT_PHIEU_DAT")
public class ReservationDetail implements Serializable {

  public static final String COL_SO_LUONG_PHONG = "SO_LUONG_PHONG";
  public static final String COL_GIA_HANG_PHONG = "GIA_HANG_PHONG";
  public static final String COL_VAT = "VAT";
  private static final long serialVersionUID = 1870185518410354750L;

  @EmbeddedId private final ReservationDetailId id = new ReservationDetailId();

  @Positive
  @Column(name = COL_SO_LUONG_PHONG)
  private int roomNum;

  @Positive
  @Column(name = COL_GIA_HANG_PHONG)
  private int suitePrice;

  @PercentConstaint
  @Column(name = COL_VAT)
  private int vat;

  @Transient private List<PromotionDto> promotions = new ArrayList<>();

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public ReservationDetail setPromotions(List<PromotionDto> promotions) {
    this.promotions = promotions;
    return this;
  }

  public int getOriginalSubTotal() {
    return suitePrice * roomNum * id.getReservation().getNightCount();
  }

  public int getSubTotal() {
    return getOriginalSubTotal() - getTotalDiscountFromPromotion();
  }

  public int getTotalDiscountFromPromotion() {
    return MathUtil.roundHalfThousand(
        promotions.stream()
            .mapToDouble(
                p -> suitePrice * p.getDuration() * roomNum * p.getDiscountPercent() / 100.0)
            .sum());
  }

  public Reservation getReservation() {
    return this.id.getReservation();
  }

  public ReservationDetail setReservation(Reservation reservation) {
    this.id.setReservation(reservation);
    return this;
  }

  public Suite getSuite() {
    return this.id.getSuite();
  }

  public ReservationDetail setSuite(Suite suite) {
    this.id.setSuite(suite);
    return this;
  }

  public ReservationDetailId getId() {
    return id;
  }

  public int getRoomNum() {
    return roomNum;
  }

  public ReservationDetail setRoomNum(final int roomNum) {
    if (roomNum <= 0) throw new IllegalArgumentException();
    this.roomNum = roomNum;
    return this;
  }

  public int getSuitePrice() {
    return suitePrice;
  }

  public ReservationDetail setSuitePrice(final int suitePrice) {
    if (suitePrice <= 0) throw new IllegalArgumentException();
    this.suitePrice = suitePrice;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public ReservationDetail setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReservationDetail)) return false;
    ReservationDetail that = (ReservationDetail) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "ReservationDetail{"
        + "id="
        + id
        + ", roomNum="
        + roomNum
        + ", suitePrice="
        + suitePrice
        + ", vat="
        + vat
        + ", promotion="
        + promotions
        + '}';
  }

  @Embeddable
  public static class ReservationDetailId implements Serializable {

    private static final long serialVersionUID = 6246999007575555123L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Reservation.COL_MA_PHIEU_DAT, nullable = false)
    @JsonBackReference
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Suite.COL_MA_HANG_PHONG, nullable = false)
    private Suite suite;

    public Reservation getReservation() {
      return reservation;
    }

    public ReservationDetailId setReservation(final Reservation reservation) {
      this.reservation = reservation;
      return this;
    }

    public Suite getSuite() {
      return suite;
    }

    public ReservationDetailId setSuite(final Suite suite) {
      this.suite = suite;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ReservationDetailId)) return false;
      ReservationDetailId that = (ReservationDetailId) o;
      return Objects.equals(getReservation(), that.getReservation())
          && Objects.equals(getSuite(), that.getSuite());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getReservation(), getSuite());
    }

    @Override
    public String toString() {
      return "ReservationDetailId{" + "reservation=" + reservation + ", suite=" + suite + '}';
    }
  }
}