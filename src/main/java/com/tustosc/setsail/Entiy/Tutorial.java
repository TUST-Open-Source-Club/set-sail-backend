package com.tustosc.setsail.Entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("tutorial")
public class Tutorial {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String description;

    private Float costTime;

    private String imageLink;

    private String achievement;

    private String howManyLearned;

    private List<Chapter> chapters;
}
