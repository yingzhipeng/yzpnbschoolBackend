package com.yzpnb.order_service.feign;

import com.yzpnb.order_service.entity.ucenter.UcenterMember;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="service-ucenter")
@Component
public interface FeignToUcenterClient {

    @ApiOperation("根据id获取用户信息")
    @PostMapping("/ucenter_service/ucenter-member/selectById")
    public UcenterMember selectById(@RequestParam("id") String id);
}
