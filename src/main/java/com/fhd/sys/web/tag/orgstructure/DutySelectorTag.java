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

import com.fhd.sys.dao.duty.DutyDAO;
import com.fhd.sys.entity.duty.Duty;

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

public class DutySelectorTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	// 是否多选
	private boolean multiple;

	// 窗口标题
	private String title;

	// 窗口宽度
	private int width;

	// 窗口高度
	private int height;

	// 已经选择的职务列表
	private String choosedDutys;

	// 属性名称 用户获取选择值
	private String attributeName;

	// 弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssClass;

	// 必填标识  需要有星号 ，true必填  false 不需要必填
	private boolean compulsoryFlag = false;
	
	// 是否有复选框
	private boolean checkNode;

	// 是否只是叶子节点加复选框
	private boolean onlyLeafCheckable;

	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel;

	public DutySelectorTag() {
		super();

		multiple = false;

		title = "选择职务";

		width = 606;

		height = 400;

		choosedDutys = "";

		attributeName = "";

		cssClass = "extjs";

		checkNode = false;

		onlyLeafCheckable = true;

		checkModel = "multiple";
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
		DutyDAO dutyEntryBean = (DutyDAO) webApplicationContext.getBean("dutyDAO");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		if (multiple) {// 多选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/duty/openEmpSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "');}").append("</script>");

			sb.append("<input type='button' id='u' value='请选择/修改' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn_Long'/>");
			if(compulsoryFlag){
				sb.append("<font style='color:red'>*</font>");
			}
			
			
			sb.append("<br/><div id='div" + attributeName + "' class='fhd_selected_orgs'>");
			
			
			if (StringUtils.isNotBlank(choosedDutys)) {
				String queryStr[] = choosedDutys.split(",");
				StringBuilder stringBuilder = new StringBuilder("from Duty se where se.id in (");
				for (int i = 0; i < queryStr.length; i++) {
					stringBuilder.append("'").append(queryStr[i].trim()).append("'");
					if (i != queryStr.length - 1)
						stringBuilder.append(",");
				}
				stringBuilder.append(")");

				List<Duty> dutys = dutyEntryBean.find(stringBuilder.toString());
				if (dutys != null && dutys.size() > 0) {
					for (Duty duty : dutys)
						sb.append("<div id='" + duty.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedDutys(\"" + attributeName + "\",\"" + duty.getId() + "\")' name='" + attributeName + "' value='" + duty.getId() + "' /><span ext:qtip='"+duty.getDutyName()+"'>" + duty.getDutyName() + "</span></div>");
				}
			}
			sb.append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/duty/openEmpSelectorTreePage.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
							+ "&tag=" + attributeName + "');}").append("</script>");

			sb.append("<div id='div" + attributeName + "' class='fhd_selected_org'>");
			if (StringUtils.isNotBlank(choosedDutys)) {
				String queryStr[] = choosedDutys.split(",");
				Duty duty = dutyEntryBean.get(queryStr[0]);
				if (duty != null) {
					sb.append("<input type='radio' checked='checked' onClick='javascript:removeCheckedDuty(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + duty.getId() + "' /><span ext:qtip='"+duty.getDutyName()+"'>" + duty.getDutyName() + "</span>");
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value='" + duty.getId() + "'></div>");
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
				}
			} else{
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' name='" + this.attributeName + "' value=''></div>");
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
	
	public String getChoosedDutys() {
		return choosedDutys;
	}

	public void setChoosedDutys(String choosedDutys) {
		this.choosedDutys = choosedDutys;
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

	public boolean isCompulsoryFlag() {
		return compulsoryFlag;
	}

	public void setCompulsoryFlag(boolean compulsoryFlag) {
		this.compulsoryFlag = compulsoryFlag;
	}

}
