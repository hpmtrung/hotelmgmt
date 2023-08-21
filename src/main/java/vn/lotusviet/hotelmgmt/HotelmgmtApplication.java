package vn.lotusviet.hotelmgmt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class HotelmgmtApplication {

  private static final Logger log = LoggerFactory.getLogger(HotelmgmtApplication.class);

  private final Environment env;

  public HotelmgmtApplication(Environment env) {
    this.env = env;
  }

  private static void logApplicationStartup(Environment env) {
    String protocol =
        Optional.ofNullable(env.getProperty("server.ssl.key-store"))
            .map(key -> "https")
            .orElse("http");
    String serverPort = env.getProperty("server.port");
    String contextPath =
        Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
    String hostAddress = "localhost";

    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }

    printApplicationStartupInfo(env, protocol, serverPort, contextPath, hostAddress);
  }

  private static void printApplicationStartupInfo(
      Environment env, String protocol, String serverPort, String contextPath, String hostAddress) {
    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Application '{}' is running! Access URLs:\n\t"
            + "Local: \t\t{}://localhost:{}{}\n\t"
            + "External: \t{}://{}:{}{}\n\t"
            + "Profile(s): \t{}\n"
            + "----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        serverPort,
        contextPath,
        protocol,
        hostAddress,
        serverPort,
        contextPath,
        env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(HotelmgmtApplication.class);
    Environment env = app.run(args).getEnvironment();
    logApplicationStartup(env);
  }

  @PostConstruct
  public void verifyActiveProfiles() {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    boolean isDevProfileActive = activeProfiles.contains("dev");
    boolean isProdProfileActive = activeProfiles.contains("prod");
    if (isDevProfileActive && isProdProfileActive) {
      log.error("It should not run with both the 'dev' and 'prod' profiles at the same time.");
    }
  }
}