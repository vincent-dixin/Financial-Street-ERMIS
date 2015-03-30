package com.fhd.comm.business.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.comm.dao.report.ReportInfomationDAO;
import com.fhd.comm.entity.report.ReportInfomation;
import com.fhd.comm.entity.report.ReportRelaAssessment;
import com.fhd.comm.utils.SaxParseXml;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessPlanBO;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.assess.AssessResultBO;
import com.fhd.icm.business.bpm.AssessPlanBpmBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.assess.AssessResult;
import com.fhd.icm.entity.control.MeasureRelaRisk;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPointRelaRisk;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 内控报告BO.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Service
@SuppressWarnings({"unchecked","unused"})
public class ReportInfomationBO {
	
	private static String HEADER_TD_START = "<td style=\"background:#0070c0;\" valign=\"top\"><p><span style=\"font-family:宋体;background:#0070c0;font-size:12pt;\">";
	private static String HEADER_TD_END = "</span></p></td>";
	private static String BODY_TD_START = "<td valign=\"top\"><p><span style=\"font-family:宋体;font-size:12pt;\">";
	private static String BODY_TD_END = "</span></p></td>";
	
	@Autowired
	private ReportInfomationDAO o_reportInfomationDAO;
	@Autowired
	private ReportRelaAssessmentBO o_reportRelaAssessmentBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessResultBO o_assessResultBO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	@Autowired
	private AssessPlanBpmBO o_assessPlanBpmBO;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	
	/**
	 * <pre>
	 * 保存公司年度评价报告
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param report
	 * @param reportData
	 * @param assessplanId 
	 * @param request
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveCompanyYearReport(ReportInfomation report, String reportData, String assessplanId, HttpServletRequest request) throws Exception {
		
		String newReportData = "";
		
		if(Contents.ASSESSMENT_REPORT_TYPE_COMPANY.equals(report.getReportType().getId())){
			//公司年度评价报告
			newReportData = this.replaceCompanyYearReportByBusinessDatas(reportData, assessplanId);
		}else if(Contents.ASSESSMENT_REPORT_TYPE_TEST.equals(report.getReportType().getId())){
			if(StringUtils.isBlank(reportData)){
				//axis读取xml
				reportData = SaxParseXml.findReportContentsByXml(request);
			}
			//测试报告
			newReportData = this.replaceTestReportByBusinessDatas(reportData, assessplanId);
		}else if(Contents.ASSESSMENT_REPORT_TYPE_GROUP.equals(report.getReportType().getId())){
			//集团年度评价报告
			newReportData = this.replaceGroupYearReportByBusinessDatas(reportData, assessplanId);
		}
		
		String content = "<html>" + newReportData + "</html>"; 
		
        byte b[] = content.getBytes("GBK");  
        ByteArrayInputStream bais = new ByteArrayInputStream(b);  
        POIFSFileSystem poifs = new POIFSFileSystem();  
        DirectoryEntry directory = poifs.getRoot();  
        directory.createDocument("WordDocument", bais);  
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        poifs.writeFilesystem(outputStream);
		if("test_report".equals(report.getReportType().getId())){
			report.setReportData(reportData.getBytes("GBK"));
		}else{
			report.setReportData(reportData.getBytes("GBK"));
		}
        report.setReportDoc(outputStream.toByteArray());
        report.setStatus(Contents.STATUS_SAVED);
        report.setExecuteStatus(Contents.DEAL_STATUS_NOTSTART);
        report.setCompany(new SysOrganization(UserContext.getUser().getCompanyid()));
		
		o_reportInfomationDAO.merge(report);
		
		o_reportRelaAssessmentBO.removeReportRelaAssessmentByReportId(report.getId());
		
		String[] assessplanIds = StringUtils.split(assessplanId, ",");
		for (String id : assessplanIds) {
			ReportRelaAssessment reportRelaAssessment = new ReportRelaAssessment();
			reportRelaAssessment.setId(Identities.uuid());
			reportRelaAssessment.setReport(report);
			reportRelaAssessment.setAssessPlan(new AssessPlan(id));
			
			o_reportRelaAssessmentBO.mergeReportRelaAssessment(reportRelaAssessment);
		}
	}

	/**
	 * <pre>
	 * 保存并提交报告
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param report
	 * @param reportData
	 * @param assessplanId
	 * @param request
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveCompanyYearReportSubmit(ReportInfomation report,
			String reportData, String assessplanId,String opinion,
			String processInstanceId, String transition, HttpServletRequest request) throws Exception {
		
		saveCompanyYearReport(report, reportData, assessplanId, request);
		
		report.setStatus(Contents.STATUS_SUBMITTED);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("id", report.getId());
		variables.put("name", report.getReportName());
		
		// 工作流提交
		if(StringUtils.isEmpty(processInstanceId)) {
			// 编制人  
			variables.put("compiler", UserContext.getUser().getEmpid());
			// TODO 审批人未设置 - 胡迪新
			variables.put("approver", UserContext.getUser().getEmpid());
			
			String newProcessInstanceId = o_jbpmBO.startProcessInstance("reportExamineApprove", variables);
			o_jbpmBO.doProcessInstance(newProcessInstanceId, variables);
		} else {
			variables.put("isReject", transition);
			
			if(StringUtils.isNotEmpty(opinion)){
				variables.put("path", transition);
				variables.put("examineApproveIdea", opinion);
			}
			
			o_jbpmBO.doProcessInstance(processInstanceId, variables);	
		}
	}
	
	/**
	 * 公司年度评价报告替换业务数据.
	 * @param reportData 公司年度评价报告模板
	 * @param assessplanIds 公司年度评价报告相关的评价计划id集合
	 * @return String 公司年度评价报告
	 */
	public String replaceCompanyYearReportByBusinessDatas(String reportData, String assessplanIds){
		//替换评价计划相关数据
		String newReportData = createDefects(reportData, assessplanIds);
		//替换整改计划相关数据
		newReportData = createRectify(newReportData, assessplanIds);
		
		return newReportData;
	}
	
