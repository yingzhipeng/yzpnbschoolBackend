package com.yzpnb.eduservice.feign.impl;

import com.yzpnb.eduservice.entity.ucenter_member.UcenterMember;
import com.yzpnb.eduservice.feign.FeignToUcenterClient;
import org.springframework.stereotype.Component;

@Component
public class FeignToUcenterClientImpl implements FeignToUcenterClient {
    @Override
    public UcenterMember selectById(String id) {
        return null;
    }
}
