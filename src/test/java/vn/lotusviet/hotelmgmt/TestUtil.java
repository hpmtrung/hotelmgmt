package vn.lotusviet.hotelmgmt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import vn.lotusviet.hotelmgmt.core.exception.ErrorDetail;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Utility class for testing REST controllers. */
public final class TestUtil {

  private static final ObjectMapper mapper = createObjectMapper();

  private TestUtil() {}

  private static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public static MockMultipartFile createMockMultipartFileFromFilePath(
      String paramName, String originalFileName, String contentType, String resourcePath)
      throws URISyntaxException, IOException {
    return new MockMultipartFile(
        paramName, originalFileName, contentType, TestUtil.getResourceAsStream(resourcePath));
  }

  public static MockMultipartFile createMockMultipartFileFromByteArray(
      String paramName, String originalFileName, String contentType, Object content)
      throws IOException {
    return new MockMultipartFile(
        paramName, originalFileName, contentType, convertObjectToJsonBytes(content));
  }

  public static void verifyErrorResponseWithFields(
      ResultActions resultActions, HttpStatus status, String name) throws Exception {
    resultActions.andExpect(status().is(status.value()));
    resultActions.andExpect(jsonPath("$.name").value(name));
  }

  public static void verifyErrorResponseWithFields(
      ResultActions resultActions, HttpStatus status, String name, List<ErrorDetail> errorDetails)
      throws Exception {
    verifyErrorResponseWithFields(resultActions, status, name);
    for (int index = 0; index < errorDetails.size(); index++) {
      ErrorDetail errorDetail = errorDetails.get(index);
      resultActions.andExpect(
          jsonPath(String.format("$.details.[%d].issue", index)).value(errorDetail.getIssue()));
    }
  }

  public static InputStream getResourceAsStream(String path) {
    InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream(path);
    if (inputStream == null) {
      throw new IllegalArgumentException("File is not found");
    }
    return inputStream;
  }

  public static File getResourceAsFile(String path) throws URISyntaxException {
    URL resource = TestUtil.class.getClassLoader().getResource(path);
    if (resource == null) {
      throw new IllegalArgumentException("File is not found");
    }
    return new File(resource.toURI());
  }

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert.
   * @return the JSON byte array.
   */
  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    return mapper.writeValueAsBytes(object);
  }

  /**
   * Create a byte array with a specific size filled with specified data.
   *
   * @param size the size of the byte array.
   * @param data the data to put in the byte array.
   * @return the JSON byte array.
   */
  public static byte[] createByteArray(int size, String data) {
    byte[] byteArray = new byte[size];
    for (int i = 0; i < size; i++) {
      byteArray[i] = Byte.parseByte(data, 2);
    }
    return byteArray;
  }

  /**
   * Creates a matcher that matches when the examined string represents the same instant as the
   * reference datetime.
   *
   * @param date the reference datetime against which the examined string is checked.
   */
  public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date) {
    return new ZonedDateTimeMatcher(date);
  }

  /**
   * Creates a matcher that matches when the examined number represents the same value as the
   * reference BigDecimal.
   *
   * @param number the reference BigDecimal against which the examined number is checked.
   */
  public static NumberMatcher sameNumber(BigDecimal number) {
    return new NumberMatcher(number);
  }

  /** Verifies the equals/hashcode contract on the domain object. */
  public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
    T domainObject1 = clazz.getConstructor().newInstance();
    assertThat(domainObject1.toString()).isNotNull();
    assertThat(domainObject1).isEqualTo(domainObject1);
    assertThat(domainObject1).hasSameHashCodeAs(domainObject1);
    // Test with an instance of another class
    Object testOtherObject = new Object();
    assertThat(domainObject1).isNotEqualTo(testOtherObject);
    assertThat(domainObject1).isNotEqualTo(null);
    // Test with an instance of the same class
    T domainObject2 = clazz.getConstructor().newInstance();
    assertThat(domainObject1).isNotEqualTo(domainObject2);
    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1).hasSameHashCodeAs(domainObject2);
  }

  /**
   * Create a {@link FormattingConversionService} which use ISO date format, instead of the
   * localized one.
   *
   * @return the {@link FormattingConversionService}.
   */
  public static FormattingConversionService createFormattingConversionService() {
    DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
    DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
    registrar.setUseIsoFormat(true);
    registrar.registerFormatters(dfcs);
    return dfcs;
  }

  /**
   * Makes a an executes a query to the EntityManager finding all stored objects.
   *
   * @param <T> The errorType of objects to be searched
   * @param em The instance of the EntityManager
   * @param clss The class errorType to be searched
   * @return A list of all found objects
   */
  public static <T> List<T> findAll(EntityManager em, Class<T> clss) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(clss);
    Root<T> rootEntry = cq.from(clss);
    CriteriaQuery<T> all = cq.select(rootEntry);
    TypedQuery<T> allQuery = em.createQuery(all);
    return allQuery.getResultList();
  }

  /**
   * A matcher that tests that the examined string represents the same instant as the reference
   * datetime.
   */
  public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String> {

    private final ZonedDateTime date;

    public ZonedDateTimeMatcher(ZonedDateTime date) {
      this.date = date;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a String representing the same Instant as ").appendValue(date);
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
      try {
        if (!date.isEqual(ZonedDateTime.parse(item))) {
          mismatchDescription.appendText("was ").appendValue(item);
          return false;
        }
        return true;
      } catch (DateTimeParseException e) {
        mismatchDescription
            .appendText("was ")
            .appendValue(item)
            .appendText(", which could not be parsed as a ZonedDateTime");
        return false;
      }
    }
  }

  /**
   * A matcher that tests that the examined number represents the same value - it can be Long,
   * Double, etc - as the reference BigDecimal.
   */
  public static class NumberMatcher extends TypeSafeMatcher<Number> {

    final BigDecimal value;

    public NumberMatcher(BigDecimal value) {
      this.value = value;
    }

    private static BigDecimal asDecimal(Number item) {
      if (item == null) {
        return null;
      }
      if (item instanceof BigDecimal) {
        return (BigDecimal) item;
      } else if (item instanceof Long) {
        return BigDecimal.valueOf((Long) item);
      } else if (item instanceof Integer) {
        return BigDecimal.valueOf((Integer) item);
      } else if (item instanceof Double) {
        return BigDecimal.valueOf((Double) item);
      } else if (item instanceof Float) {
        return BigDecimal.valueOf((Float) item);
      } else {
        return BigDecimal.valueOf(item.doubleValue());
      }
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a numeric value is ").appendValue(value);
    }

    @Override
    protected boolean matchesSafely(Number item) {
      BigDecimal bigDecimal = asDecimal(item);
      return bigDecimal != null && value.compareTo(bigDecimal) == 0;
    }
  }
}