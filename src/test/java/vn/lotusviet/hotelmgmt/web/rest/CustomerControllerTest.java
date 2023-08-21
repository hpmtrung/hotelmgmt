package vn.lotusviet.hotelmgmt.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import vn.lotusviet.hotelmgmt.TestUtil;
import vn.lotusviet.hotelmgmt.WithCustomerRoleMockUser;
import vn.lotusviet.hotelmgmt.WithPortalRoleMockUser;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.CustomerService;
import vn.lotusviet.hotelmgmt.service.ReservationService;
import vn.lotusviet.hotelmgmt.service.mapper.CustomerMapper;
import vn.lotusviet.hotelmgmt.service.mapper.ReservationMapper;
import vn.lotusviet.hotelmgmt.service.reader.CustomerImportStyleSheetReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    value = CustomerController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CustomerControllerTest extends AbstractRestControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private CustomerService customerService;

  @MockBean private AccountService accountService;

  @MockBean private ReservationService reservationService;

  @MockBean private CustomerImportStyleSheetReader customerImportStyleSheetReader;

  @MockBean private CustomerMapper customerMapper;

  @MockBean private ReservationMapper reservationMapper;

  private void givenLoginCustomer(Account account, Customer customer) {
    given(accountService.getLoginAccount()).willReturn(account);
    given(customerService.getCustomerByAccountId(account.getId()))
        .willReturn(Optional.of(customer));
  }

  @Test
  @WithCustomerRoleMockUser
  void givenNotExistReservationId_whenGetDetailOfReservationOfLoginAccount_thenGetErrorResponse()
      throws Exception {
    // Given
    long reservationId = 1L;
    Account account = new Account().setId(1L);
    Customer customer = new Customer().setPersonalId("123");
    givenLoginCustomer(account, customer);
    given(reservationService.isReservationExistByIdAndOwnerId(account.getId(), customer.getPersonalId()))
        .willReturn(true);

    // When
    var resultAction = mvc.perform(get(getUrl("/reservations/" + reservationId)));

    // Then
    resultAction.andExpect(status().isBadRequest());
  }

  @Test
  @WithCustomerRoleMockUser
  void givenExistReservationId_whenGetDetailOfReservationOfLoginAccount_thenGetDetail()
      throws Exception {
    // Given
    long reservationId = 1L;
    Account account = new Account().setId(1L);
    Customer customer = new Customer().setPersonalId("123");

    givenLoginCustomer(account, customer);
    given(reservationService.isReservationExistByIdAndOwnerId(account.getId(), customer.getPersonalId()))
        .willReturn(false);

    // When
    var resultAction = mvc.perform(get(getUrl("/reservations/" + reservationId)));

    // Then
    resultAction.andExpect(status().isOk());
  }

  @Test
  @WithPortalRoleMockUser
  void givenValidImportCustomerStyleSheetFile_whenGetImportPreview_thenGetCorrectResponse()
      throws Exception {
    MockMultipartFile multipartFile =
        TestUtil.createMockMultipartFileFromFilePath(
            "file",
            "test-correct.xlsx",
            MediaType.APPLICATION_JSON_VALUE,
            "sheet/customer-import/test-correct.xlsx");
    List<Customer> customers = new ArrayList<>();
    given(customerImportStyleSheetReader.readFromFile(any(File.class))).willReturn(customers);

    var result = mvc.perform(multipart(getUrl("/import/stylesheet")).file(multipartFile));

    verify(customerMapper).toCustomerDto(customers);
    result.andExpect(status().isOk());
  }

  @Override
  protected String getUrlPrefix() {
    return CustomerController.URL_PREFIX;
  }
}