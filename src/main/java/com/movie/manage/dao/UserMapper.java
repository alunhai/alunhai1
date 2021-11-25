package com.movie.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
