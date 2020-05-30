package com.yzpnb.eduservice.controller;

import com.yzpnb.common_utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin//解决跨域问题
public class EduLoginController {

    //login
    @PostMapping("login")
    public Result login()
    {
        /**
         * token 登陆信息
         *
         */
        return Result.ok().data("token","admin");//之后我们都会使用框架，这里先写一个假数据admin之后再改
    }
    //info
    @GetMapping("info")
    public Result info()
    {
        /**
         * name 名称
         * avatar 头像
         * 全部用假数据，头像是网上随便找的一张图片
         */
        Map<String,Object> map=new HashMap<>();
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok().data(map);//因为是单例模式，有多个值，需要用map
    }
}
