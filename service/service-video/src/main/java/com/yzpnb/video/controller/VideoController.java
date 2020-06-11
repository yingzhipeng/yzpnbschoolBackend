package com.yzpnb.video.controller;

import com.yzpnb.common_utils.Result;
import com.yzpnb.video.service.VideoService;
import com.yzpnb.video.util.AliyunVideoUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
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

    @ApiOperation("根据视频id删除视频")
    @DeleteMapping("{videoId}")
    public Result removeVideo(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                              @PathVariable String videoId){
        videoService.removeVideo(videoId);
        return Result.ok().message("视频删除成功");
    }

    @ApiOperation("批量删除视频")
    @DeleteMapping("removeVideoList")
    public Result removeVideoList(@ApiParam(name = "videoId", value = "云端视频id", required = true)
                                  @RequestBody List<String> videoIdList){
        videoService.removeVideoList(videoIdList);
        return Result.ok().message("视频删除成功");
    }

    @ApiOperation("根据视频id获取视频凭证")
    @GetMapping("getVideoPlayAuth/{id}")
    public Result getVideoPlayAuth(@ApiParam(name = "id",value = "视频id")
                                   @PathVariable String id){

        try {
            String playAuth=new AliyunVideoUtil().getVideoPlayAuth(id);
            return Result.ok().message("视频凭证获取成功").data("playAuth",playAuth);
        } catch (Exception e) {
            return Result.error().message("视频凭证获取失败");
        }
    }
}
