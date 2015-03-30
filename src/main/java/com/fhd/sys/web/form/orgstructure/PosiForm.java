/**
 * PosiForm.java
 * com.fhd.fdc.commons.web.form.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-12 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.orgstructure;

import java.util.List;

import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * 岗位Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-10  
 * @since    Ver 1.1
 * @Date	 2010-9-10		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class PosiForm extends SysPosition {

	private static final long serialVersionUID = 2366865559848700210L;
	/**
	 * 机构id.
	 */
	private String orgId;
	/**
	 * 机构名称.
	 */
	private String orgName;
	/**
	 * 所有的岗位id集合.
	 */
	private List<String> posiIds;
	/**
	 * 所有的角色id字符串.
	 */
	private String roleIds;
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<String> getPosiIds() {
		return posiIds;
	}

	public void setPosiIds(List<String> posiIds) {
		this.posiIds = posiIds;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}

