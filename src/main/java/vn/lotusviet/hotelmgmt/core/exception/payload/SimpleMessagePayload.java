package vn.lotusviet.hotelmgmt.core.exception.payload;

import org.springframework.http.HttpStatus;

public interface SimpleMessagePayload {

  HttpStatus getStatus();

  ErrorType getErrorType();

  String getName();

  String getMessage();

  enum ErrorType {
    UNHANDLED_ERROR,
    SERVER_ERROR,
    BAD_REQUEST_ERROR, // Error of invalid request params
    CONCURRENCY_ERROR,
    AUTHORIZATION_ERROR,
    RESOURCE_NOT_FOUND_ERROR,
  }
}