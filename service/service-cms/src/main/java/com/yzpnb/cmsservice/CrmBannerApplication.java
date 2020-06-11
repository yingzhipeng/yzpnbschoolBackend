package com.yzpnb.cmsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.yzpnb"})//这样它会将所有com.yzpnb包下内容扫描，而不只是扫描子文件或同级文件
@MapperScan(value = "com.yzpnb.cmsservice.mapper")//自动扫描mapper指定mapper映射的扫描位置
@EnableDiscoveryClient //nacos注册
public class CrmBannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmBannerApplication.class);
    }
}
