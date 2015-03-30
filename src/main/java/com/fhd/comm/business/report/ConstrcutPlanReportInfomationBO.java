package com.fhd.comm.business.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Criteria;
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
import com.fhd.comm.utils.SaxParseXml;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.assess.AssessResultBO;
import com.fhd.icm.business.bpm.AssessPlanBpmBO;
import com.fhd.icm.business.icsystem.ConstructPlanBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.dao.assess.MeasureRelaRiskDAO;
import com.fhd.icm.dao.icsystem.ConstructRelaProcessDAO;
import com.fhd.icm.entity.control.MeasureRelaRisk;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.business.ProcessPointBO;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.dao.ProcessRelaFileDAO;
import com.fhd.process.dao.ProcessRelaRuleDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.process.entity.ProcessPointRelaOrg;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.process.entity.ProcessRelaRule;
import com.fhd.risk.entity.Risk;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 体系建设报告BO.
 * @author 宋佳
 * @since 2013-06-06 pm 13:40
 */
@Service
@SuppressWarnings({"unchecked","unused"})
public class ConstrcutPlanReportInfomationBO {
	
	private static String HEADER_TD_START = "<td style=\"background:#0070c0;\" valign=\"top\"><p><span style=\"font-family:宋体;background:#0070c0;font-size:12pt;\">";
	private static String HEADER_TD_END = "</span></p></td>";
	private static String BODY_TD_START = "<td valign=\"top\"><p><span style=\"font-family:宋体;font-size:12pt;\">";
	private static String BODY_TD_END = "</span></p></td>";
	private static String HEADER_TITLE0_START = "<p style=\"text-align:left;\" align=\"left\"><b><span style=\"font-size:18pt;font-family:'Arial','sans-serif';\">";
	private static String HEADER_TITLE0_END = "</span></b></p>";
	private static String HEADER_TITLE1_START = "<p style=\"text-align:left;\" align=\"left\"><b><span style=\"font-size:12pt;font-family:'Arial','sans-serif';\">";
	private static String HEADER_TITLE1_END = "</span></b></p>";
	private static String HEADER_TITLE2_START = "<p style=\"text-align:left;\" align=\"left\"><b><span style=\"font-size:12pt;font-family:'Arial','sans-serif';\">";
	private static String HEADER_TITLE2_END = "</span></b></p>";
	private static String HEADER_TITLE3_START = "<p style=\"text-align:left;\" align=\"left\"><b><span style=\"font-size:12pt;font-family:'Arial','sans-serif';\">";
	private static String HEADER_TITLE3_END = "</span></b></p>";
	private static String BODY_START = "<p style=\"text-align:left;text-indent:24pt;\" align=\"left\"><span style=\"font-size:12pt;font-family:宋体;\">";
	private static String BODY_END = "</span></p>";
	@Autowired
	private ReportInfomationDAO o_reportInfomationDAO;
	@Autowired
	private ReportRelaConstructPlanBO o_reportRelaConstructPlanBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
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
	@Autowired
	private ConstructRelaProcessDAO o_constructRelaProcessDAO;
	@Autowired
	private ProcessRelaFileDAO o_processRelaFileDAO;
	@Autowired
	private MeasureRelaRiskDAO o_measureRelaRiskDAO;
	@Autowired
	private ProcessRelaRuleDAO o_processRelaRuleDAO;
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private ProcessBO o_processBO;
	@Autowired
	private ProcessPointBO o_processPointBO;
	
