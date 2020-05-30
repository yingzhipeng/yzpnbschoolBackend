package com.yzpnb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//exclude表示不包含，DataSourceAutoConfiguration.class表示数据源自动配置的类，也就是不包含它，不进行数据源自动配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.yzpnb"})//自动扫描,扫描的包下必须有子目录
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
