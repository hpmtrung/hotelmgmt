package vn.lotusviet.hotelmgmt.model.dto.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ServiceUsagePayDto {

  @NotNull
  @Size(min = 1)
  private List<Long> detailIds;

  public List<Long> getDetailIds() {
    return detailIds;
  }

  public void setDetailIds(List<Long> serviceUsageDetailIds) {
    this.detailIds = serviceUsageDetailIds;
  }

  @Override
  public String toString() {
    return "ServiceUsagePayDto{" + "serviceUsageDetailIds=" + detailIds + '}';
  }
}