package com.noahark.webUtile.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Table {

	private String tableName;
	private int rowCount;
	private List<Row> rows;	
	private String message;
	private String creationdate;
	private String queryruntime;
	private String fetchedrows;
	private String creatorname;
	private String creationdateformated;
	private String description;
	private String universe;
	private List<String> columns;
	private String ReportDate;
	private String projectName;
	
	
	
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInsertSql(){
		StringBuilder insert = new StringBuilder();
		StringBuilder values = new StringBuilder();
		
		insert.append("INSERT INTO ").append(tableName).append("(");
		values.append(" VALUES(");
		for(String str : columns){
			insert.append(str).append(",");
			values.append("?,");
		}
		
		insert.append("imp_filename").append(") ");
		values.append("?)");
		
		insert.append(values);
		
		return insert.toString();
	}
	
	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getCreationdateformated() {
		return creationdateformated;
	}

	public void setCreationdateformated(String creationdateformated) {
		this.creationdateformated = creationdateformated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUniverse() {
		return universe;
	}

	public void setUniverse(String universe) {
		this.universe = universe;
	}

	@SuppressWarnings("rawtypes")
	public void insert(Row row){
		if (columns == null || columns.size() == 0){
			columns = new ArrayList<String>();
			Iterator iter = row.getMap().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next(); 
				String key = (String) entry.getKey();
				columns.add(key);
			}
		}
		
		rows.add(row);
		rowCount++;
		
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	public int getRowCount() {
		return rowCount;
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}

	public String getQueryruntime() {
		return queryruntime;
	}

	public void setQueryruntime(String queryruntime) {
		this.queryruntime = queryruntime;
	}

	public String getFetchedrows() {
		return fetchedrows;
	}

	public void setFetchedrows(String fetchedrows) {
		this.fetchedrows = fetchedrows;
	}

	public Table(String tableName) {
		this.tableName = tableName;
		this.rows = new ArrayList<Row>();
		this.rowCount = 0;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getReportDate() {
		return ReportDate;
	}

	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	
	
	
}
