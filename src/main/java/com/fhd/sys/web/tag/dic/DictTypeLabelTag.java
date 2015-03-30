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

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.core.utils.StringUtils;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.entity.dic.DictType;


/**
 * 自定义DictTypeTag Label标签.
 * 
 * <pre>
 * 自定义DictTypeTag标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:dictTypeLabel path="id"/>
 * 参数path:数据字典类型id.
 * </pre>
 * 
 * @author wudefu
 * @since Ver 1.1
 * @Date 2010-8-15 下午04:52:36
 * @see
 */
public class DictTypeLabelTag extends TagSupport {

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
			OldDictEntryBO dictTypeBean = (OldDictEntryBO) webApplicationContext.getBean("oldDictEntryBO");
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String locale = request.getHeader("accept-language");
			JspWriter out = pageContext.getOut();
			List<DictType> dictTypes = dictTypeBean.queryDictTypeByLocale(StringUtils.split(locale, ",")[0].toLowerCase());
			
			//label tag
			StringBuffer s = new StringBuffer();
			for(DictType dictType : dictTypes){
				if(path.equals(dictType.getId())){
					if(dictType.getLevel()==1){
						s.append(dictType.getName());
					}
					if(dictType.getLevel()==2){
						s.append(dictTypeBean.queryDictTypeById(dictType.getParent().getId()).getName());
						s.append(">>");
						s.append(dictType.getName());
					}
					if(dictType.getLevel()==3){
						s.append(dictTypeBean.queryDictTypeById(dictTypeBean.queryDictTypeById(dictType.getParent().getId()).getParent().getId()).getName());
						s.append(">>");
						s.append(dictTypeBean.queryDictTypeById(dictType.getParent().getId()).getName());
						s.append(">>");
						s.append(dictType.getName());
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

