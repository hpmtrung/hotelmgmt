package vn.lotusviet.hotelmgmt.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;
import vn.lotusviet.hotelmgmt.IntegrationTest;
import vn.lotusviet.hotelmgmt.TestUtil;
import vn.lotusviet.hotelmgmt.model.dto.person.LoginDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class AuthenticationControllerIT {

  @Autowired private AccountRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private MockMvc mockMvc;

  @Test
  @Transactional
  void testAuthorize() throws Exception {
    Account user = new Account();
    user.setEmail("user-jwt-controller@example.com");
    user.setActivated(true);
    user.setHashedPassword(passwordEncoder.encode("test"));

    userRepository.saveAndFlush(user);

    LoginDto login = new LoginDto();
    login.setEmail("user-jwt-controller");
    login.setPassword("test");
    mockMvc
        .perform(
            post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(login)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id_token").isString())
        .andExpect(jsonPath("$.id_token").isNotEmpty())
        .andExpect(header().string("Authorization", not(nullValue())))
        .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  @Transactional
  void testAuthorizeWithRememberMe() throws Exception {
    Account user = new Account();
    user.setEmail("user-jwt-controller-remember-me");
    user.setEmail("user-jwt-controller-remember-me@example.com");
    user.setActivated(true);
    user.setHashedPassword(passwordEncoder.encode("test"));

    userRepository.saveAndFlush(user);

    LoginDto login = new LoginDto();
    login.setEmail("user-jwt-controller-remember-me");
    login.setPassword("test");
    login.setRememberMe(true);
    mockMvc
        .perform(
            post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(login)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id_token").isString())
        .andExpect(jsonPath("$.id_token").isNotEmpty())
        .andExpect(header().string("Authorization", not(nullValue())))
        .andExpect(header().string("Authorization", not(is(emptyString()))));
  }

  @Test
  void testAuthorizeFails() throws Exception {
    LoginDto login = new LoginDto();
    login.setEmail("wrong-user");
    login.setPassword("wrong password");
    mockMvc
        .perform(
            post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(login)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.id_token").doesNotExist())
        .andExpect(header().doesNotExist("Authorization"));
  }
}