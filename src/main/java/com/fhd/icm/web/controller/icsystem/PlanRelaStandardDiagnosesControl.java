package com.fhd.icm.web.controller.icsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.bpm.icsystem.ConstructPlanBpmBO;
import com.fhd.icm.business.icsystem.ConstructPlanBO;
import com.fhd.icm.business.icsystem.ConstructPlanRelaStandardBO;
import com.fhd.icm.business.icsystem.PlanRelaStandardDiagnosesBO;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.icm.entity.icsystem.Diagnoses;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.web.controller.bpm.icsystem.ConstructPlanBpmObject;
import com.fhd.icm.web.form.icsystem.DefectClearUpGroupForm;
import com.fhd.icm.web.form.icsystem.GroupForm;
import com.fhd.process.entity.ProcessRelaStandard;
import com.fhd.sys.dao.dic.DictEntryDAO;

/**
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-12		上午9:16
 *
 * @see 	 
 */
@Controller
public class PlanRelaStandardDiagnosesControl {
	
	@Autowired
	private PlanRelaStandardDiagnosesBO o_planRelaStandardDiagnosesBO;
	@Autowired
	private ConstructPlanRelaStandardBO o_constructPlanRelaStandardBO;
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private ConstructPlanBpmBO o_constructPlanBpmBO;
	/**
	 * 
	 * <pre>
	 *   批量保存标准对应
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanJson :grid中修改过的数据集合
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/mergeconstructplanreladiagnosesbatch.f")
	public Map<String, Object> mergeConstructPlanRelaDiagnosesBatch(String modifiedRecord,String type) {
		Map<String, Object> constructPlanMap = new HashMap<String, Object>();
		o_planRelaStandardDiagnosesBO.mergeConstructPlanRelaDiagnosesBatch(modifiedRecord,type);
		constructPlanMap.put("success", true);
		return constructPlanMap;
	}
	

	

	/**
	 * <pre>
	 * 内控维护流程范围列表中添加流程功能
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessMeasureId :穿行，抽样，全部
	 * @param processIds  ：流程Ids，多选是以‘,’间隔的字符串
	 * @param assessPlanId 相关的内控评价计划Id
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/savediagnoses.f")
	public Map<String, Object> saveConstructPlanRelaStandard(String standardIds, String constructPlanId) {
		Map<String, Object> constructPlanMap = new HashMap<String, Object>();
		o_planRelaStandardDiagnosesBO.saveConstructPlanRelaStandard(standardIds,constructPlanId);
		constructPlanMap.put("success", true);
		return constructPlanMap;
	}
	/**
	 * <pre>
	 * 查询评价计划关联的流程范围
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param query 查询条件
	 * @param constructPlanId 评价计划ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findplanrelastandarddiagnoseslistbypage.f")
	public Map<String, Object> findPlanRelaStandardDiagnosesListByPage(int limit,int start, String query, String constructPlanId,String executionId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtils.isBlank(executionId)){
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			ConstructPlanBpmObject constructPlanBpmObject = o_constructPlanBpmBO.findBpmObjectByExecutionId(executionId,"item");
			String diagnosesIds = constructPlanBpmObject.getForeachExecutionId();
			String[] diagnosesIdArray = diagnosesIds.split(",");
			for(String diagnosesId : diagnosesIdArray){
				    Map<String, Object> diagnosesMap = null;
				    Diagnoses diagnoses = o_planRelaStandardDiagnosesBO.findDiagnosesById(diagnosesId);
				    diagnosesMap = new HashMap<String, Object>();
					diagnosesMap.put("id",diagnoses.getId());
					diagnosesMap.put("planStandardId",diagnoses.getConstructPlanRelaStandard().getId());
					diagnosesMap.put("diagnosis",diagnoses.getDiagnosis());
					diagnosesMap.put("proof",diagnoses.getProof());
					diagnosesMap.put("description",diagnoses.getDescription());
					diagnosesMap.put("controldesc",diagnoses.getControldesc());
					//通过标准Id 找到对应关系 
					ProcessRelaStandard processRelaStandard = o_constructPlanRelaStandardBO.getProcessRelaStandard(diagnoses.getConstructPlanRelaStandard().getStandard().getId());
	//				constructPlanRelaStandardMap.put("orgName", StringUtils.join(orgNameSet, ","));
					if (null != processRelaStandard) {// 显示末级流程名称
						diagnosesMap.put("processName",processRelaStandard.getProcess().getName());
						diagnosesMap.put("text", processRelaStandard.getProcess().getName());
						diagnosesMap.put("processId",processRelaStandard.getProcess().getId());// 末级流程Id
					}
					Set<StandardRelaOrg> orgList = diagnoses.getConstructPlanRelaStandard().getStandard().getStandardRelaOrg();
					for(Iterator<StandardRelaOrg> it = orgList.iterator();it.hasNext();){
						StandardRelaOrg org = (StandardRelaOrg) it.next();
						if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
							diagnosesMap.put("standardRelaOrg",org.getOrg().getOrgname());
						}
					}
					//根据计划标准id找到对应的责任人
					ConstructPlanRelaStandardEmp constructPlanRelaStandardEmp = o_constructPlanRelaStandardBO.getPlanStandardEmp(diagnoses.getConstructPlanRelaStandard().getId());
					if(constructPlanRelaStandardEmp != null){
						diagnosesMap.put("constructPlanRelaOrgEmpId", constructPlanRelaStandardEmp.getId());
						diagnosesMap.put("constructPlanEmp", constructPlanRelaStandardEmp.getConstructPlanRelaOrg().getId());
					}
					diagnosesMap.put("standardName",diagnoses.getConstructPlanRelaStandard().getStandard().getParent().getName());
					diagnosesMap.put("controlRequirement",diagnoses.getConstructPlanRelaStandard().getStandard().getName());
					if(StringUtils.isNotBlank(diagnoses.getConstructPlanRelaStandard().getStandard().getControlPoint())){
						diagnosesMap.put("controlPoint",o_dictEntryDAO.get(diagnoses.getConstructPlanRelaStandard().getStandard().getControlPoint()).getName());
					}
					datas.add(diagnosesMap);
				}
				map.put("datas", datas);
				map.put("totalCount", diagnosesIdArray.length);
		}
		return map;
	}
	/**
	 * <pre>
	 * 根据风险id获取控制id
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointEditID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/finddefectidbyconstructplanid.f")
	public Map<String, Object> findDefectIdbyConstructId(String constructPlanId,String executionId) {
		Map<String, Object> processRiskMap = o_planRelaStandardDiagnosesBO.findDefectIdbyExecutionId(executionId);
		return processRiskMap;

	}
	/**
	 * <pre>
	 * 根据风险id获取控制id
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointEditID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findDefectRelaDiagnosesIdbyConstructId.f")
	public Map<String, Object> findDefectRelaDiagnosesIdbyConstructId(String constructPlanId,String executionId) {
		Map<String, Object> processRiskMap = o_planRelaStandardDiagnosesBO.findDefectIdbyExecutionId(executionId);
		return processRiskMap;
		
	}
	/**
	 *  
	 * 加载缺陷列表
	 *  
	 * 
	 * @author 宋佳
	 * @param ProcessPointEditID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/loaddiagnosesdefectformdata.f")
	public Map<String, Object> loadDiagnosesDefectFormData(String defectId) {
		Map<String, Object> processRiskMap = o_planRelaStandardDiagnosesBO
				.loadDiagnosesDefectFormData(defectId);
		return processRiskMap;
		
	}
	/**
	 * <pre>
	 * 保存缺陷反馈意见
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointForm
	 * @param parentId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/savediagnosesdefect.f")
	public Map<String, Object> saveDiagnosesDefect(GroupForm groupForm) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_planRelaStandardDiagnosesBO.saveDiagnosesDefect(groupForm);
		result.put("success", true);
		return result;
	}
	/**
	 *  
	 * 加载缺陷列表
	 *  
	 * 
	 * @author 宋佳
	 * @param ProcessPointEditID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/loaddefectclearupformdata.f")
	public Map<String, Object> loadDefectClearUpFormData(String defectId) {
		Map<String, Object> processRiskMap = o_planRelaStandardDiagnosesBO
				.loadDefectClearUpFormData(defectId);
		return processRiskMap;
		
	}
	/**
	 * <pre>
	 * 保存缺陷反馈意见
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param ProcessPointForm
	 * @param parentId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/savedefectclearup.f")
	public Map<String, Object> saveDefectClearUp(DefectClearUpGroupForm groupForm) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_planRelaStandardDiagnosesBO.saveDefectClearUp(groupForm);
		result.put("success", true);
		return result;
	}

}

