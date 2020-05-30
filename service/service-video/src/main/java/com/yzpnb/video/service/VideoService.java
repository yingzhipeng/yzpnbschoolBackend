package com.yzpnb.video.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    /**
     *
     * 上传本地视屏到阿里云
     * @param videoFile
     * @return
     */
    String uploadVideo(MultipartFile videoFile);

    void removeVideo(String videoId);

    void removeVideoList(List<String> videoIdList);
}
