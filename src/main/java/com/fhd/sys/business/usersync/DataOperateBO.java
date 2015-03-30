/**
 * DataOperateBO.java
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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.usersync.DataOperateDao;

/**
 * 中间表转入大平台数据操作BO类
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午01:27:26
 *
 * @see 	 
 */
@Service
public class DataOperateBO {
	
	
	@Autowired
	private DataOperateDao o_dataOperateDao;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
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
	public boolean  checkDtsData() throws SQLException {
		ResultSet rs=o_dataOperateDao.checkDtsData();
		StringBuffer sbf=new StringBuffer();
		int count=0;
		while (rs.next()) {
			sbf.append("，").append(rs.getString("results"));
			count++;
		}
		if(count==0){
			o_businessLogBO.saveBusinessLogInterface("用户同步校验", "中间表数据校验", "成功" );
		}else{
			o_businessLogBO.saveBusinessLogInterface("用户同步校验", "中间表数据校验", "失败",sbf.toString().substring(1) );
		}
		return true;
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
	@Transactional
	public boolean clearDtsData()   {
		boolean rvalue=o_dataOperateDao.clearDtsData();
		o_businessLogBO.saveBusinessLogInterface("清理数据", "中间表", "成功" );
		return rvalue;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 机构和用户相关的数据处理
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @return
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public boolean OperateOrgAndEmp() throws SQLException   {
		boolean rvalue=o_dataOperateDao.OperateOrgAndEmp();
		o_businessLogBO.saveBusinessLogInterface("转换", "机构和用户表数据", "成功" );
		return rvalue;
	}
	
	
	
}

