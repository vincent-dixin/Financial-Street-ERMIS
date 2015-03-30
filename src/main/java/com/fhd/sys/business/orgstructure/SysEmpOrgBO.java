/**
 * SysEmpOrgBO.java
 * com.fhd.fdc.commons.business.sys.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-17 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.orgstructure;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.orgstructure.SysEmpOrgDAO;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysOrganization;



/**
 * 机构员工BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-19 
 * @since    Ver 1.1
 * @Date	 2010-9-19		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class SysEmpOrgBO {
	
	@Autowired
	private SysEmpOrgDAO o_sysEmpOrgDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * 添加机构员工的关系.
	 * @author 吴德福
	 * @param sysEmpOrg
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void save(SysEmpOrg sysEmpOrg){
		try {
			o_sysEmpOrgDAO.merge(sysEmpOrg);
			o_businessLogBO.saveBusinessLogInterface("新增", "机构员工", "成功", sysEmpOrg.getSysEmployee().getId(),sysEmpOrg.getSysOrganization().getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "机构员工", "失败", sysEmpOrg.getSysEmployee().getId(),sysEmpOrg.getSysOrganization().getId());
		}
	}
	/**
	 * 更新员工机构关系.
	 * @author 吴德福
	 * @param sysEmpOrg
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateEmpOrg(SysEmpOrg sysEmpOrg){
		try {
			o_sysEmpOrgDAO.merge(sysEmpOrg);
			o_businessLogBO.modBusinessLogInterface("修改", "机构员工", "成功", sysEmpOrg.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "机构员工", "失败", sysEmpOrg.getId());
		}
	}
	/**
	 * 根据id删除员工机构关系.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeEmoOrgById(String id){
		try {
			o_sysEmpOrgDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "机构员工", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "机构员工", "失败", id);
		}
	}
	/**
	 * 根据机构id和员工id删除机构与员工的关系.
	 * @author 吴德福
	 * @param orgid
	 * @param empid
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeEmpOrg(String orgid,String empid){
		try {
			StringBuilder hql = new StringBuilder("delete From SysEmpOrg where sysOrganization.id ='"+orgid+"' and sysEmployee.id ='"+empid+"'");
			o_sysEmpOrgDAO.createQuery(hql.toString()).executeUpdate();
			o_businessLogBO.delBusinessLogInterface("删除", "机构员工", "成功", empid);
		} catch (HibernateException e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "机构员工", "失败", empid);
		}
	}
	/**
	 * 根据员工id查询机构与员工的关系是否存在.
	 * @author 吴德福
	 * @param empid 员工id.
	 * @return List<SysEmpOrg> 机构与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpOrg> querySysEmpOrgByEmpid(String empid){
		return o_sysEmpOrgDAO.findBy("sysEmployee.id", empid);
	}
	/**
	 * 根据员工id查询员工的主机构.
	 * @author 吴德福
	 * @param empid 员工id.
	 * @return SysOrganization
	 * @since  fhd　Ver 1.1
	 */
	public SysOrganization queryMajorOrgByEmpid(String empid){
		SysOrganization org = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmpOrg.class);
		dc.add(Restrictions.eq("sysEmployee.id", empid));
		dc.add(Restrictions.eq("ismain", true));
		List<SysEmpOrg> empOrgList = o_sysEmpOrgDAO.findByCriteria(dc);
		if(null != empOrgList && empOrgList.size()>0){
			SysEmpOrg empOrg = empOrgList.get(0);
			org = empOrg.getSysOrganization();
		}
		return org;
	}
	/**根据员工id查询员工的主机构，如果没有主机构就返回最后一个机构
	 * @author 和振远
	 * @param empid
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public SysOrganization queryOrgByEmpid(String empid) throws Exception{
		SysOrganization org = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmpOrg.class);
		dc.add(Restrictions.eq("sysEmployee.id", empid));
		List<SysEmpOrg> empOrgList = o_sysEmpOrgDAO.findByCriteria(dc);
		for(SysEmpOrg empOrg : empOrgList){
			if(empOrg.getIsmain().equals(true)){
				org=empOrg.getSysOrganization();
				break;
			}
			org=empOrg.getSysOrganization();
		}
		return org;		
	}
	/**
	 * 根据机构id查询机构与员工的关系是否存在.
	 * @author 吴德福
	 * @param orgid 机构id.
	 * @return List<SysEmpOrg> 机构与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpOrg> querySysEmpOrgByOrgid(String orgid){
		return o_sysEmpOrgDAO.findBy("sysOrganization.id", orgid);
	}
	/**
	 * 根据机构id和员工id联合查询机构与员工的关系是否存在.
	 * @author 吴德福
	 * @param orgid 机构id.
	 * @param empid 员工id.
	 * @return List<SysEmpOrg> 机构与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpOrg> querySysEmpOrgByUnionid(String orgid, String empid){
		StringBuilder hql = new StringBuilder("From SysEmpOrg where sysOrganization.id='"+orgid+"' and sysEmployee.id ='"+empid+"'");
		return o_sysEmpOrgDAO.createQuery(hql.toString()).list();
	}
	/**
	 * 根据id查询员工机构关系.
	 * @author 吴德福
	 * @param id
	 * @return SysEmpOrg
	 * @since  fhd　Ver 1.1
	 */
	public SysEmpOrg querySysEmpOrgById(String id){
		return o_sysEmpOrgDAO.get(id);
	}
}

