/*
 * Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司.
 */
package com.meiya.springboot.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 字符串工具类
 * @author linwb
 * @since 2019/12/19
 */
public class StringUtil {

    /**
     * 判断字符串是否异常
     * @param val       需要判断的值
     * @param errorInfo 异常提示信息
     */
    public static void isBlank(String val, String errorInfo) throws IllegalArgumentException {
        if(StringUtils.isNotBlank(val)){
            return ;
        }
        throw new IllegalArgumentException(errorInfo==null ? "参数异常" : errorInfo);
    }

   /**
    * 获取md5处理后的base64编码ID   将32位ID转化成22位ID
    * @return java.lang.String
    * @author linwb
    * @since 2019/12/19
    **/
    public static String getBase64Guid() {
        return Base64.encodeBase64URLSafeString(DigestUtils.md5(getFormatGuid()));
    }


    /**
     * 获取md5处理后的base64编码ID   将32位ID转化成22位ID
     * @return java.lang.String
     * @author linwb
     * @since 2019/12/19
     **/
    public static String getBase64Guid(String str) {
        return Base64.encodeBase64URLSafeString(DigestUtils.md5(str));
    }


    /**
     * 获取随机GUID
     * @return java.lang.String
     * @author linwb
     * @since 2019/12/19
     **/
    public static String getFormatGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }



}
