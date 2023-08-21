package vn.lotusviet.hotelmgmt.security.oauth.user;

import vn.lotusviet.hotelmgmt.core.exception.auth.OAuth2AthenticationProcessingException;
import vn.lotusviet.hotelmgmt.security.oauth.OAuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

  private OAuth2UserInfoFactory() {}

  public static OAuth2UserInfo getOAuth2UserInfo(
      String registrationId, Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(OAuthProvider.facebook.name())) {
      return new FacebookOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(OAuthProvider.google.name())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AthenticationProcessingException(
          String.format("Login with %s is not supported yet", registrationId));
    }
  }
}