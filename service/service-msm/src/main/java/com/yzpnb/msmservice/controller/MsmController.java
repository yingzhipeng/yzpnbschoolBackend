package com.yzpnb.msmservice.controller;

import com.yzpnb.common_utils.Result;
import com.yzpnb.msmservice.service.MsmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msmservice/")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;//用来操作redis

    @ApiOperation("根据手机号发送短信")
    @GetMapping("shortMessage/{phoneNumber}")
    public Result shortMessage(@ApiParam(name = "phoneNumber",value = "电话号")
                               @PathVariable String phoneNumber){
        /**实现验证码5分钟内有效*/
        //1、从redis中获取验证码，获取到直接返回，获取不到重新生成发送
        String s = redisTemplate.opsForValue().get(phoneNumber);
        if(!StringUtils.isEmpty(s)){
            return Result.ok();
        }
        /**redis中没有内容，表示验证码过期，重新生成*/
        //1、生成验证码
        DecimalFormat fourdf=new DecimalFormat("0000");//生成4位模板
        String code = fourdf.format(new Random().nextInt(10000));//生成4位随机数字符串

        //2、将验证码发送
        Map<String,Object> map=new HashMap<>();
        map.put("code",code);//我习惯将验证码存储到Map集合中

        Boolean flag = msmService.shortMessage(phoneNumber, map);//传入手机号和验证码

        if (flag==true){
            //发送成功，把验证码放在redis中，并设置有效时间
            //key用手机号，value是验证码,5表示，TimeUnit.MINUTES表示分钟，配合上数字5表示5分钟
            redisTemplate.opsForValue().set(phoneNumber,code,5, TimeUnit.MINUTES);
            return Result.ok();
        }else{
            return Result.error().message("发送失败");
        }
    }
}
