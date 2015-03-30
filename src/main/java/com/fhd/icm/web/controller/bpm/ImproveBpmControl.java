package com.fhd.icm.web.controller.bpm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.ImproveBpmBO;
import com.fhd.icm.business.defect.DefectBO;
import com.fhd.icm.business.defect.DefectRelaImproveBO;
import com.fhd.icm.business.rectify.ImproveBO;
import com.fhd.icm.business.rectify.ImprovePlanBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;
import com.fhd.icm.entity.rectify.ImproveRelaPlan;

/**
 * 内控整改计划工作流control.
 * @author 吴德福
 */
@Controller
public class ImproveBpmControl {

	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private ImproveBpmBO o_improveBpmBO;
	@Autowired
	private ImproveBO o_improveBO;
	@Autowired
	private ImprovePlanBO o_improvePlanBO;
	@Autowired
	private DefectRelaImproveBO o_defectRelaImproveBO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	@Autowired
	private DefectBO o_defectBO;
	
	
	/**
	 * <pre>
	 * 工作流调用
	 * 整改计划改为已完成状态
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveId
	 * @since  fhd　Ver 1.1
	*/
	public void mergeImprovePlanStatus(String improveId) {
		//将整改计划的处理状态置位已完成
		ImproveBO improveBO = ContextLoader.getCurrentWebApplicationContext().getBean(ImproveBO.class);
		DefectRelaImproveBO defectRelaImproveBO = ContextLoader.getCurrentWebApplicationContext().getBean(DefectRelaImproveBO.class);
		DefectBO defectBO = ContextLoader.getCurrentWebApplicationContext().getBean(DefectBO.class);
		Improve improve = improveBO.findImproveById(improveId);
		improve.setDealStatus(Contents.DEAL_STATUS_FINISHED);
		improveBO.mergeImprove(improve);
		List<DefectRelaImprove> defectRelaImproveList = defectRelaImproveBO.findDefectRelaImproveListBySome(null, improveId);
		for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
			Defect defect = defectRelaImprove.getDefect();
			defect.setDealStatus(Contents.DEAL_STATUS_FINISHED);
			defectBO.mergeDefect(defect);
		}
	}
	
	/**
	 * 整改计划制定，修改整改计划状态为提交状态
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "/icm/rectify/rectifyDraft.f")
	public void rectifyPlanDraft(String executionId, String businessId, HttpServletResponse response) throws IOException{
        PrintWriter out = null;
		try {
			out = response.getWriter();
			String empId = o_improveBpmBO.findRectifyEmpIdByRole("ICDepartmentMinister");
			if(!"".equals(empId)){
				Improve improve = o_improveBO.findImproveById(businessId);
				if(null != improve){
					improve.setStatus(Contents.STATUS_SUBMITTED);//保存为已提交状态
					improve.setDealStatus(Contents.DEAL_STATUS_HANDLING);
					o_improveBO.mergeImprove(improve);
					Map<String, Object> variables=new HashMap<String, Object>();
					String entityType = "icmRectifyPlan";
					variables.put("entityType",entityType);
					variables.put("ICDepartmentStaffEmpId",UserContext.getUser().getEmpid());//设置当前处理人
					variables.put("ICDepartmentMinisterEmpId", empId);//设置下一节点处理人
					variables.put("id", businessId);
					variables.put("name", improve.getName());
					if(StringUtils.isBlank(executionId)){
						executionId = o_jbpmBO.startProcessInstance(entityType, variables);
						o_jbpmBO.doProcessInstance(executionId, variables);
					}else{
						o_jbpmBO.doProcessInstance(executionId, variables);
					}
					out.write("true");
				}else{
					out.write("false");
				}
			}
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	
	/**
	 * 内控整改计划部门部长审批.
	 * @param executionId 流程id
	 * @param businessId 业务id
	 * @param isPass :审批，通过，或不通过 ：通过：‘yes’,不通过：‘no’
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/icm/rectify/rectifyRectifyApprove.f")
	public void rectifyApproval(String executionId, String businessId,String isPass, String examineApproveIdea, HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String empId = o_improveBpmBO.findRectifyEmpIdByRole("ICDepartmentLeader");
			Improve improve = o_improveBO.findImproveById(businessId);
			if(null != improve){
				improve.setDealStatus(Contents.DEAL_STATUS_HANDLING);
				o_improveBO.mergeImprove(improve);//处理状态保存为处理中
				if(!"".equals(empId)){
					Map<String, Object> variables=new HashMap<String, Object>();
					variables.put("ICDepartmentLeaderEmpId", empId);//设置内控部门分管领导为下一节点处理人
					variables.put("path", isPass);
					variables.put("examineApproveIdea", examineApproveIdea);
					o_jbpmBO.doProcessInstance(executionId, variables);
				}
				out.write("true");
			}else{
				out.write("false");
			}
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	/**
	 * <pre>
	 *内控部门领导审批
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param isPass
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/rectify/rectifyRectifyLeadApprove.f")
	public void rectifyLeadApproval(String executionId, String businessId,String isPass, String examineApproveIdea, HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String empId = o_improveBpmBO.findRectifyEmpIdByRole("ICDepartmentStaff");
			if(StringUtils.isNotBlank(empId)){
				Map<String, Object> variables=new HashMap<String, Object>();
				variables.put("ICDepartmentStaffEmpId1", empId);//内控部门管理员
				variables.put("path", isPass);
				variables.put("examineApproveIdea", examineApproveIdea);
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * <pre>
	 *内控整改计划发布
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/rectify/rectifyPublish.f")
	public void rectifyPublish(String executionId, String businessId,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Improve improve = o_improveBO.findImproveById(businessId);
			if(null != improve){
				List<String> empIdList = o_defectRelaImproveBO.findEmpIdListBySome(null, businessId);//查询本次计划相关的责任部门下的所有人员信息
				Set<String> empIdSetTemp = new HashSet<String>();
				Set<String> empIdSet = new HashSet<String>();
				for (String empId : empIdList) {
					empIdSetTemp.add(empId);
				}
				String[] empIds = o_improveBpmBO.findRectifyEmpIdsByRole("ICByTestingDepartmentMinister");//查询角色编号为ICByTestingDepartmentStaff的所有人员信息
				String ICDepartmentMinisterEmpId1 = o_improveBpmBO.findRectifyEmpIdByRole("ICDepartmentMinister");
				if(null != empIds &&empIds.length>0 && StringUtils.isNotBlank(ICDepartmentMinisterEmpId1)){
					for (int i = 0; i < empIds.length; i++) {
						for (String empId : empIdSetTemp) {
							if(empId.equals(empIds[i])){
								empIdSet.add(empId);
							}
						}
					}
					/*初始化需要上报的方案的数据*/
					List<DefectRelaImprove> defectRelaImproveList = o_defectRelaImproveBO.findDefectRelaImproveListBySome(null, businessId);
					Set<String> defectIdSet = new HashSet<String>();
					for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
						Defect defect = defectRelaImprove.getDefect();
						defectIdSet.add(defect.getId());
						defect.setDealStatus(Contents.DEAL_STATUS_HANDLING);
						o_defectBO.mergeDefect(defect);
					}
					List<ImprovePlanRelaDefect> improvePlanRelaDefectList = o_improvePlanRelaDefectBO.findImprovePlanRelaDefectListBySome(defectIdSet);
					List<ImproveRelaPlan> improveRelaPlanList = o_improvePlanBO.findImproveRelaPlanListBySome(businessId,null);
					Set<String> improvePlanIdSet = new HashSet<String>();
					for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectList) {
						for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
							if(improvePlanRelaDefect.getImprovePlan().getId().equals(improveRelaPlan.getImprovePlan().getId())){
								improvePlanIdSet.add(improvePlanRelaDefect.getImprovePlan().getId());
							}
						}
					}
					if(null == improvePlanIdSet || (null != improvePlanIdSet && improvePlanIdSet.size() == 0)){//如果该缺陷没有关联整改方案，则需要创建
						for (String defectId : defectIdSet) {
							o_improvePlanBO.saveImprovePlan(businessId, defectId, UserContext.getUser().getCompanyid());
						}
					}
					Map<String, Object> variables=new HashMap<String, Object>();
					variables.put("empIds", empIdSet);//上报方案的处理人
					variables.put("ICDepartmentMinisterEmpId1", ICDepartmentMinisterEmpId1);//制定任务节点（汇总并制定复核人）的处理人
					variables.put("joinCount", empIdSet.size());//上报方案的任务数
					o_jbpmBO.doProcessInstance(executionId, variables);
				}
			}
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	
	
	/**
	 * <pre>
	 *整改计划上报方案
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/rectify/rectifyDraftFile.f")
	public void rectifyPlanDraftfile(String executionId, String businessId, String modifiedRecord, HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			out = response.getWriter();			
			if(StringUtils.isNotBlank(modifiedRecord)){
				o_improvePlanBO.mergeImprovePlanBatch(businessId, modifiedRecord);
			}
			Map<String, Object> variables=new HashMap<String, Object>();
			o_jbpmBO.doProcessInstance(executionId, variables);
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	
	
	
	/**
	 * <pre>
	 *汇总并指定复核人
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping("/icm/rectify/rectifyFileCheck.f")
	public void rectifyFileCheck(String executionId, String businessId,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		try {
			out = response.getWriter();
			List<ImproveBpmObject> bpmObjectList = o_improveBpmBO.findDistributeParameterForBpmTask(businessId,"ICByTestingDepartmentMinister","ICDepartmentMinister");
			if(null != bpmObjectList && bpmObjectList.size()>0){
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("items", bpmObjectList);
				variables.put("joinCount", bpmObjectList.size());//填写进度及复核的任务数
				o_jbpmBO.doProcessInstance(executionId, variables);
			}
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	
	
	/**
	 * <pre>
	 *填写整改方案进度
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/icm/rectify/rectifyFollowListGrid.f")
	public void rectifyFollowListGrid(String executionId, String businessId, HttpServletResponse response) throws IOException{
        PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, Object> variables = new HashMap<String, Object>();
			o_jbpmBO.doProcessInstance(executionId, variables);
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	
	
	/**
	 * <pre>
	 *整改方案复核
	 * </pre>
	 * 
	 * @author 李克东
	 * @param executionId
	 * @param businessId
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/icm/rectify/rectifyPlanCheck.f")
	public void rectifyPlanCheck(String executionId, String businessId, HttpServletResponse response) throws IOException{
        PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, Object> variables=new HashMap<String, Object>();
			o_jbpmBO.doProcessInstance(executionId, variables);
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	@RequestMapping(value = "/icm/rectify/rectifyPlanCheckApprove.f")
	public void rectifyPlanCheckApprove(String executionId, String businessId, String isPass, String examineApproveIdea, HttpServletResponse response) throws IOException{
           PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, Object> variables=new HashMap<String, Object>();
			if(StringUtils.isNotBlank(isPass) && isPass.equals("yes")){
				isPass = "path1"; 
			}
			variables.put("path", isPass);
			variables.put("examineApproveIdea", examineApproveIdea);
			o_jbpmBO.doProcessInstance(executionId, variables);
			out.write("true");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
}
