package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

public class AuthorityNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -1927143726719868519L;

  public AuthorityNotFoundException(AuthorityName code) {
    super("authority", code.name());
  }
}