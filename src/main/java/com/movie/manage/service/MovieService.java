package com.movie.manage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.manage.dao.ManageUserMapper;
import com.movie.manage.dao.MovieMapper;
import com.movie.manage.entity.ManageUser;
import com.movie.manage.entity.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieService extends ServiceImpl<MovieMapper, Movie> {

}
