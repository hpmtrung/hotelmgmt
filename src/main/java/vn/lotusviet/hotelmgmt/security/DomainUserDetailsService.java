package vn.lotusviet.hotelmgmt.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.exception.auth.AccountUnActivatedException;
import vn.lotusviet.hotelmgmt.core.exception.auth.OAuth2AthenticationProcessingException;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.model.entity.system.BusinessRule;
import vn.lotusviet.hotelmgmt.repository.misc.BusinessRuleRepository;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;
import vn.lotusviet.hotelmgmt.repository.person.AuthorityRepository;
import vn.lotusviet.hotelmgmt.security.oauth.user.OAuth2UserInfo;
import vn.lotusviet.hotelmgmt.security.oauth.user.OAuth2UserInfoFactory;
import vn.lotusviet.hotelmgmt.service.mail.MailService;

import java.util.Collections;

/** Authenticate a user from the database. */
@Component("userDetailsService")
@Transactional
public class DomainUserDetailsService extends DefaultOAuth2UserService
    implements UserDetailsService {

  private static final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

  private final AuthorityRepository authorityRepository;
  private final AccountRepository accountRepository;
  private final MailService mailService;
  private final BusinessRuleRepository businessRuleRepository;

  public DomainUserDetailsService(
      AuthorityRepository authorityRepository,
      AccountRepository accountRepository,
      MailService mailService,
      BusinessRuleRepository businessRuleRepository) {
    this.authorityRepository = authorityRepository;
    this.accountRepository = accountRepository;
    this.mailService = mailService;
    this.businessRuleRepository = businessRuleRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User user = super.loadUser(userRequest);

    log.debug("Loaded user {}", user);

    try {
      return processOAuth2User(userRequest, user);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the
      // OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo =
        OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.getClientRegistration().getRegistrationId(),
            oAuth2User.getAttributes());
    log.debug("Processing user request {}", getRequestString(oAuth2UserRequest));

    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AthenticationProcessingException("Email not found from OAuth2 provider");
    }

    Account account = accountRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);

    if (account == null) {
      account = registerNewAccount(oAuth2UserInfo);
    }

    return UserPrincipal.create(account);
  }

  private String getRequestString(OAuth2UserRequest request) {
    return String.format(
        "Access token: %s, Additional params: %s, client registration: %s",
        request.getAccessToken(),
        request.getAdditionalParameters(),
        request.getClientRegistration());
  }

  private Account registerNewAccount(OAuth2UserInfo info) {
    Account newAccount =
        new Account()
            .setEmail(info.getEmail())
            .setFirstName(info.getFirstName())
            .setLastName(info.getLastName())
            .setActivated(true)
            .setLangKey(
                businessRuleRepository
                    .findById(BusinessRuleKey.CUSTOMER_ACCOUNT_REGISTRATION_DEFAULT_LANGKEY.getId())
                    .map(BusinessRule::getValue)
                    .orElseThrow());

    newAccount.setAuthority(
        authorityRepository.findByName(AuthorityName.ROLE_CUSTOMER).orElseThrow());

    newAccount = accountRepository.save(newAccount);

    // Send welcome email
    mailService.sendWelcomeMail(newAccount);

    return newAccount;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(final String email) {
    log.debug("Authenticating {}", email);

    return accountRepository
        .findByEmail(email)
        .map(account -> createSpringSecurityUser(email, account))
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    String.format("User with email '%s' was not found in the database", email)));
  }

  private User createSpringSecurityUser(String email, Account account) {
    if (!account.isActivated()) {
      throw new AccountUnActivatedException(
          String.format("User with email '%s' was not activated", email));
    }
    return new User(
        account.getEmail(),
        account.getHashedPassword(),
        Collections.singletonList(
            new SimpleGrantedAuthority(account.getAuthority().getName().name())));
  }
}