package com.fhd.icm.business.assess;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
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
import com.fhd.icm.dao.assess.AssessPlanPointRelaOrgEmpDAO;
import com.fhd.icm.dao.assess.AssessPlanRelaPointDAO;
import com.fhd.icm.dao.assess.AssessPlanRelaProcessDAO;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanPointRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaPoint;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.process.entity.Process;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-24		上午11:44:05
 *
 * @see 	 
 */
@SuppressWarnings("unchecked")
@Service
public class AssessPlanRelaPointBO {
	
	@Autowired
	private AssessPlanBO o_assessPlanBO;
	
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	
	@Autowired
	private AssessPlanRelaProcessDAO o_assessPlanRelaProcessDAO;
	
	@Autowired
	private ProcessDAO o_processDAO;
	
	@Autowired
	private AssessPlanRelaPointDAO o_assessPlanRelaPointDAO;
	
	@Autowired
	private AssessPlanPointRelaOrgEmpDAO o_assessPlanPointRelaOrgEmpDAO;
	
	/**
	 * <pre>
	 * 根据评价计划ID批量生成评价关联的评价点数据
	 * </pre>
	 * @author 张雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveAssessPlanRelaPointBatch(String assessPlanId){
		AssessPlan assessPlan = o_assessPlanBO.findAssessPlanByAssessPlanId(assessPlanId);
		removeAssessPlanRelaPoinByAssessPlanId(assessPlanId);//先批量删除
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByAssessPlanId(assessPlanId);//查询评价计划ID为assessPlanId的所有的评价的流程范围
		for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
			Process process = assessPlanRelaProcess.getProcess();
			if(null != process){
				String processId = assessPlanRelaProcess.getProcess().getId();
				if(StringUtils.isNotBlank(processId)){
					List<AssessPoint> assessPointList = o_assessPlanBO.findAssessPointListBySome(processId);//查询流程ID为processId的所有的评价点范围
					for (AssessPoint assessPoint : assessPointList) {
						AssessPlanRelaPoint assessPlanRelaPoint = new AssessPlanRelaPoint(Identities.uuid());
						assessPlanRelaPoint.setAssessPlanRelaProcess(assessPlanRelaProcess);
						assessPlanRelaPoint.setAssessPoint(assessPoint);
						assessPlanRelaPoint.setProcess(assessPoint.getProcess());
						//是否穿行测试，是否抽样测试，穿行几次，抽取多少样本
						if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessPlan.getAssessMeasure().getId())){
							if(Contents.ASSESS_POINT_TYPE_DESIGN.equals(assessPoint.getType())){
								assessPlanRelaPoint.setIsPracticeTest(assessPlanRelaProcess.getIsPracticeTest());
								assessPlanRelaPoint.setPracticeNum(assessPlanRelaProcess.getPracticeNum());//穿行次数
								assessPlanRelaPoint.setProcessPoint(assessPoint.getProcessPoint());
								o_assessPlanRelaPointDAO.merge(assessPlanRelaPoint);
							}
						}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessPlan.getAssessMeasure().getId())){
							if(Contents.ASSESS_POINT_TYPE_EXECUTE.equals(assessPoint.getType())){
								assessPlanRelaPoint.setControlMeasure(assessPoint.getControlMeasure());
								assessPlanRelaPoint.setIsSampleTest(assessPlanRelaProcess.getIsSampleTest());
								assessPlanRelaPoint.setCoverageRate(assessPlanRelaProcess.getCoverageRate());//抽样比例
								o_assessPlanRelaPointDAO.merge(assessPlanRelaPoint);
							}
						}else{
							if(Contents.ASSESS_POINT_TYPE_DESIGN.equals(assessPoint.getType())){
								assessPlanRelaPoint.setIsPracticeTest(assessPlanRelaProcess.getIsPracticeTest());
								assessPlanRelaPoint.setPracticeNum(assessPlanRelaProcess.getPracticeNum());//穿行次数
								assessPlanRelaPoint.setProcessPoint(assessPoint.getProcessPoint());
								o_assessPlanRelaPointDAO.merge(assessPlanRelaPoint);
							}else if(Contents.ASSESS_POINT_TYPE_EXECUTE.equals(assessPoint.getType())){
								assessPlanRelaPoint.setControlMeasure(assessPoint.getControlMeasure());
								assessPlanRelaPoint.setIsSampleTest(assessPlanRelaProcess.getIsSampleTest());
								assessPlanRelaPoint.setCoverageRate(assessPlanRelaProcess.getCoverageRate());//抽样比例
								o_assessPlanRelaPointDAO.merge(assessPlanRelaPoint);
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * <pre>
	 * 根据评价流程的评价人和复核人，生成评价评价点的评价人和复核人数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	public void mergeAssessPlanPointRelaOrgEmpFromAssessPlanProcessRelaOrgEmpBatch(String assessPlanId){
		Criteria criteria = o_assessPlanRelaPointDAO.createCriteria();
		criteria.createAlias("assessPlanRelaProcess", "assessPlanRelaProcess")
		.createAlias("assessPlanRelaProcess.assessPlan", "assessPlan").add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.createAlias("assessPlanRelaProcess.assessPlanProcessRelaOrgEmp", "assessPlanProcessRelaOrgEmp", CriteriaSpecification.INNER_JOIN);
		criteria.createAlias("assessPlanProcessRelaOrgEmp.assessPlanRelaOrgEmp", "assessPlanRelaOrgEmp", CriteriaSpecification.INNER_JOIN);
		criteria.setProjection(Projections.projectionList().add(Projections.property("assessPlan.id").as("assessPlanId")).add(Projections.property("assessPlanRelaOrgEmp.id"))
				.add(Projections.property("id")).add(Projections.property("assessPlanProcessRelaOrgEmp.type")));
		List<Object[]> objectArrayList = criteria.list();
		for (Object[] objects : objectArrayList) {
			AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp = new AssessPlanPointRelaOrgEmp(Identities.uuid());
			assessPlanPointRelaOrgEmp.setAssessPlan(new AssessPlan(String.valueOf(objects[0])));
			assessPlanPointRelaOrgEmp.setAssessPlanRelaOrgEmp(new AssessPlanRelaOrgEmp(String.valueOf(objects[1])));
			assessPlanPointRelaOrgEmp.setAssessPlanRelaPoint(new AssessPlanRelaPoint(String.valueOf(objects[2])));
			assessPlanPointRelaOrgEmp.setType(String.valueOf(objects[3]));
			mergeAssessPlanPointRelaOrgEmp(assessPlanPointRelaOrgEmp);
		}
	}

	/**
	 * <pre>
	 * 保存评价点关联的评价人和复核人等
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanPointRelaOrgEmp 评价点关联的评价人和复核人
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanPointRelaOrgEmp(AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp){
		o_assessPlanPointRelaOrgEmpDAO.merge(assessPlanPointRelaOrgEmp);
	}
	
	/**
	 * <pre>
	 * 批量保存参与评价的人：
	 * 按流程批量保存参与评价的人的信息，当batchType为process，时，例如：[{processId:'23311',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]；
	 * 按部门批量保存参与评价的人的信息，当batchType为org时，例如：[{orgId:'23311',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * handlerId和reviewerId分别为assessPlanRelaOrgEmp的ID
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param jsonString 保存信息的字符串
	 * @param batchType 批量保持的依据：process,org
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergerAssessPlanPointRelaOrgEmpBatch(String assessPlanId,String jsonString, String batchType){
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
			if(Contents.BATCHTYPE_PROCESS.equals(batchType)){//按流程查询
				String processId = jsonObject.getString("processId");//流程ID
				assessPlanRelaProcessCriteria.add(Restrictions.eq("process.id", processId));
			}else if(Contents.BATCHTYPE_ORG.equals(batchType)){//按流程的责任部门查询
				String orgId = jsonObject.getString("orgId");//部门ID
				Criteria processCriteria = o_processDAO.createCriteria();
				processCriteria.createAlias("processRelaOrg", "processRelaOrg").createAlias("processRelaOrg.org", "org").add(Restrictions.eq("org.id", orgId));
				processCriteria.add(Restrictions.eq("processRelaOrg.type", Contents.ORG_RESPONSIBILITY));
				processCriteria.setProjection(Projections.property("id"));
				assessPlanRelaProcessCriteria.add(Property.forName("process.id").in(processCriteria.list()));
			}
			List<AssessPlanRelaPoint> assessPlanRelaPointList = new ArrayList<AssessPlanRelaPoint>();
			List<AssessPlanRelaProcess> assessPlanRelaProcessList = assessPlanRelaProcessCriteria.list();
			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {//
				assessPlanRelaPointList.addAll(assessPlanRelaProcess.getAssessPlanRelaPoint());
			}
			List<String> assessPlanRelaPointIdList = new ArrayList<String>();
			for (AssessPlanRelaPoint assessPlanRelaPoint : assessPlanRelaPointList) {
				assessPlanRelaPointIdList.add(assessPlanRelaPoint.getId());
			}
			if(null != assessPlanRelaPointIdList && 0<assessPlanRelaPointIdList.size()){
				removeAssessPlanPointRelaOrgEmpByAssessPlanRelaPointIdList(assessPlanRelaPointIdList);//保存之前先删除原有的实施评价的评价人和复核人
			}
			for (AssessPlanRelaPoint assessPlanRelaPoint : assessPlanRelaPointList) {
				String assessPlanRelaPointId = assessPlanRelaPoint.getId();
				AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp = new AssessPlanPointRelaOrgEmp(Identities.uuid());
				assessPlanPointRelaOrgEmp.setAssessPlan(new AssessPlan(assessPlanId));
				assessPlanPointRelaOrgEmp.setAssessPlanRelaPoint(new AssessPlanRelaPoint(assessPlanRelaPointId));
				assessPlanPointRelaOrgEmp.setAssessPlanRelaOrgEmp(new AssessPlanRelaOrgEmp(handlerId));
				assessPlanPointRelaOrgEmp.setType(Contents.EMP_HANDLER);
				mergeAssessPlanPointRelaOrgEmp(assessPlanPointRelaOrgEmp);
				AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmpReviewer = new AssessPlanPointRelaOrgEmp(Identities.uuid());
				assessPlanPointRelaOrgEmpReviewer.setAssessPlan(new AssessPlan(assessPlanId));
				assessPlanPointRelaOrgEmpReviewer.setAssessPlanRelaPoint(new AssessPlanRelaPoint(assessPlanRelaPointId));
				assessPlanPointRelaOrgEmpReviewer.setAssessPlanRelaOrgEmp(new AssessPlanRelaOrgEmp(reviewerId));
				assessPlanPointRelaOrgEmpReviewer.setType(Contents.EMP_REVIEW_PERSON);
				mergeAssessPlanPointRelaOrgEmp(assessPlanPointRelaOrgEmpReviewer);
			}
		}
	}
	
	/**
	 * <pre>
	 * 保存实施评价计划ID为assessPlanId的计划的评价点的评价人信息
	 * handlerId和reviewerId分别为assessPlanRelaOrgEmp的ID
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 保存人员信息的字符串，例如：[{assessPlanRelaPointId:'11111',handlerId:'dfasdf',reviewerId:'dfasdf'},{...}]
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeAssessPlanPointRelaOrgEmpBatch(String jsonString) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessPlanRelaPointId = jsonObject.getString("assessPlanRelaPointId");//评价计划关联的评价点ID
			String handlerId = jsonObject.getString("handlerId");//评价人
			String reviewerId = jsonObject.getString("reviewerId");//复核人
			removeAssessPlanPointRelaOrgEmpByAssessPlanRelaPointId(assessPlanRelaPointId);//保存之前先删除原有的实施评价的评价人和复核人
			AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp = new AssessPlanPointRelaOrgEmp(Identities.uuid());
			assessPlanPointRelaOrgEmp.setAssessPlanRelaPoint(new AssessPlanRelaPoint(assessPlanRelaPointId));
			assessPlanPointRelaOrgEmp.setAssessPlanRelaOrgEmp(new AssessPlanRelaOrgEmp(handlerId));
			assessPlanPointRelaOrgEmp.setType(Contents.EMP_HANDLER);
			mergeAssessPlanPointRelaOrgEmp(assessPlanPointRelaOrgEmp);
			AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmpReviewer = new AssessPlanPointRelaOrgEmp(Identities.uuid());
			assessPlanPointRelaOrgEmpReviewer.setAssessPlanRelaPoint(new AssessPlanRelaPoint(assessPlanRelaPointId));
			assessPlanPointRelaOrgEmpReviewer.setAssessPlanRelaOrgEmp(new AssessPlanRelaOrgEmp(reviewerId));
			assessPlanPointRelaOrgEmpReviewer.setType(Contents.EMP_REVIEW_PERSON);
			mergeAssessPlanPointRelaOrgEmp(assessPlanPointRelaOrgEmpReviewer);
		}
	}
	
	/**
	 * <pre>
	 * 物理删除评价关联评价点的ID为assessPlanRelaPointId的评价人的信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaPointId 评价关联评价点的ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanPointRelaOrgEmpByAssessPlanRelaPointId(String assessPlanRelaPointId){
		o_assessPlanPointRelaOrgEmpDAO.createQuery("delete AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp where assessPlanPointRelaOrgEmp.assessPlanRelaPoint.id=:assessPlanRelaPointId")
		.setString("assessPlanRelaPointId", assessPlanRelaPointId)
		.executeUpdate();
	}
	
	/**
	 * 根据评价计划ID批量删除计划评价点信息
	 * @param assessPlanId 评价计划ID
	 */
	@Transactional
	public void removeAssessPlanRelaPoinByAssessPlanId(String assessPlanId){
		o_assessPlanPointRelaOrgEmpDAO.createQuery("delete AssessPlanRelaPoint aprp  where aprp.assessPlanRelaProcess.id in (select aprpro.id from AssessPlanRelaProcess aprpro where aprpro.assessPlan.id=:assessPlanId)").setString("assessPlanId", assessPlanId).executeUpdate();
	}
	
