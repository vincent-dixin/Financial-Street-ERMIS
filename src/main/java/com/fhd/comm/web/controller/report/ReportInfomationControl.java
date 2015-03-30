package com.fhd.comm.web.controller.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.report.ReportInfomationBO;
import com.fhd.comm.entity.report.ReportInfomation;
import com.fhd.comm.entity.report.ReportRelaAssessment;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessPlanRelaProcessBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.process.entity.Process;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 内控报告Control.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Controller
public class ReportInfomationControl {

	@Autowired
	private ReportInfomationBO o_reportInfomationBO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	
	/**
	 * 
	 * <pre>
	 * 保存公司年度评价报告.
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @param name
	 * @param code
	 * @param assessplanId
	 * @param reportData
	 * @param reportType
	 * @param request
	 * @return Map<String,Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/comm/report/saveCompanyYearReport.f")
	public Map<String,Object> saveCompanyYearReport(String id, String name, String code, String assessplanId, String reportData, String reportType, HttpServletRequest request) throws Exception{
		
		ReportInfomation report = null;
		
		if(StringUtils.isNotEmpty(id)){
			report = o_reportInfomationBO.findReportById(id);
		}else {
			report =  new ReportInfomation();
			report.setId(Identities.uuid());
			if(StringUtils.isNotBlank(reportType)){
				report.setReportType(new DictEntry(reportType));
			}
		}
		
		if(StringUtils.isNotBlank(name)){
			report.setReportName(name);
		}
		if(StringUtils.isNotBlank(code)){
			report.setReportCode(code);
		}
		
		o_reportInfomationBO.saveCompanyYearReport(report, reportData, assessplanId, request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		
		return map;
	}
	
	/**
	 * 保存测试评价报告.
	 * @author 吴德福
	 * @param id
	 * @param reportName
	 * @param reportCode
	 * @param assessplanId
	 * @param reportDataText
	 * @param request
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/comm/report/saveTestReport.f")
	public Map<String,Object> saveTestReport(String id, String reportName, String reportCode, String assessplanId, String reportDataText, HttpServletRequest request) throws Exception{
		
		ReportInfomation report = null;
		
		if(StringUtils.isNotEmpty(id)){
			report = o_reportInfomationBO.findReportById(id);
		}else {
			report = new ReportInfomation();
			report.setId(Identities.uuid());
			report.setReportType(new DictEntry("test_report"));
			
			double versionCode = o_reportInfomationBO.findVersionCodeByPlanId(assessplanId, Contents.ASSESSMENT_REPORT_TYPE_TEST);
			AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessplanId);
			if(null != assessPlan){
				report.setReportName(assessPlan.getName()+"测试报告"+String.valueOf((int)versionCode+1.0));
			}
			report.setReportCode(String.valueOf((int)versionCode+1.0));
		}
		if(StringUtils.isNotBlank(reportName)){
			report.setReportName(reportName);
		}
		if(StringUtils.isNotBlank(reportCode)){
			report.setReportCode(reportCode);
		}
		
		o_reportInfomationBO.saveCompanyYearReport(report, reportDataText, assessplanId, request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		
		return map;
	}
	
	/**
	 * 根据测试报告id查询测试报告信息.
	 * @param reportId
	 * @return Map<String, Object>
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping("/comm/report/findAssessPlanTestReportbyId.f")
	public Map<String, Object> findAssessPlanTestReportbyId(String reportId) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> row = null;
		ReportInfomation reportInfomation = o_reportInfomationBO.findReportById(reportId);
		if(null != reportInfomation){
			row = new HashMap<String, Object>();
			row.put("id", reportInfomation.getId());
			row.put("reportCode", reportInfomation.getReportCode());
			row.put("reportName", reportInfomation.getReportName());
			if(null != reportInfomation.getReportData()){
				row.put("reportData", IOUtils.toString(new ByteArrayInputStream(reportInfomation.getReportData()),"GBK"));
			}
			
			Set<ReportRelaAssessment> reportRelaAssessPlans = reportInfomation.getReportRelaAssessPlans();
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<HashMap<String, Object>> reportRelaAssesslist = new ArrayList<HashMap<String, Object>>();
			
			for (ReportRelaAssessment reportRelaAssessment : reportRelaAssessPlans) {
				list.add(reportRelaAssessment.getAssessPlan().getId() );
				HashMap<String, Object> reportRelaAssessJsonMap = new HashMap<String, Object>();
				reportRelaAssessJsonMap.put("id", reportRelaAssessment.getAssessPlan().getId());
				reportRelaAssessJsonMap.put("name", reportRelaAssessment.getAssessPlan().getName());
				reportRelaAssessJsonMap.put("code", reportRelaAssessment.getAssessPlan().getCode());
				reportRelaAssesslist.add(reportRelaAssessJsonMap);
			}
			row.put("reportRelaAssesslist", reportRelaAssesslist);
			row.put("assessplanId", StringUtils.join(list, ","));
		}
		
		map.put("data", row);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * 提交报告
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @param name
	 * @param code
	 * @param assessplanId
	 * @param reportData
	 * @param reportType
	 * @param opinion
	 * @param processInstanceId
	 * @param transition
	 * @param request
	 * @return Map<String,Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/comm/report/savecompanyyearreportsubmit.f")
	public Map<String,Object> saveCompanyYearReportSubmit(String id, String name, 
			String code, String assessplanId, String reportData, String reportType, String opinion,
			String processInstanceId, String transition, HttpServletRequest request) throws Exception{
		
		ReportInfomation report = null;
		
		if(StringUtils.isNotEmpty(id)){
			report = o_reportInfomationBO.findReportById(id);
		}else {
			report = new ReportInfomation();
			report.setId(Identities.uuid());
			if(StringUtils.isNotBlank(reportType)){
				report.setReportType(new DictEntry(reportType));
			}
		}
		
		if(StringUtils.isNotBlank(name)){
			report.setReportName(name);
		}
		if(StringUtils.isNotBlank(code)){
			report.setReportCode(code);
		}
		
		o_reportInfomationBO.saveCompanyYearReportSubmit(report, reportData, assessplanId, opinion, processInstanceId, transition, request);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		
		return map;
	}
	
	/**
	 * 根据id集合批量删除评价报告.
	 * @param ids 对象id集合
	 * @param response
	 * @throws IOException 
	 */
    @RequestMapping("/comm/report/removeReportInfomationByIds.f")
    public void removeReportInfomationByIds(String ids, HttpServletResponse response) throws IOException{
    	PrintWriter out = response.getWriter();
    	try {
    		if(StringUtils.isNotBlank(ids)){
    			o_reportInfomationBO.removeReportByIds(ids);
    		}
			
			out.print("true");
    	} catch (Exception e) {
			e.printStackTrace();
			out.print("false");
		} finally{
			if(null != out){
				out.close();
			}
		}
    }
    
