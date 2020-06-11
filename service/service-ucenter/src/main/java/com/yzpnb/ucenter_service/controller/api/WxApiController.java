package com.yzpnb.ucenter_service.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yzpnb.utils.JwtUtils;
import com.yzpnb.service_base_handler.CustomExceptionHandler;
import com.yzpnb.ucenter_service.entity.UcenterMember;
import com.yzpnb.ucenter_service.service.UcenterMemberService;
import com.yzpnb.ucenter_service.util.ConstantYmlUtil;
import com.yzpnb.ucenter_service.util.HttpClientUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置 @RestController，如果你使用了此注解，此注解会将字符串返回，而不能重定向了，所以我们这个类不能用此注解
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    UcenterMemberService ucenterMemberService;

    @ApiOperation("获取扫描人信息，添加数据")
    @GetMapping("callback")
    public String callback(@ApiParam(name = "code",value = "code是微信官方api生成的唯一验证码（有过期时间），这个值可以通过请求固定地址" +
                                                           "获取访问凭证（access_token）和当前微信的唯一id值（openid）" +
                                                           "获取的凭证和id值可以让我们获取到微信扫码人的信息（头像昵称等）")String code,
                           @ApiParam(name = "state",value = "state是生成二维码的时候原样传递过来的") String state){//获取code和state

        /**1、通过获取的code值，请求固定地址获取访问凭证和微信id**/
        //1、向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
        "?appid=%s" +
        "&secret=%s" +
        "&code=%s" +
        "&grant_type=authorization_code";//占位形式拼接固定地址
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantYmlUtil.WX_OPEN_APP_ID,
                ConstantYmlUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);//根据http请求获取微信响应结果
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            throw new CustomExceptionHandler(20001, "获取access_token失败");
        }
        //解析json字符串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String)map.get("access_token");//获取访问凭证
        String openid = (String)map.get("openid");           //获取微信id

         /**2、根据微信id获取微信的用户信息**/
        //查询数据库当前用用户是否曾经使用过微信登录
        QueryWrapper<UcenterMember> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UcenterMember ucenterMember = ucenterMemberService.getOne(queryWrapper);
        if(ucenterMember == null){//没有值表示没用过，需要获取信息并注册
            System.out.println("新用户注册");
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
            "?access_token=%s" +
            "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new CustomExceptionHandler(20001, "获取用户信息失败");
            }
            //解析json
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname");
            String headimgurl = (String)mapUserInfo.get("headimgurl");
            //向数据库中插入一条记录
            ucenterMember = new UcenterMember();
            ucenterMember.setNickname(nickname);
            ucenterMember.setOpenid(openid);
            ucenterMember.setAvatar(headimgurl);
            ucenterMemberService.save(ucenterMember);
        }//如果有值，直接登录即可

        // 生成jwt
        String token = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());
        //存入cookie
        //CookieUtils.setCookie(request, response, "guli_jwt_token", token);
        //因为端口号不同存在蛞蝓问题，cookie不能跨域，所以这里使用url重写
        return "redirect:http://localhost:3000?token=" + token;
    }


    @ApiOperation("生成微信二维码")
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +               //这里通过%s占位，之后我们可以通过String.format统一赋值，不容易出错，下面的内容都是
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";

        // 回调地址，redirectUrl，官方规定，我们需要对这个字符串进行URLEncoder编码
        String redirectUrl = ConstantYmlUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new CustomExceptionHandler(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);
        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟
        //生成qrcodeUrl
        String qrcodeUrl = String.format(//为baseUrl中的占位符传值
                baseUrl,
                ConstantYmlUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;//重定向到地址，如果你使用了@RestController注解，此注解会将字符串返回，而不能重定向了，所以我们这个类不能用此注解
    }
}
