package com.yzpnb.order_service.controller;


import com.yzpnb.common_utils.Result;
import com.yzpnb.order_service.service.TPayLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/order_service/t-pay-log")
@CrossOrigin
public class TPayLogController {

    @Autowired
    private TPayLogService tPayLogServicee;
    /**
     * 根据订单号生成支付二维码
     *
     * @return
     */
    @ApiOperation("根据订单号生成支付二维码")
    @GetMapping("createNative/{orderNo}")
    public Result createNative( @ApiParam(name = "orderNo",value = "订单号")
                                @PathVariable String orderNo) {
        Map map = tPayLogServicee.createNative(orderNo);
        return Result.ok().data(map);
    }

    @ApiOperation("根据订单号查询支付状态，支付成功，添加订单日志")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo) {

        //调用查询接口，查询订单状态（不是我们的数据库状态，而是微信中支付状态，为了方便，还是返回map）
        Map<String, String> map = tPayLogServicee.queryPayStatus(orderNo);

        if (map == null) {//没有值，表示没有支付，出错
            return Result.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {//获取微信返回数据中支付状态，如果成功
            //更改订单状态，改订单状态为1，并添加日志
            tPayLogServicee.updateOrderStatus(map);
            return Result.ok().message("支付成功");
        }

        return Result.ok().code(25000).message("支付中");
    }
}

