/**
 * OrgCmpBO.java
 * com.fhd.sys.business.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-24 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.orgstructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * ClassName:OrgCmpBO
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-24		上午10:18:34
 *
 * @see 	 
 */
@SuppressWarnings({ "deprecation", "unchecked" })
@Service 
public class OrgCmpBO {
	@Autowired
	private SysOrganizationDAO o_sysOrganizationDAO;
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	
	/**
	 * <pre>
	 * 根据parentId查询所有公司
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String, Object> findCompanyByParentId(String parentId) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		Criteria criteria = o_sysOrganizationDAO.createCriteria();
		criteria.add(Restrictions.like("orgseq", "." + parentId + ".",MatchMode.ANYWHERE));
		criteria.add(Restrictions.in("orgType", new String[]{"0orgtype_c","0orgtype_sc"}));
		criteria.addOrder(Order.asc("orgseq"));
		List<SysOrganization> organizations = criteria.list();
		for (SysOrganization org : organizations) {
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", org.getId());
			StringBuffer sb = new StringBuffer();
			node.put("name", sb.append(org.getOrgname()).toString());
			nodes.add(node);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companys", nodes);
		return map;
	}
	/**
	 * <pre>
	 * 根据机构的Id的Set集合查询机构列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param idSet 机构的Id的Set集合
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<SysOrganization> findOrgByIdSet(Set<String> idSet) {
		Criteria criteria = o_sysOrganizationDAO.createCriteria();
		if(null != idSet && idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet.toArray()));
		}else{
			criteria.add(Restrictions.isNotNull("id"));
		}
		criteria.addOrder(Order.asc("orgseq"));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 根据员工的Id的Set集合查询员工列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param idSet 员工的Id的Set集合
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<SysEmployee> findEmpByIdSet(Set<String> idSet) {
		Criteria criteria = o_sysEmployeeDAO.createCriteria();
		if(null != idSet && idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet.toArray()));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		return criteria.list();
	}
	
	/**
	 * 根据公司id查询公司的所有部门列表.
	 * @author 吴德福
	 * @param companyId
	 * @return List<SysOrganization>
	 */
	public List<SysOrganization> findDeptListByCompanyId(String companyId){
		Criteria criteria = o_sysOrganizationDAO.createCriteria();
		criteria.add(Restrictions.eq("parentOrg.id", companyId));
		criteria.add(Restrictions.in("orgType", new Object[]{"0orgtype_d","0orgtype_sd"}));
		return criteria.list();
	}
}

