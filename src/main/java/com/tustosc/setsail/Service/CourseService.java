package com.tustosc.setsail.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Entiy.Response;
import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Mappers.CourseMapper;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public Course getCourseDetail(String id) {
        return courseMapper.selectById(id);
    }

    public static Logger logger = LoggerFactory.getLogger(CourseService.class);

    public ResponseEntity<Object> updateCourse(Course course){
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(course, HttpStatus.OK);
        if(course.getId() == null){
            if(course.getHomework() == null){
                course.setHomework("本节课没有作业");
            }
            if(course.getName() == null){
                course.setName("无标题");
            }
            if(course.getDescription() == null){
                course.setDescription("");
            }
            if(course.getCostTime() == null){
                course.setCostTime(0.0F);
            }
            try {
                courseMapper.insert(course);
            }catch (Exception e){
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            try {
                courseMapper.updateById(course);
            }catch (Exception e){
                logger.error(e.getMessage());
                logger.error(Arrays.toString(e.getStackTrace()));
                responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return responseEntity;
    }

    public boolean deleteCourse(String id){
        boolean isSucceed=true;
        try{
            Course course=new Course();
            course.setId(id);
            course.setIsDeleted(true);
            courseMapper.updateById(course);
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            isSucceed=false;
        }
        return isSucceed;
    }
}
