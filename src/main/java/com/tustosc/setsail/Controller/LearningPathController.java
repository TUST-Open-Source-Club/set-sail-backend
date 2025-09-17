package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entity.LearningPath;
import com.tustosc.setsail.Service.LearningPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/learning-paths")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    /**
     * 获取所有学习路线（包含教程列表）
     */
    @GetMapping
    public List<LearningPath> listAllLearningPaths() {
        return learningPathService.listAllLearningPaths();
    }

    /**
     * 根据 ID 获取学习路线详情（包含教程列表）
     */
    @GetMapping("/{id}")
    public Optional<LearningPath> getLearningPath(@PathVariable String id) {
        return learningPathService.getLearningPathInfo(id);
    }
}
