package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(value = ReservationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ReservationRestControllerTest extends AbstractRestControllerTest {



  @Override
  protected String getUrlPrefix() {
    return ReservationController.URL_PREFIX;
  }
}