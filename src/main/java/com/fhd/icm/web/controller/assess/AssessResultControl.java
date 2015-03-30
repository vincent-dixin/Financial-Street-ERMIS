/**
 * AssessResultControl.java
 * com.fhd.icm.web.controller.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-15 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.controller.assess;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessPlanRelaProcessBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.assess.AssessResultBO;
import com.fhd.icm.business.bpm.AssessPlanBpmBO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.assess.AssessResult;
import com.fhd.icm.entity.assess.AssessSample;
import com.fhd.icm.entity.assess.AssessSampleRelaFile;
import com.fhd.icm.entity.assess.Assessor;
import com.fhd.icm.web.controller.bpm.AssessPlanBpmObject;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 操作评价结果相关的方法。查询评价结果，执行评价等
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-15		下午1:48:38
 *
 * @see 	 
 */
@Controller
public class AssessResultControl {

	@Autowired
	private AssessResultBO o_assessResultBO;
	@Autowired
    private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessPlanBpmBO o_assessPlanBpmBO;
	@Autowired
	private FileUploadBO o_fileUploadBO;
	@Autowired
	private ProcessBO o_processBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	
	/**
	 * 穿行测试生成样本.
	 * @author 吴德福
	 * @param assessPlanId 评价计划id
	 * @param assessResultId 评价结果id
	 * @return Boolean
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/saveThroughSample.f")
	public Boolean saveThroughSample(String assessPlanId, String assessResultId){
		//首先判断当前流程是否进行过穿行测试，未测试则自动生成[未穿行的次数]个样本
		List<AssessSample> assessSampleList = o_assessResultBO.findAssessSampleListByAssessPlanIdAndAssessResultId(assessPlanId, assessResultId);
		if(null != assessSampleList && assessSampleList.size()>0){
			return true;
		}else{
			AssessResult assessResult = o_assessResultBO.findAssessResultById(assessResultId);
			if(null!=assessResult){
				Integer toExtractAmount = assessResult.getToExtractAmount();
				if(null != toExtractAmount){
			        List<Integer> list = new ArrayList<Integer>();
			        for (int i = 0; i < toExtractAmount; i++) {
			            list.add(i + 1);
			        }
			        int count = toExtractAmount;
			        int items[] = new int[toExtractAmount];
			        for (int i = 0; i < toExtractAmount; i++) {
			            int randomInt = new Random().nextInt(count);
			            items[i] = list.get(randomInt);
			            list.remove(randomInt);
			            count--;
			        }
					
			        Arrays.sort(items);
			        List<Integer> numList=new ArrayList<Integer>();
			        for (int i : items) {
			        	numList.add(i);
					}
					
					o_assessResultBO.saveAssessSampleBatch(assessPlanId, assessResultId, numList, "");
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 抽样测试生成样本.
	 * @author 黄晨曦
	 * @param assessPlanId 评价计划id
	 * @param assessResultId 评价结果id
	 * @param processId 流程id
	 * @param sampletext 样本编号前缀
	 * @param startNum 起始期间
	 * @param stopNum 结束期间
	 * @return Boolean
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/saveSample.f")
	public Boolean saveSample(String assessPlanId, String assessResultId, String processId, String sampletext,int startNum,int stopNum){
		//生成样本
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListBySome(assessPlanId, processId);
		if(null != assessPlanRelaProcessList && assessPlanRelaProcessList.size()>0){
			Double coverageRate = assessPlanRelaProcessList.get(0).getCoverageRate();
			if(null!=coverageRate){
				int outCount = (int) ((stopNum-startNum)*coverageRate);
				
				List<Integer> numList = this.findRandomSequence(startNum, stopNum, outCount);
				
				o_assessResultBO.saveAssessSampleBatch(assessPlanId, assessResultId, numList, sampletext);
			}
		}
		return true;
	}
	
	/**
	 * 随机生成1-total之间不重复的随机数.
	 * @param startNum
	 * @param stopNum 
	 * @param outCount
	 * @return List<Integer>
	 */
	public List<Integer> findRandomSequence(int startNum, int stopNum, int outCount) {
		List<Integer> list = new ArrayList<Integer>();
		
		int[] sequence = new int[stopNum];
		int[] output = new int[outCount];
		for (int i = startNum; i < stopNum; i++) {
			sequence[i] = i;
		}
		Random random = new Random();
		int end = stopNum - 1;
		for (int i = 0; i < outCount; i++) {
			int num = random.nextInt(end-startNum)+startNum;
			output[i] = sequence[num];
			sequence[num] = sequence[end];
			end--;
		}
		Arrays.sort(output);
		for (int i : output) {
			list.add(i);
		}
		return list;
	}
	
