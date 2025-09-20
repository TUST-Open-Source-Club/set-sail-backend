package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Entiy.Response;
import com.tustosc.setsail.Service.CourseService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/course/detail")
    public Course getCourseDetail(@RequestParam String id) {
        return courseService.getCourseDetail(id);
    }

    @PostMapping("/admin/course/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }
}
