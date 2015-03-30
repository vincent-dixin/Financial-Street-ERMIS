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
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 员工查询条件多选择标签.
 * @author 吴德福
 * @version
 * @since Ver 1.1
 * @Date 2012-5-9 上午10:23:50
 * @see
 */
@SuppressWarnings("unchecked")
public class EmpSelectConditionTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	// 全路径
	private boolean fullPath = false;
	// 是否多选
	private boolean multiple = true;
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
	// 默认选中的员工
	private Boolean defaultEmp;
	// 组件宽度
	private int cmWidth = 300;
	// 组件高度
	private int cmHeight = 90;
	// 必填标识  需要有星号 ，true必填  false 不需要必填
	private boolean compulsoryFlag = false;
	// 是否置灰
	private boolean disabledFlag = false;

	public EmpSelectConditionTag() {
		super();
		multiple = true;
		choosedEmps = "";
		attributeName = "";
		cssClass = "extjs";
		checkNode = false;
		onlyLeafCheckable = true;
		checkModel = "multiple";
		empfilter="";
		cmWidth = 300;
		cmHeight = 90;
		defaultEmp = false;
	}

	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		//String path = request.getContextPath();
		//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysEmployeeDAO employeeDAO = (SysEmployeeDAO) webApplicationContext.getBean("sysEmployeeDAO");
		EmpolyeeBO employeeBO = (EmpolyeeBO) webApplicationContext.getBean("empolyeeBO");
		
		//如果默认部门为空，则插入当前登录人员所在的部门
		if (defaultEmp && StringUtils.isBlank(choosedEmps)) {
			String userid = UserContext.getUserid();
			SysEmployee employee = employeeBO.getEmployee(userid);
			if(null != employee){
				SysOrganization department = employeeBO.getDepartmentByEmpId(employee.getId());
				if(null != department){
					choosedEmps = department.getId();
				}
			}
		}
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		String empId = "";
		String empName = "";
		String fullName = "";
		if (multiple) {// 多选
			List<SysEmployee> emps = null;
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
				emps = employeeDAO.find(stringBuilder.toString());
				if(null != emps && emps.size()>0){
					int i=0;
					for(SysEmployee emp : emps){
						empId += emp.getId();
						empName += emp.getEmpname();
						if(fullPath){
							SysOrganization department = employeeBO.getDepartmentByEmpId(emp.getId());
							if(null != department){
								fullName = department.getOrgname()+"->"+emp.getEmpname();
							}
						}else{
							fullName += emp.getEmpname();
						}
						if(i != emps.size()-1){
							empId += ",";
							empName += ",";
							fullName += ",";
						}
						i++;
					}
					sb.append("<input type='text' id='").append(attributeName).append("_check' name='").append(attributeName).append("_check' value='").append(empName).append("' style='width:").append(cmWidth-18).append("' />")
					.append("<span ext:qtip='"+fullName+"'>"+empName+ "</span>");
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
			sb.append("<input type='hidden' id='").append(attributeName).append("' name='").append(attributeName).append("' value='").append(empId).append("' />");
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

	public Boolean getDefaultEmp() {
		return defaultEmp;
	}

	public void setDefaultEmp(Boolean defaultEmp) {
		this.defaultEmp = defaultEmp;
	}

	public boolean isCompulsoryFlag() {
		return compulsoryFlag;
	}

	public void setCompulsoryFlag(boolean compulsoryFlag) {
		this.compulsoryFlag = compulsoryFlag;
	}

	public boolean isDisabledFlag() {
		return disabledFlag;
	}

	public void setDisabledFlag(boolean disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	public boolean isFullPath() {
		return fullPath;
	}

	public void setFullPath(boolean fullPath) {
		this.fullPath = fullPath;
	}
}
