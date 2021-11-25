package com.movie.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.manage.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
