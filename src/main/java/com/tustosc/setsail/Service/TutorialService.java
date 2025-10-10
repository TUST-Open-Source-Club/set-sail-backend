package com.tustosc.setsail.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tustosc.setsail.Entiy.Chapter;
import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Mappers.ChapterMapper;
import com.tustosc.setsail.Mappers.CourseMapper;
import com.tustosc.setsail.Mappers.TutorialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TutorialService {

    @Autowired
    private TutorialMapper tutorialMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CourseMapper courseMapper;

    public List<Tutorial> getAllTutorials(){
        QueryWrapper<Tutorial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        return tutorialMapper.selectList(queryWrapper);
    }

    public Tutorial getTutorialById(int id){
        Tutorial tutorial = tutorialMapper.selectById(id);

        List<Chapter> chapters=new ArrayList<>();

        Arrays.stream(tutorial.getChapterIds().split(","))
                .forEach(idString -> {
                    Chapter chapter=chapterMapper.selectById(Integer.parseInt(idString));
                    List<Course> courses=new ArrayList<>();
                    Arrays.stream(chapter.getCourseIds().split(","))
                                    .forEach(courseString -> {
                                        courses.add(courseMapper.selectById(courseString));
                                    });
                    chapter.setCourses(courses);
                    chapters.add(chapter);
                });
        tutorial.setChapters(chapters);
        return tutorial;
    }



}
