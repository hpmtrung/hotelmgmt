package vn.lotusviet.hotelmgmt.security.oauth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
  public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
  public static final String NAVIGATE_PATH_URI_PARAM_COOKIE_NAME = "navigate_path";
  private static final Logger log =
      LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository.class);
  private static final int cookieExpireSeconds = 180;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
        .orElse(null);
  }

  @Override
  public void saveAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (authorizationRequest == null) {
      removeAuthorizationRequestCookies(request, response);
      return;
    }

    CookieUtil.addCookie(
        response,
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
        CookieUtil.serialize(authorizationRequest),
        cookieExpireSeconds);

    saveRequestParamAsCookie(
        request, response, REDIRECT_URI_PARAM_COOKIE_NAME, NAVIGATE_PATH_URI_PARAM_COOKIE_NAME);
  }

  private void saveRequestParamAsCookie(
      HttpServletRequest request, HttpServletResponse response, String... params) {
    for (final String param : params) {
      String value = request.getParameter(param);
      if (StringUtils.isNotBlank(value)) {
        CookieUtil.addCookie(response, param, value, cookieExpireSeconds);
      }
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
    return this.loadAuthorizationRequest(request);
  }

  public void removeAuthorizationRequestCookies(
      HttpServletRequest request, HttpServletResponse response) {
    CookieUtil.deleteCookie(
        request,
        response,
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
        REDIRECT_URI_PARAM_COOKIE_NAME,
        NAVIGATE_PATH_URI_PARAM_COOKIE_NAME);
  }
}