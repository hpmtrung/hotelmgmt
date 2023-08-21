package vn.lotusviet.hotelmgmt.model.dto.room;

import vn.lotusviet.hotelmgmt.core.annotation.validation.percent.PercentConstaint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SuiteCreateDto {

  @NotNull private Integer suiteTypeId;

  @NotNull private Integer suiteStyleId;

  @NotNull @Positive private Integer price;

  @NotNull @PercentConstaint private Integer vat;

  @NotNull @Positive private Integer area;

  public Integer getArea() {
    return area;
  }

  public SuiteCreateDto setArea(Integer area) {
    this.area = area;
    return this;
  }

  public Integer getSuiteTypeId() {
    return suiteTypeId;
  }

  public SuiteCreateDto setSuiteTypeId(Integer suiteTypeId) {
    this.suiteTypeId = suiteTypeId;
    return this;
  }

  public Integer getSuiteStyleId() {
    return suiteStyleId;
  }

  public SuiteCreateDto setSuiteStyleId(Integer suiteStyleId) {
    this.suiteStyleId = suiteStyleId;
    return this;
  }

  public Integer getPrice() {
    return price;
  }

  public SuiteCreateDto setPrice(Integer price) {
    this.price = price;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public SuiteCreateDto setVat(Integer vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteCreateDto{"
        + "suiteTypeId="
        + suiteTypeId
        + ", suiteStyleId="
        + suiteStyleId
        + ", price="
        + price
        + ", vat="
        + vat
        + ", area="
        + area
        + '}';
  }
}