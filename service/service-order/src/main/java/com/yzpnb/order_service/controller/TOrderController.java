package com.yzpnb.order_service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.utils.JwtUtils;
import com.yzpnb.common_utils.Result;
import com.yzpnb.order_service.entity.TOrder;
import com.yzpnb.order_service.entity.educourse.CourseAllInfoVo;
import com.yzpnb.order_service.entity.ucenter.UcenterMember;
import com.yzpnb.order_service.feign.FeignToEduServiceClient;
import com.yzpnb.order_service.feign.FeignToUcenterClient;
import com.yzpnb.order_service.service.TOrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/order_service/t-order")
@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService tOrderService;

    @Autowired
    private FeignToEduServiceClient feignToEduServiceClient;

    @Autowired
    private FeignToUcenterClient feignToUcenterClient;

    @ApiOperation("根据课程id和用户id创建订单，返回订单id")
    @PostMapping("createOrder/{courseId}")
    public Result createOrder(@ApiParam(name = "courseId",value = "课程id")
                              @PathVariable(value = "courseId") String courseId,
                              HttpServletRequest request){
        //1、获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //2、根据课程id获取课程信息
        CourseAllInfoVo courseAllInfoVo = feignToEduServiceClient.selectCourseAllInfoVoApiById(courseId);

        //3、根据用户id获取用户信息
        UcenterMember ucenterMember = feignToUcenterClient.selectById(memberId);

        //4、随机生成订单号
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddHHmmss");

        Random random=new Random();
        String orderNo="";
        for(int i=0;i<=3;i++){
            orderNo += random.nextInt(10);
        }
        orderNo=sd.format(new Date())+orderNo;

        //4、创建订单
        TOrder order=new TOrder();
        order.setOrderNo(orderNo);//订单号
        order.setCourseId(courseId);//课程id
        order.setCourseTitle(courseAllInfoVo.getTitle());//课程标题
        order.setCourseCover(courseAllInfoVo.getCover());//课程封面
        order.setTeacherName(courseAllInfoVo.getTeacherName());//讲师姓名
        order.setTotalFee(courseAllInfoVo.getPrice());//课程价格
        order.setMemberId(memberId);//用户id
        order.setMobile(ucenterMember.getMobile());//用户电话
        order.setNickname(ucenterMember.getNickname());//用户昵称
        order.setStatus(0);//订单状态，默认未支付
        order.setPayType(1);//支付类型默认为1，微信支付

        tOrderService.save(order);
        return Result.ok().data("orderId",orderNo);
    }

    @ApiOperation("根据订单id获取订单信息")
    @GetMapping("selectOrderById/{orderId}")
    public Result selectOrderById(@PathVariable String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        TOrder order = tOrderService.getOne(wrapper);
        return Result.ok().data("item", order);
    }

    @ApiOperation("根据用户id和课程id查询订单信息，已经支付返回true，没有返回false")
    @GetMapping("isBuyCourse")
    public boolean isBuyCourse(@RequestParam(value = "memberid") String memberid,
                               @RequestParam(value = "courseid") String courseid) {
        //订单状态是1表示支付成功
        int count = tOrderService.count(new QueryWrapper<TOrder>().eq("member_id", memberid).eq("course_id", courseid).eq("status", 1));
        if(count>0) {
            return true;
        } else {
            return false;
        }
    }
}

