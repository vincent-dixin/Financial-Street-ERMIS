package com.fhd.sys.business.assess;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.assess.WeightSetDAO;
import com.fhd.sys.entity.assess.WeightSet;

@Service
public class WeightSetBO {

	@Autowired
	private WeightSetDAO o_weightSetDAO;
	
	/**
	 * 查询权重设置所有数据
	 * @return List<WeightSet>
	 * @author 金鹏祥
	 * */
	@SuppressWarnings("unchecked")
	public HashMap<String,Double> findWeightSetAllMap(){
		Criteria criteria = o_weightSetDAO.createCriteria();
		List<WeightSet> weightSetList = criteria.list();
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (WeightSet weightSet : weightSetList) {
			map.put("dutyDeptWeight", Double.parseDouble(weightSet.getObjectKey()));//责任部门权重
			map.put("relatedDeptWeight", Double.parseDouble(weightSet.getRelatedDeptWeight()));//相关部门权重
			map.put("assistDeptWeight", Double.parseDouble(weightSet.getAssistDeptWeight()));//辅助部门权重
			map.put("partakeDeptWeight", Double.parseDouble(weightSet.getPartakeDeptWeight()));//参与部门权重
			map.put("leadWeight", Double.parseDouble(weightSet.getLeadWeight()));//领导权重
			map.put("seaffWeight", Double.parseDouble(weightSet.getStaffWeight()));//员工权重
		}
		
		return map;
	}
}
