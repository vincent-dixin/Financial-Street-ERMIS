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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 自定义DictEntryTag显示label标签.
 * 
 * <pre>
 * 自定义DictEntryTag标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictEntryLabel path="id"/>
 * 参数path:数据字典条目id.
 * </pre>
 * 
 * @author   wudefu
 * @since    Ver 1.1
 * @Date	 2010-8-15		下午04:52:36
 * @see 	 
 */
public class DictEntryLabelTag extends TagSupport {

	private static final long serialVersionUID = 6304759040279662213L;
	/**
	 * lable的path属性值.
	 */
	private String path;
	
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		
		try {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			OldDictEntryBO dictEntryBean = (OldDictEntryBO) webApplicationContext.getBean("oldDictEntryBO");
			JspWriter out = pageContext.getOut();
			List<DictEntry> dictEntrys = dictEntryBean.queryAllDictEntry();
			
			//label tag
			StringBuffer s = new StringBuffer();
			for(DictEntry dictEntry : dictEntrys){
				if(path.equals(dictEntry.getId())){
					if(dictEntry.getLevel()==1){
						s.append(dictEntry.getName());
					}
					if(dictEntry.getLevel()==2){
						s.append(dictEntryBean.queryDictEntryById(dictEntry.getParent().getId()).getName());
						s.append(">>");
						s.append(dictEntry.getName());
					}
					if(dictEntry.getLevel()==3){
						s.append(dictEntryBean.queryDictEntryById(dictEntryBean.queryDictEntryById(dictEntry.getParent().getId()).getParent().getId()).getName());
						s.append(">>");
						s.append(dictEntryBean.queryDictEntryById(dictEntry.getParent().getId()).getName());
						s.append(">>");
						s.append(dictEntry.getName());
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

