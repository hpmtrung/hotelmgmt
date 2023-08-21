package vn.lotusviet.hotelmgmt.model.dto.reservation;

public class ResourceAuthorizeResponseDto {

  private String approvalURL;

  public String getApprovalURL() {
    return approvalURL;
  }

  public void setApprovalURL(final String approvalURL) {
    this.approvalURL = approvalURL;
  }

  @Override
  public String toString() {
    return "ResourceAuthorizeResponseDto{" + "approvalURL='" + approvalURL + '\'' + '}';
  }
}