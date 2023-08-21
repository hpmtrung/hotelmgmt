package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.core.exception.ErrorResponse;
import vn.lotusviet.hotelmgmt.core.exception.ExceptionConverter;
import vn.lotusviet.hotelmgmt.core.exception.MessageWithDetailException;
import vn.lotusviet.hotelmgmt.core.exception.SimpleMessageException;

import javax.validation.ConstraintViolationException;

import static org.springframework.web.client.HttpClientErrorException.*;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final ApplicationProperties applicationProperties;

  public RestExceptionHandler(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  private String getInformationLink(String errorType) {
    return applicationProperties.getRestErrorDocsBaseUrl() + "/#" + errorType;
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleIsolatedApplicationException(
      SimpleMessageException exception) {
    var responseBuilder =
        ErrorResponse.builder(exception.getStatus(), exception.getName(), exception.getMessage())
            .withInformationLink(getInformationLink(exception.getErrorType().name().toLowerCase()));
    if (exception instanceof MessageWithDetailException) {
      responseBuilder.withDetail(((MessageWithDetailException) exception).getDetails());
    }
    return new ResponseEntity<>(responseBuilder.build(), new HttpHeaders(), exception.getStatus());
  }

  @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
  public ResponseEntity<Object> handleSecurityException(
      final Exception exception, final NativeWebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @ExceptionHandler({
    BadRequest.class,
    NotFound.class,
    TooManyRequests.class,
    MethodNotAllowed.class,
    UnsupportedMediaType.class,
    UnprocessableEntity.class
  })
  public ResponseEntity<Object> handleHttpClientErrorException(
      HttpClientErrorException exception, NativeWebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @NonNull
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }

  @ExceptionHandler({
    ConstraintViolationException.class,
    ConcurrencyFailureException.class,
    ServerErrorException.class,
    MultipartException.class
  })
  public ResponseEntity<Object> handleException(Exception exception, NativeWebRequest request) {
    return handleIsolatedApplicationException(ExceptionConverter.convert(exception));
  }
}