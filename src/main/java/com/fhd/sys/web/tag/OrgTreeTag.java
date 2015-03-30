/**
 * OrgTreeTag.java
 * com.fhd.fdc.commons.web.tag.org
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-2-16 		David
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/


package com.fhd.sys.web.tag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


/**
 * ClassName:OrgTreeTag
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-2-16		下午05:09:42
 *
 * @see 	 
 */

public class OrgTreeTag extends TagSupport {
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @author David
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 4089326474125833219L;

	private String id;//树的ID
	
	private String height = "500";//默认高度
	
	private String width = "300";//默认宽度
	
	private Boolean checkNode = false;// 是否有复选框
	
	// 多选: 'multiple'(默认)、单选: 'single'、级联多选:
	// 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel = "cascade";
	
	private Boolean onlyLeafCheckable = false;//是否只叶子节点可选
	
	private Boolean rootVisible = true;//是否显示根节点
	
	private String rootId;//树根ID
	
	private String rootName;//树根名称
	
	private Boolean rootCheck = false;//是否允许根节点带复选框
	
	private String onCheck = "Ext.emptyFn";//节点点击事件
	
	private Boolean showFreshBtn = false;//是否显示刷新按钮
	
	private String dataUrl = "";//树数据源地址
	
	private Boolean allowRootLink = true;//是否允许根节点有链接
	
	private String rootLink = "";//根节点链接
	
	private String sbEmptyText = "查找机构";//查询框默认文本
	
	private Boolean expandAll = false;//是否默认展开所有节点

	private String choose;
	
	@Override
	public int doStartTag() throws JspException {
		VelocityEngine ve = new VelocityEngine();
		JspWriter out = pageContext.getOut();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
				    ((OrgTreeTag.class.getResourceAsStream("/component_vm/SearchTreeTag.vm")))));
			VelocityContext context = new VelocityContext();
			context.put("id", id);
			context.put("height", height);
			context.put("width", width);
			context.put("checkModel", checkModel);
			context.put("onlyLeafCheckable", onlyLeafCheckable);
			
			context.put("rootVisible", rootVisible);
			context.put("rootId", rootId);
			context.put("rootName", rootName);
			context.put("onCheck", onCheck);
			context.put("sbEmptyText", sbEmptyText);
			
			if(showFreshBtn){
				context.put("freshBtn", ", '-', {iconCls: 'icon-arrow-refresh-blue',tooltip: '刷新',handler: function(){ root"+id+".reload(); },scope: this}");
				context.put("sbWidth", width+"-90");
			}else{
				context.put("freshBtn", "");
				context.put("sbWidth", width+"-65");
			}
			
			if(checkNode){
				context.put("checkNode", "baseAttrs:{uiProvider:Ext.ux.TreeCheckNodeUI},");
			}else{
				context.put("checkNode", "");
			}
			
			if(rootCheck){
				context.put("rootCheck", "checked:false,");
			}else{
				context.put("rootCheck", "");
			}
			
			if(StringUtils.isNotBlank(rootLink) && allowRootLink){//指定的地址
				context.put("rootLink", "href:'" + rootLink + "',");
			}else if(allowRootLink){//默认地址
				context.put("rootLink", "href:'" + pageContext.getServletContext().getContextPath() + "/risk/relations/relationorg/tabs.do?id="+rootId + "',");
			}else{//无连接
				context.put("rootLink", "");
			}
			
			if(expandAll){
				context.put("expandAll", id+".expandAll();");
			}else{
				context.put("expandAll", "");
			}
			
			context.put("beforeload","");
			
			context.put("searchPath", pageContext.getServletContext().getContextPath() + "/risk/relations/relationorg/getPathsBySearchName.do");
			
			if(StringUtils.isNotBlank(dataUrl)){
				context.put("dataUrl", dataUrl);
			}else{//树数据源地址
				context.put("dataUrl", pageContext.getServletContext().getContextPath() + "/risk/relations/relationorg/loadOrgTreeWithNoRandoom.do");
			}

			StringBuilder baseParams = new StringBuilder("{");
			baseParams.append("checkNode:").append(checkNode).append(",choose:'").append(choose).append("'}");
			context.put("baseParams", baseParams);
			
			StringWriter writer = new StringWriter();
			ve.evaluate(context, writer, "", br);
			out.write(writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Boolean getCheckNode() {
		return checkNode;
	}

	public void setCheckNode(Boolean checkNode) {
		this.checkNode = checkNode;
	}

	public String getCheckModel() {
		return checkModel;
	}

	public void setCheckModel(String checkModel) {
		this.checkModel = checkModel;
	}

	public Boolean getOnlyLeafCheckable() {
		return onlyLeafCheckable;
	}

	public void setOnlyLeafCheckable(Boolean onlyLeafCheckable) {
		this.onlyLeafCheckable = onlyLeafCheckable;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getOnCheck() {
		return onCheck;
	}

	public void setOnCheck(String onCheck) {
		this.onCheck = onCheck;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public Boolean getAllowRootLink() {
		return allowRootLink;
	}

	public void setAllowRootLink(Boolean allowRootLink) {
		this.allowRootLink = allowRootLink;
	}

	public String getRootLink() {
		return rootLink;
	}

	public void setRootLink(String rootLink) {
		this.rootLink = rootLink;
	}

	public Boolean getShowFreshBtn() {
		return showFreshBtn;
	}

	public void setShowFreshBtn(Boolean showFreshBtn) {
		this.showFreshBtn = showFreshBtn;
	}

	public Boolean getRootCheck() {
		return rootCheck;
	}

	public void setRootCheck(Boolean rootCheck) {
		this.rootCheck = rootCheck;
	}

	public String getSbEmptyText() {
		return sbEmptyText;
	}

	public void setSbEmptyText(String sbEmptyText) {
		this.sbEmptyText = sbEmptyText;
	}

	public Boolean getExpandAll() {
		return expandAll;
	}

	public void setExpandAll(Boolean expandAll) {
		this.expandAll = expandAll;
	}

	public Boolean getRootVisible() {
		return rootVisible;
	}

	public void setRootVisible(Boolean rootVisible) {
		this.rootVisible = rootVisible;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}
}

