package com.tustosc.setsail.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tustosc.setsail.Entiy.LearningPath;
import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Entiy.User;
import com.tustosc.setsail.Mappers.LearningPathMapper;
import com.tustosc.setsail.Mappers.TutorialMapper;
import com.tustosc.setsail.Mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TutorialMapper tutorialMapper;

    @Autowired
    private LearningPathMapper learningPathMapper;

    /**
     * 查询User，自动化填充学习路线和教程
     * @param uuid 用户ID
     * @return 用户实体类
     */
    private Optional<User> getUserWithLearningPathAndTutorial(String uuid) {
        User user = userMapper.selectById(uuid);
        if (user == null) return Optional.empty();

        QueryWrapper<LearningPath> queryWrapper = new QueryWrapper<>();
        List<String> ids = List.of(user.getLearningPathIds().split(","));
        queryWrapper.in("id", ids);
        List<LearningPath> learningPaths = learningPathMapper.selectList(queryWrapper);

        QueryWrapper<Tutorial> queryWrapperTutorial = new QueryWrapper<>();
        queryWrapperTutorial.in("id", List.of(user.getTutorialIds().split(",")));
        List<Tutorial> tutorials = tutorialMapper.selectList(queryWrapperTutorial);

        user.setLearningPaths(learningPaths);
        user.setTutorials(tutorials);
        return Optional.of(user);
    }

    /**
     * 对应查询User的Controller
     * @param uuid 用户ID
     * @return 用户实体类
     */
    public Optional<User> getUserInfo(String uuid) {
        return getUserWithLearningPathAndTutorial(uuid);
    }
}