package vn.lotusviet.hotelmgmt.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class ErrorDetail implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The unique, fine-grained application-level error code. */
  private final String issue;

  /**
   * The human-readable description for an issue. The description can change over the lifetime of an
   * API, so clients must not depend on this value.
   */
  private String description;

  /** The location of the field that caused the error. Value is body, path, or query. */
  private ErrorLocation location;

  /**
   * The field that caused the error. If this field is in the body, set this value to the field's
   * JSON pointer value. Required for client-side errors.
   */
  private String field;

  /** The value of the field that caused the error. */
  private String value;

  public ErrorDetail(String issue) {
    this.issue = issue;
  }

  public String getIssue() {
    return issue;
  }

  public String getDescription() {
    return description;
  }

  public ErrorDetail setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getLocation() {
    return location.name().toLowerCase();
  }

  public ErrorDetail setLocation(ErrorLocation location) {
    this.location = location;
    return this;
  }

  public String getField() {
    return field;
  }

  public ErrorDetail setField(String field) {
    this.field = field;
    return this;
  }

  public String getValue() {
    return value;
  }

  public ErrorDetail setValue(String value) {
    this.value = value;
    return this;
  }

  public enum ErrorLocation {
    BODY,
    PATH,
    QUERY,
  }
}