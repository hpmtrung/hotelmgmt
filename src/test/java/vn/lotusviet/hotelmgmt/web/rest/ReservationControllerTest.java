package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vn.lotusviet.hotelmgmt.service.paying.PaypalService;
import vn.lotusviet.hotelmgmt.service.ReservationService;
import vn.lotusviet.hotelmgmt.service.RoomService;

@WebMvcTest(value = ReservationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ReservationControllerTest extends AbstractRestControllerTest{

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ReservationService reservationService;

  @MockBean
  private PaypalService paypalService;

  @MockBean
  private RoomService roomService;

  @Override
  protected String getUrlPrefix() {
    return ReservationController.URL_PREFIX;
  }
}