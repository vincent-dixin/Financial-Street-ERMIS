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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 自定义DictEntryTag下拉框标签.
 * 
 * <pre>
 * 自定义DictEntryTag下拉框标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictEntrySelect path="id" id="" name="" value="" cssClass="" style=""/>
 * 参数path:数据字典条目属性path.
 * 参数id:数据字典条目属性id.
 * 参数name:数据字典条目属性name.
 * 参数value:数据字典条目属性value.
 * 参数cssClass:数据字典条目属性cssClass.
 * 参数style:数据字典条目属性style.
 * 参数disabled:数据字典条目属性disabled.
 * </pre>
 * 
 * @author   wudefu
 * @since    Ver 1.1
 * @Date	 2010-8-15		下午04:52:36
 * @see 	 
 */
public class DictEntrySelectTag extends TagSupport {

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
	/**
	 * select的style属性值.
	 */
	private String style;
	/**
	 * select的disabled属性值.
	 */
	private String disabled;
	/**
	 * 数据字典名称的属性值.
	 */
	private String dictName;
	/**
	 * 是否添加onchange响应方法 onchangeFun 为方法名
	 */
	private String onchangeFun;
	/**
	 * 过滤条件
	 */
	private String filter;
	
	public DictEntrySelectTag(){
		super();
		path = "";
		id = "";
		name = "";
	}
	
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
			
			List<DictEntry> dictEntrys = dictEntryBean.queryDictEntryByLocale(dictName,locale);
			
			//select tag
			StringBuffer s = new StringBuffer();
			s.append("<select name='"+name+"' "+"id='"+id+"'");
			if(null != cssClass && !"".equals(cssClass)){
				s.append(" class='"+cssClass+"'");
			}
			if(null != style && !"".equals(style)){
				s.append(" style='"+style+"'");
			}
			if(null != disabled && !"".equals(disabled)){
				s.append(" disabled='"+disabled+"'");
			}
			if(null != onchangeFun && !"".equals(onchangeFun)){
				s.append("  onchange= 'javascript:"+onchangeFun+"(this.value);'");
			}
			s.append(">");
			s.append("<option value=''>"+"请选择"+"</option>");
			for(DictEntry dictEntry : dictEntrys){
				//过滤选项
				if(StringUtils.isNotBlank(filter)){
					boolean isclude=false;
					String item = filter.split("/")[0].trim();
					String content=filter.split("/")[1].trim();
					if("name".equalsIgnoreCase(item)){
						isclude = dictEntry.getName().indexOf(content)>=0?true:false;
					}else if("value".equalsIgnoreCase(item)){
						isclude = dictEntry.getValue().indexOf(content)>=0?true:false;
					}else{
						isclude=true;
					}
					if(!isclude){
						continue;
					}
				}
				
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getOnchangeFun() {
		return onchangeFun;
	}

	public void setOnchangeFun(String onchangeFun) {
		this.onchangeFun = onchangeFun;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
}

