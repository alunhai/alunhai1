package com.movie.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.manage.entity.Rating;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RatingMapper extends BaseMapper<Rating> {
}
