/**
 * BusinessBO.java
 * com.fhd.fdc.commons.business.log
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-27 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.business.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.dao.log.BusinessLogDAO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.log.BusinessLog;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.web.form.log.BusinessLogForm;

/**
 * 业务日志BO类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-9-3 Company FirstHuiDa.
 */

@Service
@SuppressWarnings("unchecked")
public class BusinessLogBO {

	@Autowired
	private BusinessLogDAO o_businessLogDAO;
	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	
	/**
	 * 新增业务日志.
	 * @author 吴德福
	 * @param businessLog 业务日志.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveBusinessLog(BusinessLog businessLog) {
		o_businessLogDAO.merge(businessLog);
	}
	/**
	 * 删除业务日志.
	 * @author 吴德福
	 * @param id 业务日志id.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeBusinessLog(String id) {
		o_businessLogDAO.removeById(id);
	}
	/**
	 * 根据id查询业务日志.
	 * @author 吴德福
	 * @param id 业务日志id.
	 * @return BusinessLog 业务日志.
	 * @since fhd　Ver 1.1
	 */
	public BusinessLog queryBusinessLogById(String id) {
		return o_businessLogDAO.get(id);
	}
	/**
	 * 查询所有的业务日志.
	 * @author 吴德福
	 * @param page 分页信息.
	 * @return Page<BusinessLog> 业务日志集合.
	 * @since fhd　Ver 1.1
	 */
	/*
	public Page<BusinessLog> queryAllBusinessLog(Page<BusinessLog> page){
		DetachedCriteria dc = DetachedCriteria.forClass(BusinessLog.class);
		dc.setFetchMode("sysOrganization", FetchMode.SELECT);
		dc.addOrder(Order.desc("operateTime"));
		return o_businessLogDAO.pagedQuery(dc, page);
 	}
	*/

	/**
	 * 根据登录用户查询用户所属机构的业务日志.
	 * @author 吴德福
	 * @param page 分页信息.
	 * @return Page<BusinessLog> 业务日志集合.
	 * @since fhd　Ver 1.1
	 */
	public Page<BusinessLog> queryAllBusinessLogByUser(Page<BusinessLog> page,
			String orgid) {
		DetachedCriteria dc = DetachedCriteria.forClass(BusinessLog.class);
		dc.setFetchMode("sysOrganization", FetchMode.SELECT);
		dc.createAlias("sysOrganization", "o");
		dc.add(Restrictions.eq("o.id", orgid));
		dc.addOrder(Order.desc("operateTime"));
		return o_businessLogDAO.pagedQuery(dc, page);
	}

	/**
	 * 查询所有用户日志.
	 * @author 王龙 
	 * @param page
	 * @param username
	 * @param operateTime
	 * @return Page<BusinessLog>
	 */
	public Page<BusinessLog> queryAllBusinessLog(Page<BusinessLog> page,
			String username, String beginTime, String endTime,String sort,String dir) throws Exception {

		DetachedCriteria dc = DetachedCriteria.forClass(BusinessLog.class);
		dc.createAlias("sysUser", "user");

		if (StringUtils.isNotBlank(username)) {
			dc.add(Restrictions.like("user.username", username,	MatchMode.ANYWHERE));
		}
		if(null != beginTime || null != endTime){
			beginTime = beginTime + " 00:00:00";
			endTime = endTime + " 23:59:59";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    dc.add(Restrictions.ge("operateTime",sdf.parse(beginTime)));
		    dc.add(Restrictions.le("operateTime",sdf.parse(endTime)));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"username".equals(sort) && !"orgname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.asc(sort));
			}else if("orgname".equals(sort)){
				dc.createAlias("sysOrganization", "org");
				dc.addOrder(Order.asc("org.orgname"));
			}
		}else {
			if(!"username".equals(sort) && !"orgname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.desc(sort));
			}else if("orgname".equals(sort)){
				dc.createAlias("sysOrganization", "org");
				dc.addOrder(Order.desc("org.orgname"));
			}
		}
		
		if("operateTime".equals(sort) && "asc".equals(dir)){
			dc.addOrder(Order.asc("operateTime"));
		}else{
			dc.addOrder(Order.desc("operateTime"));
		}