	/**
	 * 批量设置样本是否合格.
	 * @param jsonString
	 * @param isQualified 是否合格
	 * @return Boolean
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/batchSaveSamplesIsQualified.f")
	public Boolean batchSaveSamplesIsQualified(String jsonString, String isQualified){
		
		o_assessResultBO.batchSetSampleIsQ(jsonString, isQualified);
		
		return true;
	}
	
	/**
	 * <pre>
	 * 保存评价人信息
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId 计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/saveAssessorAndAssessResultBatch.f")
	public Boolean saveAssessorAndAssessResultBatch(String assessPlanId,String jsonString){
		//保存评价计划ID为assessPlanId的计划的流程的评价人信息
		o_assessPlanRelaProcessBO.mergeAssessPlanProcessRelaOrgEmpBatch(assessPlanId,jsonString);
		//批量添加参评人信息和评价结果
		o_assessResultBO.saveAssessorAndAssessResultBatch(assessPlanId);
		return true;
	}

	/**
	 * 保存评价计划执行中评价流程form数据.
	 * @param isDesirableAdjust
	 * @param assessmentDesc
	 * @param assessPlanId
	 * @param processId
	 * @return Boolean
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/saveResultForm.f")
	public Boolean saveResultForm(String isDesirableAdjust,String assessmentDesc,String assessPlanId,String processId){
		o_assessResultBO.saveResultForm(isDesirableAdjust, assessmentDesc,assessPlanId,processId);
		return true;
	}
	/**
	 * <pre>
	 * 添加一个补充样本
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessSampleId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/mergeSample.f")
	public Boolean mergeAssessSample(String assessSampleId){
		o_assessResultBO.mergeAssessSample(assessSampleId);
		return true;
	}
	
	/**
	 * 根据样本id集合批量删除样本.
	 * @param ids
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/removeSamplesByIds.f")
	public Boolean removeSamplesByIds(String ids){
		o_assessResultBO.removeSamplesByIds(ids);
		return true;
	}
	/**
	 * <pre>
	 *保存样本与附件关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param fileId 文件ID
	 * @param assessSampleId 样本ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessSampleRelaFile.f")
	public Map<String,String> mergeAssessSampleRelaFile(String fileId, String assessSampleId){
		o_assessResultBO.mergeAssessSampleRelaFile(fileId, assessSampleId);
		
		Map<String,String> map = new HashMap<String,String>();
		FileUploadEntity fileUploadEntity = o_fileUploadBO.queryFileById(fileId);
		if(null != fileUploadEntity){
			map.put("fileName", fileUploadEntity.getOldFileName());
		}
		map.put("fileId", fileId);
		map.put("success", "true");
		return map;
	}
	
	/**
	 * 下载样本文件.
	 * @param fileId
	 * @param assessSampleId
	 * @return Map<String,String>
	 * @throws IOException 
	 */
	@RequestMapping("/icm/assess/downloadSampleRelaFile.f")
	public void downloadSampleRelaFile(String fileId, String assessSampleId, OutputStream os, HttpServletResponse response) throws IOException{
		AssessSampleRelaFile assessSampleRelaFile = o_assessResultBO.findAssessSampleRelaFileByAssessSampleIdAndFileId(fileId, assessSampleId);
		if(null != assessSampleRelaFile){
			FileUploadEntity fileUpload = assessSampleRelaFile.getFile();
			if(null != fileUpload){
				response.setContentType(fileUpload.getFileType().getValue());
				response.setHeader("Content-Disposition", "attachment;filename="
						+ URLEncoder.encode(fileUpload.getOldFileName(), "UTF-8"));
				
				IOUtils.write(fileUpload.getContents(), os);
			}
		}
	}
	