	/**
	 * <pre>
	 * 保存公司年度评价报告
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param report
	 * @param reportData
	 * @param constructPlanId 
	 * @param request
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveCompanyYearReport(ReportInfomation report, String reportData, HttpServletRequest request) throws Exception {
		
		String newReportData = "";
		
		if(StringUtils.isBlank(reportData)){
				//axis读取xml
				reportData = SaxParseXml.findReportContentsByXml(request,"app\\view\\comm\\report\\icsystem\\ConstructTestReportTpl.xml");
			}
			//测试报告
		newReportData = this.replaceTestReportByBusinessDatas(reportData);
		
		String content = "<html>" + newReportData + "</html>"; 
		
        byte b[] = content.getBytes("GBK");  
        ByteArrayInputStream bais = new ByteArrayInputStream(b);  
        POIFSFileSystem poifs = new POIFSFileSystem();  
        DirectoryEntry directory = poifs.getRoot();  
        directory.createDocument("WordDocument", bais);  
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        poifs.writeFilesystem(outputStream);
		if("test_construct_report".equals(report.getReportType().getId())){
			report.setReportData(reportData.getBytes("GBK"));
		}else{
			report.setReportData(reportData.getBytes("GBK"));
		}
        report.setReportDoc(outputStream.toByteArray());
        report.setStatus(Contents.STATUS_SAVED);
        report.setExecuteStatus(Contents.DEAL_STATUS_NOTSTART);
        report.setCompany(new SysOrganization(UserContext.getUser().getCompanyid()));
		
		o_reportInfomationDAO.merge(report);
		
		o_reportRelaConstructPlanBO.removeReportRelaConstructPlanByReportId(report.getId());
		
//		String[] constructplanIds = StringUtils.split(constructPlanId, ",");
//		for (String id : constructplanIds) {
//			ReportRelaConstructPlan reportRelaConstructPlan = new ReportRelaConstructPlan();
//			reportRelaConstructPlan.setId(Identities.uuid());
//			reportRelaConstructPlan.setReport(report);
//			reportRelaConstructPlan.setConstructPlan(new ConstructPlan(id));
//			
//			o_reportRelaConstructPlanBO.mergeReportRelaConstructPlan(reportRelaConstructPlan);
//		}
	}
	/**
	 * 测试报告替换业务数据.
	 * @param reportData 测试报告模板
	 * @param constructplanId 测试报告相关的评价计划id集合
	 * @return String 测试报告
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws FileNotFoundException 
	 */
	public String replaceTestReportByBusinessDatas(String reportData) throws FileNotFoundException, JDOMException, IOException{
		/*
		 *  该换的都换了
		 */
		
		String newReportData = createConstructPlanBaseInfo(reportData);
		newReportData = createProcessList(newReportData);
		return newReportData;
	}
	
	/**
	 * 生成评价计划基本信息表.
	 * @param reportData
	 * @param constructPlanId
	 * @return String
	 */
	private String createConstructPlanBaseInfo(String reportData){
		String newReportData = reportData;
		String companyName = UserContext.getUser().getCompanyName();;
		String orgName = "";
		String empId = o_assessPlanBpmBO.findAssessPlanEmpIdByRole("ICDepartmentStaff");
		SysOrganization department = o_empolyeeBO.getDepartmentByEmpId(empId);
		if(null != department){
			orgName = department.getOrgname();
		}
		newReportData = StringUtils.replaceEach(newReportData, new String[]{"${公司名称}","${内控主责部门}","${内控归口部门}","${发布日期}"},new String[]{companyName,orgName,orgName,DateUtils.formatDate(new Date(), "yyyy-MM-dd")});
		return newReportData;
	}
	
