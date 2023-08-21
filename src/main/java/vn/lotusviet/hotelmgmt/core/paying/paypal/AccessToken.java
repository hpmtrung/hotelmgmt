package vn.lotusviet.hotelmgmt.core.paying.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AccessToken implements Authorization {
  private final Date createDate = new Date();

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType = "Bearer";

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("app_id")
  private String appId;

  @JsonProperty("nonce")
  private String nonce;

  public String getScope() {
    return scope;
  }

  public AccessToken setScope(String scope) {
    this.scope = scope;
    return this;
  }

  public String getAppId() {
    return appId;
  }

  public AccessToken setAppId(String appId) {
    this.appId = appId;
    return this;
  }

  public String getNonce() {
    return nonce;
  }

  public AccessToken setNonce(String nonce) {
    this.nonce = nonce;
    return this;
  }

  public boolean isExpired() {
    Date expireDate = new Date(createDate.getTime() + (expiresIn * 1000));
    return new Date().after(expireDate);
  }

  public Date getCreateDate() {
    return createDate;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public AccessToken setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public String getTokenType() {
    return tokenType;
  }

  public AccessToken setTokenType(final String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public AccessToken setExpiresIn(final Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  @Override
  public String authorizationString() {
    return String.format("Bearer %s", accessToken);
  }

  @Override
  public String toString() {
    return "AccessToken{"
        + "createDate="
        + createDate
        + ", accessToken='"
        + accessToken
        + '\''
        + ", tokenType='"
        + tokenType
        + '\''
        + ", expiresIn="
        + expiresIn
        + '}';
  }
}