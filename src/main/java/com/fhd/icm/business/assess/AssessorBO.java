package com.fhd.icm.business.assess;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.icm.dao.assess.AssessorDAO;
import com.fhd.icm.entity.assess.Assessor;

/**
 * 评价点参与人BO.
 * @author 吴德福
 * @version
 * @since Ver 1.1
 * @Date 2013-3-20 下午22:28:25
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class AssessorBO {

	@Autowired
	private AssessorDAO o_assessorDAO;
	
	/**
	 * 新增评价点参与人.
	 * @param assessor
	 */
	@Transactional
	public void saveAssessor(Assessor assessor){
		o_assessorDAO.merge(assessor);
	}
	
	/**
	 * 修改评价点参与人.
	 * @param assessor
	 */
	@Transactional
	public void mergeAssessor(Assessor assessor){
		o_assessorDAO.merge( assessor);
	}
	
	/**
	 * 根据id查询评价点参与人.
	 * @param id
	 * @return Assessor
	 */
	public Assessor findAssessorById(String id){
		return o_assessorDAO.get(id);
	}
	
	/**
	 * 根据查询条件查询评价点参与人.
	 * @param assessPlanId
	 * @param operaterId
	 * @return Assessor
	 */
	public Assessor findAssessorBySome(String assessPlanId, String operaterId){
		Criteria criteria = o_assessorDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("emp.id", operaterId));
		List<Assessor> list = criteria.list();
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 根据评价计划查询参评人.
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId 评价计划ID
	 * @param empId 参评人对应的人员ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Assessor> findAssessorListBySome(String assessPlanId, String empId){
		Criteria criteria = o_assessorDAO.createCriteria(); 
		if(StringUtils.isNotBlank(assessPlanId)){
			criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		}
		if(StringUtils.isNotBlank(empId)){
			criteria.add(Restrictions.eq("emp.id", empId));
		}
		return criteria.list();
	}
}
