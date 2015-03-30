package com.fhd.assess.business.formulatePlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.fhd.assess.dao.formulatePlan.RiskScoreDeptDAO;
import com.fhd.assess.dao.formulatePlan.RiskScoreObjectDAO;
import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.assess.interfaces.formulatePlan.IRiskScoreBO;
import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.dao.Page;
import com.fhd.risk.dao.RiskOrgDAO;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.sys.business.autho.SysoRoleBO;
import com.fhd.sys.dao.organization.SysOrgEmpDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
/**
 * 打分对象，打分部门BO类
 * @author 王再冉
 *
 */
@Service
public class RiskScoreBO implements IRiskScoreBO{
	@Autowired
	private RiskOrgDAO r_riskOrgDAO;
	@Autowired
	private RiskScoreObjectDAO r_riskScoreObjDAO;
	@Autowired
	private RiskScoreDeptDAO r_riskScoreDeptDAO;
	@Autowired
	private SysoRoleBO o_sysRoleBO;
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	@Autowired
	private SysOrgEmpDAO o_sysOrgEmpDAO;
	@Autowired
	private JBPMBO o_jbpmBO;
	
	/**
	 * 查询部门id下所有部门风险实体
	 * @param orgIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskOrg> findRiskOrgsById(String orgIds) {
		Criteria c = r_riskOrgDAO.createCriteria();
		List<RiskOrg> list = null;
		List<String> idList = new ArrayList<String>();
		if (StringUtils.isNotBlank(orgIds)) {
			  String[] idArray = orgIds.split(",");
			  for (String id : idArray) {
					  idList.add(id);
			  }
			  c.add(Restrictions.in("sysOrganization.id", idList));
		}
		list = c.list();
		return list;
	}
	/**
	 * 查询评估风险列表
	 * @param objName
	 * @param page
	 * @param sort
	 * @param dir
	 * @param planId
	 * @return
	 */
	public Page<RiskScoreObject> findScoreObjsPageBySome(String objName, Page<RiskScoreObject> page, String sort, String dir, String planId) {
		DetachedCriteria dc = DetachedCriteria.forClass(RiskScoreObject.class);
		if(null != planId){
			dc.add(Restrictions.eq("assessPlan.id", planId));
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
	 * 根据打分对象id查询打分部门实体
	 * @param ObjId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskScoreDept> findRiskDeptByObjId(String ObjId) {
		Criteria c = r_riskScoreDeptDAO.createCriteria();
		List<RiskScoreDept> list = null;
		if (StringUtils.isNotBlank(ObjId)) {
			c.add(Restrictions.eq("scoreObject.id", ObjId));
		} else {
			return null;
		}
		list = c.list();
		return list;
	}
	/**
	 * 根据风险id查询所有关联部门
	 * @param riskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskOrg> findRiskOrgsByriskId(String riskId) {
		Criteria c = r_riskOrgDAO.createCriteria();
		List<RiskOrg> list = null;
		if (StringUtils.isNotBlank(riskId)) {
			c.add(Restrictions.eq("risk.id", riskId));
		} 
		list = c.list();
		return list;
	}
	/**
	 * 判断打分部门中是否存在该部门
	 * @param ObjId
	 * @param orgId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskScoreDept> findRiskScoreDeptIsSave(String ObjId, String orgId, String type) {
		Criteria c = r_riskScoreDeptDAO.createCriteria();
		List<RiskScoreDept> list = null;
		c.add(Restrictions.and(Restrictions.eq("scoreObject.id", ObjId), 
			Restrictions.and(Restrictions.eq("organization.id", orgId),Restrictions.eq("orgType", type))));
		list = c.list();
		return list;
	}
	/**
	 * 保存打分对象
	 * @param scoreObj
	 */
	@Transactional
	public void saveRiskScoreObject(RiskScoreObject scoreObj) {
		r_riskScoreObjDAO.merge(scoreObj);
	}
	/**
	 * 保存打分部门
	 * @param scoreDept
	 */
	@Transactional
	public void saveRiskScoreDept(RiskScoreDept scoreDept) {
		r_riskScoreDeptDAO.merge(scoreDept);
	}
	/**
	 * 查询打分对象是否存在
	 * @param planId
	 * @param riskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RiskScoreObject findRiskScoreObjsByPlanAndRisk(String planId, String riskId) {
		Criteria c = r_riskScoreObjDAO.createCriteria();
		List<RiskScoreObject> list = null;
		if (StringUtils.isNotBlank(planId)&&StringUtils.isNotBlank(riskId)) {
			c.add(Restrictions.and(Restrictions.eq("assessPlan.id", planId), Restrictions.eq("risk.id", riskId)));
		}
		list = c.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 根据id查询打分对象
	 * @param objId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RiskScoreObject findRiskScoreObjById(String objId) {
		Criteria c = r_riskScoreObjDAO.createCriteria();
		List<RiskScoreObject> list = null;
		if (StringUtils.isNotBlank(objId)) {
			c.add(Restrictions.eq("id", objId));
		}
		list = c.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 删除打分部门
	 * @param depts
	 */
	@Transactional
	public void removeRiskScoreDepts(List<RiskScoreDept> depts) {
		for(RiskScoreDept dept : depts){
			r_riskScoreDeptDAO.delete(dept);
		}
	}
	/**
	 * 删除打分对象
	 * @param obj
	 */
	@Transactional
	public void removeRiskScoreObj(RiskScoreObject obj) {
		r_riskScoreObjDAO.delete(obj);
	}
	/**
	 * 通过评估计划id查询打分对象
	 * @param planId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskScoreObject> findRiskScoreObjByplanId(String planId) {
		Criteria c = r_riskScoreObjDAO.createCriteria();
		List<RiskScoreObject> list = null;
		if (StringUtils.isNotBlank(planId)) {
			c.add(Restrictions.eq("assessPlan.id", planId));
		}
		list = c.list();
			return list;
	}
	/**
	 * 通过计划id查询所有打分部门id
	 * @param planId
	 * @return
	 */
	public List<String> findScoreDeptIdsByPlanId(String planId){
		List<String> deptIdlist = new ArrayList<String>();
		List<RiskScoreObject> entityList = findRiskScoreObjByplanId(planId);
		if(entityList.size()>0){
			for(RiskScoreObject de : entityList){
				List<RiskScoreDept> scoDepts = findRiskDeptByObjId(de.getId());
				for(RiskScoreDept dept : scoDepts){
					if(!(deptIdlist.contains(dept.getOrganization().getId()))){
						deptIdlist.add(dept.getOrganization().getId());//将所有部门id返回页面（去重）
					}
				}
			}
		}
		return deptIdlist;
	}
	/**
	 * 查找登录人所在公司的的承办人（风险管理员）
	 * @param deptIds
	 * @return
	 */
	public SysEmployee findEmpsByRoleIdAnddeptId(String deptId){
		List<SysEmployee> empList = new ArrayList<SysEmployee>();
		Set<SysUser> userSet = o_sysRoleBO.fingUserByRoleId("RiskManagemer");//风险管理员
		for(SysUser user : userSet){
			List<SysEmployee> emps = findEmpEntryByUserId(user.getId());
			if(emps.size()>0){
				for(SysEmployee emp : emps){
					List<SysEmpOrg> empDepts = findEmpDeptsByEmpId(emp.getId());
					if(empDepts.size()>0){
						for(SysEmpOrg empDept : empDepts){
							if(deptId.equals(empDept.getSysOrganization().getId())){
								empList.add(emp);
							}
						}
					}
				}
			}
		}
		if(empList.size()>0){
			return empList.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 根据用户id查员工
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysEmployee> findEmpEntryByUserId(String userId) {
		Criteria c = o_sysEmployeeDAO.createCriteria();
		List<SysEmployee> list = null;
		
		if (StringUtils.isNotBlank(userId)) {
			c.add(Restrictions.eq("sysUser.id", userId));
		} else {
			return null;
		}
		
		list = c.list();
		return list;
	}
	/**
	 * 根据员工id查员工部门实体(不区分主，辅)
	 * @param empId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysEmpOrg> findEmpDeptsByEmpId(String empId) {
		Criteria c = o_sysOrgEmpDAO.createCriteria();
		List<SysEmpOrg> list = null;
		if (StringUtils.isNotBlank(empId)) {
			c.add(Restrictions.eq("sysEmployee.id", empId));
		} 
		list = c.list();
		return list;
	}
	/**
	 * 通过部门id查询所有员工
	 * @param deptId
	 * @return
	 */
	public List<SysEmployee> findEmpsBydeptId(String deptId){
		List<SysEmployee> empList = new ArrayList<SysEmployee>();
		if(null != deptId){
			List<SysEmpOrg> empOrgs = findEmpDeptBydeptId(deptId);
			for(SysEmpOrg eo : empOrgs){
				empList.add(eo.getSysEmployee());
			}
		}
		return empList;
	}
	/**
	 * 根据部门id查找员工集合
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysEmpOrg> findEmpDeptBydeptId(String deptId) {
		Criteria c = o_sysOrgEmpDAO.createCriteria();
		List<SysEmpOrg> list = null;
		if (StringUtils.isNotBlank(deptId)) {
			c.add(Restrictions.eq("sysOrganization.id", deptId));
		} 
		list = c.list();
		return list;
	}
	
	/**
	 * 工作流第一步--评估计划提交，审批
	 * @param empIds	承办人id
	 * @param approver	审批人
	 * @param businessId	评估计划id
	 */
	@Transactional
	public void submitAssessRiskPlanToApprover(String empIds, String approver, String businessId, String executionId){
		if (!"".equals(approver)) {
			if(StringUtils.isBlank(executionId)){
				//菜单触发提交开启工作流
				Map<String, Object> variables = new HashMap<String, Object>();
				String entityType = "riskWorkFlow";//流程名称
				variables.put("entityType", entityType);
				variables.put("AssessPlanApproverEmpId", approver);//审批人
				variables.put("businessId", businessId);
				variables.put("empIds", empIds);
				executionId = o_jbpmBO.startProcessInstance(entityType,variables);//开启工作流
				//工作流提交
				Map<String, Object> variablesBpmTwo = new HashMap<String, Object>();
				variablesBpmTwo.put("AssessPlanApproverEmpId", approver);
				o_jbpmBO.doProcessInstance(executionId, variablesBpmTwo);
			}
		}
	}
	
}
