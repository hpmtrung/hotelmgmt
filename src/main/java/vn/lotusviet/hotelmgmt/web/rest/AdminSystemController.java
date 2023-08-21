package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.model.dto.system.AdminDashboardDataDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleUpdateDto;
import vn.lotusviet.hotelmgmt.service.AdminSystemService;
import vn.lotusviet.hotelmgmt.service.SystemService;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@AdminSecured
@RestController
@Validated
@RequestMapping("/api/v1/admin_system")
public class AdminSystemController {

  private final SystemService systemService;
  private final AdminSystemService adminSystemService;

  public AdminSystemController(SystemService systemService, AdminSystemService adminSystemService) {
    this.systemService = systemService;
    this.adminSystemService = adminSystemService;
  }

  @GetMapping("/dashboard")
  public AdminDashboardDataDto getDashboardData() {
    return adminSystemService.getDashboardData();
  }

  @GetMapping("/config/business")
  public List<BusinessRuleDto> getAllBusinessConfigurations() {
    return systemService.getAllBusinessRules();
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/config/business/update")
  public List<BusinessRuleDto> updateBusinessConfiguration(
      @Valid @RequestBody BusinessRuleUpdateDto ruleUpdateDto) {
    return adminSystemService.updateBusinessRules(ruleUpdateDto);
  }
}