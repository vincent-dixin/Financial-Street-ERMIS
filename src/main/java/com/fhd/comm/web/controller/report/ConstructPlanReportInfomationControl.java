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
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.report.ConstrcutPlanReportInfomationBO;
import com.fhd.comm.business.report.ReportInfomationBO;
import com.fhd.comm.entity.report.ReportInfomation;
import com.fhd.comm.entity.report.ReportRelaAssessment;
import com.fhd.comm.entity.report.ReportRelaConstructPlan;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessPlanRelaProcessBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.icsystem.ConstructPlanBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.process.entity.Process;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 内控报告Control.
 * @author 宋佳
 * @since 2013-03-05 am 11:03
 */
@Controller
public class ConstructPlanReportInfomationControl {

	@Autowired
	private ReportInfomationBO o_reportInfomationBO;
	@Autowired
	private ConstrcutPlanReportInfomationBO o_constrcutPlanReportInfomationBO;
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	/**
	 * 保存内控手册
	 * @author 宋佳
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
	@RequestMapping("/comm/report/saveconstructplantestreport.f")
	public Map<String,Object> saveConstructPlanTestReport(String id, String reportName, String reportCode, String reportDataText, HttpServletRequest request) throws Exception{
		
		ReportInfomation report = null;
		
		if(StringUtils.isNotEmpty(id)){
			report = o_reportInfomationBO.findReportById(id);
		}else {
			report = new ReportInfomation();
			report.setId(Identities.uuid());
			report.setReportType(new DictEntry(Contents.CONSTRUCT_REPORT_TYPE_TEST));
			double versionCode = o_constrcutPlanReportInfomationBO.findVersionCodeByPlanId(Contents.CONSTRUCT_REPORT_TYPE_TEST);
			report.setReportCode(String.valueOf((int)versionCode+1.0));
		}
		if(StringUtils.isNotBlank(reportName)){
			report.setReportName(reportName);
		}
		if(StringUtils.isNotBlank(reportCode)){
			report.setReportCode(reportCode);
		}
		
		o_constrcutPlanReportInfomationBO.saveCompanyYearReport(report, reportDataText, request);
		
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
	@RequestMapping("/comm/report/findconstructionplantestreportbyid.f")
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
			
			Set<ReportRelaConstructPlan> reportRelaAssessPlans = reportInfomation.getReportRelaConstructPlans();
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<HashMap<String, Object>> reportRelaConstructlist = new ArrayList<HashMap<String, Object>>();
			
			for (ReportRelaConstructPlan reportRelaConstructPlan : reportRelaAssessPlans) {
				list.add(reportRelaConstructPlan.getConstructPlan().getId());
				HashMap<String, Object> reportRelaAssessJsonMap = new HashMap<String, Object>();
				reportRelaAssessJsonMap.put("id", reportRelaConstructPlan.getConstructPlan().getId());
				reportRelaAssessJsonMap.put("name", reportRelaConstructPlan.getConstructPlan().getName());
				reportRelaAssessJsonMap.put("code", reportRelaConstructPlan.getConstructPlan().getCode());
				reportRelaConstructlist.add(reportRelaAssessJsonMap);
			}
			row.put("reportRelaConstructlist", reportRelaConstructlist);
			row.put("constructplanId", StringUtils.join(list, ","));
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
//	@RequestMapping("/comm/report/saveconstructplancompanyyearreportsubmit.f")
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
	 * 根据id集合批量删除内控体系建设报告.
	 * @param ids 对象id集合
	 * @param response
	 * @throws IOException 
	 */
    @RequestMapping("/comm/report/removereportinfomationbyidsforconstruct.f")
    public void removeReportInfomationByIdsForConstruct(String ids, HttpServletResponse response) throws IOException{
    	PrintWriter out = response.getWriter();
    	try {
    		if(StringUtils.isNotBlank(ids)){
    			o_constrcutPlanReportInfomationBO.removeReportByIds(ids);
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
//    @RequestMapping("/comm/report/findReportInfomationList.f")
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
	 * 下载报告.
	 * @author 胡迪新
	 * @param id
	 * @param os
	 * @param response
	 * @throws Exception
	 */
//	@RequestMapping("/comm/report/downloadReport.f")
	public void downloadReport(String id, OutputStream os, HttpServletResponse response) throws Exception {
		ReportInfomation reportInfomation = o_reportInfomationBO.findReportById(id);
		
		response.setContentType("doc");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ URLEncoder.encode(reportInfomation.getReportName()+".doc", "UTF-8"));
		
		IOUtils.write(reportInfomation.getReportDoc(), os);
	}
}