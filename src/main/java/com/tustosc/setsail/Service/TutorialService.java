package com.tustosc.setsail.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import tools.jackson.databind.ObjectMapper;
import com.tustosc.setsail.Entiy.Chapter;
import com.tustosc.setsail.Entiy.Course;
import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Mappers.ChapterMapper;
import com.tustosc.setsail.Mappers.CourseMapper;
import com.tustosc.setsail.Mappers.TutorialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
    @Autowired
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(TutorialService.class);

    /**
     * 获取教程列表
     * @return 教程列表，不包括章节和课程
     */
    public List<Tutorial> getAllTutorials(){
        QueryWrapper<Tutorial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        return tutorialMapper.selectList(queryWrapper);
    }

    /**
     * 按ID获取教程详情
     * @param id 教程ID
     * @return 教程，包括章节和课程
     */
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

    /**
     * 添加或更新教程
     * @param tutorial 教程实体类
     * @param ip IP地址
     * @return 是否成功
     */
    public boolean update(Tutorial tutorial, String ip){
        if(tutorial.getId()==null){
            logger.info("Tutorial id is null, adding new tutorial");
        }
        else{
            logger.info("Tutorial id is {}, updating tutorial", tutorial.getId());
        }
        logger.info("IP address is {}", ip);
        logger.info("Tutorial is {}", objectMapper.writeValueAsString(tutorial));

        // 如果前端给出的不是章节ID而是具体的章节数据结构
        if(tutorial.getChapterIds()==null || tutorial.getChapterIds().isEmpty()){
            logger.warn("Tutorial chapterIds is null, failed");
            return false;
        }

        boolean ret=true;
        try{
            if(tutorial.getId()==null){
                ret=tutorialMapper.insert(tutorial)>0;
            }
            else{
                tutorialMapper.updateById(tutorial);
            }
        }
        catch(Exception e){
            ret=false;
            logger.warn("Add or update tutorial {}: Update database failed.", tutorial.getId()==null?"":tutorial.getId());
            logger.warn(e.toString());
        }
        logger.info("Add or update tutorial succeed. Tutorial is {}", objectMapper.writeValueAsString(tutorial));
        return ret;

    }

    /**
     * 删除教程
     */
    private boolean delete(String tutorialId, String ip){
        logger.info("Tutorial id is {}, begin to delete. IP address: {}", tutorialId, ip);
        boolean ret=true;

        if(tutorialId==null){
            logger.info("Tutorial id is null, failed");
            return false;
        }

        Tutorial tutorial=tutorialMapper.selectById(Integer.parseInt(tutorialId));
        if(tutorial==null){
            logger.info("Tutorial id is {}, not exist, failed", tutorialId);
            return false;
        }

        Tutorial newTutorial=new Tutorial();
        newTutorial.setId(tutorialId);
        newTutorial.setIsDeleted(true);
        try {
            tutorialMapper.updateById(newTutorial);
        }
        catch(Exception e){
            ret=false;
            logger.warn("Delete tutorial {}: Update database failed.", tutorialId);

        }

        logger.info("Tutorial id is {}, soft deleted.", tutorialId);

        return ret;
    }

}
