package vn.lotusviet.hotelmgmt.repository.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class SpringMongoTestingApplication {
  public static void main(String... args) {
    SpringApplication.run(SpringMongoTestingApplication.class, args);
  }
}