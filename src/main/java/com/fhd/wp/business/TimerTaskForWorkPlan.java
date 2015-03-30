package com.fhd.wp.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.entity.JbpmHistProcinst;
import com.fhd.core.utils.DateUtils;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.rectify.ImproveBO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.interfaces.IDictBO;
import com.fhd.wp.entity.EnforcementOrgs;
import com.fhd.wp.entity.Milestone;
import com.fhd.wp.entity.MilestoneImprovement;
import com.fhd.wp.entity.WorkPlan;

/**
 * 定时任务
 * 定时维护里程碑计划关联表（T_WP_MILESTONE_RE_IMPROVEMENT）的状态与完成情况
 * @author 邓广义
 * @date 2013-5-8
 * @since  fhd　Ver 1.1
 */

@Service
public class TimerTaskForWorkPlan {
	
	@Autowired
	private JBPMBO o_jbpmBO;
	
	@Autowired
	private MilestoneImprovementBO o_milestoneImprovementBO;
	
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	
	@Autowired
	private ImproveBO o_improveBO;
	
	@Autowired
	private IDictBO o_iDictBO;
	
	@Autowired
	private WorkPlanBO o_workPlanBO;
	
	@Autowired
	private EnforcementOrgsBO o_enforcementOrgsBO;
	
	
	private  final String year = DateUtils.getYear(new Date());
	/**
	 * 获得所有关联的计划
	 * @return
	 */
	public List<MilestoneImprovement> findsubPlanBySome(){
		
		return this.o_milestoneImprovementBO.findMilestoneImprovementBySome();
	}
	/**
	 * 更新所有子计划 所对应的完成情况 调用接口o_jbpmBO.findJbpmHistProcinstByBusinessId()
	 * @param subplanids
	 */
	@Transactional
	public void updateSubPlanFinishCondition(List<MilestoneImprovement> milestoneImprovement){
		for(MilestoneImprovement mi: milestoneImprovement){
			JbpmHistProcinst jhp = o_jbpmBO.findJbpmHistProcinstByBusinessId(mi.getSubPlanId());
			Map<String,String> map = new HashMap<String,String>();
				map.put("N", "unfinish");
				map.put("H", "unfinish");
				map.put("F", "finish");
				map.put("A", "overdue_finish");
			if("assessment_plan".equals(mi.getType().getId())){ //评价计划
				String planid = mi.getSubPlanId();
				if(!StringUtils.isBlank(planid)){
					AssessPlan ap = o_assessPlanBO.findAssessPlanByAssessPlanId(planid);
					if(null!=ap){
						String exestatus = map.get(ap.getDealStatus());
						if(!StringUtils.isBlank(exestatus)){
							DictEntry dic = this.o_iDictBO.findDictEntryById(exestatus);
							mi.setStatus(dic);
						}
					}
					//mi.setStatus(ap.getDealStatus());
				}
			}
			else if("rectify_plan".equals(mi.getType().getId())){	//整改计划
				String planid = mi.getSubPlanId();
				if(!StringUtils.isBlank(planid)){
					Improve ir = o_improveBO.findImproveById(planid);
					if(null!=ir){
						String exestatus = map.get(ir.getDealStatus());
						if(!StringUtils.isBlank(exestatus)){
							DictEntry dic = this.o_iDictBO.findDictEntryById(exestatus);
							mi.setStatus(dic);
						}
					}
				}
			}
			if(null != jhp){
				Integer processPercent = o_jbpmBO.processRateOfProgress(jhp.getId_());
				mi.setFinishCondition(new Double(processPercent.toString()));
				o_milestoneImprovementBO.saveMilestoneImprovement(mi);
			}
			// 先获得 当前年份的 workplans
			
			List<WorkPlan> workplans = o_workPlanBO.findWorkPlanByYear(new Integer(this.year));
			
			// 遍历workplan
			for(WorkPlan wp :workplans){
				String workplanid = wp.getId();
				//获得 workplan下的 org
				Set<EnforcementOrgs> orgs = wp.getEnforcementOrgses() ;
				// 获得 workplan 下的 milestone
				Set<Milestone> mss = wp.getMilestones();
				//遍历 org 获得org下的 milestone 查询条件为org = and milestone in("workplan下的milestone")
				List<String> Milestoneids = new ArrayList<String>();
				for(Milestone ms : mss){
					Milestoneids.add(ms.getId());
				}
				for(EnforcementOrgs efmo : orgs){
					if(null!=efmo.getOrg()){
						String orgid = efmo.getOrg().getId();//公司部门维度ID
						//根据公司部门维度ID 与 其下的里程碑集合 获得 当前工作计划下的某一个公司下的里程碑 
						List<MilestoneImprovement> msirms=  o_milestoneImprovementBO.findMilestoneImprovementByOrgIdAndMilestoneIds(orgid, Milestoneids);
						Double status = 0.0;
						//将当前工作计划下的某一个公司下的里程碑 关联的子计划 完成情况汇总 
						for(MilestoneImprovement msirm :msirms){
							status += msirm.getFinishCondition();
						}
						//通过公司部门维度ID 与 工作计划ID 确定一条记录
						List<EnforcementOrgs>  list = o_enforcementOrgsBO.findEnforcementOrgByOrgIdAndWorkPlanId(orgid,workplanid);
						EnforcementOrgs enforcementorgs = null;
						if(null!=list && list.size()==1){
							enforcementorgs = list.get(0);
						}
						if(null!=enforcementorgs){
							enforcementorgs.setStatus(status.toString());
							o_enforcementOrgsBO.saveEnforcementOrgs(enforcementorgs);
						}
					}
				}
			}
		}
	}
	/**
	 * 开放给计划任务的接口
	 */
	@Transactional
	public void startTimer(){
		List<MilestoneImprovement> list = this.findsubPlanBySome();
		this.updateSubPlanFinishCondition(list);
	}
}
