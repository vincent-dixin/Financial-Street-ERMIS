package com.fhd.icm.web.controller.standard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.StandardBpmBO;
import com.fhd.icm.business.chart.AngularGauge;
import com.fhd.icm.business.chart.ChartColumn2D;
import com.fhd.icm.business.defect.DefectBO;
import com.fhd.icm.business.standard.StandardBO;
import com.fhd.icm.dao.standard.StandardDAO;
import com.fhd.icm.dao.standard.StandardRelaOrgDAO;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.web.form.StandardForm;

/**
 * STANDARD_内控Controller 
 * 
 * @author 元杰
 * @version
 */
@Controller
public class StandardControl {
	@Autowired
	private StandardBO o_standardBO;
	@Autowired
	private StandardBpmBO o_standardBpmBO;
	@Autowired
	private StandardDAO o_standardDAO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private StandardRelaOrgDAO o_standardRelaOrgDAO;
	@Autowired
	private DefectBO o_defectBO;
	
	/**
	 * <pre>
	 * 工作流调用,内控标准更新汇总,工作流结束自动触发
	 * 内控标准和内控标准下要求改为已完成状态
	 * </pre>
	 * 
	 * @author 元杰
	 * @param standardId
	 * @since  fhd　Ver 1.1
	*/
	public void mergeStandardStatus(String standardId) {
		//将内控标准和内控标准下要求的处理状态置为已完成
		StandardBO standardBO = ContextLoader.getCurrentWebApplicationContext().getBean(StandardBO.class);
		StandardDAO standardDAO = ContextLoader.getCurrentWebApplicationContext().getBean(StandardDAO.class);
		Standard standard = standardBO.findStandardById(standardId);
		standard.setDealStatus(Contents.DEAL_STATUS_FINISHED);
		standard.setStatus(Contents.STATUS_SOLVED);
		standardDAO.merge(standard);
		Set<Standard> standardControlList = standard.getChildren();
		Iterator<Standard> iterator = standardControlList.iterator();
		if(iterator.hasNext()){
			Standard standardControl = iterator.next();
			standardControl.setDealStatus(Contents.DEAL_STATUS_FINISHED);
			standardDAO.merge(standardControl);
		}
	}
	
