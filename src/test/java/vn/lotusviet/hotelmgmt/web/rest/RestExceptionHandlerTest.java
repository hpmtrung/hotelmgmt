package vn.lotusviet.hotelmgmt.web.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ServerErrorException;
import vn.lotusviet.hotelmgmt.TestUtil;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.web.rest.sut.RestExceptionHandlerTestController;
import vn.lotusviet.hotelmgmt.web.rest.sut.TestService;

import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    value = RestExceptionHandlerTestController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class RestExceptionHandlerTest extends AbstractRestControllerTest {

  private static final String INVALID_ARGUMENT_ISSUE =
      "Request is not well-formed, syntactically incorrect, or violates schema.";

  @Autowired private MockMvc mvc;
  @MockBean private TestService testService;

  @Test
  void whenHasAccessDeniedError_thenGetCorrectResponse() throws Exception {
    AccessDeniedException exception = new AccessDeniedException("Cannot access");
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isForbidden());
    verifyMandatoryJsonFields(result, exception.getMessage(), "FORBIDDEN");
  }

  @Test
  void whenHasAuthenticationError_thenGetCorrectResponse() throws Exception {
    var exception = new BadCredentialsException("bad credentials");
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isUnauthorized());
    verifyMandatoryJsonFields(result, exception.getMessage(), "INVALID_CLIENT");
  }

  @Test
  void whenHasMethodArgumentNotValid_thenGetCorrectResponse() throws Exception {
    var result =
        mvc.perform(
            post(getUrl("/method_argument"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Map.of("another", "abc"))));

    verifyMandatoryJsonFields(result, INVALID_ARGUMENT_ISSUE, "INVALID_REQUEST");
    result
        .andExpect(jsonPath("$.details.[0].issue").isNotEmpty())
        .andExpect(jsonPath("$.details.[0].description").isNotEmpty())
        .andExpect(jsonPath("$.details.[0].location").value("query"))
        .andExpect(jsonPath("$.details.[0].field").value("test"))
        .andExpect(jsonPath("$.details.[0].value").isEmpty());
  }

  @Test
  void whenHasConstraintViolated_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/constraint_violated")).param("email", "abc"));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, INVALID_ARGUMENT_ISSUE, "INVALID_REQUEST");
    result
        .andExpect(jsonPath("$.details.[0].issue").isNotEmpty())
        .andExpect(jsonPath("$.details.[0].description").isNotEmpty())
        .andExpect(jsonPath("$.details.[0].location").value("query"))
        .andExpect(jsonPath("$.details.[0].field").value("email"))
        .andExpect(jsonPath("$.details.[0].value").value("abc"));
  }

  @Test
  void whenHasEntityIdNotFoundException_thenGetCorrectResponse() throws Exception {
    var exception = InvalidParamException.builder().body("id.notNull", "id", "anyValue").build();
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, INVALID_ARGUMENT_ISSUE, "INVALID_REQUEST");
    result
        .andExpect(jsonPath("$.details.[0].issue").value("id.notNull"))
        .andExpect(jsonPath("$.details.[0].description").isNotEmpty())
        .andExpect(jsonPath("$.details.[0].location").value("body"))
        .andExpect(jsonPath("$.details.[0].field").value("id"))
        .andExpect(jsonPath("$.details.[0].value").value("anyValue"));
  }

  @Test
  void whenHasInternalServerError_thenGetCorrectResponse() throws Exception {
    var exception = new ServerErrorException("any reason", new RuntimeException("message"));
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isInternalServerError());
    verifyMandatoryJsonFields(
        result, "An internal server error has occurred.", "INTERNAL_SERVER_ERROR");
  }

  @Test
  void whenHasConcurrencyError_thenGetCorrectResponse() throws Exception {
    var exception = new ConcurrencyFailureException("concurrency failure");
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isConflict());
    verifyMandatoryJsonFields(
        result, "Conflict due to concurrency failure.", "CONCURRENCY_FAILURE");
  }

  @Test
  void whenHasMultipartError_thenGetCorrectResponse() throws Exception {
    var exception = new MultipartException("size limit");
    given(testService.throwsInternalException()).willThrow(exception);

    var result = mvc.perform(get(getUrl("/testGet")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, exception.getMessage(), "MULTIPART_FAILURE");
  }

  @Test
  @Disabled("Unknown fail reason")
  void whenHasRequestMethodNotSupportedException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(delete(getUrl("/not_supported_method")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "METHOD_NOT_SUPPORT");
  }

  @Test
  void whenHasTypeMisMatchException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/type_mis_match")).param("key", "abc111"));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "PROPERTY_TYPE_INCORRECT");
  }

  @Test
  void whenHasMediaTypeNotSupportException_thenGetCorrectResponse() throws Exception {
    var result =
        mvc.perform(get(getUrl("/media_type_not_support")).contentType(MediaType.APPLICATION_JSON));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "MEDIA_TYPE_NOT_SUPPORT");
  }

  @Test
  void whenHasMediaTypeNotAcceptableException_thenGetCorrectResponse() throws Exception {
    var result =
        mvc.perform(get(getUrl("/media_type_not_accept")).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "MEDIA_TYPE_NOT_ACCEPT");
  }

  @Test
  @Disabled("Unknown fail reason")
  void whenHasMessageNotReadableException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/message_not_readable")).param("date", "2022-02-31"));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "PROPERTY_VALUE_INCORRECT");
  }

  @Test
  void whenHasMissingServletRequestParameterException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/missing_request_parameter")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "MISSING_REQUEST_PARAM");
  }

  @Test
  void whenHasMissingServletRequestPartException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/missing_request_part")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "MISSING_REQUEST_PART");
  }

  @Test
  @Disabled("Unknown fail reason")
  void whenHasMissingServletRequestPathVariableException_thenGetCorrectResponse() throws Exception {
    var result = mvc.perform(get(getUrl("/missing_request_path_variable/")));

    result.andExpect(status().isBadRequest());
    verifyMandatoryJsonFields(result, "MISSING_REQUEST_PATH_VARIABLE");
  }

  @Override
  protected String getUrlPrefix() {
    return RestExceptionHandlerTestController.URL_PREFIX;
  }

  private void verifyMandatoryJsonFields(ResultActions resultActions, String message, String name)
      throws Exception {
    resultActions
        .andExpect(jsonPath("$.message").value(message))
        .andExpect(jsonPath("$.name").value(name));
  }

  private void verifyMandatoryJsonFields(ResultActions resultActions, String name)
      throws Exception {
    resultActions
        .andExpect(jsonPath("$.message").isNotEmpty())
        .andExpect(jsonPath("$.name").value(name));
  }
}