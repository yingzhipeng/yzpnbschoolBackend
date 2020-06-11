package com.yzpnb.order_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.yzpnb"})//这样它会将所有com.yzpnb包下内容扫描，而不只是扫描子文件或同级文件
@EnableDiscoveryClient //nacos注册
@MapperScan("com.yzpnb.order_service.mapper")
@EnableFeignClients //开启Feign服务熔断
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
