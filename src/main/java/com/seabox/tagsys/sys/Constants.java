package com.seabox.tagsys.sys;

/**
 * 
 * Description
 * @author shibh
 * @create date 2015年12月18日
 * @version 0.0.1
 */
public class Constants {
	/**
	 * 验证码
	 */
	public static final String RANDOM_WORD_GENERATOR_ACCEPTED_CHARS="023456789ABCDEFGHIJKLMNOPQRSTUVWXVZ";
	/**
	 * 验证码图片格式
	 */
	public static final String CAPTCHA_IMAGE_FORMAT="JPEG";
	/**
	 * 单点登录url
	 */
	public static final String AUTHLOGIN_POST_URL="/hdi-auth/v1/authentications";
	/**
	 * 单点登出url
	 */
	public static final String AUTHLOGOUT_DELETE_URL="/hdi-auth/v1/authentications";
	/**
	 * 获取用户角色url
	 */
	public static final String GETROLES_GET_URL="/hdi-auth/v1/authorizations/roles";
	/**
	 * 判断用户是否拥有权限
	 */
	public static final String HASROLES_GET_URL="/hdi-auth/v1/authorizations/roles/check";
	/**
	 * 验证凭证url
	 */
	public static final String AUTHVALIDATETOKEN_GET_URL="/hdi-auth/v1/authentications";
	/**
	 * 判断用户是否有指定权限
	 */
	public static final String HASPERMISSION_GET_URL="/hdi-auth/v1/authorizations/permissions/check";
	/**
	 * 获取用户所有权限
	 */
	public static final String GETPERMISSION_GET_URL="/hdi-auth/v1/authorizations/roles";
	/**
	 * 单点登录系统ip
	 */
	public static final String HDI_AUTH_HOST="http://120.26.192.100:20012";
	/**
	 * 舆情系统在hdi-auth系统的产品线id
	 */
	public static final String TENANT_ID="10000004";
}
