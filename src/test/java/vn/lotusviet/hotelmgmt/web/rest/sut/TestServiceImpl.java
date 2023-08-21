package vn.lotusviet.hotelmgmt.web.rest.sut;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class TestServiceImpl implements TestService {

  @Override
  public Object throwsInternalException() {
    return null;
  }

}