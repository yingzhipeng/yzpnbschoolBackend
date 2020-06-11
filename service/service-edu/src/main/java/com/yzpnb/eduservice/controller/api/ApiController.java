package com.yzpnb.eduservice.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduCourse;
import com.yzpnb.eduservice.entity.EduTeacher;
import com.yzpnb.eduservice.service.EduCourseService;
import com.yzpnb.eduservice.service.EduTeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/eduservice/api/")
@CrossOrigin
public class ApiController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;


    @ApiOperation("获取最新添加的8门课程和4名教师")
    @GetMapping("selectNewRecommended")
    public Result selectNewRecommended() {
        //查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(wrapper);

        //查询前4条名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(wrapperTeacher);

        Map<String,Object> map=new HashMap<>();
        map.put("courseList",courseList);
        map.put("teacherList",teacherList);
        return Result.ok().data("newRecommended",map);
    }
}
