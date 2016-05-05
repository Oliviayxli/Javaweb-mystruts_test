package com.woniuxy.ms.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.woniuxy.ms.exception.URLPatternNotFoundException;

public class Agent {
	
	public static List<String> list = new ArrayList<String>(0);
	public static List<Document> docs = new ArrayList<Document>(0);

	public Map getActionProperty(String uri) throws URLPatternNotFoundException {
		Map map = new HashMap();
		for(Document doc : docs){
			Element element = (Element) doc.selectSingleNode("//myStruts/actions/action[@name='"+uri+"']");
		
			if(null != element){
				map.put("class", element.attributeValue("class"));
				map.put("input", element.attributeValue("input"));
				map.put("form", element.attributeValue("form"));
				List<Element> elements = element.selectNodes("result");
				
				if(null != elements){
					for(Element et : elements){
						map.put(et.attributeValue("name"), et.getText());
					}
				}
				
				map.put("doc", doc);
				
				break;
			}
		}
		
		if(null == map || map.size() <= 0)
			throw new URLPatternNotFoundException();
		
		return map;
	}

	public static void init() {
		for(String s : list){
			InputStream is = Agent.class.getResourceAsStream("/" + s);
			SAXReader reader = new SAXReader();
			Document xmlDoc = null;
			try {
				xmlDoc = reader.read(is);
				docs.add(xmlDoc);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	public Map getFormProperty(String name, Document doc) {
		Map map = new HashMap();
		Element element = (Element) doc.selectSingleNode("//myStruts/form-bean/form[@name='"+name+"']");
		if(null != element){
			map.put("class", element.attributeValue("class"));
			List<Element> list = element.selectNodes("property");
			if(null != list){
				for(Element el : list)
					map.put(el.attributeValue("name"), el.attributeValue("type"));
			}
		}
		
		return map;
	}

}
