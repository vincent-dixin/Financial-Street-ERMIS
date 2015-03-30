/**
 * SysEmpPosiBO.java
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

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.orgstructure.SysEmpPosiDAO;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysPosition;


/**
 * 岗位员工BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-19 
 * @since    Ver 1.1
 * @Date	 2010-9-19		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class SysEmpPosiBO {

	@Autowired
	private SysEmpPosiDAO o_sysEmpPosiDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * 添加机构员工的关系.
	 * @author 吴德福
	 * @param sysEmpPosi
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void save(SysEmpPosi sysEmpPosi){
		try {
			o_sysEmpPosiDAO.merge(sysEmpPosi);
			o_businessLogBO.saveBusinessLogInterface("新增", "岗位员工", "成功", sysEmpPosi.getSysPosition().getId(),sysEmpPosi.getSysEmployee().getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "岗位员工", "失败", sysEmpPosi.getSysPosition().getId(),sysEmpPosi.getSysEmployee().getId());
		}
	}
	/**
	 * 修改机构员工的关系.
	 * @author 吴德福
	 * @param sysEmpPosi
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateEmpPosi(SysEmpPosi sysEmpPosi){
		try {
			o_sysEmpPosiDAO.merge(sysEmpPosi);
			o_businessLogBO.modBusinessLogInterface("修改", "岗位员工", "成功", sysEmpPosi.getId(), sysEmpPosi.getSysPosition().getId(), sysEmpPosi.getSysEmployee().getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "岗位员工", "失败", sysEmpPosi.getId(), sysEmpPosi.getSysPosition().getId(), sysEmpPosi.getSysEmployee().getId());
		}
	}
	/**
	 * 删除岗位员工.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeEmpPosiById(String id){
		try {
			o_sysEmpPosiDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "岗位员工", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "岗位员工", "失败", id);
		}
	}
	/**
	 * 删除岗位与员工的关系(删除岗位与员工的关系时使用).
	 * @author 吴德福
	 * @param orgid
	 * @param empid
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeEmpPosi(String posiId,String empid){
		try {
			StringBuilder hql = new StringBuilder("delete From SysEmpPosi where sysPosition.id ='"+posiId+"' and sysEmployee.id ='"+empid+"'");
			o_sysEmpPosiDAO.createQuery(hql.toString()).executeUpdate();
			o_businessLogBO.delBusinessLogInterface("删除", "岗位员工", "成功", empid);
		} catch (HibernateException e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "岗位员工", "失败", empid);
		}
	}
	/**
	 * 根据员工id查询岗位与员工的关系是否存在.
	 * @author 吴德福
	 * @param empid 员工id.
	 * @return List<SysEmpPosi> 岗位与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpPosi> querySysEmpPosiByEmpid(String empid){
		return o_sysEmpPosiDAO.findBy("sysEmployee.id", empid);
	}
	/**
	 * 根据员工id查询员工的主岗位.
	 * @author 吴德福
	 * @param empid 员工id.
	 * @return SysPosition
	 * @since  fhd　Ver 1.1
	 */
	public SysPosition queryMajorPositionByEmpid(String empid){
		SysPosition position = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmpPosi.class);
		dc.add(Restrictions.eq("sysEmployee.id", empid));
		dc.add(Restrictions.eq("ismain", true));
		List<SysEmpPosi> empPositionList = o_sysEmpPosiDAO.findByCriteria(dc);
		if(null != empPositionList && empPositionList.size()>0){
			SysEmpPosi empPosition = empPositionList.get(0);
			position = empPosition.getSysPosition();
		}
		return position;
	}
	/**
	 * 根据岗位id查询岗位与员工的关系是否存在.
	 * @author 吴德福
	 * @param posiId 岗位id.
	 * @return List<SysEmpPosi> 岗位与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpPosi> querySysEmpPosiByPosiId(String posiId){
		Criteria criteria = o_sysEmpPosiDAO.createCriteria();
		criteria.setFetchMode("sysPosition", FetchMode.SELECT);
		criteria.createAlias("sysPosition", "p");
		criteria.add(Restrictions.eq("p.id", posiId));
		return criteria.list();
		//return o_sysEmpPosiDAO.findBy("sysPosition.id", posiId);
	}
	/**
	 * 根据岗位id和员工id联合查询岗位与员工的关系是否存在.
	 * @author 吴德福
	 * @param posiId 岗位id.
	 * @param empid 员工id.
	 * @return List<SysEmpPosi> 岗位与员工关系的集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmpPosi> querySysEmpPosiByUnionid(String posiId, String empid){
		StringBuilder hql = new StringBuilder("From SysEmpPosi where sysPosition.id='"+posiId+"' and sysEmployee.id ='"+empid+"'");
		return o_sysEmpPosiDAO.createQuery(hql.toString()).list();
	}
	/**
	 * 根据id查询员工岗位关系.
	 * @author 吴德福
	 * @param id
	 * @return SysEmpPosi
	 * @since  fhd　Ver 1.1
	 */
	public SysEmpPosi querySysEmpPosiById(String id){
		return o_sysEmpPosiDAO.get(id);
	}
	
}

