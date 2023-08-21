package vn.lotusviet.hotelmgmt.service.paying;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.PayingService.PaypalConfig;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.exception.PaypalRestException;
import vn.lotusviet.hotelmgmt.core.paying.PaymentDetail;
import vn.lotusviet.hotelmgmt.core.paying.PaymentResponse;
import vn.lotusviet.hotelmgmt.core.paying.paypal.*;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.util.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PaypalService implements PayingService {

  private static final String DEFAULT_CURRENCY_CODE = "USD";
  private static final String SANDBOX_URL = "https://api-m.sandbox.paypal.com";

  private static final Logger log = LoggerFactory.getLogger(PaypalService.class);

  private final ApplicationContext apiContext = new ApplicationContext();
  private final PaypalConfig paypalConfig;

  private String accessToken;

  public PaypalService(ApplicationProperties applicationProperties) throws IOException {
    this.paypalConfig = applicationProperties.getPayingService().getPaypal();
    apiContext.setCancelUrl(paypalConfig.getCancelUrl()).setReturnUrl(paypalConfig.getReturnUrl());
    getAccessToken();
  }

  private static Item getItemFromReservationDetail(final ReservationDetailDto detail) {
    SuiteDto suite = detail.getSuite();
    return new Item()
        .setName(suite.getName())
        .setQuantity(String.valueOf(detail.getRoomNum()))
        .setUnitAmount(getUnitAmount(detail));
  }

  private static UnitAmount getUnitAmount(final ReservationDetailDto detail) {
    return new UnitAmount()
        .setCurrencyCode(DEFAULT_CURRENCY_CODE)
        .setValue(String.valueOf(detail.getSubTotal() / detail.getRoomNum()));
  }

  @Override
  @LogAround
  public String getApproveURLFromPaymentCreation(final ReservationDto reservation) {
    List<Item> items = getItems(reservation);
    Breakdown breakdown = getBreakdown(reservation);
    Amount amount = getAmount(reservation, breakdown);
    PurchaseUnit purchaseUnit = getPurchaseUnit(reservation, items, amount);
    PaymentRequest paymentRequest = getPaymentRequest(purchaseUnit);

    log.debug("Create payment: {}", paymentRequest);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");

    final String contentBeforeSending = JacksonUtil.toString(paymentRequest);
    log.debug("Body before sending: {}", contentBeforeSending);

    RequestBody body = RequestBody.create(JacksonUtil.toString(paymentRequest), mediaType);

    Request request =
        new Request.Builder()
            .url(SANDBOX_URL + "/v2/checkout/orders")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=representation")
            .addHeader("Authorization", "Bearer " + accessToken)
            .build();

    String approveURL;
    try {
      Response response = client.newCall(request).execute();
      PaypalPaymentResponse paypalPaymentResponse =
          JacksonUtil.fromString(
              Objects.requireNonNull(response.body()).string(), PaypalPaymentResponse.class);

      log.debug("Payment creation response: {}", paypalPaymentResponse);

      approveURL = getApproveURL(paypalPaymentResponse).orElse(null);

    } catch (IOException e) {
      throw new PaypalRestException("Create paypal payment error", e);
    }

    if (approveURL == null) {
      throw new PaypalRestException("Payment creation is not accepted!");
    }

    return approveURL;
  }

  @Override
  @LogAround
  public PaymentDetail getPaymentDetail(final String orderId) {
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    MediaType mediaType = MediaType.parse("text/plain");

    RequestBody body = RequestBody.create("", mediaType);
    Request request =
        new Request.Builder()
            .url(SANDBOX_URL + "/v2/checkout/orders/" + orderId)
            .method("GET", body)
            .build();

    PaypalPaymentDetail paypalPaymentDetail;

    try {
      Response response = client.newCall(request).execute();

      paypalPaymentDetail =
          JacksonUtil.fromString(
              Objects.requireNonNull(response.body()).string(), PaypalPaymentDetail.class);

    } catch (IOException e) {
      throw new PaypalRestException("Fail to get payment detail", e);
    }

    if (paypalPaymentDetail == null) {
      throw new PaypalRestException("Payment is not found");
    }

    return paypalPaymentDetail;
  }

  @Override
  @LogAround
  public PaymentResponse executePayment(final String paymentId, final String payerId) {
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("", mediaType);

    Request request =
        new Request.Builder()
            .url(SANDBOX_URL + "/v2/checkout/orders/" + paymentId + "/authorize")
            .method("POST", body)
            .addHeader("Authorization", "Bearer " + accessToken)
            .addHeader("Content-Type", "application/json")
            .addHeader("Content-Length", "0")
            .addHeader("Prefer", "return=representation")
            .build();

    PaypalPaymentResponse paypalPaymentResponse;

    try {
      Response response = client.newCall(request).execute();

      paypalPaymentResponse =
          JacksonUtil.fromString(
              Objects.requireNonNull(response.body()).string(), PaypalPaymentResponse.class);

      if (paypalPaymentResponse.getId() == null) {
        throw new PaypalRestException("Fail to execute payment: " + paypalPaymentResponse);
      }
    } catch (IOException e) {
      throw new PaypalRestException(e);
    }

    return paypalPaymentResponse;
  }

  private void getAccessToken() throws IOException {
    OkHttpClient client = new OkHttpClient.Builder().authenticator(this::getAuthenticator).build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create("grant_type=client_credentials", mediaType);
    Request request =
        new Request.Builder().url(SANDBOX_URL + "/v1/oauth2/token").method("POST", body).build();

    AccessToken token;

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      token =
          JacksonUtil.fromString(
              Objects.requireNonNull(response.body()).string(), AccessToken.class);
    }

    if (token != null && token.getAccessToken() != null) {
      this.accessToken = token.getAccessToken();
      log.debug("Access token: {}", token);
    } else {
      throw new PaypalRestException("Token is not found!");
    }
  }

  private List<Item> getItems(ReservationDto reservation) {
    return reservation.getDetails().stream()
        .map(PaypalService::getItemFromReservationDetail)
        .collect(Collectors.toList());
  }

  private PaymentRequest getPaymentRequest(PurchaseUnit purchaseUnit) {
    return new PaymentRequest()
        .setApplicationContext(apiContext)
        .setIntent("AUTHORIZE")
        .setPurchaseUnits(List.of(purchaseUnit));
  }

  private PurchaseUnit getPurchaseUnit(
      ReservationDto reservation, List<Item> items, Amount amount) {
    return new PurchaseUnit()
        .setAmount(amount)
        .setInvoiceId(String.valueOf(reservation.getId()))
        .setItems(items);
  }

  private Amount getAmount(ReservationDto reservation, Breakdown breakdown) {
    return new Amount()
        .setValue(String.valueOf(reservation.getTotal()))
        .setCurrencyCode(DEFAULT_CURRENCY_CODE)
        .setBreakdown(breakdown);
  }

  private Breakdown getBreakdown(ReservationDto reservation) {
    return new Breakdown()
        .setItemTotal(
            new ItemTotal()
                .setCurrencyCode(DEFAULT_CURRENCY_CODE)
                .setValue(String.valueOf(reservation.getTotal())));
  }

  private Optional<String> getApproveURL(PaypalPaymentResponse response) {
    List<Link> linksList = response.getLinks();
    String approvalLink = null;
    for (Link link : linksList) {
      if (link.getRel().value().equalsIgnoreCase("approve")) {
        approvalLink = link.getHref();
      }
    }
    return Optional.ofNullable(approvalLink);
  }

  private Request getAuthenticator(Route route, Response response) {
    if (response.request().header("Authorization") != null) {
      return null;
    }

    String credential =
        Credentials.basic(paypalConfig.getClientId(), paypalConfig.getClientSecret());

    return response.request().newBuilder().header("Authorization", credential).build();
  }
}