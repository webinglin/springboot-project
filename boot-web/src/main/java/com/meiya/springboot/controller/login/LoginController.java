/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */

package com.meiya.springboot.controller.login;

import com.meiya.springboot.service.user.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 登录控制类
 * @author linwb
 * @since 2019-09-10
 */
@Controller
public class LoginController extends BaseLoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService userService;

    @GetMapping("/")
    public String loginPage(){
        try{
            request.setAttribute("name","项目根路径");
            request.setAttribute("age",30);


        } catch (Exception e){
            logger.error("测试出错", e);
        }

        return getLoginPage();
    }


    /**
     * 登陆页面
     */
    @GetMapping("/login")
    public String login(){
        try {
            request.setAttribute("name", "linwb");
            request.setAttribute("age", 30);

        } catch (Exception e){
            logger.error("跳转登录页面出错", e);
            // TODO 处理异常
        }

        return getLoginPage();
    }


    @PostMapping("/login")
    public String login(String userName, String password, String checkCode){

        try {


        } catch (Exception e){
            logger.error("登录出错", e);
            // TODO 处理异常
        }

        // 返回登录成功的页面
        return getIndexPage();
    }





}
