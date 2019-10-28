package com.jf.common.constant;

/**
 *
 * @ClassName: SystemConstant
 * @Description: 系统常量配置
 * @author: sunwei
 * @date: 2018年09月23日 下午6:45:57
 */
public class SystemConstant {

    /**
     * 字符串：null
     */
    public static final String NULL = "null";
    /**
     * \/.*
     */
    public static final String ANY_ASTERISK = "/.*";
    /**
     * *号
     */
    public static final String ASTERISK = "*";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * druid过滤器中所要忽略的资源路径
     */
    public static final String DRUID_EXCLUSIONS = "*.js,*.gif,*.jpg,*.png,*.bmp,*.css,*.ico,/druid/*";
    /**
     * 认证常量：Authorization
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * 认证常量：bearer
     */
    public static final String BEARER = "bearer";
    /**
     * 对外暴漏地址前缀(都需要做验签)：/outside
     */
    public static final String OUTSIDE = "/outside";
    /**
     * 对内地址前缀(不需要做验签)：/inside
     */
    public static final String INSIDE = "/inside";
    /**
     * 开放性地址前缀(不需要做验签)：/open
     */
    public static final String OPEN = "/open";

    /**
     * 过滤器中所要忽略的资源路径
     */
    public static final String EXCLUSIONS = DRUID_EXCLUSIONS+COMMA+INSIDE+ASTERISK+COMMA+OPEN+ASTERISK;


    public static final String HTTP_CONFIG_PREFIX= "httpclient.config";

}
