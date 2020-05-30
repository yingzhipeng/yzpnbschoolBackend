package com.yzpnb.eduservice.controller;


import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.entity.EduVideo;
import com.yzpnb.eduservice.feign.FeignToVideoClient;
import com.yzpnb.eduservice.service.EduVideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-24
 */
@RestController
@RequestMapping("/eduservice/edu-video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private FeignToVideoClient feignToVideoClient;

    /**
     * 查询
     */
    @ApiOperation("根据id查询小节")
    @GetMapping("selectById/{id}")
    public Result selectById(@ApiParam(name = "id",value = "小节id")
                             @PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        return Result.ok().data("video",eduVideo);
    }
    /**
     * 添加
     */
    @ApiOperation("添加小节")
    @PostMapping("insertVideo")
    public Result insertVideo(@ApiParam(name="eduVideo",value="小节对象json")
                              @RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return Result.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("根据id修改小节内容")
    @PostMapping("updateVideo")
    public Result updateVideo(@ApiParam(name = "eduVideo",value = "小节对象json")
                              @RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return Result.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("根据id删除小节")
    @DeleteMapping("{id}")
    public Result deleteVideoById(@ApiParam(name="id",value="小节id")
                                  @PathVariable String id){
        //根据小节id获取小节对象中的视频id
        String videoSourceId = eduVideoService.getById(id).getVideoSourceId();

        if(!StringUtils.isEmpty(videoSourceId)){//判断当前小节是否有视频，使用的spirng提供的工具类

            //调用Feign接口中方法，调用阿里云视频点播微服务中的根据视频id删除视频的方法
            feignToVideoClient.removeVideo(videoSourceId);
        }

        //删除小节
        eduVideoService.removeById(id);
        return Result.ok();
    }


}

