package com.yzpnb.ucenter_service.mapper;

import com.yzpnb.ucenter_service.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer countRegisterByDay(String day);
}
