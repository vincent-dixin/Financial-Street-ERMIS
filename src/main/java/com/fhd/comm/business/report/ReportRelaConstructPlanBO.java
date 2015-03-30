package com.fhd.comm.business.report;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.report.ReportRelaConstructPlanDAO;
import com.fhd.comm.entity.report.ReportRelaConstructPlan;

/**
 * 报告与评价计划关联表BO.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Service
@SuppressWarnings("unchecked")
public class ReportRelaConstructPlanBO {

	@Autowired
	private ReportRelaConstructPlanDAO o_reportRelaConstructPlanDAO;
	
	/**
	 * 新增报告与评价计划关联.
	 * @param reportRelaPlan
	 */
	@Transactional
	public void saveReportRelaAssessment(ReportRelaConstructPlan reportRelaConstructPlan){
		o_reportRelaConstructPlanDAO.merge(reportRelaConstructPlan);
	}
	/**
	 * 修改报告与评价计划关联.
	 * @param reportRelaPlan
	 */
	@Transactional
	public void mergeReportRelaConstructPlan(ReportRelaConstructPlan reportRelaConstructPlan){
		o_reportRelaConstructPlanDAO.merge(reportRelaConstructPlan);
	}
	/**
	 * 根据id删除报告与评价计划关联.
	 * @param id
	 */
	@Transactional
	public void removeReportRelaAssessmentById(String id){
		o_reportRelaConstructPlanDAO.delete(id);
	}
	/**
	 * 根据实体删除报告与评价计划关联关系.
	 * @param reportId
	 */
	@Transactional
	public void removeReportRelaAssessmentByEntity(ReportRelaConstructPlan reportRelaAssessment){
		o_reportRelaConstructPlanDAO.delete(reportRelaAssessment);
	}
	/**
	 * 根据报告ids集合批量删除报告与评价计划关联.
	 * @param reportIds
	 */
	@Transactional
	public void removeReportRelaConstructByReportIds(String reportIds){
		o_reportRelaConstructPlanDAO.createQuery("delete ReportRelaConstructPlan where report.id in (:ids)")
		.setParameterList("ids", StringUtils.split(reportIds,",")).executeUpdate();
	}
	/**
	 * <pre>
	 * 根据报告ID删除报告关联评价计划
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param reportId
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void removeReportRelaConstructPlanByReportId(String reportId) {
		o_reportRelaConstructPlanDAO.batchExecute("delete ReportRelaConstructPlan ra where ra.report.id = ?", reportId);
	}
	/**
	 * 根据id查询报告与评价计划关联.
	 * @param id
	 * @return ReportRelaPlan
	 */
	public ReportRelaConstructPlan findReportRelaConstructPlanById(String id){
		return o_reportRelaConstructPlanDAO.get(id);
	}
	/**
	 * 根据参数查询报告与评价计划关联.
	 * @param reportId
	 * @param type
	 * @return List<ReportRelaPlan>
	 */
	public List<ReportRelaConstructPlan> findReportRelaConstructPlanByParams(String reportId, String type){
		Criteria criteria = o_reportRelaConstructPlanDAO.createCriteria();
		
		if(StringUtils.isNotBlank(reportId)){
			criteria.add(Restrictions.eq("report.id", reportId));
		}
		if(StringUtils.isNotBlank(type)){
			criteria.add(Restrictions.eq("type", type));
		}
		return criteria.list();
	}
}