package com.movie.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.manage.entity.*;
import com.movie.manage.service.ManageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MangeUserController extends BaseController<ManageUser> {
  @Autowired
  public MangeUserController(ManageUserService service) {
    super(service);
  }

  @Override
  @GetMapping("/manage-users/{id}")
  public ResponseEntity<ManageUser> get(@PathVariable("id") Long id) {
    return super.get(id);
  }

  @Override
  @PostMapping("/manage-users")
  public ResponseEntity<ManageUser> create(@RequestBody ManageUser movie) {
    return super.create(movie);
  }

  @Override
  @PutMapping("/manage-users")
  public ResponseEntity<ManageUser> update(ManageUser user) {
    return super.update(user);
  }

  @Override
  @DeleteMapping("/manage-users/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    return super.delete(id);
  }

  @Override
  @GetMapping("/manage-users/list")
  public ResponseEntity<Page<ManageUser>> list(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
    return super.list(pageNo, pageSize);
  }

  @PostMapping("/manage-users/login")
  public ResponseEntity<ManageUser> login(@RequestBody ManageUser user) {
    ManageUserService service = (ManageUserService) this.service;
    ManageUser exists = service.getOne(new QueryWrapper<ManageUser>().lambda()
            .eq(ManageUser::getUsername, user.getUsername())
            .eq(ManageUser::getPassword, user.getPassword())
    );
    if(exists == null) {
      return ResponseEntity.fail(-1, "login fail");
    }
    return ResponseEntity.success(exists);
  }
}
