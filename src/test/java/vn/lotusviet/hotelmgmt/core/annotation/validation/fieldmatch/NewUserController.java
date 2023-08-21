package vn.lotusviet.hotelmgmt.core.annotation.validation.fieldmatch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class NewUserController {

  @PostMapping("/test/user")
  public ResponseEntity<String> submitForm(@Valid NewUserForm newUserForm) {
    return ResponseEntity.ok("data");
  }

}