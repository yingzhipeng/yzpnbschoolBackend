package com.yzpnb.ucenter_service.controller;


import com.yzpnb.utils.JwtUtils;
import com.yzpnb.common_utils.Result;
import com.yzpnb.ucenter_service.entity.UcenterMember;
import com.yzpnb.ucenter_service.entity.vo.RegisterVo;
import com.yzpnb.ucenter_service.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/ucenter_service/ucenter-member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation("登录，判断登录的手机号和密码是否正确，返回JWT加密后token字符串")
    @PostMapping("login")
    public Result login(@RequestBody UcenterMember ucenterMember){

        String token=ucenterMemberService.login(ucenterMember);//返回由JWT生成的token字符串
        return Result.ok().data("token",token);
    }

    @ApiOperation("注册，判断注册手机号是否已经存在，密码需要加密存储，需要验证码正确才能注册成功")
    @PostMapping("register")
    public Result register(@ApiParam(name = "registerVo",value = "注册对象")
                           @RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return Result.ok();
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("getUserInfo")
    public Result getUserInfo(HttpServletRequest request){//使用原生HttpServlet请求对象，获取请求体

        //通过JWT工具类方法，根据request请求头，返回用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //根据id查询数据
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return Result.ok().data("userInfo",ucenterMember);
    }

    @ApiOperation("根据id获取用户信息")
    @PostMapping("selectById")
    public UcenterMember selectById(@ApiParam(name = "id",value = "用户id") @RequestParam("id") String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);

        return ucenterMember;
    }

    @ApiOperation("统计某一天的注册人数")
    @GetMapping(value = "countregister")
    public Integer registerCount(@ApiParam(name = "day",value = "天数")
                                @RequestParam(value = "day") String day){
        Integer count = ucenterMemberService.countRegisterByDay(day);
        return count;
    }
}

