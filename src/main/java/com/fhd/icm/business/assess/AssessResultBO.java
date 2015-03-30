package com.fhd.icm.business.assess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.assess.AssessPlanRelaProcessDAO;
import com.fhd.icm.dao.assess.AssessRelaDefectDAO;
import com.fhd.icm.dao.assess.AssessResultDAO;
import com.fhd.icm.dao.assess.AssessSampleDAO;
import com.fhd.icm.dao.assess.AssessSampleRelaFileDAO;
import com.fhd.icm.dao.assess.AssessorDAO;
import com.fhd.icm.dao.defect.DefectDAO;
import com.fhd.icm.dao.defect.DefectRelaOrgDAO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaPoint;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.assess.AssessResult;
import com.fhd.icm.entity.assess.AssessSample;
import com.fhd.icm.entity.assess.AssessSampleRelaFile;
import com.fhd.icm.entity.assess.Assessor;
import com.fhd.icm.entity.control.Measure;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.process.dao.ProcessRelaOrgDAO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 评价计划分配完任务之后，评价执行时用到的业务方法
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-15		上午9:41:01
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class AssessResultBO {
	
	@Autowired
	private AssessorDAO o_assessorDAO;
	@Autowired
	private AssessResultDAO o_assessResultDAO;
	@Autowired
	private DefectDAO o_defectDAO;
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	@Autowired
	private AssessSampleDAO o_assessSampleDAO;
	@Autowired
	private AssessSampleRelaFileDAO o_assessSampleRelaFileDAO;
	@Autowired
	private AssessPlanRelaProcessDAO o_assessPlanRelaProcessDAO;
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	@Autowired
	private AssessRelaDefectDAO o_assessRelaDefectDAO;
	@Autowired
	private AssessorBO o_assessorBO;
	@Autowired
	private DefectRelaOrgDAO o_defectRelaOrgDAO;
	@Autowired
	private ProcessRelaOrgDAO o_processRelaOrgDAO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	
	/**
	 * <pre>
	 *保存评价流程
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param isDesirableAdjust
	 * @param assessmentDesc
	 * @param assessPlanId
	 * @param processId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveResultForm(String isDesirableAdjust, String assessmentDesc,String assessPlanId,String processId) {
		Criteria criteria = o_assessPlanRelaProcessDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("process.id", processId));
		List<AssessPlanRelaProcess> list=criteria.list();
		if(null!=list && list.size()>0){
			AssessPlanRelaProcess assessPlanRelaProcess=new AssessPlanRelaProcess();
			assessPlanRelaProcess = list.get(0);
			assessPlanRelaProcess.setIsDesirableAdjust(isDesirableAdjust);
			assessPlanRelaProcess.setAssessmentDesc(assessmentDesc);
			o_assessPlanRelaProcessDAO.merge(assessPlanRelaProcess);
		}
	}
	/**
	 * <pre>
	 * 批量添加参评人信息和评价结果
	 * 说明：当评价计划创建完成后，进入评价任务发布阶段提交时调用该方法。
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveAssessorAndAssessResultBatch(String assessPlanId){
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanById(assessPlanId);
		removeAssessResultByAssessPlanId(assessPlanId);//批量删除评价结果信息
		removeAssessorByAssessPlanId(assessPlanId);//批量删除参评人信息
		List<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmpList = o_assessPlanRelaProcessBO.findAssessPlanProcessRelaOrgEmpBySome(assessPlanId, true, false, Contents.EMP_HANDLER, null, null);
		List<Assessor> assessorList = o_assessorBO.findAssessorListBySome(assessPlanId, null);//获得已经填加的参评人
		for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmpList) {
			SysEmployee emp = assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp();
			Assessor assessor = null;
			if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType())){
				//如果评价计划已存在评价参与人，不再插入
				for (Assessor assessorTmp : assessorList) {
					if(assessorTmp.getEmp().getId().equals(emp.getId())){
						assessor = assessorTmp;
						break;
					}
				}
				if(null == assessor){
					assessor = new Assessor(Identities.uuid2());
					assessor.setAssessPlan(assessPlan);
					assessor.setEmp(assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp());
					assessor.setDeadLine(assessPlan.getPlanEndDate());
					assessor.setStatus(Contents.STATUS_SAVED);
					mergeAssessor(assessor);//保存参评人信息
					
					assessorList.add(assessor);
				}
				
				AssessPlanRelaProcess assessPlanRelaProcess = assessPlanProcessRelaOrgEmp.getAssessPlanRelaProcess();
				Set<AssessPlanRelaPoint> assessPlanRelaPointSet = assessPlanRelaProcess.getAssessPlanRelaPoint();
				for(AssessPlanRelaPoint assessPlanRelaPoint:assessPlanRelaPointSet){
					AssessPoint assessPoint = assessPlanRelaPoint.getAssessPoint();
					Process process = assessPoint.getProcess();
					AssessResult assessResult = new AssessResult(Identities.uuid());
					assessResult.setAssessor(assessor);
					assessResult.setAssessPlan(assessPlan);
					assessResult.setAssessPoint(assessPoint);
					assessResult.setProcess(process);
					
					if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
						if(assessPlanRelaProcess.getId().equals(assessPlanRelaPoint.getAssessPlanRelaProcess().getId()) 
								&& null != assessPlanRelaPoint.getIsPracticeTest() && assessPlanRelaPoint.getIsPracticeTest()){//如果是穿行测试
							ProcessPoint processPoint = assessPoint.getProcessPoint();
							assessResult.setProcessPoint(processPoint);
							assessResult.setAssessMeasure(new DictEntry(Contents.ASSESS_MEASURE_PRACTICE_TEST));
							assessResult.setToExtractAmount(assessPlanRelaPoint.getPracticeNum());//穿行的次数
							mergeAssessResult(assessResult);
						}
					}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){
						if(assessPlanRelaProcess.getId().equals(assessPlanRelaPoint.getAssessPlanRelaProcess().getId()) 
								&& null != assessPlanRelaPoint.getIsSampleTest() && assessPlanRelaPoint.getIsSampleTest()){//如果是抽样测试
							Measure measure = assessPlanRelaPoint.getControlMeasure();
							assessResult.setControlMeasure(measure);
							assessResult.setAssessMeasure(new DictEntry(Contents.ASSESS_MEASURE_SAMPLE_TEST));
							mergeAssessResult(assessResult);
						}
					}else{
						if(assessPlanRelaProcess.getId().equals(assessPlanRelaPoint.getAssessPlanRelaProcess().getId()) 
								&& null != assessPlanRelaPoint.getIsPracticeTest() && assessPlanRelaPoint.getIsPracticeTest()){//如果是穿行测试
							ProcessPoint processPoint = assessPoint.getProcessPoint();
							assessResult.setProcessPoint(processPoint);
							assessResult.setAssessMeasure(new DictEntry(Contents.ASSESS_MEASURE_PRACTICE_TEST));
							assessResult.setToExtractAmount(assessPlanRelaPoint.getPracticeNum());//穿行的次数
							mergeAssessResult(assessResult);
						}else if(assessPlanRelaProcess.getId().equals(assessPlanRelaPoint.getAssessPlanRelaProcess().getId()) 
								&& null != assessPlanRelaPoint.getIsSampleTest() && assessPlanRelaPoint.getIsSampleTest()){//如果是抽样测试
							Measure measure = assessPlanRelaPoint.getControlMeasure();
							assessResult.setControlMeasure(measure);
							assessResult.setAssessMeasure(new DictEntry(Contents.ASSESS_MEASURE_SAMPLE_TEST));
							mergeAssessResult(assessResult);
						}
					}
				}
			}

		}
	}

	/**
	 * <pre>
	 * 批量初始化评价样本数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划
	 * @param assessResultId 评价结果数据
	 * @param 编号前缀
	 * @param sampleNum 随机数集合
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveAssessSampleBatch(String assessPlanId, String assessResultId, List<Integer> sampleNum, String sampletext){
		//首先判断当前流程是否进行过抽样测试，未测试则自动生成[抽样样本总数*抽样频率=结果]个样本
		List<AssessSample> assessSampleList = this.findAssessSampleListByAssessPlanIdAndAssessResultId(assessPlanId, assessResultId);
		if(null != assessSampleList && assessSampleList.size()>0){
			//进行过抽样测试，先删除已有的抽样测试样本
			this.removeSamplesByAssessPlanIdAndAssessResultId(assessPlanId, assessResultId);
		}
		for (int i = 0; i < sampleNum.size(); i++) {
			AssessSample assessSample = new AssessSample(Identities.uuid());
			assessSample.setAssessPlan(new AssessPlan(assessPlanId));
			assessSample.setAssessResult(new AssessResult(assessResultId));
			if(StringUtils.isNotBlank(sampletext)){
				assessSample.setCode(sampletext+sampleNum.get(i));
			}else{
				assessSample.setCode("ZD"+String.valueOf(sampleNum.get(i)));
			}
			//assessSample.setName("ZD"+String.valueOf(sampleNum.get(i)));
			assessSample.setType(Contents.SAMPLE_STATUS_AUTO);
			//assessSample.setIsQualified(Contents.IS_OK_Y);
			mergeAssessSample(assessSample);
		}
	}
	
	/**
	 * 根据评价计划id和评价结果id判断是否进行过穿行或者抽样测试.
	 * @author 吴德福
	 * @param assessPlanId 评价计划id
	 * @param assessResultId 评价结果id
	 * @return List<AssessSample>
	 */
	public List<AssessSample> findAssessSampleListByAssessPlanIdAndAssessResultId(String assessPlanId, String assessResultId){
		Criteria criteria = o_assessSampleDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("assessResult.id", assessResultId));
		return criteria.list();
	}
	
	/**
	 * 抽样测试时，根据评价计划id和评价结果id批量删除相应的测试样本.
	 * @param assessPlanId
	 * @param assessResultId
	 */
	@Transactional
	public void removeSamplesByAssessPlanIdAndAssessResultId(String assessPlanId, String assessResultId){
		o_assessSampleDAO.createQuery("delete AssessSample where assessPlan.id = :assessPlanId and assessResult.id = :assessResultId")
		.setParameter("assessPlanId", assessPlanId).setParameter("assessResultId", assessResultId).executeUpdate();
	}
	/**
	 * <pre>
	 *添加一个补充样本
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessSampleId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessSample(String assessSampleId){
		AssessSample assessSample=o_assessSampleDAO.get(assessSampleId);
		AssessSample newassessSample=new AssessSample(Identities.uuid());
		newassessSample.setSourceSample(new AssessSample(assessSampleId));
		newassessSample.setAssessPlan(assessSample.getAssessPlan());
		newassessSample.setAssessResult(assessSample.getAssessResult());
		newassessSample.setType(Contents.SAMPLE_STATUS_SUPPLEMENT);
		newassessSample.setIsQualified(Contents.IS_OK_Y);
		mergeAssessSample(newassessSample);
	}
	
	/**
	 * 根据样本id查询该样本是否存在补充样本.
	 * @param assessSampleId
	 * @return List<AssessSample>
	 */
	public List<AssessSample> findSupplementAssessSampleByAssessSampleId(String assessSampleId){
		Criteria criteria = o_assessSampleDAO.createCriteria();
		criteria.add(Restrictions.eq("type", Contents.SAMPLE_STATUS_SUPPLEMENT));
		criteria.add(Restrictions.eq("sourceSample.id", assessSampleId));
		return criteria.list();
	}
	
	/**
	 * 根据样本id集合批量删除样本.
	 * @param ids
	 */
	@Transactional
	public void removeSamplesByIds(String ids){
		StringBuilder sampleIds = new StringBuilder();
		String[] idArray = ids.split(",");
		for(int i=0;i<idArray.length;i++){
			sampleIds.append("'").append(idArray[i]).append("'");
			if(i!=idArray.length-1){
				sampleIds.append(",");
			}
		}
		o_assessSampleDAO.createQuery("delete AssessSample where id in ("+ sampleIds +")").executeUpdate();
	}
	/**
	 * 储存样本和附件的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param fileId
	 * @param assessSampleId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessSampleRelaFile(String fileId, String assessSampleId){
		//先删除该样本的附件
		removeAssessSampleRelaFile(assessSampleId);
		//上传该样本的附件
		AssessSampleRelaFile assessSampleRelaFile = new AssessSampleRelaFile();
		assessSampleRelaFile.setId(Identities.uuid());
		assessSampleRelaFile.setFile(new FileUploadEntity(fileId));
		assessSampleRelaFile.setAssessSample(new AssessSample(assessSampleId));
		o_assessSampleRelaFileDAO.merge(assessSampleRelaFile);
	}
	
	/**
	 * 根据样本id和附件id查询该样本的附件.
	 * @param fileId
	 * @param assessSampleId
	 * @return AssessSampleRelaFile
	 */
	public AssessSampleRelaFile findAssessSampleRelaFileByAssessSampleIdAndFileId(String fileId, String assessSampleId){
		AssessSampleRelaFile assessSampleRelaFile = null;
		Criteria criteria = o_assessSampleRelaFileDAO.createCriteria();
		criteria.add(Restrictions.eq("assessSample.id", assessSampleId));
		criteria.add(Restrictions.eq("file.id", fileId));
		List<AssessSampleRelaFile> list = criteria.list();
		if(null != list && list.size()>0){
			assessSampleRelaFile = list.get(0);
		}
		return assessSampleRelaFile;
	}
	
	/**
	 * <pre>
	 * 批量保存评价样本信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessResultId 评价结果ID
	 * @param jsonString 评价样本信息 [{assessSampleId:'dfdafe',isQualified:'NAN',code:'1235332',name:'2012年1月12日的审批单',comment:'未产生该单子'}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessSampleBatch(String assessResultId, String jsonString){
		boolean isHasDefect = true;
		List<AssessSample> assessSampleList = this.findAssessSampleListByAssessResultId(assessResultId);
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessSampleId = jsonObject.getString("assessSampleId");//评价结果ID
			String isQualified = jsonObject.getString("isQualified");//是否合格
			String code = jsonObject.getString("code");//样本编号
			String name = jsonObject.getString("name");//样本名称
			String comment = jsonObject.getString("comment");//说明
			for (AssessSample assessSample : assessSampleList) {
				if(StringUtils.isNotBlank(assessSampleId) && assessSampleId.equals(assessSample.getId())){
					if(Contents.IS_OK_N.equals(isQualified)){
						isHasDefect = false;
					}
					assessSample.setIsQualified(isQualified);
					if(StringUtils.isNotBlank(code) && !"null".equals(code)){
						assessSample.setCode(code);
					}
					if(StringUtils.isNotBlank(name) && !"null".equals(name)){
						assessSample.setName(name);
					}
					if(StringUtils.isNotBlank(comment)){
						assessSample.setComment(comment);
					}
					mergeAssessSample(assessSample);
				}else{
					if(Contents.IS_OK_N.equals(assessSample.getIsQualified())){
						isHasDefect = false;
					}
				}
			}
		}
		
		if(null != assessSampleList && assessSampleList.size()>0){
			AssessResult assessResult = this.findAssessResultById(assessResultId);
			if(null != assessResult){
				for (AssessSample assessSample : assessSampleList) {
					if(Contents.IS_OK_N.equals(assessSample.getIsQualified())){
						isHasDefect = false;
					}
				}
				assessResult.setHasDefect(isHasDefect);
				this.mergeAssessResult(assessResult);
			}
		}
	}
	
	/**
	 * 批量设置样本是否合格.
	 * @param jsonString
	 */
	@Transactional
	public void batchSetSampleIsQ(String jsonString, String isQualified){
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		if(jsonArray.size() == 0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessSampleId = jsonObject.getString("assessSampleId");//评价结果ID
			AssessSample assessSample = o_assessSampleDAO.get(assessSampleId);
			if(null != assessSample){
				if(StringUtils.isNotBlank(isQualified)){
					assessSample.setIsQualified(isQualified);
					o_assessSampleDAO.merge(assessSample);
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 保存评价样本
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessSample 评价样本
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessSample(AssessSample assessSample){
		o_assessSampleDAO.merge(assessSample);
	}
	
	/**
	 * <pre>
	 * 批量保存评价结果
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessorId 参评人
	 * @param jsonString 评价结果信息 [{assessResultId:'rew32ff',hasDefect:true,comment:'存在未审批的缺陷'},{...}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessResultBatch(String assessorId, String jsonString,String assessPlanId,String processId,String orgId){
		List<AssessResult> assessResultList = findAssessResultListBySome(assessorId, null);//查询该参评人评价的评价内容数据
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessResultId = jsonObject.getString("assessResultId");//评价结果ID
			//Boolean hasDefect = jsonObject.getBoolean("hasDefect");//是否存在缺陷
			Boolean hasDefectAdjust = null;
			if(StringUtils.isNotBlank(jsonObject.getString("hasDefectAdjust")) && !"null".equals(jsonObject.getString("hasDefectAdjust"))){
				hasDefectAdjust = jsonObject.getBoolean("hasDefectAdjust");//是否存在缺陷
			}
			String comment = jsonObject.getString("comment");//缺陷描述
			String adjustDesc=jsonObject.getString("adjustDesc");//补充说明
			String testType = jsonObject.getString("testType");//测试类型
			String assessPointId = jsonObject.getString("assessPointId");//评价点id
			String processPointId = "";
			String measureId = "";
			if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(testType)){
				processPointId=jsonObject.getString("processPointId");//流程节点ID
			}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(testType)){
				measureId=jsonObject.getString("measureId");//控制措施ID
			}
			for (AssessResult assessResult : assessResultList) {
				if(StringUtils.isNotBlank(assessResultId) && assessResultId.equals(assessResult.getId())){
					//assessResult.setHasDefect(hasDefect);
					assessResult.setHasDefectAdjust(hasDefectAdjust);
					
					/*
					if(null != hasDefectAdjust && !hasDefectAdjust){//前台验证，后台不验证
						//人为缺陷不为空
						assessResult.setComment(comment);
					}else if(!assessResult.getHasDefect() && StringUtils.isNotBlank(comment)){
						//人为缺陷为空，自动计算为否，缺陷描述必填
						assessResult.setComment(comment);
					}
					*/
					//缺陷描述
					if(StringUtils.isNotBlank(comment)){
						assessResult.setComment(comment);
					}else{
						assessResult.setComment(null);
						
					}
					/*
					if(null != assessResult.getHasDefect() && null != hasDefectAdjust && StringUtils.isNotBlank(adjustDesc)){//前台验证，后台不验证
						//人为调整与自动计算不一致时，补充说明必填
						if(assessResult.getHasDefect() != hasDefectAdjust){
							//补充说明
							assessResult.setAdjustDesc(adjustDesc);
						}
					}
					*/
					//补充说明
					if(StringUtils.isNotBlank(adjustDesc)){
						assessResult.setAdjustDesc(adjustDesc);
					}else{
						assessResult.setAdjustDesc(null);
					}
					
					mergeAssessResult(assessResult);
					
					if(null == hasDefectAdjust){
						//人为调整为空
						if(null != assessResult.getHasDefect() && !assessResult.getHasDefect()){
							//自动计算为'否'
							mergeAssessRelaDefect(comment,assessPlanId,processId,processPointId,measureId,testType,assessPointId,orgId);
						}
					}else{
						//人为调整不为空
						if(!hasDefectAdjust){
							//人为调整为'否'
							mergeAssessRelaDefect(comment,assessPlanId,processId,processPointId,measureId,testType,assessPointId,orgId);
						}else {
							//人为调整为'是'
							removeAssessRelaDefect(assessPlanId,processId,processPointId,measureId);
						}
					}
				}
			}
		}
	}

	/**
	 * 保存评价计划和缺陷的关联.
	 * @author 黄晨曦
	 * @modify 吴德福
	 * @param defectDesc
	 * @param assessPlanId
	 * @param processId
	 * @param processPointId
	 * @param measureId
	 * @param testType
	 * @param assessPointId 评价点id
	 * @param orgId
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	private void mergeAssessRelaDefect(String defectDesc,String assessPlanId,String processId,String processPointId,String measureId,String testType,String assessPointId,String orgId) {
		//先删除
		this.removeAssessRelaDefect(assessPlanId,processId,processPointId,measureId);
		//再添加
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("process.id", processId));
		if(StringUtils.isNotBlank(processPointId)){
			criteria.add(Restrictions.eq("processPoint.id", processPointId));
		}
		if(StringUtils.isNotBlank(measureId)){
			criteria.add(Restrictions.eq("controlMeasure.id", measureId));
		}
		AssessRelaDefect assessRelaDefect = null;
		List<AssessRelaDefect> list=criteria.list();
		if(null!=list && list.size()>0){
			assessRelaDefect=list.get(0);
			
			assessRelaDefect.getDefect().setDesc(defectDesc);
			o_defectDAO.merge(assessRelaDefect.getDefect());
		}else{
			assessRelaDefect=new AssessRelaDefect();
			assessRelaDefect.setId(Identities.uuid());
			
			assessRelaDefect.setAssessPlan(new AssessPlan(assessPlanId));
			assessRelaDefect.setProcess(new Process(processId));
			if(StringUtils.isNotBlank(processPointId)){
				assessRelaDefect.setProcessPoint(new ProcessPoint(processPointId));
			}
			if(StringUtils.isNotBlank(measureId)){
				assessRelaDefect.setControlMeasure(new Measure(measureId));
			}
			if(StringUtils.isNotBlank(assessPointId)){
				AssessPoint assessPoint = new AssessPoint();
				assessPoint.setId(assessPointId);
				assessRelaDefect.setAssessPoint(assessPoint);
			}
		
			Defect defect = new Defect(Identities.uuid());
			defect.setDesc(defectDesc);
			defect.setCompany(new SysOrganization(orgId));
			defect.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
			if(StringUtils.isNotBlank(testType)){
				DictEntry dictEntry = new DictEntry();
				if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(testType)){
					//穿行测试设计缺陷
					dictEntry.setId(Contents.DEFECT_TYPE_DESIGN);
					defect.setType(dictEntry);
				}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(testType)){
					//抽样测试执行缺陷
					dictEntry.setId(Contents.DEFECT_TYPE_EXECUTE);
					defect.setType(dictEntry);
				}
			}
			o_defectDAO.merge(defect);
			
			Criteria c = o_processRelaOrgDAO.createCriteria();
			c.add(Restrictions.eq("process.id", processId));
			c.add(Restrictions.eq("type", Contents.ORG_RESPONSIBILITY));
			List<ProcessRelaOrg> processRelaOrgList = c.list();
			if(null != processRelaOrgList && processRelaOrgList.size()>0){
				//删除缺陷关联责任部门
				o_defectRelaOrgDAO.createQuery("delete DefectRelaOrg dro where dro.defect.id=:defectId and dro.type=:type").setString("defectId", defect.getId()).setString("type", Contents.ORG_RESPONSIBILITY).executeUpdate();
				
				DefectRelaOrg defectRelaOrg = new DefectRelaOrg(Identities.uuid());
				defectRelaOrg.setDefect(defect);
				defectRelaOrg.setOrg(processRelaOrgList.get(0).getOrg());
				defectRelaOrg.setType(Contents.ORG_RESPONSIBILITY);
				o_defectRelaOrgDAO.merge(defectRelaOrg);
			}
			
			assessRelaDefect.setDefect(defect);
			o_assessRelaDefectDAO.merge(assessRelaDefect);
		}
	}

	/**
	 * <pre>
	 * 保存参评人实际完成日期和生成该参评人评价产生的缺陷数据
	 * 评价执行提交时调用
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessorId 参评人ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessorAndDefect(String assessorId){
		Assessor assessor = findAssessorById(assessorId);
		assessor.setActualFinishDate(new Date());
		mergeAssessor(assessor);
		List<AssessResult> assessResultList = findAssessResultListBySome(assessorId, true);//查询有缺陷的评价结果，并生成缺陷数据。
		for (AssessResult assessResult : assessResultList) {
			Defect defect = new Defect(Identities.uuid());
			defect.setDesc(assessResult.getComment());
			o_defectDAO.merge(defect);
		}
	}
	
	/**
	 * <pre>
	 * 保存参评人
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessor 参评人
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessor(Assessor assessor){
		o_assessorDAO.merge(assessor);
	}
	
	/**
	 * <pre>
	 * 保存评价结果
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessResult 评价结果
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessResult(AssessResult assessResult){
		o_assessResultDAO.merge(assessResult);
	}
	

	/**
	 * <pre>
	 * 删除缺陷与流程的关联表
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId
	 * @param processId
	 * @param processPointId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	private void removeAssessRelaDefect(String assessPlanId, String processId, String processPointId, String measureId) {
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("process.id", processId));
		if(StringUtils.isNotBlank(processPointId)){
			criteria.add(Restrictions.eq("processPoint.id", processPointId));
		}
		if(StringUtils.isNotBlank(measureId)){
			criteria.add(Restrictions.eq("controlMeasure.id", measureId));
		}
		List<AssessRelaDefect> list=criteria.list();
		if(null!=list && list.size()>0){
			for (AssessRelaDefect assessRelaDefect : list) {
				//删除评价结果与缺陷的关联表
				StringBuilder hql = new StringBuilder();
				hql.append("delete AssessRelaDefect assessRelaDefect where assessRelaDefect.assessPlan.id=:assessPlanId and assessRelaDefect.process.id =:processId ");
				if(StringUtils.isNotBlank(processPointId)){
					hql.append("and assessRelaDefect.processPoint.id=:processPointId ");
				}
				if(StringUtils.isNotBlank(measureId)){
					hql.append("and assessRelaDefect.controlMeasure.id=:controlMeasureId ");
				}
				Query query = o_assessRelaDefectDAO.createQuery(hql.toString());
				query.setString("assessPlanId", assessPlanId).setString("processId", processId);
				if(StringUtils.isNotBlank(processPointId)){
					query.setString("processPointId", processPointId);
				}
				if(StringUtils.isNotBlank(measureId)){
					query.setString("controlMeasureId",measureId);
				}
				query.executeUpdate();
				
				//删除缺陷与部门关联表
				o_defectRelaOrgDAO.createQuery("delete DefectRelaOrg defectRelaOrg where defectRelaOrg.defect.id=:defectId").setString("defectId", assessRelaDefect.getDefect().getId()).executeUpdate();
				
				//删除缺陷表
				o_defectDAO.createQuery("delete Defect defect where defect.id=:defectId").setString("defectId", assessRelaDefect.getDefect().getId()).executeUpdate();
			}
		}
	}
	
	/**
	 * <pre>
	 * 根据评价计划ID批量删除参评人信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessorByAssessPlanId(String assessPlanId){
		o_assessorDAO.createQuery("delete Assessor assessor where assessor.assessPlan.id=:assessPlanId")
		.setString("assessPlanId", assessPlanId).executeUpdate();
	}
	
	/**
	 * <pre>
	 * 根据参评人ID批量删除评价结果信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessorId 参评人ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessResultByAssessorId(String assessorId){
		o_assessResultDAO.createQuery("delete AssessResult assessResult where assessResult.assessor.id=:assessorId")
		.setString("assessorId", assessorId).executeUpdate();
	}
	
	/**
	 * <pre>
	 * 根据评价计划ID批量删除评价结果信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessResultByAssessPlanId(String assessPlanId){
		o_assessResultDAO.createQuery("delete AssessResult assessResult where assessResult.assessPlan.id=:assessPlanId")
		.setString("assessPlanId", assessPlanId).executeUpdate();
	}
	/**
	 * <pre>
	 *根据样本ID删除样本与文件的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessSampleId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessSampleRelaFile(String assessSampleId){
		o_assessSampleRelaFileDAO.createQuery("delete AssessSampleRelaFile assessSampleRelaFile where assessSampleRelaFile.assessSample.id=:assessSampleId")
		.setString("assessSampleId", assessSampleId).executeUpdate();
	}
	
	/**
	 * <pre>
	 * get方法
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessorId 参评人ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Assessor findAssessorById(String assessorId){
		return o_assessorDAO.get(assessorId);
	}
	
	/**
	 * <pre>
	 * 获取评价计划ID为assessPlanId的所有参评人的信息
	 * 查询条件模糊匹配员工姓名
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param page 分页类
	 * @param query 查询条件
	 * @param assessPlanId 评价计划ID
	 * @param assessorId 参评人ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<Assessor> findAssessorPageBySome(Page<Assessor> page, String query, String assessPlanId, String assessorId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Assessor.class);
		if(StringUtils.isNotBlank(assessPlanId)){
			detachedCriteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		}
		if(StringUtils.isNotBlank(query)){
			detachedCriteria.createAlias("emp", "emp").add(Restrictions.like("emp.empname", query,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(assessorId)){
			detachedCriteria.add(Restrictions.eq("id", assessorId));
		}
		return o_assessorDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 * 根据参评人ID获取评价的流程
	 * 查询条件模糊匹配流程名称和评价点名称
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @param assessorId 参评人ID
	 * @param processId 流程ID
	 * @param testType 评价方式：穿行测试（ca_assessment_measure_0）或者抽样测试（ca_assessment_measure_1）
	 * @param assessPlanId
	 * @param isAll 是否查询全部：not--待测试;all--全部;
	 * @return List<AssessResult>
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessResult> findAssessResultPageBySome(String query, String assessorId, String processId, String testType, String assessPlanId, String isAll){
		List<AssessResult> retList = new ArrayList<AssessResult>();
		
		Criteria criteria = o_assessResultDAO.createCriteria();
		criteria.add(Restrictions.eq("assessor.id", assessorId));
		criteria.add(Restrictions.eq("process.id", processId));
		criteria.createAlias("process", "process",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("assessPoint", "assessPoint",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("processPoint", "processPoint",CriteriaSpecification.LEFT_JOIN);
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("processPoint.name").like(query, MatchMode.ANYWHERE), Property.forName("assessPoint.desc").like(query, MatchMode.ANYWHERE)));
		}
		if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(testType)){
			criteria.add(Restrictions.eq("assessPoint.type", Contents.ASSESS_POINT_TYPE_DESIGN));
		}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(testType)){
			criteria.add(Restrictions.eq("assessPoint.type", Contents.ASSESS_POINT_TYPE_EXECUTE));
		}
		List<AssessResult> assessResultList = criteria.list();
		if(StringUtils.isNotBlank(isAll)){
			if("not".equals(isAll)){
				List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListByAssessPlanId(assessPlanId,processId);
				//待测试
				for (AssessResult assessResult : assessResultList) {
					if(!this.isNotTest(assessResult, assessRelaDefectList)){
						retList.add(assessResult);
					}
				}
			}else if("all".equals(isAll)){
				//全部
				retList = assessResultList;
			}
		}
		return retList;
	}
	
	/**
	 * 判断评价结果是否通过验证测试.
	 * @param assessResult
	 * @param assessRelaDefectList
	 * @return boolean true：未测试;false：已测试
	 */
	public boolean isNotTest(AssessResult assessResult, List<AssessRelaDefect> assessRelaDefectList){
		
		boolean isDefect = false;
		if(null == assessResult.getHasDefectAdjust()){
			//人为调整为空
			if(null != assessResult.getHasDefect()){
				if(!assessResult.getHasDefect()){
					//自动调整为否
					for(AssessRelaDefect assessRelaDefect : assessRelaDefectList){
						if(assessResult.getAssessPoint().getId().equals(assessRelaDefect.getAssessPoint().getId())){
							//存在缺陷，， 已通过验证，进行过测试
							isDefect = true;
							break;
						}
					}
				}else{
					//自动调整为是， 已通过验证，进行过测试
					isDefect = true;
				}
			}
		}else{
			//人为调整不为空
			if(!assessResult.getHasDefectAdjust()){
				//人为调整为否
				for(AssessRelaDefect assessRelaDefect : assessRelaDefectList){
					if(assessResult.getAssessPoint().getId().equals(assessRelaDefect.getAssessPoint().getId())){
						//存在缺陷
						isDefect = true;
						break;
					}
				}
				if(null != assessResult.getHasDefect() && assessResult.getHasDefect()!=assessResult.getHasDefectAdjust()){
					//自动计算与人为调整不一致
					if(StringUtils.isNotBlank(assessResult.getAdjustDesc())){
						//存在补充说明
						isDefect = true;
					}
				}
			}else{
				//人为调整为是
				if(null != assessResult.getHasDefect() && assessResult.getHasDefect()!=assessResult.getHasDefectAdjust()){
					//自动计算与人为调整不一致
					if(StringUtils.isNotBlank(assessResult.getAdjustDesc())){
						//存在补充说明
						isDefect = true;
					}
				}
			}
		}
		return isDefect;
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询评价结果
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessorId 参评人ID
	 * @param hasDefect 是否存在缺陷
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessResult> findAssessResultListBySome(String assessorId, Boolean hasDefect){
		Criteria criteria = o_assessResultDAO.createCriteria();
		criteria.add(Restrictions.eq("assessor.id", assessorId));
		if(null != hasDefect){
			criteria.add(Restrictions.eq("hasDefect", hasDefect));
		}
		return criteria.list();
	}
	/**
	 * <pre>
	 *根据样本Id查询样本和文件的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessSampleIds 样本Id字符串
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessSampleRelaFile> findAssessResultListByAssessSampleIds(List<String> assessSampleIds){
		if(null!=assessSampleIds&&assessSampleIds.size()>0){
			String hql = "from AssessSampleRelaFile assessSampleRelaFile where assessSampleRelaFile.assessSample.id in (:assessSampleIds)";
			Query query=o_assessSampleRelaFileDAO.createQuery(hql);
			query.setParameterList("assessSampleIds", assessSampleIds);
			return query.list();
		}else{
			return null;
		}
		
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询评价样本
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessResultId 评价结果ID
	 * @param isQualified 合格/不合格/不适用的常量值
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessSample> findAssessSampleListBySome(String assessResultId, String isQualified){
		Criteria criteria = o_assessSampleDAO.createCriteria();
		criteria.add(Restrictions.eq("assessResult.id", assessResultId));
		if(StringUtils.isNotBlank(isQualified)){
			criteria.add(Restrictions.eq("isQualified", isQualified));
		}
		criteria.addOrder(Order.asc("type"));
		criteria.addOrder(Order.asc("code"));
		return criteria.list();
	}
	
	/**
	 * 根据评价计划id和流程id查询样本列表.
	 * @param assessPlanId
	 * @param processId
	 * @param type 穿行--through;抽样--sampling;
	 * @param query 
	 * @return List<AssessSample>
	 */
	public List<AssessSample> findAssessSampleListByAssessPlanIdAndProcessId(String assessPlanId, String processId, String type, String query){
		Criteria criteria = o_assessSampleDAO.createCriteria();
		criteria.createAlias("assessResult", "ar");
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.or(Restrictions.eq("isQualified", Contents.IS_OK_Y), Restrictions.eq("isQualified", Contents.IS_OK_N)));
		//criteria.add(Restrictions.ne("isQualified", Contents.IS_OK_NAN));
		criteria.add(Restrictions.eq("ar.process.id", processId));
		if(StringUtils.isNotBlank(type)){
			if("through".equals(type)){
				criteria.add(Restrictions.eq("ar.assessMeasure.id", Contents.ASSESS_MEASURE_PRACTICE_TEST));
			}else if("sampling".equals(type)){
				criteria.add(Restrictions.eq("ar.assessMeasure.id", Contents.ASSESS_MEASURE_SAMPLE_TEST));
			}
		}
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		criteria.addOrder(Order.asc("ar.process.id"));
		criteria.addOrder(Order.asc("type"));
		criteria.addOrder(Order.asc("code"));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 *根据一些条件查询计划与流程的关联
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanRelaProcess> findAssessPlanRelaProcessBySome(String assessPlanId,String processId){
		Criteria criteria = o_assessPlanRelaProcessDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(processId)){
			criteria.add(Restrictions.eq("process.id", processId));
		}
		return criteria.list();
	}
	
	/**
	 * 根据评价计划id和流程id查询对应的评价结果.
	 * @author 吴德福
	 * @param assessPlanId
	 * @param processId
	 * @return List<AssessResult>
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessResult> findAssessResultByAssessPlanIdAndProcessId(String assessPlanId, String processId){
		Criteria criteria = o_assessResultDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(processId)){
			criteria.add(Restrictions.eq("process.id", processId));
		}
		return criteria.list();
	}
	
	/**
	 * <pre>
	 *查询样本数
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessResultList
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> findAssessSampleCount(List<AssessResult> assessResultList){
		Map<String,Object> assessResultMap= new HashMap<String, Object>();
		for(AssessResult assessResult:assessResultList){
			Map<String,Object> sampleMap=new HashMap<String, Object>();
			String assessResultId = assessResult.getId();
			long yCount = findAssessSampleCountBySome(assessResultId,Contents.IS_OK_Y);
			long nCount = findAssessSampleCountBySome(assessResultId,Contents.IS_OK_N);
			long NANCount = findAssessSampleCountBySome(assessResultId,Contents.IS_OK_NAN);
			sampleMap.put("Y", yCount);
			sampleMap.put("N", nCount);
			sampleMap.put("NAN", NANCount);
			assessResultMap.put(assessResult.getId(), sampleMap);
		}
		return assessResultMap;
	}
	
	/**
	 * 根据评价结果id查询是否存在样本.
	 * @author 吴德福
	 * @param assessResultId
	 * @return List<AssessSample>
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessSample> findAssessSampleListByAssessResultId(String assessResultId){
		Criteria criteria = o_assessSampleDAO.createCriteria();
		criteria.add(Restrictions.eq("assessResult.id", assessResultId));
		return criteria.list();
	}
	
	/**
	 * <pre>
	 *根据条件查询数目
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessResultId
	 * @param isQualified
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public long findAssessSampleCountBySome(String assessResultId,String isQualified){
		String hql="select count(id) from AssessSample assessSample where assessSample.assessResult.id=:assessResultId and isQualified=:isQualified";
		Query query = o_assessPlanRelaProcessDAO.createQuery(hql);
		query.setParameter("assessResultId", assessResultId);
		query.setParameter("isQualified", isQualified);
		List<Long> list=query.list();
		long count=(Long) list.get(0);//总记录数 
		return count;
	}

	/**
	 * 查询穿行测试的样本数
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param practiceIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Integer finPracticeTestSampleNum(String practiceIds) {
		String hql="select count(id) from AssessSample assessSample where assessSample.assessResult.id in (:assessResultId) and (assessSample.isQualified='Y' or assessSample.isQualified='N') ";
		Query query = o_assessSampleDAO.createQuery(hql);
		query.setParameterList("assessResultId", practiceIds.split(","));
		List<Long> list=query.list();
		Long l=(Long) list.get(0);
		Integer count = l.intValue();
		return count;
		
	}

	/**
	 * <pre>
	 *查询抽样测试的样本数
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param sampletestIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Integer findSampleTestSampleNum(String sampletestIds) {
		String hql="select count(id) from AssessSample assessSample where assessSample.assessResult.id in (:assessResultId) and (assessSample.isQualified='Y' or assessSample.isQualified='N') ";
		Query query = o_assessSampleDAO.createQuery(hql);
		query.setParameterList("assessResultId", sampletestIds.split(","));
		List<Long> list=query.list();
		Long l=(Long) list.get(0);
		Integer count = l.intValue();
		return count;
		
	}

	/**
	 * <pre>
	 *查询穿行测试合格样本数
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param practiceIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Integer findQualifiedPracticeTestSampleNum(String practiceIds) {
		String hql="select count(id) from AssessSample assessSample where assessSample.assessResult.id in (:assessResultId) and assessSample.isQualified=:isQualified";
		Query query = o_assessSampleDAO.createQuery(hql);
		query.setParameterList("assessResultId", practiceIds.split(","));
		query.setParameter("isQualified", Contents.IS_OK_Y);
		List<Long> list=query.list();
		Long l=(Long) list.get(0);
		Integer count = l.intValue();
		return count;
	}

	/**
	 * <pre>
	 *查询抽样测试合格样本数
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param sampletestIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Integer qualifiedSampleTestSampleNum(String sampletestIds) {
		String hql="select count(id) from AssessSample assessSample where assessSample.assessResult.id in (:assessResultId)  and assessSample.isQualified=:isQualified";
		Query query = o_assessSampleDAO.createQuery(hql);
		query.setParameterList("assessResultId", sampletestIds.split(","));
		query.setParameter("isQualified", Contents.IS_OK_Y);
		List<Long> list=query.list();
		Long l=(Long) list.get(0);
		Integer count = l.intValue();
		return count;
	}

	/**
	 * 根据id查询评价结果.
	 * @author 吴德福
	 * @param id
	 * @return AssessResult
	 */
	public AssessResult findAssessResultById(String id){
		return o_assessResultDAO.get(id);
	}
	
	/**
	 * 根据companyId查询所有的评价结果列表.
	 * @param companyId
	 * @return List<AssessResult>
	 */
	public List<AssessResult> findAssessResultListByCompanyId(String companyId){
		Criteria criteria = o_assessResultDAO.createCriteria();
		criteria.createAlias("assessPlan", "p");
		
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("p.company.id", companyId));
		}
		
		return criteria.list();
	}
}