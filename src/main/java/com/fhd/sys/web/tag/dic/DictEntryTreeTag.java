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

package com.fhd.sys.web.tag.dic;

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

import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.entity.dic.DictEntry;

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

public class DictEntryTreeTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	// 是否多选
	private boolean multiple;

	// 窗口标题
	private String title;

	// 窗口宽度
	private int width;

	// 窗口高度
	private int height;

	// 已经选择的字典条目列表
	private String choosedDicts;

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
	
	//数据类型标题
	private String dictTypeTitle;
	
	private int cmWidth = 300;//组件宽度
	
	private int cmHeight = 90;//组件高度

	public DictEntryTreeTag() {
		super();

		multiple = false;

		title = "选择员工";

		width = 606;

		height = 400;

		choosedDicts = "";

		attributeName = "";

		cssClass = "extjs";

		checkNode = false;

		onlyLeafCheckable = true;

		checkModel = "multiple";
		
		dictTypeTitle = "";
	}
	
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
		DictEntryDAO dictEntryBean = (DictEntryDAO) webApplicationContext.getBean("dictEntryDAO");
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");

		if (multiple) {// 多选
			List<DictEntry> dicts = new ArrayList<DictEntry>();
			if (StringUtils.isNotBlank(choosedDicts)) {
				String queryStr[] = choosedDicts.split(",");
				StringBuilder stringBuilder = new StringBuilder("from DictEntry de where de.id in (");
				for (int i = 0; i < queryStr.length; i++) {
					stringBuilder.append("'").append(queryStr[i]).append("'");
					if (i != queryStr.length - 1)
						stringBuilder.append(",");
				}
				stringBuilder.append(")");

				dicts = dictEntryBean.find(stringBuilder.toString());
			}
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/dict/openDictSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel + "&dictTypeTitle="+dictTypeTitle
							+ "&tag=" + attributeName + "');}").append("</script>");

//			sb.append("<input type='button' id='u' value='请选择/修改' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn_Long'/><br/>").append("<div id='div" + attributeName + "' class='fhd_selected_orgs'>");
			sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div><input type='hidden' id='check_").append(attributeName).append("' name='check_").append(attributeName).append("' value='");
			if (dicts != null && dicts.size() > 0) {
				sb.append("1");
			}
			sb.append("' /></div>")
			.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
			
			
			if (dicts != null && dicts.size() > 0) {
				for (DictEntry dict : dicts)
					sb.append("<div id='" + dict.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedValues(\"" + attributeName + "\",\"" + dict.getId() + "\")' name='" + attributeName + "' value='" + dict.getId() + "' />" + dict.getName() + "</div>");
			}
			
			sb.append("</div>");
		} else {// 单选
			sb.append("<script type=\"text/javascript\">").append(
					"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/dict/openDictSelectorTreePage.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel + "&dictTypeTitle="+dictTypeTitle
							+ "&tag=" + attributeName + "');}").append("</script>");

//			sb.append("<div id='div" + attributeName + "' class='fhd_selected_org'>");
			sb.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='border: 1px solid #CCDFF0;width:"+cmWidth+";height:20px;float:left;font-size:12px;'>");
			if (StringUtils.isNotBlank(choosedDicts)) {
				DictEntry dict = dictEntryBean.get(choosedDicts);
				if (dict != null) {
					sb.append("<input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + dict.getId() + "' />" + dict.getName());
					sb.append("<input type='hidden' name='" + this.attributeName + "' value='" + dict.getId() + "'></div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/></div>");
				}else{
					sb.append("<input type='hidden' name='" + this.attributeName + "' value=''></div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/></div>");
				}
			}else{
				sb.append("<input type='hidden' name='" + this.attributeName + "' value=''/></div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/></div>");
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

	public String getChoosedDicts() {
		return choosedDicts;
	}

	public void setChoosedDicts(String choosedDicts) {
		this.choosedDicts = choosedDicts;
	}

	public String getDictTypeTitle() {
		return dictTypeTitle;
	}

	public void setDictTypeTitle(String dictTypeTitle) {
		this.dictTypeTitle = dictTypeTitle;
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

}
