package vn.lotusviet.hotelmgmt.exception;

public class PromotionNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -6653046008214072637L;

  public PromotionNotFoundException(Long id) {
    super("promotion", String.valueOf(id));
  }
}