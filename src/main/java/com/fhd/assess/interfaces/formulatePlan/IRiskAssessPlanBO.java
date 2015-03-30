package com.fhd.assess.interfaces.formulatePlan;

import java.util.List;

import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.core.dao.Page;

public interface IRiskAssessPlanBO {

	/**
	 * 查询计划任务列表
	 * @param planName	计划名称
	 * @param page
	 * @param sort
	 * @param dir
	 * @param companyId
	 * @return
	 */
	public Page<RiskAssessPlan> findPlansPageBySome(String planName, Page<RiskAssessPlan> page, String sort, String dir, String companyId);
	
	/**
	 * 保存
	 * @param riskPlan
	 */
	public void saveRiskAssessPlan(RiskAssessPlan riskPlan);
	
	/**
	 * 根据id查询计划任务实体
	 * @param id
	 * @return
	 */
	public RiskAssessPlan findRiskAssessPlanById(String id);
	
	/**
	 * (批量)删除计划任务
	 * @param ids
	 */
	 public void removeRiskAssessPlansByIds(List<String> ids);
	 /**
	  * 更新计划
	  * @param riskPlan
	  */
	 public void mergeRiskAssessPlan(RiskAssessPlan riskPlan);
	
}
