/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */

package com.meiya.springboot.controller.login;

import com.meiya.springboot.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础的登录控制类
 * @author linwb
 * @since 2019-09-10
 */
public class BaseLoginController extends BaseController {

    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpServletRequest request;


    /**
     * 返回登录页面的字符串
     */
    protected String getLoginPage(){
        return "login";
    }

    /**
     * 返回首页面字符串
     */
    protected String getIndexPage(){
        return "index";
    }

}
