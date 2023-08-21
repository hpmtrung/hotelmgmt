package vn.lotusviet.hotelmgmt.model.entity.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DynamicUpdate
@Entity
@Table(name = "HANG_PHONG")
@Cacheable
@org.hibernate.annotations.Cache(region = Suite.CACHE, usage = CacheConcurrencyStrategy.READ_WRITE)
public class Suite extends VersionedEntity implements Serializable {

  public static final String CACHE = "Suite";
  public static final String CACHE_ROOMS = "Suite.rooms";

  public static final String COL_MA_HANG_PHONG = "MA_HANG_PHONG";
  public static final String COL_MA_LOAI_PHONG = "MA_LOAI_PHONG";
  public static final String COL_MA_KIEU_PHONG = "MA_KIEU_PHONG";
  public static final String COL_DIEN_TICH_PHONG = "DIEN_TICH_PHONG";
  public static final String COL_GIA_HANG_PHONG = "GIA_HANG_PHONG";
  public static final String COL_VAT = "VAT";
  public static final String COL_ANH = "ANH";
  public static final String COL_LUOT_MUA = "LUOT_MUA";
  public static final String COL_PHAN_TRAM_KM = "PHAN_TRAM_KM";
  public static final String COL_SO_PHONG_TRONG = "SO_PHONG_TRONG";
  public static final String COL_TIEN_NGHI = "TIEN_NGHI";
  public static final String COL_TRANG_THAI_HD = "TRANG_THAI_HD";

  private static final long serialVersionUID = -174376290777802935L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COL_MA_HANG_PHONG)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = COL_MA_LOAI_PHONG, nullable = false)
  @JsonIgnore
  private SuiteType suiteType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = COL_MA_KIEU_PHONG, nullable = false)
  @JsonIgnore
  private SuiteStyle suiteStyle;

  @Positive
  @Column(name = COL_DIEN_TICH_PHONG)
  private int area;

  @Positive
  @Column(name = COL_GIA_HANG_PHONG)
  private int originalPrice;

  @PercentConstaint
  @Column(name = COL_VAT)
  private int vat;

  @Size(max = 100)
  @Column(name = COL_ANH)
  private String imageURL;

  @PositiveOrZero
  @Column(name = COL_LUOT_MUA, updatable = false)
  private int rentalNum;

  @Column(name = COL_TRANG_THAI_HD)
  private boolean available = true;

  @OneToMany(
      mappedBy = "suite",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @org.hibernate.annotations.Cache(
      region = CACHE_ROOMS,
      usage = CacheConcurrencyStrategy.READ_WRITE)
  @OrderBy("id")
  @JsonManagedReference
  private Set<Room> rooms = new HashSet<>();

  public boolean isAvailable() {
    return available;
  }

  public Suite setAvailable(boolean available) {
    this.available = available;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public Suite setId(final Integer id) {
    this.id = id;
    return this;
  }

  public SuiteType getSuiteType() {
    return suiteType;
  }

  public Suite setSuiteType(final SuiteType suiteType) {
    this.suiteType = suiteType;
    return this;
  }

  public SuiteStyle getSuiteStyle() {
    return suiteStyle;
  }

  public Suite setSuiteStyle(final SuiteStyle suiteStyle) {
    this.suiteStyle = suiteStyle;
    return this;
  }

  public int getArea() {
    return area;
  }

  public Suite setArea(final int area) {
    if (area <= 0) throw new IllegalArgumentException();
    this.area = area;
    return this;
  }

  public int getOriginalPrice() {
    return originalPrice;
  }

  public Suite setOriginalPrice(final int price) {
    if (price <= 0) throw new IllegalArgumentException();
    this.originalPrice = price;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public Suite setVat(final int vat) {
    if (vat < 0) throw new IllegalArgumentException();
    this.vat = vat;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public Suite setImageURL(final String imageURL) {
    if (imageURL == null || imageURL.isBlank()) throw new IllegalArgumentException();
    this.imageURL = imageURL;
    return this;
  }

  public Integer getRentalNum() {
    return rentalNum;
  }

  public Suite setRentalNum(final Integer rentalNum) {
    if (rentalNum <= 0) throw new IllegalArgumentException();
    this.rentalNum = rentalNum;
    return this;
  }

  public Set<Room> getRooms() {
    return rooms;
  }

  public Suite setRooms(final Set<Room> rooms) {
    this.rooms = Objects.requireNonNull(rooms);
    return this;
  }

  public Suite addRoom(final Room room) {
    Objects.requireNonNull(room);
    if (!rooms.contains(room)) {
      room.setSuite(this);
      rooms.add(room);
    }
    return this;
  }

  public Suite removeRoom(Room room) {
    if (room == null) throw new NullPointerException();
    if (rooms.contains(room)) {
      rooms.remove(room);
      room.setSuite(null);
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Suite)) return false;
    Suite suite = (Suite) o;
    return Objects.equals(getSuiteType(), suite.getSuiteType())
        && Objects.equals(getSuiteStyle(), suite.getSuiteStyle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSuiteType(), getSuiteStyle());
  }

  @Override
  public String toString() {
    return "Suite{"
        + "id="
        + id
        + ", area="
        + area
        + ", price="
        + originalPrice
        + ", vat="
        + vat
        + ", image='"
        + imageURL
        + '\''
        + ", rentalNum="
        + rentalNum
        + ", available="
        + available
        + ", version='"
        + version
        + '\''
        + '}';
  }
}