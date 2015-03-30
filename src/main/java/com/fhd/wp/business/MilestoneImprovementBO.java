/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.wp.dao.MilestoneImprovementDAO;
import com.fhd.wp.entity.MilestoneImprovement;

/**
 * 里程碑关联计划
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-3-13  上午10:54:29
 *
 * @see 	 
 */
@Service
public class MilestoneImprovementBO {

	@Autowired
	private MilestoneImprovementDAO o_milestoneImprovementDAO;
	@Autowired
	private JBPMBO o_jbpmBO;
	
	/*
	 * 添加
	 */
	
	/**
	 * 
	 * <pre>
	 * 添加里程碑和计划的关系
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param mi
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveMilestoneImprovement(MilestoneImprovement mi) {
		o_milestoneImprovementDAO.merge(mi);
	}
	
	/**
	 * 
	 * <pre>
	 * 批量添加里程碑和计划的关系
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param mi
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveMilestoneImprovementBatch(List<MilestoneImprovement> mis) {
		for (MilestoneImprovement mi : mis) {
			saveMilestoneImprovement(mi);
		}
	}

	/**
	 * <pre>
	 * saveMilestoneImprovementBatch:(这里用一句话描述这个方法的作用)
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param mis
	 * @param processInstanceId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveMilestoneImprovementBatchSubmit(List<MilestoneImprovement> mis,
			String processInstanceId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		saveMilestoneImprovementBatch(mis);
		o_jbpmBO.doProcessInstance(processInstanceId, variables);	
	}
	
	
	/*
	 * 修改
	 */

	/*
	 * 删除
	 */

	/*
	 * 查询
	 */
	
	/**
	 * <pre>
	 * 根据下级计划ID查询与里程碑之间的关系
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param subPlanId
	 * @since  fhd　Ver 1.1
	*/
	
	public MilestoneImprovement findMilestoneImprovementBySubPlanId(String subPlanId) {
		
		return o_milestoneImprovementDAO.findUnique(Restrictions.eq("subPlanId", subPlanId));
		
	}
	/**
	 * 获得所有计划于里程碑关联
	 * @return
	 */
	public List<MilestoneImprovement> findMilestoneImprovementBySome(){
		return this.o_milestoneImprovementDAO.getAll();
	}
	/**
	 * 获得工作计划下的公司获得部门下的 里程碑 关联
	 * @param orgId
	 * @param milestoneids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MilestoneImprovement> findMilestoneImprovementByOrgIdAndMilestoneIds(String orgId,List<String> milestoneids){
		if(StringUtils.isBlank(orgId)){
			return null;
		}
		if(null==milestoneids){
			return null;
		}
		Criteria criteria = o_milestoneImprovementDAO.createCriteria();
		criteria.add(Restrictions.eq("org.id", orgId));
		criteria.add(Restrictions.in("milestone.id",milestoneids ));
		
		return criteria.list();
	}
}

