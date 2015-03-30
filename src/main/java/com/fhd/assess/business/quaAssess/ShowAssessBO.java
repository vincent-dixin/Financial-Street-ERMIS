package com.fhd.assess.business.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.assess.business.formulatePlan.RiskAssessPlanBO;
import com.fhd.assess.dao.kpiSet.RangObjectDeptEmpDAO;
import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.comm.business.formula.StatisticFunctionCalculateBO;
import com.fhd.comm.dao.AlarmPlanDAO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.kpi.business.KpiBO;
import com.fhd.risk.business.RiskService;
import com.fhd.risk.dao.TemplateRelaDimensionDAO;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.TemplateRelaDimension;

@Service
public class ShowAssessBO {

	@Autowired
	private RangObjectDeptEmpDAO o_rangObjectDeptEmpDAO;
	
	@Autowired
	private TemplateRelaDimensionDAO o_templateRelaDimensionDAO;
	
	@Autowired
	private AlarmPlanDAO o_alarmPlanDAO;
	
	@Autowired
	private RiskService o_risRiskService;
	
	@Autowired
	private KpiBO o_kpiBo;
	
	@Autowired
	private RiskAssessPlanBO o_riskAssessPlanBO;
	
	/**
	 * 分组集合
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	private ArrayList<HashMap<String, String>> findScoreResultByRangObjectDeptEmpIdAndTemplateIdGroupMapALL() {
		StringBuffer sql = new StringBuffer();
        
		ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        
		sql.append(" select a.RANG_OBJECT_DEPT_EMP_ID,c.score_dim_id,c.parent_template_dim_id, c.template_id   " +
        		" from t_rm_score_result a , " +
        		" t_dim_score_dic b, ");
        sql.append(" (select score_dim_id,parent_template_dim_id,template_id from	t_dim_template_rela_dim ) c ");
//        sql.append(" where a.rang_object_dept_emp_id='z3' ");
        sql.append(" where a.score_dic_id=b.id  ");
        sql.append(" and c.score_dim_id=b.score_dim_id  ");
        sql.append("  GROUP BY c.parent_template_dim_id ");
      
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            //String scoreDimId = "";//维度ID
            String parentTemplateDimId = "";//上级维度ID
            String templateId = "";//模板ID
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }
//            if(null != objects[1]){
//            	scoreDimId = objects[1].toString();
//            }
            
            if(null != objects[2]){
            	parentTemplateDimId = objects[2].toString();
            }if(null != objects[3]){
            	templateId = objects[3].toString();
            }
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(rangObjectDeptEmpId + "--" + templateId, parentTemplateDimId);
            maps.add(map);
        }
        
        return maps;
	}
	
	/**
	 * 信息集合
	 * @return ArrayList<HashMap<String, String>>
	 * @author 金鹏祥
	 * */
	private ArrayList<HashMap<String, String>> findScoreResultByRangObjectDeptEmpIdAndTemplateIdMapALL() {
		StringBuffer sql = new StringBuffer();
        
		ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        
		sql.append(" select  a.RANG_OBJECT_DEPT_EMP_ID,b.SCORE_DIC_value,c.score_dim_id,c.parent_template_dim_id, c.template_id  from    " +
        		" t_rm_score_result a , " +
        		" t_dim_score_dic b, ");
        sql.append(" (select score_dim_id,parent_template_dim_id,template_id from t_dim_template_rela_dim) c  ");
//        sql.append(" where a.rang_object_dept_emp_id='z3' ");
        sql.append(" where a.score_dic_id=b.id ");
        sql.append(" and c.score_dim_id=b.score_dim_id ");
        sql.append(" ORDER BY parent_template_dim_id ");
        
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            //String scoreDimId = "";//维度ID
            String scoreDicValue = "";//维度Value
            String parentTemplateDimId = "";//上级维度ID
            String templateId = "";//模板ID
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	scoreDicValue = objects[1].toString();
            }
//            if(null != objects[2]){
//            	scoreDimId = objects[2].toString();
//            }
            
