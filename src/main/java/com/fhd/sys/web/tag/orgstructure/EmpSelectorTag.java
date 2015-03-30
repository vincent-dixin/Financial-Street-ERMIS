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
import java.util.ArrayList;
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
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * OrgSelector Function
 * @author David.Niu
 * @version
 * @since Ver 1.1
 * @Date 2010-10-11 上午10:23:50
 * @see
 */
@SuppressWarnings("unchecked")
public class EmpSelectorTag extends TagSupport {

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
	// 员工显示过滤条件
	private String empfilter;
	// 样式
	private String style;
	
	private Boolean defaultOrg;
	
	private int cmWidth = 300;//组件宽度
	
	private int cmHeight = 90;//组件高度

	public EmpSelectorTag() {
		super();
		multiple = false;
		title = "选择员工";
		width = 606;
		height = 400;
		choosedEmps = "";
		attributeName = "";
		cssClass = "extjs";
		checkNode = false;
		onlyLeafCheckable = true;
		checkModel = "multiple";
		empfilter="";
		defaultOrg = false;
	}

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
			List<SysEmployee> emps =new ArrayList<SysEmployee>();
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
				emps=empEntryBean.find(stringBuilder.toString());

				
			}
		
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "&empfilter=" + empfilter + "');}").append("</script>");

//			sb.append("<input type='button' id='u' value='请选择/修改' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn_Long'/><br/>").append("<div id='div" + attributeName + "' class='fhd_selected_orgs'>");
			//sb.append("<table style='cursor:pointer' onclick='" + attributeName + "function()' width='" + cmWidth  + "' border='0' cellspacing='0' cellpadding='0'><tr height='20'><td background='" + contextPath +"/images/btn/cm_left.gif' width='5'>&nbsp;</td><td align='center' valign='middle' background='" + contextPath +"/images/btn/cm_middle.gif' style='font-size:12px'><strong>选择/删除</strong></td><td background='" + contextPath +"/images/btn/cm_right.gif' width='11'>&nbsp;</td></tr></table>")
			//.append("<div id='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'");
			sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div>");
			sb.append("<input type='hidden' id='check_").append(attributeName).append("' name='check_").append(attributeName).append("' value='");
			if (emps != null && emps.size() > 0) {
				sb.append("1");
			}
			sb.append("' /></div>")
			.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
			
			if (emps != null && emps.size() > 0) {
				for (SysEmployee emp : emps){
					sb.append("<div id='" + emp.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + attributeName + "\",\"" + emp.getId() + "\")' name='" + attributeName + "' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname() + "</span></div>");
				}
			}
			sb.append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorTreePage.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "&empfilter=" + empfilter + "&defaultOrg=" + defaultOrg + "');}").append("</script>");

			sb.append("<div ");
			if(null != style && !"".equals(style)){
				sb.append(" style='").append(style).append("'");
			}
			//sb.append(" id='div" + attributeName + "' class='fhd_selected_org'>");
			sb.append("id='div" + attributeName + "' style='border: 1px solid #CCDFF0;width:"+cmWidth+";height:20px;float:left;font-size:12px;'>");
			if (StringUtils.isNotBlank(choosedEmps)) {
				String queryStr[] = choosedEmps.split(",");
				SysEmployee emp = empEntryBean.get(queryStr[0]);
				if (emp != null) {
					sb.append("<input type='radio' checked='checked' onClick='javascript:removeCheckedEmp(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname()+"</span>");
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value='" + emp.getId() + "'></div>");
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
				}
			}else{
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn'/><input type='hidden' name='").append(this.attributeName).append("' value='' /></div>");
			}
		}

		JspWriter out = pageContext.getOut();
		try {
			out.print(sb.toString());
		} catch (IOException e) {
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public int getCmWidth() {
		return cmWidth;
	}

	public void setCmWidth(int cmWidth) {
		this.cmWidth = cmWidth;
	}

	public int getCmHeight() {
		return cmHeight;
	}

	public void setCmHeight(int cmHeight) {
		this.cmHeight = cmHeight;
	}

	public Boolean getDefaultOrg() {
		return defaultOrg;
	}

	public void setDefaultOrg(Boolean defaultOrg) {
		this.defaultOrg = defaultOrg;
	}
}
