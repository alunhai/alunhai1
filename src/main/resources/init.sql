drop table if exists `manage_user`;
drop table if exists `user`;
drop table if exists `movie`;
drop table if exists `rating`;
drop table if exists `tag`;

create table `user` (
    id bigint auto_increment primary key comment 'id',
    gender varchar(16) comment '性别',
    age int comment '年龄段标记',
    occupation varchar(128) comment '职业',
    zipCode varchar(32) comment 'zip code'
);

create table `manage_user` (
    id bigint auto_increment primary key comment 'id',
    username varchar(128) not null comment '用户名',
    password varchar(128) not null comment '密码',
    isAdmin bool default 0 comment '是否是管理员'
);

insert into `manage_user` (username, password, isAdmin) values ('admin', 'admin123', true);

create table `movie` (
    id bigint auto_increment primary key,
    title varchar(1024),
    genres varchar(1024)
);