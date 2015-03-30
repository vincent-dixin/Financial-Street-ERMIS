/**
 * SysUserForm.java
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

import java.util.List;
import java.util.Map;

import com.fhd.sys.entity.auth.SysUser;

/**
 * 用户Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */
public class SysUserForm extends SysUser {
	
	private static final long serialVersionUID = 1487551196355242520L;
	/**
	 * 所有的用户id集合.
	 */
	private List<String> userIds;
	/**
	 * 所有的角色id字符串.
	 */
	private String roleIds;
	/**
	 * 上一级名称，修改时默认选中值.
	 */
	private Map<String,String> parentMap;
	
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public Map<String, String> getParentMap() {
		return parentMap;
	}
	public void setParentMap(Map<String, String> parentMap) {
		this.parentMap = parentMap;
	}
		
}

