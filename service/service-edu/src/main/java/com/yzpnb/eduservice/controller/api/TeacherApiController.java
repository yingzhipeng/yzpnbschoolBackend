package com.yzpnb.eduservice.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduCourse;
import com.yzpnb.eduservice.entity.EduTeacher;
import com.yzpnb.eduservice.service.EduCourseService;
import com.yzpnb.eduservice.service.EduTeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/api/teacher")
@CrossOrigin
public class TeacherApiController {


    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation("分页查询讲师")
    @GetMapping("limitSelectTeacher/{page}/{limit}")
    public Result limitSelectTeacher(@ApiParam(name="page",value = "当前页")
                                     @PathVariable Long page,
                                     @ApiParam(name = "limit",value = "每页记录数")
                                     @PathVariable Long limit){

        Page<EduTeacher> pageTeacher=new Page<>(page,limit);

        Map<String,Object> map=eduTeacherService.limitSelectTeacher(pageTeacher);

        return Result.ok().data(map);
    }

    @Autowired
    EduCourseService eduCourseService;
    @ApiOperation("根据id查询讲师信息和他讲的课程")
    @GetMapping("selectTeacherAndCourse/{id}")
    public Result selectTeacherAndCourse(@ApiParam(name = "id",value = "讲师id")
                                         @PathVariable String id){

        //查询讲师信息
        EduTeacher eduTeacher = eduTeacherService.getById(id);

        //查询课程
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("teacher_id",id);
        List<EduCourse> eduCourseList = eduCourseService.list(queryWrapper);

        //将数据存入map集合
        Map<String,Object> map=new HashMap<>();
        map.put("teacher",eduTeacher);
        map.put("courseList",eduCourseList);

        //返回数据
        return Result.ok().data(map);
    }
}
