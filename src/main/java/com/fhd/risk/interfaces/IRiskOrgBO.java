/**
 * IRiskOrgBO.java
 * com.fhd.risk.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * IRiskOrgBO.java
 * com.fhd.risk.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.risk.interfaces;

import java.util.List;
import java.util.Set;

import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskOrg;


/**
 * 风险、部门关联接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-19		上午09:34:53
 *
 * @see 	 
 */
public interface IRiskOrgBO {

	/**
	 * 查询风险部门关联
	 * 
	 * @author 金鹏祥
	 * @param searchName 目标id
	 * @param companyId 公司ID
	 * @param orgId 部门ID
	 * @param deleteStatus 是否删除状态
	 * @return List<Risk>
	 */
	public List<RiskOrg> findRiskOrgBySome(String searchName, String companyId, String orgId, String deleteStatus, Boolean rbs);
	
	/**
	 * 查询风险部门关联
	 * 
	 * @author 金鹏祥
	 * @param companyId 公司ID
	 * @param query 查询条件
	 * @param deleteStatus 是否删除状态
	 * @return List<Risk>
	 */
	public List<RiskOrg> findRiskOrgBySome(String companyId, String query, String deleteStatus, Boolean rbs);
	
	/**
	 * 查询风险关联返回ID已SET<STRING>存储
	 * 
	 * @author 金鹏祥
	 * @return Set<String>
	 * @since  fhd　Ver 1.1
	*/
	public Set<String> findRiskOrg();
	
	/**
	 * 按编号查询
	 * @author zhengjunxiang
	 * @param id
	 * @return
	 */
	public void removeRiskOrgById(RiskOrg riskOrg);
	
	/**
	 * 保存风险责任部门和相关部门
	 * @author zhengjunxiang
	 * @param risk
	 */
	public void saveRiskOrg(RiskOrg riskOrg);
}

