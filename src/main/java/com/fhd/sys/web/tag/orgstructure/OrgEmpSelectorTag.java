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

import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * OrgSelector Function
 * @author 吴德福
 * @version
 * @since Ver 1.1
 * @Date 2012-3-12 下午15:23:50
 * @see
 */
@SuppressWarnings("unchecked")
public class OrgEmpSelectorTag extends TagSupport {

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
	// 属性名称用户获取选择值
	private String attributeName;
	// 属性名称部门获取选择值
	private String orgName;
	// 属性名称岗位获取选择值
	private String posiName;
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
	// 默认部门
	private Boolean defaultOrg;
	// 组件宽度
	private int cmWidth = 300;
	// 组件高度
	private int cmHeight = 90;

	public OrgEmpSelectorTag() {
		super();
		multiple = false;
		title = "选择员工";
		width = 606;
		height = 400;
		choosedEmps = "";
		attributeName = "";
		orgName="";
		posiName="";
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
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
		StringBuilder sb = new StringBuilder();
		// generate scripts
		sb.append("<script type=\"text/javascript\" src=\"").append(contextPath).append("/scripts/orgemptag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"").append(contextPath).append("/css/tags.css\"/>");

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysEmployeeDAO empEntryBean = (SysEmployeeDAO) webApplicationContext.getBean("sysEmployeeDAO");
		//OrganizationBO orgEntryBean = (OrganizationBO) webApplicationContext.getBean("organizationBO");
		EmpolyeeBO empBO = (EmpolyeeBO) webApplicationContext.getBean("empolyeeBO");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		if (multiple) {// 多选
			sb.append("<script type=\"text/javascript\">").append(
					"function ").append(attributeName).append("function(){openWindow('").append(title).append("',").append(width).append(",").append(height).append(",'").append(basePath).append("sys/orgstructure/org/openEmpSelectorTreePage.do?type=mutiple&checkNode=").append(checkNode).append("&onlyLeafCheckable=").append(onlyLeafCheckable).append("&checkModel=").append(checkModel
							).append("&tag=").append(attributeName).append("&orgName=").append(orgName).append("&posiName=").append(posiName).append("&empfilter=").append(empfilter).append("');}").append("</script>");

			//sb.append("<input type='button' id='u' value='请选择/修改' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn_Long'/><br/>").append("<div id='div").append(attributeName).append("' class='fhd_selected_orgs'>");
			
			//sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div>")
			
			//sb.append("<table style='cursor:pointer' onclick='").append(attributeName).append("function()' width='").append(cmWidth).append("' border='0' cellspacing='0' cellpadding='0'><tr height='20'><td background='").append(contextPath).append("/images/btn/cm_left.gif' width='5'>&nbsp;</td><td align='center' valign='middle' background='").append(contextPath).append("/images/btn/cm_middle.gif' style='font-size:12px;repeat:repeat-x;}'><strong>选择/删除</strong></td><td background='").append(contextPath).append("/images/btn/cm_right.gif' width='11'>&nbsp;</td></tr></table>")
			//.append("<div id='div").append(attributeName).append("' style='width: ").append(cmWidth).append(";height:").append(cmHeight).append(";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
			sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div>");
			sb.append("<input type='hidden' id='check_").append(attributeName).append("' name='check_").append(attributeName).append("' value='");
			
			SysPosition position = null;
			SysOrganization org = null;
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
					sb.append("1");
				}
				sb.append("' /></div>")
				.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
				
				if (emps != null && emps.size() > 0) {
					for (SysEmployee emp : emps){
						position = empBO.getPositionByEmpId(emp.getId());
						if(null != position){
							org = position.getSysOrganization();
							sb.append("<div id='").append(emp.getId()).append("'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedOrgEmps(\"").append(attributeName).append("\",\"").append(emp.getId()).append("\")' name='").append(attributeName).append("_check' value='").append(emp.getId()).append("' />").append("<span ext:qtitle='' ext:qtip='").append(org.getOrgname()).append("->").append(emp.getEmpname()).append("'>").append(position.getPosiname()).append("->").append(emp.getEmpname()).append("</span>");
							sb.append("<input type='hidden' name='").append(attributeName).append("' value='").append(emp.getId()).append("'/><input type='hidden' name='").append(orgName).append("' value='").append(org.getId()).append("'/><input type='hidden' name='").append(posiName).append("' value='").append(position.getId()).append("'/>");
							sb.append("</div>");
						}else{
							org = empBO.getDepartmentByEmpId(emp.getId());
							sb.append("<div id='").append(emp.getId()).append("'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedOrgEmps(\"").append(attributeName).append("\",\"").append(emp.getId()).append("\")' name='").append(attributeName).append("_check' value='").append(emp.getId()).append("' />").append("<span ext:qtitle='' ext:qtip='").append(org.getOrgname()).append("->").append(emp.getEmpname()).append("'>").append(emp.getEmpname()).append("</span>");
							sb.append("<input type='hidden' name='").append(attributeName).append("' value='").append(emp.getId()).append("'/><input type='hidden' name='").append(orgName).append("' value='").append(org.getId()).append("'/><input type='hidden' name='").append(posiName).append("' value=''/>");
							sb.append("</div>");
						}
					}
				}
			}
			
