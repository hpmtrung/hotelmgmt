package vn.lotusviet.hotelmgmt.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import vn.lotusviet.hotelmgmt.security.ratelimit.APIBucketManager;
import vn.lotusviet.hotelmgmt.security.ratelimit.APILimitInterceptor;

import java.time.Duration;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Override
  public void addInterceptors(@NotNull InterceptorRegistry registry) {
    boolean isNotDevProfile = !activeProfile.equalsIgnoreCase("dev");
    if (isNotDevProfile) {
      registry
          .addInterceptor(
              new APILimitInterceptor(new APIBucketManager(), 50, Duration.ofMinutes(1)))
          .addPathPatterns("/api/**")
          .excludePathPatterns("/api/**/common/**", "/api/**/authenticate/**", "/oauth2/**");
    }

    registry.addInterceptor(getLocaleChangeInterceptor());
  }

  private LocaleChangeInterceptor getLocaleChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("language");
    return localeChangeInterceptor;
  }
}