	/**
	 * 集团年度评价报告替换业务数据.
	 * @param reportData 集团年度评价报告模板
	 * @param assessplanIds 集团年度评价报告相关的评价计划id集合
	 * @return String 集团年度评价报告
	 */
	public String replaceGroupYearReportByBusinessDatas(String reportData, String assessplanIds){
		//替换评价计划相关数据
		String newReportData = createDefects(reportData, assessplanIds);
		//替换整改计划相关数据
		newReportData = createRectify(newReportData, assessplanIds);
		
		return newReportData;
	}
	
	/**
	 * 测试报告替换业务数据.
	 * @param reportData 测试报告模板
	 * @param assessplanId 测试报告相关的评价计划id集合
	 * @return String 测试报告
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws FileNotFoundException 
	 */
	public String replaceTestReportByBusinessDatas(String reportData, String assessplanId) throws FileNotFoundException, JDOMException, IOException{
		/*
		 * 替换评价计划相关数据
		 * 1.替换评价计划基本信息数据
		 * 2.替换评价计划范围列表
		 * 3.替换评价计划结果列表:穿行测试和抽样测试
		 * 4.替换评价计划缺陷认定列表
		 */
		
		String newReportData = createAssessPlanBaseInfo(reportData, assessplanId);
		newReportData = createAssessRelaProcessList(newReportData, assessplanId);
		//newReportData = createAssessResultList(newReportData, assessplanId);
		newReportData = createDefects(newReportData, assessplanId);
		return newReportData;
	}
	
