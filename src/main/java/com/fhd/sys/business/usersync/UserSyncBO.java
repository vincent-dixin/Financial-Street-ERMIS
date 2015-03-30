/**
 * UserSyncBO.java
 * com.fhd.usersync.business
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

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.entity.usersync.DtsEmp;
import com.fhd.sys.entity.usersync.DtsOrg;

/**
 *用户同步功能业务操作BO类
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午01:29:54
 *
 * @see 	 
 */
@Service
public class UserSyncBO {
	
	@Autowired
	private DtsEmpBO o_dtsEmpBO;
	@Autowired
	private DtsOrgBO o_dtsOrgBo;
	@Autowired
	private DataOperateBO o_dataOperateBO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	/**
	 * 
	 * <pre>
	 * 清空中间表数据
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeAllDtsData() {
		//清空用户中间表数据
		o_dtsEmpBO.removeAllDtsEmp();
		//清空组织机构中间表数据
		o_dtsOrgBo.removeAllDtsOrg();
	} 
	
    /**
     * 
     * <pre>
     * 通过固定的规则把岗位转换成职务并且写入到DtsEmp实体中
     * </pre>
     * 
     * @author 陈建毅
     * @param dtsEmpList
     * @return
     * @since  fhd　Ver 1.1
     */
//    public List<DtsEmp>	 getDutyByPositions(List<DtsEmp>  dtsEmpList){
//    	
//    	return dtsEmpList;
//    }
    /**
     * 
     * <pre>
     * 组织机构数据写入中间表
     * </pre>
     * 
     * @author 陈建毅
     * @param dtsEmpList
     * @return
     * @since  fhd　Ver 1.1
     */
	@Transactional
    public boolean addDtsEmp(List<DtsEmp> dtsEmpList){
    	try {
			for (DtsEmp dtsEmp : dtsEmpList) {
				o_dtsEmpBO.saveDtsEmp(dtsEmp);
			}
			o_businessLogBO.saveBusinessLogInterface("新增", "中间表用户", "成功" );
			return true;
		} catch (Exception e) {
			o_businessLogBO.saveBusinessLogInterface("新增", "中间表用户", "失败" );
			return false;
		}
    }
    /**
     * 
     * <pre>
     * 用户数据写入中间表
     * </pre>
     * 
     * @author 陈建毅
     * @param dtsOrgList
     * @return
     * @since  fhd　Ver 1.1
     */
	@Transactional
    public boolean addDtsOrg(List<DtsOrg> dtsOrgList){
    	try {
			for (DtsOrg dtsOrg : dtsOrgList) {
				o_dtsOrgBo.saveDtsOrg(dtsOrg);
			}
			o_businessLogBO.saveBusinessLogInterface("新增", "中间表组织机构", "成功" );
			return true;
		} catch (Exception e) {
			o_businessLogBO.saveBusinessLogInterface("新增", "中间表组织机构", "失败" );
			return false;
		}
    }
    
    
    /**
	 * 
	 * <pre>
	 * 调用中间表向大平台转换前的校验方法
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @return
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public boolean  checkDtsData() throws SQLException  {
		return o_dataOperateBO.checkDtsData();
	}
	
	/**
	 * 
	 * <pre>
	 * 调用中间表向大平台转换前的清理数据方法
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @return
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	
	public boolean clearDtsData()   {
		return o_dataOperateBO.clearDtsData();
	}
	/**
	 * 
	 * <pre>
	 * 中间表数据写入大平台，并做好初始化和相关数据的插入
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	
	public void syncSysDatas() throws SQLException{
		o_dataOperateBO.OperateOrgAndEmp();
	}
    
}

