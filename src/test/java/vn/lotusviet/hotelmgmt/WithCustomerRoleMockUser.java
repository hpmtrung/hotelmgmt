package vn.lotusviet.hotelmgmt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import vn.lotusviet.hotelmgmt.security.AuthorityConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomerRoleMockUser.Factory.class)
public @interface WithCustomerRoleMockUser {
  class Factory implements WithSecurityContextFactory<WithCustomerRoleMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomerRoleMockUser annotation) {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(
          new UsernamePasswordAuthenticationToken(
              "CUSTOMER",
              "TOKEN",
              List.of(new SimpleGrantedAuthority(AuthorityConstants.CUSTOMER))));
      return securityContext;
    }
  }
}