package com.yzpnb.ucenter_service.service;

import com.yzpnb.ucenter_service.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzpnb.ucenter_service.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    /**
     * 登录，判断登录的手机号和密码是否正确，返回JWT加密后token字符串
     * @param ucenterMember
     * @return
     */
    String login(UcenterMember ucenterMember);

    /**
     * 注册，判断注册手机号是否已经存在，密码需要加密存储，需要验证码正确才能注册成功
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    /**
     * 根据年-月-日格式字符串，查询指定日期的注册人数
     * @param day
     * @return
     */
    Integer countRegisterByDay(String day);
}
