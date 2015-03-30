/**
 * EmployeeForm.java
 * com.fhd.sys.web.form.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-21 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.web.form.theme;

import com.fhd.comm.entity.theme.LayoutDetailedSet;

/**
 * @author   郝静
 * @version  
 * @since    Ver 1.1
 *
 */
public class ThemeLayoutDetailedForm extends LayoutDetailedSet{
	private static final long serialVersionUID = 1L;

//	private String chartType;//图形类型
//	private String dataSource;//数据源
//	private String chartEffect;//图表效果
//	private String chartForm;//图表形式
//	private String isShowUnit;//是否显示单位
//	private String isMainSource;//是否主源
//	private String url;//url
	private String layoutInfoId;//是否显示单位
	private String layoutType;
	private String position;//位置
	
	
	

	public String getLayoutInfoId() {
		return layoutInfoId;
	}

	public void setLayoutInfoId(String layoutInfoId) {
		this.layoutInfoId = layoutInfoId;
	}

	public ThemeLayoutDetailedForm(){
		
	}

	public ThemeLayoutDetailedForm(String layoutInfoId){
		
		this.layoutInfoId = layoutInfoId;
		
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	
}

