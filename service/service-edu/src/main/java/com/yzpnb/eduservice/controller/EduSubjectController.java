package com.yzpnb.eduservice.controller;


import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.subject.OneSubject;
import com.yzpnb.eduservice.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-14
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    /**
     * 查询
     */
    //1、查询课程，根据id和parend_id决定树形结构
    @GetMapping("selectTree")
    @ApiOperation("查询树形结构课程目录")
    public Result selectTree(){
        List<OneSubject> list=eduSubjectService.selectAllOneSubject();
        return Result.ok().data("allSubject",list);
    }
    /**
     * 添加
     */
    //1、根据excel表格插入数据，获取用户上传的excel文件，读取数据
    @PostMapping("insertExcel")
    @ApiOperation("上传excel文件添加课程")
    public Result insertByExcel(MultipartFile file){
        eduSubjectService.insertByExcel(file,eduSubjectService);
        return Result.ok();
    }
}

