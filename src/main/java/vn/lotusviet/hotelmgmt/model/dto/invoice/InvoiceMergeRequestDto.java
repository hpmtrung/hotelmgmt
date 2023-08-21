package vn.lotusviet.hotelmgmt.model.dto.invoice;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class InvoiceMergeRequestDto {

  @NotNull
  @Size(min = 1)
  private List<Long> rentalIds;

  public List<Long> getRentalIds() {
    return rentalIds;
  }

  public void setRentalIds(List<Long> rentalIds) {
    this.rentalIds = rentalIds;
  }

  @Override
  public String toString() {
    return "InvoiceMergeRequestDto{" + "rentalIds=" + rentalIds + '}';
  }
}