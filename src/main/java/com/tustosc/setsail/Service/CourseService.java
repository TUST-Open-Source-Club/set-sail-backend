package com.tustosc.setsail.Service;

import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public Course getCourseDetail(String id) {
        return courseMapper.selectById(id);
    }
}
