package com.woniuxy.ms.demo.servlet;

import com.woniuxy.ms.util.ActionError;
import com.woniuxy.ms.util.ActionForm;

public class LoginForm implements ActionForm{
	
	private String name;
	private String pass;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public ActionError validate() {
		ActionError error = new ActionError();
		if(null == name || name.trim().length() <= 0)
			error.add("name is null");
		else if(null == pass || pass.trim().length() <= 0)
			error.add("pass is null");
		
		return error;
	}

}
