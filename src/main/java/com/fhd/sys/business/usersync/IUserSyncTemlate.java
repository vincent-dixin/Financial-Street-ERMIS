/**
 * UserSyncTemlate.java
 * com.fhd.usersync.template
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-10-18 	陈建毅
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.usersync;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.entity.usersync.DtsEmp;
import com.fhd.sys.entity.usersync.DtsOrg;

/**
 * 用户同步模板类.
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午01:33:47
 * @see 	 
 */
@Service
public abstract class IUserSyncTemlate {
	
	@Autowired
	private UserSyncBO o_userSyncBO;
	
	/**
	 * 清空中间表数据.
	 * @author 陈建毅
	 * @since  fhd　Ver 1.1
	 */
	public void removeAllDtsData() {
		o_userSyncBO.removeAllDtsData();
	} 
	
	/**
	 * 客户化方法（抽取用户信息的接口 ）.
	 * @author 陈建毅
	 * @since  fhd　Ver 1.1
	 */
	abstract protected List<DtsEmp> getDtsEmps();
	
	/**
	 * 客户化方法（抽取组织结构信息的接口 ）.
	 * @author 陈建毅
	 * @since  fhd　Ver 1.1
	 */
	abstract protected List<DtsOrg> getDtsOrgs();
	
    /**
     * 通过固定的规则把岗位转换成职务并且写入到DtsEmp实体中.
     * @author 陈建毅
     * @param dtsEmpList
     * @return
     * @since  fhd　Ver 1.1
     */
//  public List<DtsEmp>	 getDutyByPositions(List<DtsEmp>  dtsEmpList){
//  	return o_userSyncBO.getDutyByPositions(dtsEmpList);
//  }
	
    /**
     * 组织机构数据写入中间表.
     * @author 陈建毅
     * @param dtsEmpList
     * @return boolean
     * @since  fhd　Ver 1.1
     */
    public boolean addDtsEmp(List<DtsEmp> dtsEmpList){
    	return o_userSyncBO.addDtsEmp(dtsEmpList);
    }
    
    /**
     * 用户数据写入中间表.
     * @author 陈建毅
     * @param dtsOrgList
     * @return boolean
     * @since  fhd　Ver 1.1
     */
    public boolean addDtsOrg(List<DtsOrg> dtsOrgList){
    	return o_userSyncBO.addDtsOrg(dtsOrgList);
    }
    
    /**
	 * 调用中间表向大平台转换前的校验方法.
	 * @author 陈建毅
	 * @return boolean
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public boolean  checkDtsData() throws SQLException  {
		return o_userSyncBO.checkDtsData();
	}
	
	/**
	 * 调用中间表向大平台转换前的清理数据方法.
	 * @author 陈建毅
	 * @return boolean
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public boolean clearDtsData()   {
		return o_userSyncBO.clearDtsData();
	}
	
	/**
	 * 中间表数据写入大平台，并做好初始化和相关数据的插入.
	 * @author 陈建毅
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public void syncSysDatas() throws SQLException{
		o_userSyncBO.syncSysDatas();
	}
    
	/**
	 * 执行用户同步方法（总调方法）.
	 * @author 陈建毅
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void operateUserSync() throws SQLException{
		// 调用总体业务BO清空中间表方法
		this.removeAllDtsData();
		// 客户化方法（抽取用户信息的接口 ）
		List<DtsEmp> dtsEmpList=this.getDtsEmps();
		List<DtsOrg> dtsOrgList=this.getDtsOrgs();
		
		//写入中间表方法
		this.addDtsEmp(dtsEmpList);
		this.addDtsOrg(dtsOrgList);
		//调用中间表向大平台转化时的校验方法
		this.checkDtsData();
		//调用中间表向大平台转化时的清空方法
		this.clearDtsData();
		//调用中间表向大平台转化时的插入方法
		this.syncSysDatas();
	}
}

