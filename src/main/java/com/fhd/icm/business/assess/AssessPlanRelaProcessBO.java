package com.fhd.icm.business.assess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.assess.AssessPlanProcessRelaOrgEmpDAO;
import com.fhd.icm.dao.assess.AssessPlanRelaOrgEmpDAO;
import com.fhd.icm.dao.assess.AssessPlanRelaProcessDAO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-24		上午11:43:41
 *
 * @see 	 
 */
@SuppressWarnings("unchecked")
@Service
public class AssessPlanRelaProcessBO {
	
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessPlanRelaProcessDAO o_assessPlanRelaProcessDAO;
	@Autowired
	private AssessPlanProcessRelaOrgEmpDAO o_assessPlanProcessRelaOrgEmpDAO;
	@Autowired
	private ProcessDAO o_processDAO;
	@Autowired
	private ProcessBO o_processBO;
	@Autowired
	private AssessPlanRelaOrgEmpDAO o_assessPlanRelaOrgEmpDAO;

	/**
	 * 内控维护流程范围列表中添加流程功能.
	 * @author 刘中帅
	 * @modify 吴德福 2013-3-12
	 * @param processIds：流程Ids，多选是以‘,’间隔的字符串
	 * @param assessPlanId 评价计划Id
	 * @param assessPlanDepartIds 评价计划相关部门ids，多选是以‘,’间隔的字符串
	 * @param selectType 流程选择类型：按部门/按流程
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveAssessPlanRelaProcess(String processIds,String assessPlanId,String assessPlanDepartIds,String selectType){
		//查询评价计划
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
		//查询出已经有的计划流程，流程Id有重复的不添加
		List<AssessPlanRelaProcess> appList= findAssessPlanRelaProcessListByAssessPlanId(assessPlanId);
		//查询出已经有的计划人员，人员id有得复的不添加
		Criteria criteria = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		List<AssessPlanRelaOrgEmp> apoeList = criteria.list();
		
		boolean isPracticeTest=true,isSampleTest=true;//穿行，抽样
		//穿行测试
		if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
			isSampleTest=false;
		}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){//抽样测试
			isPracticeTest=false;
		}
		String[] processIdArray=null;
		if(Contents.ASSESS_MEASURE_PROCESSSELECTBYDEPT.equals(selectType)){//按部门选择
			processIdArray=assessPlanDepartIds.split(",");
			//按部门选择的时候要保存相应的部门到关联表
		}else if(Contents.ASSESS_MEASURE_PROCESSSELECTBYBUSSESS.equals(selectType)){
			processIdArray=processIds.split(",");
		}
		Set<String> processIdSet = new HashSet<String>();
		for(String processId : processIdArray){
			if(!processIdSet.contains(processId)){
				processIdSet.add(processId);
			}
		}
		Map<String,String> map=new HashMap<String,String>();
		
		for(String pId : processIdSet){
			boolean flag=false;
			for(AssessPlanRelaProcess app:appList){
				//判断是否有重复数据,没有重复的数据才添加
				if(pId.equals(app.getProcess().getId())){
					flag=true;
				}
			}
			if(!flag){
				//内控评价计划与流程的关联
				AssessPlanRelaProcess assessPlanRelaProcess =new AssessPlanRelaProcess();
				assessPlanRelaProcess.setId(Identities.uuid());
				assessPlanRelaProcess.setIsPracticeTest(isPracticeTest);
				assessPlanRelaProcess.setPracticeNum(1);
				assessPlanRelaProcess.setIsSampleTest(isSampleTest);
				assessPlanRelaProcess.setExecuteStatus(Contents.DEAL_STATUS_NOTSTART);
				//内控评价计划
				assessPlanRelaProcess.setAssessPlan(assessPlan);
				//流程实体
				Process process=o_processBO.findProcessById(pId);
				assessPlanRelaProcess.setProcess(process);
				//抽取样本比例
				Double coverageRate = o_assessPlanBO.findFrequenceNumByProcess(process);
				assessPlanRelaProcess.setCoverageRate(coverageRate);
				o_assessPlanRelaProcessDAO.merge(assessPlanRelaProcess);
				
				if(null != assessPlan.getType() && Contents.ASSESS_MEASURE_ETYPE_SELF.equals(assessPlan.getType().getId())){//自评
					//评价人
					ProcessRelaOrg processRelaOrg=o_processBO.findSysEmployeeByProcessId(pId);
					if(null!=processRelaOrg && null!=processRelaOrg.getEmp()){
						if(!map.containsKey(processRelaOrg.getEmp().getId())){
							boolean isExistEmp = false;
							for (AssessPlanRelaOrgEmp apoe : apoeList) {
								if(processRelaOrg.getEmp().getId().equals(apoe.getEmp().getId())){
									isExistEmp = true;
								}
							}
							if(!isExistEmp){
								AssessPlanRelaOrgEmp assessPlanRelaOrgEmp=new AssessPlanRelaOrgEmp(Identities.uuid()); 
								assessPlanRelaOrgEmp.setAssessPlan(assessPlan);
								assessPlanRelaOrgEmp.setEmp(processRelaOrg.getEmp());
								assessPlanRelaOrgEmp.setType(Contents.EMP_HANDLER);
								o_assessPlanRelaOrgEmpDAO.merge(assessPlanRelaOrgEmp);
							}
							
							//放到集合
							map.put(processRelaOrg.getEmp().getId(), processRelaOrg.getEmp().getId());
					  	}
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 批量设置评价涉及的的流程范围：
	 * 批量保存是否进行抽样测试及抽取样本数；
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanProcessIds 评价计划关联流程的ID串，多个以“,”隔开，例如：dfaafsadfa,fadfaferf
	 * @param isSampleTest 是否抽样测试，例如: false
	 * @param jsonString 抽样比率：[{frequencyCode:'ic_control_frequency_0',rate:0.30},{frequencyCode:'ic_control_frequency_1',rate:0.20},{frequencyCode:'ic_control_frequency_week',rate:0.25},{...}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanRelaProcessForSampleTestBatch(String assessPlanProcessIds, Boolean isSampleTest, String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		Set<String> assessPlanProcessIdSet = new HashSet<String>();
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = new ArrayList<AssessPlanRelaProcess>();
		String[] assessPlanProcessIdArray = null;
		if(StringUtils.isNotBlank(assessPlanProcessIds)){
			assessPlanProcessIdArray = assessPlanProcessIds.split(",");
		}
		if(null != assessPlanProcessIdArray){
			for (String assessPlanProcessId : assessPlanProcessIdArray) {
				assessPlanProcessIdSet.add(assessPlanProcessId);
			}
		}
		assessPlanRelaProcessList = o_assessPlanRelaProcessDAO.get(assessPlanProcessIdSet);
		for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
			if(null != isSampleTest){
				Double coverageRate = o_assessPlanBO.findFrequenceNumByProcess(assessPlanRelaProcess.getProcess());
				assessPlanRelaProcess.setCoverageRate(coverageRate);
				assessPlanRelaProcess.setIsSampleTest(isSampleTest);
				mergeAssessPlanRelaProcess(assessPlanRelaProcess);
			}
		}
	}
	
	/**
	 * <pre>
	 * 保存评价相关流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaProcess
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanRelaProcess(AssessPlanRelaProcess assessPlanRelaProcess){
		o_assessPlanRelaProcessDAO.merge(assessPlanRelaProcess);
	}
	
	/**
	 * <pre>
	 * 批量设置评价涉及的的流程范围：
	 * 批量保存是否进行穿行测试及穿几回；
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanProcessIds 评价计划关联流程的ID串，多个以“,”隔开，例如：dfaafsadfa,fadfaferf
	 * @param isPracticeTest 是否穿行测试，例如：true
	 * @param practiceNum 穿几回，例如：2
	  * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanRelaProcessForPracticeTestBatch(String assessPlanProcessIds,
			Boolean isPracticeTest, Integer practiceNum){
		String[] assessPlanProcessIdArray = null;
		Set<String> assessPlanProcessIdSet = new HashSet<String>();
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = new ArrayList<AssessPlanRelaProcess>();
		if(StringUtils.isNotBlank(assessPlanProcessIds)){
			assessPlanProcessIdArray = assessPlanProcessIds.split(",");
		}
		if(null != assessPlanProcessIdArray){
			for (String assessPlanProcessId : assessPlanProcessIdArray) {
				assessPlanProcessIdSet.add(assessPlanProcessId);
			}
		}
		assessPlanRelaProcessList = o_assessPlanRelaProcessDAO.get(assessPlanProcessIdSet);
		for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
			if(null != isPracticeTest){
				assessPlanRelaProcess.setIsPracticeTest(isPracticeTest);
				assessPlanRelaProcess.setPracticeNum(practiceNum);
			}
			mergeAssessPlanRelaProcess(assessPlanRelaProcess);
		}
	}
	
	
	/**
	 * <pre>
	 * 批量保存评价计划关联的流程范围
	 * 批量保存是否穿行测试，穿行次数，是否抽样测试，抽取样本数，抽样覆盖率
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param jsonString 保存信息的字符串，例如：[{id:'11111',isPracticeTest:true,isSampleTest:false,practiceNum:1,sampleNum:2},{...}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanRelaProcessBatch(String assessPlanId,String jsonString) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
		
		Set<String> assessPlanRelaProcessIdSet = new HashSet<String>();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//AssessPlanRelaProcess的ID
			assessPlanRelaProcessIdSet.add(id);
		}
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = o_assessPlanRelaProcessDAO.get(assessPlanRelaProcessIdSet);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//AssessPlanRelaProcess的ID
			if(StringUtils.isNotBlank(id)){
				for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
					if(id.equals(assessPlanRelaProcess.getId())){
						if(Contents.ASSESS_MEASURE_ALL_TEST.equals(assessPlan.getAssessMeasure().getId())){
							//全部：穿行测试和抽样测试
							Boolean isPracticeTest = jsonObject.getBoolean("isPracticeTest");//是否穿行测试
							assessPlanRelaProcess.setIsPracticeTest(isPracticeTest);
							Integer practiceNum = jsonObject.getInt("practiceNum");
							if(null == practiceNum){
								practiceNum=1;
							}
							assessPlanRelaProcess.setPracticeNum(practiceNum);
							
							Boolean isSampleTest = jsonObject.getBoolean("isSampleTest");//是否抽样测试
							assessPlanRelaProcess.setIsSampleTest(isSampleTest);
						}else if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
							//穿行测试
							Boolean isPracticeTest = jsonObject.getBoolean("isPracticeTest");//是否穿行测试
							assessPlanRelaProcess.setIsPracticeTest(isPracticeTest);
							Integer practiceNum = jsonObject.getInt("practiceNum");
							if(null == practiceNum){
								practiceNum=1;
							}
							assessPlanRelaProcess.setPracticeNum(practiceNum);
						}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){
							//抽样测试
							Boolean isSampleTest = jsonObject.getBoolean("isSampleTest");//是否抽样测试
							assessPlanRelaProcess.setIsSampleTest(isSampleTest);
						}
						
						mergeAssessPlanRelaProcess(assessPlanRelaProcess);
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 批量保存流程的参与评价的人：
	 * 按部门批量保存参与评价的人的信息，当batchType为org时，例如：[{orgId:'23311',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * handlerId和reviewerId分别为assessPlanRelaOrgEmp的ID
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param jsonString 保存信息的字符串
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergerAssessPlanProcessRelaOrgEmpBatch(String assessPlanId,String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String handlerId = jsonObject.getString("handlerId");//评价人
			String reviewerId = jsonObject.getString("reviewerId");//复核人
			Criteria assessPlanRelaProcessCriteria = o_assessPlanRelaProcessDAO.createCriteria();
			assessPlanRelaProcessCriteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
			String orgId = jsonObject.getString("orgId");//部门ID
			Criteria processCriteria = o_processDAO.createCriteria();
			processCriteria.createAlias("processRelaOrg", "processRelaOrg").createAlias("processRelaOrg.org", "org").add(Restrictions.eq("org.id", orgId));
			processCriteria.add(Restrictions.eq("processRelaOrg.type", Contents.ORG_RESPONSIBILITY));
			processCriteria.setProjection(Projections.property("id"));
			assessPlanRelaProcessCriteria.add(Property.forName("process.id").in(processCriteria.list()));
			List<AssessPlanRelaProcess> assessPlanRelaProcessList = assessPlanRelaProcessCriteria.list();
			List<String> assessPlanRelaProcessIdList = new ArrayList<String>();
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {//
				assessPlanRelaProcessIdList.add(assessPlanRelaProcess.getId());
			}
			if(null != assessPlanRelaProcessIdList && 0<assessPlanRelaProcessIdList.size()){
				removeAssessPlanProcessRelaOrgEmpByAssessPlanRelaProcessIdList(assessPlanRelaProcessIdList);//保存之前先删除原有的流程的评价人和复核人
				for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
					String assessPlanRelaProcessId = assessPlanRelaProcess.getId();
					
					AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp = new AssessPlanProcessRelaOrgEmp(Identities.uuid());
					assessPlanProcessRelaOrgEmp.setAssessPlanRelaProcess(new AssessPlanRelaProcess(assessPlanRelaProcessId));
					assessPlanProcessRelaOrgEmp.setAssessPlanRelaOrgEmp(this.findAssessPlanRelaOrgEmpByEmpIdAndType(assessPlanId, handlerId));
					assessPlanProcessRelaOrgEmp.setType(Contents.EMP_HANDLER);
					mergeAssessPlanProcessRelaOrgEmp(assessPlanProcessRelaOrgEmp);
					
					AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmpReviewer = new AssessPlanProcessRelaOrgEmp(Identities.uuid());
					assessPlanProcessRelaOrgEmpReviewer.setAssessPlanRelaProcess(new AssessPlanRelaProcess(assessPlanRelaProcessId));
					assessPlanProcessRelaOrgEmpReviewer.setAssessPlanRelaOrgEmp(this.findAssessPlanRelaOrgEmpByEmpIdAndType(assessPlanId, reviewerId));
					assessPlanProcessRelaOrgEmpReviewer.setType(Contents.EMP_REVIEW_PERSON);
					mergeAssessPlanProcessRelaOrgEmp(assessPlanProcessRelaOrgEmpReviewer);
				}
			}
			
		}
	}
	
	/**
	 * <pre>
	 * 保存评价计划ID为assessPlanId的计划的流程的评价人信息
	 * handlerId和reviewerId分别为assessPlanRelaOrgEmp的ID
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId
	 * @param jsonString 保存人员信息的字符串，例如：[{assessPlanRelaProcessId:'11111',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanProcessRelaOrgEmpBatch(String assessPlanId, String jsonString) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessPlanRelaProcessId = jsonObject.getString("assessPlanRelaProcessId");//评价计划关联的评价点ID
			String handlerId = jsonObject.getString("handlerId");//评价人
			String reviewerId = jsonObject.getString("reviewerId");//复核人
			removeAssessPlanProcessRelaOrgEmpByAssessPlanRelaProcessId(assessPlanRelaProcessId);//保存之前先删除原有的实施评价的评价人和复核人
			
			AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp = new AssessPlanProcessRelaOrgEmp(Identities.uuid());
			assessPlanProcessRelaOrgEmp.setAssessPlanRelaProcess(new AssessPlanRelaProcess(assessPlanRelaProcessId));
			assessPlanProcessRelaOrgEmp.setAssessPlanRelaOrgEmp(this.findAssessPlanRelaOrgEmpByEmpIdAndType(assessPlanId, handlerId));
			//根据empid查询评价计划人员表
			assessPlanProcessRelaOrgEmp.setType(Contents.EMP_HANDLER);
			mergeAssessPlanProcessRelaOrgEmp(assessPlanProcessRelaOrgEmp);
			
			AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmpReviewer = new AssessPlanProcessRelaOrgEmp(Identities.uuid());
			assessPlanProcessRelaOrgEmpReviewer.setAssessPlanRelaProcess(new AssessPlanRelaProcess(assessPlanRelaProcessId));
			//根据empid查询评价计划人员表
			assessPlanProcessRelaOrgEmpReviewer.setAssessPlanRelaOrgEmp(this.findAssessPlanRelaOrgEmpByEmpIdAndType(assessPlanId, reviewerId));
			assessPlanProcessRelaOrgEmpReviewer.setType(Contents.EMP_REVIEW_PERSON);
			mergeAssessPlanProcessRelaOrgEmp(assessPlanProcessRelaOrgEmpReviewer);
		}
	}
	
	/**
	 * 根据查询条件查询评价计划关联部门人员.
	 * @param assessPlanId
	 * @param empId
	 * @return AssessPlanRelaOrgEmp
	 */
	public AssessPlanRelaOrgEmp findAssessPlanRelaOrgEmpByEmpIdAndType(String assessPlanId, String empId){
		AssessPlanRelaOrgEmp assessPlanRelaOrgEmp = null;
		Criteria criteria = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("emp.id", empId));
		List<AssessPlanRelaOrgEmp> list = criteria.list();
		if(null != list && list.size()>0){
			assessPlanRelaOrgEmp = list.get(0);
		}
		return assessPlanRelaOrgEmp;
	}
	
	/**
	 * <pre>
	 * 保存流程关联的评价人和复核人等
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanProcessRelaOrgEmp 流程关联的评价人和复核人
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanProcessRelaOrgEmp(AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp){
		o_assessPlanProcessRelaOrgEmpDAO.merge(assessPlanProcessRelaOrgEmp);
	}
	
	/**
	 * <pre>
	 * 物理删除评价关联流程的ID为assessPlanRelaProcessId的评价人的信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaProcessId 评价关联流程的ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanProcessRelaOrgEmpByAssessPlanRelaProcessId(String assessPlanRelaProcessId){
		o_assessPlanProcessRelaOrgEmpDAO.createQuery("delete AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp where assessPlanProcessRelaOrgEmp.assessPlanRelaProcess.id=:assessPlanRelaProcessId")
		.setString("assessPlanRelaProcessId", assessPlanRelaProcessId)
		.executeUpdate();
	}
	
	/**
	 * <pre>
	 * 物理删除评价计划ID为assessPlanId的评价计划关联的流程范围
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanRelaProcesByAssessPlanId(String assessPlanId){
		String hql="delete AssessPlanRelaProcess where assessPlan.id=:assessPlanId";
		Query query=o_assessPlanRelaProcessDAO.createQuery(hql);
		query.setParameter("assessPlanId",assessPlanId);
		query.executeUpdate();
	}
	
	/**
	 * <pre>
	 * 物理删除评价计划ID为assessPlanId的评价计划关联的部门人员信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanRelaOrgEmpByAssessPlanId(String assessPlanId){
		String hql="delete AssessPlanRelaOrgEmp where assessPlan.id=:assessPlanId";
		Query	query=o_assessPlanRelaProcessDAO.createQuery(hql);
		query.setParameter("assessPlanId",assessPlanId);
		query.executeUpdate();
	}
	
	/**
	 * <pre>
	 * 物理删除评价关联流程的ID为assessPlanRelaProcessId的评价人的信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaProcessIdList 评价关联流程的ID的List
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanProcessRelaOrgEmpByAssessPlanRelaProcessIdList(List<String> assessPlanRelaProcessIdList){
		if(null !=assessPlanRelaProcessIdList && assessPlanRelaProcessIdList.size()>0){
			StringBuffer hql = new StringBuffer();
			hql.append("delete AssessPlanProcessRelaOrgEmp approe where approe.assessPlanRelaProcess.id in('").append(StringUtils.join(assessPlanRelaProcessIdList,"','")).append("')");
			o_assessPlanProcessRelaOrgEmpDAO.createQuery(hql.toString()).executeUpdate();
		}
	}
	
	/**
	 * <pre>
	 * 物理删除评价关联流程的ID的List的集合
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param idList 评价关联流程的ID的List
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanRelaProcessesByIdList(List<String> idList){
		if(null !=idList && idList.size()>0){
			StringBuffer hql = new StringBuffer();
			hql.append("delete AssessPlanRelaProcess app where app.id in('").append(StringUtils.join(idList,"','")).append("')");
			o_assessPlanRelaProcessDAO.createQuery(hql.toString()).executeUpdate();
		}
	}
	
	/**
	 * <pre>
	 * 根据评价计划id查询评价计划关联流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @return List<AssessPlanRelaProcess>
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanRelaProcess> findAssessPlanRelaProcessListByAssessPlanId(String assessPlanId){
		Criteria criteria = o_assessPlanRelaProcessDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		return criteria.list();
	}
	
	/**
	 * 根据评价计划id和流程id查询评价计划关联流程.
	 * @author 吴德福
	 * @param assessPlanId 评价计划ID
	 * @param processId 流程id
	 * @return List<AssessPlanRelaProcess>
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanRelaProcess> findAssessPlanRelaProcessListBySome(String assessPlanId, String processId){
		Criteria criteria = o_assessPlanRelaProcessDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("process.id", processId));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 根据评价关联的流程ID查询AssessPlanRelaProcess实体
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaProcessId 评价关联流程的ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public AssessPlanRelaProcess findAssessPlanRelaProcessById(String assessPlanRelaProcessId){
		return o_assessPlanRelaProcessDAO.get(assessPlanRelaProcessId);
	}
	/**
	 * <pre>
	 * 查询评价计划关联的流程
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param page 分页类
	 * @param query 查询条件模糊匹配流程名称及上级流程名称
	 * @param assessPlanId 评价计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<AssessPlanRelaProcess> findAssessPlanRelaProcessListByPage(Page<AssessPlanRelaProcess> page, String query, String assessPlanId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssessPlanRelaProcess.class);
		detachedCriteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		detachedCriteria.setFetchMode("assessPlan", FetchMode.SELECT);
		detachedCriteria.createAlias("process", "process",CriteriaSpecification.LEFT_JOIN);
		detachedCriteria.createAlias("process.parent", "parentProcess",CriteriaSpecification.LEFT_JOIN);
		detachedCriteria.createAlias("process.processRelaOrg", "processRelaOrg",CriteriaSpecification.LEFT_JOIN);
		detachedCriteria.add(Restrictions.eq("processRelaOrg.type", Contents.ORG_RESPONSIBILITY));//责任部门
		detachedCriteria.setFetchMode("process.createBy", FetchMode.SELECT);
		detachedCriteria.setFetchMode("process.lastModifyBy", FetchMode.SELECT);
		detachedCriteria.setFetchMode("process.processRelaOrg", FetchMode.SELECT);
		detachedCriteria.setFetchMode("process.processRelaOrg.org", FetchMode.SELECT);
		if(StringUtils.isNotBlank(query)){//模糊匹配流程的名称及上级流程名称
			detachedCriteria.add(Restrictions.or(Property.forName("process.name").like(query, MatchMode.ANYWHERE), Property.forName("parentProcess.name").like(query, MatchMode.ANYWHERE)));
		}
		return o_assessPlanRelaProcessDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询流程相关的部门或人
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param empIsNotNull 员工ID不为空
	 * @param orgIsNotNull 部门ID不为空
	 * @param returnType 参与部门,经办人，复核人的常量
	 * @param empId 人员ID
	 * @param orgId 机构ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanProcessRelaOrgEmp> findAssessPlanProcessRelaOrgEmpBySome(String assessPlanId, Boolean empIsNotNull,Boolean orgIsNotNull, String returnType, String empId, String orgId){
		Criteria criteria = o_assessPlanProcessRelaOrgEmpDAO.createCriteria();
		criteria.createAlias("assessPlanRelaProcess", "assessPlanRelaProcess", CriteriaSpecification.LEFT_JOIN)
		.createAlias("assessPlanRelaProcess.process", "process", CriteriaSpecification.LEFT_JOIN)
		.createAlias("assessPlanRelaProcess.assessPlan", "assessPlan", CriteriaSpecification.LEFT_JOIN).add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(returnType)){
			criteria.add(Restrictions.eq("type", returnType));
		}
		if(empIsNotNull){
			criteria.createAlias("assessPlanRelaOrgEmp", "assessPlanRelaOrgEmp", CriteriaSpecification.LEFT_JOIN)
			.createAlias("assessPlanRelaOrgEmp.emp", "emp", CriteriaSpecification.LEFT_JOIN)
			.add(Restrictions.isNotNull("emp.id"));
			if(StringUtils.isNotBlank(empId)){
				criteria.add(Restrictions.eq("emp.id", empId));
			}
		}
		if(orgIsNotNull){
			criteria.createAlias("assessPlanRelaOrgEmp", "assessPlanRelaOrgEmp", CriteriaSpecification.LEFT_JOIN)
			.createAlias("assessPlanRelaOrgEmp.org", "org", CriteriaSpecification.LEFT_JOIN)
			.add(Restrictions.isNotNull("org.id"));
			if(StringUtils.isNotBlank(orgId)){
				criteria.add(Restrictions.eq("org.id", orgId));
			}
		}
		return criteria.list();
	}

	
	/**
	 *根据评价计划Id查询所有评价人.
	 * @param assessPlanId 评价计划Id
	 * @param type 评价人类型
	 * @author 刘中帅
	 * @return List<SysEmployee>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> findEmpByAssessPlanId(String assessPlanId,String type){
		Criteria criteria = o_assessPlanProcessRelaOrgEmpDAO.createCriteria();
		criteria.createAlias("assessPlanRelaProcess", "assessPlanRelaProcess");
		
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("assessPlanRelaProcess.assessPlan.id", assessPlanId));
		List<SysEmployee> resultList=new ArrayList<SysEmployee>();
		List<AssessPlanProcessRelaOrgEmp> list=criteria.list();
		for(AssessPlanProcessRelaOrgEmp apo : list){
			if(!resultList.contains(apo.getAssessPlanRelaOrgEmp().getEmp())){
				resultList.add(apo.getAssessPlanRelaOrgEmp().getEmp());
			}
		}
		return resultList;
	}
	
	/**
	 * 根据评价计划id查询该评价计划关联的人员对应的流程数量.
	 * @param assessPlanId
	 * @return List<Object[]>
	 */
	public List<Object[]> findEmpRelaProcessCount(String assessPlanId){
		List<Object[]> list = null;
		StringBuilder selectBuffer = new StringBuilder();
		StringBuilder fromBuffer = new StringBuilder();
		StringBuilder whereBuffer = new StringBuilder();
		StringBuilder groupByBuffer = new StringBuilder();
		StringBuilder orderByBuffer = new StringBuilder();
		
		selectBuffer.append("select po.emp_id,count(po.emp_id) ");
		
		fromBuffer.append("from t_ca_plan_processure pp  ")
			.append("left join t_ic_processure p on pp.processure_id=p.id ")
			.append("left join t_ic_processure_rela_org po on p.id=po.processure_id ");
		
		whereBuffer.append("where po.etype='ER' ");
		if(StringUtils.isNotBlank(assessPlanId)){
			whereBuffer.append("and pp.plan_id = :assessPlanId ");
		}
		
		groupByBuffer.append("group by po.emp_id ");
		orderByBuffer.append("order by pp.processure_id ");
		
		SQLQuery sqlQuery = o_assessPlanRelaProcessDAO.createSQLQuery(selectBuffer.append(fromBuffer).append(whereBuffer).append(groupByBuffer).append(orderByBuffer).toString());
		if(StringUtils.isNotBlank(assessPlanId)){
			sqlQuery.setString("assessPlanId", assessPlanId);
		}
		list = sqlQuery.list();
		
		return list;
	}
}

