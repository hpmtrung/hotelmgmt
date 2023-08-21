package vn.lotusviet.hotelmgmt.model.dto.room;

public class SuiteUpdateDto {

  private Integer price;

  private Integer vat;

  private Integer area;

  private Boolean available;

  public Boolean getAvailable() {
    return available;
  }

  public SuiteUpdateDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public Integer getPrice() {
    return price;
  }

  public SuiteUpdateDto setPrice(final Integer price) {
    this.price = price;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public SuiteUpdateDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  public Integer getArea() {
    return area;
  }

  public SuiteUpdateDto setArea(Integer area) {
    this.area = area;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteUpdateDto{"
        + "price="
        + price
        + ", vat="
        + vat
        + ", area="
        + area
        + ", available="
        + available
        + '}';
  }
}