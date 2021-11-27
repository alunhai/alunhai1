package com.movie.manage.controller;


import com.movie.manage.entity.ChartOption;
import com.movie.manage.entity.ResponseEntity;
import com.movie.manage.entity.Triplet;
import com.movie.manage.entity.User;
import com.movie.manage.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChartController {
  private final ChartService chartService;

  @Autowired
  public ChartController(ChartService chartService) {
    this.chartService = chartService;
  }

  @GetMapping("charts")
  public ResponseEntity<ChartOption> getData(@RequestParam("type") String type,
                                             @RequestParam(value = "force", defaultValue = "false") boolean force) throws Exception {
    List<Triplet> triplets = null;
    try {
      triplets = chartService.readQueryResult(type, force);
    } catch (Exception e) {
      chartService.resetStat();
      throw e;
    }
    if(triplets == null) {
      return ResponseEntity.success(null);
    }
    ChartOption option = new ChartOption();
    ChartOption.Axis xAxis = new ChartOption.Axis();
    ChartOption.Axis yAxis = new ChartOption.Axis();
    List<ChartOption.Series> seriesList = new ArrayList<>();

    option.setXAxis(xAxis);
    option.setYAxis(yAxis);
    option.setSeries(seriesList);

    Map<String, Map<String, Double>> datas = new HashMap<>();
    triplets.forEach(triplet -> {
      datas.compute(triplet.getCategory(), (c, map)-> {
        if(map == null) {
          map = new HashMap<>();
        }
        map.put(triplet.getType(), triplet.getAvgRate());
        return map;
      });
    });
    List<String> labels = triplets.stream().map(Triplet::getCategory).distinct().collect(Collectors.toList());
    Map<String, ChartOption.Series> rates = new HashMap<>();
    triplets.forEach(triplet -> {
      int i = labels.indexOf(triplet.getCategory());
      ChartOption.Series series = rates.get(triplet.getType());
      if(series == null) {
        series = new ChartOption.Series();
        series.setData(new ArrayList<>());
        for (int i1 = 0; i1 < labels.size(); i1++) {
          series.getData().add(0d);
        }
        switch (type) {
          case "gender":
            if(triplet.getType().equals("F")) {
              series.setName("Female");
            } else {
              series.setName("Male");
            }
            break;
          case "age":
            series.setName(User.ageDescs.get(Integer.parseInt(triplet.getType())));
            break;
          case "occupation":
            series.setName(User.occupationDescs.get(triplet.getType()));
            break;
          case "zipcode":
            series.setName(triplet.getType());
            break;
          default:
            throw new UnsupportedOperationException("unknow type");
        }
        rates.put(triplet.getType(), series);
      }
      series.getData().set(i, triplet.getAvgRate());
    });
    rates.forEach((k,v)->seriesList.add(v));
    yAxis.setData(labels);
    xAxis.setType("value");

    return ResponseEntity.success(option);
  }
}
