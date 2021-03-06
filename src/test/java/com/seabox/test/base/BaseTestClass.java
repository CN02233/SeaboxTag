package com.seabox.test.base;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={
		"classpath*:data-source-context.xml", 
		"classpath*:applicationContext.xml", 
		"classpath*:dispatcherServlet-servlet.xml" 
		}) 
@TransactionConfiguration(defaultRollback = false) 
public class BaseTestClass {
//	protected Logger logger = Logger.getLogger(this.getClass());  
	 
	 @Resource
	 protected SqlSession mybatisTemplate;
}
