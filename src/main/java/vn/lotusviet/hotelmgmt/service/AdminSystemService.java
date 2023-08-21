package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.system.AdminDashboardDataDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleUpdateDto;

import java.util.List;

public interface AdminSystemService {

  AdminDashboardDataDto getDashboardData();

  List<BusinessRuleDto> updateBusinessRules(BusinessRuleUpdateDto ruleUpdateDto);
}