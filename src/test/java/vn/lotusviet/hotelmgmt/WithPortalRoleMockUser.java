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
import static vn.lotusviet.hotelmgmt.security.AuthorityConstants.RECEPTIONIST;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithPortalRoleMockUser.Factory.class)
public @interface WithPortalRoleMockUser {
  class Factory implements WithSecurityContextFactory<WithPortalRoleMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithPortalRoleMockUser annotation) {
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(
          new UsernamePasswordAuthenticationToken(
              "PORTAL",
              "TOKEN",
              List.of(
                  new SimpleGrantedAuthority(RECEPTIONIST), new SimpleGrantedAuthority(ADMIN))));
      return securityContext;
    }
  }
}