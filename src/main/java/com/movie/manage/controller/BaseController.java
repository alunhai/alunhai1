package com.movie.manage.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.manage.entity.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class BaseController<T extends IService<M>, M> {
  protected final IService<M> service;

  public BaseController(IService<M> service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<M> get(@PathVariable("id") Long id) {
    return ResponseEntity.success(service.getById(id));
  }
}
