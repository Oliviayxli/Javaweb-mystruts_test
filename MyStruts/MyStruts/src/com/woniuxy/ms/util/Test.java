package com.woniuxy.ms.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.woniuxy.ms.demo.servlet.LoginForm;

public class Test {
	
	public void test(){
		try {
			Class clzz = Class.forName("com.woniuxy.ms.demo.servlet.LoginForm");
			Object obj = clzz.newInstance();
			
			Field[] fis = clzz.getDeclaredFields();
			for(Field f : fis){
				String fieldName = f.getName();
				System.out.println(f.getType() + ":" + fieldName);
				String fs = fieldName.substring(0,1).toUpperCase();
				String fe = fieldName.substring(1);
				String methodName = "set"+fs+fe;
				System.out.println(methodName);
				
				Method m = clzz.getMethod(methodName, new Class[]{f.getType()});
				m.invoke(obj, new Object[]{"123"});
			}
			
			LoginForm form = (LoginForm)obj;
			System.out.println(form.getName());
			System.out.println(form.getPass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
