package com.movie.manage.entity;

import lombok.Data;

import java.util.List;

@Data
public class ChartOption {
  private Axis xAxis;
  private Axis yAxis;
  private List<Series> series;

  @Data
  public static class Axis {
    String[] data;
  }

  @Data
  public static class Series {
    double[] data;
    String type = "bar";
    String stack = "x";
  }
}