	/**
	 * 生成流程列表
	 * @author 宋佳
	 * @param reportData
	 * @param constructPlanId 建设计划id
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String createProcessList(String reportData) {
		String newReportData = reportData;
		StringBuilder builder = new StringBuilder();
		builder.append(HEADER_TITLE0_START)
			.append("4.3 文件控制")
			.append(HEADER_TITLE0_END);
		List<Process> processList = findParentProcessResult();
		int title0 = 1;
		for(Process process : processList){
			int title1 = 1;
			int title2 = 1;
			int title3 = 1;
			int i = 0;
			builder.append(HEADER_TITLE1_START)
			.append("4.3."+title0+process.getName())
			.append(HEADER_TITLE1_END);
			builder = findResultByQuery(process,builder,title0,title1,title2,title3,i);
			title0++;
		}
		return reportData.replace("${控制文件}", builder.toString());
	}
	/**
	 * <pre>
	 * 	查询内控所有流程和流程节点和
	 * </pre>
	 * @author 宋佳
	 * @param id
	 * @param query
	 * @param processResult
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public StringBuilder findResultByQuery(Process process,StringBuilder sb,int title0,int title1,int title2,int title3,int i){
		// 加载一级目录
		Criteria criteria = o_processDAO.createCriteria();
		criteria.add(Restrictions.eq("parent.id", process.getId()));
		List<Process> processList=criteria.list();
		for(Process processVar:processList ){	
			if(!processVar.getIsLeaf()){
				if(i > 0){
					title1++;
				}
				sb.append(HEADER_TITLE1_START)
				.append("4.3."+title0+"."+title1+" "+processVar.getName())
				.append(HEADER_TITLE1_END);
				i++;
				findResultByQuery(processVar,sb,title0,title1,title2,title3,i);
			}else{
				//获取流程的责任部门和相关部门
				String[] orgs = new String[2];
				StringBuffer sbRules = new StringBuffer();
				orgs = o_processBO.getProcessOrg(processVar);
				sb.append(HEADER_TITLE3_START)
				.append("4.3."+title0+"."+title1+"."+title2+processVar.getName())
				.append(HEADER_TITLE3_END);
				// 流程目标、流程管理部门、流程涉及到的部门领导、规章制度及管理标准
				sb.append(HEADER_TITLE1_START)
				.append("1、流程目标:")
				.append(HEADER_TITLE1_END);
				sb.append(BODY_START);
				if(StringUtils.isBlank(processVar.getControlTarget())){
					sb.append("无");
				}else{
					sb.append(processVar.getControlTarget());
				}
				sb.append(BODY_END);
				sb.append(HEADER_TITLE1_START)
				.append("2、流程管理部门:")
				.append(HEADER_TITLE1_END);
				sb.append(BODY_START)
				.append(orgs[0])
				.append(BODY_END);
				sb.append(HEADER_TITLE1_START)
				.append("3、流程涉及部门及领导、流程目标: ")
				.append(HEADER_TITLE1_END);
				sb.append(BODY_START)
				.append(orgs[1])
				.append(BODY_END);
				sb.append(HEADER_TITLE1_START)
				.append("4、规章制度及管理标准:")
				.append(HEADER_TITLE1_END);
				List<ProcessRelaRule> processRelaRuleList = o_processBO.getProcessRelaRules(processVar.getId());
				int j = 1;
				for(ProcessRelaRule processRelaRule : processRelaRuleList){
					sb.append(BODY_START)
					.append("4."+ j +" ").append("《").append(processRelaRule.getRule().getName()).append("》")
					.append(BODY_END);
					j++;
				}
				title2++;
				//查询所有流程节点  并显示为列表
				List<ProcessPoint> processPointList = o_processPointBO.findProcessPointListByProcessId(processVar.getId());
				// 加入流程节点内容
				sb.append(HEADER_TITLE1_START)
				.append("5、流程描述:")
				.append(HEADER_TITLE1_END);
				sb.append(this.appendProcessPoint(processPointList));
				//查询出所有风险并显示为列表
				sb.append(HEADER_TITLE1_START)
				.append("6、风险描述:")
				.append(HEADER_TITLE1_END);
				sb.append(appendProcessRelaRiskList(processVar));
			}
		}
		return sb;
	}
	/**
	 * 根据流程id加入流程节点列表
	 */
	public StringBuilder appendProcessPoint(List<ProcessPoint> processPointList){
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
			.append("<tr>")
			.append(HEADER_TD_START)
			.append("编号")
			.append(HEADER_TD_END)
			.append(HEADER_TD_START)
			.append("责任部门")
			.append(HEADER_TD_END)
			.append(HEADER_TD_START)
			.append("流程步骤描述")
			.append(HEADER_TD_END)
			.append(HEADER_TD_START)
			.append("输出文档")
			.append(HEADER_TD_END)
			.append("</tr>");
		for(ProcessPoint processPoint : processPointList){
			String orgName = "";
			builder.append("<tr>")
			.append(BODY_TD_START)
			.append(processPoint.getCode())
			.append(BODY_TD_END);
			Set<ProcessPointRelaOrg> processPointRelaOrgs = processPoint.getProcesspointRelaOrg();
			for (ProcessPointRelaOrg processPointRelaOrg : processPointRelaOrgs) {
				if (Contents.ORG_RESPONSIBILITY.equals(processPointRelaOrg.getType()) && null != processPointRelaOrg.getOrg()){
					orgName = processPointRelaOrg.getOrg().getOrgname();
				}
			}
			builder.append(BODY_TD_START)
			.append(orgName)
			.append(BODY_TD_END);
			builder.append(BODY_TD_START)
			.append(processPoint.getName())
			.append(BODY_TD_END)
			.append("</tr>");
		}
		builder.append("</tbody></table>");
		return builder;
	}
	
