package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Entiy.Response;
import com.tustosc.setsail.Enums.RedisPrefix;
import com.tustosc.setsail.Service.CourseService;
import com.tustosc.setsail.Utils.RedisUtils;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/course/detail")
    public Course getCourseDetail(@RequestParam String id) {
        return courseService.getCourseDetail(id);
    }

    @PostMapping("/admin/course/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    @DeleteMapping("/admin/course/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteCourse(@RequestParam String id){
        if(redisUtils.set(RedisPrefix.DELETE_COURSE, id, "", 3600L)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
