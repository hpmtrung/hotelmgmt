package vn.lotusviet.hotelmgmt.model.dto.system;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class BusinessRuleUpdateDto {

  @NotNull
  @Size(min = 1)
  List<RuleUpdate> ruleUpdates;

  public List<RuleUpdate> getRuleUpdates() {
    return ruleUpdates;
  }

  public BusinessRuleUpdateDto setRuleUpdates(List<RuleUpdate> ruleUpdates) {
    this.ruleUpdates = ruleUpdates;
    return this;
  }

  @Override
  public String toString() {
    return "BusinessRuleUpdateDto{" + "ruleUpdates=" + ruleUpdates + '}';
  }

  public static class RuleUpdate {
    private int id;
    private String value;

    public int getId() {
      return id;
    }

    public RuleUpdate setId(int id) {
      this.id = id;
      return this;
    }

    public String getValue() {
      return value;
    }

    public RuleUpdate setValue(String value) {
      this.value = value;
      return this;
    }

    @Override
    public String toString() {
      return "RuleUpdate{" + "name='" + id + '\'' + ", value='" + value + '\'' + '}';
    }
  }
}