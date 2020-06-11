package com.yzpnb.statistics_service.feign;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-ucenter")
public interface FeignToUcenterClient {

    @ApiOperation("统计某一天的注册人数")
    @GetMapping(value = "/ucenter_service/ucenter-member/countregister")
    public Integer registerCount(@ApiParam(name = "day",value = "天数")
                                 @RequestParam(value = "day") String day);
}
