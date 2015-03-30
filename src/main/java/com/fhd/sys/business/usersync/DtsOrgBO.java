/**
 * DtsOrgBO.java
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.usersync.DtsOrgDao;
import com.fhd.sys.entity.usersync.DtsOrg;

/**
 * 中间表组织机构BO类
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		下午12:03:01
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class DtsOrgBO {

	@Autowired
	private DtsOrgDao o_dtsOrgDao;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	
	/**
	 * 新增组织机构.
	 * @author 陈建毅
	 * @param DtsOrg 组织机构.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public Object saveDtsOrg(DtsOrg dtsOrg) {
		DtsOrg org = new DtsOrg();
		try {
			org = (DtsOrg) o_dtsOrgDao.merge(dtsOrg);
		
			//o_businessLogBO.saveBusinessLogInterface("新增", "中间表组织机构", "成功", dtsOrg.getOrgName() );
		} catch (Exception e) {
			e.printStackTrace();
			//o_businessLogBO.saveBusinessLogInterface("新增", "中间表组织机构", "失败", dtsOrg.getOrgName());
		}
		return org;
	}

	/**
	 * 修改组织机构.
	 * @author 陈建毅
	 * @param DtsOrg 组织机构.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void updateUser(DtsOrg dtsOrg) {
		try {
			o_dtsOrgDao.merge(dtsOrg);
			//o_businessLogBO.modBusinessLogInterface("修改", "中间表组织机构", "成功", dtsOrg.getOrgName());
		} catch (Exception e) {
			e.printStackTrace();
			//o_businessLogBO.modBusinessLogInterface("修改", "中间表组织机构", "失败", dtsOrg.getOrgName());
		}
	}
	/**
	 * 删除组织机构.
	 * @author 陈建毅
	 * @param id 组织机构id.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeDtsOrg(String id) {
		try {
			DtsOrg org = o_dtsOrgDao.get(id);
			o_dtsOrgDao.remove(org);
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "失败", id);
		}
	}
	
	/**
	 * <pre>
	 * removeDtsOrgs:删除组织机构临时表数据
	 * </pre>
	 * 
	 * @author David
	 * @param dtsOrgs
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeDtsOrgs(List<DtsOrg> dtsOrgs) {
		if(dtsOrgs==null)
			return;
		
		List<String> removeIds = new ArrayList<String>();
		for(DtsOrg dtsOrg:dtsOrgs)
			removeIds.add(dtsOrg.getId());
		String ids = StringUtils.join(removeIds.iterator(), ",");
		
		try {
			o_dtsOrgDao.removeAll(dtsOrgs);
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "成功", ids);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "失败", ids);
		}
	}

	/**
	 * 删除所以组织机构表记录方法
	 * @author 陈建毅
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAllDtsOrg() {
		try {
			o_dtsOrgDao.createQuery("delete from DtsOrg dtsOrg ").executeUpdate();
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "组织机构", "失败", null);
		}
	}

	/**
	 * 查询所有组织机构.
	 * @author 陈建毅
	 * @return List<DtsOrg> 组织机构集合.
	 * @since fhd　Ver 1.1
	 */
	public List<DtsOrg> queryAllUser() {
		return o_dtsOrgDao.find("from DtsOrg ");
	}
}

