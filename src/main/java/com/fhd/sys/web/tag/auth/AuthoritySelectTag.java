/**
 * AuthoritySelectTag.java
 * com.fhd.fdc.commons.web.tag.dic
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.auth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.entity.auth.SysAuthority;

/**
 * 自定义AuthoritySelectTag下拉框标签.
 * 
 * <pre>
 * 自定义AuthoritySelectTag下拉框标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:authoritySelect path="id" id="" name="" value="" cssClass=""/>
 * 参数path:权限选择框的属性path.
 * 参数id:权限选择框的属性id.
 * 参数name:权限选择框的属性name.
 * 参数value:权限选择框的属性value.
 * 参数cssClass:权限选择框的属性cssClass.
 * </pre>
 * 
 * @author wudefu
 * @since Ver 1.1
 * @Date 2010-8-30 下午04:52:36
 * @see
 */
public class AuthoritySelectTag extends TagSupport {

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
		
		try {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			SysAuthorityBO sysAuthorityBean = (SysAuthorityBO) webApplicationContext.getBean("sysAuthorityBO");
			ResourceBundleMessageSource messageSource = (ResourceBundleMessageSource) webApplicationContext.getBean("messageSource");
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String locale = request.getHeader("accept-language");
			JspWriter out = pageContext.getOut();
			
			List<SysAuthority> sysAuthoritys = sysAuthorityBean.queryAllAuthorityBySqlServer();
			
			
			//select tag
			StringBuilder s = new StringBuilder();
			if("".equals(name) || null ==name){
				name = path;
			}
			if("".equals(id) || null ==id){
				id = path;
			}
			if(!"".equals(cssClass) && null != cssClass){
				s.append("<select name='"+name+"' id='"+id+"' class='"+cssClass+"'>");
			}else{
				s.append("<select name='"+name+"' "+"id='"+id+"'>");
			}
			s.append("<option value=''>"+messageSource.getMessage("fhd.common.pleaseSelect", null, new Locale(StringUtils.lowerCase(StringUtils.split(locale, ",")[0])))+"</option>");
			
			for(SysAuthority sysAuthority : sysAuthoritys){
				if(value.equals(sysAuthority.getId())){
					s.append("<option value='"+sysAuthority.getId()+"' selected='selected'>"+sysAuthority.getAuthorityName()+"</option>");
				}else{
					s.append("<option value='"+sysAuthority.getId()+"'>"+sysAuthority.getAuthorityName()+"</option>");
				}
			}
			s.append("</select>");
			
			out.write(s.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	/**
	 * @return String
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return String
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return String
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return String
	 */
	public String getCssClass() {
		return cssClass;
	}
	/**
	 * @param cssClass
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
}

