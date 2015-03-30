package com.fhd.assess.interfaces.formulatePlan;

import java.util.List;

import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.core.dao.Page;
import com.fhd.risk.entity.RiskOrg;

/**
 * RiskScoreBO类接口
 * @author 王再冉
 *
 */
public interface IRiskScoreBO {

	/**
	 * 查询部门id下所有部门风险实体
	 * @param orgIds
	 * @return
	 */
	public List<RiskOrg> findRiskOrgsById(String orgIds);
	/**
	 * 查询评估风险列表
	 * @param objName
	 * @param page
	 * @param sort
	 * @param dir
	 * @param planId
	 * @return
	 */
	public Page<RiskScoreObject> findScoreObjsPageBySome(String objName, Page<RiskScoreObject> page, String sort, String dir, String planId);
	/**
	 * 根据打分对象id查询打分部门实体
	 * @param ObjId
	 * @return
	 */
	public List<RiskScoreDept> findRiskDeptByObjId(String ObjId);
	/**
	 * 根据风险id查询所有关联部门
	 * @param riskId
	 * @return
	 */
	public List<RiskOrg> findRiskOrgsByriskId(String riskId);
	/**
	 * 判断打分部门中是否存在该部门
	 * @param ObjId
	 * @param orgId
	 * @param type
	 * @return
	 */
	public List<RiskScoreDept> findRiskScoreDeptIsSave(String ObjId, String orgId, String type);
	/**
	 * 保存打分对象
	 * @param scoreObj
	 */
	public void saveRiskScoreObject(RiskScoreObject scoreObj);
	/**
	 * 保存打分部门
	 * @param scoreDept
	 */
	public void saveRiskScoreDept(RiskScoreDept scoreDept);
	/**
	 * 查询打分对象是否存在
	 * @param planId
	 * @param riskId
	 * @return
	 */
	public RiskScoreObject findRiskScoreObjsByPlanAndRisk(String planId, String riskId);
	/**
	 * 根据id查询打分对象
	 * @param objId
	 * @return
	 */
	public RiskScoreObject findRiskScoreObjById(String objId);
	/**
	 * 删除打分部门
	 * @param depts
	 */
	public void removeRiskScoreDepts(List<RiskScoreDept> depts);
	/**
	 * 删除打分对象
	 * @param obj
	 */
	public void removeRiskScoreObj(RiskScoreObject obj);
	
}
