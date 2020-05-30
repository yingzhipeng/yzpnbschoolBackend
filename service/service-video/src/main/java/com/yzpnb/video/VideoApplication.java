package com.yzpnb.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//这里不使用数据库，所以需要取消自动扫描数据源
@ComponentScan(basePackages = {"com.yzpnb"})//自动扫描,扫描的包下必须有子目录
@EnableDiscoveryClient //将此微服务注册到Nacos
public class VideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class);
    }
}
