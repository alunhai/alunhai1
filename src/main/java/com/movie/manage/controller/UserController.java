package com.movie.manage.controller;

import com.movie.manage.entity.Page;
import com.movie.manage.entity.ResponseEntity;
import com.movie.manage.entity.User;
import com.movie.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends BaseController<User>{

  @Autowired
  public UserController(UserService service) {
    super(service);
  }

  @Override
  @GetMapping("/users/{id}")
  public ResponseEntity<User> get(@PathVariable("id") Long id) {
    return super.get(id);
  }

  @Override
  @PostMapping("/users")
  public ResponseEntity<User> create(@RequestBody User user) {
    return super.create(user);
  }

  @Override
  @PutMapping("/users")
  public ResponseEntity<User> update(@RequestBody User user) {
    return super.update(user);
  }

  @Override
  @GetMapping("/users/list")
  public ResponseEntity<Page<User>> list(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
    return super.list(pageNo, pageSize);
  }
}
