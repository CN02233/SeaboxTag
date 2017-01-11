package com.seabox.base.listener;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextLoaderListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//Properties prop = new Properties();
		// InputStream in = Object.class.getResourceAsStream("/c.properties");
		//InputStream in = ContextLoaderListener.class.getClassLoader().getResourceAsStream("svn-ver.properties");
		ResourceBundle res = ResourceBundle.getBundle("svn-ver");

		//prop.load(in);
		//String revision = prop.getProperty("revision");
		String revision = res.getString("css.js.revision");
		logger.debug("css.js.revision:" + revision);
		arg0.getServletContext().setAttribute("revision", revision);

	}

	/*
	 * public static void main(String[] args) { Properties prop = new
	 * Properties(); InputStream in =
	 * Object.class.getResourceAsStream("/svn-ver.properties");
	 * 
	 * try { logger.debug("aaaaaaa:"); prop.load(in); logger.debug("ccccccc:" +
	 * prop.getProperty("revision")); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

}