    /**
	 * 查询评价报告列表.
	 * @author 吴德福
	 * @param limit
	 * @param start
	 * @param sort
	 * @param dir
	 * @param query
	 * @return Map<String,Object>
	 */
	@ResponseBody
    @RequestMapping("/comm/report/findReportInfomationList.f")
    public Map<String,Object> findReportInfomationList(int limit, int start, String sort, String query, String reportType){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		Page<ReportInfomation> page = new Page<ReportInfomation>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_reportInfomationBO.findReportListBySome(page, sort, query,reportType);
		List<ReportInfomation> assessmentReportList = page.getResult();
		if(null != assessmentReportList && assessmentReportList.size()>0){
			Map<String, Object> row = null;
			for(ReportInfomation ap : assessmentReportList){
				row = new HashMap<String, Object>();
				//id
				row.put("id", ap.getId());
				//版本号
				row.put("code", ap.getReportCode());
				//标题
				row.put("name", ap.getReportName());
				//公司名称
				if(null != ap.getCompany()){
					row.put("companyName", ap.getCompany().getOrgname());
				}else{
					row.put("companyName", UserContext.getUser().getCompanyName());
				}
				//状态
				row.put("status", ap.getStatus());
				//创建人
				if(null != ap.getCreateBy()){
					row.put("createEmp", ap.getCreateBy().getEmpname());
				}else{
					row.put("createEmp", "");
				}
				//执行状态
				row.put("dealStatus", ap.getExecuteStatus());
				//创建时间
				if(null != ap.getCreateTime()){
					row.put("createDate", DateUtils.formatDate(ap.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				}else{
					row.put("createDate", "");
				}
				//修改人
				if(null != ap.getLastModifyBy()){
					row.put("modifyEmp", ap.getLastModifyBy().getEmpname());
				}else{
					row.put("modifyEmp", "");
				}
				//修改时间
				if(null != ap.getLastModifyTime()){
					row.put("modifyDate", DateUtils.formatDate(ap.getLastModifyTime(), "yyyy-MM-dd HH:mm:ss"));
				}else{
					row.put("modifyDate", "");
				}
				
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}

	/**
	 * 公司年度评价报告列表.
	 * @param reportId
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/comm/report/findCompanyYearReport.f")
	public Map<String,Object> findCompanyYearReport(String reportId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		ReportInfomation reportInfomation = o_reportInfomationBO.findReportById(reportId);
		jsonMap.put("id", reportInfomation.getId());
		jsonMap.put("name", reportInfomation.getReportName());
		jsonMap.put("code", reportInfomation.getReportCode());
		if(null != reportInfomation.getReportData()){
			jsonMap.put("reportDataText", IOUtils.toString(new ByteArrayInputStream(reportInfomation.getReportData()),"GBK"));
		}
		
		Set<ReportRelaAssessment> reportRelaAssessPlans = reportInfomation.getReportRelaAssessPlans();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<HashMap<String, Object>> reportRelaAssesslist = new ArrayList<HashMap<String, Object>>();
		
		for (ReportRelaAssessment reportRelaAssessment : reportRelaAssessPlans) {
			list.add(reportRelaAssessment.getAssessPlan().getId() );
			HashMap<String, Object> reportRelaAssessJsonMap = new HashMap<String, Object>();
			reportRelaAssessJsonMap.put("id", reportRelaAssessment.getAssessPlan().getId());
			reportRelaAssessJsonMap.put("name", reportRelaAssessment.getAssessPlan().getName());
			reportRelaAssessJsonMap.put("code", reportRelaAssessment.getAssessPlan().getCode());
			reportRelaAssesslist.add(reportRelaAssessJsonMap);
		}
		jsonMap.put("reportRelaAssesslist", reportRelaAssesslist);
		jsonMap.put("assessplanId", StringUtils.join(list, ","));
		
		map.put("data", jsonMap);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 下载报告.
	 * @author 胡迪新
	 * @param id
	 * @param os
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/comm/report/downloadReport.f")
	public void downloadReport(String id, OutputStream os, HttpServletResponse response) throws Exception {
		ReportInfomation reportInfomation = o_reportInfomationBO.findReportById(id);
		
		response.setContentType("doc");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ URLEncoder.encode(reportInfomation.getReportName()+".doc", "UTF-8"));
		
		IOUtils.write(reportInfomation.getReportDoc(), os);
	}
	
	/**
	 * 查询评价报告下评价范围列表.
	 * @author 吴德福
	 * @param limit
	 * @param start
	 * @param sort
	 * @param query
	 * @param assessplanId 评价计划id
	 * @return Map<String,Object>
	 */
	@ResponseBody
    @RequestMapping("/comm/report/findReportProcessList.f")
    public Map<String,Object> findReportProcessList(int limit, int start, String sort, String query, String assessplanId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByPage(page, query, assessplanId);
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
		if(null != assessPlanRelaProcessList && assessPlanRelaProcessList.size()>0){
			Map<String, Object> row = null;
			for(AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList){
				row = new HashMap<String, Object>();
				//id
				row.put("id", assessPlanRelaProcess.getId());
				Process process = assessPlanRelaProcess.getProcess();
				if(null != process){
					//末级流程
					row.put("processName", process.getName());
					if(null != process.getParent()){
						//流程分类
						row.put("parentProcessName", process.getParent().getName());
					}
				}
				//是否穿行测试
				if(null != assessPlanRelaProcess.getIsPracticeTest()){
					if(assessPlanRelaProcess.getIsPracticeTest()){
						row.put("isPracticeTest", "是");
					}else{
						row.put("isPracticeTest", "否");
					}
				}
				//穿行次数
				row.put("practiceNum", assessPlanRelaProcess.getPracticeNum());
				//是否抽样测试
				if(null != assessPlanRelaProcess.getIsSampleTest()){
					if(assessPlanRelaProcess.getIsSampleTest()){
						row.put("isSampleTest", "是");
					}else{
						row.put("isSampleTest", "否");
					}
				}
				DecimalFormat df = new DecimalFormat("0.00");
				if(null != assessPlanRelaProcess.getCoverageRate()){
					//抽样比例
					row.put("coverageRate", df.format(assessPlanRelaProcess.getCoverageRate()));
				}
				
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	
	/**
	 * 查询评价报告下缺陷认定结果表.
	 * @author 吴德福
	 * @param limit
	 * @param start
	 * @param sort
	 * @param query
	 * @param assessplanId 评价计划id
	 * @return Map<String,Object>
	 */
	@ResponseBody
    @RequestMapping("/comm/report/findReportDefectList.f")
    public Map<String,Object> findReportDefectList(int limit, int start, String sort, String query, String assessplanId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		Page<AssessRelaDefect> page = new Page<AssessRelaDefect>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessRelaDefectBO.findAssessRelaDefectListByPage(page, assessplanId, "", query);
		List<AssessRelaDefect> assessRelaDefectList = page.getResult();
		if(null != assessRelaDefectList && assessRelaDefectList.size()>0){
			Map<String, Object> row = null;
			for(AssessRelaDefect assessRelaDefect : assessRelaDefectList){
				row = new HashMap<String, Object>();
				//id
				row.put("id", assessRelaDefect.getId());
				//末级流程
				if(null != assessRelaDefect.getProcess()){
					row.put("processName", assessRelaDefect.getProcess().getName());
				}
				//评价点和实施证据
				if(null != assessRelaDefect.getAssessPoint()){
					row.put("assessPointName", assessRelaDefect.getAssessPoint().getDesc());
					row.put("comment", assessRelaDefect.getAssessPoint().getComment());
				}
				
				if(null != assessRelaDefect.getDefect()){
					Defect defect = assessRelaDefect.getDefect();
					//缺陷描述
					row.put("defectDesc", defect.getDesc());
					//缺陷类型
					if(null != defect.getType()){
						row.put("defectType", defect.getType().getName());
					}
					//缺陷级别
					if(null != defect.getLevel()){
						row.put("defectLevel", defect.getLevel().getName());
					}
					//整改责任部门
					Set<DefectRelaOrg> defectRelaOrgs = defect.getDefectRelaOrg();
					for (DefectRelaOrg defectRelaOrg : defectRelaOrgs) {
						if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType()) && null != defectRelaOrg.getOrg()){
							row.put("responsibilityOrg", defectRelaOrg.getOrg().getOrgname());
						}
					}
				}
				
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	
	/**
	 * 查询评价报告下缺陷整改情况表.
	 * @param assessplanId
	 * @return Map<String,Object>
	 */
	@ResponseBody
    @RequestMapping("/comm/report/findReportDefectImproveInfoList.f")
    public Map<String,Object> findReportDefectImproveInfoList(String assessplanId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		List<Object[]> list = o_improvePlanRelaDefectBO.findDefectImproveInfoByAssessplanId(assessplanId);
		if(null != list && list.size()>0){
			Map<String, Object> row = null;
			for (Object[] objects : list) {
				row = new HashMap<String, Object>();
				//id
				row.put("id", String.valueOf(objects[4]));
				//缺陷描述
				String defectDealStatus = "";
				if(null != objects[0]){
					row.put("defectDesc", String.valueOf(objects[0]));
				}
				//缺陷整改方案
				if(null != objects[1]){
					row.put("improvePlanContent", String.valueOf(objects[1]));
				}
				//缺陷整改复核测试情况
				if(null != objects[2]){
					row.put("compensationControl", String.valueOf(objects[2]));
				}
				//缺陷级别
				String defectLevel = "";
				if(null != objects[5]){
					defectLevel = String.valueOf(objects[5]);
					if(Contents.DEFECT_LEVEL_GREAT.equals(defectLevel)){
						defectLevel = "重大缺陷";
					}else if(Contents.DEFECT_LEVEL_IMPORTANT.equals(defectLevel)){
						defectLevel = "重要缺陷";
					}else if(Contents.DEFECT_LEVEL_GENERAL.equals(defectLevel)){
						defectLevel = "一般缺陷";
					}else if(Contents.DEFECT_LEVEL_EXCEPTION.equals(defectLevel)){
						defectLevel = "例外事项";
					}
					row.put("defectLevel", defectLevel);
				}
				//缺陷状态
				if(null != objects[3]){
					defectDealStatus = String.valueOf(objects[3]);
					if(Contents.DEAL_STATUS_NOTSTART.equals(defectDealStatus)){
						defectDealStatus = "未开始";
					}else if(Contents.DEAL_STATUS_HANDLING.equals(defectDealStatus)){
						defectDealStatus = "处理中";
					}else if(Contents.DEAL_STATUS_FINISHED.equals(defectDealStatus)){
						defectDealStatus = "已完成";
					}else if(Contents.DEAL_STATUS_AFTER_DEADLINE.equals(defectDealStatus)){
						defectDealStatus = "逾期";
					}
					row.put("defectDealStatus", defectDealStatus);
				}
				
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", list.size());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
}