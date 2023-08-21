package vn.lotusviet.hotelmgmt.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
public abstract class VersionedEntity implements Serializable {

  private static final long serialVersionUID = -4652050235970042223L;

  @JsonIgnore
  @Version
  @Column(name = "VERSION")
  protected int version;

  public int getVersion() {
    return version;
  }
}