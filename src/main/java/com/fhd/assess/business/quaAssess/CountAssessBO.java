package com.fhd.assess.business.quaAssess;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.assess.dao.kpiSet.RangObjectDeptEmpDAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class CountAssessBO {

	@Autowired
	private RangObjectDeptEmpDAO o_rangObjectDeptEmpDAO;
	
	/**
	 * 通过风险ID集合查询已评估风险
	 * @param scoreEmpId 人员ID
	 * @return HashMap<String, String>
	 * @author 金鹏祥
	 * */
	private HashMap<String, String> findAssessMap(String scoreEmpId) {
		StringBuffer sql = new StringBuffer();
		HashMap<String, String> map = new HashMap<String, String>();
        
        sql.append(" select a.id,b.risk_id from T_RM_RANG_OBJECT_DEPT_EMP a,  T_RM_RISK_SCORE_OBJECT b  , t_rm_score_result c ");
        sql.append(" where a.score_emp_id = '" + scoreEmpId + "' ");// and a.score_range_name is null  or a.score_range_name='' 
        sql.append(" and  a.score_object_id = b.id and a.id = c.rang_object_dept_emp_id GROUP BY b.risk_id ");
        
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String riskId = "";
            
            if(null != objects[1]){
            	riskId = objects[1].toString();
            }
            
            map.put(riskId, riskId);
        }
        
        return map;
	}
	
	/**
	 * 通过风险ID集合查询已评估风险总数、未评估总数、已评估总数
	 * @param scoreEmpId 人员ID
	 * @param params 风险ID集合
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> getAssessCountMap(String scoreEmpId, String params) {
		HashMap<String, String> assessMap = this.findAssessMap(scoreEmpId);
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		int isAssessCount = 0;
		int isNotAssessCount = 0;
		int totalCount = 0;
		JSONArray jsonarr = JSONArray.fromObject(params);
		for (Object objects : jsonarr) {
			JSONObject jsobjs = (JSONObject) objects;
			String riskId = jsobjs.get("riskId").toString();
			
			if(assessMap.get(riskId) != null){
				isAssessCount++;
			}else{
				isNotAssessCount++;
			}
			
			totalCount++;
		}
		
		countMap.put("isAssessCount", isAssessCount);
		countMap.put("isNotAssessCount", isNotAssessCount);
		countMap.put("totalCount", totalCount);
		
		return countMap;
	}
}