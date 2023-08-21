package vn.lotusviet.hotelmgmt.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.cache.annotation.Cacheable;

import java.time.Duration;

public class APIBucketManager {

  public static final String RATE_LIMIT_CACHE_NAME = "RATE_LIMIT_CACHE";

  @Cacheable(cacheNames = RATE_LIMIT_CACHE_NAME, key = "#key", sync = true)
  public Bucket resolveBucket(String key, int limit, Duration duration) {
    return Bucket.builder()
        .addLimit(Bandwidth.classic(limit, Refill.intervally(limit, duration)))
        .build();
  }
}