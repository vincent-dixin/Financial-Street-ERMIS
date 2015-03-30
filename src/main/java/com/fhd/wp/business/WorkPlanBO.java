/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.wp.dao.WorkPlanDAO;
import com.fhd.wp.entity.EnforcementOrgs;
import com.fhd.wp.entity.WorkPlan;

/**
 * 工作计划添加修改删除查询
 * 工作流的发布和导入导出
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-20  下午5:50:53
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class WorkPlanBO {
	
	
	private Log log = LogFactory.getLog(WorkPlanBO.class);
	
	@Autowired
	private WorkPlanDAO o_workPlanDAO;
	@Autowired
	private MilestionBO o_milestionBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private EnforcementOrgsBO o_enforcementOrgsBO;
	/*
	 * 添加
	 */

	/**
	 * <pre>
	 * 保存工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlan 工作计划实体
	 * @since  fhd　Ver 1.1
	*/
	@Transactional 
	public void saveWorkPlan(WorkPlan workPlan) {
		
		workPlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
		
		// 删除关联的里程碑
		o_milestionBO.removeMilestoneByWorkPlanId(workPlan.getId());
		// 删除关联的实施单位
		o_enforcementOrgsBO.removeEnforcementOrgsByWorkPlanId(workPlan.getId());
		
		workPlan.setCompany(o_organizationBO.get(UserContext.getUser().getCompanyid()));
		o_workPlanDAO.merge(workPlan);
		
	}
	
	
	/**
	 * <pre>
	 * 提交工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlan 工作计划实体
	 * @param transition 审批通过或不通过
	 * @param opinion 审批意见
	 * @since  fhd　Ver 1.1
	 */
	@Transactional 
	public void saveWorkPlanSubmit(WorkPlan workPlan, String transition, String opinion, 
			String processInstanceId,String superior) {
		
		saveWorkPlan(workPlan);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("id", workPlan.getId());
		variables.put("name", workPlan.getName());
		// 工作流提交
		if(StringUtils.isEmpty(processInstanceId)) {
			// 发起人  
			variables.put("proposer", UserContext.getUser().getEmpid());
			// TODO 审批人未设置 - 胡迪新
			variables.put("approver", UserContext.getUser().getEmpid());
			// TODO 领导未设置 - 胡迪新
			variables.put("superior", UserContext.getUser().getEmpid());
			
			// 执行人 多个
			List<String> executors = new ArrayList<String>();
			Set<EnforcementOrgs> orgs = workPlan.getEnforcementOrgses();
			for (EnforcementOrgs org : orgs) {
				List<SysEmployee> emps = o_sysRoleBO.getEmpByCorpAndRole(org.getOrg().getId(),"内控部门员工");
				if(emps.size() > 0) {
					executors.add(emps.get(0).getId());
				}else{
					log.error("公司ID为 [" + org.getOrg().getId() + "], 没有角色为 [内控部门员工] 的员工");
				}
			}
			variables.put("executors", executors);
			variables.put("joinCount", executors.size());

			String newProcessInstanceId = o_jbpmBO.startProcessInstance("workplan_approve", variables);
			o_jbpmBO.doProcessInstance(newProcessInstanceId, variables);	
		} else {
			variables.put("isReject", transition);
			if("0yn_y".equals(superior) && "yes".equals(transition)){
				variables.put("isSuperior", "yes");
			}else {
				variables.put("isSuperior", "no");
			}
			
			
			if(StringUtils.isNotEmpty(opinion)){
				variables.put("examineApproveIdea", opinion);
			}
			
			o_jbpmBO.doProcessInstance(processInstanceId, variables);	
		}
		
		
	}
	
	/*
	 * 修改
	 */
	

	/*
	 * 删除
	 */

	/**
	 * <pre>
	 * 根据id 删除工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param ids
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeWorkPlan(String ids) {
		
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("deleteStatus", Contents.DELETE_STATUS_DELETED);
		var.put("ids", StringUtils.split(ids,","));
		
		o_workPlanDAO.batchExecute("update WorkPlan p set p.deleteStatus =:deleteStatus where p.id in (:ids)",var);
		
	}
	
	/*
	 * 查询
	 */
	
	/**
	 * <pre>
	 * 根据ID查询工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId 工作计划ID
	 * @return 工作计划实体
	 * @since  fhd　Ver 1.1
	*/
	
	public WorkPlan findWorkPlanById(String workPlanId) {
		return o_workPlanDAO.get(workPlanId);
	}
	
	/**
	 * 
	 * <pre>
	 * 根据编号,id查询计划
	 * 如果id为null 则不按id进行查询
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param code 编号
	 * @param workPlanId 工作计划ID
	 * @return 工作计划实体
	 * @since  fhd　Ver 1.1
	 */
	public WorkPlan findWorkPlanByCode(String code, String workPlanId) {
		Criteria criteria = o_workPlanDAO.createCriteria();
		criteria.add(Restrictions.eq("code", code));
		if(StringUtils.isNotBlank(workPlanId)) {
			criteria.add(Restrictions.not(Restrictions.eq("id", workPlanId)));
		}
		
		return (WorkPlan) criteria.uniqueResult();
	}

	/**
	 * <pre>
	 * 分页查询工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param page 分页对象
	 * @param query 查询条件
	 * @since  fhd　Ver 1.1
	*/
	public void findWorkPlanByPage(Page<WorkPlan> page, String query) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(WorkPlan.class);
		if(StringUtils.isNotBlank(query)) {
			dc.add(Restrictions.or(Restrictions.like("name", query, MatchMode.ANYWHERE),
					Restrictions.like("code", query, MatchMode.ANYWHERE)));
		}
		dc.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		dc.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));

		dc.addOrder(Order.asc("createTime"));
		o_workPlanDAO.findPage(dc, page, false);
		
	}

	/**
	 * 
	 * <pre>
	 * 根据年份查询工作计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param year 年份
	 * @return 工作计划集合
	 * @since  fhd　Ver 1.1
	 */
	public List<WorkPlan> findWorkPlanByYear(Integer year) {
		
		Criteria criteria = o_workPlanDAO.createCriteria();
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("enforcementOrgses", "eo");
		criteria.createAlias("eo.org", "o");
		
		criteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
		
		criteria.add(Restrictions.between("createTime",  
				DateUtils.stringToDateToSecond(year + "-1-1 00:00:00"), 
				DateUtils.stringToDateToSecond(year + "-12-31 23:59:59")));
		
		// 开始时间排序
		criteria.addOrder(Order.asc("createTime"));
		
		return criteria.list();
	}
	

	

	
	
}

