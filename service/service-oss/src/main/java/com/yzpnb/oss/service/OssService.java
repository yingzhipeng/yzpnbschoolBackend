package com.yzpnb.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    /**上传头像，返回上传后文件的路径*/
    String uploadFileAvatar(MultipartFile file);
}
