package com.yzpnb.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduTeacher;
import com.yzpnb.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-07
 */
@Api(value="讲师eduteacher的增删改查")
@RestController//此注解功能和Controller相同，但是增加了Rest功能，也就是返回JSON数据，我们以往都是在方法上添加@ResponseBody注解，现在，可以省略这个注解了
@RequestMapping("/eduservice/")//注意我这里设置的路径
@Slf4j//日志注解
@CrossOrigin//解决跨域问题
public class EduTeacherController {

    /*注入service*/
    @Autowired
    EduTeacherService eduTeacherService;

    /**
     * 查询
     * REST风格
     * */
    //1、查询所有讲师数据
    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")//这里没写@ResponseBody注解，是因为我的类使用的@ResrController注解，封装了这个注解
    public Result findAll()
    {
        //list方法是代码生成器帮我们生成的service接口继承的工具类提供的方法，用来查询所有数据
        //通过Result.ok()的形式获取Result对象，然后通过我们设置值的方法data添加一条数据
        return Result.ok().data("allEduTeacher",eduTeacherService.list());
    }
    //2、分页查询讲师
    @ApiOperation(value="分页查询讲师")
    @GetMapping("limitSelect/{current}/{size}")
    public Result limitSelect(@ApiParam(name="current",value ="当前页",required=true)
                                @PathVariable("current") Long current,
                              @ApiParam(name="size",value="每页记录数")
                                @PathVariable("size") Long size){
        //1、创建page对象
        Page<EduTeacher> page=new Page<>(current,size);
        //2、通过接口中方法实现分页
        return Result.ok().data("limitEduTeacher",eduTeacherService.page(page));
    }
    //3、按id查询
    @ApiOperation("根据id查询数据")
    @GetMapping("selectById/{id}")
    public Result selectById(@PathVariable("id") Long id){
        return Result.ok().data("eudTeacher",eduTeacherService.getById(id));
    }

    /**
     * 删除
     * */
    //1、根据id删除
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@ApiParam(name="id",value = "标注在请求方法参数上的解释信息",required = true) @PathVariable String id)
    {
        if(eduTeacherService.removeById(id))//继承的方法，根据id删除数据,删除成功返回true，失败返回false
        {
            return Result.ok();
        }
        else{
            return Result.error();
        }
    }
    /**
     * 添加
     * */
    //1、添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("insertEduTeacher")
    public Result insertEduTeacher(@ApiParam(name = "eduTeacher",value ="添加的json数据" )
                                   @RequestBody EduTeacher eduTeacher)//@RequestBody可将传来的json字符串转换为指定对象
    {
        if(eduTeacherService.save(eduTeacher))
        {
            return Result.ok();
        }
        else
        {
            return Result.error();
        }

    }
    /**
     * 修改
     * */
    @ApiOperation("根据id修改讲师信息")
    @PutMapping("updateEduTeacher")
    public Result updateEduTeacher(@ApiParam(name="eduTeacher",value="修改的讲师json")
                                   @RequestBody EduTeacher eduTeacher)
    {
        //2、修改
        if(eduTeacherService.updateById(eduTeacher)){
            return Result.ok();
        }
        else
        {
            return Result.error();
        }

    }
}

