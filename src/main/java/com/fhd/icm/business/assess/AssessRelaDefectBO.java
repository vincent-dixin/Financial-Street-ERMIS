/**
 * AssessRelaDefectBO.java
 * com.fhd.icm.business.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-2-21 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.business.assess;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.dao.assess.AssessRelaDefectDAO;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-2-21		上午11:08:15
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class AssessRelaDefectBO {

	@Autowired
	private AssessRelaDefectDAO o_assessRelaDefectDAO;
	
	/**
	 * 分页查询评价计划关联缺陷列表.
	 * @param page
	 * @param assessPlanId 评价计划id
	 * @param processId 流程id
	 * @param query 查询条件
	 * @return Page<AssessRelaDefect>
	 */
	public Page<AssessRelaDefect> findAssessRelaDefectListByPage(Page<AssessRelaDefect> page, String assessPlanId, String processId, String query){
		DetachedCriteria dc = DetachedCriteria.forClass(AssessRelaDefect.class);
		dc.createAlias("defect", "d");
		dc.add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(processId)){
			dc.add(Restrictions.eq("process.id", processId));
		}
		if(StringUtils.isNotBlank(query)){
			dc.add(Restrictions.like("d.desc", query, MatchMode.ANYWHERE));
		}
		//dc.add(Restrictions.eq("d.deleteStatus", "1"));
		return o_assessRelaDefectDAO.findPage(dc, page, false);
	}
	
	/**
	 * <pre>
	 * 根据评价计划Id和流程id查询评价计划关联缺陷表.
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId
	 * @param processId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessRelaDefect> findAssessRelaDefectListByAssessPlanId(String assessPlanId, String processId){
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		if(StringUtils.isNotBlank(processId)){
			criteria.add(Restrictions.eq("process.id", processId));
		}
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询评价关联的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanIdList 评价计划ID的List
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessRelaDefect> findAssessRelaDefectListBySome(List<String> assessPlanIdList){
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.createAlias("defect", "d", CriteriaSpecification.LEFT_JOIN);
		if(null != assessPlanIdList && assessPlanIdList.size()>0){
			criteria.add(Restrictions.in("assessPlan.id", assessPlanIdList));
		}
		criteria.add(Restrictions.eq("d.dealStatus", Contents.DEAL_STATUS_NOTSTART));
		criteria.add(Restrictions.eq("d.status", Contents.STATUS_SUBMITTED));
		List<AssessRelaDefect> list = criteria.list();
		return list;
	}
	
	/**
	 * 缺陷反馈意见指保存.
	 * @author 吴德福
	 * @param jsonString
	 */
	@Transactional
	public void mergeDefectFeedback(String jsonString){
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String assessRelaDefectId = jsonObject.getString("assessRelaDefectId");//缺陷关联id
			String isAgree = jsonObject.getString("isAgree");//是否同意
			String feedback = jsonObject.getString("feedback");//缺陷级别
			
			if(StringUtils.isNotBlank(assessRelaDefectId)){
				AssessRelaDefect assessRelaDefect = o_assessRelaDefectDAO.get(assessRelaDefectId);
				if(null != assessRelaDefect){
					if(StringUtils.isNotBlank(isAgree)){
						DictEntry isAgreeEntry = new DictEntry();;
						isAgreeEntry.setId(isAgree);
						assessRelaDefect.setIsAgree(isAgreeEntry);
					}
					if(StringUtils.isNotBlank(feedback)){
						assessRelaDefect.setFeedback(feedback);
					}
				}
			}
		}
	}
	
	/**
	 * 根据评价计划id集合查询缺陷情况.
	 * @param assessPlanIds
	 * @return List<AssessRelaDefect>
	 */
	public List<AssessRelaDefect> findDefectRelaInfoByAssessPlanIds(String assessPlanIds){
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.add(Restrictions.in("assessPlan.id", StringUtils.split(assessPlanIds, ",")));
		criteria.addOrder(Order.asc("assessPlan.id"));
		return criteria.list();
	}
	
	/**
	 * 根据companyId查询所有的评价结果列表.
	 * @param companyId
	 * @return List<AssessRelaDefect>
	 */
	public List<AssessRelaDefect> findAssessRelaDefectListByCompanyId(String companyId){
		Criteria criteria = o_assessRelaDefectDAO.createCriteria();
		criteria.createAlias("assessPlan", "p");
		
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("p.company.id", companyId));
		}
		
		return criteria.list();
	}
}