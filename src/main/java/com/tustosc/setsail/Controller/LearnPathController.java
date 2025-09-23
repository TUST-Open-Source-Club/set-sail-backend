package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Service.LearningPathService;
import com.tustosc.setsail.Entiy.LearningPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearnPathController {

    @Autowired
    private LearningPathService learningPathService;

    @GetMapping("/learningpath/detail")
    public LearningPath getLearningPathDetail(@RequestParam String id) {
        return learningPathService.getLearningPathDetail(id);
    }
}
