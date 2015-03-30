/**
 * DictTypeTag.java
 * com.fhd.fdc.commons.web.tag.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-11 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.dic;

import java.io.IOException;
import java.util.ArrayList;
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
 * 自定义DictEntryNameTag下拉框标签.
 * 
 * <pre>
 * 自定义DictEntryNameTag下拉框标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictEntryNameSelect path="id" id="" name="" value="" cssClass=""/>
 * 参数path:数据字典条目属性path.
 * 参数id:数据字典条目属性id.
 * 参数name:数据字典条目属性name.
 * 参数value:数据字典条目属性value.
 * 参数cssClass:数据字典条目属性cssClass.
 * </pre>
 * 
 * @author wudefu
 * @since Ver 1.1
 * @Date 2010-8-15 下午04:52:36
 * @see
 */
public class DictEntryNameSelectTag extends TagSupport {

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
		try {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			OldDictEntryBO dictEntryBean = (OldDictEntryBO) webApplicationContext.getBean("oldDictEntryBO");
			@SuppressWarnings("unused")
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			JspWriter out = pageContext.getOut();
			List<Object[]> resultList = dictEntryBean.queryAllDictEntryBySqlServer();
			
			List<DictEntry> dictEntryList = new ArrayList<DictEntry>();
			for(int i=0;i<resultList.size();i++){
				Object[] s = resultList.get(i);
				String id = (String)s[0];
				String dictEntryName = (String)s[1];
				DictEntry dictEntry = dictEntryBean.queryDictEntryById(id);
					dictEntry.setName(dictEntryName);
					dictEntryList.add(dictEntry);
			}
			
			//select tag
			StringBuffer s = new StringBuffer();
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
			//ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n",new Locale(StringUtils.lowerCase(StringUtils.split(request.getHeader("accept-language"), ",")[0])));
			//s.append("<option value=''>"+RESOURCE_BUNDLE.getString("pleaseSelect")+"</option>");
			s.append("<option value=''>请选择</option>");
			for(DictEntry dictEntry : dictEntryList){
				if(value.equals(dictEntry.getId())){
					s.append("<option value='"+dictEntry.getId()+"' selected='selected'>"+dictEntry.getName()+"</option>");
				}else{
					s.append("<option value='"+dictEntry.getId()+"'>"+dictEntry.getName()+"</option>");
				}
			}
			s.append("</select>");
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

