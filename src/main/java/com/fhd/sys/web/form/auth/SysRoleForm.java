/**
 * SysRoleForm.java
 * com.fhd.fdc.commons.web.form.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.auth;

import java.util.Map;

import com.fhd.sys.entity.auth.SysRole;

/**
 * 角色Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */
public class SysRoleForm extends SysRole {

	private static final long serialVersionUID = -6350245938937074378L;
	/**
	 * 临时的角色id.
	 */
	private String roleid;
	/**
	 * 上一级名称，修改时默认选中值.
	 */
	private Map<String,String> parentMap;
	
	public Map<String, String> getParentMap() {
		return parentMap;
	}
	public void setParentMap(Map<String, String> parentMap) {
		this.parentMap = parentMap;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
}

