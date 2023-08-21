package vn.lotusviet.hotelmgmt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

  private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

  @Value("${application.web-client-base-url}")
  private String weburl;

  @Bean
  public CorsConfiguration corsConfiguration() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin(weburl);
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.setExposedHeaders(List.of("Authorization", "Link", "X-Entity-Count"));
    config.setMaxAge(1200L);
    return config;
  }

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = corsConfiguration();

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    source.registerCorsConfiguration("/management/**", config);

    log.debug(
        "Cors config: allowedOrigins '{}', allowedOriginPatterns '{}'",
        config.getAllowedOrigins(),
        config.getAllowedOriginPatterns());
    return new CorsFilter(source);
  }
}