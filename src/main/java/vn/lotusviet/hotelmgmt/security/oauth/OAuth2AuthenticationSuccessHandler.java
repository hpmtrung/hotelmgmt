package vn.lotusviet.hotelmgmt.security.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.core.exception.BadRequestException;
import vn.lotusviet.hotelmgmt.security.jwt.TokenProvider;
import vn.lotusviet.hotelmgmt.util.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static vn.lotusviet.hotelmgmt.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.NAVIGATE_PATH_URI_PARAM_COOKIE_NAME;
import static vn.lotusviet.hotelmgmt.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenProvider tokenProvider;
  private final ApplicationProperties applicationProperties;
  private final HttpCookieOAuth2AuthorizationRequestRepository requestRepository;

  public OAuth2AuthenticationSuccessHandler(
      TokenProvider tokenProvider,
      ApplicationProperties applicationProperties,
      HttpCookieOAuth2AuthorizationRequestRepository requestRepository) {
    this.tokenProvider = tokenProvider;
    this.applicationProperties = applicationProperties;
    this.requestRepository = requestRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  @Override
  protected String determineTargetUrl(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<String> redirectUri =
        CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new BadRequestException(
          "We've got an Unauthorized Redirect URI and can't proceed with the authentication");
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    String navigatePath =
        CookieUtil.getCookie(request, NAVIGATE_PATH_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse("/");

    String token = tokenProvider.createToken(authentication);

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("token", token)
        .queryParam("navigate_path", navigatePath)
        .build()
        .toUriString();
  }

  private void clearAuthenticationAttributes(
      HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    requestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return applicationProperties
        .getSecurity()
        .getAuthentication()
        .getoAuth2()
        .getAuthorizedRedirectUris()
        .stream()
        .anyMatch(
            authorizedRedirectUri -> {
              // Only validate host and port. Let the clients use different paths if they want to
              URI authorizedURI = URI.create(authorizedRedirectUri);
              return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                  && authorizedURI.getPort() == clientRedirectUri.getPort();
            });
  }
}