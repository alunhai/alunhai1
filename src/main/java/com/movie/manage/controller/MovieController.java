package com.movie.manage.controller;


import com.movie.manage.entity.Movie;
import com.movie.manage.entity.Page;
import com.movie.manage.entity.ResponseEntity;
import com.movie.manage.service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController("/movies")
public class MovieController extends BaseController<Movie>{

  public MovieController(MovieService movieService) {
    super(movieService);
  }

  @Override
  @GetMapping("/movies/{id}")
  public ResponseEntity<Movie> get(@PathVariable("id") Long id) {
    return super.get(id);
  }

  @Override
  @PostMapping("/movies")
  public ResponseEntity<Movie> create(@RequestBody Movie movie) {
    return super.create(movie);
  }

  @Override
  @PutMapping("/movies")
  public ResponseEntity<Movie> update(Movie movie) {
    return super.update(movie);
  }

  @Override
  @DeleteMapping("/movies/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    return super.delete(id);
  }

  @Override
  @GetMapping("/movies/list")
  public ResponseEntity<Page<Movie>> list(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
    return super.list(pageNo, pageSize);
  }
}