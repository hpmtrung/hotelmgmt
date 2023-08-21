package vn.lotusviet.hotelmgmt.model.dto.service;

public class ServicePriceUpdateDto {

  private Integer price;
  private Integer vat;

  public Integer getPrice() {
    return price;
  }

  public ServicePriceUpdateDto setPrice(final Integer price) {
    this.price = price;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public ServicePriceUpdateDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return "ServicePriceUpdateDto{" + "price=" + price + ", vat=" + vat + '}';
  }
}