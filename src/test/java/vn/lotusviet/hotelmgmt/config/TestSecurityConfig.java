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
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

@Profile("test")
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class TestSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().formLogin().disable();

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

    http.httpBasic();

    return http.build();
  }
}