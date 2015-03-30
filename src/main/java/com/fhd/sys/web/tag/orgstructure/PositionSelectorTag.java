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

import com.fhd.sys.dao.orgstructure.SysPositionDAO;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * ClassName:OrgSelector
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David.Niu
 * @version  
 * @since    Ver 1.1
 * @Date	 2010-10-11		上午10:23:50
 *
 * @see 	 
 */

public class PositionSelectorTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	//是否多选
	private boolean multiple = false;
	
	//窗口标题
	private String title = "选择岗位";
	
	//窗口宽度
	private int width = 606;
	
	//窗口高度
	private int height = 400;
	
	//已经选择的岗位列表
	private String choosedPositions;
	
	//弹出窗口样式,系统集成"default","extjs","skyblue","silvergray"
	private String cssClass = "extjs";
	
	private String attributeName;
	
	//是否有复选框
	private boolean checkNode = false;
	
	//是否只是叶子节点加复选框
	private boolean onlyLeafCheckable = true;
	
	//多选: 'multiple'(默认)、单选: 'single'、级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	private String checkModel = "multiple";
	
	/**
	 * 展现方式
	 * 1、checkType：选择方式(带有radio或者checkbox,默认)
	 * 2、textFiledType：文本框方式
	 */
	private String showType = "checkType";
	
	//显示宽度
	private Integer objectSize = 20;
	
	//所属公司ID属性
	private String companyIdProperty;
	
	private int cmWidth = 300;//组件宽度
	
	private int cmHeight = 90;//组件高度
	
	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String contextPath = request.getContextPath();
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		StringBuilder sb = new StringBuilder();
		//generate scripts
		sb.append("<script type=\"text/javascript\" src=\"" + contextPath + "/scripts/risktag.js\"></script>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contextPath + "/css/tags.css\"/>");
		
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		SysPositionDAO poiEntryBean = (SysPositionDAO) webApplicationContext.getBean("sysPositionDAO");
		
		sb.append("<script type=\"text/javascript\">");
		sb.append("var tipsGroupMgr = new Ext.WindowGroup();tipsGroupMgr.zseed=1;");
		sb.append("</script>");
		
		//带radio或者checkbox的展现方式
		if("checkType".equalsIgnoreCase(showType)){
			if(multiple){//多选
				List<SysPosition> positions = new ArrayList<SysPosition>();
				if(StringUtils.isNotBlank(choosedPositions)){
					String queryStr[] = choosedPositions.split(",");
					StringBuilder stringBuilder = new StringBuilder("from SysPosition sp where sp.id in (");
					for(int i=0;i<queryStr.length;i++){
						stringBuilder.append("'").append(queryStr[i]).append("'");
						if(i!=queryStr.length-1)
							stringBuilder.append(",");
					}
					stringBuilder.append(")");
					
					positions = poiEntryBean.find(stringBuilder.toString());
				}
				sb.append("<script type=\"text/javascript\">")
				.append("function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/getRootOrg.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel + "&tag=" + attributeName + "');}")
				.append("</script>");
				
				sb.append("<div style='width:100%; height:20px;'><div style='cursor:pointer;width:5px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_left.gif);float:left;'></div><div onclick='" + attributeName + "function()' style='cursor:pointer;float:left; width:"+(cmWidth-16)+"px; height:20px;background-image:url(" + contextPath +"/images/btn/cm_middle.gif);font-size:12px;vertical-align:bottom; text-align:center;'>选择/删除</div><div style='width:11px; height:20px; background-image:url(" + contextPath +"/images/btn/cm_right.gif);float:left;cursor:pointer'></div><input type='hidden' id='check_").append(attributeName).append("' name='check_").append(attributeName).append("' value='");
				if (positions!=null && positions.size()>0) {
					sb.append("1");
				}
				sb.append("' /></div>")
				.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='width: "+cmWidth+";height:"+cmHeight+";overflow-x:auto;overflow-y:auto;border: 1px solid #CCDFF0;font-size:12px;'>");
				
				
				if(positions!=null && positions.size()>0){
					for(SysPosition position:positions)
						sb.append("<div id='" + position.getId() + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedValues(\"" + attributeName + "\",\"" + position.getId() + "\")' name='" + attributeName + "' value='"+ position.getId() + "' />&nbsp;&nbsp;&nbsp;<span ext:qtip='"+position.getPosiname()+"--"+position.getSysOrganization().getOrgname()+"'>"+ position.getPosiname() +"</span></div>");
				}
				
				sb.append("</div>");
			}else{//单选
				sb.append("<script type=\"text/javascript\">")
				.append("function " + attributeName + "function(){openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/getRootOrg.do?type=single&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel + "&tag=" + attributeName + "');}")
				.append("</script>");
				//sb.append("<div id='div" + attributeName + "' class='fhd_selected_org'>");
				sb.append("<div id='div" + attributeName + "' name='div" + attributeName + "' style='border: 1px solid #CCDFF0;width:"+cmWidth+";height:20px;float:left;font-size:12px;'>");
				if(StringUtils.isNotBlank(choosedPositions)){
					String queryStr[] = choosedPositions.split(",");
					SysPosition position = poiEntryBean.get(queryStr[0]);
					if(position!=null){
						sb.append("<input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + attributeName + "\")' name='" + attributeName + "' value='"+ position.getId() + "," + position.getPosiname() + "' />&nbsp;&nbsp;&nbsp;<span ext:qtip='"+position.getPosiname()+"'>"+position.getPosiname()+"</span>");
					}
				}
				sb.append("</div><div class='fhd_selected_org_radio'>&nbsp;<input type='button' value='请选择' onclick='" + attributeName + "function()' class='fhd_tagMS_Btn'/></div>");
			}
			//带文本框的展现方式
		}else if("textFiledType".equalsIgnoreCase(showType)){
			sb.append("<script type=\"text/javascript\">");
			sb.append("function " + attributeName + "function(){");
			if(StringUtils.isNotBlank(companyIdProperty)){
				sb.append("var ").append(attributeName).append("CompanyId=document.getElementById('").append(companyIdProperty).append("').value;");
			}
			sb.append("openWindow('" + title + "'," + width + "," + height + ",'" + basePath + "sys/orgstructure/org/getRootOrg.do?type=mutiple&checkNode=" + checkNode + "&onlyLeafCheckable=" + onlyLeafCheckable + "&checkModel=" + checkModel + "&tag=" + attributeName
					 + "&showType=" + showType);
			if(StringUtils.isNotBlank(companyIdProperty))
				sb.append("&companyId='+").append(attributeName+"CompanyId");
			sb.append(");}");
			sb.append("</script>");
			StringBuilder orgNames = new StringBuilder();
			
			//获取数据
			List<SysPosition> positions = null;//数据
			if (StringUtils.isNotBlank(choosedPositions)) {
				String queryStr[] = choosedPositions.split(",");
				StringBuilder stringBuilder = new StringBuilder("from SysPosition sp where sp.id in (");
				for(int i=0;i<queryStr.length;i++){
					stringBuilder.append("'").append(queryStr[i]).append("'");
					if(i!=queryStr.length-1)
						stringBuilder.append(",");
				}
				stringBuilder.append(")");
				
				positions = poiEntryBean.find(stringBuilder.toString());
			}
			
			//生成隐藏部分
			sb.append("<div id='div" + attributeName + "' style='display:none'>");
			if(positions!=null && positions.size()>0){
				for (SysPosition position : positions){
					String tempId = position.getId()+RandomUtils.nextInt(9999);
					sb.append("<div id='" + tempId + "'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedPositions(\"" + attributeName + "\",\"" + tempId + "\")' name='" + attributeName + "' value='" + position.getId() + "' /><span ext:qtip='"+position.getPosiname()+"'>" + position.getPosiname() + "</span></div>");
					orgNames.append(position.getPosiname()).append(",");
				}
					
			}
			sb.append("</div>");
			
			//生成textfiled和button
			String riskNameStr = orgNames.toString().length()>0?orgNames.toString().substring(0,orgNames.toString().length()-1):"";
			sb.append("<input type='text' id='textFiledType" + attributeName + "' value='" + riskNameStr + "'/>&nbsp;<input type='button' id='u' value='请选择' onclick='" + attributeName + "function()' class='fhd_btn'/>");
		}
		
		
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


	public String getChoosedPositions() {
		return choosedPositions;
	}

	public void setChoosedPositions(String choosedPositions) {
		this.choosedPositions = choosedPositions;
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

