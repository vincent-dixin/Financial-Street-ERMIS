/**
 * ImprovePlanRelaDefectBO.java
 * com.fhd.icm.business.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-4 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.business.rectify;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.icm.dao.rectify.ImprovePlanRelaDefectDAO;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-4		上午10:52:43
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class ImprovePlanRelaDefectBO {
	@Autowired
	private ImprovePlanRelaDefectDAO o_improvePlanRelaDefectDAO;
	
	/**
	 * <pre>
	 * 保存整改方案关联缺陷关联信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanRelaDefect 整改方案关联缺陷关联信息
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeImprovePlanRelaDefect(ImprovePlanRelaDefect improvePlanRelaDefect){
		o_improvePlanRelaDefectDAO.merge(improvePlanRelaDefect);
	}
	
	/**
	 * <pre>
	 * 查询
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanRelaDefectId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public ImprovePlanRelaDefect findImprovePlanRelaDefectById(String improvePlanRelaDefectId){
		return o_improvePlanRelaDefectDAO.get(improvePlanRelaDefectId);
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询整改方案关联缺陷关联信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectIdSet 缺陷ID的Set集合
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<ImprovePlanRelaDefect> findImprovePlanRelaDefectListBySome(Set<String> defectIdSet){
		Criteria criteria = o_improvePlanRelaDefectDAO.createCriteria();
		criteria.createAlias("improvePlan", "improvePlan", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("defect", "defect", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("defect.defectRelaOrg", "dro", CriteriaSpecification.LEFT_JOIN);
		if(null != defectIdSet && defectIdSet.size()>0){
			criteria.add(Restrictions.in("defect.id", defectIdSet));
		}
		List<ImprovePlanRelaDefect> list = criteria.list();
		return list;
	}
	
	/**
	 * 根据评价计划id查询该评价计划下的缺陷整改情况信息.
	 * @param assessplanIds
	 * @return List<Object[]>
	 */
	public List<Object[]> findDefectImproveInfoByAssessplanId(String assessplanIds){
		List<Object[]> list = null;
		/*
		select td.edesc,tip.econtent,t.compensation_control,td.deal_status,t.id,t.after_defect_level,t.create_time 
		from t_rectify_defect_trace t 
		left join t_rectify_defect_improve tdi on t.defect_improve_id = tdi.id 
		left join t_rectify_improve_plan tip on tip.id = tdi.improvement_id
		left join t_ca_defect_assessment tda on tda.defect_id=tdi.defect_id
		left join t_ca_defect td on td.id= tdi.defect_id 
		left join t_ca_assessment_plan tap on tda.plan_id=tap.id 
		where tda.plan_id in('a7366a6a-f90d-415b-87fc-2a7e0409e250','3c5943c0-aa8f-44a9-a383-01a034ed9fe2','3cb6ca18-a213-4afc-85a1-a59799b75152')
		group by td.edesc,tip.econtent,t.compensation_control,td.deal_status,td.id,t.after_defect_level
		having t.create_time = max(t.create_time)
		order by tap.id
		*/
		
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append("select td.edesc,tip.econtent,t.compensation_control,td.deal_status,t.id,t.after_defect_level,t.create_time ")
			.append("from t_rectify_defect_trace t ")
			.append("left join t_rectify_defect_improve tdi on t.defect_improve_id = tdi.id ")
			.append("left join t_rectify_improve_plan tip on tip.id = tdi.improvement_id ")
			.append("left join t_ca_defect_assessment tda on tda.defect_id=tdi.defect_id ")
			.append("left join t_ca_defect td on td.id= tdi.defect_id ")
			.append("left join t_ca_assessment_plan tap on tda.plan_id=tap.id ")
			.append("where 1=1 ");
		
		
		if(StringUtils.isNotBlank(assessplanIds)){
			sqlBuffer.append("and tda.plan_id in (:ids) ");
		}
		
		sqlBuffer.append("group by td.edesc,tip.econtent,t.compensation_control,td.deal_status,td.id,t.after_defect_level ")
		.append("having t.create_time = max(t.create_time) ")
		.append("order by tap.id ");
		SQLQuery sqlQuery = o_improvePlanRelaDefectDAO.createSQLQuery(sqlBuffer.toString());
		if(StringUtils.isNotBlank(assessplanIds)){
			list = sqlQuery.setParameterList("ids", StringUtils.split(assessplanIds,",")).list();
		}
		
		return list;
	}
}