package vn.lotusviet.hotelmgmt.model.entity.room;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@DynamicUpdate
@Entity
@Table(name = "PHONG")
public class Room extends VersionedEntity implements Serializable {

  public static final String COL_MA_PHONG = "MA_PHONG";
  public static final String COL_TEN_PHONG = "TEN_PHONG";
  public static final String COL_VI_TRI_TANG = "VI_TRI_TANG";
  public static final String COL_TRANG_THAI_HD = "TRANG_THAI_HD";

  private static final String ID_GEN = "room_id_gen";

  private static final long serialVersionUID = 2661215691678077568L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_PHONG"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_PHONG)
  private Integer id;

  @Size(max = 50)
  @Column(name = COL_TEN_PHONG, unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = RoomStatus.COL_MA_TTP, nullable = false)
  private RoomStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Suite.COL_MA_HANG_PHONG, nullable = false)
  @JsonBackReference
  private Suite suite;

  @Column(name = Suite.COL_MA_HANG_PHONG, insertable = false, updatable = false)
  private Integer suiteId;

  @Column(name = COL_TRANG_THAI_HD)
  private boolean available = true;

  @Positive
  @Column(name = COL_VI_TRI_TANG)
  private int floor;

  public boolean isAvailable() {
    return available;
  }

  public Room setAvailable(boolean available) {
    this.available = available;
    return this;
  }

  public Integer getSuiteId() {
    return suiteId;
  }

  public Room setSuiteId(Integer suiteId) {
    this.suiteId = suiteId;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public Room setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Room setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  public RoomStatus getStatus() {
    return status;
  }

  public Room setStatus(final RoomStatus status) {
    this.status = Objects.requireNonNull(status);
    return this;
  }

  public Suite getSuite() {
    return suite;
  }

  public Room setSuite(final Suite suite) {
    this.suite = suite;
    return this;
  }

  public int getFloor() {
    return floor;
  }

  public Room setFloor(final int floor) {
    if (floor <= 0) throw new IllegalArgumentException();
    this.floor = floor;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Room)) return false;
    Room room = (Room) o;
    return Objects.equals(getName(), room.getName());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Room{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", suiteId="
        + suiteId
        + ", floor="
        + floor
        + ", available="
        + available
        + "} "
        + super.toString();
  }
}