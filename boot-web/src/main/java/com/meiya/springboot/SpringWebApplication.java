/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */

package com.meiya.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * WEB应用启动类
 * @author linwb
 * @since 2019-09-10
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.meiya.springboot.*"})
public class SpringWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebApplication.class, args);
    }


}
