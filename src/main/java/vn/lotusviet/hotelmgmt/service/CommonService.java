package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.system.CommonDataDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;

public interface CommonService {

  String COMMON_DATA_APP_CACHE_NAME = "commonDataCustomerAppCache";

  CommonDataDto getCustomerAppCommonData();

  CommonDataDto getPortalAppCommonData();

  PaymentMethod getPaymentMethodByCode(PaymentMethodCode paymentMethodCode);

  ReservationStatus getReservationStatusByCode(ReservationStatusCode statusCode);

  RentalStatus getRentalStatusByCode(RentalStatusCode statusCode);

  RoomStatus getRoomStatusByCode(RoomStatusCode roomStatusCode);

  SuiteStyle getSuiteStyleById(int id);

  SuiteType getSuiteTypeById(int id);

  ServiceType getServiceTypeById(int id);

  Service getServiceById(int serviceId);

  void clearCache();
}