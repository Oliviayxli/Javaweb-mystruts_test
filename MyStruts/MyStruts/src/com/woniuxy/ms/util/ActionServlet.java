package com.woniuxy.ms.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;

import com.woniuxy.ms.exception.URLPatternNotFoundException;

public class ActionServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		int start = uri.lastIndexOf("/") + 1;
		int end = uri.indexOf(".do");
		
		uri = uri.substring(start, end);
		
		Agent agent = new Agent();
		
		Map actionProperty = new HashMap(0);
		Map formProperty = new HashMap(0);
		
		
		try {
			actionProperty = agent.getActionProperty(uri);
		} catch (URLPatternNotFoundException e) {
			response.sendError(500,"提交的路径没有映射");
			return;
		}
		
		formProperty = agent.getFormProperty(actionProperty.get("form").toString(),(Document)actionProperty.get("doc"));

		createFormBean(formProperty,request,response,actionProperty);
		
	}

	private void createFormBean(Map formProperty, HttpServletRequest request, HttpServletResponse response, Map actionProperty) throws ServletException, IOException {
		try {
			Class clzz = Class.forName(formProperty.get("class").toString());
			Object obj = clzz.newInstance();//??
			if(obj instanceof ActionForm){
				ActionForm af = (ActionForm)obj;
				formProperty.remove("class");
				Set<String> props = formProperty.keySet();
				
				for(String proName : props){
					String proValue = request.getParameter(proName);
					String method = "set"+proName.substring(0,1).toUpperCase() + proName.substring(1);
					Method m = clzz.getMethod(method, new Class[]{Class.forName(formProperty.get(proName).toString())});
					m.invoke(obj, new Object[]{proValue});
				}
				
 				ActionError error = af.validate();
				if(error != null && null != error.getValue() && error.getValue().trim().length() > 0){
					request.setAttribute("error", error.getValue());
					request.getRequestDispatcher(actionProperty.get("input").toString()).forward(request, response);
					return;
				}
				
				createAction(request,response,actionProperty,af);
			}else{
				response.sendError(500, "FormBean的类型不正确");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private void createAction(HttpServletRequest request,
			HttpServletResponse response, Map actionProperty, ActionForm form) {
		
		try {
			Class clzz = Class.forName(actionProperty.get("class").toString());
			Object obj = clzz.newInstance();
			
			if(obj instanceof Action){
				Action action = (Action)obj;
				Map context = new HashMap();
				context.put("request", request);
				context.put("response", response);
				context.put("session", request.getSession(true));
				String result = action.execute(context, form);
				String page = actionProperty.get(result).toString();
				
				if(null == page || page.trim().length() <= 0){
					response.sendError(500,"跳转资源没有被映射");
					return;
				}
				
				request.getRequestDispatcher(page).forward(request, response);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
