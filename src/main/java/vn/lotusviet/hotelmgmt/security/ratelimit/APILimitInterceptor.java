package vn.lotusviet.hotelmgmt.security.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

public class APILimitInterceptor implements HandlerInterceptor {

  private static final String FORWARD_HEADER = "X-Forward-For";
  private static final String RATE_LIMIT_REMAIN_HEADER = "API-Rate-Limit-Remaining";
  private static final String RATE_LIMIT_RETRY_HEADER = "API-Rate-Limit-Retry-After-Seconds";

  private final APIBucketManager bucketManager;
  private final int limit;
  private final Duration duration;
  private String addtionalKeyPrefix = "";

  public APILimitInterceptor(APIBucketManager bucketManager, int limit, Duration duration) {
    if (limit < 0) {
      throw new IllegalArgumentException("Limit must be greater than 0");
    }
    if (duration.isNegative() || duration.isZero()) {
      throw new IllegalArgumentException("Duration must be greater than 0");
    }

    this.bucketManager = bucketManager;
    this.limit = limit;
    this.duration = duration;
  }

  public APILimitInterceptor(
      APIBucketManager bucketManager, int limit, Duration duration, String addtionalKeyPrefix) {
    this(bucketManager, limit, duration);
    this.addtionalKeyPrefix = addtionalKeyPrefix;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String bucketKey = getBucketKeyFromClientIPAddress(request);
    Bucket bucket = bucketManager.resolveBucket(bucketKey, limit, duration);
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

    if (probe.isConsumed()) {
      response.addHeader(RATE_LIMIT_REMAIN_HEADER, String.valueOf(probe.getRemainingTokens()));
      return true;
    } else {
      long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
      response.addHeader(RATE_LIMIT_RETRY_HEADER, String.valueOf(waitForRefill));
      response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have sent too many requests");
      return false;
    }
  }

  @NotNull
  private String getBucketKeyFromClientIPAddress(HttpServletRequest request) {
    String bucketKey = addtionalKeyPrefix;

    String clientIPAddress = request.getHeader(FORWARD_HEADER);
    if (clientIPAddress == null) {
      clientIPAddress = request.getRemoteAddr();
    }

    bucketKey += clientIPAddress;
    return bucketKey;
  }
}