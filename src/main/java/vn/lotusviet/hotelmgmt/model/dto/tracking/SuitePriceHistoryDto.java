package vn.lotusviet.hotelmgmt.model.dto.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuitePriceHistoryDto {

  private SuiteDto suite;
  private LocalDate editedAt;
  private EmployeeDto editedBy;
  private Integer price;
  private Integer vat;

  public SuiteDto getSuite() {
    return suite;
  }

  public SuitePriceHistoryDto setSuite(final SuiteDto suite) {
    this.suite = suite;
    return this;
  }

  public LocalDate getEditedAt() {
    return editedAt;
  }

  public SuitePriceHistoryDto setEditedAt(final LocalDate editedAt) {
    this.editedAt = editedAt;
    return this;
  }

  public EmployeeDto getEditedBy() {
    return editedBy;
  }

  public SuitePriceHistoryDto setEditedBy(final EmployeeDto editedBy) {
    this.editedBy = editedBy;
    return this;
  }

  public Integer getPrice() {
    return price;
  }

  public SuitePriceHistoryDto setPrice(final Integer price) {
    this.price = price;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public SuitePriceHistoryDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return "SuitePriceHistoryDto{"
        + "suite="
        + suite
        + ", editedAt="
        + editedAt
        + ", editedBy="
        + editedBy
        + ", price="
        + price
        + ", vat="
        + vat
        + '}';
  }
}