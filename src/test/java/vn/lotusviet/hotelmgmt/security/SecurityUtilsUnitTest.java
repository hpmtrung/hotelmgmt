package vn.lotusviet.hotelmgmt.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.lotusviet.hotelmgmt.util.SecurityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUtilsUnitTest {

  @BeforeEach
  @AfterEach
  void cleanup() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void testGetCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    Optional<String> login = SecurityUtil.getCurrentAccountLogin();
    assertThat(login).contains("admin");
  }

  @Test
  void testGetCurrentUserJWT() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
    SecurityContextHolder.setContext(securityContext);
    Optional<String> jwt = SecurityUtil.getCurrentAccountJWT();
    assertThat(jwt).contains("token");
  }

  @Test
  void testIsAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtil.isAuthenticated();
    assertThat(isAuthenticated).isTrue();
  }

  @Test
  void testAnonymousIsNotAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthorityConstants.ANONYMOUS));
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtil.isAuthenticated();
    assertThat(isAuthenticated).isFalse();
  }

  @Test
  void testHasCurrentUserThisAuthority() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthorityConstants.ANONYMOUS));
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtil.hasCurrentUserThisAuthority(AuthorityConstants.ANONYMOUS)).isTrue();
    assertThat(SecurityUtil.hasCurrentUserThisAuthority(AuthorityConstants.ADMIN)).isFalse();
  }

  @Test
  void testHasCurrentUserAnyOfAuthorities() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthorityConstants.ANONYMOUS));
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(
            SecurityUtil.hasCurrentUserAnyOfAuthorities(
                AuthorityConstants.ANONYMOUS, AuthorityConstants.ADMIN))
        .isTrue();
  }

  @Test
  void testHasCurrentUserNoneOfAuthorities() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthorityConstants.ANONYMOUS));
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(
            SecurityUtil.hasCurrentUserNoneOfAuthorities(
                AuthorityConstants.ANONYMOUS, AuthorityConstants.ADMIN))
        .isFalse();
  }
}