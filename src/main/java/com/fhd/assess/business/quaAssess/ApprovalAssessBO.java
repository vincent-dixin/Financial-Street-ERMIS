package com.fhd.assess.business.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.assess.dao.kpiSet.RangObjectDeptEmpDAO;
import com.fhd.risk.dao.RiskOrgDAO;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.risk.entity.Score;

@Service
public class ApprovalAssessBO {

	@Autowired
	private RangObjectDeptEmpDAO o_rangObjectDeptEmpDAO;
	
	@Autowired
	private RiskOrgDAO o_riskOrgDAO;
	
	/**
	 * 该评估计划下某部门需要评估的风险数量
	 * */
	public Integer findNeedAssessDeptRisk(String assessPlanId,String orgId){
		StringBuffer sql = new StringBuffer();
		
        sql.append(" select a.id,a.score_emp_id,b.risk_id,a.score_org_name,a.score_emp_name,a.org_id from ");
        sql.append(" (select a.id,a.score_emp_id,a.score_org_name ,a.score_emp_name,a.score_object_id,b.org_id from  ");
        sql.append(" T_RM_RANG_OBJECT_DEPT_EMP a, (select id,ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID in  ");
        sql.append(" (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId + "')) b where a.score_dept_id = b.id) a,   ");
        sql.append(" t_rm_risk_score_object b ");
        sql.append(" where  a.score_object_id=b.id and a.org_id='" + orgId + "' ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        return list.size();
	}
	
	/**
	 * 该评估计划下某部门已被评估的风险数量
	 * */
	public Integer findAlreadyAssessDeptRisk(String assessPlanId, String orgId){
		StringBuffer sql = new StringBuffer();
        
        sql.append(" select a.id,b.risk_id,a.score_emp_id,a.score_org_name,a.score_emp_name from  ");
        sql.append(" (select a.id,a.score_emp_id,a.score_org_name ,a.score_emp_name,a.score_object_id,b.org_id from  ");
        sql.append(" T_RM_RANG_OBJECT_DEPT_EMP a, (select id,ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID in  ");
        sql.append(" (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId + "')) b where a.score_dept_id = b.id) a, ");
        sql.append(" T_RM_RISK_SCORE_OBJECT b  , t_rm_score_result c  ");
        sql.append(" where  a.score_object_id = b.id ");
        sql.append(" and a.id = c.rang_object_dept_emp_id  and a.org_id='" + orgId + "' ");
        sql.append(" GROUP BY a.id ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        return list.size();
	}
	
	
	
	/**
	 * 查询所有风险关联部门表数据并已MAP方式存储
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> findRiskOrgAllMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		Criteria criteria = o_riskOrgDAO.createCriteria();
		List<RiskOrg> list = null;
		list = criteria.list();
		for (RiskOrg riskOrg : list) {
			map.put(riskOrg.getRisk().getId() + "--" + riskOrg.getSysOrganization().getId(), riskOrg.getType());
		}
		
		return map;
	}
	
	/**
	 * 通过综合ID,打分结果维度分值
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	private HashMap<String, String> getScoreResultMap(ArrayList<ArrayList<String>> scoreResultAllList, String rangObjectDeptEmpId){
		HashMap<String, String> scoreResultMap = new HashMap<String, String>();
		for (ArrayList<String> arrayList : scoreResultAllList) {
			for (String string : arrayList) {
				if(rangObjectDeptEmpId.equalsIgnoreCase(string)){
					scoreResultMap.put(arrayList.get(1), arrayList.get(3));
				}
			}
		}
		
		return scoreResultMap;
	}
	
	/**
	 * 查询打分结果维度分值所有数据
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	public ArrayList<ArrayList<String>> finScoreResultAllList() {
		StringBuffer sql = new StringBuffer();
        
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
        
        sql.append(" select a.rang_object_dept_emp_id,a.score_dim_id,c.score_dim_name, b.score_dic_value ");
        sql.append(" from t_rm_score_result a, t_dim_score_dic b ,T_DIM_SCORE_DIM c ");
        sql.append(" where a.score_dic_id = b.id and c.id=a.score_dim_id ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            String scoreDimId = "";//维度ID
            String scoreDimName = "";//维度名称
            String scoreDicValue = "";//维度分值
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	scoreDimId = objects[1].toString();
            }if(null != objects[2]){
            	scoreDimName = objects[2].toString();
            }if(null != objects[3]){
            	scoreDicValue = objects[3].toString();
            }
            ArrayList<String> array = new ArrayList<String>();
            array.add(rangObjectDeptEmpId);
            array.add(scoreDimId);
            array.add(scoreDimName);
            array.add(scoreDicValue);
            arrayList.add(array);
        }
        
        return arrayList;
	}
	
	/**
	 * 通过风险ID,得到同一部门打分的风险
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	private ArrayList<String> getDeptRisks(ArrayList<ArrayList<String>> finDeptByRiskAllList, String riskId){
		ArrayList<String> arrayList = new ArrayList<String>();
		for (ArrayList<String> arrayList2 : finDeptByRiskAllList) {
			for (String string : arrayList2) {
				if(riskId.equalsIgnoreCase(string)){
					arrayList.add(arrayList2.get(1) + "--" + arrayList2.get(2));//存放综合ID
				}
			}
		}
		return arrayList;
	}
	
	/**
	 * 通过风险ID,查询领导部门下的同一部门所有评估人
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	public ArrayList<ArrayList<String>> finDeptByRiskAllList(String assessPlanId, String ogrId) {
		StringBuffer sql = new StringBuffer();
        
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
        
		sql.append(" select a.id,a.score_emp_id,b.risk_id,a.score_org_name,a.score_emp_name,a.org_id from  ");
        sql.append(" (select a.id,a.score_emp_id,a.score_org_name ,a.score_emp_name,a.score_object_id,b.org_id from   ");
        sql.append(" T_RM_RANG_OBJECT_DEPT_EMP a, (select id,ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID in  ");
        sql.append(" (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId +"')) b where a.score_dept_id = b.id) a,   ");
        sql.append(" t_rm_risk_score_object b   ");
        sql.append(" where  a.score_object_id=b.id and a.org_id='" + ogrId + "' ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            String empId = "";//打分人员ID
            String riskId = "";//风险ID
            
            ArrayList<String> array = new ArrayList<String>();
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	empId = objects[1].toString();
            }if(null != objects[2]){
            	riskId = objects[2].toString();
            }
            array.add(riskId);
            array.add(rangObjectDeptEmpId);
            array.add(empId);
            arrayList.add(array);
        }
        
        return arrayList;
	}
	
	/**
	 * 查询领导部门下的同一部门所有评估人
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	public ArrayList<HashMap<String, String>> findDeptByLeaderId(
			String assessPlanId, 
			String orgId,
			ArrayList<ArrayList<String>> deptByRiskAllList,
			HashMap<String, String> riskOrgAllMap,
			ArrayList<ArrayList<String>> scoreResultAllList
			) {
		StringBuffer sql = new StringBuffer();
//		ArrayList<ArrayList<String>> deptByRiskAllList = this.finDeptByRiskAllList(assessPlanId, orgId);
//		HashMap<String, String> riskOrgAllMap = this.findRiskOrgAllMap();
//		ArrayList<ArrayList<String>> scoreResultAllList = this.finScoreResultAllList();
		ArrayList<String> deptRisksList = null;
		HashMap<String, String> deptScoreResultMap = null;
		ArrayList<String> tempRiskList = new ArrayList<String>();
		boolean isContinue = false;
		
		ArrayList<HashMap<String, String>> mapsList = new ArrayList<HashMap<String, String>>();
        //f4a9e5abc805469790a9765d9d950b7a
        sql.append(" select a.id,a.score_emp_id,b.risk_id,a.score_org_name,a.score_emp_name,a.org_id,c.RISK_NAME from  ");
        sql.append(" (select a.id,a.score_emp_id,a.score_org_name ,a.score_emp_name,a.score_object_id,b.org_id from   ");
        sql.append(" T_RM_RANG_OBJECT_DEPT_EMP a, (select id,ORG_ID from T_RM_RISK_SCORE_DEPT where SCORE_OBJECT_ID in  ");
        sql.append(" (select id from t_rm_risk_score_object where ASSESS_PLAN_ID = '" + assessPlanId + "')) b where a.score_dept_id = b.id) a,   ");
        sql.append(" t_rm_risk_score_object b  , t_rm_risks c  ");
        sql.append(" where  a.score_object_id=b.id and a.org_id='" + orgId + "'  AND b.risk_id = c.id ");
        
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            String empId = "";//打分人员ID
            String riskId = "";//风险ID
            String deptId = "";//人员ID
            String type = "";//M:责任部门,A:相关部门
            String fsScoreDimId ="";
            String yxScoreDimId ="";
            String fsScoreDicValue = "";
            String yxScoreDicValue = "";
            String riskName = "";
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	empId = objects[1].toString();
            }if(null != objects[2]){
            	riskId = objects[2].toString();
            }if(null != objects[5]){
            	deptId = objects[5].toString();
            }if(null != objects[6]){
            	riskName = objects[6].toString();
            }
            
            if(tempRiskList.size() != 0){
            	//筛选风险事件
	            for (String risk : tempRiskList) {
					if(risk.equalsIgnoreCase(riskId)){
						System.out.println("返回");
						isContinue = true;
					}
				}
            }
            
            if(isContinue){
            	isContinue = false;
            	continue;
            }
            
            deptRisksList = this.getDeptRisks(deptByRiskAllList, riskId);
            deptScoreResultMap = this.getScoreResultMap(scoreResultAllList, rangObjectDeptEmpId);
            
            if(deptRisksList.size() > 1){
            	tempRiskList.add(riskId);//进入筛选区
            	
            	//同一部门多个评估人进行打分
            	for (String rangObjectDeptEmpIds : deptRisksList) {
            		String strs[] = rangObjectDeptEmpIds.split("--");
            		
					System.out.println("综合ID="+strs[0]);
					System.out.println("打分人员ID="+strs[1]);
					
					type = riskOrgAllMap.get(riskId + "--" +deptId);
	            	deptScoreResultMap.get("707752d7-70bb-48d2-af1d-8270e6e62a1d");//发生可能性
	            	deptScoreResultMap.get("46e75071-9946-4b37-b407-aff729219a7b");//影响可能性
	            	System.out.println("打分部门"+ deptId + "在风险事件 " + riskId + "中是" + type);
	            	if("M".equalsIgnoreCase(type)){//M:责任部门
	            		
	            	}else if("A".equalsIgnoreCase(type)){//A:相关部门
	            		
	            	}
				}
            }else{
            	//只有一人在打分
            	System.out.println("综合ID="+rangObjectDeptEmpId);
				System.out.println("打分人员ID="+empId);
            	
            	riskOrgAllMap.get(riskId + "--" +deptId);
            	if("M".equalsIgnoreCase(type)){//M:责任部门
            		
            	}else if("A".equalsIgnoreCase(type)){//A:相关部门
            		
            	}
            }
            
            //将某部门下的风险与灯 存储在map中
            
            fsScoreDimId ="707752d7-70bb-48d2-af1d-8270e6e62a1d";//发生可能性
            yxScoreDimId ="46e75071-9946-4b37-b407-aff729219a7b";//影响可能性
            fsScoreDicValue = "0.4";
            yxScoreDicValue = "0.5";
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
            map.put("parentRiskId", "1111");
            map.put("parentRiskName", "11111");
            map.put("riskId", riskId);
            map.put("riskName", riskName);
            map.put("fsScoreDimId", fsScoreDimId);
            map.put("fsScoreDicValue", fsScoreDicValue);
            map.put("yxScoreDimId", yxScoreDimId);
            map.put("yxScoreDicValue", yxScoreDicValue);
            map.put("orgId", deptId);
            mapsList.add(map);
        }
        
        return mapsList;
	}
}