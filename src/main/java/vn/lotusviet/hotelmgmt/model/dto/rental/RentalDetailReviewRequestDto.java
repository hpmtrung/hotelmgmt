package vn.lotusviet.hotelmgmt.model.dto.rental;

import javax.validation.constraints.NotBlank;

public class RentalDetailReviewRequestDto {

  @NotBlank private String detailIds;

  public String getDetailIds() {
    return detailIds;
  }

  public void setDetailIds(String detailIds) {
    this.detailIds = detailIds;
  }

  @Override
  public String toString() {
    return "RentalDetailReviewRequestDto{" + "detailIds=" + detailIds + '}';
  }
}