/**
 * SysOrganizationDAO.java
 * com.fhd.fdc.commons.dao.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-30 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
*/

package com.fhd.sys.dao.orgstructure;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 机构DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-17
 * Company FirstHuiDa.
 */
@Repository
@SuppressWarnings("unchecked")
public class SysOrganizationDAO extends HibernateEntityDao<SysOrganization> {

	public String[] getAllIds(){
		String hql="select org.id from SysOrganization org";
		List<String> list=this.getSession().createQuery(hql).list();
		return list.toArray(new String[list.size()]);
	}
	public SysOrganization getByleaderid(String empid){
		String hql="select org from SysOrganization org where org.empid='"+empid+"'";
		List<SysOrganization> list =this.getSession().createQuery(hql).list();
		if(list.size()>0)
			return list.get(0);
		else
			return null;
		
	}
	public String[] getOrgnamebyIds(String[] ids){
		String hql="select org.orgname from SysOrganization org where org.id in(:ids)";
		List<String> list=this.getSession().createQuery(hql).setParameterList("ids", ids).list();
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 根据部门ID查询本部门和所有子部门
	 * @author 陈建毅
	 * @since fhd　Ver 1.1
	 */
	public List<String> queryOrgidsByDeptId(String deptId){
		StringBuilder sql = new StringBuilder("select org.id  from  t_sys_organization  org  where ( org.org_type='402881b22afad3b1012afae7520f0007' or org.org_type='402881b22afad3b1012afae799c60008' ) ");

		if(StringUtils.isNotBlank(deptId)){
			sql.append("and  org.id_seq like '%.").append(deptId).append(".%'  ");
		}
		return getSession().createSQLQuery(sql.toString()).list();
		
	} 
	
	
	
}

