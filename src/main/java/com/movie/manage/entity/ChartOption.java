package com.movie.manage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartOption {
  @JsonProperty("xAxis")
  private Axis xAxis;

  @JsonProperty("yAxis")
  private Axis yAxis;
  private List<Series> series;
  private Map<String, String> legend = new HashMap<String , String>() {{
    put("show", "true");
  }};

  @Data
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Axis {
    List<String> data;
    String type = "category";
    Map<String, String> axisLabel;
  }

  @Data
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Series {
    List<Double> data;
    String type = "bar";

    @JsonProperty("barGap")
    String barGap = "0";

    String name;

//    Map<String, String> label = new HashMap<String, String>(){{
//      put("show", "true");
//      put("rotate", "90");
//      put("formatter", "{a}");
//    }};

    String barMinWidth = "20";
  }
}
