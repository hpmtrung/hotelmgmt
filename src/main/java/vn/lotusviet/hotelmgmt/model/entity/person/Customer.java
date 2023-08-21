package vn.lotusviet.hotelmgmt.model.entity.person;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.lotusviet.hotelmgmt.core.annotation.validation.address.AddressConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.personalid.PersonalIdConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.phone.PhoneConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.taxcode.ValidTaxCode;
import vn.lotusviet.hotelmgmt.core.domain.VersionedEntity;
import vn.lotusviet.hotelmgmt.repository.person.CustomerRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamicUpdate
@Entity
@Cacheable
@org.hibernate.annotations.Cache(
    region = CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_PERSONAL_ID,
    usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "KHACH_HANG")
public class Customer extends VersionedEntity implements Serializable {

  public static final String COL_CMND = "CMND";
  public static final String COL_HO = "HO";
  public static final String COL_TEN = "TEN";
  public static final String COL_DIA_CHI = "DIA_CHI";
  public static final String COL_SDT = "SDT";
  public static final String COL_MA_SO_THUE = "MA_SO_THUE";
  public static final String COL_NGAY_CAP_NHAT = "NGAY_CAP_NHAT";

  private static final long serialVersionUID = 6323731315928740441L;

  @Id
  @PersonalIdConstraint
  @Column(name = COL_CMND)
  private String personalId;

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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = Account.COL_MA_TAI_KHOAN)
  private Account account;

  @ValidTaxCode
  @Column(name = COL_MA_SO_THUE)
  private String taxCode;

  @Column(name = COL_NGAY_CAP_NHAT)
  private LocalDateTime modifiedAt;

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public Customer setModifiedAt(LocalDateTime modifiedAt) {
    this.modifiedAt = modifiedAt;
    return this;
  }

  public String getPersonalId() {
    return personalId;
  }

  public Customer setPersonalId(final String personalId) {
    if (personalId == null || personalId.isBlank()) throw new IllegalArgumentException();
    this.personalId = personalId;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Customer setPhone(final String phone) {
    this.phone = phone;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Customer setLastName(final String lastName) {
    if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException();
    this.lastName = lastName;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public Customer setFirstName(final String firstName) {
    if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException();
    this.firstName = firstName;
    return this;
  }

  public String getFullName() {
    return lastName + " " + firstName;
  }

  public String getAddress() {
    return address;
  }

  public Customer setAddress(final String address) {
    this.address = address;
    return this;
  }

  public Account getAccount() {
    return account;
  }

  public Customer setAccount(final Account account) {
    this.account = account;
    return this;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public Customer setTaxCode(final String taxCode) {
    this.taxCode = taxCode;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Customer)) return false;
    Customer customer = (Customer) o;
    return Objects.equals(getPersonalId(), customer.getPersonalId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Customer{"
        + "personalId='"
        + personalId
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phone='"
        + phone
        + ", modifiedAt='"
        + modifiedAt
        + '\''
        + ", taxCode='"
        + taxCode
        + '\''
        + ", version='"
        + version
        + "'}";
  }
}