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

import com.fhd.assess.dao.formulatePlan.RiskAccessPlanDAO;
import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.assess.interfaces.formulatePlan.IRiskAssessPlanBO;
import com.fhd.core.dao.Page;
import com.fhd.risk.dao.TemplateDAO;
import com.fhd.risk.entity.Template;

/**
 * 计划任务BO
 * @author 王再冉
 *
 */
@Service
public class RiskAssessPlanBO implements IRiskAssessPlanBO{
	@Autowired
	private RiskAccessPlanDAO r_planDAO;
	@Autowired
	private TemplateDAO o_templateDAO;
	
	/**
	 * 查询计划任务列表
	 * @param planName	计划任务名称
	 * @param page
	 * @param sort
	 * @param dir
	 * @param companyId	公司id
	 * @return
	 */
	public Page<RiskAssessPlan> findPlansPageBySome(String planName, Page<RiskAssessPlan> page, String sort, String dir, String companyId) {
		DetachedCriteria dc = DetachedCriteria.forClass(RiskAssessPlan.class);
		if(null != companyId){
			dc.add(Restrictions.eq("company.id", companyId));
		}
		if(StringUtils.isNotBlank(planName)){
			dc.add(Property.forName("planName").like(planName,MatchMode.ANYWHERE));	
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return r_planDAO.findPage(dc, page, false);
	}
	/**
	 * 保存计划
	 * @param riskPlan
	 */
	@Transactional
	public void saveRiskAssessPlan(RiskAssessPlan riskPlan) {
		 r_planDAO.merge(riskPlan);
		 
	}
	/**
	 * 更新计划
	 * @param riskPlan
	 */
	@Transactional
	public void mergeRiskAssessPlan(RiskAssessPlan riskPlan) {
		 r_planDAO.merge(riskPlan);
		 
	}
	/**
	 * 根据计划任务id查询计划任务
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RiskAssessPlan findRiskAssessPlanById(String id) {
		Criteria c = r_planDAO.createCriteria();
		List<RiskAssessPlan> list = null;
		
		if (StringUtils.isNotBlank(id)) {
			c.add(Restrictions.eq("id", id));
		} else {
			return null;
		}
		
		list = c.list();
		return list.get(0);
	}
	/**
	 * 批量删除计划任务
	 * @param ids
	 */
	 @Transactional
	 public void removeRiskAssessPlansByIds(List<String> ids) {
		 for(String id : ids){
			 r_planDAO.delete(id);
		 }
	}
	/**
	 * 根据模板名称查询模板
	 * @param tempName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Template findTemplateById(String tempId){
		Criteria criteria = o_templateDAO.createCriteria();
		List<Template> tempList = null;
		if(StringUtils.isNotBlank(tempId)){
			criteria.add(Restrictions.eq("id", tempId));
		}
		tempList = criteria.list();
		if (tempList.size() > 0) {
			return tempList.get(0);
		} else {
			return null;
		}
	}

}
