package com.fhd.icm.web.controller.assess;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessPlanRelaProcessBO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-9		上午11:14:40
 *
 * @see 	 
 */
@Controller
public class AssessPlanRelaProcessControl {
	
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	
	@Autowired
	private AssessPlanBO o_assessPlanBO;

	/**
	 * <pre>
	 * 批量保存流程的参与评价的人：
	 * 按部门批量保存参与评价的人的信息，当batchType为org时，例如：[{orgId:'23311',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * handlerId和reviewerId分别为assessPlanRelaOrgEmp的ID
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param jsonString 保存人员信息的字符串，例如：[{assessPlanRelaProcessId:'11111',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/mergerAssessPlanProcessRelaOrgEmpBatch.f")
	public Boolean mergerAssessPlanProcessRelaOrgEmpBatch(String assessPlanId,String jsonString) {
		o_assessPlanRelaProcessBO.mergerAssessPlanProcessRelaOrgEmpBatch(assessPlanId, jsonString);
		return true;
	}
	/**
	 * <pre>
	 * 批量保存参与评价的人：
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param jsonString 保存人员信息的字符串，例如：[{assessPlanRelaProcessId:'11111',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessPlanProcessRelaOrgEmpBatch.f")
	public Boolean mergeAssessPlanProcessRelaOrgEmpBatch(String assessPlanId, String jsonString) {
		o_assessPlanRelaProcessBO.mergeAssessPlanProcessRelaOrgEmpBatch(assessPlanId, jsonString);
		return true;
	}
	
	/**
	 * 
	 * <pre>
	 * 批量保存内控评价计划流程范围
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanJson :grid中修改过的数据集合
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessPlanRelaProcessBatch.f")
	public Map<String, Object> mergeAssessPlanRelaProcessBatch(String assessPlanId, String modifiedRecord) {
		Map<String, Object> assessPlanMap = new HashMap<String, Object>();
		o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcessBatch(assessPlanId,modifiedRecord);
		assessPlanMap.put("success", true);
		return assessPlanMap;
	}
	

	/**
	 * <pre>
	 * 批量设置评价涉及的的流程是否进行穿行测试及穿几回。
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanProcessIds 评价计划关联流程的ID串，多个以“,”隔开，例如：dfaafsadfa,fadfaferf
	 * @param isPracticeTest 是否穿行测试，例如：true
	 * @param practiceNum 穿几回，例如：2
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/assess/assessPlan/mergeAssessPlanRelaProcessForPracticeTestBatch.f")
	public Map<String, Object> mergeAssessPlanRelaProcessForPracticeTestBatch(String assessPlanProcessIds, Boolean isPracticeTest,Integer practiceNum) {
		Map<String, Object> assessPlanMap = new HashMap<String, Object>();
		o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcessForPracticeTestBatch(assessPlanProcessIds, isPracticeTest, practiceNum);
		assessPlanMap.put("success", true);
		return assessPlanMap;
	}
	

	/**
	 * <pre>
	 * 批量设置抽样测试
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanProcessIds 评价计划关联流程的ID串，多个以“,”隔开，例如：dfaafsadfa,fadfaferf
	 * @param isSampleTest 是否抽样测试，例如：true
	 * @param jsonString 抽取比率 抽样比率：[{frequencyCode: 'ic_control_frequency_0',rate:0.30},{frequencyCode:'ic_control_frequency_1',rate:0.20},{frequencyCode:'ic_control_frequency_week',rate:0.25},{...} ]
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/assess/assessPlan/mergeAssessPlanRelaProcessForSampleTestBatch.f")
	public Map<String, Object> mergeAssessPlanRelaProcessForSampleTestBatch(String assessPlanProcessIds, Boolean isSampleTest, String jsonString) {
		Map<String, Object> assessPlanMap = new HashMap<String, Object>();
		o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcessForSampleTestBatch(assessPlanProcessIds, isSampleTest, jsonString);
		assessPlanMap.put("success", true);
		return assessPlanMap;
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
	@RequestMapping("/icm/assess/saveAssessPlanRelaProcesses.f")
	public Map<String, Object> saveAssessPlanRelaProcesses(String processIds, String assessPlanId, String assessPlanDepartIds, String selectType) {
		Map<String, Object> assessPlanMap = new HashMap<String, Object>();
		o_assessPlanRelaProcessBO.saveAssessPlanRelaProcess(processIds,assessPlanId,assessPlanDepartIds,selectType);
		assessPlanMap.put("success", true);
		return assessPlanMap;
	}
	
	/**
	 * 内控评价流程范围列表,删除功能.
	 * 1.自评--删除计划流程表、计划部门人员表
	 * 2.他评--删除计划流程表
	 * @author 刘中帅
	 * @modify 吴德福 2013-4-23
	 * @throws IOException
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/removeAssessPlanRelaProcesses.f")
	public void removeAssessPlanRelaProcesses(String assessPlanRelaProcessIds, String assessPlanId, String processIds, HttpServletResponse response) throws IOException {
		PrintWriter out = null;
		boolean flag = false;
		try {
			out = response.getWriter();
			
			if(StringUtils.isNotBlank(assessPlanRelaProcessIds)){
				//评价方式是自评，删除对应的计划部门人员表
				AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
				if(null != assessPlan.getType() && Contents.ASSESS_MEASURE_ETYPE_SELF.equals(assessPlan.getType().getId())){
					String[] processIdArray=processIds.split(",");
					if(processIdArray.length>0){
						//删除评价计划与部门、人员的关联
						o_assessPlanBO.removeAssessPlanRelaOrgEmpByProcessIds(assessPlanId, processIdArray);
					}
				}
				
				List<String> idList = new ArrayList<String>();
				String[] idArray = assessPlanRelaProcessIds.split(",");
				for (String id : idArray) {
					idList.add(id);
				}
				o_assessPlanRelaProcessBO.removeAssessPlanRelaProcessesByIdList(idList);
			}
			flag = true;
			out.print(flag);
		} catch(Exception e){
			e.printStackTrace();
			out.print(flag);
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * <pre>
	 * 查询评价计划关联的流程范围
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param query 查询条件
	 * @param assessPlanId 评价计划ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessPlanRelaProcessListByPage.f")
	public Map<String, Object> findAssessPlanRelaProcessListByPage(int limit,int start, String query, String assessPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByPage(page, query,assessPlanId);
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
		if (null != assessPlanRelaProcessList
				&& assessPlanRelaProcessList.size() > 0) {
			Map<String, Object> row = null;
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
				row = new HashMap<String, Object>();
				row.put("id",assessPlanRelaProcess.getId());
				row.put("dbid",assessPlanRelaProcess.getId());
				Set<ProcessRelaOrg> processRelaOrgSet = assessPlanRelaProcess.getProcess().getProcessRelaOrg();
				Set<String> orgNameSet = new HashSet<String>();
				for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
					if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
						orgNameSet.add(processRelaOrg.getOrg().getOrgname());
					}
				}
				row.put("orgName", StringUtils.join(orgNameSet, ","));
				if (null != assessPlanRelaProcess.getProcess()) {// 显示末级流程名称
					row.put("processName",assessPlanRelaProcess.getProcess().getName());
					row.put("text", assessPlanRelaProcess.getProcess().getName());
					row.put("processId",assessPlanRelaProcess.getProcess().getId());// 末级流程Id
					if (null != assessPlanRelaProcess.getProcess().getParent()) {// 显示二级流程分类名称
						row.put("parentProcessName",assessPlanRelaProcess.getProcess().getParent().getName());
						if (null != assessPlanRelaProcess.getProcess().getParent().getParent()) {// 显示一级流程分类名称
							row.put("firstProcessName",assessPlanRelaProcess.getProcess().getParent().getParent().getName());
						}
					}
				}
				row.put("isPracticeTest",assessPlanRelaProcess.getIsPracticeTest());
				row.put("isPracticeTestShow",assessPlanRelaProcess.getIsPracticeTest());
				row.put("practiceNum",assessPlanRelaProcess.getPracticeNum());
				row.put("isSampleTest",assessPlanRelaProcess.getIsSampleTest());
				row.put("isSampleTestShow",assessPlanRelaProcess.getIsSampleTest());
				row.put("coverageRate",assessPlanRelaProcess.getCoverageRate());
				
				Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmps = assessPlanRelaProcess.getAssessPlanProcessRelaOrgEmp();
				for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmps) {
					if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType())){
						row.put("executeEmpName", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
					}else if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType())){
						row.put("reviewerEmpName", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
					}
				}
				if(null != assessPlanRelaProcess.getAssessDate()){
					row.put("assessDate", DateUtils.formatDate(assessPlanRelaProcess.getAssessDate(), "yyyy-MM-dd"));
				}
				if(null != assessPlanRelaProcess.getReviewDate()){
					row.put("reviewDate", DateUtils.formatDate(assessPlanRelaProcess.getReviewDate(), "yyyy-MM-dd"));
				}
				if(StringUtils.isNotBlank(assessPlanRelaProcess.getDesc())){
					row.put("resultAnalysis", assessPlanRelaProcess.getDesc());
				}else{
					row.put("resultAnalysis", "");
				}
				
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		} else {
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	/**
	 * <pre>
	 * 查询计划对应的部门列表
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param limit 每夜显示条数
	 * @param start 起始条数
	 * @param query 查询条件
	 * @param assessPlanID 流程ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("icm/assess/findOrgListByPage.f")
	public Map<String, Object> findOrgListByPage(int limit, int start,String query, String assessPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByPage(page, query, assessPlanId);
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
		if (null != assessPlanRelaProcessList&& assessPlanRelaProcessList.size() > 0) {
			Map<String, Object> assessPlanRelaProcessMap = null;
			Set<SysOrganization> sysOrganizationSet = new HashSet<SysOrganization>();
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
				Set<ProcessRelaOrg> processRelaOrgSet = assessPlanRelaProcess.getProcess().getProcessRelaOrg();
				for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
					if (null != processRelaOrg.getOrg()) {
						sysOrganizationSet.add(processRelaOrg.getOrg());
					}
				}
			}
			for (SysOrganization org : sysOrganizationSet) {
				assessPlanRelaProcessMap = new HashMap<String, Object>();
				assessPlanRelaProcessMap.put("orgId", org.getId());
				assessPlanRelaProcessMap.put("orgName", org.getOrgname());
				datas.add(assessPlanRelaProcessMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		} else {
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	/**
	 * <pre>
	 * 查询计划对应的流程列表
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param limit 每夜显示条数
	 * @param start 起始条数
	 * @param query 查询条件
	 * @param assessPlanID流程ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("icm/assess/findProcessListByPage.f")
	public Map<String, Object> findAssessPlanRelaPointListbyPage(int limit,int start, String query, String assessPlanID) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByPage(page, query,assessPlanID);
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
		if (null != assessPlanRelaProcessList&& assessPlanRelaProcessList.size() > 0) {
			Map<String, Object> assessPlanRelaProcessMap = null;
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
				assessPlanRelaProcessMap = new HashMap<String, Object>();
				assessPlanRelaProcessMap.put("processId", assessPlanRelaProcess.getProcess().getId());
				assessPlanRelaProcessMap.put("processName",assessPlanRelaProcess.getProcess().getName());
				datas.add(assessPlanRelaProcessMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		} else {
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}

	
	/**
	 * <pre>
	 * 分页查询AssessPlanRelaProcess
	 * 计划创建完成之后，分配任务时，查询计划相关的流程信息
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param limit 每页显示数目
	 * @param start 起始数目
	 * @param query 查询条件
	 * @param assessPlanID 计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessPlanRelaProcessListByPageForAllocation.f")
	public Map<String,Object> findAssessPlanRelaProcessListByPageForAllocation(int limit, int start, String query,String assessPlanId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanById(assessPlanId);
		if(null != assessPlan){
			Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
			if(null != assessPlanRelaProcesses && assessPlanRelaProcesses.size()>0){
				Map<String, Object> row = null;
				for(AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcesses){
					row = new HashMap<String, Object>();
					row.put("assessPlanRelaProcessId", assessPlanRelaProcess.getId());
					
					if (null != assessPlanRelaProcess.getProcess()) {
						Process process = assessPlanRelaProcess.getProcess();
						if(null != process){
							row.put("processId",process.getId());// 末级流程Id
							row.put("processName",process.getName());// 显示末级流程名称
							if (null != process.getParent()) {
								row.put("parentProcessName",process.getParent().getName());// 显示二级流程分类名称
								if (null != process.getParent().getParent()) {
									row.put("firstProcessName",process.getParent().getParent().getName());// 显示一级流程分类名称
								}
							}
							
							Set<ProcessRelaOrg> processRelaOrgSet = process.getProcessRelaOrg();
							Set<String> orgNameSet = new HashSet<String>();
							for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
								if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
									orgNameSet.add(processRelaOrg.getOrg().getOrgname());
								}
							}
							row.put("orgName", StringUtils.join(orgNameSet, ","));
						}
					}
				
					Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmpSet = assessPlanRelaProcess.getAssessPlanProcessRelaOrgEmp();
					if(null != assessPlanProcessRelaOrgEmpSet && assessPlanProcessRelaOrgEmpSet.size()>0){
						//已保存过计划流程部门人员表
						for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmpSet) {
							if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
								row.put("handlerId", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getId());
							}
							if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
								row.put("reviewerId", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getId());
							}
						}
					}else{
						//未保存过计划流程部门人员表,且评价计划是自评，那么评价人默认为流程的责任人
						if (null != assessPlanRelaProcess.getProcess()) {
							Process process = assessPlanRelaProcess.getProcess();
							if(null != process){
								if(null != assessPlan.getType() && Contents.ASSESS_MEASURE_ETYPE_SELF.equals(assessPlan.getType().getId())){
									Set<ProcessRelaOrg> processRelaOrgs = process.getProcessRelaOrg();
									for (ProcessRelaOrg processRelaOrg : processRelaOrgs) { 
										if(Contents.EMP_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getEmp()){
											row.put("handlerId", processRelaOrg.getEmp().getId());
										}
									}
								}
							}
						}
					}
				
					row.put("planStartDate", assessPlanRelaProcess.getAssessPlan().getPlanStartDate());
					row.put("planEndDate", assessPlanRelaProcess.getAssessPlan().getPlanEndDate());
					datas.add(row);
				}
				map.put("datas", datas);
				map.put("totalCount", assessPlanRelaProcesses.size());
			}else{
				map.put("datas", new Object[0]);
				map.put("totalCount", "0");
			}
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	} 
	
}

