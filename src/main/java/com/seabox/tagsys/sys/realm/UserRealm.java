package com.seabox.tagsys.sys.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.seabox.tagsys.sys.BaseException;
import com.seabox.tagsys.sys.service.UserService;
/**
 * 
 * Description
 * @author shibh
 * @create date 2015年12月21日
 * @version 0.0.1
 */
public class UserRealm extends AuthenticatingRealm {
	@Autowired
	private UserService userService;
	@Override
	/**
	 * 
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		UsernamePasswordToken token;
		if(arg0 instanceof UsernamePasswordToken){
			token = (UsernamePasswordToken)arg0;
		}else{
			throw new BaseException("0001","认证令牌类型错误");
		}
		Boolean loginres=userService.login(token.getUsername(),new String(token.getPassword()));
		if(!loginres){
			throw new BaseException("0002","用户密码错误");
		}
		SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(
				token.getUsername(),token.getPassword(),
				ByteSource.Util.bytes(token.getCredentials()),this.getName());
		System.out.println("userRealm name is "+this.getName());
		return sai;
	}
}
