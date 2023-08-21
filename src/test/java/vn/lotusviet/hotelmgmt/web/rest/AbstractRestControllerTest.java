package vn.lotusviet.hotelmgmt.web.rest;


import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import vn.lotusviet.hotelmgmt.config.TestSecurityConfig;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public abstract class AbstractRestControllerTest {
  protected abstract String getUrlPrefix();

  protected final String getUrl(String path) {
    return getUrlPrefix() + path;
  }
}