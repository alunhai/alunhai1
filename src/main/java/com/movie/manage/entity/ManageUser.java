package com.movie.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("manage_user")
public class ManageUser {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String username;
  private String password;

  @TableField("isAdmin")
  @JsonProperty("isAdmin")
  private boolean isAdmin;
}
