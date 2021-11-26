package com.movie.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("movie")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
  @TableId(type = IdType.AUTO)
  private Long id;

  private String title;

  private String genres;
}