	/**
	 * 终止内控标准流程
	 * @author 元杰
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/terminalStandardJBPM.f")
	public Map<String, Object> terminalStandardJBPM(String executionId, String businessId, String isPass, String examineApproveIdea, StandardForm standardForm, String step) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_jbpmBO.deletePorcessInstance(executionId);
		result.put("success", true);
		return result;
	}
	
	/**
	 * 保存内控标准以及下属的内控要求，仅保存
	 * @author 元杰
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/saveStandardData.f")
	public Map<String, Object> saveStandardData(StandardForm standardForm, String step) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_standardBO.saveStandardData(standardForm);
		result.put("success", true);
		return result;
	}
	
	/**
	 * 保存内控标准以及下属的内控要求，保存并触发工作流
	 * @author 元杰
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/saveStandard.f")
	public Map<String, Object> saveStandard(String executionId, String businessId, StandardForm standardForm, String step) throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> result = new HashMap<String, Object>();
		o_standardBO.saveStandard(executionId, businessId, standardForm, step);//触发JBPM
		result.put("success", true);
		return result;
	}
	
	/**
	 * 保存内控标准和要求的审批意见
	 * @author 元杰
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/saveStandardAdvice.f")
	public Map<String, Object> saveStandardAdvice(String executionId, String businessId, String isPass, String examineApproveIdea, StandardForm standardForm, String step) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_standardBO.saveStandardAdvice(standardForm);
		
		if("no".equals(isPass)){
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("path", isPass);
			variables.put("examineApproveIdea", examineApproveIdea);
			o_jbpmBO.doProcessInstance(executionId, variables);
		}else{
			//jbpm
			if("2".equals(step)){
				Standard standard = o_standardBO.findStandardById(businessId);
				Set<Standard> standardControlList = standard.getChildren();
				String[] standardControlIds = new String[standardControlList.size()];
				int i = 0;
				for(Standard standardControl : standardControlList){//取出内控标准下的要求中的部门集合，去重
					standardControlIds[i] = standardControl.getId();
					++i;
				}
				o_standardBpmBO.startCurCompanyStandardApplyStepTwo(executionId, businessId, standard, standardControlIds, isPass, examineApproveIdea);
			}else if("5".equals(step)){
				o_standardBpmBO.startCurCompanyStandardApplyStepFive(executionId, businessId, isPass, examineApproveIdea);
			}
		}
		result.put("success", true);
		return result;
	}
	
	/**
	 * 根据内控标准ID返回标准Form，如果没有，则新创建标准
	 * @author 元杰
	 * @param standardId 就是businessId
	 * @param executionId
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/findStandardById.f")
	public Map<String, Object> findStandardById(String standardId, String executionId, String step){
		return o_standardBO.findStandardJsonById(standardId, executionId, step);
	}
	
	/**
	 * 返回内控要求form数据
	 * @author 元杰
	 * @param standardControlId
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/findstandardControlById.f")
	public Map<String, Object> findstandardControlById(String standardControlId){
		return o_standardBO.findstandardControlById(standardControlId);
	}
	
	/**
	 * 初始化驾驶舱根据评价计划id查询监控指标的xml.
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/findStandardPlanChartXmlByCompanyId.f")
	public Map<String, Object> findAssessPlanChartXmlByCompanyId(String companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(companyId)){
			companyId = UserContext.getUser().getCompanyid();
		}
		
		//计划完成率--评价结果已完成/评价结果总数
		int finishCount = 0;
		String finishRate = "0.00";
		//制度缺陷率--(1-穿行测试所有合格的评价点/穿行评价点总数)
		String systemDefectRate = "0.00";
		//执行疏失率--(1-抽样测试所有合格的评价点/抽样评价点总数)
		String performErrorRate = "0.00";
//		
//		List<AssessResult> assessResultList = o_assessResultBO.findAssessResultListByCompanyId(companyId);
//		List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListByCompanyId(companyId);
//		for (AssessResult assessResult : assessResultList) {
//			//自动计算不为空
//			if(null != assessResult.getHasDefect()){
//				if(null != assessResult.getHasDefectAdjust()){
//					//人为调整不为空
//					if(assessResult.getHasDefectAdjust()){
//						//人为调整为'是'
//						if(assessResult.getHasDefect() != assessResult.getHasDefectAdjust()){
//							//人为调整与自动计算不一致，补充说明不为空
//							if(StringUtils.isNotBlank(assessResult.getAdjustDesc())){
//								finishCount++;
//							}
//						}
//					}else{
//						boolean flag = false;
//						//人为调整为'否'
//						for (AssessRelaDefect assessRelaDefect : assessRelaDefectList) {
//							if(assessResult.getAssessPoint().getId().equals(assessRelaDefect.getAssessPoint().getId())){
//								//人为调整为空，自动计算为否，缺陷描述存在
//								flag = true;
//							}
//						}
//						if(assessResult.getHasDefect() != assessResult.getHasDefectAdjust()){
//							//人为调整与自动计算不一致，补充说明不为空
//							if(StringUtils.isNotBlank(assessResult.getAdjustDesc())){
//								flag = true;
//							}
//						}
//						if(flag){
//							finishCount++;
//						}
//					}
//				}else{
//					//人为调整为空
//					if(!assessResult.getHasDefect()){
//						//自动计算为'否'
//						for (AssessRelaDefect assessRelaDefect : assessRelaDefectList) {
//							if(assessResult.getAssessPoint().getId().equals(assessRelaDefect.getAssessPoint().getId())){
//								//人为调整为空，自动计算为否，缺陷描述存在
//								finishCount++;
//							}
//						}
//					}else if(assessResult.getHasDefect()){
//						finishCount++;
//					}
//				}
//			}
//		}
//		DecimalFormat df = new DecimalFormat("0.00");
//		if(null != assessResultList && assessResultList.size()>0){
//			finishRate = df.format(((double)finishCount/assessResultList.size())*100);
//		}
//		
//		List<Object[]> assessMeasureTestList = o_defectBO.findAssessMeasureTestListByCompanyId(companyId);
//		for (Object[] objects : assessMeasureTestList) {
//			//制度缺陷率
//			if(null != objects[0] && Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(objects[0])){
//				systemDefectRate = df.format(Double.valueOf(String.valueOf(objects[1]))*100);
//			}
//			//执行疏失率
//			if(null != objects[0] && Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(objects[0])){
//				performErrorRate = df.format(Double.valueOf(String.valueOf(objects[1]))*100);
//			}
//		}
/*		//缺陷级别
		List<Object[]> defectLevelInfoList = o_defectBO.findDefectLevelListByCompanyId(companyId);
		//部门缺陷
		List<Object[]> orgDefectInfoList = o_defectBO.findOrgDefectListByCompanyId(companyId);
		
		map.put("finishRateXml", AngularGauge.getXml(finishRate, "","0"));
		map.put("finishRate", finishRate);
		map.put("systemDefectRateXml", AngularGauge.getXml(systemDefectRate, "","1"));
		map.put("systemDefectRate", systemDefectRate);
		map.put("performErrorRateXml", AngularGauge.getXml(performErrorRate, "","1"));
		map.put("performErrorRate", performErrorRate);
		map.put("defectLevelXml", ChartColumn2D.getXml("各个等级的缺陷数量统计图", "", "", "", defectLevelInfoList));
		map.put("orgDefectXml", ChartColumn2D.getXml("各个部门的缺陷数量统计图", "", "", "", orgDefectInfoList));*/
		return map;
	}
	/**
	 * 查询内控标准的列表
	 * @author 元杰
	 * @param name 内控标注名称
	 * @param companyId 公司ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/standard/findStandardByPage.f")
	public Map<String,Object> findStandardByPage(int limit, int start, String query, String name, String companyId){
		Map<String,Object> resultMap;
		Page<Standard> page = new Page<Standard>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		if(StringUtils.isBlank(companyId)){
			companyId = UserContext.getUser().getCompanyid();
		}
		resultMap=o_standardBO.findStandardBpmListByPage(page, query, companyId);
		return resultMap;
	}
}
