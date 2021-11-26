package com.movie.manage.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.manage.dao.MovieMapper;
import com.movie.manage.entity.*;
import com.movie.manage.service.MovieService;
import com.movie.manage.service.RatingService;
import com.movie.manage.service.TagService;
import com.movie.manage.service.UserService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
public class InitController {
  private static final Logger logger = LoggerFactory.getLogger(InitController.class);
  final String path = "./ml-latest";

  private final MovieService movieMapper;
  private final UserService userMapper;
  private final RatingService ratingMapper;
  private final TagService tagMapper;

  @Autowired
  public InitController(MovieService movieMapper, UserService userMapper, RatingService ratingMapper, TagService tagMapper) {
    this.movieMapper = movieMapper;
    this.userMapper = userMapper;
    this.ratingMapper = ratingMapper;
    this.tagMapper = tagMapper;
  }


  @GetMapping("/init")
  public ResponseEntity<Object> init(@RequestParam("type") String type) throws Exception {
    File dir = new File(path);
    assert dir.exists();

    switch (type) {
      case "movie":
        logger.info("init movies");
        save(movieMapper, r -> Movie.builder()
            .id(Long.parseLong(r.get(0)))
            .title(r.get(1))
            .genres(r.get(2))
            .build(), "movies.csv");
        break;
      default:
        logger.info("init users");
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path, "users.dat")))) {
          br.lines().forEach(line -> {
            String[] sps = line.split("::");
            if (sps[0].equalsIgnoreCase("UserId")) {
              return;
            }
            userMapper.save(User.builder()
                .id(Long.parseLong(sps[0]))
                .gender(sps[1])
                .age(Integer.parseInt(sps[2]))
                .occupation(sps[3])
                .zipCode(sps[4])
                .build());
          });
        }
    }
    return ResponseEntity.success(null);
  }

  private <T> void save(IService<T> mapper, Function<CSVRecord, T> trans, String name) throws Exception {
    try (FileReader in = new FileReader(new File(path, name))) {
      Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().withSkipHeaderRecord().parse(in);
      List<T> list = new ArrayList<>();
      records.forEach(r -> {
        list.add(trans.apply(r));
      });
      mapper.saveBatch(list);
    }
  }
}
