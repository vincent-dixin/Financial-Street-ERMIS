package com.fhd.sys.web.tag.orgstructure;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * ClassName:OrgSimpleSelectTag
 * 下拉选择框标签.
 * @author   添加注释-吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-10-26		下午05:46:41
 * @see 	 
 */
@SuppressWarnings("unchecked")
public class OrgSimpleSelectTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	//下拉选择框的id和name属性值
	private String id;
	//机构类型：1-公司，2-部门，默认-所有
	private String type;
	//公司类型：1-本公司，2-本公司以及下级公司，默认-所有 
	private String companyType;
	//样式名
	private String cssClass;
	//样式属性及对应值
	private String cssStyle;
	//已经选择的ID
	private String selectId;
	//下拉框选项改变触发事件
	private String onChangeHandler;

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		List<SysOrganization> sysOrgList = new ArrayList<SysOrganization>();
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		if(StringUtils.isNotBlank(type)){
			if(type.equals("1")){//显示公司
				String[] orgType = {"402881b22afad3b1012afae5a4200004","402881b22afad3b1012afae5e33d0005"};//总公司、分公司
				dc.add(Property.forName("orgType").in(orgType)); //不显示部门
			}
			if(type.equals("2")){ //显示部门
				dc.add(Property.forName("orgType").eq("402881b22afad3b1012afae7520f0007")); //显示部门
			}			
		}
		if(StringUtils.isBlank(companyType) || "1".equals(companyType)){//显示本公司
			dc.add(Property.forName("id").eq(UserContext.getUser().getCompanyid())); 
		}else if("2".equals(companyType)){//显示本公司以及下级公司
			dc.add(Property.forName("orgseq").like("%."+UserContext.getUser().getCompanyid()+".%")); 
		}
		dc.addOrder(Order.asc("orgseq"));
		dc.addOrder(Order.desc("sn"));
		sysOrgList = ((SysOrganizationDAO) webApplicationContext.getBean("sysOrganizationDAO")).findByCriteria(dc);
		StringBuilder sb = new StringBuilder("");
		sb.append("<select id='"+id+"' name='"+id+"' style='" + this.cssStyle + "' class='" + this.cssClass + "' onChange='" + onChangeHandler + "'>");
		String spaces = null;
		sb.append("<option value=''> --请选择--</option>");
		for(SysOrganization sysOrg : sysOrgList){
			spaces="";
			if(sysOrg.getParentOrg()!=null){
				for(int m=0;m<sysOrg.getOrgLevel();m++){
					spaces += "&nbsp;&nbsp;";
				}
				spaces+="┣";
			}
			
			sb.append("<option value='"+sysOrg.getId()+"' "+ (sysOrg.getId().equals(selectId) ? "selected":"")+">"+spaces+sysOrg.getOrgname()+"</option>");
		}
		sb.append("</select>");
		try{
			out.write(sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}
	
	public String getOnChangeHandler() {
		return onChangeHandler;
	}

	public void setOnChangeHandler(String onChangeHandler) {
		this.onChangeHandler = onChangeHandler;
	}
}
