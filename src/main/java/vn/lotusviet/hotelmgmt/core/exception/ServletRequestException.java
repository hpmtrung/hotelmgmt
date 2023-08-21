package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

public class ServletRequestException extends SimpleMessageException {

  private static final long serialVersionUID = -8141550434466616241L;

  private ServletRequestException(String name, String message) {
    super(HttpStatus.BAD_REQUEST, ErrorType.BAD_REQUEST_ERROR, name, message);
  }

  public static ServletRequestException fromMissingParam(String paramName) {
    return new ServletRequestException("MISSING_REQUEST_PARAM", getMessage(paramName));
  }

  public static ServletRequestException fromMissingPart(String partName) {
    return new ServletRequestException("MISSING_REQUEST_PART", getMessage(partName));
  }

  public static ServletRequestException fromMissingPathVariable(String variableName) {
    return new ServletRequestException("MISSING_PATH_VARIABLE", getMessage(variableName));
  }

  private static String getMessage(String variable) {
    return variable + " is missing.";
  }
}