	private StringBuilder appendProcessRelaRiskList(Process process) {
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
		.append("<tr>")
		.append(HEADER_TD_START)
		.append("风险点编号")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("风险点")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制点编号")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制措施")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制方法")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制频率")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("实施证据")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("制度索引")
		.append(HEADER_TD_END)
		.append("</tr>");
			//序号
			int rownum=1;
			Set<ProcessRelaRisk> processRelaRiskSet = process.getProcessRelaRisks();  /*获取流程对应的风险总数*/
			
			for (ProcessRelaRisk processRelaRisk : processRelaRiskSet) {
				//风险点编号
				String riskCode = "";
				//风险点
				String riskName = "";
				//控制点编号
				String measureCode = "";
				//控制措施
				String measureName = "";
				//控制方法
				String measureMethod = "";
				//控制频率
				String controlFrequency = "";
				//实施证据
				String implementProof = "";
				//制度索引
				String ruleName = "";
				Criteria criteria = o_measureRelaRiskDAO.createCriteria();
				criteria.createAlias("controlMeasure", "controlMeasure");
				if(!(processRelaRisk.getRisk() instanceof Risk)){
					break;
				}
				criteria.add(Restrictions.eq("risk.id", processRelaRisk.getRisk().getId()));
				List<MeasureRelaRisk> measureRelaRiskList = criteria.list();
				for(MeasureRelaRisk measureRelaRisk : measureRelaRiskList){
					builder.append("<tr>")
					.append(BODY_TD_START)
					.append(processRelaRisk.getRisk().getCode())    /* 风险编号 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(StringUtils.isNotBlank(processRelaRisk.getRisk().getName())){
						builder.append(processRelaRisk.getRisk().getName());     /* 风险名称 */
					}else{
						builder.append("无");     /* 风险名称 */
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START)
					.append(measureRelaRisk.getControlMeasure().getCode())   /* 控制措施编号 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(measureRelaRisk.getControlMeasure().getControlMeasure() != null){
						builder.append(measureRelaRisk.getControlMeasure().getControlMeasure().getName());   /* 控制方法 */
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(measureRelaRisk.getControlMeasure().getControlFrequency() != null){
						builder.append(measureRelaRisk.getControlMeasure().getControlFrequency().getName());   /* 控制频率*/
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START)
					.append(measureRelaRisk.getControlMeasure().getImplementProof())   /* 控制证据 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					Criteria proRelaRuleCra = o_processRelaRuleDAO.createCriteria();
					proRelaRuleCra.add(Restrictions.eq("process.id", process.getId()));
					List<ProcessRelaRule> processRelaRuleList = proRelaRuleCra.list();
					if(!processRelaRuleList.isEmpty()){
						for(ProcessRelaRule processRelaRule : processRelaRuleList){
							builder.append(processRelaRule.getRule().getName()).append(",");
						}
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append("</tr>");
					rownum++;
				}
			
		}
		builder.append("</tbody></table>");
//		newReportData = StringUtils.replace(newReportData, "${风险控制矩阵}", builder.toString());
		return builder;
	}
	/**
	 * <pre>
	 *  查询所有一级流程
	 * </pre>
	 * @author 宋佳
	 * @param id
	 * @param query
	 * @param processResult
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Process> findParentProcessResult(){
		Criteria criteria = o_processDAO.createCriteria();
		criteria.add(Restrictions.isNull("parent.id"));   //  只允许使用isnull  使用isempty报错
		List<Process> processList=criteria.list();
		return processList;
	}
	private StringBuilder createProcessRelaRiskList(String reportData, Process process) {
		String newReportData = reportData;
		List<String> responsibilityOrg = new ArrayList<String>();
		
		StringBuilder builder = new StringBuilder("<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\">");
		builder.append("<tbody>")
		.append("<tr>")
		.append(HEADER_TD_START)
		.append("风险点编号")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("风险点")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制点编号")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制措施")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制方法")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("控制频率")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("实施证据")
		.append(HEADER_TD_END)
		.append(HEADER_TD_START)
		.append("制度索引")
		.append(HEADER_TD_END)
		.append("</tr>");
			//序号
			int rownum=1;
			Set<ProcessRelaRisk> processRelaRiskSet = process.getProcessRelaRisks();  /*获取流程对应的风险总数*/
			
			for (ProcessRelaRisk processRelaRisk : processRelaRiskSet) {
				//风险点编号
				String riskCode = "";
				//风险点
				String riskName = "";
				//控制点编号
				String measureCode = "";
				//控制措施
				String measureName = "";
				//控制方法
				String measureMethod = "";
				//控制频率
				String controlFrequency = "";
				//实施证据
				String implementProof = "";
				//制度索引
				String ruleName = "";
				Criteria criteria = o_measureRelaRiskDAO.createCriteria();
				criteria.createAlias("controlMeasure", "controlMeasure");
				if(null == processRelaRisk.getRisk()){
					break;
				}
				criteria.add(Restrictions.eq("risk.id", processRelaRisk.getRisk().getId()));
				List<MeasureRelaRisk> measureRelaRiskList = criteria.list();
				for(MeasureRelaRisk measureRelaRisk : measureRelaRiskList){
					builder.append("<tr>")
					.append(BODY_TD_START)
					.append(processRelaRisk.getRisk().getCode())    /* 风险编号 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(StringUtils.isNotBlank(processRelaRisk.getRisk().getName())){
						builder.append(processRelaRisk.getRisk().getName());     /* 风险名称 */
					}else{
						builder.append("无");     /* 风险名称 */
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START)
					.append(measureRelaRisk.getControlMeasure().getCode())   /* 控制措施编号 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(measureRelaRisk.getControlMeasure().getControlMeasure() != null){
						builder.append(measureRelaRisk.getControlMeasure().getControlMeasure().getName());   /* 控制方法 */
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START);
					if(measureRelaRisk.getControlMeasure().getControlFrequency() != null){
						builder.append(measureRelaRisk.getControlMeasure().getControlFrequency().getName());   /* 控制频率*/
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append(BODY_TD_START)
					.append(measureRelaRisk.getControlMeasure().getImplementProof())   /* 控制证据 */
					.append(BODY_TD_END)
					.append(BODY_TD_START);
					Criteria proRelaRuleCra = o_processRelaRuleDAO.createCriteria();
					proRelaRuleCra.add(Restrictions.eq("process.id", process.getId()));
					List<ProcessRelaRule> processRelaRuleList = proRelaRuleCra.list();
					if(!processRelaRuleList.isEmpty()){
						for(ProcessRelaRule processRelaRule : processRelaRuleList){
							builder.append(processRelaRule.getRule().getName()).append(",");
						}
					}else{
						builder.append("无");
					}
					builder.append(BODY_TD_END)
					.append("</tr>");
					rownum++;
				}
			
		}
		builder.append("</tbody></table>");
//		newReportData = StringUtils.replace(newReportData, "${风险控制矩阵}", builder.toString());
		return builder;
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
		o_reportRelaConstructPlanBO.removeReportRelaConstructByReportIds(ids);
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
	public double findVersionCodeByPlanId(String reportType){
		double ret = 0.0;
		
		/*
		select i.report_code 
		from t_report_information i left join t_report_rela_assessment a on i.id=a.report_id 
		where a.assessment_plan_id='' and i.report_type='' 
		*/
		
		List<String> list = null;
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append("select i.report_code ")
			.append("from t_report_information i ")
			.append("where 1=1 ");
		if(StringUtils.isNotBlank(reportType)){
			sqlBuffer.append("and i.report_type=:reportType ");
		}
		
		SQLQuery sqlQuery = o_reportInfomationDAO.createSQLQuery(sqlBuffer.toString());
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
