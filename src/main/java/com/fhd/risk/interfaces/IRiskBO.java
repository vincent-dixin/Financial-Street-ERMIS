/**
 * IRiskBO.java
 * com.fhd.risk.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */
/**
 * IRiskBO.java
 * com.fhd.risk.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
 */

package com.fhd.risk.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fhd.core.dao.Page;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.risk.entity.OrgAdjustHistory;
import com.fhd.risk.entity.ProcessAdjustHistory;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.StrategyAdjustHistory;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 风险接口
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012-11-19 上午09:25:56
 * 
 * @see
 */
public interface IRiskBO {

	/**
	 * 查询该风险ID、公司ID、是否可用状态(SEQ开头的)下的所有风险实体
	 * 
	 * @author 金鹏祥
	 * @return List<Risk>
	 * @param id
	 *            风险ID
	 * @param companyId
	 *            公司ID
	 * @param deleteStatus
	 *            是否可用 (true:未删除/false:删除)
	 * @since fhd　Ver 1.1
	 */
	public List<Risk> findRiskByIdAndCompanyIdAndDeleteStatus(String id,
			String companyId, boolean deleteStatus);

	/**
	 * 查询风险ID下的风险实体
	 * 
	 * @author 金鹏祥
	 * @param id
	 *            风险ID
	 * @return Risk
	 * @since fhd　Ver 1.1
	 */
	public Risk findRiskById(String id, boolean deleteStatus);

	/**
	 * 根据公司Id查询任务实体
	 * 
	 * @author 金鹏祥
	 * @param id
	 *            公司ID
	 * @return Risk
	 */
	public Risk findRiskByCompanyId(String companyId);

	/**
	 * 多条件查询
	 * 
	 * @author 金鹏祥
	 * @param companyId
	 *            公司id
	 * @param parentId
	 *            父节点id
	 * @param deleteStatus
	 *            删除状态
	 * @return List<Risk>
	 */
	public List<Risk> findRiskBySome(String companyId, String parentId,
			String deleteStatus, Boolean rbs);

	/**
	 * 多条件查询
	 * 
	 * @author 金鹏祥
	 * @param companyId
	 *            公司id
	 * @param deleteStatus
	 *            删除状态
	 * @return List<Risk>
	 */
	public List<Risk> findRiskBySome(String companyId, String deleteStatus);

	/**
	 * 查询任务并已MAP方式存储KEY:PARENT,VALUE:""
	 * 
	 * @author 金鹏祥
	 * @param companyId
	 *            公司ID
	 * @return HashMap<String, String>
	 * @since fhd　Ver 1.1
	 */
	public HashMap<String, Risk> findRiskMapByCompanyId(String companyId);

	/**
	 * 查询没有和部门关联的任务并已MAP方式存储KEY:PARENT,VALUE:""
	 * 
	 * @author 金鹏祥
	 * @param companyId
	 *            公司ID
	 * @return HashMap<String, String>
	 * @since fhd　Ver 1.1
	 */
	public HashMap<String, Risk> findRiskMapFromNotRiskOrgByCompanyId(
			String companyId);

	/**
	 * 查找没有和部门关联的Id集合
	 * 
	 * @author 金鹏祥
	 * @param id
	 *            目标id
	 * @param query
	 *            查询条件
	 * @return Set<String>
	 * @since fhd　Ver 1.1
	 */
	public Set<String> findRiskMapFromNotRiskOrgBySome(String id, String query);

	/**
	 * 根据目标ID查找没有和部门关联的目标对象
	 * 
	 * @author 金鹏祥
	 * @param id
	 *            目标id
	 * @param query
	 *            查询条件
	 * @return List<Risk>
	 */
	public List<Risk> findNotInOrgStrategyMap(String id, String deleteStatus,
			Boolean rbs);

	/**
	 * 根据目标名称、公司ID查询风险实体
	 * 
	 * @author 金鹏祥
	 * @param searchName
	 *            目标名称
	 * @param companyId
	 *            公司ID
	 * @param deleteStatus
	 *            是否删除状态
	 * @return List<Risk>
	 */
	public List<Risk> finRiskBySome(String searchName, String companyId,
			String deleteStatus);

