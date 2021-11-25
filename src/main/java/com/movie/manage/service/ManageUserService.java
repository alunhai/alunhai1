package com.movie.manage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.manage.dao.ManageUserMapper;
import com.movie.manage.entity.ManageUser;
import org.springframework.stereotype.Service;

@Service
public class ManageUserService extends ServiceImpl<ManageUserMapper,ManageUser> {
}