			sb.append("</div>").append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function ").append(attributeName).append("function(){openWindow('").append(title).append("',").append(width).append(",").append(height).append(",'").append(basePath).append("sys/orgstructure/org/openEmpSelectorTreePage.do?type=single&checkNode=").append(checkNode).append("&onlyLeafCheckable=").append(onlyLeafCheckable).append("&checkModel=").append(checkModel
							).append("&tag=").append(attributeName).append("&orgName=").append(orgName).append("&posiName=").append(posiName).append("&empfilter=").append(empfilter).append("&defaultOrg=").append(defaultOrg).append("');}").append("</script>");

			sb.append("<div ");
			if(StringUtils.isNotBlank(style)){
				sb.append(" style='").append(style).append("'");
			}
			//sb.append(" id='div").append(attributeName).append("' class='fhd_selected_org'>");
			sb.append("id='div").append(attributeName).append("' style='border: 1px solid #CCDFF0;width:").append(cmWidth).append(";height:20px;float:left;font-size:12px;'>");
			SysPosition position = null;
			SysOrganization org = null;
			if(StringUtils.isNotBlank(choosedEmps)) {
				String queryStr[] = choosedEmps.split(",");
				SysEmployee emp = empEntryBean.get(queryStr[0]);
				if(emp != null) {
					position = empBO.getPositionByEmpId(queryStr[0]);
					if(null != position){
						org = position.getSysOrganization();
						sb.append("<input type='radio' checked='checked' onClick='javascript:removeCheckedOrgEmp(\"").append(attributeName).append("\",\"").append(orgName).append("\",\"").append(posiName).append("\")' name='").append(attributeName).append("_check' value='").append(emp.getId()).append("' />").append(emp.getEmpname());
						sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn'/><input type='hidden' name='").append(attributeName).append("' value='").append(emp.getId()).append("'><input type='hidden' name='").append(orgName).append("' value='").append(org.getId()).append("'><input type='hidden' name='").append(posiName).append("' value='").append(position.getId()).append("'></div>");
					}else{
						org = empBO.getDepartmentByEmpId(queryStr[0]);
						sb.append("<input type='radio' checked='checked' onClick='javascript:removeCheckedOrgEmp(\"").append(attributeName).append("\",\"").append(orgName).append("\",\"").append(posiName).append("\")' name='").append(attributeName).append("_check' value='").append(emp.getId()).append("' />").append("<span ext:qtitle='' ext:qtip='").append(org.getOrgname()).append("->").append(emp.getEmpname()).append("'>").append(emp.getEmpname()).append("</span>");
						sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn'/><input type='hidden' name='").append(attributeName).append("' value='").append(emp.getId()).append("'><input type='hidden' name='").append(orgName).append("' value='").append(org.getId()).append("'><input type='hidden' name='").append(posiName).append("' value=''></div>");
					}
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn'/><input type='hidden' name='").append(attributeName).append("' value=''><input type='hidden' name='").append(orgName).append("' value=''><input type='hidden' name='").append(posiName).append("' value=''></div>");
				}
			}else{
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='").append(attributeName).append("function()' class='fhd_tagMS_Btn'/><input type='hidden' name='").append(attributeName).append("' value='' /><input type='hidden' name='").append(orgName).append("' value=''><input type='hidden' name='").append(posiName).append("' value=''></div>");
			}
		}
		System.out.println("sb="+sb.toString());
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPosiName() {
		return posiName;
	}

	public void setPosiName(String posiName) {
		this.posiName = posiName;
	}
}