            if(null != objects[3]){
            	parentTemplateDimId = objects[3].toString();
            }if(null != objects[4]){
            	templateId = objects[4].toString();
            }
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(rangObjectDeptEmpId + "--" + templateId + "--" + parentTemplateDimId, scoreDicValue);
            maps.add(map);
        }
        
        return maps;
	}
	
	/**
	 * 查询模版关联维度全部信息,并已MAP方式存储
	 * @author 金鹏祥
	 * @return List<TemplateRelaDimension>
	 * */
	@SuppressWarnings("unchecked")
	private HashMap<String, TemplateRelaDimension> findTemplateRelaDimensionAllMap(){
		HashMap<String, TemplateRelaDimension> map = new HashMap<String, TemplateRelaDimension>();
		Criteria criteria = o_templateRelaDimensionDAO.createCriteria();
		List<TemplateRelaDimension> list = null;
		list = criteria.list();
		
		for (TemplateRelaDimension templateRelaDimension : list) {
			map.put(templateRelaDimension.getId(), templateRelaDimension);
		}
		
		return map;
	}
	
	
	/**
	 * 结果集合
	 * @return ArrayList<ArrayList<String>>
	 * @author 金鹏祥
	 * */
	public ArrayList<ArrayList<String>> findScoreResultAllList() {
		StringBuffer sql = new StringBuffer();
        
		ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
        
		sql.append(" select a.RANG_OBJECT_DEPT_EMP_ID,b.SCORE_DIC_value ");
		sql.append(" from t_rm_score_result a , ");
		sql.append(" t_dim_score_dic b ");
//		sql.append(" where  a.rang_object_dept_emp_id='" + rangObjectDeptEmpId + "' ");
//		sql.append(" and a.score_dic_id=b.id  ");
		sql.append(" where a.score_dic_id=b.id ");
		
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
           
            String rangObjectDeptEmpId = "";//范围-对象-部门-人员综合表ID
            String scoreDicValue = "";//维度Value
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	scoreDicValue = objects[1].toString();
            }
            
            ArrayList<String> lists2 = new ArrayList<String>();
            lists2.add(rangObjectDeptEmpId);
            lists2.add(scoreDicValue);
            lists.add(lists2);
        }
        
        return lists;
	}
	
	/**
	 * 查询所有告警方案实体并已MAP方式存储
	 * @author 金鹏祥
	 * @return HashMap<String, AlarmPlan>
	 * */
	@SuppressWarnings("unchecked")
	private HashMap<String, AlarmPlan> findAlarmPlanAllMap(){
		HashMap<String, AlarmPlan> map = new HashMap<String, AlarmPlan>();
		Criteria criteria = o_alarmPlanDAO.createCriteria();
		criteria.addOrder(Order.asc("sort"));
		List<AlarmPlan> list = null;
		list = criteria.list();
		
		for (AlarmPlan alarmPlan : list) {
			map.put(alarmPlan.getId(), alarmPlan);
		}
		
		return map;
	}
	
	/**
	 * 过滤集合
	 * @param findScoreResultAllList 结果集合
	 * @param templateId 模板ID
	 * @param rangObjectDeptEmpId 综合ID
	 * @return ArrayList<ArrayList<Object>>
	 * @author 金鹏祥
	 * */
	private ArrayList<ArrayList<Object>> getScoreResultByRangObjectDeptEmpIdList(
			ArrayList<ArrayList<String>> findScoreResultAllList, 
			String rangObjectDeptEmpId, String templateId){
		
		ArrayList<ArrayList<Object>> lists = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> list = null;
		
		for (ArrayList<String> arrayList : findScoreResultAllList) {
			for (String string : arrayList) {
				if(string.equalsIgnoreCase(rangObjectDeptEmpId)){
					list = new ArrayList<Object>();
					list.add(arrayList.get(0));
					list.add(Double.parseDouble(arrayList.get(1)));
					lists.add(list);
				}
			}
		}
		return lists;
	}
	
	/**
	 * 得到风险水平分
	 * @param scoreDicValue1 维度1
	 * @param scoreDicValue2 维度2
	 * @return double
	 * @author 金鹏祥
	 * */
	private double getRiskScore(double scoreDicValue1, double scoreDicValue2){
		double riskScoreValue = 0l;
		riskScoreValue = scoreDicValue1 * scoreDicValue2;
		return riskScoreValue;
	}
	
	/**
	 * 判断类型并计算取值
	 * @param calculateMethod 取值类型
	 * @param scoreDimWeight 维度权重
	 * @param scoreDicValue 纬度值
	 * @return double
	 * @author 金鹏祥
	 * */
	private double getValue(String calculateMethod, double scoreDimWeight, double[] scoreDicValue){
		double value = 0l;
		
		if("dim_calculate_method_avg".equalsIgnoreCase(calculateMethod)){
			value = StatisticFunctionCalculateBO.ma(scoreDicValue);
		}else if("dim_calculate_method_max".equalsIgnoreCase(calculateMethod)){
			value = StatisticFunctionCalculateBO.max(scoreDicValue);
		}else if("dim_calculate_method_min".equalsIgnoreCase(calculateMethod)){
			value = StatisticFunctionCalculateBO.min(scoreDicValue);
		}else if("dim_calculate_method_weight_avg".equalsIgnoreCase(calculateMethod)){
			
		}
		
		return value;
	}
	
	/**
	 * 得到风险评估灯
	 * @param riskId 风险ID
	 * @param templateId 模板ID
	 * @param rangObjectDeptEmpId 综合ID
	 * @param groupList 分组集合
	 * @param infoList 信息集合
	 * @param map 模板维度关联所有信息
	 * @param AllList 过滤集合
	 * @param alarmPlanAllMap 告警集合
	 * @param riskAllMap 风险集合
	 * @return String
	 * @author 金鹏祥
	 * */
	private String getRiskIcon(String riskId, 
			String templateId,
			String rangObjectDeptEmpId, 
			ArrayList<HashMap<String, String>> groupList, 
			ArrayList<HashMap<String, String>> infoList, 
			HashMap<String, TemplateRelaDimension> map,
			ArrayList<ArrayList<String>> AllList,
			HashMap<String, AlarmPlan> alarmPlanAllMap,
			Map<String, Risk> riskAllMap){
		
		String parentTemplateDimId = "";
		ArrayList<Double> scoreDicValuelist = new ArrayList<Double>();
		double riskScoreValue = 0l;
		String riskIcon = "";
		
		ArrayList<ArrayList<Object>> lists = getScoreResultByRangObjectDeptEmpIdList(AllList, rangObjectDeptEmpId, templateId);
		
		if(lists.size() != 0){
			if(lists.size() <= 2){
				//一个维度
				String[] tempScoreDicValue = null;
				String scoreDicValues = "";
				for (ArrayList<Object> arrayList : lists) {
					scoreDicValues += arrayList.get(1) + "--";
				}
				
				tempScoreDicValue = scoreDicValues.split("--");
				riskScoreValue = this.getRiskScore(Double.parseDouble(tempScoreDicValue[0]), Double.parseDouble(tempScoreDicValue[1]));//得到风险水平分
			}else{
				//多个维度
				for (HashMap<String, String> groupMap : groupList) {
					if(groupMap.get(rangObjectDeptEmpId + "--" + templateId) != null){
						parentTemplateDimId = groupMap.get(rangObjectDeptEmpId + "--" + templateId);
						String calculateMethod = map.get(parentTemplateDimId).getCalculateMethod().getId();//计算方式
						double scoreDimWeight = map.get(parentTemplateDimId).getWeight();//权重
						double[] scoreDicValue = null;
						String[] tempScoreDicValue = null;
						String scoreDicValues = "";
						double scoreDimValue = 0l;
						
						for (HashMap<String, String> infoMap : infoList) {
							if(infoMap.get(rangObjectDeptEmpId + "--" + templateId + "--" + parentTemplateDimId) != null){
								scoreDicValues += infoMap.get(rangObjectDeptEmpId + "--" + templateId + "--" + parentTemplateDimId) + "--";
							}
						}
						tempScoreDicValue = scoreDicValues.split("--");
						scoreDicValue = new double[tempScoreDicValue.length];
						for (int i = 0; i < scoreDicValue.length; i++) {
							scoreDicValue[i] = Double.parseDouble(tempScoreDicValue[i]);
						}
						
						scoreDimValue = this.getValue(calculateMethod, scoreDimWeight, scoreDicValue);//维度分值
						scoreDicValuelist.add(scoreDimValue);
					}
				}
				riskScoreValue = this.getRiskScore(scoreDicValuelist.get(0), scoreDicValuelist.get(1));//得到风险水平分
			}
			
			//得到风险灯
			riskIcon = o_kpiBo.findKpiAlarmStatusByValues(alarmPlanAllMap.get(riskAllMap.get(riskId).getAlarmScenario().getId()), riskScoreValue).getValue();
			
		}
		return riskIcon;
	}
	
	public ArrayList<HashMap<String, String>> findRiskByScoreEmpId(
			String scoreEmpId, String templateId, boolean isAlarm) {
		StringBuffer sql = new StringBuffer();
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
        
        ArrayList<HashMap<String, String>> groupList = null;
		ArrayList<HashMap<String, String>> infoList = null;
		HashMap<String, TemplateRelaDimension> templateRelaDimensionMap = null;
		ArrayList<ArrayList<String>> AllList = null;
		HashMap<String, AlarmPlan> alarmPlanAllMap = null;
		Map<String, Risk> riskAllMap = null;
		String riskIcon = "";
        
        if(isAlarm){
        	groupList = this.findScoreResultByRangObjectDeptEmpIdAndTemplateIdGroupMapALL();
        	infoList = this.findScoreResultByRangObjectDeptEmpIdAndTemplateIdMapALL();
        	templateRelaDimensionMap = this.findTemplateRelaDimensionAllMap();
        	AllList = this.findScoreResultAllList();
        	alarmPlanAllMap = this.findAlarmPlanAllMap();
        	riskAllMap = o_risRiskService.getAllRiskInfo();
        }
        
        sql.append(" select i.id,g.id as parentRiskId, g.risk_name as parentRiskName ,c.id as riskId, c.risk_name as riskName, c.template_id ");
        sql.append(" from t_rm_risks c, t_rm_risks g, ");
        sql.append(" (select a.id, b.risk_id from T_RM_RANG_OBJECT_DEPT_EMP a,  T_RM_RISK_SCORE_OBJECT b ");
        sql.append(" where a.score_emp_id = '" + scoreEmpId + "' ");// and a.score_range_name is null  or a.score_range_name='' 
        sql.append(" and  a.score_object_id = b.id GROUP BY b.risk_id) as i ");
        sql.append(" where  g.id = c.parent_id and i.risk_id = c.id GROUP BY c.risk_name ");
        
        SQLQuery sqlQuery = o_rangObjectDeptEmpDAO.createSQLQuery(sql.toString());
        @SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            
            String rangObjectDeptEmpId = "";
            String parentRiskId= "";
            String parentRiskName = "";
            String riskId = "";
            String riskName = "";
            String myTemplateId = "";
            
            if(null != objects[0]){
            	rangObjectDeptEmpId = objects[0].toString();
            }if(null != objects[1]){
            	parentRiskId = objects[1].toString();
            }if(null != objects[2]){
            	parentRiskName = objects[2].toString();
            }if(null != objects[3]){
            	riskId = objects[3].toString();
            }if(null != objects[4]){
            	riskName = objects[4].toString();
            }if(null != objects[5]){
            	myTemplateId = objects[5].toString();
            }
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rangObjectDeptEmpId", rangObjectDeptEmpId);
            map.put("parentRiskId", parentRiskId);
            map.put("parentRiskName", parentRiskName);
            map.put("riskId", riskId);
            map.put("riskName", riskName);
            if(isAlarm){
            	riskIcon = this.getRiskIcon(
            			riskId, 
            			templateId, 
            			rangObjectDeptEmpId, 
            			groupList, 
            			infoList, 
            			templateRelaDimensionMap, 
            			AllList, 
            			alarmPlanAllMap,
            			riskAllMap);
            	map.put("riskIcon", riskIcon);
            }
            if(templateId.equalsIgnoreCase("dim_template_type_self")){
            	//自有模板
            	map.put("templateId", myTemplateId);
            }else{
            	map.put("templateId", templateId);
            }
            
            arrayList.add(map);
        }
        
        return arrayList;
	}
	
	/**
	 * 通过风险评估ID得到评估模板ID
	 * @param assessPlanId 评估ID
	 * @return String
	 * @author 金鹏祥
	 * */
	public String getTemplateId(String assessPlanId){
		
		String templateId = "";
		
		RiskAssessPlan riskssessPlan = o_riskAssessPlanBO.findRiskAssessPlanById(assessPlanId);
		String templateType = riskssessPlan.getTemplateType();//模板类型
		
		if("dim_template_type_sys".equalsIgnoreCase(templateType)){//系统模板
			templateId = riskssessPlan.getTemplate().getId();
		}else if("dim_template_type_self".equalsIgnoreCase("dim_template_type_self")){//自有模板
			templateId = "dim_template_type_self";
		}
		
		return templateId;
	}
}