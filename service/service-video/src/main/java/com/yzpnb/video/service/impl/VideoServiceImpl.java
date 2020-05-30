package com.yzpnb.video.service.impl;

import com.yzpnb.service_base_handler.CustomExceptionHandler;
import com.yzpnb.video.service.VideoService;
import com.yzpnb.video.util.AliyunVideoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    /**
     *
     * 上传本地视屏到阿里云
     * @param videoFile
     * @return
     */
    @Override
    public String uploadVideo(MultipartFile videoFile) {
        /**1、上传**/
        //1、创建封装阿里云视频操作的对象
        AliyunVideoUtil aliyunVideoUtil=new AliyunVideoUtil();
        
        String videoId = null;
        //2、获取输入流，文件名等等
        try {
            InputStream inputStream = videoFile.getInputStream();
            String fileName=videoFile.getOriginalFilename();//获取文件名
            /**
             * 设置上传文件名，获取文件名中第一个字符到“.”的前一个字符
             * substring：字符串截取函数，截取起始索引到末尾索引的前一个字符
             * lastIndexOf：从字符串最后面往前找，找我们指定的字符，找到的第一匹配的字符，返回字符的下标
             */
            String title=fileName.substring(0,fileName.lastIndexOf("."));
            videoId=aliyunVideoUtil.uploadVideo(title,fileName,inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoId;
    }

    /**
     * 根据id删除视频
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {
        //1、创建封装阿里云视频操作的对象
        AliyunVideoUtil aliyunVideoUtil=new AliyunVideoUtil();
        //2、删除视频
        try {
            aliyunVideoUtil.deleteVideo(videoId);
        } catch (Exception e) {
            throw new CustomExceptionHandler(20001,"删除失败");
        }
    }

    /**
     * 根据id集合批量删除视频
     * @param videoIdList
     */
    @Override
    public void removeVideoList(List<String> videoIdList) {
        //1、创建封装阿里云视频操作的对象
        AliyunVideoUtil aliyunVideoUtil=new AliyunVideoUtil();
        //2、删除视频
        try {
            aliyunVideoUtil.deleteVideo(videoIdList);
        } catch (Exception e) {
            throw new CustomExceptionHandler(20001,"删除失败");
        }
    }
}
