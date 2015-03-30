/**
 * DataOperateDao.java
 * com.fhd.usersync.dao
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-10-18 	陈建毅
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.usersync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.fdc.commons.orm.sql.SqlBuilder;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 大平台用户和组织机构和中间表转换DAO
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午02:16:00
 *
 * @see 	 
 */
@Repository
public class DataOperateDao extends HibernateEntityDao<SysOrganization>{
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
	public ResultSet checkDtsData() throws SQLException {
		   Connection connection = SessionFactoryUtils.getDataSource(
					getSessionFactory()).getConnection();
		   PreparedStatement prepareStatement = connection
					.prepareStatement(SqlBuilder.getSql(
							"select_UserSyncCheckDtsData", null));
		  return prepareStatement.executeQuery();
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
		 try {
			 Connection connection = SessionFactoryUtils.getDataSource(
						getSessionFactory()).getConnection();
			//组织机构清理
			sqlExecute(connection,"delete_clearDtsOrg");
			 //用户表清理
			 sqlExecute(connection,"delete_clearDtsEmp");
			 return true;
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 机构和机构相关的数据处理
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @return
	 * @throws SQLException 
	 * @since  fhd　Ver 1.1
	 */
	public boolean OperateOrgAndEmp() throws SQLException   {
		
	
			Connection connection = SessionFactoryUtils.getDataSource(
					getSessionFactory()).getConnection();
			
			
			try {
				//1全部设为注销 
				sqlExecute(connection,"update_orgLogout");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "全部设为注销", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//2更新已有数据 
				sqlExecute(connection,"update_existDatas");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "更新已有数据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//3插入新增数据
				sqlExecute(connection,"insert_newDatas");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "插入新增数据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//4修正公司类型
				sqlExecute(connection,"update_companyType");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "修正公司类型", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//5为各公司初始化必须的数据
				//a,风险
				sqlExecute(connection,"insert_risksCompany");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "为各公司初始化必须的数据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//b,指标
				sqlExecute(connection,"insert_KPICompany");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "指标", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//c,变量 
				sqlExecute(connection,"insert_variableCompany");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "变量", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//d,资产
				sqlExecute(connection,"insert_assetsCompany");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "资产", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//e,流程
				sqlExecute(connection,"insert_processureCompany");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "流程", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//6人员和人员相关
				//岗位
				//增加部门下不存在的岗位名称
				sqlExecute(connection,"insert_newPosition");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "增加部门下不存在的岗位名称据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//操作员
				sqlExecute(connection,"update_user");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "操作员", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				sqlExecute(connection,"insert_user");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "操作员用户表数据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//员工
				sqlExecute(connection,"update_emp");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "员工表数据", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//人员关联职务
				//如果来源数据没有职务，不更新；如果目标数据已经有职务，不更新
				sqlExecute(connection,"update_duty");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "人员关联职务", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				sqlExecute(connection,"insert_sysEmployee");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "人员关联职务", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//人员关联岗位
				//如果没有岗位，不更新；只插入，不删除
				sqlExecute(connection,"insert_sysEmpPost");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("更新", "人员关联岗位", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			try {
				//人员关联部门
				//只插入，不删除
				sqlExecute(connection,"insert_sysEmpOrg");
			} catch (SQLException e) {
				o_businessLogBO.saveBusinessLogInterface("新增", "人员关联岗位", "失败" );
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		    return true;
		
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 执行原始SQL通用方法
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @param sqlName
	 * @return
	 * @throws SQLException
	 * @since  fhd　Ver 1.1
	 */
	public boolean sqlExecute(Connection connection,String sqlName) throws SQLException{
		PreparedStatement prepareStatement = connection
		.prepareStatement(SqlBuilder.getSql(
				sqlName, null));
		return prepareStatement.execute();
	}
}

