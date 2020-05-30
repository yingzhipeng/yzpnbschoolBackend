package com.yzpnb.oss.controller;

import com.yzpnb.common_utils.Result;
import com.yzpnb.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ossservice/")
@CrossOrigin//解决跨域
public class OssController {

    @Autowired
    private OssService ossService;
    //上传头像
    @PostMapping("uploadFile")
    public Result uploadFile(MultipartFile file){
        //1、获取上传的文件 MultipartFile 可以直接获取上传的文件
        //2、返回上传到oss的路径
        String url=ossService.uploadFileAvatar(file);
        return Result.ok().data("url",url);
    }
}
