package vn.lotusviet.hotelmgmt.exception;

public class CustomerRentalDuplicatedException extends RequestParamInvalidException {
  private static final long serialVersionUID = -4260672913428540813L;

  public CustomerRentalDuplicatedException() {
    super("Customers are duplicated", "customerRental", "duplicated");
  }
}