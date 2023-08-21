package vn.lotusviet.hotelmgmt.model.dto.system;

public enum BusinessRuleKey {
  EMPLOYEE_ACCOUNT_CREATE_DEFAULT_LANGKEY(1),
  CUSTOMER_ACCOUNT_REGISTRATION_DEFAULT_LANGKEY(2),
  CUSTOMER_ACCOUNT_GENERATED_PW_LEN(3),
  CUSTOMER_ACCOUNT_RESET_PW_TIMEOUT_MINUTES(4),
  RENTAL_DETAIL_OVERDUED_FEE_PERCENT(5),
  RESERVATION_TEMPORARY_DEPOSIT_MIN_PERCENT(6),
  RESERVATION_TEMPORARY_TIMEOUT_MINUTES(7);

  private final int id;

  BusinessRuleKey(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}