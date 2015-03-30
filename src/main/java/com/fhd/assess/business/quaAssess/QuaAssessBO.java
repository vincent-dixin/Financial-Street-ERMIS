package com.fhd.assess.business.quaAssess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.assess.dao.quaAssess.ScoreResultDAO;
import com.fhd.assess.entity.quaAssess.ScoreResult;
import com.fhd.assess.interfaces.quaAssess.IQuaAssessBO;

@Service
public class QuaAssessBO implements IQuaAssessBO {
	@Autowired
	private ScoreResultDAO o_scoreResultDAO;
	
	/**
	 * 保存更新打分结果
	 * @param rangObjectDeptEmp 综合实体
	 * @author 金鹏祥
	 * */
	@Override
	@Transactional
	public void mergeScoreResult(ScoreResult scoreResult) {
		o_scoreResultDAO.merge(scoreResult);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
//	@Override
//	public ArrayList<HashMap<String, String>> findDeptOrKpiByScoreEmpId(String scoreEmpId, String scoreRangeName, String templateId) {
//		StringBuffer sql = new StringBuffer();
//        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
//        
//        sql.append(" select i.id, b.strategy_map_id, d.strategy_map_name, a.kpi_id, e.kpi_name,  " +
//        		"g.id as parentRiskId, g.risk_name as parentRiskName ," +
//        		"a.risk_id as riskId, c.risk_name as riskName ");
//        sql.append(" from t_kpi_kpi_rela_risk a, T_KPI_SM_RELA_KPI b, t_rm_risks c, t_kpi_strategy_map d, t_kpi_kpi e , t_rm_risks g, ");
//        sql.append(" (select a.id, b.risk_id, a.SCORE_RANGE_ID ");
//        sql.append(" from T_RM_RANG_OBJECT_DEPT_EMP a,  T_RM_RISK_SCORE_OBJECT b ");
//        sql.append(" where a.score_emp_id = '" + scoreEmpId + "' ");
//        sql.append(" and a.score_range_name='" + scoreRangeName + "' ");
//        sql.append(" and  a.score_object_id = b.id) as i ");
//        sql.append(" where a.risk_id = i.risk_id and a.etype='i' ");
//        sql.append(" and a.kpi_id = b.kpi_id ");
//        sql.append(" and a.risk_id=c.id ");
//        sql.append(" and d.id = b.strategy_map_id ");
//        sql.append(" and e.id = a.kpi_id ");
//        sql.append(" and g.id = c.parent_id ");
//        
//        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
//        @SuppressWarnings("unchecked")
//		List<Object[]> list = sqlQuery.list();
//        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
//            Object[] objects = (Object[]) iterator.next();
//            
//            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
//            String strategyMapId = "";//目标ID
//            String strategyMapName = "";//目标名称
//            String kpiId = "";//指标ID
//            String kpiName = "";//指标名称
//            String parentRiskId= "";//上级风险ID
//            String parentRiskName = "";//上级风险名称
//            String riskId = "";//风险ID
//            String riskName = "";//上级风险名称d
//            
//            if(null != objects[0]){
//            	rangObjectDeptEmpId = objects[0].toString();
//            }if(null != objects[1]){
//            	strategyMapId = objects[1].toString();
//            }if(null != objects[2]){
//            	strategyMapName = objects[2].toString();
//            }if(null != objects[3]){
//            	kpiId = objects[3].toString();
//            }if(null != objects[4]){
//            	kpiName = objects[4].toString();
//            }if(null != objects[5]){
//            	parentRiskId = objects[5].toString();
//            }if(null != objects[6]){
//            	parentRiskName = objects[6].toString();
//            }if(null != objects[7]){
//            	riskId = objects[7].toString();
//            }if(null != objects[8]){
//            	riskName = objects[8].toString();
//            }
//            
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
//            map.put("strategyMapId", strategyMapId);
//            map.put("strategyMapName", strategyMapName);
//            map.put("kpiId", kpiId);
//            map.put("kpiName", kpiName);
//            map.put("parentRiskId", parentRiskId);
//            map.put("parentRiskName", parentRiskName);
//            map.put("riskId", riskId);
//            map.put("riskName", riskName);
//            map.put("templateId", templateId);
//            arrayList.add(map);
//        }
//        
//        return arrayList;
//	}

	
	
	

//	@Override
//	public ArrayList<HashMap<String, String>> findProcessureByScoreEmpId(
//			String scoreEmpId, String scoreRangeName, String templateId) {
//		StringBuffer sql = new StringBuffer();
//        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
//        
//        sql.append(" select i.id,b.id, b.processure_name, g.id, g.risk_name ,a.risk_id, c.risk_name  from  ");
//        sql.append(" T_PROCESSURE_RISK_PROCESSURE a, T_IC_PROCESSURE b, t_rm_risks c, t_rm_risks g , ");
//        sql.append(" (select a.id, b.risk_id, a.SCORE_RANGE_ID ");
//        sql.append(" from T_RM_RANG_OBJECT_DEPT_EMP a,  T_RM_RISK_SCORE_OBJECT b ");
//        sql.append(" where a.score_emp_id = '" + scoreEmpId + "' ");
//        sql.append(" and a.score_range_name='" + scoreRangeName + "' ");
//        sql.append(" and  a.score_object_id = b.id) as i ");
//        sql.append(" where a.risk_id = i.risk_id ");
//        sql.append(" and a.etype = 'i' ");
//        sql.append(" and g.id = c.parent_id ");
//        sql.append(" and a.risk_id=c.id ");
//        sql.append(" and a.processure_id =b.id ");
//        
//        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
//        @SuppressWarnings("unchecked")
//		List<Object[]> list = sqlQuery.list();
//        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
//            Object[] objects = (Object[]) iterator.next();
//            
//            String rangObjectDeptEmpId = "";
//            String processureId = "";
//            String processureName = "";
//            String parentRiskId= "";
//            String parentRiskName = "";
//            String riskId = "";
//            String riskName = "";	
//            
//            if(null != objects[0]){
//            	rangObjectDeptEmpId = objects[0].toString();
//            }if(null != objects[1]){
//            	processureId = objects[1].toString();
//            }if(null != objects[2]){
//            	processureName = objects[2].toString();
//            }if(null != objects[3]){
//            	parentRiskId = objects[3].toString();
//            }if(null != objects[4]){
//            	parentRiskName = objects[4].toString();
//            }if(null != objects[5]){
//            	riskId = objects[5].toString();
//            }if(null != objects[6]){
//            	riskName = objects[6].toString();
//            }
//            
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
//            map.put("processureId", processureId);
//            map.put("processureName", processureName);
//            map.put("parentRiskId", parentRiskId);
//            map.put("parentRiskName", parentRiskName);
//            map.put("riskId", riskId);
//            map.put("riskName", riskName);
//            map.put("templateId", templateId);
//            arrayList.add(map);
//        }
//        
//        return arrayList;
//	}
	
	

	

//	/**通过人员ID、范围名称查询评估记录(风险事件)(
//	 * @param scoreEmpId 人员ID
//	 * @param templateId 模板ID
//	 * @return ArrayList<HashMap<String, String>>
//	 * @author 金鹏祥
//	 * */
//	public ArrayList<HashMap<String, String>> findRiskByScoreEmpId(
//			String scoreEmpId, String templateId, boolean isAlarm) {
//		StringBuffer sql = new StringBuffer();
//        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
//        
////        ArrayList<HashMap<String, String>> groupList = null;
////		ArrayList<HashMap<String, String>> infoList = null;
////		HashMap<String, TemplateRelaDimension> templateRelaDimensionMap = null;
////		ArrayList<ArrayList<String>> AllList = null;
////		HashMap<String, AlarmPlan> alarmPlanAllMap = null;
////		Map<String, Risk> riskAllMap = null;
////		String riskIcon = "";
////        
////        if(isAlarm){
////        	groupList = this.findScoreResultByRangObjectDeptEmpIdAndTemplateIdGroupMapALL();
////        	infoList = this.findScoreResultByRangObjectDeptEmpIdAndTemplateIdMapALL();
////        	templateRelaDimensionMap = this.findTemplateRelaDimensionAllMap();
////        	AllList = this.findScoreResultAllList();
////        	alarmPlanAllMap = this.findAlarmPlanAllMap();
////        	riskAllMap = o_risRiskService.getAllRiskInfo();
////        }
////        
////        sql.append(" select i.id,g.id as parentRiskId, g.risk_name as parentRiskName ,c.id as riskId, c.risk_name as riskName, c.template_id ");
////        sql.append(" from t_rm_risks c, t_rm_risks g, ");
////        sql.append(" (select a.id, b.risk_id from T_RM_RANG_OBJECT_DEPT_EMP a,  T_RM_RISK_SCORE_OBJECT b ");
////        sql.append(" where a.score_emp_id = '" + scoreEmpId + "' ");// and a.score_range_name is null  or a.score_range_name='' 
////        sql.append(" and  a.score_object_id = b.id GROUP BY b.risk_id) as i ");
////        sql.append(" where  g.id = c.parent_id and i.risk_id = c.id GROUP BY c.risk_name ");
////        
////        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
////        @SuppressWarnings("unchecked")
////		List<Object[]> list = sqlQuery.list();
////        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
////            Object[] objects = (Object[]) iterator.next();
////            
////            String rangObjectDeptEmpId = "";
////            String parentRiskId= "";
////            String parentRiskName = "";
////            String riskId = "";
////            String riskName = "";
////            String myTemplateId = "";
////            
////            if(null != objects[0]){
////            	rangObjectDeptEmpId = objects[0].toString();
////            }if(null != objects[1]){
////            	parentRiskId = objects[1].toString();
////            }if(null != objects[2]){
////            	parentRiskName = objects[2].toString();
////            }if(null != objects[3]){
////            	riskId = objects[3].toString();
////            }if(null != objects[4]){
////            	riskName = objects[4].toString();
////            }if(null != objects[5]){
////            	myTemplateId = objects[5].toString();
////            }
////            
////            HashMap<String, String> map = new HashMap<String, String>();
////            map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
////            map.put("parentRiskId", parentRiskId);
////            map.put("parentRiskName", parentRiskName);
////            map.put("riskId", riskId);
////            map.put("riskName", riskName);
////            if(isAlarm){
////            	riskIcon = this.getRiskIcon(
////            			riskId, 
////            			templateId, 
////            			rangObjectDeptEmpId, 
////            			groupList, 
////            			infoList, 
////            			templateRelaDimensionMap, 
////            			AllList, 
////            			alarmPlanAllMap,
////            			riskAllMap);
////            	map.put("riskIcon", riskIcon);
////            }
////            if(templateId.equalsIgnoreCase("dim_template_type_self")){
////            	//自有模板
////            	map.put("templateId", myTemplateId);
////            }else{
////            	map.put("templateId", templateId);
////            }
////            
////            arrayList.add(map);
////        }
//        
//        return arrayList;
//	}

//	/**
//	 * 评估保存操作
//	 * @param params 前台JSON
//	 * @author 金鹏祥
//	 * */
//	@Transactional
//	public boolean assessSaveOper(String params) {
//		JSONArray jsonarr = JSONArray.fromObject(params);
//		try {
//			for (Object objects : jsonarr) {
//				JSONObject jsobjs = (JSONObject) objects;
//				
//				String dimId = jsobjs.get("dimId").toString();
//				String rangObjectDeptEmpId = jsobjs.get("rangObjectDeptEmpId").toString();
//				double dimValue = Double.parseDouble(jsobjs.get("dimValue").toString());
//				RangObjectDeptEmp rangObjectDeptEmp = new RangObjectDeptEmp();
//				Dimension dimension = new Dimension();
//				Score score = new Score();
//				ScoreResult scoreResult = new ScoreResult();
//				String scoreId = this.findScoreBydimensionIdAndValue(dimId, 
//						dimValue);
//				List<ScoreResult> scoreResultList = this.findScoreBydimensionIdAndDicIdAndRangObjectDeptEmpId(dimId, rangObjectDeptEmpId);
//				
//				if(scoreResultList.size() != 0){
//					//修改打分结果
//					scoreResult.setId(scoreResultList.get(0).getId());
//				}else{
//					//添加打分结果
//					scoreResult.setId(Identities.uuid());
//				}
//				
//				rangObjectDeptEmp.setId(rangObjectDeptEmpId);
//				dimension.setId(dimId);
//				score.setId(scoreId);
//				
//				scoreResult.setDimension(dimension);
//				scoreResult.setScore(score);
//				scoreResult.setRangObjectDeptEmpId(rangObjectDeptEmp);
//				scoreResult.setSubmitTime(new Date());
//				scoreResult.setApproval(false);
//				scoreResult.setStatus(null);
//				
//				this.mergeScoreResult(scoreResult);
//			}
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	

	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}