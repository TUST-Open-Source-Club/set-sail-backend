package com.tustosc.setsail.Entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
@TableName("learning_path")
public class LearningPath {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String description;

    private Float costTime;

    private String achievement;

    private String imageLink;

    private String howManyLearned;

    private List<Tutorial> tutorials ;

    @JsonIgnore
    private String tutorialIds;

}
