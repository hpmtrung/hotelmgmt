package vn.lotusviet.hotelmgmt.service.mapper;

import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.dto.person.AccountDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest {

  private final AccountMapper mapper = new AccountMapperImpl();

  @Test
  void givenEntity_whenMapDto_thenCorrect() {
    Account account =
        new Account()
            .setId(1L)
            .setFirstName("fname")
            .setLastName("lname")
            .setEmail("mail@abc.com")
            .setLangKey("vi")
            .setActivated(true)
            .setAddress("address")
            .setPhone("1234567890")
            .setImageURL("image-url")
            .setAuthority(new Authority().setId(1).setName(AuthorityName.ROLE_CUSTOMER));

    AccountDto dto = mapper.toDto(account);

    assertEquals(account.getId(), dto.getId());
    assertEquals(account.getFirstName(), dto.getFirstName());
    assertEquals(account.getLastName(), dto.getLastName());
    assertEquals(account.getEmail(), dto.getEmail());
    assertEquals(account.getLangKey(), dto.getLangKey());
    assertEquals(account.isActivated(), dto.isActivated());
    assertEquals(account.getAddress(), dto.getAddress());
    assertEquals(account.getPhone(), dto.getPhone());
    assertEquals(account.getImageURL(), dto.getImageURL());

    assertEquals(account.getAuthority().getName(), dto.getAuthority());

    assertNull(account.getHashedPassword());
    assertNull(account.getActivationKey());
    assertNull(account.getResetAt());
  }

}