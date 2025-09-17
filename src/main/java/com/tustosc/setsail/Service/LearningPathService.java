package com.tustosc.setsail.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tustosc.setsail.Entity.LearningPath;
import com.tustosc.setsail.Entity.Tutorial;
import com.tustosc.setsail.Mappers.LearningPathMapper;
import com.tustosc.setsail.Mappers.TutorialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LearningPathService {

    @Autowired
    private LearningPathMapper learningPathMapper;

    @Autowired
    private TutorialMapper tutorialMapper;

    /**
     * 查询 LearningPath，自动填充教程列表
     * @param id 路线ID
     * @return LearningPath 实体类
     */
    private Optional<LearningPath> getLearningPathWithTutorials(String id) {
        LearningPath lp = learningPathMapper.selectById(id);
        if (lp == null) return Optional.empty();

        if (!StringUtils.hasText(lp.getTutorialIds())) {
            lp.setTutorials(Collections.emptyList());
            return Optional.of(lp);
        }

        // 解析 tutorialIds
        List<String> ids = Arrays.stream(lp.getTutorialIds().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            lp.setTutorials(Collections.emptyList());
            return Optional.of(lp);
        }

        // 查询所有教程
        List<Tutorial> tutorials = tutorialMapper.selectBatchIds(ids);

        // 保持顺序
        Map<String, Tutorial> map = tutorials.stream()
                .collect(Collectors.toMap(Tutorial::getId, t -> t));

        List<Tutorial> ordered = ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        lp.setTutorials(ordered);
        return Optional.of(lp);
    }

    /**
     * 对应 Controller 调用：根据 ID 获取学习路线详情
     * @param id 路线ID
     * @return LearningPath 实体类
     */
    public Optional<LearningPath> getLearningPathInfo(String id) {
        return getLearningPathWithTutorials(id);
    }

    /**
     * 对应 Controller 调用：获取所有学习路线
     * @return 学习路线列表
     */
    public List<LearningPath> listAllLearningPaths() {
        List<LearningPath> all = learningPathMapper.selectList(new QueryWrapper<>());
        all.forEach(lp -> getLearningPathWithTutorials(lp.getId()).ifPresent(full -> {
            lp.setTutorials(full.getTutorials());
        }));
        return all;
    }
}
