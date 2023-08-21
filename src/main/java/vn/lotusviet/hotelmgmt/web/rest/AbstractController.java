package vn.lotusviet.hotelmgmt.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public abstract class AbstractController {

  private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

  protected final ResponseEntity<byte[]> createFileByteResponse(byte[] content, String filename) {
    Objects.requireNonNull(content);
    Objects.requireNonNull(filename);
    log.debug("Create file byte response with filename {}", filename);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "range")
        .header(
            HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            "content-range, content-length, accept-ranges")
        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET")
        .body(content);
  }
}