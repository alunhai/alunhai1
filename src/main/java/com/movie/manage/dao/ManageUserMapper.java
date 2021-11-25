package com.movie.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.manage.entity.ManageUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManageUserMapper extends BaseMapper<ManageUser> {
}
