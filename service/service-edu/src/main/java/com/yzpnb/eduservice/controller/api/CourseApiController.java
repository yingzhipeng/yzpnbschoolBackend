package com.yzpnb.eduservice.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.utils.JwtUtils;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduCourse;
import com.yzpnb.eduservice.entity.chapter.ChapterVo;
import com.yzpnb.eduservice.entity.vo.CourseApiInfoVo;
import com.yzpnb.eduservice.entity.vo.CourseApiVo;
import com.yzpnb.eduservice.feign.FeignToOrderClient;
import com.yzpnb.eduservice.service.EduChapterService;
import com.yzpnb.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/eduservice/api/course")
@CrossOrigin
public class CourseApiController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation("条件分页查询")
    @PostMapping("selectIfLimitCourse/{page}/{limit}")
    public Result selectIfLimitCourse(@ApiParam(name = "page",value = "当前页")
                                      @PathVariable Long page,
                                      @ApiParam(name = "limit",value="每页记录数")
                                      @PathVariable Long limit,
                                      @ApiParam(name = "courseApiVo",value = "课程和课程条件对象,没有值也可以，不报错",required = false)
                                      @RequestBody(required = false) CourseApiVo courseApiVo){

        Page<EduCourse> pageCourse=new Page<>(page,limit);

        Map<String,Object> map=eduCourseService.selectIfLimitCourse(pageCourse,courseApiVo);

        return Result.ok().data(map);
    }

    @Autowired
    private EduChapterService eduChapterService;
    @ApiOperation("根据id获取课程详细信息，包括讲师和课程大纲")
    @GetMapping(value = "selectCourserApiInfoVoById/{id}")
    public Result selectCourserApiInfoVoById(@ApiParam(name = "id", value = "课程ID", required = true)
                                             @PathVariable String id){

        //查询课程信息和讲师信息
        CourseApiInfoVo courseApiInfoVo = eduCourseService.selectCourserApiInfoVoById(id);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = eduChapterService.selectChapterVideoByCourseId(id);

        Map<String,Object> map=new HashMap<>();
        map.put("course", courseApiInfoVo);
        map.put("chapterVoList", chapterVoList);

        return Result.ok().data(map);
    }

    //根据id查询课程详情信息
    @Autowired
    private FeignToOrderClient feignToOrderClient;
    @ApiOperation("根据id查询课程详情，并判断课程十分被购买")
    @GetMapping("getCourseInfo/{id}")
    public Result getCourseInfo(@PathVariable(value = "id") String id, HttpServletRequest request) {
        //课程查询课程基本信息
        CourseApiInfoVo courseApiInfoVo = eduCourseService.selectCourserApiInfoVoById(id);
        //查询课程里面大纲数据
        List<ChapterVo> chapterVideoList = eduChapterService.selectChapterVideoByCourseId(id);

        //远程调用，判断课程是否被购买
        boolean buyCourse = feignToOrderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request), id);

        Map<String , Object> map=new HashMap<>();
        map.put("courseFrontInfo",courseApiInfoVo);
        map.put("chapterVideoList",chapterVideoList);
        map.put("isbuy",buyCourse);
        return Result.ok().data(map);
    }
}
