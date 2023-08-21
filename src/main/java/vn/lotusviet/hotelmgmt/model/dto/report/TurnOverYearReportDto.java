package vn.lotusviet.hotelmgmt.model.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TurnOverYearReportDto {

  private List<TurnOverMonth> turnOverMonths;

  @JsonIgnore
  public static TurnOverYearReportDto createEmpty() {
    List<TurnOverMonth> months = new ArrayList<>();
    IntStream.range(1, 13)
        .mapToObj((month) -> new TurnOverMonth().setMonth(month).setTurnOver(0L))
        .forEach(months::add);
    return new TurnOverYearReportDto().setTurnOverMonths(months);
  }

  public List<TurnOverMonth> getTurnOverMonths() {
    return turnOverMonths;
  }

  public TurnOverYearReportDto setTurnOverMonths(List<TurnOverMonth> turnOverMonths) {
    this.turnOverMonths = turnOverMonths;
    return this;
  }

  public List<TurnOverQuarter> getTurnOverQuarters() {
    final List<TurnOverQuarter> turnOverQuarters = new ArrayList<>();
    for (int quarter = 1; quarter <= 4; quarter++) {
      long turnOver = 0;
      for (int month = 3 * (quarter - 1) + 1; month <= 3 * quarter; month++) {
        turnOver += turnOverMonths.get(month - 1).getTurnOver();
      }
      turnOverQuarters.add(new TurnOverQuarter().setQuarter(quarter).setTurnOver(turnOver));
    }
    return turnOverQuarters;
  }

  @Override
  public String toString() {
    return "TurnOverYearReportDto{" + "turnOverMonths=" + turnOverMonths + '}';
  }

  public static class TurnOverQuarter implements Serializable {

    private static final long serialVersionUID = 3331217084059997080L;

    private int quarter;
    private long turnOver;

    public int getQuarter() {
      return quarter;
    }

    public TurnOverQuarter setQuarter(final int quarter) {
      this.quarter = quarter;
      return this;
    }

    public long getTurnOver() {
      return turnOver;
    }

    public TurnOverQuarter setTurnOver(final long turnOver) {
      this.turnOver = turnOver;
      return this;
    }

    @Override
    public String toString() {
      return "TurnOverQuarter{" + "quarter=" + quarter + ", turnOver=" + turnOver + '}';
    }
  }

  public static class TurnOverMonth implements Serializable {

    private static final long serialVersionUID = 1688071441884655558L;

    private int month;
    private long turnOver;

    public int getMonth() {
      return month;
    }

    public TurnOverMonth setMonth(int month) {
      this.month = month;
      return this;
    }

    public long getTurnOver() {
      return turnOver;
    }

    public TurnOverMonth setTurnOver(long turnOver) {
      this.turnOver = turnOver;
      return this;
    }

    @Override
    public String toString() {
      return "TurnOverMonth{" + "month=" + month + ", turnOver=" + turnOver + '}';
    }
  }
}