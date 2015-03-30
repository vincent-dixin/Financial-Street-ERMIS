package com.fhd.assess.business.kpiSet;

import java.util.ArrayList;
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

import com.fhd.assess.business.formulatePlan.RiskScoreBO;
import com.fhd.assess.dao.formulatePlan.RiskScoreDeptDAO;
import com.fhd.assess.dao.formulatePlan.RiskScoreObjectDAO;
import com.fhd.assess.dao.kpiSet.ScoreObjectDeptEmpDAO;
import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.assess.entity.kpiSet.RangObjectDeptEmp;
import com.fhd.core.dao.Page;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
/**
 * 任务分配BO
 * @author 王再冉
 *
 */
@Service
public class AssessTaskBO {
	@Autowired
	private RiskScoreBO r_riskScoreBO;
	@Autowired
	private RiskScoreDeptDAO r_riskScoreDeptDAO;
	@Autowired
	private RiskScoreObjectDAO r_riskScoreObjDAO;
	@Autowired
	private ScoreObjectDeptEmpDAO r_objDeptEmpDAO;
	
	/**
	 * 查找当前登录用户所在部门的所有打分对象id
	 * @param empId
	 * @return
	 */
	public List<String> findUserDeptScoreObjIdsByempId(String empId) {
		List<SysEmpOrg> empOrgs = r_riskScoreBO.findEmpDeptsByEmpId(empId);
		List<RiskScoreDept> scoredeptList = new ArrayList<RiskScoreDept>();
		if(empOrgs.size()>0){
			for(SysEmpOrg empOrg : empOrgs){
				List<RiskScoreDept> depts = new ArrayList<RiskScoreDept>();
				depts = findRiskDeptsBydeptIds(empOrg.getSysOrganization().getId());
				if(depts.size()>0){
					scoredeptList.addAll(depts);
				}
			}
		}
		List<String> objIdList = new ArrayList<String>();//打分对象id
		for(RiskScoreDept dept : scoredeptList){
			objIdList.add(dept.getScoreObject().getId());
		}
		return objIdList;
	}
	/**
	 * 根据打分部门id查打分部门实体
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskScoreDept> findRiskDeptsBydeptIds(String deptId) {
		Criteria c = r_riskScoreDeptDAO.createCriteria();
		List<RiskScoreDept> list = null;
		if (StringUtils.isNotBlank(deptId)) {
			c.add(Restrictions.eq("organization.id", deptId));
		} 
		list = c.list();
		return list;
	}
	/**
	 * 任务分配列表查询
	 * @param objName
	 * @param page
	 * @param sort
	 * @param dir
	 * @param ObjIds
	 * @return
	 */
	public Page<RiskScoreObject> findScoreObjsPageByObjids(String objName, Page<RiskScoreObject> page, String sort, String dir, List<String> ObjIds, String planId) {
		DetachedCriteria dc = DetachedCriteria.forClass(RiskScoreObject.class);
		if(null != ObjIds && StringUtils.isNotBlank(planId)){
			dc.add(Restrictions.and(Restrictions.in("id", ObjIds), Restrictions.eq("assessPlan.id", planId)));
		}
		if(StringUtils.isNotBlank(objName)){
			dc.add(Property.forName("risk.name").like(objName,MatchMode.ANYWHERE));	
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return r_riskScoreObjDAO.findPage(dc, page, false);
	}
	/**
	 * 保存综合表（数据库）
	 * @param ode
	 */
	@Transactional
	public void saveObjectDeptEmp(RangObjectDeptEmp ode) {
		r_objDeptEmpDAO.merge(ode);
	}
	/**
	 * 打分部门打分对象相同的综合实体
	 * @param ode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RangObjectDeptEmp> findObjDeptEmpsByAll(RangObjectDeptEmp ode) {
		Criteria c = r_objDeptEmpDAO.createCriteria();
		List<RangObjectDeptEmp> list = null;
		if (null != ode) {
			c.add(Restrictions.and(Restrictions.eq("scoreObject.id", ode.getScoreObject().getId()),
									Restrictions.eq("scoreDept.id", ode.getScoreDept().getId())));
		} 
		list = c.list();
		return list;
	}
	/**
	 * 删除对象-部门-人员实体
	 * @param ode
	 */
	@Transactional
	public void deleteObjectDeptEmp(RangObjectDeptEmp ode) {
		r_objDeptEmpDAO.delete(ode);
	}
	
}
