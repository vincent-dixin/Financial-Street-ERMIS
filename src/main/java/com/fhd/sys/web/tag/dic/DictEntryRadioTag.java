/**
 * DictTypeRadioTag.java
 * com.fhd.fdc.commons.web.tag.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-13 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.dic;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 自定义DictEntryTag单选按钮标签.
 * 
 * <pre>
 * 自定义DictEntryTag标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictEntryRadio path="id" id="" name="" value="id" cssClass=""/>
 * 参数path:数据字典条目path.
 * 参数path:数据字典条目id.
 * 参数path:数据字典条目name.
 * 参数path:数据字典条目value.
 * 参数path:数据字典条目cssClass.
 * </pre>
 * 
 * @author   wudefu
 * @since    Ver 1.1
 * @Date	 2010-8-15		下午04:52:36
 * @see 	 
 */
public class DictEntryRadioTag extends TagSupport {

	private static final long serialVersionUID = -3321182935824561622L;
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
		
		try {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			OldDictEntryBO dictEntryBean = (OldDictEntryBO) webApplicationContext.getBean("oldDictEntryBO");
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String locale = request.getHeader("accept-language");
			JspWriter out = pageContext.getOut();
			
			if("".equals(name) || null ==name){
				name = path;
			}
			if("".equals(id) || null ==id){
				id = path;
			}
			
			
			List<DictEntry> dictEntrys = dictEntryBean.queryDictEntryByLocale(name,locale);
			int i=0;
			//radio tag
			StringBuffer s = new StringBuffer();
			for(DictEntry dictEntry : dictEntrys){
				i++;
				if(name.equals(dictEntry.getDictType().getName())){
					if(value.equals(dictEntry.getId())){
						if(!"".equals(cssClass) && null != cssClass && i==1){
							s.append("<input type='radio' checked='checked' name='" + name + "' value='" + dictEntry.getId() + "' class='"+cssClass+"' />"+dictEntry.getName());
						}else{
							s.append("<input type='radio' checked='checked' name='" + name + "' value='" + dictEntry.getId() +"' />"+dictEntry.getName());
						}
					}else{
						if(!"".equals(cssClass) && null != cssClass && i==1){
							s.append("<input type='radio' name='" + name + "' value='" + dictEntry.getId() + "' class='"+cssClass+"' />"+dictEntry.getName());
						}else{
							s.append("<input type='radio' name='" + name + "' value='" + dictEntry.getId() + "' />"+dictEntry.getName());
						}
					}
				}
			}
			
			out.write(s.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
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
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

