package com.fhd.imports.entity;

import java.util.Map;
/**
 * xml中table对应的实体
 * @author 邓广义
 * @date 2013-6-20
 * @since  fhd　Ver 1.1
 */
public class Table {
	private String label;
	private String name;
	private String sheet;
	private int columnStart;
	private int columnEnd;
	private int rowStart;
	private Map<String,Column> columns;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public int getColumnStart() {
		return columnStart;
	}
	public void setColumnStart(int columnStart) {
		this.columnStart = columnStart;
	}
	public int getColumnEnd() {
		return columnEnd;
	}
	public void setColumnEnd(int columnEnd) {
		this.columnEnd = columnEnd;
	}
	public int getRowStart() {
		return rowStart;
	}
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}
	public Map<String, Column> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, Column> columns) {
		this.columns = columns;
	}
	
}
