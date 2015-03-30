/**
 * DutyBO.java
 * com.fhd.fdc.commons.business.sys.duty
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-12-30 		David
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */
/**
 * DutyBO.java
 * com.fhd.fdc.commons.business.sys.duty
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-12-30        David
 *
 * Copyright (c) 2010, FirstHuida All Rights Reserved.
 */

package com.fhd.sys.business.duty;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.duty.DutyDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAOold;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 职务BO.
 * @author David
 * @version
 * @since Ver 1.1
 * @Date 2010-12-30 上午11:08:33
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class DutyBO {
	
	@Autowired
	private DutyDAO o_dutyDAO;
	@Autowired
	private SysEmployeeDAOold o_sysEmployeeDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;

	@Autowired
	private SysOrganizationDAO o_sysOrganizationDAO;

	/**
	 * 取得根结点.
	 * @author lihuanhuan
	 * @return SysOrganization
	 * @since fhd　Ver 1.1
	 */
	public SysOrganization getRootOrg() {
		SysOrganization org = new SysOrganization();
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		dc.add(Restrictions.isNull("parentOrg"));
		List<SysOrganization> sysOrganizationList = o_sysOrganizationDAO.findByCriteria(dc);
			if(null != sysOrganizationList && sysOrganizationList.size()>0){
				org = sysOrganizationList.get(0);
			}
		return org;
	}
      /**
	 * 构造根结点的全部树结点：所有子机构、职务.
	 * 
	 * @author lihuanhuan
	 * @param id
	 *            机构id.
	 * @param contextPath
	 *            发布应用路径.
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadOrgTree(String id, String contextPath) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(id)){
				DetachedCriteria criterions=DetachedCriteria.forClass(SysOrganization.class);
				criterions.add(Restrictions.eq("parentOrg.id", id));
				List<SysOrganization> subOrgs = o_sysOrganizationDAO.findByCriteria(criterions);
				for (SysOrganization subOrg : subOrgs) {
					// 0orgtype_c:数据字典条目指定机构类型：总公司id，不允许改变
					// 0orgtype_sc:数据字典条目指定机构类型：分公司id，不允许改变
					if ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType())) {
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("id", subOrg.getId() + RandomUtils.nextInt(9999));
						node.put("name", subOrg.getId());
						node.put("text", subOrg.getOrgname());
						node.put("leaf", subOrg.getIsLeaf());
						node.put("href", contextPath + "/sys/duty/tabs.do?id="+subOrg.getId()+"&nexduty=true");
						node.put("hrefTarget", "mainframe");
						node.put("iconCls", "icon-org");
						node.put("cls", "org");
						node.put("draggable", false);
						nodes.add(node);
				   }
				}
				
				DetachedCriteria dc=DetachedCriteria.forClass(Duty.class);
				dc.add(Restrictions.eq("company.id", id));
				List<Duty> dutys = o_dutyDAO.findByCriteria(dc);
				for (Duty duty : dutys) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", duty.getId());//+RandomUtils.nextInt(9999));
					map.put("leaf", true);
					map.put("text", duty.getDutyName());
					map.put("iconCls", "icon-group");
					map.put("cls", "duty");
					map.put("draggable", false);
					map.put("href", contextPath + "/sys/duty/tabs.do?id=" + duty.getId()+"&nexpeople=true");
					map.put("hrefTarget", "mainframe");
					map.put("draggable", false);
					
					nodes.add(map);
				}
			
		}
		return nodes;
	}
	
	 
	
	
	/**
	 * 根据职务ID更新权重.
	 * @author David
	 * @param dutyId
	 * @param weight
	 * @return boolean
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public boolean updateWeightById(String dutyId,Double weight){
		boolean status = false;
		try {
			Duty duty = o_dutyDAO.get(dutyId);
			duty.setWeight(weight);
			o_dutyDAO.merge(duty);
			o_businessLogBO.modBusinessLogInterface("修改", "部门类型权重", "成功", dutyId,String.valueOf(weight));
			status = true;
		} catch (Exception e) {
			o_businessLogBO.modBusinessLogInterface("修改", "部门类型权重", "失败", dutyId,String.valueOf(weight));
		}
		return status;
	}
	/**
	 * 根据公司ID获取所有职务.
	 * @author David
	 * @param companyId
	 * @return List<Duty>
	 * @since fhd　Ver 1.1
	 */
	public List<Duty> getDutiesByCompanyId(String companyId) {
		return o_dutyDAO.find("from Duty duty where duty.company.id=? and duty.status=?", companyId, 1);
	}
	/**
	 * 根据公司ID获取所有职务.
	 * @author David
	 * @param companyId
	 * @return Page<Duty>
	 * @since fhd　Ver 1.1
	 */
	public Page<Duty> getDutiesByCompanyId(Page<Duty> page,String companyId){
		DetachedCriteria dc = DetachedCriteria.forClass(Duty.class);
		//"E598CCCAD8AFC3EE0354719300E20313"代表状态"可用"
		dc.add(Property.forName("status").eq("E598CCCAD8AFC3EE0354719300E20313"));
		dc.add(Property.forName("company.id").eq(companyId));
		return o_dutyDAO.pagedQuery(dc, page);
	}
	/**
	 * 根据组织ID获取下级组织和相关职务信息.
	 * @param id
	 * @param contextPath
	 * @param choose 
	 * @param checkNode 
	 * @return List<Map<String, Object>>
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, Object>> loadTree(String id, String contextPath, Boolean checkNode, String choose) {
		List<Map<String, Object>> olist = new ArrayList<Map<String, Object>>();
		List<Duty> dutys = this.getAllDutiesByOrgId(UserContext.getUser().getCompanyid());
		for (Duty duty : dutys) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", duty.getId());//+RandomUtils.nextInt(9999));
			map.put("leaf", true);
			map.put("text", duty.getDutyName());
			map.put("iconCls", "icon-group");
			map.put("cls", "duty");
			map.put("draggable", false);
			map.put("href", contextPath + "/sys/duty/tabs.do?id=" + duty.getId()+"&nexpeople=true");
			map.put("hrefTarget", "mainframe");
			if (checkNode) {
				if(StringUtils.isNotBlank(choose)) {
				String[] split = StringUtils.split(choose,",");
					for (String s : split) {
						if (duty.getId().equals(s)) {
							map.put("checked", "true");
							break;
						}
					}
				}
			}
			map.put("draggable", false);
			
			olist.add(map);
		}
		return olist;
	}
	/**
	 * 查询公司的所有职务.
	 * @author 吴德福
	 * @param orgId
	 * @return List<Duty>
	 * @since  fhd　Ver 1.1
	 */
	public List<Duty> getAllDutiesByOrgId(String orgId) {
		if(StringUtils.isNotBlank(orgId)){
			return o_dutyDAO.findBy("company.id", orgId);
		}else{
			return o_dutyDAO.getAll();
		}
	}
	/**
	 * 根据职务ID获取相关职务的人员
	 * @param page
	 * @param id
	 * @param empName
	 * @return Page<SysEmployee>
	 * @return Map<String,Object>
	 */
	public Page<SysEmployee> loadPeople(Page<SysEmployee> page,String id,String empName) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		if(StringUtils.isNotBlank(id)){
			dc.add(Restrictions.eq("duty.id", id));
		}
		if(StringUtils.isNotBlank(empName)){
			dc.add(Property.forName("empname").like(empName, MatchMode.ANYWHERE));
		}
		return o_dutyDAO.pagedQuery(dc, page);
	}
	/**
	 * 根据公司id查询所有职务.
	 * @author 吴德福
	 * @param page
	 * @param id
	 * @param dutyName
	 * @return Page<Duty>
	 * @since  fhd　Ver 1.1
	 */
	public Page<Duty> loadDutysByOrgId(Page<Duty> page,String id,String dutyName) {
		DetachedCriteria dc = DetachedCriteria.forClass(Duty.class);
		if(StringUtils.isNotBlank(id)){
			dc.add(Property.forName("company.id").eq(id));
		}
		if(StringUtils.isNotBlank(dutyName)){
			dc.add(Property.forName("dutyName").like(dutyName, MatchMode.ANYWHERE));
		}
		return o_dutyDAO.pagedQuery(dc, page);
	}
	/**
	 * 更新职务.
	 * @author 吴德福
	 * @param duty
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void merge(Duty duty) {
		o_dutyDAO.merge(duty);
	}
	/**
	 * 根据id删除职务.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void deleteDutyById(String id) {
		o_dutyDAO.removeById(id);

	}
	/**
	 * 根据id查询职务.
	 * @author 吴德福
	 * @param id
	 * @return Duty
	 * @since  fhd　Ver 1.1
	 */
	public Duty get(String id) {
		return o_dutyDAO.get(id);
	}
	/**
	 * 职务添加人员.
	 * @author 吴德福
	 * @param dutyId
	 * @param ids
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void addPeople(String dutyId, String[] ids) {
		Duty duty = o_dutyDAO.get(dutyId);
		for (String peopleId : ids){
			if (StringUtils.isNotBlank(peopleId)) {
				SysEmployee employee = o_sysEmployeeDAO.get(peopleId);
				employee.setDuty(duty);
				o_sysEmployeeDAO.merge(employee);
			}
		}
	}
	/**
	 * 职务删除人员.
	 * @author 吴德福
	 * @param arr
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void deletePeople(String[] arr) {
		for (String peopleId : arr){
			if (!"".equals(peopleId)) {
				SysEmployee employee = o_sysEmployeeDAO.get(peopleId);
				employee.setDuty(null);
				o_sysEmployeeDAO.merge(employee);
			}
		}
	}
	
	/**
	 * 根据查询内容得到员工树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @since fhd Ver 1.1
	 */
	public List<String> getPathsBySearchName(String searchName){
		DetachedCriteria dc = DetachedCriteria.forClass(Duty.class);
		dc.add(Restrictions.ilike("dutyName", searchName, MatchMode.ANYWHERE));
		dc.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
//		dc.add(Restrictions.eq("status", "1"));
		dc.addOrder(Order.asc("dutyName"));
		List<Duty> dutyList = o_dutyDAO.findByCriteria(dc);
		List<String> pathList=new ArrayList<String>();
		for(Duty duty: dutyList){
			pathList.add("/"+duty.getCompany().getId()+"/"+duty.getId());
		}
		return pathList;
	}
}
