package com.yzpnb.eduservice.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-order")
@Component
public interface FeignToOrderClient {
    @ApiOperation("根据用户id和课程id查询订单信息，已经支付返回true，没有返回false")
    @GetMapping("/order_service/t-order/isBuyCourse")
    public boolean isBuyCourse(@RequestParam(value = "memberid") String memberid,
                               @RequestParam(value = "courseid") String courseid);
}
