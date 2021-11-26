package com.movie.manage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.manage.dao.RatingMapper;
import com.movie.manage.dao.UserMapper;
import com.movie.manage.entity.Rating;
import com.movie.manage.entity.User;
import org.springframework.stereotype.Service;

@Service
public class RatingService extends ServiceImpl<RatingMapper, Rating> {

}