	/**
	 * <pre>
	 * 物理删除评价关联评价点的ID为assessPlanRelaPointId的评价人的信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanRelaPointIdList 评价关联评价点的ID的List
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeAssessPlanPointRelaOrgEmpByAssessPlanRelaPointIdList(List<String> assessPlanRelaPointIdList){
		if(null !=assessPlanRelaPointIdList && assessPlanRelaPointIdList.size()>0){
			StringBuffer hql = new StringBuffer();
			hql.append("delete AssessPlanPointRelaOrgEmp approe where approe.assessPlanRelaPoint.id in('").append(StringUtils.join(assessPlanRelaPointIdList,"','")).append("')");
			o_assessPlanPointRelaOrgEmpDAO.createQuery(hql.toString()).executeUpdate();
		}
	}
	
	/**
	 * <pre>
	 * 查询评价计划关联的评价点
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param page 分页类
	 * @param query 查询条件模糊匹配流程名称和评价点名称
	 * @param assessPlanId 评价计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<AssessPlanRelaPoint> findAssessPlanRelaPointListByPage(Page<AssessPlanRelaPoint> page, String query, String assessPlanId,String assessPlanRelaProcessId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssessPlanRelaPoint.class);
		detachedCriteria.createAlias("assessPlanRelaProcess", "assessPlanRelaProcess");
		detachedCriteria.createAlias("assessPlanRelaProcess.process", "process");
		detachedCriteria.createAlias("assessPlanRelaProcess.assessPlan", "assessPlan");
		
		if(StringUtils.isNotBlank(assessPlanId)){//ID为assessPlanId的评价计划关联的评价点
			detachedCriteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		}
		if(StringUtils.isNotBlank(assessPlanRelaProcessId)){//ID为assessPlanRelaProcessId的评价相关流程关联的评价点
			detachedCriteria.add(Restrictions.eq("assessPlanRelaProcess.id", assessPlanRelaProcessId));
		}
		if(StringUtils.isNotBlank(query)){
			detachedCriteria.add(Restrictions.or(Property.forName("process.name").like(query, MatchMode.ANYWHERE), Property.forName("assessPoint.name").like(query, MatchMode.ANYWHERE)));
		}
		return o_assessPlanRelaPointDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询评价点相关的部门或人
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param empIsNotNull 员工ID不为空
	 * @param orgIsNotNull 部门ID不为空
	 * @param returnType 参与部门,经办人，复核人的常量
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanPointRelaOrgEmp> findAssessPlanPointRelaOrgEmpBySome(String assessPlanId, Boolean empIsNotNull,Boolean orgIsNotNull, String returnType){
		Criteria criteria = o_assessPlanPointRelaOrgEmpDAO.createCriteria();
		criteria.createAlias("assessPlanRelaPoint", "assessPlanRelaPoint").createAlias("assessPlanRelaPoint.assessPlanRelaProcess", "assessPlanRelaProcess")
		.createAlias("assessPlanRelaProcess.assessPlan", "assessPlan").add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(returnType)){
			criteria.add(Restrictions.eq("type", returnType));
		}
		if(empIsNotNull){
			criteria.createAlias("assessPlanRelaOrgEmp", "assessPlanRelaOrgEmp").createAlias("assessPlanRelaOrgEmp.emp", "emp").add(Restrictions.isNotNull("emp.id"));
		}
		if(orgIsNotNull){
			criteria.createAlias("assessPlanRelaOrgEmp", "assessPlanRelaOrgEmp").createAlias("assessPlanRelaOrgEmp.org", "org").add(Restrictions.isNotNull("org.id"));
		}
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询评价计划关联的评价点数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanRelaPoint> findAssessPlanRelaPointListBySome(String assessPlanId){
		Criteria criteria = o_assessPlanRelaPointDAO.createCriteria();
		criteria.createAlias("assessPlanRelaProcess", "assessPlanRelaProcess")
		.createAlias("assessPlanRelaProcess.assessPlan", "assessPlan").add(Restrictions.eq("assessPlan.id", assessPlanId));
		return criteria.list();
	}
}

