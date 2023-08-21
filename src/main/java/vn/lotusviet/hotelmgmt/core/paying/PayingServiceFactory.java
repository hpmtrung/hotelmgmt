package vn.lotusviet.hotelmgmt.core.paying;

import org.springframework.stereotype.Service;
import vn.lotusviet.hotelmgmt.service.paying.PayingService;
import vn.lotusviet.hotelmgmt.service.paying.PaypalService;
import vn.lotusviet.hotelmgmt.service.paying.VNPayService;

import java.util.Arrays;
import java.util.Optional;

import static vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory.ServiceParty.PAYPAL;
import static vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory.ServiceParty.VNPAY;

@Service
public class PayingServiceFactory {

  private final PaypalService paypalService;
  private final VNPayService vnPayService;

  public PayingServiceFactory(PaypalService paypalService, VNPayService vnPayService) {
    this.paypalService = paypalService;
    this.vnPayService = vnPayService;
  }

  public PayingService getDefaultPayingService() {
    return paypalService;
  }

  public PayingService getPayingService(ServiceParty party) {
    if (party.equals(VNPAY)) {
      return vnPayService;
    } else if (party.equals(PAYPAL)) {
      return paypalService;
    }
    throw new ServicePartyNotFound(party);
  }

  public enum ServiceParty {
    PAYPAL,
    VNPAY;

    public static Optional<ServiceParty> fromString(String party) {
      return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(party)).findAny();
    }
  }

  public static class ServicePartyNotFound extends RuntimeException {

    private static final long serialVersionUID = 102363081023174462L;
    private final ServiceParty party;

    public ServicePartyNotFound(ServiceParty party) {
      super("Service party not found " + party.name());
      this.party = party;
    }

    public ServiceParty getParty() {
      return party;
    }
  }
}