package com.yzpnb.video.util;


import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;

import java.io.InputStream;
import java.util.List;

public class AliyunVideoUtil {
    private String accessKeyId="LTAI4GGf4cjh4zZdNeKqUUGw";//id
    private String accessKeySecret="oQpda38y0cCUql5TOmaYiSJYDz32OP";//密钥
    private DefaultAcsClient client;
    /**
     * 初始化方法
     * @param accessKeyId 你的证书id
     * @param accessKeySecret   你的密钥
     * @return
     * @throws ClientException
     */
    public DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 本地文件上传接口，并使用模板组转码(流形式)
     *
     * @paramT accessKeyId 已经封装到工具类，无需声明
     * @paramT accessKeySecret 已经封装到工具类，无需声明
     * @param title 上传之后文件名
     * @param fileName  本地文件的路径和名称，就是你要上传的文件路径
     * @param inputStream 上传文件流
     */
    public String uploadVideo(String title, String fileName, InputStream inputStream) {
        /**设置上传请求头，包含转码*/
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName,inputStream);
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 模板组ID(可选) */
        request.setTemplateGroupId("fc4c9920e7332c6c8d9518d4c00aaf54");

        /**执行流上传，获取响应体*/
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);


        if (response.isSuccess()) {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            if(response.getVideoId()!=null) return response.getVideoId();
        }
            /****如果上传成功就将视频id返回****/
            return response.getVideoId();
    }

    /**
     * 提交媒体处理作业,视频转码
     */
    public SubmitTranscodeJobsResponse submitTranscodeJobs(String id) throws Exception {
        client=initVodClient(accessKeyId,accessKeySecret);

        SubmitTranscodeJobsRequest request = new SubmitTranscodeJobsRequest();
        //需要转码的视频ID
        request.setVideoId(id);
        //转码模板ID
        request.setTemplateGroupId("fc4c9920e7332c6c8d9518d4c00aaf54");
        return client.getAcsResponse(request);
    }

    /*获取播放地址函数*/
    public String getPlayInfo(String id) throws Exception {
        client=initVodClient(accessKeyId,accessKeySecret);
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        request.setVideoId(id);
        response=client.getAcsResponse(request);
        try {
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");

        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
        return response.getRequestId();
    }
    /*获取播放凭证函数*/
    public String getVideoPlayAuth(String id) throws Exception {
        client=initVodClient(accessKeyId,accessKeySecret);
        /***获取播放凭证***/
        GetVideoPlayAuthRequest requestAuth = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse responseAuth = new GetVideoPlayAuthResponse();

        requestAuth.setVideoId(id);

        responseAuth=client.getAcsResponse(requestAuth);
        //播放凭证
        System.out.println("PlayAuth = " + responseAuth.getPlayAuth() + "\n");
        return responseAuth.getPlayAuth();
    }
    /**
     * 删除视频
     * @paramT client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public void deleteVideo(String ...idList) throws Exception {
        client=initVodClient(accessKeyId,accessKeySecret);
        DeleteVideoRequest request = new DeleteVideoRequest();
        DeleteVideoResponse response = new DeleteVideoResponse();
        StringBuffer stringBuffer=new StringBuffer();

        for (String id:idList) {
            stringBuffer.append(id + ",");//将所有视频id用逗号拼接
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);//删除最后多余的逗号

        //支持传入多个视频ID，多个用逗号分隔 request.setVideoIds("VideoId1,VideoId2");
        request.setVideoIds(stringBuffer.toString());

        try {
            response=client.getAcsResponse(request);
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");

    }

    /**
     * 删除多个视频
     * @paramT client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public void deleteVideo(List<String> idList) throws Exception {
        client=initVodClient(accessKeyId,accessKeySecret);
        DeleteVideoRequest request = new DeleteVideoRequest();
        DeleteVideoResponse response = new DeleteVideoResponse();
        StringBuffer stringBuffer=new StringBuffer();

        for (String id:idList) {
            stringBuffer.append(id + ",");//将所有视频id用逗号拼接
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);//删除最后多余的逗号

        //支持传入多个视频ID，多个用逗号分隔 request.setVideoIds("VideoId1,VideoId2");
        request.setVideoIds(stringBuffer.toString());

        try {
            response=client.getAcsResponse(request);
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
