package com.movie.manage.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.manage.dao.TagMapper;
import com.movie.manage.dao.UserMapper;
import com.movie.manage.entity.Tag;
import com.movie.manage.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> {

}
