/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.business;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.wp.dao.EnforcementOrgsDAO;
import com.fhd.wp.entity.EnforcementOrgs;

/**
 * 实施单位
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-3-6  下午5:05:08
 *
 * @see 	 
 */
@Service
public class EnforcementOrgsBO {

	@Autowired
	private EnforcementOrgsDAO o_enforcementOrgsDAO;
	
	/*
	 * 添加
	 */

	/*
	 * 修改
	 */

	/*
	 * 删除
	 */

	/**
	 * 
	 * <pre>
	 * 根据工作计划id删除实施单位
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workplanId 工作计划ID
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeEnforcementOrgsByWorkPlanId(String workPlanId) {
		o_enforcementOrgsDAO.batchExecute("delete EnforcementOrgs m where m.workPlan.id = ?", workPlanId);
	}
	
	/**
	 * @author 邓广义
	 * 根据公司部门维度ID 工作 计划ID 查询对象
	 * @param orgid
	 * @param workplanid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EnforcementOrgs> findEnforcementOrgByOrgIdAndWorkPlanId(String orgid,String workplanid){
		if(StringUtils.isBlank(orgid)){
			return null;
		}
		if(StringUtils.isBlank(workplanid)){
			return null;
		}
		Criteria criteria = o_enforcementOrgsDAO.createCriteria();
		criteria.add(Restrictions.eq("org.id", orgid));
		criteria.add(Restrictions.eq("workPlan.id", workplanid));
		return criteria.list();
	}

	public void saveEnforcementOrgs(EnforcementOrgs enforcementorgs) {
		this.o_enforcementOrgsDAO.merge(enforcementorgs);
	}
	
}

