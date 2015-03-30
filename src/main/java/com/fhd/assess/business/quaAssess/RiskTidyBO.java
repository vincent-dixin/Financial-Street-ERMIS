package com.fhd.assess.business.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.assess.dao.kpiSet.RangObjectDeptEmpDAO;

@Service
public class RiskTidyBO {

	@Autowired
	private RangObjectDeptEmpDAO o_rangObjectDeptEmpDAO;
	
	@Autowired
	private ApprovalAssessBO o_appApprovalAssessBO;
	
	/**
	 * 该评估计划下需要评估的部门ID
	 * */
	public ArrayList<String> findNeedAssessDepts(String assessPlanId){
		StringBuffer sql = new StringBuffer();
		
		ArrayList<String> arrayList = new ArrayList<String>();
		
        sql.append(" select ORG_ID,ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID in ");
        sql.append(" (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId +"') GROUP BY org_id ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
		for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String deptId = "";//部门ID
           
            if(null != objects[0]){
            	deptId = objects[0].toString();
            }
            arrayList.add(deptId);
        }
		
        return arrayList;
	}
	
	/**
	 * 该评估计划下需要评估的风险数量
	 * */
	public Integer findNeedAssessRisk(String assessPlanId){
		StringBuffer sql = new StringBuffer();
		
        sql.append(" select * from T_RM_RANG_OBJECT_DEPT_EMP a, ");
        sql.append(" (select * from T_RM_RISK_SCORE_DEPT  ");
        sql.append(" where org_id in ");
        sql.append(" (select ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID  ");
        sql.append(" in (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId + "') GROUP BY org_id)) b ");
        sql.append(" where a.score_dept_id = b.id ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        return list.size();
	}
	
	/**
	 * 该评估计划下已被评估的风险数量
	 * */
	public Integer findAlreadyAssessRisk(String assessPlanId){
		StringBuffer sql = new StringBuffer();
        
        sql.append(" select * from t_rm_score_result where rang_object_dept_emp_id in( ");
        sql.append(" select a.id from T_RM_RANG_OBJECT_DEPT_EMP a, ");
        sql.append(" (select * from T_RM_RISK_SCORE_DEPT  ");
        sql.append(" where org_id in  ");
        sql.append(" (select ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID  ");
        sql.append(" in (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId + "') GROUP BY org_id)) b ");
        sql.append(" where a.score_dept_id = b.id ");
        sql.append(" ) AND is_approval = '1' GROUP BY rang_object_dept_emp_id ");
        
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        return list.size();
	}
	
	/**
	 * 风险评估全部部门风险存储到统计结果表中
	 * */
	public boolean saveStatisticsResult(String assessPlanId) {
		
//		 map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
//         map.put("parentRiskId", "1111");
//         map.put("parentRiskName", "11111");
//         map.put("riskId", riskId);
//         map.put("riskName", riskName);
//         map.put("fsScoreDimId", fsScoreDimId);
//         map.put("fsScoreDicValue", fsScoreDicValue);
//         map.put("yxScoreDimId", yxScoreDimId);
//         map.put("yxScoreDicValue", yxScoreDicValue);
		
		ArrayList<HashMap<String, String>> mapsList = new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, ArrayList<Object>>> mapAllList = new ArrayList<HashMap<String, ArrayList<Object>>>();;
		
		
		ArrayList<String> needAssessDeptsList = this.findNeedAssessDepts(assessPlanId);
		
		
		
		HashMap<String, String> riskOrgAllMap = o_appApprovalAssessBO.findRiskOrgAllMap();
		ArrayList<ArrayList<String>> scoreResultAllList = o_appApprovalAssessBO.finScoreResultAllList();
		for (String orgId : needAssessDeptsList) {
			ArrayList<ArrayList<String>> deptByRiskAllList = o_appApprovalAssessBO.finDeptByRiskAllList(assessPlanId, orgId);
			
			ArrayList<HashMap<String, String>> mapList = o_appApprovalAssessBO.findDeptByLeaderId(
					assessPlanId, 
					orgId,
					deptByRiskAllList,
					riskOrgAllMap,
					scoreResultAllList);
			for (HashMap<String, String> hashMap : mapList) {
				mapsList.add(hashMap);
			}
		}
		
		//从实体中查询出来的 该评估下需要打的 风险事件
		ArrayList<String> riskList = new ArrayList<String>();
		riskList.add("88e1ab25-940f-44fe-a192-b4c45ecb90fb");
		riskList.add("333aa017-287c-43f5-b84c-5edf3ee9e5f6");
		
		
		for (String riskId : riskList) {
			for (HashMap<String, String> hashMap : mapsList) {
				//System.out.println(hashMap.get("rangObjectDeptEmpId"));
				if(riskId.equalsIgnoreCase(hashMap.get("riskId"))){
					HashMap<String, ArrayList<Object>> map = new HashMap<String, ArrayList<Object>>();
					ArrayList<Object> list = new ArrayList<Object>();
					list.add(Double.parseDouble(hashMap.get("fsScoreDicValue").toString()));
					list.add("2");
					
					list.add(Double.parseDouble(hashMap.get("yxScoreDicValue").toString()));
					list.add("2");
					
					map.put(riskId, list);
					mapAllList.add(map);
				}
			}
		}
		
		
		
		for (String riskId : riskList) {
			double fsValue = 0l;
			double yxValue = 0l;
//			String riskId = "";
			for (HashMap<String, ArrayList<Object>> hashMap : mapAllList) {
				
				if(hashMap.get(riskId) != null){
//					riskId = hashMap.get(riskId).get(2).toString();
					fsValue += Double.parseDouble(hashMap.get(riskId).get(0).toString()) * Double.parseDouble(hashMap.get(riskId).get(1).toString());
					yxValue += Double.parseDouble(hashMap.get(riskId).get(2).toString()) * Double.parseDouble(hashMap.get(riskId).get(3).toString());
				}
			}
			
			System.out.println("风险=" + riskId + "发生可能性值=" + fsValue);
			System.out.println("风险=" + riskId + "影响可能性值=" + yxValue);
		}
		
		
		
		return true;
	}
	
}