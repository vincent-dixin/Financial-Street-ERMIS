package com.fhd.icm.business.bpm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessPlanRelaProcessBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.dao.defect.DefectDAO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.web.controller.bpm.AssessPlanBpmObject;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 内控评价计划工作流BO.
 * @author 吴德福
 * @version
 * @since Ver 1.1
 * @Date 2013-2-25 下午9:28:25
 * @see
 */
@Service
public class AssessPlanBpmBO {

	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private JBPMOperate o_jbpmOperate;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	@Autowired
	private DefectDAO o_defectDAO;
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	
	/**
	 * 根据评价计划id查询评价计划制定审批人.
	 * @param roleKye :配置文件中角色的属性名称
	 * @return String
	 */
	public String findAssessPlanEmpIdByRole(String roleKey){
		String empId = "";
		//配置文件读取文件路径
		String roleName = ResourceBundle.getBundle("application").getString(roleKey);
		List<SysEmployee> employeeList = o_sysRoleBO.getEmpByCorpAndRole(roleName);
		if(null != employeeList && employeeList.size()>0){
			SysEmployee sysEmployee = employeeList.get(0);
			if(null != sysEmployee){
				empId = sysEmployee.getId();
			}
		}
		return empId;
	}
	/**
	 * 根据流程实例ID获得类型为AssessPlanBpmObject的流程变量
	 * @author 吴德福
	 * @param executionId 流程实例ID
	 * @return AssessPlanBpmObject
	 * @since  fhd　Ver 1.1
	*/
	public AssessPlanBpmObject findBpmObjectByExecutionId(String executionId){
		return (AssessPlanBpmObject) o_jbpmOperate.getVariable1(executionId, "item");
	}
	/**
	 * 第1步工作流提交--评价计划制定.
	 * @param executionId
	 * @param businessId
	 */
	@Transactional
	public void mergeAssePlanStatusBpmOne(String executionId, String businessId){
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanById(businessId);
		if(null != assessPlan){
			String empId = this.findAssessPlanEmpIdByRole("ICDepartmentMinister");
			
			if (!"".equals(empId)) {
				if(StringUtils.isBlank(executionId)){
					//保存评价计划状态--已提交，处理中
					assessPlan.setDealStatus(Contents.DEAL_STATUS_HANDLING);
					assessPlan.setStatus(Contents.STATUS_SUBMITTED);
					assessPlan.setActualStartDate(new Date());
					o_assessPlanBO.mergeAssessPlan(assessPlan);
					
					//菜单触发提交开启工作流
					Map<String, Object> variables = new HashMap<String, Object>();
					String entityType = "icmAssessPlan";
					variables.put("entityType", entityType);
					variables.put("id", businessId);
					variables.put("name",assessPlan.getName());
					variables.put("bpmOneEmpId", UserContext.getUser().getEmpid());
					executionId = o_jbpmBO.startProcessInstance(entityType,variables);
					
					//工作流提交
					Map<String, Object> variablesBpmTwo = new HashMap<String, Object>();
					variablesBpmTwo.put("bpmTwoEmpId", empId);
					o_jbpmBO.doProcessInstance(executionId, variablesBpmTwo);
				}else{
					//工作流提交
					Map<String, Object> variablesBpmTwo = new HashMap<String, Object>();
					variablesBpmTwo.put("bpmTwoEmpId", empId);
					o_jbpmBO.doProcessInstance(executionId, variablesBpmTwo);
				}
			}
		}
	}
	/**
	 * 第2步工作流提交--评价计划制定审批.
	 * @param executionId
	 * @param businessId
	 * @param isPass
	 * @param examineApproveIdea
	 */
	@Transactional
	public void mergeAssePlanStatusBpmTwo(String executionId, String businessId, String isPass, String examineApproveIdea){
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanById(businessId);
		if(null != assessPlan){
			
			if ("no".equals(isPass)) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("path", isPass);
				variables.put("examineApproveIdea", examineApproveIdea);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}else{
				String empId = "";
				List<AssessPlanRelaOrgEmp> list=o_assessPlanBO.findAssessPlanRelaOrgEmpBySome(businessId, true, false, Contents.EMP_RESPONSIBILITY);
				if(null != list && list.size()>0){
					AssessPlanRelaOrgEmp assessPlanRelaOrgEmp = list.get(0);
					if(null != assessPlanRelaOrgEmp){
						SysEmployee emp = assessPlanRelaOrgEmp.getEmp();
						if(null != emp){
							empId = emp.getId();
						}
					}
				}
				if (!"".equals(empId)) {
					//工作流提交
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("bpmThreeEmpId", empId);
					variables.put("path", isPass);
					variables.put("examineApproveIdea", examineApproveIdea);
					o_jbpmBO.doProcessInstance(executionId, variables);
				}
			}
		}
	}
	/**
	 * 评价计划执行并更新评价日期与评价状态为已评价
	 * @param executionId
	 * @param businessId
	 */
	@Transactional
	public void mergeAssessPlanRelaProcessAssessDate(String executionId, String businessId){
		AssessPlanBpmObject assessPlanBpmObject = this.findBpmObjectByExecutionId(executionId);
		String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
			AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
			if(null != assessPlanRelaProcess){
				assessPlanRelaProcess.setExecuteStatus(Contents.DEAL_STATUS_EVALUATION);
				assessPlanRelaProcess.setAssessDate(new Date());
				o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcess(assessPlanRelaProcess);
				
				Map<String, Object> variables = new HashMap<String, Object>();
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
		}
	}
	/**
	 * 评价计划复核并更新复核日期与评价状态为已复核
	 * @param executionId
	 * @param businessId
	 * @param isPass
	 * @param examineApproveIdea
	 */
	@Transactional
	public void mergeAssessPlanRelaProcessReviewDate(String executionId, String businessId, String isPass, String examineApproveIdea){
		AssessPlanBpmObject assessPlanBpmObject = this.findBpmObjectByExecutionId(executionId);
		String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
			AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
			if(null != assessPlanRelaProcess){
				assessPlanRelaProcess.setExecuteStatus(Contents.DEAL_STATUS_REVIEW);
				assessPlanRelaProcess.setReviewDate(new Date());
				o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcess(assessPlanRelaProcess);
				
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("path", isPass);
				variables.put("examineApproveIdea", examineApproveIdea);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
		}
	}
	/**
	 * 最后1步工作流提交--缺陷确认.
	 * @param executionId
	 * @param businessId
	 * @param isPass
	 * @param examineApproveIdea
	 */
	@Transactional
	public void mergeAssessPlanStatusBpmLast(String executionId, String businessId, String isPass, String examineApproveIdea){
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(businessId);
		if(null != assessPlan){
			
			//工作流根据isPass自动判断流程指向，当为yes时自动跳到end结束.当为no时返回到上一结点：缺陷修改
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("path", isPass);
			variables.put("examineApproveIdea", examineApproveIdea);
			o_jbpmBO.doProcessInstance(executionId, variables);
			
			if("yes".equals(isPass)){
				assessPlan.setActualEndDate(new Date());
				if(null != assessPlan.getPlanEndDate() && assessPlan.getPlanEndDate().before(assessPlan.getActualEndDate())){
					//保存评价计划状态--逾期
					assessPlan.setDealStatus(Contents.DEAL_STATUS_AFTER_DEADLINE);
				}else{
					//保存评价计划状态--已完成
					assessPlan.setDealStatus(Contents.DEAL_STATUS_FINISHED);
				}
				o_assessPlanBO.mergeAssessPlan(assessPlan);
				
				//更新评价计划下所有的状态为：未开始，已提交
				List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListByAssessPlanId(businessId, "");
				for (AssessRelaDefect assessRelaDefect : assessRelaDefectList) {
					Defect defect = assessRelaDefect.getDefect();
					if(null != defect){
						defect.setStatus(Contents.STATUS_SUBMITTED);
						defect.setDealStatus(Contents.DEAL_STATUS_NOTSTART);
						o_defectDAO.merge(defect);
					}
				}
			}
		}
	}
}
