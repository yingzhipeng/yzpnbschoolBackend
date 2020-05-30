package com.yzpnb.video.controller;

import com.yzpnb.common_utils.Result;
import com.yzpnb.video.service.VideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/videoservice/")
@CrossOrigin
public class VideoController {

    @Autowired
    VideoService videoService;

    @ApiOperation("上传视频（以流形式上传，并且直接转码）")
    @PostMapping("uploadVideo")
    public Result uploadVideo(@ApiParam(name = "videoFile",value = "用户上传的文件") MultipartFile file){
        System.out.println(file.toString());
        String videoId=videoService.uploadVideo(file);
        return  Result.ok().data("videoId",videoId);
    }

    @DeleteMapping("{videoId}")
    public Result removeVideo(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                              @PathVariable String videoId){
        videoService.removeVideo(videoId);
        return Result.ok().message("视频删除成功");
    }

    @DeleteMapping("removeVideoList")
    public Result removeVideoList(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                                  @RequestBody List<String> videoIdList){
        videoService.removeVideoList(videoIdList);
        return Result.ok().message("视频删除成功");
    }
}
