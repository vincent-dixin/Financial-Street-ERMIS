/**
 * DictTypeCheckboxTag.java
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
 * 自定义DictEntryCheckboxTag复选框标签.
 * 
 * <pre>
 * 自定义DictEntryCheckboxTag标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictEntryCheckbox path="id" id="" name="" value="id" cssClass=""/>
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
public class DictEntryCheckboxTag extends TagSupport {

	private static final long serialVersionUID = -1587447078832452034L;

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
			
			if(null==name || "".equals(name)){
				name = path;
			}
			
			if(null==id || "".equals(id)){
				id = path;
			}
			
			List<DictEntry> dictEntrys = dictEntryBean.queryDictEntryByLocale(name,locale);
			
			//checkbox tag
			StringBuffer s = new StringBuffer();
			//计数器
			int count = 0;
			s.append("<table border='0' width='100%' height='100%' cellpadding='0' cellspacing='0'>");
			for(DictEntry dictEntry : dictEntrys){
				if(count%5 == 0){
					s.append("<tr>");
				}
				if(name.equals(dictEntry.getDictType().getName())){
					//是否有值需要默认选中
					if(null != value && !"".equals(value)){
						s.append("<td width='20%'>");
						
						s.append("<input type='checkbox' name='" + name +"' id='"+id+"'");
						
						s.append(" value='" + dictEntry.getId()+"'");
						String[] valueArray = value.split(",");
						for(String v : valueArray){
							if(v.equals(dictEntry.getId())){
								s.append(" checked='checked'");
							}
							
							if(null != cssClass && !"".equals(cssClass)){
								s.append(" class='"+cssClass+"'");
							}
						}
						s.append("/>"+dictEntry.getName());
						
						s.append("</td>");
					}else{
						s.append("<td width='20%'>");
						
						if(null != cssClass && !"".equals(cssClass)){
							s.append("<input type='checkbox' name='" + name + "' value='" + dictEntry.getId() + "' class='"+cssClass+"' />"+dictEntry.getName());
						}else{
							s.append("<input type='checkbox' name='" + name + "' value='" + dictEntry.getId() + "' />"+dictEntry.getName());
						}
						
						s.append("</td>");
					}
				}
				
				count++;
				if(count%5 == 0){
					s.append("</tr>");
				}
			}
			s.append("</table>");
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
}