	/**
	 * 生成评价计划基本信息表.
	 * @param reportData
	 * @param assessPlanId
	 * @return String
	 */
	private String createAssessPlanBaseInfo(String reportData, String assessPlanId){
		String newReportData = reportData;
		if(StringUtils.isNotBlank(assessPlanId)){
			AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
			if(null != assessPlan){
				newReportData = StringUtils.replace(newReportData, "${评价目标}", assessPlan.getAssessTarget());
				
				List<String> responsibilityEmp = new ArrayList<String>();
				List<String> handlerEmp = new ArrayList<String>();
				Set<AssessPlanRelaOrgEmp> assessPlanRelaOrgEmpSet = assessPlan.getAssessPlanRelaOrgEmp();
				for (AssessPlanRelaOrgEmp assessPlanRelaOrgEmp : assessPlanRelaOrgEmpSet) {
					if (Contents.EMP_RESPONSIBILITY.equals(assessPlanRelaOrgEmp.getType()) && null != assessPlanRelaOrgEmp.getEmp()) {
						responsibilityEmp.add(assessPlanRelaOrgEmp.getEmp().getEmpname());
					} else if (Contents.EMP_HANDLER.equals(assessPlanRelaOrgEmp.getType()) && null != assessPlanRelaOrgEmp.getEmp()) {
						handlerEmp.add(assessPlanRelaOrgEmp.getEmp().getEmpname());
					}
				}
				newReportData = StringUtils.replace(newReportData, "${组长}", StringUtils.join(responsibilityEmp, "、"));
				newReportData = StringUtils.replace(newReportData, "${组员}", StringUtils.join(handlerEmp, "、"));
				
				newReportData = StringUtils.replace(newReportData, "${评价依据}", assessPlan.getRequirement());
				String targetDateRange = "";
				if(null != assessPlan.getTargetStartDate()){
					targetDateRange = DateUtils.formatDate(assessPlan.getTargetStartDate(), "yyyy-MM-dd");
				}
				if(null != assessPlan.getTargetEndDate()){
					if(!"".equals(targetDateRange)){
						targetDateRange += "~";
					}
					targetDateRange += DateUtils.formatDate(assessPlan.getTargetEndDate(), "yyyy-MM-dd");
				}
				newReportData = StringUtils.replace(newReportData, "${样本期间}", targetDateRange);
				String planDateRange = "";
				if(null != assessPlan.getPlanStartDate()){
					planDateRange = DateUtils.formatDate(assessPlan.getPlanStartDate(), "yyyy-MM-dd");
				}
				if(null != assessPlan.getPlanEndDate()){
					if(!"".equals(planDateRange)){
						planDateRange += "~";
					}
					planDateRange += DateUtils.formatDate(assessPlan.getPlanEndDate(), "yyyy-MM-dd");
				}
				newReportData = StringUtils.replace(newReportData, "${计划期间}", planDateRange);
				String companyName = "";
				if(null != assessPlan.getCompany()){
					companyName = assessPlan.getCompany().getOrgname();
				}
				newReportData = StringUtils.replace(newReportData, "${公司名称}", companyName);
				String orgName = "";
				if(null != assessPlan.getCompany()){
					String empId = o_assessPlanBpmBO.findAssessPlanEmpIdByRole("ICDepartmentStaff");
					SysOrganization department = o_empolyeeBO.getDepartmentByEmpId(empId);
					if(null != department){
						orgName = department.getOrgname();
					}
				}
				newReportData = StringUtils.replace(newReportData, "${内控主责部门}", orgName);
				if(null != assessPlan.getAssessMeasure()){
					if(Contents.ASSESS_MEASURE_ALL_TEST.equals(assessPlan.getAssessMeasure().getId())){
						newReportData = StringUtils.replace(newReportData, "${评价测试方法名称}", "穿行测试和抽样测试");
					}else if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
						newReportData = StringUtils.replace(newReportData, "${评价测试方法名称}", "穿行测试");
					}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){
						newReportData = StringUtils.replace(newReportData, "${评价测试方法名称}", "抽样测试");
					}
				}
			}
		}
		return newReportData;
	}
	
	/**
	 * 生成评价范围列表.
	 * @author 吴德福
	 * @param reportData
	 * @param assessPlanId 评价计划id
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String createAssessRelaProcessList(String reportData, String assessPlanId) {
		String newReportData = reportData;
		List<String> responsibilityOrg = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
			.append("<tr><td colspan=\"12\"><p style=\"text-align:center;\" align=\"center\"><b><span style=\"font-size:12pt;font-family:宋体;color:black;\">内部控制评价范围列表</span></b></p></td></tr>")
			.append("<tr>")
				.append(HEADER_TD_START)
				.append("序号")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("流程分类")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("末级流程")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("是否穿行测试")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("穿行次数")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("是否抽样测试")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("抽样比例(%)")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("评价人")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("评价日期")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("复核人")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("复核日期")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("结果分析")
				.append(HEADER_TD_END)
			.append("</tr>");
		
		if(StringUtils.isNotBlank(assessPlanId)){
			AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
			if(null != assessPlan){
				//序号
				int rownum=1;
				
				Set<AssessPlanRelaProcess> assessPlanRelaProcessSet = assessPlan.getAssessPlanRelaProcess();
				for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessSet) {
					//流程分类
					String parentProcessName = "";
					//末级流程
					String processName = "";
					//是否穿行测试
					String isPracticeTest = "";
					//穿行次数
					int practiceNum = 0;
					//是否抽样测试
					String isSampleTest = "";
					//抽样比例
					String coverageRate = "";
					//评价人
					String executeEmpName = "";
					//评价日期
					String assessDate = "";
					//复核人
					String reviewerEmpName = "";
					//复核日期
					String reviewDate = "";
					//结果分析
					String resultAnalysis= "";
					
					Process process = assessPlanRelaProcess.getProcess();
					if(null != process){
						processName = process.getName();
						if(null != process.getParent()){
							parentProcessName = process.getParent().getName();
						}
						Set<ProcessRelaOrg> processRelaOrgs = process.getProcessRelaOrg();
						for (ProcessRelaOrg processRelaOrg : processRelaOrgs) {
							if (Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
								responsibilityOrg.add(processRelaOrg.getOrg().getOrgname());
							}
						}
					}
					if(null != assessPlanRelaProcess.getIsPracticeTest()){
						if(assessPlanRelaProcess.getIsPracticeTest()){
							isPracticeTest = "是";
						}else{
							isPracticeTest = "否";
						}
					}
					practiceNum = assessPlanRelaProcess.getPracticeNum();
					if(null != assessPlanRelaProcess.getIsSampleTest()){
						if(assessPlanRelaProcess.getIsSampleTest()){
							isSampleTest = "是";
						}else{
							isSampleTest = "否";
						}
					}
					DecimalFormat df = new DecimalFormat("0.00");
					if(null != assessPlanRelaProcess.getCoverageRate()){
						coverageRate = df.format(assessPlanRelaProcess.getCoverageRate());
					}
					Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmps = assessPlanRelaProcess.getAssessPlanProcessRelaOrgEmp();
					for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmps) {
						if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType())){
							//评价人
							executeEmpName = assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname();
						}else if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType())){
							//复核人
							reviewerEmpName = assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname();
						}
					}
					if(null != assessPlanRelaProcess.getAssessDate()){
						assessDate = DateUtils.formatDate(assessPlanRelaProcess.getAssessDate(), "yyyy-MM-dd");
					}
					if(null != assessPlanRelaProcess.getReviewDate()){
						reviewDate = DateUtils.formatDate(assessPlanRelaProcess.getReviewDate(), "yyyy-MM-dd");
					}
					if(StringUtils.isNotBlank(assessPlanRelaProcess.getDesc())){
						resultAnalysis = assessPlanRelaProcess.getDesc();
					}
					
					builder.append("<tr>")
						.append(BODY_TD_START)
							.append(rownum)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(parentProcessName)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(processName)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(isPracticeTest)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(practiceNum)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(isSampleTest)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(coverageRate)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(executeEmpName)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(assessDate)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(reviewerEmpName)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(reviewDate)
						.append(BODY_TD_END)
						.append(BODY_TD_START)
							.append(resultAnalysis)
						.append(BODY_TD_END)
						.append("</tr>");
						
						rownum++;
				}
			}
		}
		
		builder.append("</tbody></table>");
		
		newReportData = StringUtils.replace(newReportData, "${评价范围列表}", builder.toString());
		newReportData = StringUtils.replace(newReportData, "${流程的责任部门}", StringUtils.join(responsibilityOrg, "、"));
		
		return newReportData;
	}
	
	/**
	 * 生成评价结果列表--全部/穿行/抽样.
	 * @author 吴德福
	 * @param reportData
	 * @param assessPlanId 评价计划id
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String createAssessResultList(String reportData, String assessPlanId) {
		StringBuilder practiceTestBuilder = new StringBuilder();
		StringBuilder sampleTestBuilder = new StringBuilder();
		
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
		if(null != assessPlan){
			List<AssessPlanRelaProcess> assessPlanRelaProcessList = new ArrayList<AssessPlanRelaProcess>();
			Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcesses) {
				if(!assessPlanRelaProcessList.contains(assessPlanRelaProcess)){
					assessPlanRelaProcessList.add(assessPlanRelaProcess);
				}
			}
			
			List<AssessResult> assessResultList = o_assessResultBO.findAssessResultByAssessPlanIdAndProcessId(assessPlanId, "");
			
			practiceTestBuilder.append("<table  cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"0\">");
			practiceTestBuilder.append("<tbody>")
				.append("<tr><td><p style=\"text-align:center;\" align=\"center\"><b><span style=\"font-size:12pt;font-family:宋体;color:black;\">内部控制缺陷认定结果汇总表</span></b></p></td></tr>")
				.append("<tr>")
					.append(HEADER_TD_START)
					.append("序号")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("流程分类")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("末级流程")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("测试结果")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("流程节点数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价人")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价日期")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("复核人")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("复核日期")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价点数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价点通过率")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("样本数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("样本合格率")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("缺陷数")
					.append(HEADER_TD_END)
				.append("</tr>");
			
			sampleTestBuilder.append("<table  cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"0\">");
			sampleTestBuilder.append("<tbody>")
				.append("<tr><td width=\"100%\" ><p style=\"text-align:center;\" align=\"center\"><b><span style=\"font-size:12pt;font-family:宋体;color:black;\">内部控制缺陷认定结果汇总表<span></span></span></b></p></td></tr>")
				.append("<tr>")
					.append(HEADER_TD_START)
					.append("序号")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("流程分类")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("末级流程")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("控制措施数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价人")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价日期")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("复核人")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("复核日期")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价点数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("评价点通过率")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("样本数")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("样本合格率")
					.append(HEADER_TD_END)
					.append(HEADER_TD_START)
					.append("缺陷数")
					.append(HEADER_TD_END)
				.append("</tr>");
			
			//序号
			int rownum=1;
			
			for(AssessPlanRelaProcess assessPlanRelaProcessTemp : assessPlanRelaProcessList){
				//流程分类
				String parentProcessName = "";
				//末级流程
				String processName = "";
				//测试结果
				String testResult = "";
				//流程节点数
				int processPointNO = 0;
				//控制措施数
				int assessMeasureNO = 0;
				//评价人
				String executeEmpName = "";
				//评价日期
				String assessDate = "";
				//复核人
				String reviewerEmpName = "";
				//复核日期
				String reviewDate = "";
				//穿行的评价点总数
				int allNumByPracticeTest=0;
				//穿行评价点通过率
				String qualifiedRateByPracticeTest= "0.00";
				//抽样的评价点总数
				int allNumBySampleTest=0;
				//抽样评价点通过率
				String qualifiedRateBySampleTest="0.00";
				//穿行样本总数
				int practiceTestSampleNum=0;
				//穿行样本合格率
				String qualifiedPracticeTestSample="0.00";
				//抽样样本总数
				int sampleTestSampleNum=0;
				//抽样样本合格率
				String qualifiedSampleTestSample="0.00";
				//穿行测试缺陷数
				int practiceHasDefectNum=0;
				//抽样测试缺陷数
				int sampleHasDefectNum=0;
				
				Process process = assessPlanRelaProcessTemp.getProcess();
				//流程分类名称
				if (null != process.getParent()) {
					parentProcessName = process.getParent().getName();
				}
				//末级流程
				processName = process.getName();
				
				Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmps = assessPlanRelaProcessTemp.getAssessPlanProcessRelaOrgEmp();
				for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmps) {
					if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType())){
						//评价人
						executeEmpName = assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname();
					}else if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType())){
						//复核人
						reviewerEmpName = assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname();
					}
				}
				
				//评价日期
				if(null != assessPlanRelaProcessTemp.getAssessDate()){
					assessDate = DateUtils.formatDate(assessPlanRelaProcessTemp.getAssessDate(), "yyyy-MM-dd");
				}
				//复核日期
				if(null != assessPlanRelaProcessTemp.getReviewDate()){
					reviewDate = DateUtils.formatDate(assessPlanRelaProcessTemp.getReviewDate(), "yyyy-MM-dd");
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
				//合格的抽样评价点数
				int qualifiedNumBySampleTest=0;
				
				//合格的穿行样本数
				int qualifiedPracticeTestSampleNum=0;
				//合格的抽样样本数
				int qualifiedSampleTestSampleNum=0;
				
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
					testResult = Contents.AssessResult_STATUS_N;
				}else if(qualifiedNumByPracticeTest==allNumByPracticeTest){
					//完全符合
					testResult = Contents.AssessResult_STATUS_OK;
				}else if(practiceHasDefectNum+qualifiedNumByPracticeTest == allNumByPracticeTest){
					//部分符合
					testResult = Contents.AssessResult_STATUS_YORN;
				}
				
				//流程节点数
				processPointNO = processPointIdSet.size();
				//控制措施数
				assessMeasureNO = assessMeasureIdSet.size();
				
				if(allNumByPracticeTest!=0){
					qualifiedRateByPracticeTest= df.format((double)qualifiedNumByPracticeTest/allNumByPracticeTest);
				}
				if(allNumBySampleTest!=0){
					qualifiedRateBySampleTest=df.format((double)qualifiedNumBySampleTest/allNumBySampleTest);
				}
				
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
				
				practiceTestBuilder.append("<tr>")
					.append(BODY_TD_START)
						.append(rownum)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(parentProcessName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(processName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(testResult)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(processPointNO)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(executeEmpName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(assessDate)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(reviewerEmpName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(reviewDate)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(allNumByPracticeTest)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(qualifiedRateByPracticeTest)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(practiceTestSampleNum)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(qualifiedPracticeTestSample)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(practiceHasDefectNum)
					.append(BODY_TD_END)
					.append("</tr>");
				
				sampleTestBuilder.append("<tr>")
					.append(BODY_TD_START)
						.append(rownum)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(parentProcessName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(processName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(assessMeasureNO )
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(executeEmpName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(assessDate)
					.append(BODY_TD_END).append(BODY_TD_START)
						.append(reviewerEmpName)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(reviewDate)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(allNumBySampleTest)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(qualifiedRateBySampleTest)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(sampleTestSampleNum)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(qualifiedSampleTestSample)
					.append(BODY_TD_END)
					.append(BODY_TD_START)
						.append(sampleHasDefectNum)
					.append(BODY_TD_END)
					.append("</tr>");
				
				rownum++;
			}
			
			practiceTestBuilder.append("</tbody></table>");
			sampleTestBuilder.append("</tbody></table>");
		}
		
		String newReportData = reportData;
		if(null != assessPlan){
			if(null != assessPlan.getAssessMeasure()){
				if(Contents.ASSESS_MEASURE_ALL_TEST.equals(assessPlan.getAssessMeasure().getId())){
					newReportData = StringUtils.replace(newReportData, "${穿行测试结果列表}", practiceTestBuilder.toString());
					newReportData = StringUtils.replace(newReportData, "${抽样测试结果列表}", sampleTestBuilder.toString());
				}else if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
					newReportData = StringUtils.replace(newReportData, "${穿行测试结果列表}", practiceTestBuilder.toString());
				}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){
					newReportData = StringUtils.replace(newReportData, "${抽样测试结果列表}", sampleTestBuilder.toString());
				}
			}
		}
		return newReportData;
	}
	
	/**
	 * <pre>
	 * 生成缺陷认定结果表.
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param reportData
	 * @param assessplanIds 评价计划id集合
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String createDefects(String reportData, String assessplanIds) {
		List<AssessRelaDefect> defectRelaInfoList = o_assessRelaDefectBO.findDefectRelaInfoByAssessPlanIds(assessplanIds);
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
			.append("<tr><td colspan=\"8\"><p style=\"text-align:center;\" align=\"center\"><b><span style=\"font-size:12pt;font-family:宋体;color:black;\">内部控制缺陷认定结果汇总表</span></b></p></td></tr>")
			.append("<tr>")
				.append(HEADER_TD_START)
				.append("序号")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷描述")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷类型")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷级别")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("评价点")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("流程节点名称")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("控制措施名称")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("风险事件编号")
				.append(HEADER_TD_END)
			.append("</tr>");
		
		//序号
		int rownum=1;
		//缺陷总数
		int defectCount = defectRelaInfoList.size();
		//重大缺陷数量
		int greatDefectCount = 0;
		//重要缺陷数量
		int importantDefectCount = 0;
		
		for (AssessRelaDefect assessRelaDefect : defectRelaInfoList) {
			//缺陷描述
			String defectDesc = "";
			//缺陷类型
			String defectType = "";
			//缺陷级别
			String defectLevel = "";
			//评价点
			String assessPointName = "";
			//流程节点名称
			String processPointName = "";
			//控制措施名称
			String controlMeasureName = "";
			//风险事件编号
			String riskNames = "";
			
			if(null != assessRelaDefect.getDefect()){
				Defect defect = assessRelaDefect.getDefect();
				//缺陷描述
				defectDesc = defect.getDesc();
				//缺陷类型
				if(null != defect.getType()){
					defectType = defect.getType().getName();
				}
				//缺陷级别
				if(null != defect.getLevel()){
					defectLevel = defect.getLevel().getName();
					if(Contents.DEFECT_LEVEL_GREAT.equals(defect.getLevel().getId())){
						greatDefectCount++;
					}else if(Contents.DEFECT_LEVEL_IMPORTANT.equals(defect.getLevel().getId())){
						importantDefectCount++;
					}
				}
			}
			if(null != assessRelaDefect.getAssessPoint()){
				//评价点
				assessPointName = assessRelaDefect.getAssessPoint().getDesc();
			}
			if(null != assessRelaDefect.getProcessPoint()){
				//流程节点名称
				processPointName = assessRelaDefect.getProcessPoint().getName();
				Set<ProcessPointRelaRisk> processPointRelaRisks = assessRelaDefect.getProcessPoint().getProcessPointRelaRisks();
				int i=0;
				for (ProcessPointRelaRisk processPointRelaRisk : processPointRelaRisks) {
					//风险事件编号
					if(null != processPointRelaRisk.getRisk()){
						riskNames += processPointRelaRisk.getRisk().getCode();
					}
					if(i!=processPointRelaRisks.size()-1){
						riskNames += ",";
					}
					i++;
				}
			}else if(null != assessRelaDefect.getControlMeasure()){//控制措施名称
				controlMeasureName = assessRelaDefect.getControlMeasure().getName();
				Set<MeasureRelaRisk> measureRelaRisks = assessRelaDefect.getControlMeasure().getMeasureRelaRisks();
				int j=0;
				for (MeasureRelaRisk measureRelaRisk : measureRelaRisks) {
					//风险事件编号
					if(null != measureRelaRisk.getRisk()){
						riskNames += measureRelaRisk.getRisk().getCode();
					}
					if(j!=measureRelaRisks.size()-1){
						riskNames += ",";
					}
					j++;
				}
			}
			
			builder.append("<tr>")
				.append(BODY_TD_START)
					.append(rownum)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectDesc)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectType)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectLevel)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(assessPointName)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(processPointName)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(controlMeasureName)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(riskNames)
				.append(BODY_TD_END)
			.append("</tr>");
			
			rownum++;
		}
		
		builder.append("</tbody></table>");
		
		String newReportData = reportData;
		
		newReportData = StringUtils.replace(newReportData, "${缺陷总数}", String.valueOf(defectCount));
		newReportData = StringUtils.replace(newReportData, "${重大缺陷数量}", String.valueOf(greatDefectCount));
		newReportData = StringUtils.replace(newReportData, "${重要缺陷数量}", String.valueOf(importantDefectCount));
		newReportData = StringUtils.replace(newReportData, "${缺陷认定结果表}", builder.toString());
		return newReportData;
	}
	
	/**
	 * <pre>
	 * 生成缺陷的整改情况表.
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param reportData
	 * @param assessplanIds
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String createRectify(String reportData, String assessplanIds) {
		List<Object[]> list = o_improvePlanRelaDefectBO.findDefectImproveInfoByAssessplanId(assessplanIds);
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
			.append("<tr><td colspan=\"6\"><p style=\"text-align:center;\" align=\"center\"><b><span style=\"font-size:12pt;font-family:宋体;color:black;\">内部控制缺陷整改情况表</span></b></p></td></tr>")
			.append("<tr>")
				.append(HEADER_TD_START)
				.append("序号")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷描述")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷整改方案")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷整改复核测试情况")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷级别")
				.append(HEADER_TD_END)
				.append(HEADER_TD_START)
				.append("缺陷状态")
				.append(HEADER_TD_END)
			.append("</tr>");
		
		//序号
		int rownum=1;
		//缺陷总数
		int defectCount = 0;
		//重大缺陷数量
		int greatDefectCount = 0;
		//重要缺陷数量
		int importantDefectCount = 0;
		
		for (Object[] objects : list) {
			//缺陷描述
			String defectDesc = "";
			//缺陷整改方案
			String improvePlanContent = "";
			//缺陷整改复核测试情况
			String compensationControl = "";
			//缺陷级别
			String defectLevel = "";
			//缺陷状态
			String defectDealStatus = "";
			
			if(null != objects[0]){
				defectDesc = String.valueOf(objects[0]);
			}
			if(null != objects[1]){
				improvePlanContent = String.valueOf(objects[1]);
			}
			if(null != objects[2]){
				compensationControl = String.valueOf(objects[2]);
			}
			if(null != objects[3]){
				defectDealStatus = String.valueOf(objects[3]);
				if(Contents.DEAL_STATUS_NOTSTART.equals(defectDealStatus)){
					defectDealStatus = "未开始";
					defectCount++;
				}else if(Contents.DEAL_STATUS_HANDLING.equals(defectDealStatus)){
					defectDealStatus = "处理中";
					defectCount++;
				}else if(Contents.DEAL_STATUS_FINISHED.equals(defectDealStatus)){
					defectDealStatus = "已完成";
				}else if(Contents.DEAL_STATUS_AFTER_DEADLINE.equals(defectDealStatus)){
					defectDealStatus = "逾期";
				}
			}
			//缺陷级别
			if(null != objects[5]){
				defectLevel = String.valueOf(objects[5]);
				if(Contents.DEFECT_LEVEL_GREAT.equals(defectLevel)){
					defectLevel = "重大缺陷";
					greatDefectCount++;
				}else if(Contents.DEFECT_LEVEL_IMPORTANT.equals(defectLevel)){
					defectLevel = "重要缺陷";
					importantDefectCount++;
				}else if(Contents.DEFECT_LEVEL_GENERAL.equals(defectLevel)){
					defectLevel = "一般缺陷";
				}else if(Contents.DEFECT_LEVEL_EXCEPTION.equals(defectLevel)){
					defectLevel = "例外事项";
				}
			}
					
			builder.append("<tr>")
				.append(BODY_TD_START)
					.append(rownum)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectDesc)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(improvePlanContent)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(compensationControl)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectLevel)
				.append(BODY_TD_END)
				.append(BODY_TD_START)
					.append(defectDealStatus)
				.append(BODY_TD_END)
			.append("</tr>");
			
			rownum++;
		}
		
		builder.append("</tbody></table>");
		
		
		String newReportData = reportData;
		
		newReportData = StringUtils.replace(newReportData, "${整改缺陷总数}", String.valueOf(defectCount));
		newReportData = StringUtils.replace(newReportData, "${整改重大缺陷数量}", String.valueOf(greatDefectCount));
		newReportData = StringUtils.replace(newReportData, "${整改重要缺陷数量}", String.valueOf(importantDefectCount));
		newReportData = StringUtils.replace(newReportData, "${缺陷整改情况表}", builder.toString());
		
		return newReportData;
	}
	
	/**
	 * 根据id集合批量删除评价报告.
	 * @param ids
	 */
	@Transactional
	public void removeReportByIds(String ids){
		//删除评价报告表
		o_reportInfomationDAO.createQuery("delete ReportInfomation where id in (:ids)")
			.setParameterList("ids", StringUtils.split(ids,",")).executeUpdate();
		//删除评价报告关联计划表
		o_reportRelaAssessmentBO.removeReportRelaAssessmentByReportIds(ids);
	}
	
	/**
	 * 根据id查询评价报告.
	 * @param id
	 * @return
	 */
	public ReportInfomation findReportById(String id){
		return o_reportInfomationDAO.get(id);
	}
	
	/**
	 * 根据查询条件分页查询评价报告.
	 * @param page
	 * @param sort
	 * @param dir, 
	 * @param query
	 * @return Page<AssessmentReport>
	 */
	public Page<ReportInfomation> findReportListBySome(Page<ReportInfomation> page, 
			String sort, String query, String reportType){
		DetachedCriteria dc = DetachedCriteria.forClass(ReportInfomation.class);
		dc.add(Restrictions.eq("reportType.id", reportType));
		if(StringUtils.isNotBlank(query)){
			dc.add(Restrictions.like("reportName", query, MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
		dc.addOrder(Order.asc("company.id"));
		dc.addOrder(Order.asc("reportType.id"));
		dc.addOrder(Order.asc("reportName"));
		dc.addOrder(Order.asc("reportCode"));
		return o_reportInfomationDAO.findPage(dc, page, false);
	}
	
	/**
	 * 根据计划id查询报告的版本号
	 * @param planId
	 * @return double
	 */
	public double findVersionCodeByPlanId(String planId, String reportType){
		double ret = 0.0;
		
		/*
		select i.report_code 
		from t_report_information i left join t_report_rela_assessment a on i.id=a.report_id 
		where a.assessment_plan_id='' and i.report_type='' 
		*/
		
		List<String> list = null;
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append("select i.report_code ")
			.append("from t_report_information i left join t_report_rela_assessment a on i.id=a.report_id ")
			.append("where 1=1 ");
		
		if(StringUtils.isNotBlank(planId)){
			sqlBuffer.append("and a.assessment_plan_id=:planId ");
		}
		
		if(StringUtils.isNotBlank(reportType)){
			sqlBuffer.append("and i.report_type=:reportType ");
		}
		
		SQLQuery sqlQuery = o_reportInfomationDAO.createSQLQuery(sqlBuffer.toString());
		if(StringUtils.isNotBlank(planId)){
			sqlQuery.setString("planId", planId);
		}
		if(StringUtils.isNotBlank(reportType)){
			sqlQuery.setString("reportType", reportType);
		}
		list = sqlQuery.list();
		for (String versionCode : list) {
			if(StringUtils.isNotBlank(versionCode)){
				ret = Double.valueOf(String.valueOf(versionCode));
			}
		}
		return ret;
	}
}
