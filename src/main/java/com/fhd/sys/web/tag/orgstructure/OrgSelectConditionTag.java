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

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 组织机构查询条件多选择标签.
 * @author 吴德福
 * @version
 * @since Ver 1.1
 * @Date 2012-5-7 上午10:23:50
 * @see
 */
@SuppressWarnings("unchecked")
public class OrgSelectConditionTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	// 全路径
	private boolean fullPath = false;
	// 是否多选
	private boolean multiple = true;
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
	// 显示宽度
	private Integer objectSize = 20;
	// 所属公司ID属性
	private String companyIdProperty;
	// 样式
	private String style;
	// 是否显示默认部门
	private boolean defaultOrg = false;
	// 组件宽度
	private int cmWidth = 300;
	// 组件高度
	private int cmHeight = 90;
	// 是否置灰
	private boolean disabledFlag = false;
	
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		//String path = request.getContextPath();
		//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();

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

		String orgId = "";
		String orgName = "";
		String fullOrgName = "";
		if (multiple) {// 多选
			List<SysOrganization> orgs = null;
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
				if (null != orgs && orgs.size() > 0) {
					int i=0;
					for (SysOrganization org : orgs){
						orgId += org.getId();
						orgName += org.getOrgname();
						String full = getFullPath(org);
						fullOrgName += full+org.getOrgname();
						if(i != orgs.size()-1){
							orgId += ",";
							orgName += ",";
							fullOrgName += ",";
						}
						i++;
					}
					sb.append("<input type='text' id='").append(attributeName).append("_check' name='").append(attributeName).append("_check' value='").append(orgName).append("' style='width:").append(cmWidth-18).append("' />")
					.append("<span ext:qtip='"+fullOrgName+"'>"+orgName+ "</span>");
				}else{
					sb.append("<input type='text' id='").append(attributeName).append("_check' name='").append(attributeName).append("_check' value='' style='width:").append(cmWidth-18).append("' />");
				}
			}else{
				sb.append("<input type='text' id='").append(attributeName).append("_check' name='").append(attributeName).append("_check' value='' style='width:").append(cmWidth-18).append("' />");
			}
			if(!disabledFlag){
				//sb.append("<input type='button' value='请选择' onclick='treeShowHide()' class='fhd_tagMS_Btn'/>");
				sb.append("<img src='").append(contextPath).append("/images/btn/select.jpg'  onclick='treeShowHide()' align='center' width='18' height='22' />");
			}
			if(compulsoryFlag){
				sb.append("<font style='color:red'>*</font>");
			}
			sb.append("<input type='hidden' id='").append(attributeName).append("' name='").append(attributeName).append("' value='").append(orgId).append("' />");
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
				full = r.getOrgname() + "->" + full;
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
