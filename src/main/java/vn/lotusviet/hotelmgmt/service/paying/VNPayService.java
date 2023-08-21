package vn.lotusviet.hotelmgmt.service.paying;

import org.springframework.stereotype.Service;
import vn.lotusviet.hotelmgmt.core.paying.PaymentDetail;
import vn.lotusviet.hotelmgmt.core.paying.PaymentResponse;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;

@Service
public class VNPayService implements PayingService{
  @Override
  public String getApproveURLFromPaymentCreation(ReservationDto reservation) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PaymentResponse executePayment(String paymentId, String payerId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PaymentDetail getPaymentDetail(String orderId) {
    throw new UnsupportedOperationException();
  }
}