package vn.lotusviet.hotelmgmt.exception;

public final class ApplicationErrorCode {

  public static final String EMAIL_NOT_EXIST = "email.notExist";
  public static final String EMAIL_NOT_ACTIVATED = "email.notActivated";
  public static final String PASSWORD_RESET_KEY_INVALID = "resetKey.invalid";
  public static final String CURRENT_PASSWORD_NOT_MATCHED = "currentPassword.notMatch";
  // Reservation
  public static final String RESERVATION_ID_NOT_EXIST = "reservationId.notExist";
  public static final String ROOM_ID_NOT_EXIST = "roomId.notExist";
  // Room
  public static final String ROOM_NAME_DUPLICATED = "roomName.duplicated";

  private ApplicationErrorCode() {}
}