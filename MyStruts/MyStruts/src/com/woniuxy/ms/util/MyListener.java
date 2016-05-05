package com.woniuxy.ms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class MyListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		String fileName = arg0.getServletContext().getInitParameter("config");
		StringTokenizer stk = new StringTokenizer(fileName,",");
		while(stk.hasMoreTokens()){
			Agent.list.add(stk.nextToken());
		}
		
		Agent.init();
	}

	

}
