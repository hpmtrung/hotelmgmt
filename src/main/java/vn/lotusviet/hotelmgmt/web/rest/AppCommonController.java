package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.model.dto.system.CommonDataDto;
import vn.lotusviet.hotelmgmt.service.CommonService;

@RestController
@RequestMapping("/api/v1/common")
public class AppCommonController {

  private final CommonService commonService;

  public AppCommonController(CommonService commonService) {
    this.commonService = commonService;
  }

  @GetMapping("/customer")
  public CommonDataDto getCustomerAppCommonData() {
    return commonService.getCustomerAppCommonData();
  }

  @GetMapping("/portal")
  @PortalSecured
  public CommonDataDto getPortalAppCommonData() {
    return commonService.getPortalAppCommonData();
  }
}