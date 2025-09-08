package com.tustosc.setsail.Entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String username;

    private String password;

    private String email;

    private String studentId;

    private List<LearningPath> learningPaths;

    private List<Tutorial> tutorials;

    @JsonIgnore
    private String learningPathIds;

    @JsonIgnore
    private String tutorialIds;


}
