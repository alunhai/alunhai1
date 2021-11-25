package com.movie.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.manage.entity.Movie;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
}
