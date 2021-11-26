package com.movie.manage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.manage.dao.MovieMapper;
import com.movie.manage.dao.UserMapper;
import com.movie.manage.entity.Movie;
import com.movie.manage.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
