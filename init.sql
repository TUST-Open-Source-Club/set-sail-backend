create database setsail;
create user 'setsail'@'localhost' identified by "password";
grant all on setsail.* to 'setsail'@'localhost';
flush privileges;
use setsail;
create table chapter
(
    id          varchar(63)            not null
        primary key,
    name        varchar(63) default '' not null,
    description text        default '' not null,
    cost_time   float       default 0  not null,
    is_deleted  tinyint(1)  default 0  not null,
    course_ids  text        default '' not null
);

create table course
(
    id          varchar(63)            not null
        primary key,
    name        varchar(63) default '' not null,
    description text        default '' not null,
    cost_time   float       default 0  not null,
    homework    text        default '' not null,
    is_deleted  tinyint(1)  default 0  not null
);

create table learning_path
(
    id               varchar(63)            not null
        primary key,
    name             varchar(63) default '' not null,
    description      text        default '' not null,
    cost_time        float       default 0  not null,
    achievement      text        default '' not null,
    image_link       text        default '' not null,
    how_many_learned text        default '' not null,
    tutorial_ids     text        default '' not null
);

create table tutorial
(
    id               varchar(63)            not null
        primary key,
    name             varchar(63) default '' not null,
    description      text        default '' not null,
    cost_time        float       default 0  not null,
    achievement      text        default '' not null,
    image_link       text        default '' not null,
    how_many_learned text        default '' not null,
    chapter_ids      text        default '' not null
);

create table user
(
    id                varchar(63)              not null
        primary key,
    username          varchar(63)   default '' not null,
    password          varchar(2047) default '' not null,
    email             varchar(63)   default '' not null,
    student_id        varchar(15)   default '' not null,
    learning_path_ids text          default '' not null,
    tutorial_ids      text          default '' not null,
    privilege_list    text          default '' not null,
    is_deleted        tinyint(1)    default 0  not null
);



