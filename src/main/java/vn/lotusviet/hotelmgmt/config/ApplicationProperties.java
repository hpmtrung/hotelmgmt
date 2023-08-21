package vn.lotusviet.hotelmgmt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

  private final ProxyDatasource proxyDatasource = new ProxyDatasource();
  private final Async async = new Async();
  private final Security security = new Security();
  private final Http http = new Http();
  private final MailConfig mailConfig = new MailConfig();
  private final Cache cache = new Cache();
  private final PayingService payingService = new PayingService();
  private final Cloud cloud = new Cloud();
  private final ReportNameTemplate reportNameTemplate = new ReportNameTemplate();
  private String webClientBaseUrl;
  private String restErrorDocsBaseUrl;

  public String getRestErrorDocsBaseUrl() {
    return restErrorDocsBaseUrl;
  }

  public void setRestErrorDocsBaseUrl(String restErrorDocsBaseUrl) {
    this.restErrorDocsBaseUrl = restErrorDocsBaseUrl;
  }

  public ProxyDatasource getProxyDatasource() {
    return proxyDatasource;
  }

  public String getWebClientBaseUrl() {
    return webClientBaseUrl;
  }

  public void setWebClientBaseUrl(String webClientBaseUrl) {
    this.webClientBaseUrl = webClientBaseUrl;
  }

  public ReportNameTemplate getReportNameTemplate() {
    return reportNameTemplate;
  }

  public PayingService getPayingService() {
    return payingService;
  }

  public Cloud getCloud() {
    return cloud;
  }

  public Async getAsync() {
    return async;
  }

  public Security getSecurity() {
    return security;
  }

  public Http getHttp() {
    return http;
  }

  public Cache getCache() {
    return cache;
  }

  public MailConfig getMail() {
    return mailConfig;
  }

  public static class Async {

    private int corePoolSize = 2;

    private int maxPoolSize = 50;

    private int queueCapacity = 10000;

    public int getCorePoolSize() {
      return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
      this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
      return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
      return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
      this.queueCapacity = queueCapacity;
    }
  }

  public static class Security {

    private final Authentication authentication = new Authentication();
    private String contentSecurityPolicy = null;
    private String permissionSecurityPolicy = null;

    public String getPermissionSecurityPolicy() {
      return permissionSecurityPolicy;
    }

    public void setPermissionSecurityPolicy(String permissionSecurityPolicy) {
      this.permissionSecurityPolicy = permissionSecurityPolicy;
    }

    public Authentication getAuthentication() {
      return authentication;
    }

    public String getContentSecurityPolicy() {
      return contentSecurityPolicy;
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
      this.contentSecurityPolicy = contentSecurityPolicy;
    }

    public static class Authentication {

      private final JwtConfig jwtConfig = new JwtConfig();
      private final OAuth2 oAuth2 = new OAuth2();

      public OAuth2 getoAuth2() {
        return oAuth2;
      }

      public JwtConfig getJwt() {
        return jwtConfig;
      }

      public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
          return authorizedRedirectUris;
        }

        public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
          this.authorizedRedirectUris = authorizedRedirectUris;
        }
      }

      public static class JwtConfig {

        private String secret;

        private String base64Secret;

        private long tokenValidityInSeconds;

        private long tokenValidityInSecondsForRememberMe;

        public String getSecret() {
          return secret;
        }

        public void setSecret(String secret) {
          this.secret = secret;
        }

        public String getBase64Secret() {
          return base64Secret;
        }

        public void setBase64Secret(String base64Secret) {
          this.base64Secret = base64Secret;
        }

        public long getTokenValidityInSeconds() {
          return tokenValidityInSeconds;
        }

        public void setTokenValidityInSeconds(int tokenValidityInSeconds) {
          this.tokenValidityInSeconds = tokenValidityInSeconds;
        }

        public long getTokenValidityInSecondsForRememberMe() {
          return tokenValidityInSecondsForRememberMe;
        }

        public void setTokenValidityInSecondsForRememberMe(
            int tokenValidityInSecondsForRememberMe) {
          this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
        }
      }
    }
  }

  public static class Http {

    private final Cache cache = new Cache();

    public Cache getCache() {
      return cache;
    }

    public static class Cache {

      private int timeToLiveInDays = 1461; // 7days

      public int getTimeToLiveInDays() {
        return timeToLiveInDays;
      }

      public void setTimeToLiveInDays(int timeToLiveInDays) {
        this.timeToLiveInDays = timeToLiveInDays;
      }
    }
  }

  public static class Cache {

    private Ehcache ehcache = null;

    public Ehcache getEhcache() {
      return ehcache;
    }

    public void setEhcache(Ehcache ehcache) {
      this.ehcache = ehcache;
    }

    public static class Ehcache {

      private int timeToLiveSeconds = 30;

      private long maxEntries = 100L;

      public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
      }

      public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
      }

      public long getMaxEntries() {
        return maxEntries;
      }

      public void setMaxEntries(long maxEntries) {
        this.maxEntries = maxEntries;
      }
    }
  }

  public static class MailConfig {

    private boolean enabled = false;

    private String from = "";

    private String baseUrl = "";

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }
  }

  public static class Cloud {
    private Aws aws;

    public Aws getAws() {
      return aws;
    }

    public void setAws(Aws aws) {
      this.aws = aws;
    }

    public static class Aws {
      private Credentials credentials = new Credentials();
      private S3ClientConfig s3;

      public S3ClientConfig getS3() {
        return s3;
      }

      public void setS3(S3ClientConfig s3ClientConfig) {
        this.s3 = s3ClientConfig;
      }

      public Credentials getCredentials() {
        return credentials;
      }

      public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
      }

      public static class S3ClientConfig {
        private String bucketName;
        private String region;
        private String baseUrl;

        public String getBaseUrl() {
          return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
          this.baseUrl = baseUrl;
        }

        public String getRegion() {
          return region;
        }

        public void setRegion(String region) {
          this.region = region;
        }

        public String getBucketName() {
          return bucketName;
        }

        public void setBucketName(String bucketName) {
          this.bucketName = bucketName;
        }
      }

      public static class Credentials {

        private String accessKey;
        private String secretKey;

        public String getAccessKey() {
          return accessKey;
        }

        public void setAccessKey(String accessKey) {
          this.accessKey = accessKey;
        }

        public String getSecretKey() {
          return secretKey;
        }

        public void setSecretKey(String secretKey) {
          this.secretKey = secretKey;
        }
      }
    }
  }

  public static class PayingService {
    private PaypalConfig paypalConfig;

    public PaypalConfig getPaypal() {
      return paypalConfig;
    }

    public void setPaypal(PaypalConfig paypalConfig) {
      this.paypalConfig = paypalConfig;
    }

    public static class PaypalConfig {
      private String clientId;
      private String clientSecret;
      private String cancelUrl;
      private String returnUrl;

      public String getCancelUrl() {
        return cancelUrl;
      }

      public PaypalConfig setCancelUrl(final String cancelUrl) {
        this.cancelUrl = cancelUrl;
        return this;
      }

      public String getReturnUrl() {
        return returnUrl;
      }

      public PaypalConfig setReturnUrl(final String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
      }

      public String getClientId() {
        return clientId;
      }

      public void setClientId(String clientId) {
        this.clientId = clientId;
      }

      public String getClientSecret() {
        return clientSecret;
      }

      public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
      }
    }
  }

  public static class ReportNameTemplate {
    private String invoiceReport;
    private String roomOccupancyReport;
    private String reservationMonthStatsReport;
    private String suiteTurnOverStatsReport;

    public String getSuiteTurnOverStatsReport() {
      return suiteTurnOverStatsReport;
    }

    public ReportNameTemplate setSuiteTurnOverStatsReport(String suiteTurnOverStatsReport) {
      this.suiteTurnOverStatsReport = suiteTurnOverStatsReport;
      return this;
    }

    public String getReservationMonthStatsReport() {
      return reservationMonthStatsReport;
    }

    public ReportNameTemplate setReservationMonthStatsReport(String reservationMonthStatsReport) {
      this.reservationMonthStatsReport = reservationMonthStatsReport;
      return this;
    }

    public String getInvoiceReport() {
      return invoiceReport;
    }

    public void setInvoiceReport(String invoiceReport) {
      this.invoiceReport = invoiceReport;
    }

    public String getRoomOccupancyReport() {
      return roomOccupancyReport;
    }

    public void setRoomOccupancyReport(String roomOccupancyReport) {
      this.roomOccupancyReport = roomOccupancyReport;
    }
  }

  public static class ProxyDatasource {
    private String name;
    private String logLevel;
    private long slowQueryThresholdMiliseconds;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getLogLevel() {
      return logLevel;
    }

    public void setLogLevel(String logLevel) {
      this.logLevel = logLevel;
    }

    public long getSlowQueryThresholdMiliseconds() {
      return slowQueryThresholdMiliseconds;
    }

    public void setSlowQueryThresholdMiliseconds(long slowQueryThresholdMiliseconds) {
      this.slowQueryThresholdMiliseconds = slowQueryThresholdMiliseconds;
    }
  }
}