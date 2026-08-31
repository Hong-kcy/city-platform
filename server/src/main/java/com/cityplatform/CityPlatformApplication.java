package com.cityplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 城市商业街区虚实融合数字消费平台 - 应用入口。
 * 单体模块化工程，业务域以 Package 形式组织。
 */
@SpringBootApplication
public class CityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityPlatformApplication.class, args);
    }
}
