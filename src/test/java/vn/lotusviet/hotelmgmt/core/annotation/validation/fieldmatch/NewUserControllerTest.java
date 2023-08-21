package vn.lotusviet.hotelmgmt.core.annotation.validation.fieldmatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NewUserControllerTest {
  private MockMvc mvc;

  @BeforeEach
  public void setUp() {
    this.mvc = MockMvcBuilders.standaloneSetup(new NewUserController()).build();
  }

  @Test
  void givenMatchingEmailPassword_whenPostNewUserForm_thenOk() throws Exception {
    this.mvc
        .perform(
            post("/test/user")
                .param("email", "bob@gmail.com")
                .param("verifyEmail", "bob@gmail.com")
                .param("password", "pass")
                .param("verifyPassword", "pass"))
        .andExpect(status().isOk());
  }
}