	/**
	 * <pre>
	 *批量保存样本
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessResultId 评价结果ID
	 * @param jsonString 评价样本信息 [{assessSampleId:'dfdafe',isQualified:'NAN',code:'1235332',name:'2012年1月12日的审批单',comment:'未产生该单子'}]
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessSampleBatch.f")
	public Boolean mergeAssessSampleBatch(String assessResultId, String jsonString){
		o_assessResultBO.mergeAssessSampleBatch(assessResultId, jsonString);
		return true;
	}
	/**
	 * <pre>
	 *保存评价信息
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessorId 参评人
	 * @param jsonString 评价结果信息 [{assessResultId:'rew32ff',hasDefect:true,comment:'存在未审批的缺陷'},{...}]
	 * @return 
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessResultBatch.f")
	public Boolean mergeAssessResultBatch(String empId, String jsonString,String assessPlanId,String processId){
		String assessorId="";
		String orgId="";
		if(StringUtils.isBlank(empId)){
			empId = UserContext.getUser().getEmpid();
		}
		AssessPlan assessPlan=o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
		if(null!=assessPlan){
			Set<Assessor> assessorList=assessPlan.getAssessor();
			for(Assessor assessor:assessorList){
				if(assessor.getEmp().getId().equals(empId)){
					orgId=assessor.getEmp().getSysOrganization().getId();
					assessorId=assessor.getId();
				}
			}
		}
		
		o_assessResultBO.mergeAssessResultBatch(assessorId, jsonString,assessPlanId,processId,orgId);
		return true;
	}
	/**
	 * <pre>
	 * 提交评价信息
	 * 保存参评人实际完成日期和生成该参评人评价产生的缺陷数据
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessorId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/mergeAssessorAndDefect.f")
	public Boolean mergeAssessorAndDefect(String assessorId){
		o_assessResultBO.mergeAssessorAndDefect(assessorId);
		return true;
	}
	/**
	 * <pre>
	 *移除样本与文件的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param fileId
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/removeAssessSampleRelaFile.f")
	public Boolean removeAssessSampleRelaFile(String assessSampleId){
		o_assessResultBO.removeAssessSampleRelaFile(assessSampleId);
		return true;
	}
	/**
	 * <pre>
	 * 查询参评人列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 页容量
	 * @param start 起始值
	 * @param query 查询条件
	 * @param assessPlanId 评价计划ID
	 * @param assessorId 参评人ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("icm/assess/findAssessorListByPage.f")
	public Map<String,Object> findAssessorListByPage(int limit, int start, String query,String assessPlanId, String assessorId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<Assessor> page = new Page<Assessor>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessResultBO.findAssessorPageBySome(page, query, assessPlanId, assessorId);
		List<Assessor> assessorList = page.getResult();
		for (Assessor assessor : assessorList) {
			Map<String, Object> assessorMap = new HashMap<String, Object>();
			assessorMap.put("id", assessor.getId());//参评人ID
			assessorMap.put("empName", assessor.getEmp().getEmpname()+"("+assessor.getEmp().getEmpcode()+")");//参评人
			assessorMap.put("deadLine", assessor.getDeadLine());//完成期限
			assessorMap.put("actualFinishDate", assessor.getActualFinishDate());//实际完成日期
			Set<AssessResult> assessResultSet = assessor.getAssessResult();
			int allNumByPracticeTest = 0;//穿行的评价点总数
			int allNumBySampleTest = 0;//抽样的评价点总数
			int numByPracticeTest = 0;//待穿行的评价点数
			int numBySampleTest = 0;//待抽样的评价点数
			for (AssessResult assessResult : assessResultSet) {
				if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessResult.getAssessMeasure().getId())){
					allNumByPracticeTest++;
					if(assessResult.getExtractedAmount()!=null && assessResult.getExtractedAmount()!= 0){
						numByPracticeTest++;
					}
				}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessResult.getAssessMeasure().getId())){
					allNumBySampleTest++;
					if(assessResult.getExtractedAmount()!=null && assessResult.getExtractedAmount()!= 0){
						numBySampleTest++;
					}
				}
			}
			assessorMap.put("allNumByPracticeTest", allNumByPracticeTest);
			assessorMap.put("allNumBySampleTest", allNumBySampleTest);
			assessorMap.put("numByPracticeTest", numByPracticeTest);
			assessorMap.put("numBySampleTest", numBySampleTest);
			datas.add(assessorMap);
		}
		map.put("datas", datas);
		map.put("totalCount", page.getTotalItems());
		return map;
	} 
	
	/**
	 * <pre>
	 * 根据参评人ID查询该参评人评价的流程列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @modify 吴德福
	 * @param empId 参评人ID
	 * @param executionId 流程id
	 * @return Map<String,Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessorRelaProcessFormListByAssessorId.f")
	public Map<String,Object> findAssessorRelaProcessFormListByAssessorId(String assessPlanId, String executionId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		AssessPlanBpmObject assessPlanBpmObject = o_assessPlanBpmBO.findBpmObjectByExecutionId(executionId);
		String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
			//根据评价计划参与流程id查询评价计划参与流程
			AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
			if(null != assessPlanRelaProcess){
				Map<String,Object> row = new HashMap<String,Object>();
				row.put("assessPlanRelaProcessId", assessPlanRelaProcess.getId());
				
				AssessPlan assessPlan = assessPlanRelaProcess.getAssessPlan();
				if(null != assessPlan){
					row.put("assessPlanMeasureId", assessPlan.getAssessMeasure().getId());
				}
				
				row.put("isPracticeTest", assessPlanRelaProcess.getIsPracticeTest());
				row.put("isSampleTest", assessPlanRelaProcess.getIsSampleTest());
				
				if(StringUtils.isNotBlank(assessPlanBpmObject.getExecuteEmpId())){
					SysEmployee executeEmp = o_employeeBO.get(assessPlanBpmObject.getExecuteEmpId());
					if(null != executeEmp){
						row.put("executeEmpId", executeEmp.getEmpname()+"("+executeEmp.getEmpcode()+")");
					}
				}
				if(StringUtils.isNotBlank(assessPlanBpmObject.getReviewerEmpId())){
					SysEmployee reviewerEmp = o_employeeBO.get(assessPlanBpmObject.getReviewerEmpId());
					if(null != reviewerEmp){
						row.put("reviewerEmpId", reviewerEmp.getEmpname()+"("+reviewerEmp.getEmpcode()+")");
					}
				}
				
				Process process = assessPlanRelaProcess.getProcess();
				if(null != process){
					Set<ProcessRelaOrg> processRelaOrgSet = process.getProcessRelaOrg();
					Set<String> orgNameSet = new HashSet<String>();
					for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
						if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
							orgNameSet.add(processRelaOrg.getOrg().getOrgname());
						}
					}
					row.put("orgName", StringUtils.join(orgNameSet, ","));
					//末级流程ID
					row.put("processId", process.getId());
					//末级流程
					row.put("name", process.getName());
					//风险水平
					String riskLevel = "";
					List<Object[]> objectList = o_processBO.findRiskStatusByProcessId(process.getId());
					if(null != objectList && objectList.size()>0){
						for (Object[] objects : objectList) {
							/**
							 * objects[5]表示：评价状态
							 * 0alarm_startus_h 高(红)
							 * 0alarm_startus_m 中(黄)
							 * 0alarm_startus_l 低(绿)
							 */
							if(null != objects[5]){
								String assessmentStatus = String.valueOf(objects[5]);
								if("0alarm_startus_h".equals(assessmentStatus)){
									riskLevel = assessmentStatus;
									break;
								}else if(riskLevel.compareTo(assessmentStatus) < 0){
									riskLevel = assessmentStatus;
								}
							}
						}
					}
					row.put("riskLevel", riskLevel);
					//流程发生频率
					if(null != process.getFrequency()){
						row.put("frequency", process.getFrequency().getName());
					}else{
						row.put("frequency", "");
					}
					if (null != process.getParent()) {
						// 显示二级流程分类名称
						row.put("parentProcess", process.getParent().getName());
						if (null != process.getParent().getParent()) {
							// 显示一级流程分类名称
							row.put("firstProcess", process.getParent().getParent().getName());
						}
					}
					
					//根据评价计划id和流程id查询对应的评价结果
					List<AssessResult> assessResultList = o_assessResultBO.findAssessResultByAssessPlanIdAndProcessId(assessPlanId, process.getId());
					if(null != assessResultList && assessResultList.size()>0){
						//参评人ID
						row.put("assessorId", assessResultList.get(0).getAssessor().getId());
						
						int allNumByPracticeTest = 0;//穿行的评价点总数
						int allNumBySampleTest = 0;//抽样的评价点总数
						int numByPracticeTest = 0;//待穿行的评价点数
						int numBySampleTest = 0;//待抽样的评价点数
						List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListByAssessPlanId(assessPlanId,process.getId());
						for (AssessResult assessResult : assessResultList) {
							if(assessResult.getProcess().getId().equals(process.getId())){
								if(null != assessResult.getAssessMeasure()){
									if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessResult.getAssessMeasure().getId())){
										allNumByPracticeTest++;
										if(!o_assessResultBO.isNotTest(assessResult, assessRelaDefectList)){//判断是否测试过，如果没有测试过则+1
											numByPracticeTest++;
										}
									}
									if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessResult.getAssessMeasure().getId())){
										allNumBySampleTest++;
										if(!o_assessResultBO.isNotTest(assessResult, assessRelaDefectList)){//判断是否测试过，如果没有测试过则+1
											numBySampleTest++;
										}
									}
								}
							}
						}
						row.put("allNumByPracticeTest", allNumByPracticeTest);
						row.put("allNumBySampleTest", allNumBySampleTest);
						row.put("numByPracticeTest", numByPracticeTest);
						row.put("numBySampleTest", numBySampleTest);
					}
					
					datas.add(row);
				}
			}
		}

		map.put("datas", datas);
		map.put("totalCount", 1);
		return map;
	}
	
	/**
	 * <pre>
	 * 根据参评人ID和流程ID查询评价结果列表
	 * 
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @param assessorId 参评人列表
	 * @param processId 流程ID
	 * @param testType 评价方式：穿行测试（ca_assessment_measure_0）或者抽样测试（ca_assessment_measure_1）
	 * @param assessPlanId
	 * @param isAll 是否查询全部：not--待测试;all--全部;
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessResultPageBySome.f")
	public Map<String,Object> findAssessResultPageBySome(String query, String assessorId, String processId, String testType, String assessPlanId, String isAll){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<AssessResult> assessResultList = o_assessResultBO.findAssessResultPageBySome(query, assessorId, processId, testType, assessPlanId, isAll);
		for (AssessResult assessResult : assessResultList) {
			Map<String, Object> assessResultMap = new HashMap<String, Object>();
			assessResultMap.put("testType", testType);
			if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(testType) && null!=assessResult.getProcessPoint()){
				assessResultMap.put("processPointId", assessResult.getProcessPoint().getId());//流程节点ID
				assessResultMap.put("processPointName", assessResult.getProcessPoint().getName());//流程节点
			}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(testType) && null!=assessResult.getControlMeasure()){
				assessResultMap.put("measureId", assessResult.getControlMeasure().getId());//控制措施
				assessResultMap.put("measureName", assessResult.getControlMeasure().getName());//控制措施
			}
			assessResultMap.put("assessResultId", assessResult.getId());
			assessResultMap.put("assessPointId", assessResult.getAssessPoint().getId());//评价点id
			assessResultMap.put("assessPointDesc", assessResult.getAssessPoint().getDesc());//评价点名称
			assessResultMap.put("assessSampleName", assessResult.getAssessPoint().getComment());//实施证据
			assessResultMap.put("extractedAmount", assessResult.getExtractedAmount());//如果是穿行测试，则为“穿几次”；如果是抽样测试，则为“抽取样本数”
			
			if(null!=assessResult.getEffectiveNumber()){
				assessResultMap.put("effectiveNumber", assessResult.getEffectiveNumber());//有效样本数
			}else{
				assessResultMap.put("effectiveNumber", "0");//有效样本数
			}
			if(null!=assessResult.getDefectNumber()){
				assessResultMap.put("defectNumber", assessResult.getDefectNumber());//无效样本数
			}else{
				assessResultMap.put("defectNumber", "0");
			}
			if(null!=assessResult.getHasDefect()){
				assessResultMap.put("hasDefect", assessResult.getHasDefect());//机算:是否存在缺陷
			}
			if(null != assessResult.getHasDefectAdjust()){
				assessResultMap.put("hasDefectAdjust", assessResult.getHasDefectAdjust());//手动调整：是否存在缺陷
			}
			if(StringUtils.isNotBlank(assessResult.getAdjustDesc())){
				assessResultMap.put("adjustDesc", assessResult.getAdjustDesc());//补充说明
			}
			if(StringUtils.isNotBlank(assessResult.getComment())){
				assessResultMap.put("comment", assessResult.getComment());//说明
			}
			
			int qualifiedNumber = 0;//合格样本数
			int notQualifiedNumber = 0;//不合格样本数
			int notApplyNumber = 0;//不适用样本数
			Set<AssessSample> assessSamples = assessResult.getAssessSamples();
			for(AssessSample assessSample : assessSamples){
				if(Contents.IS_OK_Y.equals(assessSample.getIsQualified())){
					qualifiedNumber++;
				}else if(Contents.IS_OK_N.equals(assessSample.getIsQualified())){
					notQualifiedNumber++;
				}else if(Contents.IS_OK_NAN.equals(assessSample.getIsQualified())){
					notApplyNumber++;
				}
			}
			assessResultMap.put("qualifiedNumber", qualifiedNumber);
			assessResultMap.put("notQualifiedNumber", notQualifiedNumber);
			assessResultMap.put("notApplyNumber", notApplyNumber);
			
			datas.add(assessResultMap);
		}
		map.put("datas", datas);
		map.put("totalCount", assessResultList.size());
		return map;
	}
	
	/**
	 * <pre>
	 * 查找机算结果.
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param empId
	 * @param processId
	 * @param assessPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessResult.f")
	public Map<String, Object> findAssessResult(String empId, String processId, String assessPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<AssessPlanRelaProcess> list = o_assessResultBO.findAssessPlanRelaProcessBySome(assessPlanId,processId);
		if(null!=list && list.size()>0){
			AssessPlanRelaProcess assessPlanRelaProcess=list.get(0);
			if(StringUtils.isNotBlank(assessPlanRelaProcess.getIsDesirableAdjust())){
				data.put("isDesirableAdjust", assessPlanRelaProcess.getIsDesirableAdjust());
			}
			data.put("assessmentDesc", assessPlanRelaProcess.getAssessmentDesc());
				
		}
		String ret = "";
		int count=0;
		//合格数
		int notDefectNum = 0;
		//不合格数
		int hasDefectNum = 0;
		List<AssessResult> assessResultList = o_assessResultBO.findAssessResultByAssessPlanIdAndProcessId(assessPlanId, processId);
		if(null != assessResultList && assessResultList.size()>0){
			for(AssessResult assessResult : assessResultList){
				if(null != assessResult.getProcessPoint()){
					count++;
					if(null != assessResult.getHasDefect() && assessResult.getHasDefect()){
						//合格
						notDefectNum++;
					}else{
						//不合格
						hasDefectNum++;
					}
				}
			}
			if(notDefectNum == count){
				//完全符合
				ret = Contents.AssessResult_STATUS_OK;
			}else if(hasDefectNum == count){
				//不符合
				ret = Contents.AssessResult_STATUS_N;
			}else if(notDefectNum+hasDefectNum == count){
				//部分符合
				ret = Contents.AssessResult_STATUS_YORN;
			}
		}
		data.put("assessResult", ret);
		map.put("data", data);
		map.put("success", true);
		return map;
	}
	
	/**
	 * <pre>
	 * 根据评价结果id查询样本列表.
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessResultId
	 * @param isQualified
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessSampleListBySome.f")
	public Map<String,Object> findAssessSampleListBySome(String assessResultId, String isQualified){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		if(null!=isQualified&&isQualified.equals("undefined")){
			isQualified=null;
		}
		List<AssessSample> assessSampleList=o_assessResultBO.findAssessSampleListBySome(assessResultId, isQualified);
		List<String> assessSampleIdList=new ArrayList<String>();
		for(AssessSample assessSample : assessSampleList){
			assessSampleIdList.add(assessSample.getId());
		}
		
		List<AssessSampleRelaFile> assessSampleRelaFileList = o_assessResultBO.findAssessResultListByAssessSampleIds(assessSampleIdList);
		for(AssessSample assessSample:assessSampleList){
			Map<String, Object> assessSampleMap = new HashMap<String, Object>();
			assessSampleMap.put("assessSampleId", assessSample.getId());
			assessSampleMap.put("type", assessSample.getType());
			assessSampleMap.put("code", assessSample.getCode());
			if(StringUtils.isNotBlank(assessSample.getName())){
				assessSampleMap.put("name", assessSample.getName());
			}else{
				assessSampleMap.put("name", "");
			}
			if(StringUtils.isNotBlank(assessSample.getIsQualified())){
				assessSampleMap.put("isQualified", assessSample.getIsQualified());
				if(Contents.IS_OK_NAN.equals(assessSample.getIsQualified())){
					//判断'不适用'的样本是否有补充来源
					List<AssessSample> assessSamples = o_assessResultBO.findSupplementAssessSampleByAssessSampleId(assessSample.getId());
					if(null != assessSamples && assessSamples.size()>0){
						assessSampleMap.put("isHasSupplement","Y");
					}else{
						assessSampleMap.put("isHasSupplement ","N");
					}
				}else{
					assessSampleMap.put("isHasSupplement ","N");
				}
			}else{
				assessSampleMap.put("isQualified", "");
				assessSampleMap.put("isHasSupplement","N");
			}
			
			if(null!=assessSample.getSourceSample()){
				assessSampleMap.put("sourceSample", assessSample.getSourceSample().getCode());
			}
			if(null!=assessSample.getComment()&&assessSample.getComment()!="null"){
				assessSampleMap.put("comment", assessSample.getComment());
			}
			for(AssessSampleRelaFile assessSampleRelaFile:assessSampleRelaFileList){
				if(assessSampleRelaFile.getAssessSample().getId().equals(assessSample.getId())){
					assessSampleMap.put("file", assessSampleRelaFile.getFile().getNewFileName());
					assessSampleMap.put("fileId", assessSampleRelaFile.getFile().getId());
				}
			}
			
			datas.add(assessSampleMap);
		}
		map.put("datas", datas);
		map.put("totalCount",assessSampleList.size());
		return map;
	}

	/**
	 * 根据评价计划id和流程id查询样本列表.
	 * @author 吴德福
	 * @param assessResultId
	 * @param processId
	 * @param type 穿行--through;抽样--sampling;
	 * @param query
	 * @return Map<String,Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessSampleListByAssessPlanIdAndProcessId.f")
	public Map<String,Object> findAssessSampleListByAssessPlanIdAndProcessId(String assessPlanId, String processId, String type, String query){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		List<AssessSample> assessSampleList = o_assessResultBO.findAssessSampleListByAssessPlanIdAndProcessId(assessPlanId, processId, type, query);
		List<String> assessSampleIdList=new ArrayList<String>();
		for(AssessSample assessSample : assessSampleList){
			assessSampleIdList.add(assessSample.getId());
		}
		
		List<AssessSampleRelaFile> assessSampleRelaFileList = o_assessResultBO.findAssessResultListByAssessSampleIds(assessSampleIdList);
		for(AssessSample assessSample:assessSampleList){
			Map<String, Object> assessSampleMap = new HashMap<String, Object>();
			assessSampleMap.put("assessPointDesc", assessSample.getAssessResult().getAssessPoint().getDesc());
			assessSampleMap.put("assessSampleName", assessSample.getAssessResult().getAssessPoint().getComment());
			assessSampleMap.put("assessSampleId", assessSample.getId());
			assessSampleMap.put("type", assessSample.getType());
			assessSampleMap.put("code", assessSample.getCode());
			if(StringUtils.isNotBlank(assessSample.getName())){
				assessSampleMap.put("name", assessSample.getName());
			}else{
				assessSampleMap.put("name", "");
			}
			if(StringUtils.isNotBlank(assessSample.getIsQualified())){
				assessSampleMap.put("isQualified", assessSample.getIsQualified());
				if(Contents.IS_OK_NAN.equals(assessSample.getIsQualified())){
					//判断'不适用'的样本是否有补充来源
					List<AssessSample> assessSamples = o_assessResultBO.findSupplementAssessSampleByAssessSampleId(assessSample.getId());
					if(null != assessSamples && assessSamples.size()>0){
						assessSampleMap.put("isHasSupplement","Y");
					}else{
						assessSampleMap.put("isHasSupplement ","N");
					}
				}else{
					assessSampleMap.put("isHasSupplement ","N");
				}
			}else{
				assessSampleMap.put("isQualified", "");
				assessSampleMap.put("isHasSupplement","N");
			}
			
			if(null!=assessSample.getSourceSample()){
				assessSampleMap.put("sourceSample", assessSample.getSourceSample().getCode());
			}
			if(null!=assessSample.getComment()&&assessSample.getComment()!="null"){
				assessSampleMap.put("comment", assessSample.getComment());
			}
			for(AssessSampleRelaFile assessSampleRelaFile:assessSampleRelaFileList){
				if(assessSampleRelaFile.getAssessSample().getId().equals(assessSample.getId())){
					assessSampleMap.put("file", assessSampleRelaFile.getFile().getNewFileName());
					assessSampleMap.put("fileId", assessSampleRelaFile.getFile().getId());
				}
			}
			
			datas.add(assessSampleMap);
		}
		map.put("datas", datas);
		map.put("totalCount",assessSampleList.size());
		return map;
	}
	
	/**
	 * <pre>
	 * 根据评价计划id和流程id查询测试底稿结果预览.
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId 评价计划ID
	 * @param executionId 流程id
	 * @return Map<String, Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/findpracticeAnalysis.f")
	public Map<String, Object> findPracticeAnalysis(String assessPlanId, String executionId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		/*
		 * 针对几个流程进行统计.
		 * 1.executionId不为空，表示计划执行第2步结果分析页面显示使用，只有一个流程
		 * 2.executionId为空，表示评价计划弹出详细页面显示使用
		 */
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = new ArrayList<AssessPlanRelaProcess>();
		
		String processId = "";
		if(StringUtils.isNotBlank(executionId)){
			//计划执行第2步结果分析页面显示使用
			AssessPlanBpmObject assessPlanBpmObject = o_assessPlanBpmBO.findBpmObjectByExecutionId(executionId);
			String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
			if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
				//根据评价计划参与流程id查询评价计划参与流程
				AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
				if(null != assessPlanRelaProcess){
					if(!assessPlanRelaProcessList.contains(assessPlanRelaProcess)){
						assessPlanRelaProcessList.add(assessPlanRelaProcess);
					}
					processId = assessPlanRelaProcess.getProcess().getId();
				}
			}
		}else{
			//评价计划弹出详细页面显示使用
			AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
			if(null != assessPlan){
				Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
				for (AssessPlanRelaProcess assessPlanRelaProcess2 : assessPlanRelaProcesses) {
					if(!assessPlanRelaProcessList.contains(assessPlanRelaProcess2)){
						assessPlanRelaProcessList.add(assessPlanRelaProcess2);
					}
				}
			}
		}
		
		List<AssessResult> assessResultList = o_assessResultBO.findAssessResultByAssessPlanIdAndProcessId(assessPlanId, processId);
			
		for(AssessPlanRelaProcess assessPlanRelaProcessTemp : assessPlanRelaProcessList){
			Map<String, Object> data = new HashMap<String, Object>();
			Process process = assessPlanRelaProcessTemp.getProcess();
			//末级流程ID
			data.put("id", process.getId());
			data.put("assessPlanId", assessPlanRelaProcessTemp.getAssessPlan().getId());
			//流程分类名称
			if (null != process.getParent()) {
				data.put("parentProcess", process.getParent().getName());
			}
			//末级流程
			data.put("name", process.getName());
			Set<String> orgNameSet = new HashSet<String>();
			Set<ProcessRelaOrg> processRelaOrgSet =process.getProcessRelaOrg();
			for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
				if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
					orgNameSet.add(processRelaOrg.getOrg().getOrgname());
				}
			}
			//责任部门名称
			data.put("orgNames", StringUtils.join(orgNameSet, ","));
			
			Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmps = assessPlanRelaProcessTemp.getAssessPlanProcessRelaOrgEmp();
			for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmps) {
				if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType())){
					//评价人
					data.put("executeEmpName", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
				}else if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType())){
					//复核人
					data.put("reviewerEmpName", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
				}
			}
			
			//评价日期
			if(null != assessPlanRelaProcessTemp.getAssessDate()){
				data.put("assessDate", DateUtils.formatDate(assessPlanRelaProcessTemp.getAssessDate(), "yyyy-MM-dd"));
			}
			//复核日期
			if(null != assessPlanRelaProcessTemp.getReviewDate()){
				data.put("reviewDate", DateUtils.formatDate(assessPlanRelaProcessTemp.getReviewDate(), "yyyy-MM-dd"));
			}
			
			//流程节点set去重
			Set<String> processPointIdSet = new HashSet<String>(0);
			//控制措施set去重
			Set<String> assessMeasureIdSet = new HashSet<String>(0);
			
			//穿行测试id集合
			StringBuilder practiceIds = new StringBuilder();
			//抽样测试id集合
			StringBuilder sampletestIds = new StringBuilder();
			
			//合格的穿行评价点数
			int qualifiedNumByPracticeTest=0;
			//穿行的评价点总数
			int allNumByPracticeTest=0;
			//穿行评价点通过率
			String qualifiedRateByPracticeTest= "0.00";
			
			//合格的穿行样本数
			int qualifiedPracticeTestSampleNum=0;
			//穿行样本总数
			int practiceTestSampleNum=0;
			//穿行样本合格率
			String qualifiedPracticeTestSample="0.00";
			
			//合格的抽样样本数
			int qualifiedSampleTestSampleNum=0;
			//抽样样本总数
			int sampleTestSampleNum=0;
			//抽样样本合格率
			String qualifiedSampleTestSample="0.00";
			
			//合格的抽样评价点数
			int qualifiedNumBySampleTest=0;
			//抽样的评价点总数
			int allNumBySampleTest=0;
			//抽样评价点通过率
			String qualifiedRateBySampleTest="0.00";
			
			//穿行测试结果
			String autoTestResult = "";
			String adjustTestResult = "";
			String adjustDesc = "";
			if(StringUtils.isNotBlank(assessPlanRelaProcessTemp.getIsDesirableAdjust())){
				if(Contents.IS_OK_Y.equals(assessPlanRelaProcessTemp.getIsDesirableAdjust())){
					adjustTestResult = "完全符合";
				}else if(Contents.IS_OK_YORN.equals(assessPlanRelaProcessTemp.getIsDesirableAdjust())){
					adjustTestResult = "部分符合";
				}else if(Contents.IS_OK_N.equals(assessPlanRelaProcessTemp.getIsDesirableAdjust())){
					adjustTestResult = "不符合";
				}else if(Contents.IS_OK_NAN.equals(assessPlanRelaProcessTemp.getIsDesirableAdjust())){
					adjustTestResult = "不适用";
				}
				if(StringUtils.isNotBlank(assessPlanRelaProcessTemp.getAssessmentDesc())){
					adjustDesc = assessPlanRelaProcessTemp.getAssessmentDesc();
				}
			}
			
			//穿行测试缺陷数
			int practiceHasDefectNum=0;
			//抽样测试缺陷数
			int sampleHasDefectNum=0;
			
			//数字格式化
			DecimalFormat df = new DecimalFormat("0.00");
			
			for(AssessResult assessResult : assessResultList){
				if(!process.getId().equals(assessResult.getProcess().getId())){
					continue;
				}
				//穿行测试
				if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessResult.getAssessMeasure().getId())){
					practiceIds.append(assessResult.getId());
					if(!"".equals(practiceIds.toString())){
						practiceIds.append(",");
					}
					allNumByPracticeTest++;
					if(null!=assessResult.getProcessPoint()){
						processPointIdSet.add(assessResult.getProcessPoint().getId());
					}
					/*
					 * 评价点合格标准：
					 * 1.人为调整不为空，且为是，合格
					 * 2.人为调整为空，自动计算为是，合格
					 */
					if(null == assessResult.getHasDefectAdjust()){
						//人为调整为空
						if(null != assessResult.getHasDefect() && assessResult.getHasDefect()){
							//自动计算为是
							qualifiedNumByPracticeTest++;
						}else{
							practiceHasDefectNum++;
						}
					}else{
						//人为调整不为空
						if(assessResult.getHasDefectAdjust()){
							qualifiedNumByPracticeTest++;
						}else{
							practiceHasDefectNum++;
						}
					}
				}
				//抽样测试
				if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessResult.getAssessMeasure().getId())){
					sampletestIds.append(assessResult.getId());
					if(!"".equals(sampletestIds.toString())){
						sampletestIds.append(",");
					};
					allNumBySampleTest++;
					if(null!=assessResult.getControlMeasure()){
						assessMeasureIdSet.add(assessResult.getControlMeasure().getId());
					}
					/*
					 * 评价点合格标准：
					 * 1.人为调整不为空，且为是，合格
					 * 2.人为调整为空，自动计算为是，合格
					 */
					if(null == assessResult.getHasDefectAdjust()){
						//人为调整为空
						if(null != assessResult.getHasDefect() && assessResult.getHasDefect()){
							//自动计算为是
							qualifiedNumBySampleTest++;
						}else{
							sampleHasDefectNum++;
						}
					}else{
						//人为调整不为空
						if(assessResult.getHasDefectAdjust()){
							qualifiedNumBySampleTest++;
						}else{
							sampleHasDefectNum++;
						}
					}
				}
			}
			
			if(practiceHasDefectNum==allNumByPracticeTest){
				//不符合
				autoTestResult = Contents.AssessResult_STATUS_N;
			}else if(qualifiedNumByPracticeTest==allNumByPracticeTest){
				//完全符合
				autoTestResult = Contents.AssessResult_STATUS_OK;
			}else if(practiceHasDefectNum+qualifiedNumByPracticeTest == allNumByPracticeTest){
				//部分符合
				autoTestResult = Contents.AssessResult_STATUS_YORN;
			}
			
			//流程节点数
			data.put("processPointNO", processPointIdSet.size());
			//穿行测试结果
			data.put("autoTestResult", autoTestResult);
			data.put("adjustTestResult", adjustTestResult);
			data.put("adjustDesc", adjustDesc);
			//控制措施数
			data.put("assessMeasureNO", assessMeasureIdSet.size());
			
			if(allNumByPracticeTest!=0){
				qualifiedRateByPracticeTest= df.format((double)qualifiedNumByPracticeTest/allNumByPracticeTest);
			}
			if(allNumBySampleTest!=0){
				qualifiedRateBySampleTest=df.format((double)qualifiedNumBySampleTest/allNumBySampleTest);
			}
			//评价点数
			data.put("allNumByPracticeTest", allNumByPracticeTest);//穿行测试评价点数
			data.put("allNumBySampleTest", allNumBySampleTest);//抽样测试评价点数
			//评价点通过率
			data.put("qualifiedRateByPracticeTest", qualifiedRateByPracticeTest);//穿行测试评价点通过率
			data.put("qualifiedRateBySampleTest", qualifiedRateBySampleTest);//抽样测试评价点通过率
			
			qualifiedPracticeTestSampleNum = o_assessResultBO.findQualifiedPracticeTestSampleNum(practiceIds.toString());
			practiceTestSampleNum=o_assessResultBO.finPracticeTestSampleNum(practiceIds.toString());
			qualifiedSampleTestSampleNum = o_assessResultBO.qualifiedSampleTestSampleNum(sampletestIds.toString());
			sampleTestSampleNum=o_assessResultBO.findSampleTestSampleNum(sampletestIds.toString());
			if(practiceTestSampleNum!=0){
				qualifiedPracticeTestSample=df.format((double)qualifiedPracticeTestSampleNum/practiceTestSampleNum);
			}
			if(sampleTestSampleNum!=0){
				qualifiedSampleTestSample=df.format((double)qualifiedSampleTestSampleNum/sampleTestSampleNum);
			}
			//样本数
			data.put("practiceTestSampleNum", practiceTestSampleNum);//穿行测试样本数
			data.put("sampleTestSampleNum", sampleTestSampleNum);//抽样测试样本数
			//样本合格率
			data.put("qualifiedPracticeTestSample", qualifiedPracticeTestSample);//穿行测试样本合格率
			data.put("qualifiedSampleTestSample", qualifiedSampleTestSample);//抽样测试样本合格率
			
			//缺陷数
			data.put("practiceHasDefectNum", practiceHasDefectNum);//穿行测试缺陷数
			data.put("sampleHasDefectNum", sampleHasDefectNum);//抽样测试缺陷数
			
			datas.add(data);
		}
		
		map.put("datas", datas);
		map.put("totalCount",datas.size());
		
		return map;
	}
	
	/**
	 * 计划执行复核查询结果分析.
	 * @param businessId
	 * @param executionId
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/findResultAnalysisByAssessplanIdAndExecutionId.f")
	public Map<String,Object> findResultAnalysisByAssessplanIdAndExecutionId(String businessId, String executionId){
		Map<String, Object> data = new HashMap<String, Object>();
		AssessPlanBpmObject assessPlanBpmObject = o_assessPlanBpmBO.findBpmObjectByExecutionId(executionId);
		String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
			//根据评价计划参与流程id查询评价计划参与流程
			AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
			if(null != assessPlanRelaProcess){
				if(StringUtils.isNotBlank(assessPlanRelaProcess.getDesc())){
					data.put("sampleAnalysisOfResults", assessPlanRelaProcess.getDesc());
				}else{
					data.put("sampleAnalysisOfResults", "");
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 保存评价点参与人的结果分析.
	 * @author 吴德福
	 * @param assessPlanId
	 * @param executionId
	 * @param resultAnalysis 结果分析
	 * @return Map<String, Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/assess/saveAssessorResultAnalysis.f")
	public Map<String, Object> saveAssessorResultAnalysis(String assessPlanId, String executionId, String resultAnalysis) {
		AssessPlanBpmObject assessPlanBpmObject = o_assessPlanBpmBO.findBpmObjectByExecutionId(executionId);
		String assessPlanRelaProcessId = assessPlanBpmObject.getAssessPlanRelaProcessId();
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){
			//根据评价计划参与流程id查询评价计划参与流程
			AssessPlanRelaProcess assessPlanRelaProcess = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessById(assessPlanRelaProcessId);
			if(null != assessPlanRelaProcess){
				if(StringUtils.isNotBlank(resultAnalysis)){
					assessPlanRelaProcess.setDesc(resultAnalysis);
					o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcess(assessPlanRelaProcess);
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 根据评价结果id查询评价点描述与评价点实施证据.
	 * @param assessResultId
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/findAssessPointViewByAssessResultId.f")
	public Map<String, Object> findAssessPointViewByAssessResultId(String assessResultId){
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(assessResultId)){
			Map<String, Object> row = new HashMap<String, Object>();
			AssessResult assessResult = o_assessResultBO.findAssessResultById(assessResultId);
			if(null != assessResult){
				AssessPoint assessPoint = assessResult.getAssessPoint();
				if(null != assessPoint){
					if(StringUtils.isNotBlank(assessPoint.getDesc())){
						row.put("assessPointDesc", assessPoint.getDesc());
					}else{
						row.put("assessPointDesc", "");
					}
					if(StringUtils.isNotBlank(assessPoint.getComment())){
						row.put("assessPointComment", assessPoint.getComment());
					}else{
						row.put("assessPointComment", "");
					}
					map.put("data", row);
				}
			}
		}
		
		map.put("success", true);
		return map;
	}
}