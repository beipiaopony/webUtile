package com.noahark.webUtile.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.noahark.webUtile.bean.Row;
import com.noahark.webUtile.bean.Table;
import com.noahark.webUtile.exception.BaseException;

public class XmlUtile {

	private static Logger logger = Logger.getLogger(XmlUtile.class);

	public static void writeXmlToFile(String d, String fileName) {
		try {
			Document doc = DocumentHelper.parseText(d);
			doc.setXMLEncoding("UTF-8");

			FileOutputStream fos = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			OutputFormat of = new OutputFormat();
			of.setEncoding("UTF-8");
			of.setIndent(true);
			of.setIndent("    ");
			of.setNewlines(true);
			XMLWriter writer = new XMLWriter(osw, of);
			writer.write(doc);
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeXmlToFile(InputStream in, String fileName) {

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			throw new BaseException("无法打开文件：" + fileName, e);
		}

		int bytesWritten = 0;
		int byteCount = 0;
		byte[] bytes = new byte[1024 * 1024];

		try {
			while ((byteCount = in.read(bytes)) != -1) {

				outputStream.write(bytes, bytesWritten, byteCount);

			}

			outputStream.flush();

		} catch (IOException e) {
			throw new BaseException("无法写入文件：" + fileName, e);
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	public static String getParameterFromXml(String fileName,
			Map<String, String> map) {
		File inputXML = new File(fileName);
		// 使用 SAXReader 解析 XML 文档 orderList.xml
		SAXReader saxReader = new SAXReader();
		Document document = null;

		try {
			document = saxReader.read(inputXML);
		} catch (DocumentException e) {
			throw new BaseException("无法读取xml文件", e);
		}

		//
		Element root = document.getRootElement();// 根节点
		Element body = root.element("Body");//
		Element method = body.element("req");//

		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();

			logger.debug(key + ":" + val);
			Element temp = method.element(key);
			if (temp == null) {
				temp = method.addElement(key);
			}

			temp.setText(val);
			// System.out.println(temp.asXML());
		}

		logger.debug("request data:\n" + document.getRootElement().asXML());
		return document.getRootElement().asXML();

	}

	public static Table readXml(String fileName) {
		File inputXml = new File(fileName);
		return readXml(inputXml);
	}

	public static Table readXml(File inputXml) {

		SAXReader saxReader = new SAXReader();
		Document document = null;

		try {
			document = saxReader.read(inputXml);

		} catch (DocumentException e) {
			throw new BaseException("无法读取xml文件", e);
		}

		return parserXml(document);

	}

	
	public static Table readXmlString(String xml) {
		try {
			Document doc = DocumentHelper.parseText(xml);

			List<Element> nodes = doc.selectNodes("//IT_VALUE/item");

			for (Element row : nodes) {

				for (Iterator k = row.elementIterator(); k.hasNext();) {
					Element column = (Element) k.next();
					System.out.println(column.getName() + ":"
							+ column.getText());
					// r.putValue(column.getName(), column.getText());
					// logger.debug(column.getName() + ":" +
					// column.getText());
				}
			}

			System.out.println(nodes.size());

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String readStringData(InputStream in) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(in);

		} catch (DocumentException e) {
			throw new BaseException("无法读取xml输入流", e);
		}

		HashMap xmlMap = new HashMap();
		xmlMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		xmlMap.put("urn", "urn:digichannel.fanuc.com");

		XPath x = document.createXPath("//urn:execReturn");

		x.setNamespaceURIs(xmlMap);

		@SuppressWarnings("rawtypes")
		Node node = x.selectSingleNode(document);

		// List tmp =
		// document.selectNodes("/*[local-name()='Envelope' and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']");
		// System.out.println();

		//readXmlString(node.getText());

		return node.getText();

	}

	public static Table readXml(InputStream in) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(in);

		} catch (DocumentException e) {
			throw new BaseException("无法读取xml输入流", e);
		}

		return parserXml(document);
	}

	
	
	private static Table parserXml(Document document){
		Table table = new Table("mytest");
		
		try {
			//Document doc = DocumentHelper.parseText(xml);

			List<Element> nodes = document.selectNodes("//IT_VALUE/item");

			for (Element row : nodes) {
				Row r = new Row();
				
				for (Iterator k = row.elementIterator(); k.hasNext();) {
					Element column = (Element) k.next();
					r.putValue(column.getName(), column.getText());
					
					/*System.out.println(column.getName() + ":"
							+ column.getText());*/
					// r.putValue(column.getName(), column.getText());
					// logger.debug(column.getName() + ":" +
					// column.getText());
				}
				
				table.insert(r);
			}

			System.out.println(nodes.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
	/*
	@SuppressWarnings("rawtypes")
	private static Table parserXml(Document document) {

		Table table = new Table("mytest");

		Class userCla = table.getClass();

		// root
		Element root = document.getRootElement();

		// table message..
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element parent = (Element) i.next();
			// 是否有子项：table
			if (parent.elementIterator().hasNext()) {
				// table
				for (Iterator j = parent.elementIterator(); j.hasNext();) {

					// row
					Element row = (Element) j.next();
					Row r = new Row();

					for (Iterator k = row.elementIterator(); k.hasNext();) {
						Element column = (Element) k.next();
						// System.out.println(column.getNamespace());
						r.putValue(column.getName(), column.getText());
						// logger.debug(column.getName() + ":" +
						// column.getText());
					}

					// logger.debug(r);

					table.insert(r);

				}
			} else {
				logger.debug(parent.getName() + ":" + parent.getText());
				String vname = parent.getName();

				try {
					Field f = userCla.getDeclaredField(vname);
					f.setAccessible(true);
					f.set(table, parent.getText());
				} catch (SecurityException e) {
					throw new BaseException("没有权限访问类的属性", e);
				} catch (NoSuchFieldException e) {
					throw new BaseException("类的属性不存在", e);
				} catch (IllegalArgumentException e) {
					throw new BaseException("参数错误", e);
				} catch (IllegalAccessException e) {
					throw new BaseException("访问类属性异常", e);
				}

			}

		}

		return table;

	}
    */
	
	public static void main(String[] arg0) {
		String file1 = "data/project1_150928223852.xml";
		String file2 = "cfg/test.xml";
		InputStream inputStream = null;

		File inputXML = new File(file1);
		try {
			inputStream = new FileInputStream(inputXML);

			System.out.println(readStringData(inputStream));
			// writeXmlToFile(inputStream, file2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
