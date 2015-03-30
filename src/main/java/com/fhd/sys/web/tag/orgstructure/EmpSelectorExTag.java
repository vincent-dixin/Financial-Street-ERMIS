/**
 * OrgSelector.java
 * com.fhd.fdc.commons.web.tag.sys.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-11 		David.Niu
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */
/**
 * OrgSelector.java
 * com.fhd.fdc.commons.web.tag.sys.orgstructure
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-11        David.Niu
 *
 * Copyright (c) 2010, FirstHuida All Rights Reserved.
 */

package com.fhd.sys.web.tag.orgstructure;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * ClassName:OrgSelector Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author David.Niu
 * @version
 * @since Ver 1.1
 * @Date 2010-10-11 上午10:23:50
 * 
 * @see
 */

public class EmpSelectorExTag extends TagSupport {

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
	private String choosedEmps;

	// 属性名称 用户获取选择值
	private String attributeName;

	// 弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssClass;

	// 是否有复选框
	private boolean checkNode;

	// 是否只是叶子节点加复选框
	private boolean onlyLeafCheckable;

	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel;
	
	//员工显示过滤条件
	private String empfilter;
	
	private String require;

	public EmpSelectorExTag() {
		super();

		multiple = false;

		title = "选择员工";

		width = 1000;

		height = 600;

		choosedEmps = "";

		attributeName = "";

		cssClass = "extjs";

		checkNode = false;

		onlyLeafCheckable = true;

		checkModel = "multiple";
		
		empfilter="";
		
		require="";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();
		// generate scripts
		sb.append("<script type=\"text/javascript\" src=\"" + contextPath + "/scripts/orgtag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/tags.css\"/>");

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysEmployeeDAO empEntryBean = (SysEmployeeDAO) webApplicationContext.getBean("sysEmployeeDAO");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		if (multiple) {// 多选
			sb.append("<script type=\"text/javascript\">").append(//EmpSelectorTag
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorExTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName +"&selects="+choosedEmps+ "&empfilter=" + empfilter + "');}").append("</script>");

			sb.append("<input type='button' id='u' value='请选择/修改' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn_Long'/>"+("true".equals(require)?"<font color='red'>*</font>":"")+"<br/>").append("<div id='div" + attributeName + "' class='fhd_selected_orgs'>");
		
			if (StringUtils.isNotBlank(choosedEmps)) {
				String queryStr[] = choosedEmps.split(",");
				StringBuilder stringBuilder = new StringBuilder("from SysEmployee se where se.id in (");
				for (int i = 0; i < queryStr.length; i++) {
					stringBuilder.append("'").append(queryStr[i]).append("'");
					if (i != queryStr.length - 1){
						stringBuilder.append(",");
					}
				}
				stringBuilder.append(")");

				List<SysEmployee> emps = empEntryBean.find(stringBuilder.toString());
				if (emps != null && emps.size() > 0) {
					for (SysEmployee emp : emps){
						sb.append("<span id='" + emp.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + attributeName + "\",\"" + emp.getId() + "\")' name='" + attributeName + "' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname() + "</span></span>");
					}
				}
			}
			
			sb.append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorExTreePage.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "&empfilter=" + empfilter + "');}").append("</script>");

			sb.append("<div id='div" + attributeName + "' class='fhd_selected_org'>");
			if (StringUtils.isNotBlank(choosedEmps)) {
				String queryStr[] = choosedEmps.split(",");
				SysEmployee emp = empEntryBean.get(queryStr[0]);
				if (emp != null) {
					sb.append("<input type='radio' checked='checked' onClick='javascript:removeCheckedEmp(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname()+"</span>");
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/>"+("true".equals(require)?"<font color='red'>*</font>":"")+"<input type='hidden' name='" + this.attributeName + "' value='" + emp.getId() + "'></div>");
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/>"+("true".equals(require)?"<font color='red'>*</font>":"")+"<input type='hidden' name='" + this.attributeName + "' value=''></div>");
				}
			}else{
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
			}
		}

		JspWriter out = pageContext.getOut();
		try {
			out.print(sb.toString());
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
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

	public String getChoosedEmps() {
		return choosedEmps;
	}

	public void setChoosedEmps(String choosedEmps) {
		this.choosedEmps = choosedEmps;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
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

	public String getEmpfilter() {
		return empfilter;
	}

	public void setEmpfilter(String empfilter) {
		this.empfilter = empfilter;
	}

	public String getRequire() {
		return require;
	}

	public void setRequire(String require) {
		this.require = require;
	}
	

}
