package vn.lotusviet.hotelmgmt.model.entity.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import vn.lotusviet.hotelmgmt.core.annotation.validation.address.AddressConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.langkey.LangKeyConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamicUpdate
@Entity
@Cacheable
@org.hibernate.annotations.Cache(
    region = AccountRepository.CACHE_ACCOUNT_ENTITY_BY_ID,
    usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TAI_KHOAN")
public class Account extends VersionedEntity implements Serializable {

  public static final String COL_MA_TAI_KHOAN = "MA_TAI_KHOAN";
  public static final String COL_EMAIL = "EMAIL";
  public static final String COL_HO = "HO";
  public static final String COL_TEN = "TEN";
  public static final String COL_DIA_CHI = "DIA_CHI";
  public static final String COL_SDT = "SDT";
  public static final String COL_MAT_KHAU = "MAT_KHAU";
  public static final String COL_DA_KICH_HOAT = "DA_KICH_HOAT";
  public static final String COL_MA_KICH_HOAT = "MA_KICH_HOAT";
  public static final String COL_MA_DOI_MK = "MA_DOI_MK";
  public static final String COL_NGAYGIO_RESET = "NGAYGIO_RESET";
  public static final String COL_NGON_NGU = "NGON_NGU";
  public static final String COL_HINH = "HINH";

  private static final long serialVersionUID = -3219819588454675156L;

  private static final String ID_GEN = "account_id_gen";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GEN)
  @GenericGenerator(
      name = ID_GEN,
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "ID_SEQ_TAI_KHOAN"),
        @Parameter(name = "increment_size", value = "1"),
        @Parameter(name = "optimizer", value = "pooled")
      })
  @Column(name = COL_MA_TAI_KHOAN)
  private Long id;

  @EmailConstraint
  @Column(name = COL_EMAIL, unique = true)
  private String email;

  @PersonNameConstraint(type = PersonNameConstraint.Type.LAST_NAME)
  @Column(name = COL_HO)
  private String lastName;

  @PersonNameConstraint(type = PersonNameConstraint.Type.FIRST_NAME)
  @Column(name = COL_TEN)
  private String firstName;

  @AddressConstraint
  @Column(name = COL_DIA_CHI)
  private String address;

  @PhoneConstraint
  @Column(name = COL_SDT)
  private String phone;

  @JsonIgnore
  @Column(name = COL_MAT_KHAU)
  private String hashedPassword;

  @Column(name = COL_DA_KICH_HOAT)
  private boolean activated;

  @Column(name = COL_MA_KICH_HOAT)
  private String activationKey;

  @Column(name = COL_MA_DOI_MK)
  private String pwResetKey;

  @Column(name = COL_NGAYGIO_RESET)
  private LocalDateTime resetAt;

  @LangKeyConstraint
  @Column(name = COL_NGON_NGU)
  private String langKey;

  @Size(max = 100)
  @Column(name = COL_HINH)
  private String imageURL;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = Authority.COL_MA_NQ, nullable = false)
  private Authority authority;

  public Long getId() {
    return id;
  }

  public Account setId(final Long id) {
    this.id = id;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Account setEmail(final String email) {
    if (email == null || email.isBlank()) throw new IllegalArgumentException();
    this.email = email;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Account setLastName(final String lastName) {
    if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException();
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Account setFirstName(final String firstName) {
    if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException();
    this.firstName = firstName;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public Account setAddress(final String address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Account setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public Account setHashedPassword(final String hashedPassword) {
    this.hashedPassword = hashedPassword;
    return this;
  }

  public boolean isActivated() {
    return activated;
  }

  public Account setActivated(final boolean activated) {
    this.activated = activated;
    return this;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public Account setActivationKey(final String activationKey) {
    this.activationKey = activationKey;
    return this;
  }

  public String getPwResetKey() {
    return pwResetKey;
  }

  public Account setPwResetKey(final String pwResetKey) {
    this.pwResetKey = pwResetKey;
    return this;
  }

  public LocalDateTime getResetAt() {
    return resetAt;
  }

  public Account setResetAt(final LocalDateTime resetAt) {
    this.resetAt = resetAt;
    return this;
  }

  public String getLangKey() {
    return langKey;
  }

  public Account setLangKey(final String langKey) {
    this.langKey = langKey;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public Account setImageURL(final String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public Authority getAuthority() {
    return authority;
  }

  public Account setAuthority(final Authority authority) {
    this.authority = Objects.requireNonNull(authority);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account)) return false;
    Account account = (Account) o;
    return Objects.equals(getEmail(), account.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmail());
  }

  @Override
  public String toString() {
    return "Account{"
        + "id="
        + id
        + ", lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", address='"
        + address
        + '\''
        + ", isActivated="
        + activated
        + ", activationKey='"
        + activationKey
        + '\''
        + ", pwResetKey='"
        + pwResetKey
        + '\''
        + ", resetAt="
        + resetAt
        + ", langKey='"
        + langKey
        + '\''
        + ", imageURL='"
        + imageURL
        + '\''
        + ", authority="
        + (authority != null ? authority.getName() : "NULL")
        + ", version="
        + version
        + '}';
  }
}