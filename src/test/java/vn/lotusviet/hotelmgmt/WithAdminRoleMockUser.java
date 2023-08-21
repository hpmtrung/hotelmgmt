package vn.lotusviet.hotelmgmt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static vn.lotusviet.hotelmgmt.security.AuthorityConstants.ADMIN;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAdminRoleMockUser.Factory.class)
public @interface WithAdminRoleMockUser {
  class Factory implements WithSecurityContextFactory<WithAdminRoleMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithAdminRoleMockUser annotation) {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(
          new UsernamePasswordAuthenticationToken(
              "ADMIN", "TOKEN", List.of(new SimpleGrantedAuthority(ADMIN))));
      return securityContext;
    }
  }
}