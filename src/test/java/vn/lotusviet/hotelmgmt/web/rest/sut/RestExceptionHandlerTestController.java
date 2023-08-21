package vn.lotusviet.hotelmgmt.web.rest.sut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RestController
@RequestMapping(RestExceptionHandlerTestController.URL_PREFIX)
@Validated
@Profile("test")
public class RestExceptionHandlerTestController {

  public static final String URL_PREFIX = "/api/test/exception_handler";

  @Autowired private TestService testService;

  @GetMapping("/testGet")
  public void testGet() {
    testService.throwsInternalException();
  }

  @PostMapping("/method_argument")
  public void methodArgument(@Valid @RequestBody TestDto testDto) {}

  @GetMapping("/constraint_violated")
  public void constraintViolated(@RequestParam @Email String email) {}

  @GetMapping("/missing_request_part")
  public void missingServletRequestPartException(@RequestPart String part) {}

  @GetMapping("/missing_request_parameter")
  public void missingServletRequestParameterException(@RequestParam String param) {}

  @GetMapping("/missing_request_path_variable/{key}")
  public void missingServletRequestPathVariableException(@PathVariable String key) {}

  // @DeleteMapping("/not_supported")
  // public void notSupportedMethod() {}

  @GetMapping("/type_mis_match")
  public void typeMisMatchException(@RequestParam Integer key) {}

  @GetMapping(
      value = "/media_type_not_support",
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public void mediaTypeNotSupport() {}

  @GetMapping(
      value = "/media_type_not_accept",
      produces = {MediaType.APPLICATION_XML_VALUE})
  public void mediaTypeNotAccept() {}

  @GetMapping("/message_not_readable")
  public void messageNotReadable(@RequestParam LocalDate date) {}

  public static class TestDto {

    @NotNull private String test;

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
  @SuppressWarnings("serial")
  public static class TestResponseStatusException extends RuntimeException {}
}