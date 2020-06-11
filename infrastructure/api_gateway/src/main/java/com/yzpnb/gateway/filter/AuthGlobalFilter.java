package com.yzpnb.gateway.filter;

import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 全局Filter，统一处理会员登录与外部不允许访问的服务
 * </p>
 *
 * @author qy
 * @since 2019-11-21
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();//获取web体中的请求体
        String path = request.getURI().getPath();//的到请求地址路径
        //谷粒学院api接口，校验用户必须登录
//        if(antPathMatcher.match("/api/**/auth/**", path)) {//请求路径中必须有关于登陆微服务的路径
//            List<String> tokenList = request.getHeaders().get("token");
//            if(null == tokenList) {
//                ServerHttpResponse response = exchange.getResponse();
//                return out(response);
//            } else {
////                Boolean isCheck = JwtUtils.checkToken(tokenList.get(0));
////                if(!isCheck) {
//                    ServerHttpResponse response = exchange.getResponse();
//                    return out(response);//没有调用方法，报错
////                }
//            }
//        }
        //内部服务接口，不允许外部访问
        if(antPathMatcher.match("/**/inner/**", path)) {//判断如果路径中有inner，表示此地址是内部接口，不允许外部访问
            ServerHttpResponse response = exchange.getResponse();//不让其得到响应
            return out(response);//报错
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 28004);
        message.addProperty("data", "鉴权失败");
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
