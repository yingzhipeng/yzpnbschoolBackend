package com.yzpnb.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.vo.CourseAllInfoVo;
import com.yzpnb.eduservice.entity.vo.CourseInfoVo;
import com.yzpnb.eduservice.feign.FeignToVideoClient;
import com.yzpnb.eduservice.service.EduChapterService;
import com.yzpnb.eduservice.service.EduCourseDescriptionService;
import com.yzpnb.eduservice.service.EduCourseService;
import com.yzpnb.eduservice.service.EduVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 * 课程基本信息
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    /**
     * 查询
     */

    @ApiOperation("根据id查询课程基本信息和简介")
    @GetMapping("selectById/{id}")
    public Result selectById(@ApiParam(name = "id",value = "课程id")
                             @PathVariable String id){
        CourseInfoVo courseInfoVo=eduCourseService.selectCourseInfo(id);
        return Result.ok().data("courseInfoVo",courseInfoVo);
    }

    @ApiOperation("根据id查询出指定课程id的课程id，课程标题，课程价格，总课时，课程封面，课程讲师，课程简介，课程类别一级和二级")
    @GetMapping("selectCourseAllInfoVoById/{id}")
    public Result selectCourseAllInfoVoById(@PathVariable String id){
        CourseAllInfoVo courseAllInfoVo = eduCourseService.selectCourseAllInfoVo(id);
        return Result.ok().data("courseAllInfoVo",courseAllInfoVo);
    }

    @ApiOperation("分页查询课程信息")
    @GetMapping("limitSelectCourseAllInfoVo/{current}/{size}")
    public Result limitSelect(@ApiParam(name = "current",value = "当前页")
                              @PathVariable Long current,
                              @ApiParam(name = "size",value = "每页记录数")
                              @PathVariable Long size){
        List<CourseAllInfoVo> courseAllInfoVoList= eduCourseService.limitSelectCourseAllInfoVo(current,size);
        Map<String,Object> map=new HashMap<>();
        map.put("courseAllInfoVoList",courseAllInfoVoList);
        map.put("total",eduCourseService.count());//添加数据总量
        return Result.ok().data("courseAllInfoVoMap",map);
    }

    /**
     * 修改
     */
    @ApiOperation("根据id修改课程信息")
    @PostMapping("updateCourseInfoVo")
    public Result updateCourseInfoVo(@RequestBody CourseInfoVo courseInfoVo){

        eduCourseService.updateCourseInfoVo(courseInfoVo);
        return Result.ok();
    }

    @ApiOperation("修改指定id的发布状态,如果是未发布Draft就改为Normal已发布，如果是已发布Normal，就改为Draft未发布")
    @PostMapping("updateStatus/{id}")
    public Result updateStatus(@ApiParam(name ="id" ,value ="课程id" )
                               @PathVariable String id){
        eduCourseService.updateStatus(id);
        return Result.ok();
    }
    /**
     * 添加
     */
    //1、添加课程基本信息
    @PostMapping("insertCourseInfo")
    public Result insertCourseInfo(@RequestBody CourseInfoVo courseInfoVo)//将用户提交的表单保存到此对象中
    {
        //调用接口
        String id=eduCourseService.insertCourseInfo(courseInfoVo);
        return Result.ok().data("courseId",id);
    }

    /**
     * 删除课程，需要删除课程信息表，简介表，章节表,小节表
     */
    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    EduChapterService eduChapterService;
    @Autowired
    EduVideoService eduVideoService;
    @Autowired
    FeignToVideoClient feignToVideoClient;
    @ApiOperation("删除课程")
    @DeleteMapping("deleteCourseById/{id}")
    public Result deleteCourse(@ApiParam(name = "id",value = "课程id")
                               @PathVariable String id ){

        //根据课程id获取所有小节的视频id
        List<String> idList = eduVideoService.getVideoSourceIdList(id);

        if(idList.size()>0){//如果List集合>0表示有视频，小于0则没有视频，不执行删除视频操作
            //删除视频
            feignToVideoClient.removeVideoList(idList);
        }


        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("course_id",id);

        //删除小节
        eduVideoService.remove(queryWrapper);
        //删除章节
        eduChapterService.remove(queryWrapper);
        //删除简介
        eduCourseDescriptionService.removeById(id);
        //删除课程信息
        eduCourseService.removeById(id);

        return Result.ok();
    }
}

