package vn.lotusviet.hotelmgmt.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.util.List;

public final class PaginationUtil {

  private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

  private PaginationUtil() {}

  public static <T> ResponseEntity<List<T>> createPaginationResponse(Page<T> page) {
    return new ResponseEntity<>(
        page.getContent(),
        generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page),
        HttpStatus.OK);
  }

  private static String prepareLink(
      UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
    return MessageFormat.format(
        HEADER_LINK_FORMAT, preparePageUri(uriBuilder, pageNumber, pageSize), relType);
  }

  private static String preparePageUri(
      UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
    return uriBuilder
        .replaceQueryParam("page", Integer.toString(pageNumber))
        .replaceQueryParam("size", Integer.toString(pageSize))
        .toUriString()
        .replace(",", "%2C")
        .replace(";", "%3B");
  }

  private static <T> HttpHeaders generatePaginationHttpHeaders(
      UriComponentsBuilder uriBuilder, Page<T> page) {
    HttpHeaders headers = new HttpHeaders();

    headers.add("x-entity-count", Long.toString(page.getTotalElements()));

    int pageNumber = page.getNumber();
    int pageSize = page.getSize();

    StringBuilder link = new StringBuilder();

    if (pageNumber < page.getTotalPages() - 1) {
      link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
    }

    if (pageNumber > 0) {
      link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
    }

    link.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last"))
        .append(",")
        .append(prepareLink(uriBuilder, 0, pageSize, "first"));

    headers.add("Link", link.toString());

    return headers;
  }
}