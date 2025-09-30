package com.tustosc.setsail.Service;

import com.tustosc.setsail.Entiy.LearningPath;
import com.tustosc.setsail.Mappers.LearningPathMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningPathService {

    @Autowired
    private LearningPathMapper learningPathMapper;

    public LearningPath getLearningPathDetail(String id) {
        // 获取完整的学习路径信息
        LearningPath learningPath = learningPathMapper.selectById(id);

        // 如果教程列表存在，确保只返回教程的名称和描述
        if (learningPath.getTutorials() != null) {
            learningPath.getTutorials().forEach(tutorial -> {
                // 只保留教程的名称和描述
                tutorial.setName(tutorial.getName());
                tutorial.setDescription(tutorial.getDescription());
            });
        }

        return learningPath;
    }
}
