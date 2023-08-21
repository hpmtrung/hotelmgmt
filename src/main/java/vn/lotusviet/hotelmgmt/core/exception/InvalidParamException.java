package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static vn.lotusviet.hotelmgmt.core.exception.ErrorDetail.ErrorLocation.*;
import static vn.lotusviet.hotelmgmt.core.exception.BadRequestException.DEFAULT_MESSAGE;
import static vn.lotusviet.hotelmgmt.core.exception.BadRequestException.DEFAULT_NAME;

public class InvalidParamException extends MessageWithDetailException {

  private static final long serialVersionUID = -8143776304745274917L;

  public InvalidParamException(List<ErrorDetail> errorDetails) {
    super(
        HttpStatus.BAD_REQUEST,
        ErrorType.BAD_REQUEST_ERROR,
        DEFAULT_NAME,
        DEFAULT_MESSAGE,
        errorDetails);
  }

  private InvalidParamException(Builder builder) {
    this(builder.errorDetails);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final List<ErrorDetail> errorDetails;

    public Builder() {
      errorDetails = new ArrayList<>();
    }

    private static ErrorDetail createErrorDetail(
        String code,
        String field,
        String value,
        String message,
        ErrorDetail.ErrorLocation location) {
      return new ErrorDetail(code)
          .setField(field)
          .setValue(value)
          .setDescription(message)
          .setLocation(location);
    }

    private static String getDefaultMessage(String field) {
      return field + " is invalid";
    }

    public Builder body(String code, String field, String value, String message) {
      this.errorDetails.add(createErrorDetail(code, field, value, message, BODY));
      return this;
    }

    public Builder body(String code, String field, String value) {
      return body(code, field, value, getDefaultMessage(field));
    }

    public Builder path(String code, String field, String value, String message) {
      this.errorDetails.add(createErrorDetail(code, field, value, message, PATH));
      return this;
    }

    public Builder path(String code, String field, String value) {
      return path(code, field, value, getDefaultMessage(field));
    }

    public Builder query(String code, String field, String value, String message) {
      this.errorDetails.add(createErrorDetail(code, field, value, message, QUERY));
      return this;
    }

    public Builder query(String code, String field, String value) {
      return query(code, field, value, getDefaultMessage(field));
    }

    public InvalidParamException build() {
      return new InvalidParamException(this);
    }
  }
}