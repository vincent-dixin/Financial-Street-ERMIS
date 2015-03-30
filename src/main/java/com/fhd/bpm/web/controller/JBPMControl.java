/**
 * RiskControl.java
 * com.fhd.fdc.commons.web.controller.risk
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-11-18 		David
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.bpm.web.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.model.ActivityCoordinates;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.entity.BusinessWorkFlow;
import com.fhd.bpm.entity.ExamineApproveIdea;
import com.fhd.bpm.entity.JbpmHistActinst;
import com.fhd.bpm.entity.JbpmHistProcinst;
import com.fhd.bpm.entity.ProcessDefinitionDeploy;
import com.fhd.bpm.entity.view.VJbpmHistTask;
import com.fhd.bpm.entity.view.VJbpmDeployment;
import com.fhd.bpm.entity.view.VJbpmExecution;
import com.fhd.bpm.web.form.ProcessDefinitionDeployForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.fdc.utils.Contents;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

@Controller
public class JBPMControl {
	@Autowired
	private JBPMBO o_jBPMBO;
	@Autowired
	ProcessEngine o_processEngine;
	
	/**
	 * 
	 * removePorcessInstance:
	 * 
	 * @author 杨鹏
	 * @param processInstanceIdsStr
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/jbpm/removePorcessInstance.f")
	public void removePorcessInstance(String processInstanceIdsStr,ServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		String[] processInstanceIds = processInstanceIdsStr.split(",");
		o_jBPMBO.deletePorcessInstance(processInstanceIds);
	}

	/**
	 * 
	 * processDefinitionDeployPage:查询流程定义
	 * 
	 * @author 杨鹏
	 * @param query
	 * @param limit
	 * @param start
	 * @param sort
	 * @param dir
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/jbpm/processDefinitionDeploy/processDefinitionDeployPage.f")
	public Map<String,Object> processDefinitionDeployPage(String query,String deleteStatus, Integer limit, Integer start, String sort) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotBlank(sort)){
			sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		}
		Page<ProcessDefinitionDeploy> page=new Page<ProcessDefinitionDeploy>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_jBPMBO.findProcessDefinitionDeployPageBySome(page,query,deleteStatus,sortList);
		List<ProcessDefinitionDeploy> list = page.getResult();
		List<Map<String,Object>> datas = new ArrayList<Map<String, Object>>();
		for (ProcessDefinitionDeploy processDefinitionDeploy : list) {
			Map<String, Object> processDefinitionDeployToMap = processDefinitionDeployToMap(processDefinitionDeploy);
			datas.add(processDefinitionDeployToMap);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	/**
	 * 
	 * findProcessDefinitionDeployById:读取流程定义
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeployId
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/jbpm/processDefinitionDeploy/findProcessDefinitionDeployById.f")
	public Map<String,Object> findProcessDefinitionDeployById(String processDefinitionDeployId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> data = new HashMap<String, Object>();
		ProcessDefinitionDeploy processDefinitionDeploy = o_jBPMBO.findProcessDefinitionDeployById(processDefinitionDeployId);
		String id = processDefinitionDeploy.getId();
		String disName = processDefinitionDeploy.getDisName();
		String deleteStatus = processDefinitionDeploy.getDeleteStatus();
		String url = processDefinitionDeploy.getUrl();
		String fileUploadEntityId = "";
		FileUploadEntity fileUploadEntity = processDefinitionDeploy.getFileUploadEntity();
		if(fileUploadEntity!=null){
			fileUploadEntityId = fileUploadEntity.getId();
			
		}
		data.put("id", id);
		data.put("disName", disName);
		data.put("deleteStatus", deleteStatus);
		data.put("url", url);
		data.put("fileUploadEntityId", fileUploadEntityId);
		map.put("data", data);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * mergeDeployProcessDefinitionDeploy:
	 * 
	 * @author 杨鹏
	 * @param form
	 * @param result
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping("/jbpm/processDefinitionDeploy/mergeDeployProcessDefinitionDeploy.f")
	public Boolean mergeDeployProcessDefinitionDeploy(ProcessDefinitionDeployForm form, BindingResult result)throws Exception {
		Boolean flag=false;
		try {
			ProcessDefinitionDeploy processDefinitionDeploy = null;
			processDefinitionDeploy=new ProcessDefinitionDeploy();
			BeanUtils.copyProperties(form, processDefinitionDeploy);
			processDefinitionDeploy.setId(Identities.uuid());
			FileUploadEntity fileUploadEntity = new FileUploadEntity();
			fileUploadEntity.setId(form.getFileUploadEntityId());
			processDefinitionDeploy.setFileUploadEntity(fileUploadEntity);
			o_jBPMBO.mergeDeployProcessDefinitionDeploy(processDefinitionDeploy);
			flag = true;
		} finally{
			return flag;
		}
	}
	
	/**
	 * 
	 * updateProcessDefinitionDeploy:
	 * 
	 * @author 杨鹏
	 * @param form
	 * @param result
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping("/jbpm/processDefinitionDeploy/updateProcessDefinitionDeploy.f")
	public Boolean updateProcessDefinitionDeploy(ProcessDefinitionDeployForm form, BindingResult result)throws Exception {
		Boolean flag=false;
		try {
			ProcessDefinitionDeploy processDefinitionDeploy = null;
			String id = form.getId();
			processDefinitionDeploy=o_jBPMBO.findProcessDefinitionDeployById(id);
			processDefinitionDeploy.setDisName(form.getDisName());
			processDefinitionDeploy.setUrl(form.getUrl());
			o_jBPMBO.mergeProcessDefinitionDeploy(processDefinitionDeploy);
			flag = true;
		} finally{
			return flag;
		}
	}
	/**
	 * 
	 * removeDeployment:删除发布流程
	 * 
	 * @author 杨鹏
	 * @param deploymentIdsStr
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/jbpm/removeDeployment.f")
	public void removeDeployment(String deploymentIdsStr,ServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		String[] deploymentIds = StringUtils.split(deploymentIdsStr, ",");
		o_jBPMBO.removeDeployment(deploymentIds);
	}
	
	/**
	 * 
	 * deploy:发布流程
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeployId
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/jbpm/processDefinitionDeploy/deploy.f")
	public void deploy(String processDefinitionDeployId) throws Exception {
		o_jBPMBO.deploy(processDefinitionDeployId);
	}
	
	/**
	 * 
	 * vJbpmDeploymentPage:分页查询所有发布后的流程
	 * 
	 * @author 杨鹏
	 * @param query
	 * @param limit
	 * @param start
	 * @param sort
	 * @param dir
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/jbpm/processDefinitionDeploy/vJbpmDeploymentPage.f")
	public Map<String,Object> vJbpmDeploymentPage(String query, Integer limit, Integer start, String sort) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotBlank(sort)){
			sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		}
		Page<VJbpmDeployment> page=new Page<VJbpmDeployment>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_jBPMBO.findVJbpmDeploymentPageBySome(page,query, sortList);
		List<VJbpmDeployment> list = page.getResult();
		List<Map<String,Object>> datas = new ArrayList<Map<String, Object>>();
		for (VJbpmDeployment vJbpmDeployment : list) {
			Map<String, Object> vJbpmDeploymentToMap = vJbpmDeploymentToMap(vJbpmDeployment);
			datas.add(vJbpmDeploymentToMap);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * p_processInstanceList:读取流程实例
	 * 
	 * @author 杨鹏
	 * @param businessName
	 * @param vJbpm4DeploymentName
	 * @param startEmpName
	 * @param limit
	 * @param start
	 * @param sort
	 * @param dir
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/jbpm/processInstance/processInstanceList.f")
	public Map<String,Object> p_processInstanceList(String query,String businessName,String jbpmDeploymentName,String startEmpName,Long dbversion,String assigneeId, Integer limit, Integer start,String sort) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotBlank(sort)){
			sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		}
		Page<JbpmHistProcinst> page=new Page<JbpmHistProcinst>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_jBPMBO.findJbpmHistProcinstPageBySome(page,query, businessName, jbpmDeploymentName,startEmpName,dbversion,assigneeId, sortList);
		List<JbpmHistProcinst> list = page.getResult();
		List<Map<String,Object>> datas = new ArrayList<Map<String, Object>>();
		for (JbpmHistProcinst jbpmHistProcinst : list) {
			Map<String, Object> jbpmHistProcinstToMap = jbpmHistProcinstToMap(jbpmHistProcinst);
			datas.add(jbpmHistProcinstToMap);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * jbpmHistProcinstForm:流程实例信息
	 * 
	 * @author 杨鹏
	 * @param jbpmHistProcinstId
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/jbpm/processinstance/jbpmHistProcinstForm.f")
	public Map<String,Object> jbpmHistProcinstForm(Long jbpmHistProcinstId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> data = new HashMap<String, Object>();
		String businessName="";
		String endactivityStr="";
		String realname="";
		String orgnames="";
		String createTimeStr="";
		String updateTimeStr="";
		
		JbpmHistProcinst jbpmHistProcinst=o_jBPMBO.findJbpmHistProcinstById(jbpmHistProcinstId);
		List<JbpmHistActinst> jbpmHistActinsts = jbpmHistProcinst.getJbpmHistActinsts();
		Date updateTime=null;
		for (JbpmHistActinst jbpmHistActinst : jbpmHistActinsts) {
			Date end = jbpmHistActinst.getEnd();
			Date start = jbpmHistActinst.getStart();
			if(end!=null&&(updateTime==null||updateTime.getTime()<end.getTime())){
				updateTime=end;
			}
			if(start!=null&&(updateTime==null||updateTime.getTime()<start.getTime())){
				updateTime=start;
			}
		}
		businessName=jbpmHistProcinst.getBusinessWorkFlow().getBusinessName();
		String endactivity = jbpmHistProcinst.getEndactivity();
		if("end1".equals(endactivity)){
			endactivityStr="已完成";
		}else if("remove1".equals(endactivity)){
			endactivityStr="已完成";
		}else{
			endactivityStr="进行中";
		}
		realname=jbpmHistProcinst.getBusinessWorkFlow().getCreateByEmp().getRealname();
		Set<SysEmpOrg> sysEmpOrgs = jbpmHistProcinst.getBusinessWorkFlow().getCreateByEmp().getSysEmpOrgs();
		List<String> orgnameList = new ArrayList<String>();
		for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
			orgnameList.add(sysEmpOrg.getSysOrganization().getOrgname());
		}
		orgnames=StringUtils.join(orgnameList.toArray(),",");
		Date createTime = jbpmHistProcinst.getBusinessWorkFlow().getCreateTime();
		createTimeStr=DateUtils.formatDate(createTime, "yyyy年MM月dd日 hh:mm:ss");
		updateTimeStr=DateUtils.formatDate(updateTime, "yyyy年MM月dd日 hh:mm:ss");
		
		data.put("businessName", businessName);
		data.put("endactivity", endactivityStr);
		data.put("realname", realname);
		data.put("orgnames", orgnames);
		data.put("createTime", createTimeStr);
		data.put("updateTime", updateTimeStr);
		map.put("data", data);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * jbpmHistActinstPage:
	 * 
	 * @author 杨鹏
	 * @param assigneeId
	 * @param jbpmHistProcinstId
	 * @param dbversion
	 * @param limit
	 * @param start
	 * @param sort
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/jbpm/processInstance/jbpmHistActinstPage.f")
	public Map<String,Object> jbpmHistActinstPage(String query,String assigneeId,Long jbpmHistProcinstId,String endactivity,Long dbversion,Integer limit, Integer start, String sort)  {
		Map<String,Object> map = new HashMap<String, Object>();
		Page<JbpmHistActinst> page=new Page<JbpmHistActinst>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotBlank(sort)){
			sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		}
		page=o_jBPMBO.findJbpmHistActinstPageBySome(page,query,assigneeId, jbpmHistProcinstId,endactivity,dbversion, sortList);
		List<JbpmHistActinst> list = page.getResult();
		List<Map<String,Object>> datas = new ArrayList<Map<String, Object>>();
		for (JbpmHistActinst jbpmHistActinst : list) {
			Map<String, Object> jbpmHistActinstToMap = jbpmHistActinstToMap(jbpmHistActinst);
			datas.add(jbpmHistActinstToMap);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * 
	 * flowChart:读取流程图
	 * 
	 * @author 杨鹏
	 * @param jbpmHistProcinstId:发布流程ID
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/jbpm/flowChart.f")
	public void flowChart(Long jbpmHistProcinstId, ServletResponse response) throws Exception {
		OutputStream outputStream=null;
		InputStream inputStream=null;
		try {
			outputStream = response.getOutputStream();
			RepositoryService repositoryService = o_processEngine.getRepositoryService();
			JbpmHistProcinst jbpmHistProcinst = o_jBPMBO.findJbpmHistProcinstById(jbpmHistProcinstId);
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(jbpmHistProcinst.getvJbpmDeployment().getPdid()).uniqueResult();
			inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getImageResourceName());
			response.reset();
			IOUtils.copy(inputStream, outputStream);
		} finally{
			if(null!=outputStream){
				outputStream.flush();
				outputStream.close();
			}
			if(null!=inputStream){
				inputStream.close();
			}
		}
	}
	
	/**
	 * 
	 * toDoTaskAC:读取当前代办活动坐标
	 * 
	 * @author 杨鹏
	 * @param processInstanceId
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/jbpm/toDoTaskAC.f")
	public List<Map<String, Object>> toDoTaskAC(String processInstanceId) {
		ExecutionService executionService = o_processEngine.getExecutionService();
		ProcessInstance processInstance = executionService.findProcessInstanceById(processInstanceId);
		List<Map<String, Object>> activityCoordinatesList = new ArrayList<Map<String, Object>>();
		if(processInstance!=null){
			RepositoryService repositoryService = o_processEngine.getRepositoryService();
			Set<String> activityNames = processInstance.findActiveActivityNames();
			for (String activityName : activityNames) {
				ActivityCoordinates activityCoordinates = repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(),activityName);
				Map<String, Object> activityCoordinatesMap=new HashMap<String, Object>();
				activityCoordinatesMap.put("height", activityCoordinates.getHeight());
				activityCoordinatesMap.put("width", activityCoordinates.getWidth());
				activityCoordinatesMap.put("x", activityCoordinates.getX());
				activityCoordinatesMap.put("y", activityCoordinates.getY());
				activityCoordinatesList.add(activityCoordinatesMap);
			}
		}
		return activityCoordinatesList;
	}
	
	/**
	 * 
	 * vJbpmExecutionToMap:将工作流实例视图转换为MAP格式
	 * 
	 * @author 杨鹏
	 * @param vJbpmExecution
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public static Map<String,Object> vJbpmExecutionToMap(VJbpmExecution vJbpmExecution){
		String id = vJbpmExecution.getId();
		String businessName = vJbpmExecution.getBusinessName();
		String jbpmDeploymentName=vJbpmExecution.getJbpmDeploymentName();
		String startEmpName = "";
		if(vJbpmExecution.getStartEmp()!=null){
			startEmpName = vJbpmExecution.getStartEmp().getEmpname();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("businessName", businessName);
		map.put("jbpmDeploymentName", jbpmDeploymentName);
		map.put("startEmpName", startEmpName);
		
		return map;
	}
	/**
	 * 
	 * jbpmHistProcinstToMap:jbpm历史实例转Map
	 * 
	 * @author 杨鹏
	 * @param jbpmHistProcinst
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> jbpmHistProcinstToMap(JbpmHistProcinst jbpmHistProcinst){
		Map<String,Object> map = new HashMap<String, Object>();
		if(jbpmHistProcinst!=null){
			Long id = jbpmHistProcinst.getId();
			String endactivity=jbpmHistProcinst.getEndactivity();
			String endactivityShow="";
			BusinessWorkFlow businessWorkFlow = jbpmHistProcinst.getBusinessWorkFlow();
			
			String businessName="";
			String createTime="";
			String createByRealname="";
			String processInstanceId="";
			String jbpmDeploymentName = "";
			String url="";
			String businessId = "";
			
			if("end1".equals(endactivity)){
				endactivityShow="已完成";
			}else if("remove1".equals(endactivity)){
				endactivityShow="已删除";
			}else{
				endactivityShow="执行中";
			}

			if(businessWorkFlow!=null){
				businessName = businessWorkFlow.getBusinessName();
				url = businessWorkFlow.getUrl();
				businessId = businessWorkFlow.getBusinessId();
				SysEmployee createByEmp = businessWorkFlow.getCreateByEmp();
				if(null != createByEmp){
					createByRealname = createByEmp.getRealname();
				}
				Date date = businessWorkFlow.getCreateTime();
				if(date!=null){
					createTime = new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(date);
				}
				processInstanceId = businessWorkFlow.getProcessInstanceId();
			}
			VJbpmDeployment vJbpmDeployment = jbpmHistProcinst.getvJbpmDeployment();
			if(vJbpmDeployment!=null){
				ProcessDefinitionDeploy processDefinitionDeploy = vJbpmDeployment.getProcessDefinitionDeploy();
				if(null!=processDefinitionDeploy){
					jbpmDeploymentName = processDefinitionDeploy.getDisName();
				}
			}
			
			map.put("id", id);
			map.put("endactivity", endactivity);
			map.put("endactivityShow", endactivityShow);
			map.put("businessName", businessName);
			map.put("createByRealname", createByRealname);
			map.put("createTime", createTime);
			map.put("jbpmDeploymentName", jbpmDeploymentName);
			map.put("processInstanceId", processInstanceId);
			map.put("url", url);
			map.put("businessId", businessId);
		}
		return map;
	}

	/**
	 * 
	 * processDefinitionDeployToMap:将流程定义类转为map
	 * 
	 * @author 杨鹏
	 * @param processDefinitionDeploy
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> processDefinitionDeployToMap(ProcessDefinitionDeploy processDefinitionDeploy){
		String id = processDefinitionDeploy.getId();
		String disName = processDefinitionDeploy.getDisName();
		String url = processDefinitionDeploy.getUrl();
		String fileName="";
		
		FileUploadEntity fileUploadEntity = processDefinitionDeploy.getFileUploadEntity();
		if(null!=fileUploadEntity){
			fileName = fileUploadEntity.getOldFileName();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("disName", disName);
		map.put("url", url);
		map.put("fileName", fileName);
		return map;
	}
	
	/**
	 * 
	 * vJbpmDeploymentToMap:将发布后的流程转为map格式
	 * 
	 * @author 杨鹏
	 * @param vJbpmDeployment
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> vJbpmDeploymentToMap(VJbpmDeployment vJbpmDeployment){
		String id = vJbpmDeployment.getId();
		String pdid = vJbpmDeployment.getPdid();
		Integer pdversion = vJbpmDeployment.getPdversion();
		Integer executionCount = vJbpmDeployment.getExecutionCount();
		String processDefinitionDeployId=null;
		String disName = "";
		
		if(null==executionCount){
			executionCount=0;
		}
		ProcessDefinitionDeploy processDefinitionDeploy = vJbpmDeployment.getProcessDefinitionDeploy();
		if(null!=processDefinitionDeploy){
			processDefinitionDeployId = processDefinitionDeploy.getId();
			disName = processDefinitionDeploy.getDisName();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("pdid", pdid);
		map.put("pdversion", pdversion);
		map.put("executionCount", executionCount);
		map.put("processDefinitionDeployId", processDefinitionDeployId);
		map.put("disName", disName);
		return map;
	}
	/**
	 * 
	 * jbpmHistActinstToMap:将历史节点转成MAP
	 * 
	 * @author 杨鹏
	 * @param jbpmHistActinst
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,Object> jbpmHistActinstToMap(JbpmHistActinst jbpmHistActinst){
		Long id=jbpmHistActinst.getId();
		Long dbversion =null;
		String disName = "";
		
		String processInstanceId = "";
		String dbversionStr="";
		String activityName = jbpmHistActinst.getActivityName();
		String businessId="";
		String businessName="";
		String assigneeRealname="";
		String assigneeRealCode="";
		String assigneeCompanyName="";
		String assigneeOrgNamesStr="";
		//办理部门极为影响运行效率
		/*List<String> assigneeOrgNames = new ArrayList<String>();*/
		String createByRealname="";
		String startStr="";
		String ea_Content="";
		String ea_Operate = "";
		String gatherBy="";
		String endStr="";
		String form="";
		
		JbpmHistProcinst jbpmHistProcinst = jbpmHistActinst.getJbpmHistProcinst();
		VJbpmDeployment vJbpmDeployment = jbpmHistProcinst.getvJbpmDeployment();
		ProcessDefinitionDeploy processDefinitionDeploy = vJbpmDeployment.getProcessDefinitionDeploy();
		disName = processDefinitionDeploy.getDisName();
		dbversion = jbpmHistActinst.getDbversion();
		if(dbversion==0){
			dbversionStr="未处理";
		}else if(dbversion==1){
			dbversionStr="已处理";
		}
		VJbpmHistTask jbpmHistTask = jbpmHistActinst.getJbpmHistTask();
		
		if(jbpmHistTask!=null){
			SysEmployee assignee = jbpmHistTask.getAssignee();
			SysEmployee taskAssignee = jbpmHistTask.getTaskAssignee();
			if(assignee!=null){
				assigneeRealCode=assignee.getEmpcode();
				assigneeRealname = assignee.getRealname();
				SysOrganization assigneeCompany = assignee.getSysOrganization();
				if(assigneeCompany!=null){
					assigneeCompanyName=assigneeCompany.getOrgname();
				}
				/*Set<SysEmpOrg> sysEmpOrgs = assignee.getSysEmpOrgs();
				for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
					assigneeOrgNames.add(sysEmpOrg.getSysOrganization().getOrgname());
				}*/
			}else if(taskAssignee!=null){
				assigneeRealCode=taskAssignee.getEmpcode();
				assigneeRealname=taskAssignee.getRealname();
				SysOrganization assigneeCompany = taskAssignee.getSysOrganization();
				if(assigneeCompany!=null){
					assigneeCompanyName=assigneeCompany.getOrgname();
				}
				/*Set<SysEmpOrg> sysEmpOrgs = taskAssignee.getSysEmpOrgs();
				for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
					assigneeOrgNames.add(sysEmpOrg.getSysOrganization().getOrgname());
				}*/
			}
			/*assigneeOrgNamesStr=StringUtils.join(assigneeOrgNames.toArray(),",");*/
			
			ExamineApproveIdea examineApproveIdea = jbpmHistTask.getExamineApproveIdea();
			if(examineApproveIdea!=null){
				ea_Content=examineApproveIdea.getEa_Content();
				ea_Operate = examineApproveIdea.getEa_Operate();
				gatherBy = examineApproveIdea.getGatherBy().getRealname();
			}
			
			Date end = jbpmHistTask.getEnd();
			if(end!=null){
				endStr=new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(end);
			}
			form = jbpmHistTask.getForm();
		}
		if(jbpmHistProcinst!=null){
			BusinessWorkFlow businessWorkFlow = jbpmHistProcinst.getBusinessWorkFlow();
			businessId = businessWorkFlow.getId();
			businessName = businessWorkFlow.getBusinessName();
			if(businessWorkFlow!=null){
				processInstanceId = businessWorkFlow.getProcessInstanceId();
				String createBy = businessWorkFlow.getCreateBy();
				if(null != createBy && !Contents.SYSTEM.equals(createBy)){
					createByRealname = businessWorkFlow.getCreateByEmp().getRealname();
				} else {
					createByRealname = createBy;
				}
			}
		}
		Date start = jbpmHistActinst.getStart();
		if(start!=null){
			startStr = new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(start);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("disName", disName);
		map.put("activityName", activityName);
		map.put("dbversion", dbversion);
		map.put("dbversionStr", dbversionStr);
		map.put("businessId", businessId);
		map.put("businessName", businessName);
		map.put("assigneeRealCode", assigneeRealCode);
		map.put("assigneeRealname", assigneeRealname);
		map.put("assigneeCompanyName", assigneeCompanyName);
		map.put("assigneeOrgNamesStr", assigneeOrgNamesStr);
		map.put("createByRealname", createByRealname);
		map.put("startStr", startStr);
		map.put("ea_Content", ea_Content);
		map.put("ea_Operate", ea_Operate);
		map.put("gatherBy", gatherBy);
		map.put("endStr", endStr);
		map.put("form", form);
		map.put("processInstanceId", processInstanceId);
		
		return map;
	}
}
