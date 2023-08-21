package vn.lotusviet.hotelmgmt.exception;

public class ServicePriceUpdateNotAllowedException extends RuntimeException{

  private static final long serialVersionUID = -8187816512940703985L;

  public ServicePriceUpdateNotAllowedException(String message) {
    super(message);
  }
}