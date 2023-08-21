package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;

public class ServiceUsagePayingErrorException extends ConstraintViolationException {
  private static final long serialVersionUID = -3328253008105110807L;

  public ServiceUsagePayingErrorException() {
    super(ServiceUsageDetail.class.getSimpleName(), "isPaid", "illegalState");
  }
}