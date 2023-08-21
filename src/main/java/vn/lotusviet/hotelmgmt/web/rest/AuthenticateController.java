package vn.lotusviet.hotelmgmt.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.model.dto.person.AccountDto;
import vn.lotusviet.hotelmgmt.model.dto.person.LoginDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.security.jwt.TokenProvider;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.mapper.AccountMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthenticateController {

  private final AccountService accountService;
  private final AccountMapper accountMapper;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;
  private final StorageService storageService;

  public AuthenticateController(
      AccountService accountService,
      AccountMapper accountMapper,
      AuthenticationManagerBuilder authenticationManagerBuilder,
      TokenProvider tokenProvider,
      StorageService storageService) {
    this.accountService = accountService;
    this.accountMapper = accountMapper;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.tokenProvider = tokenProvider;
    this.storageService = storageService;
  }

  @GetMapping
  public AccountDto getCurrentLoginAccountInfo() {
    Account account = accountService.getLoginAccount();
    return accountMapper
        .toDto(account)
        .setImageURL(
            account.getImageURL() != null
                ? storageService.getFileURL(account.getImageURL())
                : null);
  }

  @PostMapping("/login")
  public ResponseEntity<JWTToken> login(final @Valid @RequestBody LoginDto loginDto) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

    Authentication authentication =
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication, loginDto.isRememberMe());

    return new ResponseEntity<>(new JWTToken(jwt), HttpStatus.OK);
  }

  /** Object to return as body in JWT Authentication. */
  static class JWTToken {

    private String idToken;

    JWTToken(String idToken) {
      this.idToken = idToken;
    }

    @JsonProperty("id_token")
    String getIdToken() {
      return idToken;
    }

    void setIdToken(String idToken) {
      this.idToken = idToken;
    }
  }
}