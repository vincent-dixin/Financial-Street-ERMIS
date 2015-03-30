/**
 * OrgSelector.java
 * com.fhd.fdc.commons.web.tag.sys.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-4-6 		Sword
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.tag.orgstructure;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Description: 选择预警的组件
 * @author Sword
 * @version fhd Ver 1.1
 * @since Ver 1.1
 * @Date 2011-4-6
 * @see
 */
public class WarningSelectorTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	// 是否多选
	private boolean multiple;

	// 窗口标题
	private String title;

	// 窗口宽度
	private int width;

	// 窗口高度
	private int height;

	// 已经选择的员工列表
	private String choosedWarnIds;
	//数据类型
	private String dataType;

	// 属性名称 用户获取选择值
	private String attributeName;

	// 弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssStyle;

	// 是否有复选框
	private boolean checkNode;

	// 是否只是叶子节点加复选框
	private boolean onlyLeafCheckable;

	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel;
	// 是否只选择风险大类
	private String targetId;

	public WarningSelectorTag() {
		super();

		multiple = false;

		title = "预警方案选择";

		width =1000;

		height = 600;

		choosedWarnIds = "";

		attributeName = "";

		cssStyle = "";

		checkNode = false;

		onlyLeafCheckable = true;

		checkModel = "multiple";
	}

	@Override
	public int doEndTag() throws JspException {
		
		return super.doEndTag();
	}


	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public boolean isCheckNode() {
		return checkNode;
	}

	public void setCheckNode(boolean checkNode) {
		this.checkNode = checkNode;
	}

	public boolean isOnlyLeafCheckable() {
		return onlyLeafCheckable;
	}

	public void setOnlyLeafCheckable(boolean onlyLeafCheckable) {
		this.onlyLeafCheckable = onlyLeafCheckable;
	}

	public String getCheckModel() {
		return checkModel;
	}

	public void setCheckModel(String checkModel) {
		this.checkModel = checkModel;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getChoosedWarnIds() {
		return choosedWarnIds;
	}

	public void setChoosedWarnIds(String choosedWarnIds) {
		this.choosedWarnIds = choosedWarnIds;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}


	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
