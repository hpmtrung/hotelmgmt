package vn.lotusviet.hotelmgmt.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.util.SecurityUtil;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(SecurityUtil.getCurrentAccountLogin().orElse("system"));
  }
}