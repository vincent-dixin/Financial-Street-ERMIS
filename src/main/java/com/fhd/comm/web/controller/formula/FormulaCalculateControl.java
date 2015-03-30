package com.fhd.comm.web.controller.formula;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.comm.business.formula.FormulaObjectRelationBO;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.formula.FormulaObjectRelation;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.StrategyMap;

/**
 * 公式计算Controller.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-28		上午10:51:15
 * @see 	 
 */
@Controller
public class FormulaCalculateControl {
	
	@Autowired
	private FormulaCalculateBO o_formulaCalculateBO;
	@Autowired
	private KpiGatherResultBO o_kpiGatherResultBO;
	@Autowired
	private FormulaObjectRelationBO o_formulaObjectRelationBO;
	@Autowired
	private KpiBO o_kpiBO;
	@Autowired
	private CategoryBO o_categroyBO;
	@Autowired
	private StrategyMapBO o_strategyMapBO;
	
	/**
	 * 验证公式.
	 * @param id 要计算的指标或风险id
	 * @param type kpi或risk
	 * @param objectColumn 对象所属列
	 * @param formula 公式
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/formula/validateFormula.f")
	public void validateFormula(String id, String type, String objectColumn, String formula, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		if(StringUtils.isBlank(formula)){
		    try{
		        out.print("true");
		    }catch (Exception e) {
	            e.printStackTrace();
	            out.print("false");
	        }finally{
	            if(null != out){
	                out.close();
	            }
	        }
		}
		String relaObjectId = "";
		String relaObjectType = "";
		String relaObjectColumn = "";
		
		try {
			String targetName = "";
			if("kpi".equals(type)){
				Kpi kpi = o_kpiBO.findKpiById(id);
				if(null != kpi){
					targetName = kpi.getName();
				}
			}else if("category".equals(type)){
				//记分卡
				Category category = o_categroyBO.findCategoryById(id);
				if(null != category){
					targetName = category.getName();
				}
			}else if("strategy".equals(type)){
				//战略目标
				StrategyMap strategyMap = o_strategyMapBO.findById(id);
				if(null != strategyMap){
					targetName = strategyMap.getName();
				}
			}else if("risk".equals(type)){
				//风险
				
			}
			
			if("kpi".equals(type)){
				//指标
				/**
				 * 后台验证循环嵌套
				 * Map<String, String> map格式：{name:'',type:'',originalName:''}
				 */
				List<String> nameList = new ArrayList<String>();
				List<Map<String, String>> result = o_formulaCalculateBO.decompositionFormulaString(targetName, formula);
				for(Map<String, String> m : result){
					String name = m.get("name");
					if(!nameList.contains(name)){
						nameList.add(name);
					}
				}
				/*
				 * 根据指标名称批量查询对应的指标采集结果list
				 * @param nameList
				 */
				List<KpiGatherResult> kpiGatherResultList = o_kpiGatherResultBO.findKpiGatherResultListByKpiNames(nameList, "");
				for(Map<String, String> map : result){
					for(KpiGatherResult kpiGatherResult : kpiGatherResultList){
						//指标id
						if(null != kpiGatherResult.getKpi()){
							relaObjectId = kpiGatherResult.getKpi().getId();
						}
						relaObjectType = "kpi";
						relaObjectColumn = "";
						if(Contents.TARGET_VALUE.equals(map.get("type"))){
							relaObjectColumn = "targetValueFormula";
						}else if(Contents.RESULT_VALUE.equals(map.get("type"))){
							relaObjectColumn = "resultValueFormula";
						}else if(Contents.ASSEMENT_VALUE.equals(map.get("type"))){
							relaObjectColumn = "assessmentValueFormula";
						}
						
						/*
						 * 判断是否存在循环嵌套,存在循环嵌套返回错误提示
						 * 例如:
						 * a).若kpiA%结果值 = kpiB + kpiC, kpiC = kpiA%结果值 - 50, 则存在循环调用；
						 * b).若kpiA%结果值  = kpiA%结果值*80%，则存在循环调用
						 * 说明：若kpiA%结果值  = kpiA%目标值*80%，则不存在循环调用
						 */
						//a情况验证
						List<FormulaObjectRelation> formulaObjectRelationList = o_formulaObjectRelationBO.findFormulaObjectRelationListBySome(relaObjectId, relaObjectType, relaObjectColumn, id, type, objectColumn);
						if(null != formulaObjectRelationList && formulaObjectRelationList.size()>0){
							out.print("false");
							return;
						}
						//b情况验证
						if("id".equals(relaObjectId) && "type".equals(relaObjectType) && "objectColumn".equals(relaObjectColumn)){
							out.print("false");
							return;
						}
					}
				}
			}else if("category".equals(type)){
				//记分卡
				
			}else if("strategy".equals(type)){
				//战略目标
				
			}else if("risk".equals(type)){
				//风险
				
			}
			
			out.print("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("false");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * 计算公式.
	 * @param id 要计算的指标或风险id
	 * @param type kpi或risk
	 * @param formula 公式
	 * @param timePeriodId 时间区间维id
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/formula/calculateFormula.f")
	public void calculateFormula(String id, String type, String formula, String timePeriodId, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		
		String targetName = "";
		if("kpi".equals(type)){
			Kpi kpi = o_kpiBO.findKpiById(id);
			if(null != kpi){
				targetName = kpi.getName();
			}
		}else if("category".equals(type)){
			//记分卡
			
		}else if("strategy".equals(type)){
			//战略目标
			
		}else if("risk".equals(type)){
			//风险
			
		}
		try {
			String ret = o_formulaCalculateBO.calculate(targetName, type, formula, timePeriodId);
			
			out.print(ret);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("计算失败");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
}
