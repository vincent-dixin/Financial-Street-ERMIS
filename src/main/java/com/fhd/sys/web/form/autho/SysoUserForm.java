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

import java.util.Set;

import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * @author   翟辉
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:17:33
 *
 * @see 	 
 */

public class SysoUserForm extends SysRole{
	
	/**
	 * @author 翟辉
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -7173787048286039090L;

	private String username;//用户名
	
	private String realname;//用户真实姓名
	
	private String rolename;//角色名称
	
	private String roleId;//角色ID
	
	private String usernameId;//用户ID
	
	private String post;//岗位名称
	
	private String organizationName;//部门名称
	
	private String companyname;//公司
	
	private String empname;//员工姓名

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getUsernameId() {
		return usernameId;
	}

	public void setUsernameId(String usernameId) {
		this.usernameId = usernameId;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getUsername() {
		return username;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * TODO（RoleForm）
	 *
	 * @author 翟辉
	 * @since  fhd　Ver 1.1
	 */

	public SysoUserForm(String posiName,String deptName,String username,String empname,String companyname,String roleId,String userId){
		this.username = username;//用户名
		//this.realname = 
		this.usernameId = userId;//用户Id
		this.organizationName = deptName;//部门
		this.post = posiName;//岗位
		this.roleId = roleId;//角色Id
		this.companyname = companyname;//公司
		this.empname = empname;//员工
	}	
}

