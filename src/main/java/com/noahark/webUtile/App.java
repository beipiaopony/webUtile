package com.noahark.webUtile;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.noahark.webUtile.bean.Table;
import com.noahark.webUtile.bean.TableDao;

/**
 * Hello world!
 * 
 */
public class App {
	
	private static Logger logger = Logger.getLogger(App.class);

	//private static Logger logger = Logger.getLogger(
	public static void main(String[] args) {
		
		/*args[0] = "project1";
		args[1] = "ACCOUNT=123";
		args[2] = "XZBM=3A";*/
		
		System.out.println("start " + args[0] + " process...");
		
		Map<String, String> map = parserParameters(args);

		try {
			// Resource rs = new FileSystemResource("cfg/SpringContext.xml");
			ApplicationContext factory = new FileSystemXmlApplicationContext(
					"cfg/SpringContext.xml");
			// Parameter parameter = (Parameter) factory.getBean("parameter");
			TableDao dao = (TableDao) factory.getBean("tableDao");
			WebserviceExecutor webExecutor = (WebserviceExecutor) factory
					.getBean("webExecutor");

			
			//args[0]
			String projectName = args[0];
			//String projectName = "project1";
			Table table = webExecutor.ExectureByName(projectName, map);

			//table.setProjectName(projectName);
			if (table.getRows().size() > 0 ){
				dao.save(table);
			} else {
				System.out.println("无数据!");
			}
			
			
			System.out.println("process " + args[0] + " completed!");
		} catch (Exception e) {			
			logger.error(e);
		}

	}

	private static Map<String, String> parserParameters(String[] args) {
		Map<String, String> map = new HashMap<String, String>();

		if (args.length > 1) {
			for (int i = 1; i<args.length;i++){
				String[] tmp = args[i].split(":");
				if (tmp.length == 2) {
					map.put(tmp[0], tmp[1]);
				}
			}
		}

		//System.out.println(map);
		return map;

	}

	// private Map<String,String> buildRequestString();

}
