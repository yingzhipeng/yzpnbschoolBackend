package com.yzpnb.eduservice.feign;

import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.feign.impl.FeignToVideoClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-video",fallback = FeignToVideoClientImpl.class) //标识此接口是一个使用Feign的接口，参数是你要互联的微服务名（注册中心的名字（不是自己的，是你要互联的微服务））
@Component //标识此接口是一个组件
public interface FeignToVideoClient {

    //定义你要调用的接口路径（和前端api调用类似）

    /**
     * 阿里云视频点播微服务的接口
     * @param videoId 云端视频id
     * @return
     */
    @DeleteMapping("/videoservice/{videoId}")
    public Result removeVideo(@PathVariable(value = "videoId") String videoId);

    /**
     *
     * 阿里云视频点播微服务的批量删除视频接口
     * @param videoIdList
     * @return
     */
    @DeleteMapping("/videoservice/removeVideoList")
    public Result removeVideoList(@RequestBody List<String> videoIdList);
}
