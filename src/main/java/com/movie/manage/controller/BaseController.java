package com.movie.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.manage.entity.Page;
import com.movie.manage.entity.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class BaseController<M> {
  protected final IService<M> service;

  public BaseController(IService<M> service) {
    this.service = service;
  }

  public ResponseEntity<M> get(Long id) {
    return ResponseEntity.success(service.getById(id));
  }

  public ResponseEntity<M> create(M m) {
    service.save(m);
    return ResponseEntity.success(m);
  }

  public ResponseEntity<M> update(M m) {
    service.updateById(m);
    return ResponseEntity.success(m);
  }

  public ResponseEntity<Object> delete(Long id) {
    service.removeById(id);
    return ResponseEntity.success(null);
  }

  public ResponseEntity<Page<M>> list(int pageNo, int pageSize) {
    int count = service.count();
    LambdaQueryWrapper<M> queryWrapper = new QueryWrapper<M>()
        .lambda()
        .last(String.format("limit %d,%d", (pageNo-1)*pageSize, pageSize));
    List<M> items = service.list(queryWrapper);
    return ResponseEntity.success(new Page<>(items, count, pageNo, pageSize));
  }
}
