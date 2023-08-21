package vn.lotusviet.hotelmgmt.exception;

public class AccountNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = -1062523375921826100L;

  public AccountNotFoundException(long id) {
    super("account", String.valueOf(id));
  }

  public AccountNotFoundException(String email) {
    super("account", email);
  }
}