package vn.lotusviet.hotelmgmt.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class ErrorResponse {

  private final HttpStatus status;

  /** The message that describes the error. */
  private final String message;

  /** The human-readable, unique name of the error. */
  private final String name;

  /**
   * The information link, or URI, that shows detailed information about this error for the
   * developer.
   */
  private String informationLink;

  /** An array of additional details about the error. */
  private List<ErrorDetail> details;

  private ErrorResponse(Builder builder) {
    status = builder.status;
    name = builder.name;
    message = builder.message;
    informationLink = builder.informationLink;
    details = builder.details;
  }

  public static Builder builder(HttpStatus status, String name, String message) {
    return new Builder(status, name, message);
  }

  public List<ErrorDetail> getDetails() {
    return details;
  }

  public ErrorResponse setDetails(List<ErrorDetail> details) {
    this.details = details;
    return this;
  }

  @JsonIgnore
  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getName() {
    return name;
  }

  public String getInformationLink() {
    return informationLink;
  }

  public ErrorResponse setInformationLink(String informationLink) {
    this.informationLink = informationLink;
    return this;
  }

  public static class Builder {
    private final HttpStatus status;
    private final String name;
    private final String message;
    private String informationLink;
    private List<ErrorDetail> details;

    private Builder(HttpStatus status, String name, String message) {
      this.status = status;
      this.name = name;
      this.message = message;
    }

    public Builder withDetail(List<ErrorDetail> detail) {
      this.details = detail;
      return this;
    }

    public Builder withInformationLink(String link) {
      this.informationLink = link;
      return this;
    }

    public ErrorResponse build() {
      return new ErrorResponse(this);
    }
  }
}