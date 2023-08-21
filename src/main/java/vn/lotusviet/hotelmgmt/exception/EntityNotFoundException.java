package vn.lotusviet.hotelmgmt.exception;

public class EntityNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String entityName;

  private final String id;

  public EntityNotFoundException(String entityName, String id) {
    this.entityName = entityName;
    this.id = id;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getId() {
    return id;
  }
}