package com.noahark.webUtile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.noahark.webUtile.bean.Table;
import com.noahark.webUtile.bean.WebProject;

public class WebserviceExecutor {

	private static Logger logger = Logger.getLogger(WebserviceExecutor.class);
	private Map<String,WebProject> projects;

	public Map<String, WebProject> getProjects() {
		return projects;
	}

	public void setProjects(Map<String, WebProject> projects) {
		this.projects = projects;
	}


	public Table ExectureByName(String projectName,Map<String,String> map){
		
				
		Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHms");
        
        String f = "";
        if (map.containsKey("functionName")){
        	f = map.get("functionName");
        } else {
        	f = projectName;
        }
        
        String fileName = f + "_" + format.format(c1.getTime()) +".xml" ;
		
        logger.debug(fileName);
        //logger.debug(map);
		return projects.get(projectName).parserXmlToTable(fileName, map);
		
	}
		
	
	
}
