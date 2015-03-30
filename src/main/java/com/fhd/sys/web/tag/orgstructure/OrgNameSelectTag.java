/**
 * OrgNameTag.java
 * com.fhd.fdc.commons.web.tag.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-11-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.orgstructure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 自定义OrgNameTag下拉框标签.
 * 
 * <pre>
 * 自定义OrgNameTag下拉框标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:orgNameSelect path="id" id="" name="" value="" cssClass=""/>
 * 参数path:数据字典条目属性path.
 * 参数id:数据字典条目属性id.
 * 参数name:数据字典条目属性name.
 * 参数value:数据字典条目属性value.
 * 参数cssClass:数据字典条目属性cssClass.
 * </pre>
 * 
 * @author wudefu
 * @since Ver 1.1
 * @Date 2010-11-20 下午03:52:36
 * @see
 */
public class OrgNameSelectTag extends TagSupport {

	private static final long serialVersionUID = 6304759040279662213L;
	/**
	 * select的path属性值.
	 */
	private String path;
	/**
	 * select的id属性值.
	 */
	private String id;
	/**
	 * select的name属性值.
	 */
	private String name;
	/**
	 * select的option属性value值.
	 */
	private String value;
	/**
	 * select的class属性值.
	 */
	private String cssClass;
	
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		
		
		return super.doEndTag();
	}
	/**
	 * 封装机构名称字符串.
	 * @author 吴德福
	 * @param org
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	private String getOrgNameSelect(SysOrganization org){
		String space = "";
		if(org.getOrgLevel() == 2){
			space =  "　";
		}
		if(org.getOrgLevel() == 3){
			space =  "　　";
		}
		if(org.getOrgLevel() == 4){
			space =  "　　　";
		}
		if(org.getOrgLevel() == 5){
			space =  "　　　　";
		}
		return space+"┠"+org.getOrgname();
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getCssClass() {
		return cssClass;
	}
	
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
}

