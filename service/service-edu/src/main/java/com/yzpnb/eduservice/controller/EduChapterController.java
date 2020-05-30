package com.yzpnb.eduservice.controller;


import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduChapter;
import com.yzpnb.eduservice.entity.chapter.ChapterVo;
import com.yzpnb.eduservice.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    /**
     *
     * 查询
     */
    @ApiOperation("查询指定的课程大纲列表")
    @GetMapping("selectTree/{courseId}")
    public Result selectTree(   @ApiParam(name = "courseId",value = "课程id")
                                @PathVariable String courseId){

        List<ChapterVo> list= eduChapterService.selectChapterVideoByCourseId(courseId);//根据课程id查询章节和小节
        return Result.ok().data("chapterAndVideo",list);
    }

    @ApiOperation("根据Id查询章节信息")
    @GetMapping("selectById/{id}")
    public Result selectById(@ApiParam(name = "id",value = "章节id")
                             @PathVariable String id){

        EduChapter eduChapter = eduChapterService.getById(id);
        return Result.ok().data("chapter",eduChapter);
    }
    /**
     * 添加章节
     */
    @ApiOperation("添加章节")
    @PostMapping("insertChapter")
    public Result insertChapter(@ApiParam(name = "eduChapter",value = "章节信息数据")
                                @RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return Result.ok();
    }
    /**
     * 修改
     */
    @ApiOperation("修改章节")
    @PostMapping("updateChapter")
    public Result updateChapter(@ApiParam(name = "eduChapter",value = "章节信息数据")
                                @RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return Result.ok();
    }
    /**
     * 删除
     */
    @ApiOperation("删除章节")
    @DeleteMapping("{id}")
    public Result deleteById(@ApiParam(name = "id",value = "章节id")
                             @PathVariable String id) {
        eduChapterService.deleteChapterVideo(id);//删除章节后，需要对小节处理
        return Result.ok();
    }
}

