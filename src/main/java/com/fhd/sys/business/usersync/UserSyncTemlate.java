/**
 * UserSyncTempImpl.java
 * com.fhd.fdc.commons.business.usersync
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-10-23 	陈建毅
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.usersync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fhd.sys.entity.usersync.DtsEmp;
import com.fhd.sys.entity.usersync.DtsOrg;

/**
 * ClassName:UserSyncTempImpl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-23		上午09:20:40
 *
 * @see 	 
 */
@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class UserSyncTemlate  extends IUserSyncTemlate{

	
	
	
	/**
	 * 
	 * <pre>
	 * 配置数据库连接（此方法根据不同项目需要 ，编写）
	 * </pre>
	 * 
	 * @author 陈建毅
	 * @return
	 * @throws SQLException
	 * @since  fhd　Ver 1.1
	 */
	public Connection  getConnection() throws SQLException
	{
		//相关数据库配置
		String dbURL;
		String name="sa";//输入SQL Server 2005登录名
		String pass="fhdadmin";//输入SQL Server 2005密码
		String server="127.0.0.1:1433;DatabaseName=ermis4_test3";//输入要登录的数据库服务器的地址
		String instance="";//输入要连接到的数据库实例(默认不填)
		if(instance.trim().equals("")){
			dbURL= "jdbc:sqlserver://"+server;
		}else{
			dbURL="jdbc:sqlserver://"+server+"\\"+instance;
		}
		Connection con=null; 
		try { 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbURL,name,pass);
		}catch (Exception e){ 
			e.printStackTrace(); 
		}
		return con;
	}
	

	@Override
	protected List<DtsEmp> getDtsEmps()  {
		
		Connection connection =null;
		PreparedStatement prepareStatement  = null;
		try {
			connection = this.getConnection();
			String sql = "select * from TMP_USER";
			prepareStatement = connection.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();
			List<DtsEmp> dtsEmpList = new ArrayList();
			while (rs.next()) {
				DtsEmp dtsEmp = new DtsEmp();
				dtsEmp.setDuty(rs.getString("userduty")==null?" ":rs.getString("userduty"));
				dtsEmp.setId(rs.getString("id"));
				dtsEmp.setOrg(rs.getString("departmentid")==null?"":rs.getString("departmentid"));
				dtsEmp.setPosition(rs.getString("posts")==null?" ":rs.getString("posts"));
				dtsEmp.setUserId(rs.getString("username")==null?"":rs.getString("username"));
				dtsEmp.setUserName(rs.getString("empname")==null?"":rs.getString("empname"));
				dtsEmpList.add(dtsEmp);
			}
			return dtsEmpList;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				prepareStatement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		return null;
		
	}

	@Override
	protected List<DtsOrg> getDtsOrgs() {
		
		
		PreparedStatement prepareStatement  = null;
		Connection connection =null;
		try {
			connection = this.getConnection();
			String sql = "select * from TMP_ORG";
			prepareStatement = connection.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();
			List<DtsOrg> dtsOrgList = new ArrayList();
			while (rs.next()) {
				DtsOrg dtsOrg = new DtsOrg();
				dtsOrg.setId(rs.getString("orgid"));
				dtsOrg.setEsort(rs.getString("sort")==null?"":rs.getString("sort"));
				
				dtsOrg.setOrgCode(rs.getString("orgcode")==null?rs.getString("orgid"):rs.getString("orgcode"));
				dtsOrg.setOrgName(rs.getString("orgname")==null?"":rs.getString("orgname"));
				String orgtype="";
				if("1".equals(rs.getString("orgtype"))){
					orgtype="402881b22afad3b1012afae5a4200004";
				}else if("2".equals(rs.getString("orgtype"))){
					orgtype="402881b22afad3b1012afae799c60008";
				}
				dtsOrg.setOrgType(orgtype);
				if(!"0".equals(rs.getString("parentorgid"))){
				dtsOrg.setParentId(rs.getString("parentorgid"));
				}
				dtsOrg.setEmp("");
				dtsOrgList.add(dtsOrg);
			}
			return dtsOrgList;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				prepareStatement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		return null;
		
	}

}

