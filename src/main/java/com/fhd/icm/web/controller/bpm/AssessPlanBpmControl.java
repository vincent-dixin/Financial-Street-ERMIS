package com.fhd.icm.web.controller.bpm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.bpm.AssessPlanBpmBO;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;

/**
 * 内控评价计划工作流control.
 * @author 吴德福
 * @since 2013-3-1
 */
@Controller
public class AssessPlanBpmControl {

	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private AssessPlanBpmBO o_assessPlanBpmBO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;

	/**
	 * 内控评价计划制定.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/assess/assessPlanDraft.f")
	public void assessPlanDraft(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			o_assessPlanBpmBO.mergeAssePlanStatusBpmOne(executionId, businessId);
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 内控评价计划审批.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param isPass 审批:通过--‘yes’,不通过--‘no’
	 * @param examineApproveIdea 审批意见
	 * @param response
	 */
	@RequestMapping("/icm/assess/assessPlanApproval.f")
	public void assessPlanApproval(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			o_assessPlanBpmBO.mergeAssePlanStatusBpmTwo(executionId, businessId, isPass, examineApproveIdea);
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 内控评价计划任务分配.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/assess/assessPlanAllocation.f")
	public void assessPlanAllocationAllocation(String executionId,String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String empId = o_assessPlanBpmBO.findAssessPlanEmpIdByRole("ICDepartmentMinister");
			if (!"".equals(empId)) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("bpmFourEmpId", empId);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 内控评价计划任务分配审批.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param isPass 审批:通过--‘yes’,不通过--‘no’
	 * @param examineApproveIdea 审批意见
	 * @param response
	 */
	@RequestMapping("/icm/assess/assessPlanAllocationApproval.f")
	public void assessPlanAllocationAllocationApproval(String executionId,String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String empId="";
			if ("no".equals(isPass)) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("path", isPass);
				variables.put("examineApproveIdea", examineApproveIdea);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}else{
				empId=o_assessPlanBpmBO.findAssessPlanEmpIdByRole("ICDepartmentStaff");
				if (!"".equals(empId)) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("bpmFiveEmpId", empId);
					variables.put("path", isPass);
					variables.put("examineApproveIdea", examineApproveIdea);
					o_jbpmBO.doProcessInstance(executionId, variables);
				}
			}
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 内控评价计划任务发布.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 */
	@RequestMapping("/icm/assess/assessPlanPublish.f")
	public void assessPlanPublish(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			List<AssessPlanBpmObject> list = o_assessPlanBO.findAssessPlanRelaProcessParameterForBpmTask(businessId);
			if(null != list && list.size()>0){
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("items", list);
				variables.put("joinCount", list.size());
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 
	 * 内控计划执行
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping("/icm/assess/assessPlanExecute.f")
	public void assessPlanExecute(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();

			o_assessPlanBpmBO.mergeAssessPlanRelaProcessAssessDate(executionId, businessId);
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * <pre>
	 * 内控计划执行审核
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param executionId 流程ID
	 * @param businessId 业务ID
	 * @param isPass
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/assess/assessPlanExecuteApproval.f")
	public void assessPlanExecuteApproval(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			o_assessPlanBpmBO.mergeAssessPlanRelaProcessReviewDate(executionId, businessId, isPass, examineApproveIdea);
				
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
	
	/**
	 * <pre>
	 * 缺陷汇总
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param executionId 流程ID
	 * @param businessId 业务ID
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/assess/assessDefectSummary.f")
	public void assessDefectSummary(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			//配置文件读取文件路径
			String roleName = ResourceBundle.getBundle("application").getString("ICByTestingDepartmentMinister");
			
			List<String> empIdList = this.findEmpIdListByBusinessId(businessId,roleName);
			if(null != empIdList && empIdList.size()>0){
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("empIdLists", empIdList);
				variables.put("joinCount", empIdList.size());
				o_jbpmBO.doProcessInstance(executionId, variables);
				out.write("true");
			}else{
				out.write("缺陷的责任部门下的角色'"+roleName+"'不存在人员!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
	
	/**
	 * <pre>
	 * 缺陷反馈
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param executionId 流程ID
	 * @param businessId 业务ID
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/assess/assessDefectFeedback.f")
	public void assessDefectFeedback(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			Map<String, Object> variables = new HashMap<String, Object>();
			o_jbpmBO.doProcessInstance(executionId, variables);
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
	
	/**
	 * 查询评价计划下的所有缺陷责任部门下对应的角色名称的下属人员list.
	 * @param businessId
	 * @param roleName
	 * @return List<String>
	 */
	public List<String> findEmpIdListByBusinessId(String businessId, String roleName){
		List<String> empIdList = null;
		
		//查找当前计划的所有缺陷的整改责任部门对应的被测试部门部长
		Set<String> orgIdSet = new HashSet<String>();
		List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListByAssessPlanId(businessId, "");
		if(null != assessRelaDefectList && assessRelaDefectList.size()>0){
			for (AssessRelaDefect assessRelaDefect : assessRelaDefectList) {
				Defect defect = assessRelaDefect.getDefect();
				if(null != defect){
					Set<DefectRelaOrg> defectRelaOrgs = defect.getDefectRelaOrg();
					for (DefectRelaOrg defectRelaOrg : defectRelaOrgs) {
						if(null != defectRelaOrg.getOrg()){
							if(!orgIdSet.contains(defectRelaOrg.getOrg().getId())){
								orgIdSet.add(defectRelaOrg.getOrg().getId());
							}
						}
					}
				}
			}
		}
		
		List<Object[]> objectList = o_employeeBO.findEmployeeListByOrgIdsAndRoleName(orgIdSet, roleName);
		if(null != objectList && objectList.size()>0){
			empIdList = new ArrayList<String>();
			for (Object[] objects : objectList) {
				if(!empIdList.contains(objects[0])){
					if(null != objects[0]){
						empIdList.add(String.valueOf(objects[0]));
					}
				}
			}
		}
		return empIdList;
	}
	
	/**
	 * <pre>
	 * 缺陷修改
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param executionId 流程ID
	 * @param businessId 业务ID
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/assess/assessDefectRevise.f")
	public void assessDefectRevise(String executionId, String businessId, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			String empId = o_assessPlanBpmBO.findAssessPlanEmpIdByRole("ICDepartmentMinister");
			if (StringUtils.isNotBlank(empId)) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("bpmElevenEmpId", empId);
				//工作流计划流程进度时使用joinCount参数
				variables.put("joinCount", 0);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
	
	/**
	 * <pre>
	 * 缺陷确认
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/assess/assessDefectRecognition.f")
	public void assessDefectRecognition(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			o_assessPlanBpmBO.mergeAssessPlanStatusBpmLast(executionId, businessId, isPass, examineApproveIdea);
			
			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}
}