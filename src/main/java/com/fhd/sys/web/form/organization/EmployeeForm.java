/**
 * EmployeeForm.java
 * com.fhd.sys.web.form.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-21 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.organization;

import java.util.HashSet;
import java.util.Set;

import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   王再冉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-21		上午11:29:59
 *
 * @see 	 
 */
public class EmployeeForm extends SysEmployee{
	private static final long serialVersionUID = 1L;

	private String orgStrMain;//员工所属主部门
	private String orgStrs;//辅助部门
	private String companyOrg;//员工所属机构
	private String dutyStr;//员工职务
	private String roleName;//员工角色
	private String birthDateStr;//生日
	private String regdateStr;//注册时间
	
	private String userPassword;//用户密码
	private String userState;//用户状态
	private Boolean lockstate=false;//用户锁定状态
	private Boolean enable=false;//是否启用
	private String userLastLoginTimeStr;//用户最后登录时间
	private String userCredentialsexpiryDateStr;//用户密码过期时间
	

	public EmployeeForm(){
		
	}
	
	public EmployeeForm(SysEmployee emp ,Set<SysRole> roleList,String orgPart,Set<SysEmpOrg> empOrgSet){
		Set<String> roleSet = new HashSet<String>();
		Set<String> empOrgArr = new HashSet<String>();
		
		this.setId(emp.getId());
		this.setEmpcode(emp.getEmpcode());
		this.setUsername(emp.getUsername());
		this.setEmpname(emp.getEmpname());
		this.setRealname(emp.getRealname());
		this.setGender(emp.getGender());
		//this.setBirthdate(emp.getBirthdate());
		//所属部门
		if(null != orgPart){
			this.orgStrMain = orgPart;
		}
		//所属机构
		if(null != emp.getSysOrganization()){
			SysOrganization org = emp.getSysOrganization();
			//SysOrganization company = org.getParentOrg();
			this.companyOrg = org.getOrgname();
		}
		//辅助部门 
		if(empOrgSet.size()>0){
			for(SysEmpOrg empOrg : empOrgSet){
				if(null != empOrg.getIsmain()){
					if(!empOrg.getIsmain()){
						empOrgArr.add(empOrg.getSysOrganization().getOrgname());
					}
					String empOrgString = empOrgArr.toString().substring(1, empOrgArr.toString().length()-1);
					this.orgStrs = empOrgString;
				}else{
					this.orgStrs = " ";
				}
			}
			
		}
		//角色
		if(roleList.size()>0){
			for(SysRole role : roleList){
				roleSet.add(role.getRoleName());
			}
			String roleStr = roleSet.toString().substring(1, roleSet.toString().length()-1);
			this.roleName = roleStr;
		}
		//职务
		this.dutyStr=null==emp.getDuty()?"":emp.getDuty().getDutyName();
		this.setEmpStatus(emp.getEmpStatus());
		this.setOtel(emp.getOtel());
		this.setOaddress(emp.getOaddress());
		this.setOemail(emp.getOemail());
		this.setOzipcode(emp.getOzipcode());
		this.setFaxno(emp.getFaxno());
		this.setCardno(emp.getCardno());
		this.setCardtype(emp.getCardtype());
		this.setMobikeno(emp.getMobikeno());
		this.setMsn(emp.getMsn());
		this.setHaddress(emp.getHaddress());
		this.setHtel(emp.getHtel());
		this.setHzipcode(emp.getHzipcode());
		this.setPemail(emp.getPemail());
		this.setParty(emp.getParty());
		this.setMajor(emp.getMajor());
		this.setDegree(emp.getDegree());
		this.setSpecialty(emp.getSpecialty());
		//this.setRegdate(emp.getRegdate());
		this.setRemark(emp.getRemark());
		//用户
		if(null!=emp.getSysUser()){
			this.setUserid(emp.getUserid());
			this.userPassword=emp.getSysUser().getPassword();
			this.userState=emp.getSysUser().getUserStatus();
			this.lockstate=emp.getSysUser().getLockstate();
			this.enable=emp.getSysUser().getEnable();
		}else{
			this.userPassword="";
			this.userState = "";
			this.lockstate = false;
			this.enable = false;
		}
		
	}
	
	
	public String getOrgStrs() {
		return orgStrs;
	}

	public void setOrgStrs(String orgStrs) {
		this.orgStrs = orgStrs;
	}

	public String getRegdateStr() {
		return regdateStr;
	}

	public void setRegdateStr(String regdateStr) {
		this.regdateStr = regdateStr;
	}

	public String getBirthDateStr() {
		return birthDateStr;
	}

	public void setBirthDateStr(String birthDateStr) {
		this.birthDateStr = birthDateStr;
	}
	

	public String getOrgStrMain() {
		return orgStrMain;
	}

	public void setOrgStrMain(String orgStrMain) {
		this.orgStrMain = orgStrMain;
	}

	public String getDutyStr() {
		return dutyStr;
	}

	public void setDutyStr(String dutyStr) {
		this.dutyStr = dutyStr;
	}
	//用户相关字串
	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getUserLastLoginTimeStr() {
		return userLastLoginTimeStr;
	}

	public void setUserLastLoginTimeStr(String userLastLoginTimeStr) {
		this.userLastLoginTimeStr = userLastLoginTimeStr;
	}

	public String getUserCredentialsexpiryDateStr() {
		return userCredentialsexpiryDateStr;
	}

	public void setUserCredentialsexpiryDateStr(String userCredentialsexpiryDateStr) {
		this.userCredentialsexpiryDateStr = userCredentialsexpiryDateStr;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public Boolean getLockstate() {
		return lockstate;
	}

	public void setLockstate(Boolean lockstate) {
		this.lockstate = lockstate;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getCompanyOrg() {
		return companyOrg;
	}

	public void setCompanyOrg(String companyOrg) {
		this.companyOrg = companyOrg;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
}

