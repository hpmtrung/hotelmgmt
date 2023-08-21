package vn.lotusviet.hotelmgmt.core.exception.payload;

import vn.lotusviet.hotelmgmt.core.exception.ErrorDetail;

import java.util.List;

public interface MessageWithDetailPayload extends SimpleMessagePayload {

  List<ErrorDetail> getDetails();

}