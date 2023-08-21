package vn.lotusviet.hotelmgmt.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ServerErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.web.client.HttpClientErrorException.*;
import static vn.lotusviet.hotelmgmt.core.exception.ErrorDetail.ErrorLocation.QUERY;
import static vn.lotusviet.hotelmgmt.core.exception.payload.SimpleMessagePayload.ErrorType.*;

public final class ExceptionConverter {

  private static final Logger log = LoggerFactory.getLogger(ExceptionConverter.class);

  private ExceptionConverter() {}

  public static SimpleMessageException convert(final Exception exception) {
    log.debug("Convert exception class: {}", exception.getClass().getCanonicalName());

    if (exception instanceof AccessDeniedException) {
      return new SimpleMessageException(
          HttpStatus.FORBIDDEN, AUTHORIZATION_ERROR, "FORBIDDEN", exception.getMessage());
    } else if (exception instanceof AuthenticationException) {
      return new SimpleMessageException(
          HttpStatus.UNAUTHORIZED, AUTHORIZATION_ERROR, "INVALID_CLIENT", exception.getMessage());
    } else if (exception instanceof MethodArgumentNotValidException) {
      final List<ErrorDetail> details = new ArrayList<>();
      for (FieldError fieldError :
          ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
        String fieldValue =
            Objects.requireNonNullElse(fieldError.getRejectedValue(), "").toString();
        details.add(
            new ErrorDetail(fieldError.getDefaultMessage())
                .setValue(fieldValue)
                .setDescription(String.format("Field %s is invalid.", fieldError.getField()))
                .setField(fieldError.getField())
                .setLocation(QUERY));
      }
      return new InvalidParamException(details);
    } else if (exception instanceof ConstraintViolationException) {
      List<ErrorDetail> details = new ArrayList<>();
      for (ConstraintViolation<?> violation :
          ((ConstraintViolationException) exception).getConstraintViolations()) {
        String propertyPath = violation.getPropertyPath().toString();
        String field = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
        details.add(
            new ErrorDetail(violation.getMessage())
                .setValue(violation.getInvalidValue().toString())
                .setDescription("Value is invalid.")
                .setField(field)
                .setLocation(QUERY));
      }
      return new InvalidParamException(details);
    } else if (exception instanceof ServerErrorException) {
      return new SimpleMessageException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          SERVER_ERROR,
          "INTERNAL_SERVER_ERROR",
          "An internal server error has occurred.");
    } else if (exception instanceof MultipartException) {
      return new SimpleMessageException(
          HttpStatus.BAD_REQUEST, BAD_REQUEST_ERROR, "MULTIPART_FAILURE", exception.getMessage());
    } else if (exception instanceof ConcurrencyFailureException) {
      return new SimpleMessageException(
          HttpStatus.CONFLICT,
          CONCURRENCY_ERROR,
          "CONCURRENCY_FAILURE",
          "Conflict due to concurrency failure.");
    } else if (exception instanceof TypeMismatchException) {
      return new BadRequestException("PROPERTY_TYPE_INCORRECT", exception);
    } else if (exception instanceof HttpMediaTypeNotSupportedException) {
      return new BadRequestException("MEDIA_TYPE_NOT_SUPPORT", exception);
    } else if (exception instanceof HttpMediaTypeNotAcceptableException) {
      return new BadRequestException("MEDIA_TYPE_NOT_ACCEPT", exception);
    } else if (exception instanceof HttpRequestMethodNotSupportedException) {
      return new BadRequestException(exception);
    } else if (exception instanceof HttpMessageNotReadableException) {
      return new BadRequestException(exception);
    } else if (exception instanceof MissingServletRequestPartException) {
      return ServletRequestException.fromMissingPart(
          ((MissingServletRequestPartException) exception).getRequestPartName());
    } else if (exception instanceof MissingServletRequestParameterException) {
      return ServletRequestException.fromMissingParam(
          ((MissingServletRequestParameterException) exception).getParameterName());
    } else if (exception instanceof MissingPathVariableException) {
      return ServletRequestException.fromMissingPathVariable(
          ((MissingPathVariableException) exception).getVariableName());
    } else if (exception instanceof UnprocessableEntity) {
      return new SimpleMessageException(
          HttpStatus.UNPROCESSABLE_ENTITY,
          BAD_REQUEST_ERROR,
          "UNPROCESSABLE_ENTITY",
          "The API cannot complete the requested action, or the request action is semantically incorrect or fails business validation.");
    } else if (exception instanceof UnsupportedMediaType) {
      return new SimpleMessageException(
          HttpStatus.NOT_ACCEPTABLE,
          BAD_REQUEST_ERROR,
          "MEDIA_TYPE_NOT_ACCEPTABLE",
          "The server does not implement the media type that would be acceptable to the client.");
    } else if (exception instanceof MethodNotAllowed) {
      return new SimpleMessageException(
          HttpStatus.METHOD_NOT_ALLOWED,
          BAD_REQUEST_ERROR,
          "METHOD_NOT_SUPPORTED",
          "The server does not implement the requested HTTP method.");
    } else if (exception instanceof TooManyRequests) {
      return new SimpleMessageException(
          HttpStatus.TOO_MANY_REQUESTS,
          BAD_REQUEST_ERROR,
          "RATE_LIMIT_REACHED",
          "Too many requests. Blocked due to rate limiting.");
    } else if (exception instanceof NotFound) {
      return new SimpleMessageException(
          HttpStatus.NOT_FOUND,
          RESOURCE_NOT_FOUND_ERROR,
          "RESOURCE_NOT_FOUND",
          "The specified resource does not exist.");
    } else {
      return new ErrorHandlerNotFoundException(exception);
    }
  }
}