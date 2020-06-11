package com.yzpnb.msmservice.service;

import java.util.Map;

public interface MsmService {
    /**
     * 根据手机号发送短信
     * @param phoneNumber
     * @param map
     * @return
     */
    Boolean shortMessage(String phoneNumber, Map<String, Object> map);
}
