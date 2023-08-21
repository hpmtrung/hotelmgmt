package vn.lotusviet.hotelmgmt.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.Security.Authentication.JwtConfig;
import vn.lotusviet.hotelmgmt.management.SecurityMetersService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

  public static final String AUTH_TOKEN_COOKIE_NAME = "AUTH_TOKEN";
  public static final String BEARER = "Bearer ";
  private static final String AUTHORITIES_KEY = "auth";

  private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

  private final Key key;

  private final JwtParser jwtParser;

  private final long tokenValidityInMilliseconds;

  private final long tokenValidityInMillisecondsForRememberMe;

  private final SecurityMetersService securityMetersService;

  public TokenProvider(
      ApplicationProperties applicationProperties, SecurityMetersService securityMetersService) {
    final JwtConfig jwtConfig = applicationProperties.getSecurity().getAuthentication().getJwt();

    key = getKey(jwtConfig);
    jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

    this.tokenValidityInMilliseconds = 1_000 * jwtConfig.getTokenValidityInSeconds();

    this.tokenValidityInMillisecondsForRememberMe =
        1_000 * jwtConfig.getTokenValidityInSecondsForRememberMe();

    this.securityMetersService = securityMetersService;
  }

  @NotNull
  private Key getKey(JwtConfig jwtConfig) {
    final Key resultKey;
    final byte[] keyBytes;

    String secret = jwtConfig.getBase64Secret();
    boolean isSecretEmpty = ObjectUtils.isEmpty(secret);

    if (isSecretEmpty) {
      log.error("Warning: the JWT key used is not Base64-encoded");
      secret = jwtConfig.getSecret();
      keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    } else {
      log.debug("Using a Base64-encoded JWT secret key");
      keyBytes = Decoders.BASE64.decode(secret);
    }

    resultKey = Keys.hmacShaKeyFor(keyBytes);
    return resultKey;
  }

  public String createToken(final Authentication authentication, boolean rememberMe) {
    String authorities = getAuthoritiesAsString(authentication);

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(getValidity(rememberMe))
        .compact();
  }

  @NotNull
  private String getAuthoritiesAsString(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
  }

  @NotNull
  private Date getValidity(boolean rememberMe) {
    final long now = (new Date()).getTime();
    final Date validity;
    if (rememberMe) {
      validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    } else {
      validity = new Date(now + this.tokenValidityInMilliseconds);
    }
    return validity;
  }

  public String createToken(final Authentication authentication) {
    return createToken(authentication, false);
  }

  public Authentication getAuthenticationFromJwt(final String token) {
    Claims claims = jwtParser.parseClaimsJws(token).getBody();

    String authoritiesClaim = claims.get(AUTHORITIES_KEY).toString();

    Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromClaim(authoritiesClaim);

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  @NotNull
  private Collection<? extends GrantedAuthority> getAuthoritiesFromClaim(String authoritiesClaim) {
    return Arrays.stream(authoritiesClaim.split(","))
        .filter(auth -> !auth.trim().isEmpty())
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  public boolean validateToken(String authToken) {
    try {
      jwtParser.parseClaimsJws(authToken);
      return true;
    } catch (ExpiredJwtException e) {
      this.securityMetersService.trackTokenExpired();

      log.trace("Expired JWT token", e);
    } catch (UnsupportedJwtException e) {
      this.securityMetersService.trackTokenUnsupported();

      log.trace("Unsupported JWT token", e);
    } catch (MalformedJwtException e) {
      this.securityMetersService.trackTokenMalformed();

      log.trace("Invalid JWT token", e);
    } catch (SignatureException e) {
      this.securityMetersService.trackTokenInvalidSignature();

      log.trace("Invalid JWT signature", e);
    } catch (IllegalArgumentException e) {
      log.error("Token validation error {}", e.getMessage());
    }

    return false;
  }
}