/**
 * ImprovePlanBO.java
 * com.fhd.icm.business.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-4 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.business.rectify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.rectify.DefectTraceDAO;
import com.fhd.icm.dao.rectify.ImprovePlanDAO;
import com.fhd.icm.dao.rectify.ImprovePlanFileDAO;
import com.fhd.icm.dao.rectify.ImprovePlanRelaOrgDAO;
import com.fhd.icm.dao.rectify.ImproveRelaPlanDAO;
import com.fhd.icm.dao.rectify.ImproveTraceDAO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.rectify.DefectTrace;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.icm.entity.rectify.ImprovePlan;
import com.fhd.icm.entity.rectify.ImprovePlanFile;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;
import com.fhd.icm.entity.rectify.ImprovePlanRelaOrg;
import com.fhd.icm.entity.rectify.ImproveRelaPlan;
import com.fhd.icm.entity.rectify.ImproveTrace;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-4		下午12:54:39
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ImprovePlanBO {
	@Autowired
	private ImprovePlanDAO o_improvePlanDAO;
	
	@Autowired
	private ImproveRelaPlanDAO o_improveRelaPlanDAO;
	
	@Autowired
	private ImprovePlanRelaOrgDAO o_improvePlanRelaOrgDAO;
	
	@Autowired
	private ImprovePlanFileDAO o_improvePlanFileDAO;
	
	@Autowired
	private ImproveTraceDAO o_improveTraceDAO;
	
	@Autowired
	private DefectTraceDAO o_defectTraceDAO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	
	/**
	 * <pre>
	 * 保存进度跟进
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveTrace
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveImproveTrace(ImproveTrace improveTrace){
		improveTrace.setId(Identities.uuid2());
		o_improveTraceDAO.merge(improveTrace);
	}
	
	/**
	 * <pre>
	 * 初始化整改方案的数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveId 整改计划ID
	 * @param defectId 对应的缺陷ID
	 * @param companyId 所属的公司ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveImprovePlan(String improveId,String defectId,String companyId){
		String improvePlanId = Identities.uuid2();
		ImprovePlan improvePlanNew = new ImprovePlan(improvePlanId);
		improvePlanNew.setCompany(new SysOrganization(companyId));
		mergeImprovePlan(improvePlanNew);
		String improvePlanRelaDefectId = Identities.uuid2();
		ImprovePlanRelaDefect improvePlanRelaDefectNew = new ImprovePlanRelaDefect(improvePlanRelaDefectId);
		improvePlanRelaDefectNew.setDefect(new Defect(defectId));
		improvePlanRelaDefectNew.setImprovePlan(improvePlanNew);
		o_improvePlanRelaDefectBO.mergeImprovePlanRelaDefect(improvePlanRelaDefectNew);
		ImproveRelaPlan improveRelaPlan = new ImproveRelaPlan(Identities.uuid2());
		improveRelaPlan.setImprove(new Improve(improveId));
		improveRelaPlan.setImprovePlan(improvePlanNew);
		mergeImproveRelaPlan(improveRelaPlan);
	}
	
	/**
	 * <pre>
	 * 保存整改方案
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlan 整改方案
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlan(ImprovePlan improvePlan){
		o_improvePlanDAO.merge(improvePlan);
	}
	
	/**
	 * <pre>
	 * 保存整改计划和整改方案关联信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveRelaPlan 整改计划和整改方案关联信息
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImproveRelaPlan(ImproveRelaPlan improveRelaPlan){
		o_improveRelaPlanDAO.merge(improveRelaPlan);
	}
	
	/**
	 * <pre>
	 * 保存整改方案关联的部门人员信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveRelaPlanOrg
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlanRelaOrg(ImprovePlanRelaOrg improveRelaPlanOrg){
		o_improvePlanRelaOrgDAO.merge(improveRelaPlanOrg);
	}
	
	/**
	 * <pre>
	 * 保存整改缺陷复核
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectTrace
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeDefectTrace(DefectTrace defectTrace){
		o_defectTraceDAO.merge(defectTrace);
	}
	
	/**
	 * <pre>
	 * 批量保存评价方案信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveId 评价计划ID
	 * @param jsonString 要保存的评价方案的信息
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlanBatch(String improveId,String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");//ImprovePlan的ID
			String content = jsonObject.getString("content");
			String planStartDateString = jsonObject.getString("planStartDate");
			String planEndDateString = jsonObject.getString("planEndDate");
			Date planStartDate = null;
			if(StringUtils.isNotBlank(planStartDateString)){
				planStartDate = DateUtils.stringToDateToDay(planStartDateString);
			}
			Date planEndDate = null;
			if(StringUtils.isNotBlank(planEndDateString)){
				planEndDate = DateUtils.stringToDateToDay(planEndDateString);
			}
			
			ImprovePlan improvePlan = null;
			if(StringUtils.isNotBlank(id)){
				improvePlan = findImprovePlanById(id);
			}else{
				return;
			}
			if(null != planStartDate){
				improvePlan.setPlanStartDate(planStartDate);
			}
			if(null != planEndDate){
				improvePlan.setPlanEndDate(planEndDate);
			}
			improvePlan.setContent(content);
			improvePlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
			mergeImprovePlan(improvePlan);
		}
		
	}
	
	/**
	 * <pre>
	 * 保存整改方案的复核人信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param empId 复核人员ID
	 * @param improvePlanIds 整改方案的ID串，多个以‘,’隔开
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlanSetReviewerBatch(String empId, String improvePlanIds){
		
		if(StringUtils.isNotBlank(improvePlanIds)){
			String[] improvePlanIdArray = improvePlanIds.split(",");
			List<String> improvePlanIdList = new ArrayList<String>();
			for (String improvePlanId : improvePlanIdArray) {
				improvePlanIdList.add(improvePlanId);
			}
			if(null != improvePlanIdList && improvePlanIdList.size()>0){//先删除已经保存的整改方案的复核人
				removeImprovePlanRelaOrgByImprovePlanIdList(improvePlanIdList);
			}
			for (String improvePlanId : improvePlanIdArray) {
				ImprovePlanRelaOrg improvePlanRelaOrg = new ImprovePlanRelaOrg(Identities.uuid2());
				improvePlanRelaOrg.setImprovePlan(new ImprovePlan(improvePlanId));
				improvePlanRelaOrg.setEmp(new SysEmployee(empId));
				improvePlanRelaOrg.setType(Contents.EMP_REVIEW_PERSON);
				mergeImprovePlanRelaOrg(improvePlanRelaOrg);
			}
		}
	}
	
	/**
	 * <pre>
	 * 批量删除整改方案关联部门人员信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanIdList 整改方案ID组成的List
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeImprovePlanRelaOrgByImprovePlanIdList(List<String> improvePlanIdList){
		if(null !=improvePlanIdList && improvePlanIdList.size()>0){
			StringBuffer hql = new StringBuffer();
			hql.append("delete ImprovePlanRelaOrg ipro where ipro.improvePlan.id in('").append(StringUtils.join(improvePlanIdList,"','")).append("')");
			o_improvePlanRelaOrgDAO.createQuery(hql.toString()).executeUpdate();
		}
	}
	
	/**
	 * <pre>
	 * 根据整改方案ID查询整改方案
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanId 整改方案ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public ImprovePlan findImprovePlanById(String improvePlanId){
		return o_improvePlanDAO.get(improvePlanId);
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询整改方案关联的相关附件
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanIdSet 整改方案Id的Set集合
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImprovePlanFile> findImprovePlanFileListBySome(Set<String> improvePlanIdSet){
		Criteria criteria = o_improvePlanFileDAO.createCriteria();
		criteria.createAlias("file", "file",CriteriaSpecification.LEFT_JOIN);
		if(null != improvePlanIdSet && improvePlanIdSet.size()>0){
			criteria.add(Restrictions.in("improvePlan.id", improvePlanIdSet));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		List<ImprovePlanFile> improvePlanFileList = criteria.list();
		return improvePlanFileList;
	}
	
	/**
	 * <pre>
	 * 查询整改进度跟踪
	 * </pre>
	 * 
	 * @author 李克东，张雷
	 * @param improvePlanId 整改方案的ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImproveTrace> findImproveTraceBySome(String  improvePlanId){
		Criteria criteria = o_improveTraceDAO.createCriteria();
		criteria.add(Restrictions.eq("improvePlan.id", improvePlanId));
		criteria.addOrder(Order.desc("createTime"));
		List<ImproveTrace> improveTraceList = criteria.list();
		return improveTraceList;
	}
	
	/**
	 * <pre>
	 * 查询整改复核情况
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanRelaDefectId 整改方案与缺陷关联实体ID
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<DefectTrace> findDefectTraceBySome(String  improvePlanRelaDefectId, String improveId){
		Criteria criteria = o_defectTraceDAO.createCriteria();
		criteria.createAlias("improvePlanRelaDefect", "improvePlanRelaDefect", CriteriaSpecification.LEFT_JOIN);
		if(StringUtils.isNotBlank(improveId)){
			criteria.createAlias("improvePlanRelaDefect.defect", "defect", CriteriaSpecification.LEFT_JOIN);
			criteria.createAlias("defect.defectRelaImprove", "defectRelaImprove", CriteriaSpecification.LEFT_JOIN);
			criteria.createAlias("defectRelaImprove.improve", "improve", CriteriaSpecification.LEFT_JOIN);
			criteria.add(Restrictions.eq("improve.id", improveId));
		}
		if(StringUtils.isNotBlank(improvePlanRelaDefectId)){
			
			criteria.add(Restrictions.eq("improvePlanRelaDefect.id", improvePlanRelaDefectId));
		}
		criteria.addOrder(Order.desc("createTime"));
		List<DefectTrace> defectTraceTraceList = criteria.list();
		return defectTraceTraceList;
	}
	
	/**
	 * <pre>
	 * 根据一些条件查找整改计划关联整改方案表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improveId 整改计划ID
	 * @param improvePlanId 整改方案ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImproveRelaPlan> findImproveRelaPlanListBySome(String improveId, String improvePlanId){
		Criteria criteria = o_improveRelaPlanDAO.createCriteria();
		if(StringUtils.isNotBlank(improveId)){
			criteria.add(Restrictions.eq("improve.id", improveId));
		}
		if(StringUtils.isNotBlank(improvePlanId)){
			criteria.add(Restrictions.eq("improvePlan.id", improvePlanId));
		}
		
		return criteria.list();
	}
	
	public List<ImprovePlan> findImprovePlanListBySome(String companyId, String improveId){
		Criteria criteria = o_improvePlanDAO.createCriteria();
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		criteria.createAlias("improveRelaPlan", "irp", CriteriaSpecification.LEFT_JOIN)
		.createAlias("irp.improve", "i", CriteriaSpecification.LEFT_JOIN)
		.add(Restrictions.isNotNull("i.dealStatus"));
		if(StringUtils.isNotBlank(improveId)){
			criteria.add(Restrictions.eq("i.id", improveId));
		}
		
		return criteria.list();
	}
}

