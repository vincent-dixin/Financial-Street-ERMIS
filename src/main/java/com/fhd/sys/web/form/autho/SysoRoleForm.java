/**
 * I18nForm.java
 * com.fhd.sys.web.form.i18n
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * I18nForm.java
 * com.fhd.sys.web.form.i18n
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.form.autho;
import com.fhd.sys.entity.auth.SysRole;

/**

 * @author   翟辉
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:17:33
 *
 * @see 	 
 */

public class SysoRoleForm extends SysRole{
	
	/**
	 * @author 翟辉
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * TODO（角色表单）
	 *
	 * @author 翟辉
	 * @since  fhd　Ver 1.1
	 */
	
	public SysoRoleForm(){
		
	}
	public SysoRoleForm(SysRole sysRole){
		this.setId(sysRole.getId());
		this.setRoleCode(sysRole.getRoleCode());
		this.setRoleName(sysRole.getRoleName());
	}
	/**
	 * 角色编号
	 */
	private String roleCode;
	/**
	 * 角色名称.
	 */
	private String roleName;
	/**
	 * 角色对应的人员.
	 */
	private String addPersonJson;
	
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAddPersonJson() {
		return addPersonJson;
	}
	public void setAddPersonJson(String addPersonJson) {
		this.addPersonJson = addPersonJson;
	}
	
	
}