	/**
	 * 查询parent为空的风险实体
	 * 
	 * @author 金鹏祥
	 * @return Risk
	 * @since fhd　Ver 1.1
	 */
	public Risk findRiskParentIsNull();

	/**
	 * 根据公司Id查询部门
	 * 
	 * @author zhengjunxiang
	 * @param companyId
	 * @return
	 */
	public SysOrganization findOrganizationByOrgId(String companyId);

	/**
	 * 保存风险
	 * 
	 * @author zhengjunxiang
	 * @param risk
	 */
	public void saveRisk(Risk risk);

	/**
	 * 保存风险,连带相关信息
	 * 
	 * @author zhengjunxiang
	 * @param risk
	 */
	public void addRisk(Risk risk, String respDeptName, String relaDeptName,
			String respPositionName, String relaPositionName, String riskKind,
			String relePlate, String riskKpiName, String influKpiName,
			String controlProcessureName, String influProcessureName);

	/**
	 * 修改风险,连带相关信息
	 * 
	 * @author zhengjunxiang
	 * @param risk
	 */
	public void updateRisk(Risk risk, String respDeptName, String relaDeptName,
			String respPositionName, String relaPositionName, String riskKind,
			String relePlate, String riskKpiName, String influKpiName,
			String controlProcessureName, String influProcessureName);

	/**
	 * 删除风险
	 * 
	 * @author zhengjunxiang
	 * @param ids
	 */
	public void removeRiskByIds(String ids);
	
	/**
	 * 根据风险id查询历史记录
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<RiskAdjustHistory> findRiskAdjustHistoryBySome(String id,Page<RiskAdjustHistory> page,String sort, String dir,String query);
	
	/**
	 * 根据风险id查询风险事件
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<Risk> findRiskEventBySome(String id,Page<Risk> page,String sort, String dir,String query);
	
	/**
	 * 查询最新的历史
	 * @param id
	 * @return
	 */
	public RiskAdjustHistory findLatestRiskAdjustHistoryByRiskId(String id);
	
	/**
	 * 风险树的构建
	 * @author 郑军祥
	 * @param id			nodeId
	 * @param canChecked  	是否多选
	 * @param query		  	查询条件
	 * @param rbs			true表示值查询风险，不带有风险事件；false带有风险事件
	 * @return
	 */
	public List<Map<String, Object>> riskTreeLoader(String id, String query, Boolean rbs, Boolean canChecked, String chooseId);
	
	/**
	 * 启用，停用风险
	 * 
	 * @author zhengjunxiang
	 * @param ids
	 */
	public void enableRisk(String ids,String isUsed);
	
	/**
	 * 查询目标id下的风险列表
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<Risk> findRiskEventByStrateMapId(String id,Page<Risk> page,String sort, String dir,String query);
	
	/**
	 * 查询目标id下的评估历史记录
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<StrategyAdjustHistory> findStrategyHistoryByStrateMapId(String id,Page<StrategyAdjustHistory> page,String sort, String dir,String query);
	
	/**
	 * 查询组织id下的风险列表
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<Risk> findRiskEventByOrgId(String id,Page<Risk> page,String sort, String dir,String query);
	
	/**
	 * 查询组织id下的评估历史记录
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<OrgAdjustHistory> findOrgHistoryByOrgId(String id,Page<OrgAdjustHistory> page,String sort, String dir,String query);
	
	/**
	 * 查询流程id下的风险列表
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<Risk> findRiskEventByProcessId(String id,Page<Risk> page,String sort, String dir,String query);
	
	/**
	 * 查询流程id下的评估历史记录
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<ProcessAdjustHistory> findProcessHistoryByProcessId(String id,Page<ProcessAdjustHistory> page,String sort, String dir,String query);
	
	/**
	 * 查询指标id下的风险列表
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public Page<Risk> findRiskEventByKpiId(String id,Page<Risk> page,String sort, String dir,String query);
}