package com.fhd.comm.business.formula;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.formula.FormulaLogDAO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.comm.entity.formula.FormulaLog;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 公式计算日志BO.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2012   2012-12-8       上午10:48:52
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class FormulaLogBO {

	@Autowired
	private FormulaLogDAO o_formulaLogDAO;
	
	/**
	 * 新增公式计算日志.
	 * @param formulaLog
	 */
	@Transactional
	public void saveFormulaLog(FormulaLog formulaLog){
		o_formulaLogDAO.merge(formulaLog);
	}
	/**
	 * 修改公式计算日志.
	 * @param formulaLog
	 */
	@Transactional
	public void mergeFormulaLog(FormulaLog formulaLog){
		o_formulaLogDAO.merge(formulaLog);
	}
	/**
	 * 根据id删除公式计算日志.
	 * @param id
	 */
	@Transactional
	public void removeFormulaLogById(String id){
		//逻辑删除
		/*
		FormulaLog formulaLog = this.findFormulaLogById(id);
		formulaLog.setDeleteStatus(true);
		this.mergeFormulaLog(formulaLog);
		*/
		//物理删除
		o_formulaLogDAO.delete(id);
	}
	/**
	 * 批量删除公式日志.
	 * @param ids 公式日志id集合
	 */
	@Transactional
	public void removeFormulaLogByIds(String ids){
		StringBuilder formulaLogIds = new StringBuilder();
		String[] idArray = ids.split(",");
		for(int i=0;i<idArray.length;i++){
			formulaLogIds.append("'").append(idArray[i]).append("'");
			if(i!=idArray.length-1){
				formulaLogIds.append(",");
			}
		}
		o_formulaLogDAO.createQuery("delete FormulaLog where id in ("+ formulaLogIds +")").executeUpdate();
	}
	/**
	 * 根据id查询公式计算日志.
	 * @param id
	 * @return FormulaLog
	 */
	public FormulaLog findFormulaLogById(String id){
		return o_formulaLogDAO.get(id);
	}
	/**
	 * 根据查询条件分页查询公式计算日志.
	 * @param page
	 * @param sort
	 * @param dir
	 * @param query 查询条件
	 * @return Page<FormulaLog>
	 */
	public Page<FormulaLog> findFormulaLogListByPage(Page<FormulaLog> page, String sort, String dir, String query){
		DetachedCriteria dc = DetachedCriteria.forClass(FormulaLog.class);
		if(StringUtils.isNotBlank(query)){
			dc.add(Restrictions.like("objectName", query, MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq("deleteStatus", false));
		dc.addOrder(Order.desc("calculateDate"));
		return o_formulaLogDAO.findPage(dc, page, false);
	}
	/**
	 * 根据查询条件查询公式计算日志.
	 * @param objectName 对象名称
	 * @param objectType 对象类型
	 * @return List<FormulaLog>
	 */
	public List<FormulaLog> findFormulaLogListBySome(String objectName, String objectType){
		Criteria criteria = o_formulaLogDAO.createCriteria();
		if(StringUtils.isNotBlank(objectName)){
			criteria.add(Restrictions.like("objectName", objectName, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(objectType)){
			criteria.add(Restrictions.eq("objectType", objectType));
		}
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		return criteria.list();
	}
	/**
	 * 封装公式日志实体.
	 * @param formulaLog
	 * @param objectId 对象id
	 * @param objectName 对象名称
	 * @param objectType 对象类型
	 * @param objectColumn 对象所属列
	 * @param formula 公式内容
	 * @param failureType 成功/失败类型
	 * @param failureReason 成功/失败原因
	 * @param timePeriod 区间维度
	 * @param emp 所属人
	 * @return FormulaLog
	 */
	public FormulaLog packageFormulaLog(FormulaLog formulaLog, String objectId, String objectName, String objectType, String objectColumn, String formula, String failureType, String failureReason, TimePeriod timePeriod, SysEmployee emp){
		formulaLog.setId(Identities.uuid2());
		formulaLog.setObjectId(objectId);
		formulaLog.setObjectName(objectName);
		formulaLog.setObjectType(objectType);
		formulaLog.setObjectColumn(objectColumn);
		formulaLog.setFormulaContent(formula);
		formulaLog.setFailureType(failureType);
		StringBuilder sb = new StringBuilder();
		String column = "";
		if(Contents.TARGET_VALUE_FORMULA.equals(objectColumn)){
			column = "指标目标值公式";
    	}else if(Contents.RESULT_VALUE_FORMULA.equals(objectColumn)){
    		column = "指标实际值公式";
    	}else if(Contents.ASSESSMENT_VALUE_FORMULA.equals(objectColumn)){
    		column = "指标评估值公式";
    	}else if(Contents.IMPACTS_FORMULA.equals(objectColumn)){
    		column = "风险影响程度公式";
    	}else if(Contents.PROBABILITY_FORMULA.equals(objectColumn)){
    		column = "风险发生可能性公式";
    	}else if(Contents.SM_ASSESSMENT_VALUE_FORMULA.equals(objectColumn)){
            column = "战略目标评估值公式";
        }else if(Contents.SC_ASSESSMENT_VALUE_FORMULA.equals(objectColumn)){
            column = "记分卡评估值公式";
        }
		if(Contents.OPERATE_FAILURE.equals(failureType)){
			sb.append("'").append(objectName).append("'").append("计算").append("'").append(column).append("'").append("失败").append(", 原因:").append(failureReason);
		}else if(Contents.OPERATE_SUCCESS.equals(failureType)){
			sb.append("'").append(objectName).append("'").append("计算").append("'").append(column).append("'").append("成功").append(", 结果:").append(failureReason);;
		}
		formulaLog.setFailureReason(sb.toString());
		formulaLog.setTimePeriod(timePeriod);
		formulaLog.setDeleteStatus(false);
		formulaLog.setCalculateDate(new Date());
		formulaLog.setEmp(emp);
		return formulaLog;
	}
}
