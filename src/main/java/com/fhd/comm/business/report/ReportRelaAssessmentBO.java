package com.fhd.comm.business.report;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.report.ReportRelaAssessmentDAO;
import com.fhd.comm.entity.report.ReportRelaAssessment;

/**
 * 报告与评价计划关联表BO.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Service
@SuppressWarnings("unchecked")
public class ReportRelaAssessmentBO {

	@Autowired
	private ReportRelaAssessmentDAO o_reportRelaAssessmentDAO;
	
	/**
	 * 新增报告与评价计划关联.
	 * @param reportRelaPlan
	 */
	@Transactional
	public void saveReportRelaAssessment(ReportRelaAssessment reportRelaAssessment){
		o_reportRelaAssessmentDAO.merge(reportRelaAssessment);
	}
	/**
	 * 修改报告与评价计划关联.
	 * @param reportRelaPlan
	 */
	@Transactional
	public void mergeReportRelaAssessment(ReportRelaAssessment reportRelaAssessment){
		o_reportRelaAssessmentDAO.merge(reportRelaAssessment);
	}
	/**
	 * 根据id删除报告与评价计划关联.
	 * @param id
	 */
	@Transactional
	public void removeReportRelaAssessmentById(String id){
		o_reportRelaAssessmentDAO.delete(id);
	}
	/**
	 * 根据实体删除报告与评价计划关联关系.
	 * @param reportId
	 */
	@Transactional
	public void removeReportRelaAssessmentByEntity(ReportRelaAssessment reportRelaAssessment){
		o_reportRelaAssessmentDAO.delete(reportRelaAssessment);
	}
	/**
	 * 根据报告ids集合批量删除报告与评价计划关联.
	 * @param reportIds
	 */
	@Transactional
	public void removeReportRelaAssessmentByReportIds(String reportIds){
		o_reportRelaAssessmentDAO.createQuery("delete ReportRelaAssessment where report.id in (:ids)")
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
	public void removeReportRelaAssessmentByReportId(String reportId) {
		o_reportRelaAssessmentDAO.batchExecute("delete ReportRelaAssessment ra where ra.report.id = ?", reportId);
	}
	/**
	 * 根据id查询报告与评价计划关联.
	 * @param id
	 * @return ReportRelaPlan
	 */
	public ReportRelaAssessment findReportRelaAssessmentById(String id){
		return o_reportRelaAssessmentDAO.get(id);
	}
	/**
	 * 根据参数查询报告与评价计划关联.
	 * @param reportId
	 * @param type
	 * @return List<ReportRelaPlan>
	 */
	public List<ReportRelaAssessment> findReportRelaAssessmentByParams(String reportId, String type){
		Criteria criteria = o_reportRelaAssessmentDAO.createCriteria();
		
		if(StringUtils.isNotBlank(reportId)){
			criteria.add(Restrictions.eq("assessmentReport.id", reportId));
		}
		if(StringUtils.isNotBlank(type)){
			criteria.add(Restrictions.eq("type", type));
		}
		return criteria.list();
	}
}