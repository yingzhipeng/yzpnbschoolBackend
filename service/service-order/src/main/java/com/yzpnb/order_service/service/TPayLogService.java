package com.yzpnb.order_service.service;

import com.yzpnb.order_service.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
public interface TPayLogService extends IService<TPayLog> {

    /**
     * 根据订单号生成微信支付二维码
     * @param orderNo
     * @return
     */
    Map createNative(String orderNo);

    /**
     * 根据订单号查询订单支付状态
     * @param orderNo
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);


    /**
     * 添加订单支付成功日志
     * @param map
     */
    void updateOrderStatus(Map<String, String> map);
}
