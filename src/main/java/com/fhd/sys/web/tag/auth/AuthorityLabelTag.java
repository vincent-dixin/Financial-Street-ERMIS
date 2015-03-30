/**
 * AuthorityLabelTag.java
 * com.fhd.fdc.commons.web.tag.dic
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-11 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.tag.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.entity.auth.SysAuthority;


/**
 * 自定义AuthorityLabelTag标签.
 * 
 * <pre>
 * 自定义AuthorityLabelTag标签使用范例：
 * 页面中导入标签：<%@ taglib uri="fhd-tag" prefix="fhd" %>
 * 页面中使用标签：<fhd:authorityLabel path="id" id="" name="" value=""/>
 * 参数path:authorityLabelTag的属性path.
 * 参数id:authorityLabelTag的属性id.
 * 参数id:authorityLabelTag的属性name.
 * 参数id:authorityLabelTag的属性value.
 * </pre>
 * 
 * @author wudefu
 * @since Ver 1.1
 * @Date 2010-8-30 下午04:52:36
 * @see
 */
public class AuthorityLabelTag extends TagSupport {

	private static final long serialVersionUID = 6304759040279662213L;
	/**
	 * lable的parentId属性值.
	 */
	private String path;
	/**
	 * lable的id属性值.
	 */
	private String id;
	/**
	 * lable的value属性值.
	 */
	private String value;
	/**
	 * lable的name属性值.
	 */
	private String name;
	
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		
		try {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
			SysAuthorityBO sysAuthorityBean = (SysAuthorityBO) webApplicationContext.getBean("sysAuthorityBO");
			JspWriter out = pageContext.getOut();
			List<SysAuthority> sysAuthoritys = sysAuthorityBean.queryAllAuthority();
			
			//label tag
			StringBuffer s = new StringBuffer();
			for(SysAuthority sysAuthority : sysAuthoritys){
				if(path.equals(sysAuthority.getId())){
					if(sysAuthority.getRank()==1){
						s.append(sysAuthority.getAuthorityName());
					}
					if(sysAuthority.getRank()==2){
						s.append(sysAuthority.getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getAuthorityName());
					}
					if(sysAuthority.getRank()==3){
						s.append(sysAuthority.getParentAuthority().getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getAuthorityName());
					}
					if(sysAuthority.getRank()==4){
						s.append(sysAuthority.getParentAuthority().getParentAuthority().getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getParentAuthority().getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getParentAuthority().getAuthorityName());
						s.append(">>");
						s.append(sysAuthority.getAuthorityName());
					}
				}
			}
			
			out.write(s.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	
	/**
	 * @return String
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param parentId
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
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}

