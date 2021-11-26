package com.movie.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("rating")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
  @TableId(type = IdType.AUTO)
  private Long id;

  @TableField("userId")
  private Long userId;

  @TableField("movieId")
  private Long movieId;

  private String tag;

  private Long timestamp;
}
