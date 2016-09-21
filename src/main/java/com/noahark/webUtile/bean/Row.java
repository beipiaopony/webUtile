package com.noahark.webUtile.bean;

import java.util.HashMap;
import java.util.Map;

public class Row {

	private Map<String, String> map;

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Row() {
		this.map = new HashMap<String, String>();
	}

	public void putValue(String key, String value) {
		map.put(key, value);
	}

	public String getValue(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return "";
		}
	}

	public String toString() {
		return "Row [map=" + map + "]";
	}

}
