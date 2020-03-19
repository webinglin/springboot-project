package com.meiya.springboot.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 插件的配置文件
 * @author linwb
 * @since 2019/12/12
 */
@Configuration
@MapperScan("com.meiya.springboot.mapper")
public class MybatisConfig {

    /**
     * 分页插件
     */
    @Bean(name = "paginationInterceptor")
    public PaginationInterceptor getPaginationInterceptor(){
        return new PaginationInterceptor();
    }

}
