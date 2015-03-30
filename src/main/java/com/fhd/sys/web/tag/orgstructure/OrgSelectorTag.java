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
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * ClassName:OrgSelector Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * @author David.Niu
 * @version
 * @since Ver 1.1
 * @Date 2010-10-11 上午10:23:50
 * @see
 */
@SuppressWarnings("unchecked")
public class OrgSelectorTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	// 全路径
	private boolean fullPath = false;
	// 是否多选
	private boolean multiple = false;
	// 窗口标题
	private String title = "选择组织机构";
	// 窗口宽度
	private int width = 606;
	// 窗口高度
	private int height = 400;
	// 已经选择的部门列表
	private String choosedOrgs;
	// 弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssClass = "extjs";
	// 是否有复选框
	private boolean checkNode = false;
	// 必填标识  需要有星号 ，true必填  false 不需要必填
	private boolean compulsoryFlag = false;
	// 是否只是叶子节点加复选框
	private boolean onlyLeafCheckable = true;
	// 属性名
	private String attributeName;
	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel = "multiple";
	/**
	 * 展现方式
	 * 1、checkType：选择方式(带有radio或者checkbox,默认)
	 * 2、textFiledType：文本框方式
	 */
	private String showType = "checkType";
	// 显示宽度
	private Integer objectSize = 20;
	// 所属公司ID属性
	private String companyIdProperty;
	// 样式
	private String style;
	// 是否显示默认部门
	private boolean defaultOrg = false;
	
	private int cmWidth = 300;//组件宽度
	
	private int cmHeight = 90;//组件高度

	// 是否置灰
	private boolean disabledFlag = false;
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();
		// generate scripts
		sb.append("<script type=\"text/javascript\" src=\"" + contextPath + "/scripts/risktag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/tags.css\"/>");

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysOrganizationDAO orgEntryBean = (SysOrganizationDAO) webApplicationContext.getBean("sysOrganizationDAO");
		EmpolyeeBO employeeEntryBean = (EmpolyeeBO) webApplicationContext.getBean("empolyeeBO");

		//如果默认部门为空，则插入当前登录人员所在的部门
		if (defaultOrg && StringUtils.isBlank(choosedOrgs)) {
			String userid = UserContext.getUserid();
			SysEmployee employee = employeeEntryBean.getEmployee(userid);
			if(null != employee){
				SysOrganization department = employeeEntryBean.getDepartmentByEmpId(employee.getId());
				if(null != department){
					choosedOrgs = department.getId();
				}
			}
		}
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		//带radio或者checkbox的展现方式
		if("checkType".equalsIgnoreCase(showType)){
			if (multiple) {// 多选
				List<SysOrganization> orgs = new ArrayList<SysOrganization>();
				if (StringUtils.isNotBlank(choosedOrgs)) {
					String queryStr[] = choosedOrgs.split(",");
					StringBuilder stringBuilder = new StringBuilder("from SysOrganization so where so.id in (");
					for (int i = 0; i < queryStr.length; i++) {
						stringBuilder.append("'").append(queryStr[i]).append("'");
						if (i != queryStr.length - 1)
							stringBuilder.append(",");
					}
					stringBuilder.append(")");
	
					orgs = orgEntryBean.find(stringBuilder.toString());
				}
				sb.append("<script type=\"text/javascript\">")
				.append("function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
								+ "&isFullPath=" + fullPath + "&tag=" + attributeName + "');}")
				.append("</script>");
                if(!disabledFlag){
            	   sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div>");
                }
				sb.append("<input type='hidden' id='check_").append(attributeName).append("' name='check_").append(attributeName).append("' value='");
				if (orgs != null && orgs.size() > 0) {
					sb.append("1");
				}
				sb.append("' />");
				if(compulsoryFlag){
					sb.append("<font style='color:red'>*</font>");
				}
				sb.append("</div><div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
				
				
				if (orgs != null && orgs.size() > 0) {
					for (SysOrganization org : orgs){
						String full = getFullPath(org);
						String tempId = org.getId()+"_"+RandomUtils.nextInt(9999);
						sb.append("<div id='" + tempId + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedValues(\"" + attributeName + "\",\"" + tempId + "\")' name='" + attributeName + "' value='" + org.getId() + "' /><span ext:qtip='"+full+org.getOrgname()+"'>"
								+org.getOrgname()+ "</span></div>");
					}
				}
				
				sb.append("</div>");
			} else {// 单选
				sb.append("<script type=\"text/javascript\">").append(
						"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openSelectorTreePage.do?type=single&checkNode=" + checkNode + "&isFullPath=" + fullPath + "&onlyLeafCheckable=" + onlyLeafCheckable
								+ "&checkModel=" + checkModel + "&tag=" + attributeName + "');}").append("</script>");
	
//				sb.append("<div id='div").append(attributeName).append("'");
//				if(null != style && !"".equals(style)){
//					sb.append(" style='").append(style).append("'");
//				}
//				sb.append(" class='fhd_selected_org'>");
				
				sb.append("<div id='div" + attributeName + "'");
				if(StringUtils.isNotBlank(style))
					sb.append(" style='").append(style).append("'");
				else
					sb.append(" style='border: 1px solid #CCDFF0;width:"+cmWidth+";height:20px;float:left;font-size:12px;'>");
				
				if (StringUtils.isNotBlank(choosedOrgs)) {
					String queryStr[] = choosedOrgs.split(",");
					SysOrganization org = orgEntryBean.get(queryStr[0]);
					if (org != null) {
						String full = getFullPath(org);
						sb.append("<input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + org.getId() + "' /><span ext:qtip='"+full+org.getOrgname()+"'>" + org.getOrgname()+"</span>");
						sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value='" + org.getId() + "'></div>");
					}else{
						sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "'></div>");
					}
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "'></div>");
				}
			}
			//带文本框的展现方式
		}else if("textFiledType".equalsIgnoreCase(showType)){
			sb.append("<script type=\"text/javascript\">");
			sb.append("function " + attributeName + "function(){");
			if(StringUtils.isNotBlank(companyIdProperty)){
				sb.append("var ").append(attributeName).append("CompanyId=document.getElementById('").append(companyIdProperty).append("').value;");
			}
			sb.append("openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
					+ "&isFullPath=" + fullPath + "&showType=" + showType + "&tag=" + attributeName);
			if(StringUtils.isNotBlank(companyIdProperty))
				sb.append("&companyId='+").append(attributeName+"CompanyId");
			else
				sb.append("'");
			sb.append(");}");
			sb.append("</script>");
			StringBuilder orgNames = new StringBuilder();
			
			//获取数据
			List<SysOrganization> orgs = null;//数据
			if (StringUtils.isNotBlank(choosedOrgs)) {
				String queryStr[] = choosedOrgs.split(",");
				StringBuilder stringBuilder = new StringBuilder("from SysOrganization so where so.id in (");
				for (int i = 0; i < queryStr.length; i++) {
					stringBuilder.append("'").append(queryStr[i]).append("'");
					if (i != queryStr.length - 1)
						stringBuilder.append(",");
				}
				stringBuilder.append(")");

				orgs = orgEntryBean.find(stringBuilder.toString());
			}
			
			//生成隐藏部分
			sb.append("<div id='div" + attributeName + "' style='display:none'>");
			if(orgs!=null && orgs.size()>0){
				for (SysOrganization org : orgs){
					String full = getFullPath(org);
					String tempId = org.getId()+RandomUtils.nextInt(9999);
					sb.append("<div id='" + tempId + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedValues(\"" + attributeName + "\",\"" + tempId + "\")' name='" + attributeName + "' value='" + org.getId() + "' /><span ext:qtip='"+full+org.getOrgname()+"'>" + org.getOrgname() + "</span></div>");
					orgNames.append(org.getOrgname()).append(",");
				}
					
			}
			sb.append("</div>");
			
			//生成textfiled和button
			String riskNameStr = orgNames.toString().length()>0?orgNames.toString().substring(0,orgNames.toString().length()-1):"";
			sb.append("<input type='text' style='" + style + "' id='textFiledType" + attributeName + "' value='" + riskNameStr + "'/>&nbsp;<input type='button' id='u' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/>");
		}

		JspWriter out = pageContext.getOut();
		try {
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	private String getFullPath(SysOrganization org) {
		String full = "";
		if (fullPath && null != org.getParentOrg()) {
			SysOrganization r = org.getParentOrg();
			while (null != r.getParentOrg()) {
				full = r.getOrgname() + ">>" + full;
				r = r.getParentOrg();
			}
		}
		return full;
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

	public boolean isCompulsoryFlag() {
		return compulsoryFlag;
	}

	public void setCompulsoryFlag(boolean compulsoryFlag) {
		this.compulsoryFlag = compulsoryFlag;
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

	public String getChoosedOrgs() {
		return choosedOrgs;
	}

	public void setChoosedOrgs(String choosedOrgs) {
		this.choosedOrgs = choosedOrgs;
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

	public boolean isFullPath() {
		return fullPath;
	}

	public void setFullPath(boolean fullPath) {
		this.fullPath = fullPath;
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

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Integer getObjectSize() {
		return objectSize;
	}

	public void setObjectSize(Integer objectSize) {
		this.objectSize = objectSize;
	}

	public String getCompanyIdProperty() {
		return companyIdProperty;
	}

	public void setCompanyIdProperty(String companyIdProperty) {
		this.companyIdProperty = companyIdProperty;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isDefaultOrg() {
		return defaultOrg;
	}

	public void setDefaultOrg(boolean defaultOrg) {
		this.defaultOrg = defaultOrg;
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

	public boolean isDisabledFlag() {
		return disabledFlag;
	}

	public void setDisabledFlag(boolean disabledFlag) {
		this.disabledFlag = disabledFlag;
	}
	
}
