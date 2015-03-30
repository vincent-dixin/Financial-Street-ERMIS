/**
 * AssessPlanBO.java
 * com.fhd.icm.business.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-6 		刘中帅
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
 */

package com.fhd.icm.business.assess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.assess.AssessFrequenceDAO;
import com.fhd.icm.dao.assess.AssessPlanDAO;
import com.fhd.icm.dao.assess.AssessPlanProcessRelaOrgEmpDAO;
import com.fhd.icm.dao.assess.AssessPlanRelaOrgEmpDAO;
import com.fhd.icm.dao.assess.AssessPointDAO;
import com.fhd.icm.entity.assess.AssessFrequence;
import com.fhd.icm.entity.assess.AssessPlan;
import com.fhd.icm.entity.assess.AssessPlanProcessRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaProcess;
import com.fhd.icm.entity.assess.AssessPoint;
import com.fhd.icm.interfaces.assess.IAssessPlanBO;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.controller.bpm.AssessPlanBpmObject;
import com.fhd.icm.web.form.AssessPlanForm;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaOrg;
import com.fhd.sys.business.organization.OrgGridBO;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author 刘中帅
 * @version
 * @since Ver 1.1
 * @Date 2013-1-6 下午7:28:25
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class AssessPlanBO implements IAssessPlanBO {

	@Autowired
	private AssessPlanDAO o_assessPlanDAO;
	@Autowired
	private AssessPlanRelaOrgEmpDAO o_assessPlanRelaOrgEmpDAO;
	@Autowired
	private AssessPointDAO o_assessPointDAO;
	@Autowired
	private ProcessBO o_processBO;
	@Autowired
	private AssessFrequenceDAO o_assessFrequenceDAO;
	@Autowired
	private AssessPlanRelaProcessBO o_assessPlanRelaProcessBO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	@Autowired
	private AssessPlanProcessRelaOrgEmpDAO o_assessPlanProcessRelaOrgEmpDAO;

	/**
	 * <pre>
	 * 保存 内控计划与 部门,组长,组成员的关联
	 * </pre>
	 * @author 刘中帅
	 * @param assessPlanId :评价计划Id
	 * @param orgOrEmpId :部门或员工Id
	 * @param type:常量，Contents.ORG_PARTICIPATION,Contents.EMP_RESPONSIBILITY,Contents.EMP_HANDLER.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	private void saveAssessPlanRelaOrgEmp(String assessPlanId, String orgOrEmpId, String type) {
		AssessPlanRelaOrgEmp assessPlanRelaOrgEmg = new AssessPlanRelaOrgEmp();
		assessPlanRelaOrgEmg.setId(Identities.uuid());
		AssessPlan assessPlan = new AssessPlan();
		assessPlan.setId(assessPlanId);
		assessPlanRelaOrgEmg.setAssessPlan(assessPlan);
		assessPlanRelaOrgEmg.setType(type);
		if (Contents.ORG_PARTICIPATION.equals(type)) {// 设置保存部门
			if(StringUtils.isNotBlank(orgOrEmpId)){
				SysOrganization sysOrganization = new SysOrganization();
				sysOrganization.setId(orgOrEmpId);
				assessPlanRelaOrgEmg.setOrg(sysOrganization);
			}
		} else if (Contents.EMP_RESPONSIBILITY.equals(type) || Contents.EMP_HANDLER.equals(type)) {// 设置保存组长或组员
			if(StringUtils.isNotBlank(orgOrEmpId)){
				SysEmployee sysEmployee = new SysEmployee();
				sysEmployee.setId(orgOrEmpId);
				assessPlanRelaOrgEmg.setEmp(sysEmployee);
			}
		}
		o_assessPlanRelaOrgEmpDAO.merge(assessPlanRelaOrgEmg);
	}

	/**
	 * <pre>
	 * 保存评价计划与部门关联
	 * </pre>
	 * @param assessPlanId 评价计划Id
	 * @param processIds 流程Id集合:
	 * @param saveType 保存条件
	 * @author 刘中帅
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveAssessPlanRelaOrg(String assessPlanId, String[] processIds, String saveType) {
		if (saveType.equals(Contents.ORG_PARTICIPATION)) {// 保存数据类型为 部门
			List<SysOrganization> sysOrganizationList = null;
			List<SysOrganization> sysOrganizationByPlanList = null;
			sysOrganizationList = o_processBO.findSysOrganizationByProcessId(processIds, Contents.ORG_RESPONSIBILITY);
			sysOrganizationByPlanList = findSysOrganizationListByAssessPlanId(assessPlanId);
			for (SysOrganization org : sysOrganizationList) {
				if (!sysOrganizationByPlanList.contains(org)) {
					sysOrganizationByPlanList.add(org);
					AssessPlanRelaOrgEmp apo = new AssessPlanRelaOrgEmp();
					apo.setId(Identities.uuid());

					AssessPlan assessPlan = new AssessPlan();
					assessPlan.setId(assessPlanId);
					apo.setAssessPlan(assessPlan);

					apo.setOrg(org);
					apo.setType(saveType);
					o_assessPlanRelaOrgEmpDAO.merge(apo);
				}
				
			}
		} else {// 保存数据类型为人员

			List<SysEmployee> sysEmployeeList = null;
			List<SysEmployee> sysEmployeeListByPro = null;
			sysEmployeeList = o_processBO.findSysEmployeeByProcessId(processIds, Contents.EMP_RESPONSIBILITY);
			sysEmployeeListByPro = findSysEmployeeListByAssessPlanId(assessPlanId);
			for (SysEmployee sysEmployee : sysEmployeeList) {
				if (!sysEmployeeListByPro.contains(sysEmployee)) {
					sysEmployeeListByPro.add(sysEmployee);
					AssessPlanRelaOrgEmp assessPlanRelaOrgEmp = new AssessPlanRelaOrgEmp();
					assessPlanRelaOrgEmp.setId(Identities.uuid());

					AssessPlan assessPlan = new AssessPlan();
					assessPlan.setId(assessPlanId);
					assessPlanRelaOrgEmp.setAssessPlan(assessPlan);

					assessPlanRelaOrgEmp.setEmp(sysEmployee);
					assessPlanRelaOrgEmp.setType(saveType);
					o_assessPlanRelaOrgEmpDAO.merge(assessPlanRelaOrgEmp);
				}
			}
		}

	}

	/**
	 * 修改内控计划.
	 * @param assessPlan
	 */
	@Transactional
	public void mergeAssessPlan(AssessPlan assessPlan){
		o_assessPlanDAO.merge(assessPlan);
	}
	
	/**
	 * 新增内控计划.
	 * 1.新增：
	 * a).基本信息
	 *    自评--添加计划表
	 *    他评--添加计划表、计划部门人员表
	 * b).选择流程添加
	 *    自评--添加计划流程表、计划部门人员表
	 *    他评--添加计划流程表
	 *    选择流程删除
	 *    自评--删除计划流程表、计划部门人员表
	 *    他评--删除计划流程表
	 * 2.修改:
	 * a).基本信息
	 *    自评->他评--删除自评计划部门人员表、添加他评计划部门人员表
	 *    自评->自评--不变
	 *    他评->自评--删除他评计划部门人员表、添加自评计划部门人员表
	 *    他评->他评--不变
	 * b).选择流程添加
	 *    自评--添加计划流程表、计划部门人员表
	 *    他评--添加计划流程表
	 *    选择流程删除
	 *    自评--删除计划流程表、计划部门人员表
	 *    他评--删除计划流程表
	 * @author 刘中帅
	 * @param assessPlanForm 评价计划form
	 * @return String
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public String mergeAssessPlanByForm(AssessPlanForm assessPlanForm) {
		String assessPlanId = "";
		if (null != assessPlanForm) {
			AssessPlan assessPlan = null;
			if (StringUtils.isBlank(assessPlanForm.getId())) {
				//新增
				assessPlan = new AssessPlan();
				assessPlan.setId(Identities.uuid());
				
				assessPlan.setDealStatus(Contents.DEAL_STATUS_NOTSTART);//未开始
				assessPlan.setStatus(Contents.STATUS_SAVED);//已保存
			}else{
				//修改
				assessPlan = this.findAssessPlanByAssessPlanId(assessPlanForm.getId());
			}
			assessPlan.setDesc(assessPlanForm.getDesc());
			assessPlan.setCode(assessPlanForm.getCode());
			assessPlan.setName(assessPlanForm.getName());
			assessPlan.setCompany(assessPlanForm.getCompany());
			assessPlan.setPlanStartDate(assessPlanForm.getPlanStartDate());// 计划期始
			assessPlan.setPlanEndDate(assessPlanForm.getPlanEndDate());// 计划期止
			assessPlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
			assessPlan.setRequirement(assessPlanForm.getRequirement());
			assessPlan.setAssessStandard(assessPlanForm.getAssessStandard());// 基本要求
			assessPlan.setAssessTarget(assessPlanForm.getAssessTarget());
			assessPlan.setAssessStandard(assessPlanForm.getAssessStandard());
			assessPlan.setTargetStartDate(assessPlanForm.getTargetStartDate());// 评价期始
			assessPlan.setTargetEndDate(assessPlanForm.getTargetEndDate());// 评价期止
			assessPlan.setActualStartDate(assessPlanForm.getActualStartDate());
			assessPlan.setActualEndDate(assessPlanForm.getActualEndDate());
			if (null != assessPlanForm.getAssessMeasure()) {// 评价方式
				assessPlan.setAssessMeasure(assessPlanForm.getAssessMeasure());
			}
			
			if(StringUtils.isNotEmpty(assessPlanForm.getId())){//编辑操作
				
				removeAssessPlanRelaOrgEmpByAssessPlanId(assessPlan.getId(),Contents.EMP_RESPONSIBILITY);//删除组长
				
				if(Contents.ASSESS_MEASURE_ETYPE_SELF.equals(assessPlanForm.getType().getId())){//自评
					if(Contents.ASSESS_MEASURE_ETYPE_OTHER.equals(assessPlan.getType().getId())){
						//他评->自评：删除他评计划部门人员表、添加自评计划部门人员表
						removeAssessPlanRelaOrgEmpByAssessPlanId(assessPlan.getId(),Contents.EMP_HANDLER);
						//修改时，他评已选择流程，需要把已选择的流程的责任人添加到自评计划部门人员表
						Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
						for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcesses) {
							Process process = assessPlanRelaProcess.getProcess();
							if(null != process){
								Set<ProcessRelaOrg> processRelaOrgs = process.getProcessRelaOrg();
								for (ProcessRelaOrg processRelaOrg : processRelaOrgs) {
									if(StringUtils.isNotBlank(processRelaOrg.getType()) && Contents.EMP_RESPONSIBILITY.equals(processRelaOrg.getType())){
										if(null != processRelaOrg.getEmp()){
											saveAssessPlanRelaOrgEmp(assessPlan.getId(), processRelaOrg.getEmp().getId(), Contents.EMP_HANDLER);	
										}
									}
								}
							}
						}
					}
				}else if(Contents.ASSESS_MEASURE_ETYPE_OTHER.equals(assessPlanForm.getType().getId())){//他评
					removeAssessPlanRelaOrgEmpByAssessPlanId(assessPlan.getId(),Contents.EMP_HANDLER);
				}
				
			}			

			if (null != assessPlanForm.getType()) {// 评价类型
				assessPlan.setType(assessPlanForm.getType());
			}
			o_assessPlanDAO.merge(assessPlan);
			
			if (StringUtils.isNotEmpty(assessPlanForm.getGroupLeaderId())) {
				String groupLeaderId = IcmStandardUtils.findIdbyJason(assessPlanForm.getGroupLeaderId(), "id");
				saveAssessPlanRelaOrgEmp(assessPlan.getId(), groupLeaderId, Contents.EMP_RESPONSIBILITY);	
			}
			// 保存与组成员关联
			if (StringUtils.isNotBlank(assessPlanForm.getGroupPersId()) && Contents.ASSESS_MEASURE_ETYPE_OTHER.equals(assessPlanForm.getType().getId())) {
				// 获得多选的组成员Id以","间隔
				String groupPersIds = IcmStandardUtils.findIdbyJason(assessPlanForm.getGroupPersId(), "id");
				String[] groupPersIdArray = groupPersIds.split(",");
				for (String persId : groupPersIdArray) {
					saveAssessPlanRelaOrgEmp(assessPlan.getId(), persId,Contents.EMP_HANDLER);
				}
			}
			
			assessPlanId = assessPlan.getId();
		}
		return assessPlanId;
	}

	/**
	 * 更新评价计划状态.
	 * @author 刘中帅
	 * @param assessPlanId 评价计划Id
	 * @param status 状态
	 * @param dealStatus 执行状态
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void mergeAssessPlanById(String assessPlanId,String status,String dealStatus) {
		AssessPlan assessPlan = this.findAssessPlanByAssessPlanId(assessPlanId);
		if (null != assessPlan) {
			assessPlan.setStatus(status);
			assessPlan.setDealStatus(dealStatus);
			o_assessPlanDAO.merge(assessPlan);
		}
	}

	/**
	 * 根据评价方式更新评价计划相应的是否测试字段.
	 * @param assessPlan
	 * @param assessMeasureId
	 */
	@Transactional
	public void mergeAssessPlanProcessureByAssessPlanId(AssessPlan assessPlan, String assessMeasureId){
		if(null != assessPlan){
			if(StringUtils.isNotBlank(assessMeasureId)){
				if(Contents.ASSESS_MEASURE_PRACTICE_TEST.equals(assessMeasureId)){
					//设置该计划下的所有评价流程对应的是否穿行测试字段为'是'
					Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
					for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcesses) {
						assessPlanRelaProcess.setIsPracticeTest(true);
						//默认为1
						//assessPlanRelaProcess.setPracticeNum(1);
						o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcess(assessPlanRelaProcess);
					}
				}else if(Contents.ASSESS_MEASURE_SAMPLE_TEST.equals(assessMeasureId)){
					//设置该计划下的所有评价流程对应的是否抽样测试字段为'是'
					Set<AssessPlanRelaProcess> assessPlanRelaProcesses = assessPlan.getAssessPlanRelaProcess();
					for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcesses) {
						assessPlanRelaProcess.setIsSampleTest(true);
						o_assessPlanRelaProcessBO.mergeAssessPlanRelaProcess(assessPlanRelaProcess);
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 移除评价计划与部门，人员的关联
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanId
	 *            :评价计划Id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAssessPlanRelaOrgEmpByAssessPlanId(String assessPlanId, String type) {
		//删除前判断评价计划与类型对应的评价计划人员关联表是否存在
		Criteria criteria = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteria.add(Restrictions.eq("type", type));
		List<AssessPlanRelaOrgEmp> list = criteria.list();
		if(null != list && list.size()>0){
			String assessPlanRelaOrgEmpIds = "";
			int i=0;
			for (AssessPlanRelaOrgEmp assessPlanRelaOrgEmp : list) {
				assessPlanRelaOrgEmpIds += "'"+assessPlanRelaOrgEmp.getId()+"'";
				if(i != list.size()-1){
					assessPlanRelaOrgEmpIds += ",";
				}
			}
			//删除评价计划流程部门人员关联表数据
			o_assessPlanProcessRelaOrgEmpDAO.createQuery("delete AssessPlanProcessRelaOrgEmp appo where appo.assessPlanRelaOrgEmp.id in (:assessPlanRelaOrgEmpId)").setString("assessPlanRelaOrgEmpId", assessPlanRelaOrgEmpIds).executeUpdate();
		}
		o_assessPlanRelaOrgEmpDAO.createQuery("delete AssessPlanRelaOrgEmp apo where apo.assessPlan.id=:assessPlanId and apo.type=:type").setString("assessPlanId", assessPlanId).setString("type", type).executeUpdate();
	}
	
	/**
	 * <pre>
	 * 移除评价计划，人员的关联
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanId
	 *            :评价计划Id:empId:人员Id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAssessPlanRelaOrgEmpByOrgId(String assessPlanId,
			String[] orgIds, String orgType) {
		o_assessPlanRelaOrgEmpDAO
				.createQuery(
						"delete AssessPlanRelaOrgEmp apo where apo.assessPlan.id=:assessPlanId and apo.type=:orgType and  apo.org.id in (:orgIds)")
				.setString("assessPlanId", assessPlanId)
				.setString("orgType", orgType)
				.setParameterList("orgIds", orgIds).executeUpdate();
	}

	/**
	 * <pre>
	 * 移除评价计划，人员的关联
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanId
	 *            :评价计划Id:empId:人员Id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAssessPlanRelaOrgEmpByEmpId(String assessPlanId,
			String[] empIds, String empType) {
		o_assessPlanRelaOrgEmpDAO
				.createQuery("delete AssessPlanRelaOrgEmp apo where apo.assessPlan.id=:assessPlanId and apo.type=:empType and  apo.emp.id in (:empIds)")
				.setString("assessPlanId", assessPlanId)
				.setString("empType", empType)
				.setParameterList("empIds", empIds).executeUpdate();
	}

	/**
	 * <pre>
	 * 删除流程范围的同时，要删除评价计划相关联的部门和人员.
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param processIds 流程Id集合,assessPlanId:评价计划Id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeAssessPlanRelaOrgEmpByProcessIds(String assessPlanId, String[] processId) {
		List<ProcessRelaOrg> processRelaOrgList = o_processBO.findProcessRelaOrgByProcessId(processId);
		//当前评价计划的人员关联的流程数
		List<Object[]> empObjectList = o_assessPlanRelaProcessBO.findEmpRelaProcessCount(assessPlanId);
		StringBuffer orgSb = new StringBuffer();
		StringBuffer empSb = new StringBuffer();
		for (ProcessRelaOrg processRelaOrg : processRelaOrgList) {
			if(null!=processRelaOrg.getType()){
				if (Contents.EMP_RESPONSIBILITY.equals(processRelaOrg.getType())) {// 责任人
					if(null != processRelaOrg.getEmp()){
						for(Object[] empObject : empObjectList){
							if(null != empObject[0] && processRelaOrg.getEmp().getId().equals(empObject[0])){
								if(Integer.valueOf(String.valueOf(empObject[1])) == 1){
									empSb.append(processRelaOrg.getEmp().getId());
									empSb.append(",");
								}
							}
						}
					}
				}else if (Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType())) {// 参与部门
					if(null != processRelaOrg.getOrg()){
						orgSb.append(processRelaOrg.getOrg().getId());
						orgSb.append(",");
					}
				}
			}
		}
		String[] orgIdArray = orgSb.toString().split(",");
		String[] empIdArray = empSb.toString().split(",");
		removeAssessPlanRelaOrgEmpByOrgId(assessPlanId, orgIdArray,Contents.ORG_PARTICIPATION);
		removeAssessPlanRelaOrgEmpByEmpId(assessPlanId, empIdArray, Contents.EMP_HANDLER);
	}

	/**
	 * 
	 * <pre>
	 * 批量删除内控计划（支持单条删除）
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlans
	 * @since fhd　Ver 1.1
	 */
	public void removeAssessPlanByIdList(List<String> idList) {
		if (null != idList && idList.size() > 0) {
			StringBuffer hql = new StringBuffer();
			hql.append(
					"update AssessPlan ap set ap.deleteStatus=:deleteStatus where ap.id in('")
					.append(StringUtils.join(idList, "','")).append("')");
			Query query = o_assessPlanDAO.createQuery(hql.toString());
			query.setString("deleteStatus", Contents.DELETE_STATUS_DELETED);
			query.executeUpdate();
		}
	}


	/**
	 * <pre>
	 * 根据评价计划ID获得评价计划
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanId
	 *            :评价计划Id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public AssessPlan findAssessPlanById(String assessPlanId) {
		Criteria criteria = o_assessPlanDAO.createCriteria();
		criteria.add(Restrictions.eq("id", assessPlanId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("createBy", FetchMode.JOIN);
		criteria.setFetchMode("assessMeasure", FetchMode.JOIN);
		criteria.setFetchMode("assessPlanRelaOrgEmp", FetchMode.JOIN);
		criteria.setFetchMode("assessPlanRelaOrgEmp", FetchMode.JOIN);
		return (AssessPlan) criteria.uniqueResult();
	}

	/**
	 * <pre>
	 * 根据评价计划查询创建人
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param assessPlanId
	 *            评价计划Id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public String findEmpIdByAssessPlanId(String assessPlanId) {
		AssessPlan assessPlan =o_assessPlanDAO.get(assessPlanId);
		if(null!=assessPlan){
			return assessPlan.getCreateBy().getId();
		}else{
			return "";	
		}
	}

	/**
	 * 根据评价计划id查询评价计划.
	 * @author 刘中帅
	 * @param assessPlanId 内控评价计划Id
	 * @return AssessPlan
	 * @since fhd　Ver 1.1
	 */
	public AssessPlan findAssessPlanByAssessPlanId(String assessPlanId) {
		return o_assessPlanDAO.get(assessPlanId);
	}

	/**
	 * <pre>
	 * 多条件查询assessPlan
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param param
	 *            参数名称
	 * @param paramValue
	 *            条件
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<AssessPlan> findAssessPlandBySome(String[] param,
			String[] paramValue) {
		Criteria criteria = o_assessPlanDAO.createCriteria();
		for (int i = 0; i < param.length; i++) {
			criteria.add(Restrictions.eq(param[i], paramValue[i]));
		}
		String companyId = UserContext.getUser().getCompanyid();
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.addOrder(Order.asc("name"));
		criteria.setFetchMode("createBy", FetchMode.JOIN);
		criteria.setFetchMode("assessMeasure", FetchMode.JOIN);
		criteria.setFetchMode("assessPlanRelaOrgEmp", FetchMode.JOIN);
		criteria.setFetchMode("assessor", FetchMode.JOIN);
		criteria.setFetchMode("assessPlanRelaOrgEmp", FetchMode.JOIN);
		List<AssessPlan> list = criteria.list();
		return list;
	}

	/**
	 * <pre>
	 * 通过assessPlanId查询assessPlanRelaOrgEmp
	 * </pre>
	 * 
	 * @author 刘中帅 参数名称
	 * @param assessPlanId
	 *            :内控评价Id 条件
	 * @return AssessPlanRelaOrg 实体
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> findSysOrganizationListByAssessPlanId(
			String assessPlanId) {
		Criteria criteriaOrg = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteriaOrg.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteriaOrg.add(Restrictions.eq("type", Contents.ORG_PARTICIPATION));
		List<SysOrganization> sysOrganizationList = new ArrayList<SysOrganization>();
		List<AssessPlanRelaOrgEmp> assessPlanListOrg = criteriaOrg.list();
		for (AssessPlanRelaOrgEmp oe : assessPlanListOrg) {
			if (!sysOrganizationList.contains(oe.getOrg())) {
				sysOrganizationList.add(oe.getOrg());
			}
		}
		return sysOrganizationList;
	}

	/**
	 * <pre>
	 * 通过assessPlanId查询assessPlanRelaOrgEmp
	 * </pre>
	 * 
	 * @author 刘中帅 参数名称
	 * @param assessPlanId
	 *            :内控评价Id 条件
	 * @return AssessPlanRelaOrg 实体
	 * @since fhd　Ver 1.1
	 */
	public List<SysEmployee> findSysEmployeeListByAssessPlanId(
			String assessPlanId) {
		Criteria criteriaOrg = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteriaOrg.add(Restrictions.eq("assessPlan.id", assessPlanId));
		criteriaOrg.add(Restrictions.eq("type", Contents.EMP_HANDLER));
		List<SysEmployee> sysEmployeeList = new ArrayList<SysEmployee>();
		List<AssessPlanRelaOrgEmp> assessPlanListOrg = criteriaOrg.list();
		for (AssessPlanRelaOrgEmp oe : assessPlanListOrg) {
			if (!sysEmployeeList.contains(oe.getEmp())) {
				sysEmployeeList.add(oe.getEmp());
			}

		}
		return sysEmployeeList;
	}

	/**
	 * <pre>
	 * 判断流程process的年度需抽取的样本数
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param process
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public Double findFrequenceNumByProcess(Process process) {
		/*
		 * 这里需要匹配AssessFrequence表里的标准数据，然后根据当前流程的发生频率及相关的风险水平的高中低，来获得年度需要抽取的样本数
		 * TODO 这里还需要判断该流程的风险度水平
		 */
		List<AssessFrequence> assessFrequenceList = findAssessFrequenceBySome();
		Double frequenceNum =0.0;// 年度需抽取的样本数
		for (AssessFrequence assessFrequence : assessFrequenceList) {
			if(null!=process.getFrequency()){
				if (assessFrequence.getCode().equals(process.getFrequency().getId())) {
					//TODO
						frequenceNum = assessFrequence.getAmountMax();
				}
			}
		}
		return frequenceNum;

	}

	/**
	 * <pre>
	 * 获得所有的发生频率的数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<AssessFrequence> findAssessFrequenceBySome() {
		Criteria criteria = o_assessFrequenceDAO.createCriteria();
		return criteria.list();
	}

	/**
	 * <pre>
	 * 选择所有的AssessPointFrequence（去重）
	 *  根据一些查询条件查询评价点
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param processId
	 *            流程ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<AssessPoint> findAssessPointListBySome(String processId) {
		Criteria criteria = o_assessPointDAO.createCriteria();
		criteria.add(Restrictions.eq("process.id", processId));
		return criteria.list();
	}

	/**
	 * <pre>
	 * 根据一些条件查询评价计划相关的部门或人
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanId
	 *            评价计划ID
	 * @param empIsNotNull
	 *            员工ID不为空
	 * @param orgIsNotNull
	 *            部门ID不为空
	 * @param returnType
	 *            参与部门,经办人，复核人的常量
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<AssessPlanRelaOrgEmp> findAssessPlanRelaOrgEmpBySome(
			String assessPlanId, Boolean empIsNotNull, Boolean orgIsNotNull,
			String returnType) {
		Criteria criteria = o_assessPlanRelaOrgEmpDAO.createCriteria();
		criteria.add(Restrictions.eq("assessPlan.id", assessPlanId));
		if (StringUtils.isNotBlank(returnType)) {
			criteria.add(Restrictions.eq("type", returnType));
		}
		if (empIsNotNull) {
			criteria.add(Restrictions.isNotNull("emp.id"));
			criteria.setFetchMode("emp", FetchMode.JOIN);
		}
		if (orgIsNotNull) {
			criteria.add(Restrictions.isNotNull("org.id"));
			criteria.setFetchMode("org", FetchMode.JOIN);
		}
		return criteria.list();
	}

	/**
	 * 
	 * <pre>
	 * 保存或修改内控评价计划时做数据验证
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<AssessFrequence> findAssessPointFrequences() {
		List<AssessFrequence> assessPointFrequence = new ArrayList<AssessFrequence>();
		String sql = "from AssessFrequence af group by af.code order by af.id desc";
		Query query = o_assessFrequenceDAO.createQuery(sql);
		assessPointFrequence = query.list();
		return assessPointFrequence;
	}
	/**
	 * 根据查询条件查询计划进度列表.
	 * @param page
	 * @param sort
	 * @param query
	 * @param companyId
	 * @param dealStatus
	 * @return Page<AssessPlan>
	 */
	public Page<AssessPlan> findAssessPlanListBySome(Page<AssessPlan> page, String sort, String query, String companyId, String dealStatus){
		DetachedCriteria dc= DetachedCriteria.forClass(AssessPlan.class);
		//模糊匹配缺陷的编号和名称
		if(StringUtils.isNotBlank(query)){
			dc.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE), Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		
		if(StringUtils.isNotBlank(companyId)){
			dc.add(Restrictions.eq("company.id", companyId));
		}else{
			String companyIdOnline = UserContext.getUser().getCompanyid();
			List<SysOrganization> orgList = o_orgGridBO.findSubCompanyList(companyIdOnline);
			Set<String> orgIdSet = new HashSet<String>();
			for (SysOrganization org : orgList) {
				orgIdSet.add(org.getId());
			}
			orgIdSet.add(companyIdOnline);
			if(orgIdSet.size()>0){
				dc.add(Restrictions.in("company.id", orgIdSet));
			}
		}
		
		if(StringUtils.isNotBlank(dealStatus)){
			dc.add(Restrictions.in("dealStatus", dealStatus.split(",")));
		}
		dc.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		dc.addOrder(Order.desc("createTime"));
		return o_assessPlanDAO.findPage(dc, page, false);
	}
	/**
	 * 评价计划发布分发的流程任务的参数中涉及的相关流程id、评价人和复核人.
	 * @author 吴德福
	 * @param improveId 评价计划ID
	 * @return AssessPlanBpmObject
	 * @since  fhd　Ver 1.1
	*/
	public List<AssessPlanBpmObject> findAssessPlanRelaProcessParameterForBpmTask(String assessPlanId){
		List<AssessPlanBpmObject> list = new ArrayList<AssessPlanBpmObject>();
		//根据评价计划id查询对应的评价流程列表
		List<AssessPlanRelaProcess> assessPlanRelaProcessList = o_assessPlanRelaProcessBO.findAssessPlanRelaProcessListByAssessPlanId(assessPlanId);
		if(null != assessPlanRelaProcessList && assessPlanRelaProcessList.size()>0){
			AssessPlanBpmObject assessPlanBpmObject = null;
			for(AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList){
				assessPlanBpmObject = new AssessPlanBpmObject();
				
				//评价计划对应的流程id
				assessPlanBpmObject.setAssessPlanRelaProcessId(assessPlanRelaProcess.getId());
				
				Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmps = assessPlanRelaProcess.getAssessPlanProcessRelaOrgEmp();
				for(AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmps){
					if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
						//评价计划对应的评价人
						if(null != assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp()){
							assessPlanBpmObject.setExecuteEmpId(assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getId());
						}
					}
					if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
						//评价计划对应的复核人
						if(null != assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp()){
							assessPlanBpmObject.setReviewerEmpId(assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getId());
						}
					}
				}
				
				list.add(assessPlanBpmObject);
			}
		}
		
		return list;
	}	
}
