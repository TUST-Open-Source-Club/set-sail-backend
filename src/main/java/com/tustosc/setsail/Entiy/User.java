package com.tustosc.setsail.Entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@TableName("user")
public class User implements UserDetails {

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

    private final List<String> privileges;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return privileges.stream().map(Role::from).collect(Collectors.toList());
    }

}
