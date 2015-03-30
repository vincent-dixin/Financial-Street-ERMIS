/**
 * AssessPlanBO.java
 * com.fhd.icm.business.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-6 		刘中帅
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
 */

package com.fhd.icm.business.icsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.icsystem.ConstructPlanBpmBO;
import com.fhd.icm.dao.icsystem.ConstructPlanDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaOrgDAO;
import com.fhd.icm.dao.icsystem.ConstructPlanRelaStandardDAO;
import com.fhd.icm.dao.icsystem.ConstructRelaProcessDAO;
import com.fhd.icm.dao.icsystem.DiagnosesDAO;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructRelaProcess;
import com.fhd.icm.entity.icsystem.Diagnoses;
import com.fhd.icm.utils.IcmStandardUtils;
import com.fhd.icm.web.form.ConstructPlanForm;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author 宋佳
 * @version
 * @since Ver 1.1
 * @Date 2013-1-6 下午7:28:25
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class ConstructPlanBO{

	
	@Autowired
	private ConstructPlanDAO o_constructPlanDAO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private SysOrgDao o_SysOrgDAO;
	@Autowired
	private ConstructPlanRelaOrgDAO o_constructPlanRelaOrgDAO;
	@Autowired
	private SysEmployeeDAO o_SysEmployeeDAO;
	@Autowired
	private ConstructPlanRelaStandardBO o_ConstructPlanRelaStandardBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	@Autowired
	private DiagnosesDAO o_diagnosesDAO;
	@Autowired
	private ConstructRelaProcessDAO o_constructRelaProcessDAO;
	@Autowired
	private ConstructPlanBpmBO o_constructPlanBpmBO;
	@Autowired
	private ConstructPlanRelaStandardDAO o_constructPlanRelaStandardDAO;
	/**
	 * 根据查询条件分页查询体系建设计划列表.
	 * @author 宋佳
	 * @param page
	 * @param sort
	 * @param dir
	 * @param query 查询条件
	 * @return Page<ConstructPlan>
	 * @since fhd　Ver 1.1
	 */
	public Page<ConstructPlan> findConstructPlansByPage(Page<ConstructPlan> page, String sort, String dir, String query,String status) {
		DetachedCriteria dc = DetachedCriteria.forClass(ConstructPlan.class);
		dc.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		dc.add(Restrictions.in("status", status.split(",")));
		dc.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
		if(StringUtils.isNotBlank(query)){
			dc.add(Restrictions.or(Property.forName("code").like(query, MatchMode.ANYWHERE),Property.forName("name").like(query, MatchMode.ANYWHERE)));
		}
		dc.addOrder(Order.desc("createTime"));
		return o_constructPlanDAO.findPage(dc, page, false);
	}
	
	public String getPlanCode(){
		String planCode = "BZ-01";
		String queryHql = "select code from ConstructPlan";
		List<String> resultList = null;
		List<Integer> resultList1=new ArrayList<Integer>();
		resultList = o_constructPlanDAO.find(queryHql);
		for(String value:resultList){
			Integer lastValue=Integer.parseInt(value.substring(value.indexOf("-")+1));
			resultList1.add(lastValue);
		}
		if (resultList1.size() > 0 && null != resultList1.get(0)) {
			Integer newCodeNumber = 0;
			String code = resultList.get(0);
			String codeChar = code.substring(0, code.indexOf("-") + 1);
			newCodeNumber =(Integer) Collections.max(resultList1) + 1;
			planCode = codeChar + newCodeNumber;
		}
		return planCode;
	}
	
	/**
	 * @param constructPlanForm
	 * @return
	 */
	@Transactional
	public String mergeConstructPlanByForm(ConstructPlanForm constructPlanForm){
		String constructPlanId = "";
		ConstructPlanRelaOrg constructPlanRelaOrg = null;
		if (null != constructPlanForm) {
			ConstructPlan constructPlan = null;
			if (StringUtils.isBlank(constructPlanForm.getId())) {
				//新增
				constructPlan = new ConstructPlan();
				//constructPlan = constructPlanForm;
				constructPlan.setId(Identities.uuid());
				constructPlan.setDealStatus(Contents.DEAL_STATUS_NOTSTART);//未开始
				constructPlan.setStatus(Contents.STATUS_SAVED);//已保存
				constructPlan.setCreateBy(UserContext.getUser().getEmp());
				constructPlan.setCreateTime(new Date());
				SysOrganization company = o_organizationBO.get(UserContext.getUser().getCompanyid());
				constructPlan.setCompany(company);
				constructPlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
				constructPlan.setCode(constructPlanForm.getCode());
				constructPlan.setName(constructPlanForm.getName());
				constructPlan.setPlanEndDate(constructPlanForm.getPlanEndDate());
				constructPlan.setPlanStartDate(constructPlanForm.getPlanStartDate());
				constructPlan.setRequirement(constructPlanForm.getRequirement());
				constructPlan.setWorkTarget(constructPlanForm.getWorkTarget());
				if (null != constructPlanForm.getType()) {// 评价类型
					constructPlan.setType(constructPlanForm.getType());
				}
//				if (null != constructPlanForm.getModifyReason()) {// 评价类型
//					constructPlan.setModifyReason(constructPlanForm.getModifyReason());
//				}
				o_constructPlanDAO.merge(constructPlan);
				//保存组长和组员
				constructPlanRelaOrg = new ConstructPlanRelaOrg();
				constructPlanRelaOrg.setId(Identities.uuid());
				constructPlanRelaOrg.setConstructPlan(constructPlan);
				String orgid=IcmStandardUtils.findIdbyJason(constructPlanForm.getGroupLeaderId(), "id");
				constructPlanRelaOrg.setEmp(o_SysEmployeeDAO.get(orgid));
				constructPlanRelaOrg.setType(Contents.EMP_RESPONSIBILITY);
				o_constructPlanRelaOrgDAO.merge(constructPlanRelaOrg);
				this.saveGroupPers(constructPlanForm.getGroupPersId(), constructPlan);  //保存组员
			}else{
				//修改
				constructPlan = o_constructPlanDAO.get(constructPlanForm.getId());
				constructPlan.setLastModifyBy(UserContext.getUser().getEmp());
				constructPlan.setLastModifyTime(new Date());
				constructPlan.setDeleteStatus(Contents.DELETE_STATUS_USEFUL);
				constructPlan.setCode(constructPlanForm.getCode());
				constructPlan.setName(constructPlanForm.getName());
				constructPlan.setPlanEndDate(constructPlanForm.getPlanEndDate());
				constructPlan.setPlanStartDate(constructPlanForm.getPlanStartDate());
				constructPlan.setRequirement(constructPlanForm.getRequirement());
				constructPlan.setWorkTarget(constructPlanForm.getWorkTarget());
				if(constructPlan.getType().getId().equals(constructPlanForm.getType().getId())){
					
				}else{
					if("diagnosesandprocess".equals(constructPlan.getType().getId())){
					    if("process".equals(constructPlanForm.getType().getId())){
					    	this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isNormallyDiagnosis",0);
					    }else{
					    	this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isProcessEdit",0);
					    }
					}else if("process".equals(constructPlan.getType().getId())){
						if("diagnosesandprocess".equals(constructPlanForm.getType().getId())){
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isNormallyDiagnosis",1);
						}else{
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isNormallyDiagnosis",1);
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isProcessEdit",0);
						}
					}else{
						if("diagnosesandprocess".equals(constructPlanForm.getType().getId())){
							
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isProcessEdit",1);
						}else{
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isNormallyDiagnosis",1);
							this.updatePlanRealStandardByPlanId(constructPlan.getId(), "isProcessEdit",0);
						}
					}
					constructPlan.setType(constructPlanForm.getType());
				}
				if (null != constructPlanForm.getModifyReason()) {// 评价类型
					constructPlan.setModifyReason(constructPlanForm.getModifyReason());
				}
				o_constructPlanDAO.merge(constructPlan);
				//如果是修改首先删除计划所对应的所有人员信息,这块应该是可以优化，应该在实体的建设上改进一下。
				//改进一  保存后付给隐藏域值，判断当前值与隐藏值是否相同，还要改进的话就是判断是否一致这块，如果顺序换了是不是有问题？
				if(!((constructPlanForm.getGroupLeaderId().equals(constructPlanForm.getGroupLeaderIdHid()))&&constructPlanForm.getGroupPersId().equals(constructPlanForm.getGroupPersIdHid()))){
					Criteria cra_constructPlanRelaOrg = o_constructPlanRelaOrgDAO.createCriteria();
					cra_constructPlanRelaOrg.add(Restrictions.eq("constructPlan.id", constructPlanForm.getId()));
					List<ConstructPlanRelaOrg> orgList = cra_constructPlanRelaOrg.list();
					for(ConstructPlanRelaOrg org : orgList){
						o_ConstructPlanRelaStandardBO.deletePlanStandarEmp(org.getId());
						o_constructPlanRelaOrgDAO.delete(org);
					}
					
					//保存组长和组员
					constructPlanRelaOrg = new ConstructPlanRelaOrg();
					constructPlanRelaOrg.setId(Identities.uuid());
					constructPlanRelaOrg.setConstructPlan(constructPlan);
					String orgid=IcmStandardUtils.findIdbyJason(constructPlanForm.getGroupLeaderId(), "id");
					constructPlanRelaOrg.setEmp(o_SysEmployeeDAO.get(orgid));
					constructPlanRelaOrg.setType(Contents.EMP_RESPONSIBILITY);
					o_constructPlanRelaOrgDAO.merge(constructPlanRelaOrg);
					this.saveGroupPers(constructPlanForm.getGroupPersId(), constructPlan);  //保存组员
				}
			}
			constructPlanId = constructPlan.getId();
		}
		return constructPlanId;
	}
	//保存计划实体
	public void mergeConstructPlan(ConstructPlan constructPlan){
	    o_constructPlanDAO.merge(constructPlan);
	}
	/**
	 * 
	 *  保存组员数据 
	 */
	private void saveGroupPers(String groupPersId,ConstructPlan constructPlan) {
		if (StringUtils.isNotEmpty(groupPersId)) {
			JSONArray jsonArray = JSONArray.fromObject(groupPersId);
			int jsonArrayLength = jsonArray.size();
			for (int i = 0; i < jsonArrayLength; i++) {
				ConstructPlanRelaOrg constructPlanRelaOrg = new ConstructPlanRelaOrg();
				constructPlanRelaOrg.setId(Identities.uuid());
				constructPlanRelaOrg.setConstructPlan(constructPlan);
				JSONObject jsonobj = (JSONObject) jsonArray.get(i);
				constructPlanRelaOrg.setEmp(o_SysEmployeeDAO.get(jsonobj.getString("id")));
				constructPlanRelaOrg.setType(Contents.EMP_HANDLER);
				o_constructPlanRelaOrgDAO.merge(constructPlanRelaOrg);
			}
		}
	}
	/**
	 * <pre>
	 * 根据建设计划ID获得建设计划
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param constructPlanId
	 *            :建设计划Id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public ConstructPlan findConstructPlanById(String constructPlanId) {
		if(StringUtils.isBlank(constructPlanId)){
			return new ConstructPlan();
		}else{
			return (ConstructPlan) o_constructPlanDAO.get(constructPlanId);
		}
	}
	
	/**
	 * <pre>
	 *    根据一些条件查询体系建相关的部门或人
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
	public List<ConstructPlanRelaOrg> findAssessPlanRelaOrgEmpBySome(
			String constructPlanId, Boolean empIsNotNull, Boolean orgIsNotNull,
			String returnType) {
		Criteria criteria = o_constructPlanRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
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
	@Transactional
	public int removeConstructPlanByIdList(String[] idArray) {
		StringBuffer hql = new StringBuffer();
		hql.append(
				"update ConstructPlan cp set cp.deleteStatus=:deleteStatus where cp.id in('")
					.append(StringUtils.join(idArray, "','")).append("')");
	    Query query = o_constructPlanDAO.createQuery(hql.toString());
		query.setString("deleteStatus", Contents.DELETE_STATUS_DELETED);
		return query.executeUpdate();
	}
	@Transactional
	public int updatePlanRealStandardByPlanId(String constructPlanId,String fieldName,int type) {
		StringBuffer hql = new StringBuffer();
		hql.append(
				"update ConstructPlanRelaStandard cp set cp."+fieldName+"= "+type+" where constructPlan.id =:constructPlanId");
	    Query query = o_constructPlanRelaStandardDAO.createQuery(hql.toString());
		query.setString("constructPlanId", constructPlanId);
		return query.executeUpdate();
	}
	
	public List<ConstructPlan> findConstructPlanListBySome(String companyId){
		Criteria criteria = o_constructPlanDAO.createCriteria();
		if(StringUtils.isNotBlank(companyId)){
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		criteria.add(Restrictions.eq("deleteStatus", Contents.DELETE_STATUS_USEFUL));
		return criteria.list();
	}
    //查询所有流程梳理的任务
	public List<Diagnoses> findDiagnosesListBySome(String companyId,String constructPlanId){
		Criteria criteria = o_diagnosesDAO.createCriteria();
		criteria.createAlias("constructPlan", "constructPlan");
		if(StringUtils.isNotBlank(constructPlanId)){
			criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		}else{
			criteria.add(Restrictions.eq("constructPlan.deleteStatus", Contents.DELETE_STATUS_USEFUL));
			criteria.createAlias("constructPlan.company", "company");
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		return criteria.list();
	}
	//查询所有流程梳理的任务
	public List<ConstructRelaProcess> findProcessEditListBySome(String companyId,String constructPlanId){
		Criteria criteria = o_constructRelaProcessDAO.createCriteria();
		criteria.createAlias("constructPlan", "constructPlan");
		if(StringUtils.isNotBlank(constructPlanId)){
			criteria.add(Restrictions.eq("constructPlan.id", constructPlanId));
		}else{
			criteria.add(Restrictions.eq("constructPlan.deleteStatus", Contents.DELETE_STATUS_USEFUL));
			criteria.createAlias("constructPlan.company", "company");
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		return criteria.list();
	}
}
