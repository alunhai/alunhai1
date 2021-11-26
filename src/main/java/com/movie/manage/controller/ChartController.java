package com.movie.manage.controller;


import com.movie.manage.entity.ChartOption;
import com.movie.manage.entity.ResponseEntity;
import com.movie.manage.entity.Triplet;
import com.movie.manage.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
  public ResponseEntity<ChartOption> getData(@RequestParam("type") String type) throws Exception {
    List<Triplet> triplets = null;
    try {
      triplets = chartService.readQueryResult(type);
    } catch (Exception e) {
      chartService.resetStat();
      throw e;
    }
    if(triplets == null) {
      return ResponseEntity.success(null);
    }
    ChartOption option = new ChartOption();
    ChartOption.Axis xAxis = new ChartOption.Axis();
    xAxis.setData(triplets.stream().map(Triplet::getCategory).toArray(String[]::new));
    option.setXAxis(xAxis);
    option.setYAxis(new ChartOption.Axis());
    List<ChartOption.Series> seriesList = new ArrayList<>();
    Map<String, List<Triplet>> groupedData = triplets.stream().collect(Collectors.groupingBy(Triplet::getType));
    groupedData.forEach((t, l)->{
      ChartOption.Series series = new ChartOption.Series();
      series.setData(l.stream().mapToDouble(Triplet::getAvgRate).toArray());
      seriesList.add(series);
    });
    option.setSeries(seriesList);
    return ResponseEntity.success(option);
  }
}
