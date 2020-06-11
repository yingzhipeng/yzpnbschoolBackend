package com.yzpnb.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.yzpnb.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    /**
     * 根据手机号发送短信
     * @param phoneNumber
     * @param map
     * @return
     */
    @Override
    public Boolean shortMessage(String phoneNumber, Map<String, Object> map) {
        if(StringUtils.isEmpty(phoneNumber)) return false;//如果手机号为空，返回false

        /**
         * 生成默认链接，第一个参数域节点，用默认即可
         * 第二个参数是阿里云id
         * 第三个参数是阿里云密钥
         */
        DefaultProfile profile=DefaultProfile.getProfile("default",
                                            "LTAI4GGf4cjh4zZdNeKqUUGw",
                                                "oQpda38y0cCUql5TOmaYiSJYDz32OP");
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //设置参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送内容
        request.putQueryParameter("PhoneNumbers", phoneNumber);//设置发送手机号
        request.putQueryParameter("SignName", "我的农大线上学院网站");//你申请的签名的名称
        request.putQueryParameter("TemplateCode", "SMS_192300134");//你的模板CODE
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));//验证码，需要用json格式，所以使用map，可以直接转换为json

        //发送
        boolean b=false;//默认为不成功
        try {
            CommonResponse commonResponse =acsClient.getCommonResponse(request);//发送短信

            b=commonResponse.getHttpResponse().isSuccess();//返回是否发送成功
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return b;
    }
}
