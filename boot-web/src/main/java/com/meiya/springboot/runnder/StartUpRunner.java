/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */

package com.meiya.springboot.runnder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * StartUpRunner用来处理一些初始化的逻辑，包括在web中相关的依赖
 * @author linwb
 * @since 2019-09-10
 */
@Component
@Order(1)
public class StartUpRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartUpRunner.class);

    /**
     * Callback used to run the bean.
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {



        logger.info("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        logger.info("+-+-+-+-+-+-SPRING-BOOT-WEB APPLICATION STARTUP SUCCESSFUL.+-+-+-+-+-+-");
        logger.info("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
    }

}
