package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/course/detail")
    public Course getCourseDetail(@RequestParam String id) {
        return courseService.getCourseDetail(id);
    }
}
