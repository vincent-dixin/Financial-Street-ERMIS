package com.fhd.imports.entity;
/**
 * xml文件中column对应的实体
 * @author 邓广义
 * @date 2013-6-20
 * @since  fhd　Ver 1.1
 */
public class Column {
	private String label;
	private String name;
	private String type;
	private int columnIndex;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	
}
