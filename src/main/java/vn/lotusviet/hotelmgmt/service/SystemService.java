package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey;

import java.util.List;

public interface SystemService {

  List<BusinessRuleDto> getAllBusinessRules();

  <T> T getBusinessRuleValue(BusinessRuleKey key, Class<T> resultClass);
}