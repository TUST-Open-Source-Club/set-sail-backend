package com.tustosc.setsail.Entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
@TableName("chapter")
public class Chapter {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String description;

    private Float costTime;

    private List<Course> courses;

    @JsonIgnore
    private String courseIds;
    @JsonIgnore
    private Boolean isDeleted;
}
