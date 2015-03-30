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

package com.fhd.sys.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.sys.dao.helponline.HelpCatalogDAO;
import com.fhd.sys.entity.helponline.HelpCatalog;

/**
 * 类别选择控件.
 * @author   --韩丽兵修改 --元杰修改 20120723
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-7-4		上午10:40:05
 * @see 	 
 */
@SuppressWarnings("unchecked")
public class CatalogSelectorTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	// 全路径
	private boolean fullPath = false;
	// 是否多选
	private boolean multiple;
	// 窗口标题
	private String title;
	// 窗口宽度
	private int width;
	// 窗口高度
	private int height;
	// 已经选择的员工列表
	private String choosedCatalog;
	// 属性名称 用户获取选择值
	private String attributeName;
	// 弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssClass;
	// 是否有复选框
	private boolean checkNode;
	// 是否可选根节点
	private boolean rootCheck;
	// 是否只是叶子节点加复选框
	private boolean onlyLeafCheckable;
	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel;
	// 是否只选择风险大类
	private boolean catalogClass = false;
	
	private int cmWidth = 300;//组件宽度
	
	private int cmHeight = 90;//组件高度
	
	/**
	 * 展现方式
	 * 1、checkType：选择方式(带有radio或者checkbox,默认)
	 * 2、textFiledType：文本框方式
	 */
	private String showType;
	//显示宽度
	private Integer objectSize;

	public CatalogSelectorTag() {
		super();

		multiple = false;

		title = "所属类别";

		width = 606;

		height = 400;

		choosedCatalog = "";

		attributeName = "";

		cssClass = "extjs";

		checkNode = false;

		onlyLeafCheckable = true;

		checkModel = "multiple";
		
		showType = "checkType";
		
		objectSize = 20;
	}

	@Override
	public int doStartTag() throws JspException {
//		if(onlyLeafCheckable){
//			rootCheck=false;
//		}
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		StringBuilder sb = new StringBuilder();
		// generate scripts
		sb.append("<script type=\"text/javascript\" src=\"" + contextPath + "/scripts/risktag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/tags.css\"/>");

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		HelpCatalogDAO helpEntryBean = (HelpCatalogDAO) webApplicationContext.getBean("helpCatalogDAO");
		//带radio或者checkbox的展现方式
		if("checkType".equalsIgnoreCase(showType)){
			if (multiple) {// 多选
				sb.append("<script type=\"text/javascript\">").append(
						"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "risk/identification/risk/openRiskSelectorTreePage.do?type=mutiple&checkNode=" + checkNode + "&rootCheck=" + rootCheck + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
								+ "&isFullPath=" + fullPath + "&catalogClass=" + catalogClass + "&tag=" + attributeName + "');}").append("</script>");

				sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName 
						+ "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px; font-weight:bold; vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div></div>")
				.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
				
				if (StringUtils.isNotBlank(choosedCatalog)) {
					String queryStr[] = choosedCatalog.split(",");
					StringBuilder stringBuilder = new StringBuilder("from HelpCatalog hc where hc.id in (");
					for (int i = 0; i < queryStr.length; i++) {
						stringBuilder.append("'").append(queryStr[i]).append("'");
						if (i != queryStr.length - 1){
							stringBuilder.append(",");
						}
					}
					stringBuilder.append(")");
					List<HelpCatalog> helpCatalog = helpEntryBean.find(stringBuilder.toString());
					if (helpCatalog != null && helpCatalog.size() > 0) {
						for (HelpCatalog hc : helpCatalog) {
							String full = getFullPath(hc);
							sb.append("<div id='" + hc.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedRisks(\"" + attributeName + "\",\"" + hc.getId() + "\")' name='" + attributeName + "' value='" + hc.getId() + "' /><span ext:qtip='"+full+hc.getCatalogName()+"'>"
									+ com.fhd.core.utils.StringUtils.shortString(hc.getCatalogName(), 20) + "</span></div>");
						}
					}
				}
				sb.append("</div>");
			} else {// 单选
				sb.append("<script type=\"text/javascript\">").append(
						"function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/helponline/openHelpCatalogTreeLoader.do?type=single&checkNode=" + checkNode + "&rootCheck=" + rootCheck + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel
								+ "&isFullPath=" + fullPath + "&catalogClass=" + catalogClass + "&tag=" + attributeName + "');}")
				.append("</script>");
				sb.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='border: 1px solid #CCDFF0;width:"+cmWidth+";height:20px;float:left;font-size:12px;'>");
				if (StringUtils.isNotBlank(choosedCatalog)) {
					String queryStr[] = choosedCatalog.split(",");
					//RmRisk risk = rmEntryBean.get(queryStr[0]);
					HelpCatalog helpCatalog = helpEntryBean.get(queryStr[0]);//
					if (helpCatalog != null) {
						String full = getFullPath(helpCatalog);
						sb.append("<div id='checkValue'><input type='radio' class='fhd_btn' checked='checked' onClick='javascript:removeChecked(\"" + attributeName + "\")' name='" + attributeName + "_check' value='" + helpCatalog.getId() + "' /><span ext:qtip='"+full+helpCatalog.getCatalogName()+"'>" + com.fhd.core.utils.StringUtils.shortString(helpCatalog.getCatalogName(), 20)+"</span>");
						sb.append("</div></div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' class='fhd_btn' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' id='"+ this.attributeName+"' name='" + this.attributeName + "' value='" + helpCatalog.getId() + "'></div>");

					}else{
						sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button'  class='fhd_btn' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' id='"+ this.attributeName+"' name='" + this.attributeName + "'></div>");
					}
				}else{
					sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button'  class='fhd_btn' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/><input type='hidden' id='"+ this.attributeName+"' name='" + this.attributeName + "'></div>");
				}
			}	
		}
		JspWriter out = pageContext.getOut();
		try {
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

	private String getFullPath(HelpCatalog helpCatalog) {
		String full = "";
		if (fullPath && null != helpCatalog.getParent()) {
			HelpCatalog hc = helpCatalog.getParent();
			while (hc.getParent() != null) {
				full = hc.getCatalogName() + ">>" + full;
				hc = hc.getParent();
			}
		}
		return full;
	}
//	private String getFullPath(RmRisk risk) {
//		String full = "";
//		if (fullPath && null != risk.getParentRisk()) {
//			RmRisk r = risk.getParentRisk();
//			while (r.getParentRisk() != null) {
//				full = r.getRiskName() + ">>" + full;
//				r = r.getParentRisk();
//			}
//		}
//		return full;
//	}

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

	public String getChoosedCatalog() {
		return choosedCatalog;
	}

	public void setChoosedCatalog(String choosedCatalog) {
		this.choosedCatalog = choosedCatalog;
	}

	public boolean isCatalogClass() {
		return catalogClass;
	}

	public void setCatalogClass(boolean catalogClass) {
		this.catalogClass = catalogClass;
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

	public boolean isFullPath() {
		return fullPath;
	}

	public void setFullPath(boolean fullPath) {
		this.fullPath = fullPath;
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

	public boolean isRootCheck() {
		return rootCheck;
	}

	public void setRootCheck(boolean rootCheck) {
		this.rootCheck = rootCheck;
	}

	
}
