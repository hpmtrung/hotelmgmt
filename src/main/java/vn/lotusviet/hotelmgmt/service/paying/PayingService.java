package vn.lotusviet.hotelmgmt.service.paying;

import vn.lotusviet.hotelmgmt.core.paying.PaymentDetail;
import vn.lotusviet.hotelmgmt.core.paying.PaymentResponse;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;

public interface PayingService {

  String getApproveURLFromPaymentCreation(ReservationDto reservation);

  PaymentResponse executePayment(String paymentId, String payerId);

  PaymentDetail getPaymentDetail(String orderId);
}