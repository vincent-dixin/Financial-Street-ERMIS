package com.fhd.icm.business.defect;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.icm.dao.defect.DefectRelaImproveDAO;
import com.fhd.icm.entity.defect.DefectRelaImprove;

/**
 * 整改关联缺陷的增删改查的逻辑方法
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-2		下午7:31:51
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class DefectRelaImproveBO {
	@Autowired
	private DefectRelaImproveDAO o_defectRelaImproveDAO;
	
	/**
	 * <pre>
	 * 保存整改涉及的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectRelaImprove 要保存的整改涉及的缺陷
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergeDefectRelaImprove(DefectRelaImprove defectRelaImprove){
		o_defectRelaImproveDAO.merge(defectRelaImprove);
	}
	
	/**
	 * <pre>
	 * 删除整改涉及的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectRelaImprove 要删除的整改涉及的缺陷
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeDefectRelaImprove(DefectRelaImprove defectRelaImprove){
		o_defectRelaImproveDAO.delete(defectRelaImprove);
	}
	
	/**
	 * <pre>
	 * 删除整改涉及的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectRelaImprove 要删除的整改涉及的缺陷的ID
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeDefectRelaImprove(String defectRelaImproveId){
		o_defectRelaImproveDAO.delete(defectRelaImproveId);
	}
	
	/**
	 * <pre>
	 * 分页查询整改关联的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param page 分页类
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<DefectRelaImprove> findDefectRelaImprovePageBySome(Page<DefectRelaImprove> page,String improveId) {
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(DefectRelaImprove.class);
		detachedCriteria.createAlias("defect", "d", CriteriaSpecification.LEFT_JOIN);
		if(StringUtils.isNotBlank(improveId)){
			detachedCriteria.add(Restrictions.eq("improve.id", improveId));
		}
		detachedCriteria.addOrder(Order.desc("d.id"));
		return o_defectRelaImproveDAO.findPage(detachedCriteria, page, false);
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询整改涉及的缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectId 缺陷ID
	 * @param improveId 整改计划ID
	 * @param empId 缺陷责任部门下的人员
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<DefectRelaImprove> findDefectRelaImproveListBySome(String defectId,String improveId){
		Criteria criteria = o_defectRelaImproveDAO.createCriteria();
		if(StringUtils.isNotBlank(defectId)){
			criteria.add(Restrictions.eq("defect.id", defectId));
		}
		if(StringUtils.isNotBlank(improveId)){
			criteria.add(Restrictions.eq("improve.id", improveId));
		}
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * 根据一些条件查询整改计划相关的缺陷的整改责任部门的人员Id的集合信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectId 缺陷ID
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<String> findEmpIdListBySome(String defectId, String improveId){
		Criteria criteria = o_defectRelaImproveDAO.createCriteria();
		criteria.createAlias("defect", "defect", CriteriaSpecification.LEFT_JOIN)
		.createAlias("defect.defectRelaOrg", "defectRelaOrg", CriteriaSpecification.LEFT_JOIN)
		.createAlias("defectRelaOrg.org", "org", CriteriaSpecification.LEFT_JOIN)
		.createAlias("org.sysEmpOrgs", "sysEmpOrg", CriteriaSpecification.LEFT_JOIN)
		.createAlias("sysEmpOrg.sysEmployee", "emp", CriteriaSpecification.LEFT_JOIN);
		criteria.setProjection(Projections.property("emp.id"));
		if(StringUtils.isNotBlank(defectId)){
			criteria.add(Restrictions.eq("defect.id", defectId));
		}
		if(StringUtils.isNotBlank(improveId)){
			criteria.add(Restrictions.eq("improve.id", improveId));
		}
		return criteria.list();
	}
	
}

