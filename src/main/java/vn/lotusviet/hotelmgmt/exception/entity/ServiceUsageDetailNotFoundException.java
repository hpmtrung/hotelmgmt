package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;

public class ServiceUsageDetailNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 2847183482796263298L;

  public ServiceUsageDetailNotFoundException(long id) {
    super(ServiceUsageDetail.class.getSimpleName(), String.valueOf(id));
  }
}