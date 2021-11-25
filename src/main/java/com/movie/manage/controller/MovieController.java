package com.movie.manage.controller;


import com.movie.manage.entity.Movie;
import com.movie.manage.entity.ResponseEntity;
import com.movie.manage.service.MovieService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/movies")
public class MovieController extends BaseController<MovieService, Movie>{

  public MovieController(MovieService movieService) {
    super(movieService);
  }
}