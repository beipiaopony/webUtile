package com.noahark.webUtile.bean;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.noahark.webUtile.xml.XmlUtile;

public class WebProject {

	private String requestFile;
	private String wsdlLocation;
	private String dataPath;
	private String tableName;

	// 设置请求和传输超时时间
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000)
			.setConnectTimeout(300000).build();

	private static Logger logger = Logger.getLogger(WebProject.class);

	public String getFullFileName(String fileName) {
		return dataPath + "/" + fileName;
	}

	public Table parserXmlToTable(String fileName, Map<String, String> patameterMap) {

		String fileFullName = invoke(fileName, patameterMap);

		logger.debug(requestFile);
		logger.debug(wsdlLocation);

		Table table = XmlUtile.readXml(fileFullName);
		table.setProjectName(fileFullName);

		table.setTableName(tableName);

		return table;
	}

	public String invoke(String fileName, Map<String, String> patameterMap) {

		String fullName = getFullFileName(fileName);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;

		HttpPost post = new HttpPost(wsdlLocation);
		post.setConfig(requestConfig);
		String soapRequestData = buildRequestData(patameterMap);

		try {
			HttpEntity re = new StringEntity(soapRequestData, "UTF-8");

			post.setHeader("Content-Type", "application/soap+xml; charset=UTF-8");
			post.setEntity(re);

			response = httpclient.execute(post);

			// response.getStatusLine();

			HttpEntity entity = response.getEntity();

			System.out.println("请求服务的Soap文本：" + EntityUtils.toString(post.getEntity(), "UTF-8"));
			System.out.println("请求服务结果状态:" + response.getStatusLine());

			System.out.println("请求服务返回XML文本：" + EntityUtils.toString(entity, "UTF-8"));

			// String d = XmlUtile.readStringData(soapData);

			XmlUtile.writeXmlToFile("d", fullName);

			EntityUtils.consume(entity);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return fullName;

	}

	private String buildRequestData(Map<String, String> parameterMap) {
		return XmlUtile.getParameterFromXml(requestFile, parameterMap);
	}

	public String getRequestFile() {
		return requestFile;
	}

	public void setRequestFile(String requestFile) {
		this.requestFile = requestFile;
	}

	public String getWsdlLocation() {
		return wsdlLocation;
	}

	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
