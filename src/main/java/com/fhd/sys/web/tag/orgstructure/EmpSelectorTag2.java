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

import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.auth.SysUser;
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
public class EmpSelectorTag2 extends TagSupport {
	
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
	//单选框显示0：不显示 1：自己 2：自己、自定义3：风险管理员
	private int radioType;
	//是否出现必选
	private boolean require;
	
	public EmpSelectorTag2() {
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
		radioType=0;
		require = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();
		
		SysUserBO o_sysUserBO = (SysUserBO) webApplicationContext.getBean("sysUserBO");
		SysRoleBO o_sysRoleBO = (SysRoleBO) webApplicationContext.getBean("sysRoleBO");
		EmpolyeeBO o_empolyeeBO = (EmpolyeeBO) webApplicationContext.getBean("empolyeeBO");
		
		SysEmployee employee = null;
		SysUser user = null;
		if(StringUtils.isBlank(choosedEmps)){
			user = o_sysUserBO.getSysUserByEmpId(UserContext.getUser().getEmpid());
			List<SysEmployee> employees = o_sysRoleBO.getEmpByCorpAndRole("");
			if(employees != null && employees.size() > 0) {
				employee = employees.iterator().next();
			}
		}else{
			employee = o_empolyeeBO.get(choosedEmps);
			user = o_sysUserBO.getSysUserByEmpId(choosedEmps);
		}
		
		// generate scripts
		
		sb.append("	<table border=\"0\" margin=\"0\" padding=\"0\" cellspacing=\"0\"cellpadding=\"0\">" );
		if(radioType!=0){	
			sb.append("<tr><td>	" );
					if(radioType==1||radioType==2){	
						sb.append("<input type=\"radio\" name=\"userCheck\"  value=\"1\" onclick=\"onusercheck(this.value);\" checked=\"true\" />自己&nbsp;");
					}
					if(radioType==2){
						sb.append("<input type=\"radio\" name=\"userCheck\"  value=\"2\" onclick=\"onusercheck(this.value);\"  />自定义&nbsp;" );
					}
					if(radioType==3){
						sb.append("<input type=\"radio\" name=\"userCheck\"  value=\"3\" onclick=\"onusercheck(this.value);\" checked=\"true\" />风险管理员&nbsp;");
					}
						
					sb.append("</td></tr>");
		}
		sb.append("<tr ><td >");
		
		sb.append("<script type=\"text/javascript\" src=\"" + contextPath + "/scripts/risktag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/tags.css\"/>");

		//WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysEmployeeDAO empEntryBean = (SysEmployeeDAO) webApplicationContext.getBean("sysEmployeeDAO");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		if (multiple) {// 多选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "&empfilter=" + empfilter + "');}").append("</script>");

			sb.append("<input type='button' id='u' value='请选择/修改' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn_Long'/><br/>").append("<div id='div" + attributeName + "' class='fhd_selected_orgs'>");
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
						sb.append("<div id='" + emp.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + attributeName + "\",\"" + emp.getId() + "\")' name='" + attributeName + "' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname() + "</span></div>");
					}
				}
			}
			sb.append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/openEmpSelectorTreePage.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "&empfilter=" + empfilter + "');}").append("</script>");

			sb.append("<div id='div" + attributeName + "' class='fhd_selected_org'>");
			if (StringUtils.isNotBlank(choosedEmps)) {
				String queryStr[] = choosedEmps.split(",");
				SysEmployee emp = empEntryBean.get(queryStr[0]);
				if (emp != null) {
					sb.append("<input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + attributeName + "\",\"" + emp.getId() + "\")' name='" + attributeName + "_check' value='" + emp.getId() + "' /><span ext:qtip='"+emp.getEmpname()+"'>" + emp.getEmpname()+"</span>");
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input id='pselect'  type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value='" + emp.getId() + "'></div>");
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input id='pselect'  type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
				}
			}else{
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input id='pselect'  type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
			}
		}
		if(require){
			sb.append("<font style=\"color:red\">*</font>");
		}
		sb.append("</td></tr></table>");
			sb.append("<script type=\"text/javascript\">")
				.append("function onusercheck(cv){ ")
						.append(" if(cv=='1'){")
								.append(" document.getElementById('pselect').disabled=true;")
								.append(" document.getElementById('div"+attributeName+"').innerHTML=\"<input name='"+attributeName+"_check' value='"+UserContext.getUser().getEmpid()+"' type='radio' checked=checked/>"+user.getRealname()+"\";")
								.append(" document.getElementsByName('"+attributeName+"')[0].value='"+UserContext.getUser().getEmpid()+"';")
						.append("} else if(cv=='2'){")
								.append(" document.getElementById('pselect').disabled=false;")
						.append("} else if(cv=='3')");
							if(employee != null){
								sb.append("/* document.getElementById('pselect').disabled=true;*/")
								.append(" document.getElementById('div"+attributeName+"').innerHTML=\"<input name='"+attributeName+"_check' value='"+employee.getId()+"' type='radio' checked=checked/>"+employee.getEmpname()+"\";")
								.append(" document.getElementsByName('"+attributeName+"')[0].value='"+employee.getId()+"';");
							}
						sb.append("}")
						.append("onusercheck('3')")
				.append("</script>");
				
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

	public int getRadioType() {
		return radioType;
	}

	public void setRadioType(int radioType) {
		this.radioType = radioType;
	}

	public boolean isRequire() {
		return require;
	}

	public void setRequire(boolean require) {
		this.require = require;
	}
	

}
