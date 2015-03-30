/**
 * OrganizationForm.java
 * com.fhd.sys.web.form.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-14 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.organization;

import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-14		下午5:38:41
 *
 * @see 	 
 */
public class OrganizationForm extends SysOrganization{

	private static final long serialVersionUID = 1L;
	
	private String parentOrgStr;//上级机构名称
	private String startDataStr;//开始时间
	private String endDataStr;//结束时间
	private String snStr;//排列顺序
	
	public OrganizationForm(){
		
	}

	public  OrganizationForm(SysOrganization org){
		this.setId(org.getId());
		this.setOrgcode(org.getOrgcode());
		this.setOrgname(org.getOrgname());
		//上级机构名称
		this.parentOrgStr = null == org.getParentOrg()?"":org.getParentOrg().getOrgname();
		//机构层级与上级机构关联
		if(null != org.getParentOrg()){
			this.setOrgLevel(org.getParentOrg().getOrgLevel()+1);
		}else{
			this.setOrgLevel(1);
		}
		//this.setOrgLevel(org.getOrgLevel());
		
		//this.setOrgseq(org.getOrgseq());
		this.setOrgType(org.getOrgType());
		this.setForum(org.getForum());
		this.setRegion(org.getRegion());
		this.setAddress(org.getAddress());
		this.setZipcode(org.getZipcode());
		this.setLinkTel(org.getLinkTel());
		this.setLinkMan(org.getLinkMan());
		this.setEmail(org.getEmail());
		this.setWeburl(org.getWeburl());
		//this.setStartDate(org.getStartDate());
		//this.setEndDate(org.getEndDate());
		this.setOrgStatus(org.getOrgStatus());
		this.setSn(org.getSn());
		this.setRemark(org.getRemark());
	}
	
	public String getParentOrgStr() {
		return parentOrgStr;
	}

	public void setParentOrgStr(String parentOrgStr) {
		this.parentOrgStr = parentOrgStr;
	}

	public String getStartDataStr() {
		return startDataStr;
	}

	public void setStartDataStr(String startDataStr) {
		this.startDataStr = startDataStr;
	}

	public String getEndDataStr() {
		return endDataStr;
	}

	public void setEndDataStr(String endDataStr) {
		this.endDataStr = endDataStr;
	}

	public String getSnStr() {
		return snStr;
	}

	public void setSnStr(String snStr) {
		this.snStr = snStr;
	}

	
	
}

