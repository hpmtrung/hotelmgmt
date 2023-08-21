package vn.lotusviet.hotelmgmt.exception;

import java.util.Collections;
import java.util.Map;

public class RequestParamInvalidException extends RuntimeException {

  private static final long serialVersionUID = -6288818852949588246L;

  private final String title;
  private final Map<String, String> invalidParams;

  public RequestParamInvalidException(String title, String paramName, String errorKey) {
    this.title = title;
    this.invalidParams = Collections.singletonMap(paramName, errorKey);
  }

  public RequestParamInvalidException(String title, Map<String, String> invalidParams) {
    this.title = title;
    this.invalidParams = invalidParams;
  }

  public String getTitle() {
    return title;
  }

  public Map<String, String> getInvalidParams() {
    return invalidParams;
  }
}