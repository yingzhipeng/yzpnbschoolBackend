package com.yzpnb.order_service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.yzpnb.order_service.entity.TOrder;
import com.yzpnb.order_service.entity.TPayLog;
import com.yzpnb.order_service.mapper.TPayLogMapper;
import com.yzpnb.order_service.service.TOrderService;
import com.yzpnb.order_service.service.TPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzpnb.order_service.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService tOrderService; //引入订单接口

    /**
     * 根据订单号获取支付二维码
     * @param orderNo
     * @return
     */
    @Override
    public Map createNative(String orderNo) {
        try {
            //根据订单id获取订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            TOrder order = tOrderService.getOne(wrapper);

            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");               //关联公众号
            m.put("mch_id", "1558950191");                      //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());   //微信官方提供工具类，帮我们生成随机字符串，让我们每个二维码都不一样
            m.put("body", order.getCourseTitle());              //主体，一般写课程名称什么的，表示订单名称
            m.put("out_trade_no", orderNo);                     //二维码唯一标识，这里我直接填订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//订单价格，改成圆角分的Long类型，并转为字符串
            m.put("spbill_create_ip", "127.0.0.1");             //支付ip地址，一般写你的项目域名yzpnb.XXXX，这里做本地测试，所以写127.0.0.1
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");//回调地址
            m.put("trade_type", "NATIVE");//生成支付类型，NATIAE表示根据价格生成二维码

            //2、HTTPClient来根据URL访问第三方接口并且传递参数，发送httpclient请求，传递参数xml格式，传入微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));//使用了微信工具类，通过商户key对参数进行加密
            client.setHttps(true);//我们访问https请求，默认不支持，将其设置为支持
            client.post();//使用post发送http请求

            //3、返回第三方的数据
            String xml = client.getContent();//返回信息为xml格式信息
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);//通过微信工具类，将xml格式解析为map集合

            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);                       //订单编号
            map.put("course_id", order.getCourseId());              //课程id
            map.put("total_fee", order.getTotalFee());              //总价格
            map.put("result_code", resultMap.get("result_code"));   //发送请求后状态码（微信返回的）
            map.put("code_url", resultMap.get("code_url"));         //二维码地址(微信返回的)

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();//请求二维码失败，返回空的map集合
        }
    }

    /**
     * 根据订单号查询订单支付状态（微信）
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");               //公众号
            m.put("mch_id", "1558950191");                      //商户号
            m.put("out_trade_no", orderNo);                     //二维码唯一标识，这里我直接填订单号
            m.put("nonce_str", WXPayUtil.generateNonceStr());   //微信官方提供工具类，帮我们生成随机字符串

            //2、设置请求
            //HTTPClient来根据URL访问第三方接口并且传递参数，发送httpclient请求，传递参数xml格式，传入微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));//使用了微信工具类，通过商户key对参数进行加密
            client.setHttps(true);//我们访问https请求，默认不支持，将其设置为支持
            client.post();//使用post发送http请求
            //3、返回第三方的数据
            String xml = client.getContent();//返回信息为xml格式信息
            //4、转成Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);//通过微信工具类，将xml格式解析为map集合
            //5、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更改订单状态，改订单状态为1，并添加日志
     * @param map
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");

        //根据订单id查询订单信息
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder order = tOrderService.getOne(wrapper);

        if(order.getStatus().intValue() == 1) return;//为1表示已经支付，不执行下面的操作
        order.setStatus(1);//否则就设置为1
        tOrderService.updateById(order);//修改订单状态

        //记录支付日志
        TPayLog payLog=new TPayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
