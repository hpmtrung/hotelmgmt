package vn.lotusviet.hotelmgmt.model.dto.rental;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class RentalPaymentCreateDto {
  @Positive @NotNull private Integer money;

  @NotNull private List<RentalPaymentContent> contents;

  public List<RentalPaymentContent> getContents() {
    return contents;
  }

  public RentalPaymentCreateDto setContents(List<RentalPaymentContent> contents) {
    this.contents = contents;
    return this;
  }

  public Integer getMoney() {
    return money;
  }

  public RentalPaymentCreateDto setMoney(Integer money) {
    this.money = money;
    return this;
  }

  @Override
  public String toString() {
    return "RentalPaymentCreateDto{" + "money=" + money + ", content=" + contents + '}';
  }
}