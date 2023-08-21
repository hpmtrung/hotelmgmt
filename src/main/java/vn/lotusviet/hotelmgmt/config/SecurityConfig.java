package vn.lotusviet.hotelmgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.security.DomainUserDetailsService;
import vn.lotusviet.hotelmgmt.security.jwt.JWTFilter;
import vn.lotusviet.hotelmgmt.security.jwt.TokenProvider;
import vn.lotusviet.hotelmgmt.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import vn.lotusviet.hotelmgmt.security.oauth.OAuth2AuthenticationFailureHandler;
import vn.lotusviet.hotelmgmt.security.oauth.OAuth2AuthenticationSuccessHandler;

@Profile("!test")
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {

  private final JWTFilter jwtFilter;
  private final CorsFilter corsFilter;
  private final ApplicationProperties.Security securityProperties;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
  private final DomainUserDetailsService domainUserDetailsService;
  private final HttpCookieOAuth2AuthorizationRequestRepository requestRepository;

  public SecurityConfig(
      TokenProvider tokenProvider,
      CorsFilter corsFilter,
      ApplicationProperties applicationProperties,
      OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
      OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
      DomainUserDetailsService domainUserDetailsService,
      HttpCookieOAuth2AuthorizationRequestRepository requestRepository) {
    this.jwtFilter = new JWTFilter(tokenProvider);
    this.corsFilter = corsFilter;
    this.securityProperties = applicationProperties.getSecurity();
    this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    this.domainUserDetailsService = domainUserDetailsService;
    this.requestRepository = requestRepository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().formLogin().disable();

    // Filter
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // Header policies
    http.headers(
        config -> {
          config.contentSecurityPolicy(securityProperties.getContentSecurityPolicy());
          config.referrerPolicy(
              ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN);
          config.permissionsPolicy().policy(securityProperties.getPermissionSecurityPolicy());
          config.frameOptions().deny();
        });

    // Disable "JSESSIONID" cookies
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Endpoint protection
    http.authorizeHttpRequests(
        auth -> {
          auth.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();

          auth.antMatchers(
                  "/oauth2/**",
                  "**/*swagger*/**",
                  "**/api-docs",
                  "/api/**/common/**",
                  "/api/**/authenticate/**")
              .permitAll();

          // Management endpoints
          auth.antMatchers("/management/**").hasAnyRole(AuthorityName.ROLE_ADMIN.name());

          auth.antMatchers("/api/**").permitAll();
        });

    http.oauth2Login(
        config -> {
          config
              .authorizationEndpoint()
              .baseUri("/oauth2/authorize")
              .authorizationRequestRepository(requestRepository);
          config.redirectionEndpoint().baseUri("/oauth2/callback/*");
          config.userInfoEndpoint().userService(domainUserDetailsService);
          config.successHandler(oAuth2AuthenticationSuccessHandler);
          config.failureHandler(oAuth2AuthenticationFailureHandler);
        });

    http.httpBasic();

    return http.build();
  }
}