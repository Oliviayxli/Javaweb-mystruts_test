package com.woniuxy.ms.demo.servlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woniuxy.ms.util.Action;
import com.woniuxy.ms.util.ActionForm;

public class LoginAction implements Action{

	public String execute(Map map, ActionForm form) {
		LoginForm lf = (LoginForm)form;
		
		System.out.println(lf.getName());
		System.out.println(lf.getPass());
		
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		request.setAttribute("test", "test");
		return "list";
	}

}
