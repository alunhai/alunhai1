package com.movie.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String gender;
  private Integer age;
  private String occupation;

  @TableField("zipCode")
  private String zipCode;
}
