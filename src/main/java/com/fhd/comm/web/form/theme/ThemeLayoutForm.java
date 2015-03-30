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

import com.fhd.comm.entity.theme.LayoutType;

/**
 * @author   郝静
 * @version  
 * @since    Ver 1.1
 *
 */
public class ThemeLayoutForm extends LayoutType{
	private static final long serialVersionUID = 1L;

	private String layoutName;//布局名称
	private String layoutType;//布局类型
	private String layoutTypeDesc;//布局描述
	private String themeId;//主题信息id
	private String layoutId;//布局信息id
	
	
	

	public ThemeLayoutForm(){
		
	}
	
	public ThemeLayoutForm(LayoutType layoutType,String themeId){
		
		this.layoutType = layoutType.getLayoutType();
		this.layoutTypeDesc = layoutType.getLayoutTypeDesc();
		this.themeId = themeId;
		
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	public String getLayoutDesc() {
		return layoutTypeDesc;
	}

	public void setLayoutDesc(String layoutDesc) {
		this.layoutTypeDesc = layoutDesc;
	}

	public String getLayoutTypeDesc() {
		return layoutTypeDesc;
	}

	public void setLayoutTypeDesc(String layoutTypeDesc) {
		this.layoutTypeDesc = layoutTypeDesc;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}


	
	
}