		return o_businessLogDAO.pagedQuery(dc, page);
	}

	/**
	 * 新增模块记录日志.
	 * <pre>
	 * 吴德福在2010-8-30新增权限成功。
	 * ... ...
	 * </pre>
	 * @author 吴德福
	 * @param operateType 操作类型：新增.
	 * @param moduleName 模块名称.
	 * @param isSuccess 操作结果.
	 * @return boolean 记录日志是否成功.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public boolean saveBusinessLogInterface(String operateType,
			String moduleName, String isSuccess, String... params) {
		BusinessLog businessLog = new BusinessLog();
		businessLog.setId(Identities.uuid2());
		SysUser sysUser = null;
		SysOrganization sysOrganization = null;
		if(null != UserContext.getUser()){
			sysUser = o_sysUserBO.get(UserContext.getUser().getUserid());
			if(StringUtils.isNotBlank(UserContext.getUser().getCompanyid())){
				sysOrganization = new SysOrganization();
				sysOrganization.setId(UserContext.getUser().getCompanyid());
			}
		}else{
			//设置系统用户
			sysUser = o_sysUserBO.getByUsername("admin");
			sysOrganization = o_organizationBO.getRootOrg();
		}
		businessLog.setSysUser(sysUser);
		if(null != sysOrganization){
			businessLog.setSysOrganization(sysOrganization);
		}
		businessLog.setOperateTime(new Date());
		businessLog.setOperateType(operateType);
		businessLog.setModuleName(moduleName);
		businessLog.setIsSuccess(isSuccess);
		StringBuilder record = new StringBuilder();
		if(null != sysUser){
			record.append(sysUser.getRealname());
		}else{
			record.append("admin");
		}
		record.append("在"
				+ DateUtils.formatDate(businessLog.getOperateTime(),"yyyy-MM-dd hh:mm:ss") + businessLog.getOperateType()
				+ businessLog.getModuleName()
				+ businessLog.getIsSuccess());
		if(null != params && params.length > 0){
			record.append("，参数为：");
			for (int i = 0; i < params.length; i++) {
				record.append(params[i]);
				if (i != params.length - 1) {
					record.append(",");
				}
			}
		}

		businessLog.setOperateRecord(record.toString());
		saveBusinessLog(businessLog);
		return true;
	}

	/**
	 * 修改模块记录日志.
	 * 
	 * <pre>
	 * 吴德福在2010-8-30修改权限记录id:'402881b22ac1c54f012ac1c7a47f0001'成功。
	 * ... ...
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param operateType 操作类型：修改.
	 * @param moduleName 模块名称.
	 * @param id 修改的记录id.
	 * @param isSuccess 操作结果.
	 * @return boolean 记录日志是否成功.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public boolean modBusinessLogInterface(String operateType,
			String moduleName, String isSuccess, String id, String... params) {
		BusinessLog businessLog = new BusinessLog();
		businessLog.setId(Identities.uuid2());
		SysUser sysUser = null;
		SysOrganization sysOrganization = null;
		if(null != UserContext.getUser()){
			sysUser = o_sysUserBO.get(UserContext.getUser().getUserid());
			if(StringUtils.isNotBlank(UserContext.getUser().getCompanyid())){
				sysOrganization = new SysOrganization();
				sysOrganization.setId(UserContext.getUser().getCompanyid());
			}
		}else{
			//设置系统用户
			sysUser = o_sysUserBO.getByUsername("admin");
			sysOrganization = o_organizationBO.getRootOrg();
		}
		businessLog.setSysUser(sysUser);
		if(null != sysOrganization){
			businessLog.setSysOrganization(sysOrganization);
		}
		businessLog.setOperateTime(new Date());
		businessLog.setOperateType(operateType);
		businessLog.setModuleName(moduleName);
		businessLog.setIsSuccess(isSuccess);
		StringBuilder record = new StringBuilder();
		if(null != sysUser){
			record.append(sysUser.getRealname());
		}else{
			record.append("admin");
		}
		record.append("在"
				+ DateUtils.formatDate(businessLog.getOperateTime(),"yyyy-MM-dd hh:mm:ss") + businessLog.getOperateType()
				+ businessLog.getModuleName() + "记录id:'" + id
				+ "'" + businessLog.getIsSuccess());
		if(null != params && params.length > 0){
			record.append("，参数为：");
			for (int i = 0; i < params.length; i++) {
				record.append(params[i]);
				if (i != params.length - 1) {
					record.append(",");
				}
			}
		}
		businessLog.setOperateRecord(record.toString());
		saveBusinessLog(businessLog);
		return true;
	}

	/**
	 * 删除模块记录日志.
	 * 
	 * <pre>
	 * 吴德福在2010-8-30删除权限记录id:'402881b22ac1c54f012ac1c7a47f0001'成功。
	 * 吴德福在2010-8-30删除权限记录id:'402881b22ac1c54f012ac1c7a47f0002'成功。
	 * 吴德福在2010-8-30删除权限记录id:'402881b22ac1c54f012ac1c7a47f0003'成功。
	 * ... ...
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param operateType 操作类型：删除.
	 * @param moduleName 模块名称.
	 * @param ids 删除的记录id数组.
	 * @param isSuccess 操作结果.
	 * @return boolean 记录日志是否成功.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public boolean delBusinessLogInterface(String operateType,
			String moduleName, String isSuccess, String id) {
		BusinessLog businessLog = null;
		businessLog = new BusinessLog();
		businessLog.setId(Identities.uuid2());
		SysUser sysUser = null;
		SysOrganization sysOrganization = null;
		if(null != UserContext.getUser()){
			sysUser = o_sysUserBO.get(UserContext.getUser().getUserid());
			if(StringUtils.isNotBlank(UserContext.getUser().getCompanyid())){
				sysOrganization = new SysOrganization();
				sysOrganization.setId(UserContext.getUser().getCompanyid());
			}
		}else{
			//设置系统用户
			sysUser = o_sysUserBO.getByUsername("admin");
			sysOrganization = o_organizationBO.getRootOrg();
		}
		businessLog.setSysUser(sysUser);
		if(null != sysOrganization){
			businessLog.setSysOrganization(sysOrganization);
		}
		businessLog.setOperateTime(new Date());
		businessLog.setOperateType(operateType);
		businessLog.setModuleName(moduleName);
		businessLog.setIsSuccess(isSuccess);
		StringBuilder record = new StringBuilder();
		if(null != sysUser){
			record.append(sysUser.getRealname());
		}else{
			record.append("admin");
		}
		record.append("在"+ DateUtils.formatDate(businessLog.getOperateTime(),"yyyy-MM-dd hh:mm:ss") + businessLog.getOperateType()
				+ businessLog.getModuleName() + "记录id:'" + id + "'"+ businessLog.getIsSuccess());
		businessLog.setOperateRecord(record.toString());
		saveBusinessLog(businessLog);
		return true;
	}

	/**
	 * 查询模块记录日志.
	 * 
	 * <pre>
	 * 吴德福在2010-8-30查询权限成功。
	 * ... ...
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param operateType 操作类型：查询.
	 * @param moduleName 模块名称.
	 * @param isSuccess 操作结果.
	 * @return boolean 记录日志是否成功.
	 * @since fhd　Ver 1.1
	 */
	public boolean queryBusinessLogInterface(String operateType,
			String moduleName, String isSuccess) {
		BusinessLog businessLog = new BusinessLog();
		businessLog.setId(Identities.uuid2());
		SysUser sysUser = null;
		SysOrganization sysOrganization = null;
		if(null != UserContext.getUser()){
			sysUser = o_sysUserBO.get(UserContext.getUser().getUserid());
			if(StringUtils.isNotBlank(UserContext.getUser().getCompanyid())){
				sysOrganization = new SysOrganization();
				sysOrganization.setId(UserContext.getUser().getCompanyid());
			}
		}else{
			//设置系统用户
			sysUser = o_sysUserBO.getByUsername("admin");
			sysOrganization = o_organizationBO.getRootOrg();
		}
		businessLog.setSysUser(sysUser);
		if(null != sysOrganization){
			businessLog.setSysOrganization(sysOrganization);
		}
		businessLog.setOperateTime(new Date());
		businessLog.setOperateType(operateType);
		businessLog.setModuleName(moduleName);
		businessLog.setIsSuccess(isSuccess);
		StringBuilder record = new StringBuilder();
		if(null != sysUser){
			record.append(sysUser.getRealname());
		}else{
			record.append("admin");
		}
		record.append("在"
				+ DateUtils.formatDate(businessLog.getOperateTime(),"yyyy-MM-dd hh:mm:ss") + businessLog.getOperateType()
				+ businessLog.getModuleName()
				+ businessLog.getIsSuccess());
		businessLog.setOperateRecord(record.toString());
		saveBusinessLog(businessLog);
		return true;
	}

	/**
	 * 查询指定用户的操作日志.
	 * 
	 * <pre>
	 * 吴德福在2010-8-30查询用户成功。
	 * 吴德福在2010-8-30查角色成功。
	 * 吴德福在2010-8-30查询权限成功。
	 * 吴德福在2010-8-30查询模块成功。
	 * ... ...
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param userId 用户id.
	 * @since fhd　Ver 1.1
	 */
	public List<BusinessLog> queryBusinessLogByUserId(String userId) {
		return o_businessLogDAO.findBy("sysUser.id", userId, "operateTime",	false);
	}

	/**
	 * 查询指定时间段内的操作日志.
	 * 
	 * <pre>
	 * 吴德福在2010-8-30查询用户成功。
	 * 吴德福在2010-8-30查角色成功。
	 * 吴德福在2010-8-30查询权限成功。
	 * 吴德福在2010-8-30查询模块成功。
	 * ... ...
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param userId 用户id.
	 * @since fhd　Ver 1.1
	 */
	public List<BusinessLog> queryBushinessLogByOperateTime(Date beginTime,
			Date endTime) {
		StringBuilder hql = new StringBuilder();
		hql.append("From BusinessLog Where 1=1 ");

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate = sf.format(beginTime);
		hql.append(" and operateTime >= '");
		hql.append(beginDate);
		hql.append("'");

		String endDate = sf.format(endTime);
		hql.append(" and operateTime <= '");
		hql.append(endDate);
		hql.append("'");
		return o_businessLogDAO.find(hql.toString());
	}

	/**
	 * 根据查询条件查询业务日志.
	 * @author 吴德福
	 * @param businessLogForm
	 * @return List<BusinessLog> 业务日志集合.
	 * @since fhd　Ver 1.1
	 */
	public List<BusinessLog> findBusinessLogListBySome(BusinessLogForm businessLogForm) {
		StringBuilder hql = new StringBuilder();
		hql.append("From BusinessLog Where 1=1 ");
		String userId = "";
		String moduleId = "";
		Date beginTime = null;
		Date endTime = null;
		if (businessLogForm != null && !"".equals(businessLogForm.getUserId())
				&& businessLogForm.getUserId() != null) {
			userId = businessLogForm.getUserId();
			hql.append(" and sysUser.username like '%" + userId + "%'");
		}
		if (businessLogForm != null && !"".equals(businessLogForm.getUserId())
				&& businessLogForm.getModuleId() != null) {
			moduleId = businessLogForm.getModuleId();
			hql.append(" and sysModule.id like '%" + moduleId + "%'");
		}
		if (businessLogForm != null && businessLogForm.getBeginTime() != null
				&& businessLogForm.getEndTime() != null) {
			beginTime = businessLogForm.getBeginTime();
			endTime = businessLogForm.getEndTime();

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String beginDate = sf.format(beginTime);
			hql.append(" and operateTime >= '");
			hql.append(beginDate);
			hql.append("'");

			String endDate = sf.format(endTime);
			hql.append(" and operateTime <= '");
			hql.append(endDate);
			hql.append("'");
		}
		return o_businessLogDAO.find(hql.toString());
	}

	/**
	 * 根据查询条件查询业务日志.
	 * @author 吴德福
	 * @param businessLogForm
	 * @return List<BusinessLog> 业务日志集合.
	 * @since fhd　Ver 1.1
	 */
	public Page<BusinessLog> queryByParams(Page<BusinessLog> page,
			String moduleName, String operateType) {
		DetachedCriteria dc = DetachedCriteria.forClass(BusinessLog.class);
		if (StringUtils.isNotBlank(moduleName))
			dc.add(Property.forName("moduleName").like(moduleName));
		if (StringUtils.isNotBlank(operateType))
			dc.add(Property.forName("operateType").like(operateType));
		return o_businessLogDAO.pagedQuery(dc, page);
	}
	
	/**
	 * <pre>
	 * addModBusinessLog:添加指定人员的操作日志
	 * </pre>
	 * 
	 * @author David
	 * @param operatorId
	 * @param operateType
	 * @param moduleName
	 * @param isSuccess
	 * @param id
	 * @param params
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public boolean addModBusinessLog(String operatorId,String operateType,
			String moduleName, String isSuccess, String id, String... params) {
		BusinessLog businessLog = new BusinessLog();
		businessLog.setId(Identities.uuid2());
		SysUser sysUser = new SysUser();
		sysUser.setId(operatorId);
		businessLog.setSysUser(sysUser);
		SysEmployee employee = o_employeeBO.getEmployee(sysUser.getId());
		if (null != employee) {
			businessLog.setSysOrganization(employee.getSysOrganization());
		}
		businessLog.setOperateTime(new Date());
		businessLog.setOperateType(operateType);
		businessLog.setModuleName(moduleName);
		businessLog.setIsSuccess(isSuccess);
		StringBuilder record = new StringBuilder();
		record.append(employee.getEmpname());
		record.append("在"
				+ DateUtils.formatDate(businessLog.getOperateTime(),"yyyy-MM-dd hh:mm:ss") + businessLog.getOperateType()
				+ businessLog.getModuleName() + "记录id:'" + id
				+ "'" + businessLog.getIsSuccess());
		if(null != params && params.length > 0){
			record.append("，参数为：");
			for (int i = 0; i < params.length; i++) {
				record.append(params[i]);
				if (i != params.length - 1) {
					record.append(",");
				}
			}
		}
		businessLog.setOperateRecord(record.toString());
		saveBusinessLog(businessLog);
		return true;
	} 
}
