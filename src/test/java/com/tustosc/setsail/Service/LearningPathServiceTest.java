package com.tustosc.setsail.Service;

import com.tustosc.setsail.Entiy.LearningPath;
import com.tustosc.setsail.Entiy.Tutorial;
import com.tustosc.setsail.Mappers.LearningPathMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LearningPathServiceTest {

    @InjectMocks
    private LearningPathService learningPathService;

    @Mock
    private LearningPathMapper learningPathMapper;

    private LearningPath learningPath;

    @BeforeEach
    public void setUp() {
        // 初始化 Mockito 注解
        MockitoAnnotations.openMocks(this);

        // 创建测试数据
        Tutorial tutorial1 = new Tutorial();
        tutorial1.setName("Java Basics");
        tutorial1.setDescription("Introduction to Java programming.");

        Tutorial tutorial2 = new Tutorial();
        tutorial2.setName("Advanced Java");
        tutorial2.setDescription("Deep dive into advanced Java concepts.");

        // 创建 LearningPath 对象
        learningPath = new LearningPath();
        learningPath.setId("1");
        learningPath.setName("Java Programming Path");
        learningPath.setDescription("A complete learning path for mastering Java.");
        learningPath.setCostTime(100.0f);
        learningPath.setAchievement("Java Developer Certificate");
        learningPath.setImageLink("http://example.com/java-path.jpg");
        learningPath.setHowManyLearned("2000");
        learningPath.setTutorials(Arrays.asList(tutorial1, tutorial2));
    }

    @Test
    public void testGetLearningPathDetail() {
        // 模拟学习路径查询
        when(learningPathMapper.selectById("1")).thenReturn(learningPath);

        // 调用服务层方法
        LearningPath result = learningPathService.getLearningPathDetail("1");

        // 验证学习路径信息
        assertNotNull(result);
        assertEquals("Java Programming Path", result.getName());
        assertEquals("A complete learning path for mastering Java.", result.getDescription());
        assertEquals(100.0f, result.getCostTime());
        assertEquals("Java Developer Certificate", result.getAchievement());
        assertEquals("http://example.com/java-path.jpg", result.getImageLink());
        assertEquals("2000", result.getHowManyLearned());

        // 验证教程列表中的每个教程只包含 name 和 description
        assertNotNull(result.getTutorials());
        assertEquals(2, result.getTutorials().size());

        // 验证教程 1
        Tutorial tutorial1 = result.getTutorials().get(0);
        assertEquals("Java Basics", tutorial1.getName());
        assertEquals("Introduction to Java programming.", tutorial1.getDescription());

        // 验证教程 2
        Tutorial tutorial2 = result.getTutorials().get(1);
        assertEquals("Advanced Java", tutorial2.getName());
        assertEquals("Deep dive into advanced Java concepts.", tutorial2.getDescription());
    }
}
