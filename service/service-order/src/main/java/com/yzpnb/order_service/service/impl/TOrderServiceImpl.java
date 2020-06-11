package com.yzpnb.order_service.service.impl;

import com.yzpnb.order_service.entity.TOrder;
import com.yzpnb.order_service.mapper.TOrderMapper;
import com.yzpnb.order_service.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

}
