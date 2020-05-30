package com.yzpnb.eduservice.feign.impl;

import com.yzpnb.common_utils.Result;
import com.yzpnb.eduservice.feign.FeignToVideoClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeignToVideoClientImpl implements FeignToVideoClient {
    @Override
    public Result removeVideo(String videoId) {
        return Result.ok().data("请求出错进入Hystrix","容错");
    }

    @Override
    public Result removeVideoList(List<String> videoIdList) {
        return Result.ok().data("请求出错进入Hystrix","容错");
    }
}
