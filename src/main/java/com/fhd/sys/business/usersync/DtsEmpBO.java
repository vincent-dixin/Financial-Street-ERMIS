/**
 * DtsEmpBO.java
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.usersync.DtsEmpDao;
import com.fhd.sys.entity.usersync.DtsEmp;

/**
 * 中间表用户BO类
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午12:03:12
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class DtsEmpBO {
	
	
	@Autowired
	private DtsEmpDao o_dtsEmpDao;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	
	/**
	 * 新增用户.
	 * @author 陈建毅
	 * @param DtsEmp 用户.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public Object saveDtsEmp(DtsEmp dtsEmp) {
		DtsEmp emp = new DtsEmp();
		try {
			emp = (DtsEmp) o_dtsEmpDao.merge(dtsEmp);
		//	o_businessLogBO.saveBusinessLogInterface("新增", "中间表用户", "成功", dtsEmp.getUserName() );
		} catch (Exception e) {
			e.printStackTrace();
		//	o_businessLogBO.saveBusinessLogInterface("新增", "中间表用户", "失败", dtsEmp.getUserName());
		}
		return emp;
	}

	/**
	 * 修改用户.
	 * @author 陈建毅
	 * @param DtsEmp 用户.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void updateUser(DtsEmp dtsEmp) {
		try {
			o_dtsEmpDao.merge(dtsEmp);
			o_businessLogBO.modBusinessLogInterface("修改", "中间表用户", "成功", dtsEmp.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "中间表用户", "失败", dtsEmp.getUserName());
		}
	}
	/**
	 * 删除用户.
	 * @author 陈建毅
	 * @param id 用户id.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeUser(String id) {
		try {
			DtsEmp emp = o_dtsEmpDao.get(id);
			o_dtsEmpDao.remove(emp);
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "失败", id);
		}
	}

	/**
	 * 删除所以中间表的用户表记录方法
	 * @author 陈建毅
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAllDtsEmp() {
		try {
			o_dtsEmpDao.createQuery("delete from DtsEmp dtsEmp ").executeUpdate();
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "失败", null);
		}
	}

	/**
	 * 查询所有用户.
	 * @author 陈建毅
	 * @return List<DtsEmp> 用户集合.
	 * @since fhd　Ver 1.1
	 */
	public List<DtsEmp> queryAllUser() {
		return o_dtsEmpDao.find("from DtsEmp ");
	}

}

