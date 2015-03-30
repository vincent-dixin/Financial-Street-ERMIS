package com.fhd.assess.business.formulatePlan;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.assess.dao.formulatePlan.RiskScoreRangeDAO;
import com.fhd.assess.entity.formulatePlan.RiskScoreRange;
import com.fhd.core.dao.Page;
/**
 * 打分范围BO
 * @author 王再冉
 *
 */
@Service
public class RiskScoreRangeBO {
	@Autowired
	private RiskScoreRangeDAO r_scoreRangeDAO;
	
	/**
	 * 根据id查打分范围实体
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RiskScoreRange findScoreRangeById(String id) {
		Criteria c = r_scoreRangeDAO.createCriteria();
		List<RiskScoreRange> list = null;
		if (StringUtils.isNotBlank(id)) {
			c.add(Restrictions.eq("id", id));
		}
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 保存打分范围
	 * @param scoreRange
	 */
	@Transactional
	public void saveRiskScoreRange(RiskScoreRange scoreRange) {
		r_scoreRangeDAO.merge(scoreRange);
	}
	/**
	 * 根据评估计划范围类型查询打分范围实体
	 * @param planId
	 * @param rangeType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskScoreRange> findScoreRangesPlanByPlanAndType(String planId, String rangeType) {
		Criteria c = r_scoreRangeDAO.createCriteria();
		List<RiskScoreRange> list = null;
		if (StringUtils.isNotBlank(planId)&&StringUtils.isNotBlank(rangeType)) {
			c.add(Restrictions.and(Restrictions.eq("rangeType", rangeType), Restrictions.eq("assessPlan.id", planId)));
		} else {
			return null;
		}
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	/**
	 * 删除打分范围实体
	 * @param rangeList
	 */
	@Transactional
	 public void removeRiskAssessPlansByIds(List<RiskScoreRange> rangeList) {
		 for(RiskScoreRange range : rangeList){
			 r_scoreRangeDAO.delete(range);
		 }
	}
	/**
	 * 查询打分范围
	 * @param rangeName
	 * @param page
	 * @param sort
	 * @param dir
	 * @param planId
	 * @param rangeType
	 * @return
	 */
	public Page<RiskScoreRange> findScoreRangesPageBySome(String rangeName, Page<RiskScoreRange> page, String sort, String dir, String planId, String rangeType) {
		DetachedCriteria dc = DetachedCriteria.forClass(RiskScoreRange.class);
		if(StringUtils.isNotBlank(rangeName)){
			dc.add(Property.forName("planName").like(rangeName,MatchMode.ANYWHERE));	
		}
		if(null != planId && null != rangeType){
			dc.add(Restrictions.and(Restrictions.eq("rangeType", rangeType), Restrictions.eq("assessPlan.id", planId)));
		}else{
			dc.add(Restrictions.and(Restrictions.eq("rangeType", ""), Restrictions.eq("assessPlan.id", "")));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return r_scoreRangeDAO.findPage(dc, page, false);
	}

}
