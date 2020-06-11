package com.yzpnb.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
/**
 * @author
 */
public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 24;                      //设置token过期时间
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";   //设置token密钥，我瞎写的，每个公司都有按自己规则生成的密钥

    //生成token字符串
    public static String getJwtToken(String id, String nickname){

        String JwtToken = Jwts.builder()                                           //构建jwt字符串
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")                             //设置jwt头信息

                .setSubject("guli-user")                                           //分类，名字随便起的，不同的分类可以设置不同的过期
                .setIssuedAt(new Date())                                           //设置过期时间的计时起始值为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))      //设置过期时间为当前时间+EXPIRE我们设定的过期时间

                .claim("id", id)                                                //token主体，这里放你需要的信息，我们实现登陆，就放用户登陆信息
                .claim("nickname", nickname)                                    //需要多少主体信息，就设置多少个claim属性

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)                    //签名哈希，根据指定规则和我们的密钥设定签名
                .compact();

        return JwtToken;
    }
        /**
         * 判断token是否存在与有效（方法一）
         * @param jwtToken token字符串
         * @return
         */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
        /**
         * 判断token是否存在与有效（方式二）
         * @param request 请求体
         * @return
         */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
        /**
         * 根据token获取会员id
         * @param request
         * @return
         */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");                                           //根据请求体获取token字符串
        if(StringUtils.isEmpty(jwtToken)) return "";                                               //如果token为空，返回空串
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);  //解析token字符串
        Claims claims = claimsJws.getBody();                                                       //获取主体内容
        return (String)claims.get("id");                                                           //获取主体值，这里只获取了id值
    }
}
