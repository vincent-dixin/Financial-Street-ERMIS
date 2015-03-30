package com.fhd.sys.web.controller.sysapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.business.sysapp.ScheduledTaskBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.sysapp.DealMeasure;
import com.fhd.sys.entity.sysapp.ScheduledTask;
import com.fhd.sys.entity.sysapp.ScheduledTaskEmp;
import com.fhd.sys.entity.sysapp.ScheduledTaskTarget;
import com.fhd.sys.web.form.sysapp.ScheduledTaskForm;

/**
 * 计划任务Control.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Controller
public class ScheduledTaskControl {

	@Autowired
	private ScheduledTaskBO o_scheduledTaskBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * 计划任务跳转页面.
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/sys/sysapp/scheduledTaskList.do", method=RequestMethod.GET)
	public String g_scheduledTaskList(Model model){
		model.addAttribute("scheduledTaskForm", new ScheduledTaskForm());
		return "sys/sysapp/scheduledTaskList";
	}
	/**
	 * 计划任务list.
	 * @param limit
	 * @param start
	 * @param taskName
	 * @param type
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/sysapp/scheduledTaskList.do", method=RequestMethod.POST)
	public Map<String,Object> p_scheduledTaskList(Integer limit, Integer start, String taskName, String type, String dir, String sort){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		Page<ScheduledTask> page = new Page<ScheduledTask>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		page = o_scheduledTaskBO.queryScheduledTaskList(page, taskName, type);
		List<ScheduledTask> scheduledTaskList = page.getList();
		if(null != scheduledTaskList && scheduledTaskList.size()>0){
			Map<String, Object> row = null;
			for(ScheduledTask scheduledTask : scheduledTaskList){
				row = new HashMap<String, Object>();
				//计划任务id
				row.put("id", scheduledTask.getId());
				//计划任务名称
				row.put("planName", scheduledTask.getPlanName());
				//触发类型
				if(null != scheduledTask.getTriggerType()){
					row.put("triggerType", scheduledTask.getTriggerType().getName());
				}else{
					row.put("triggerType", "");
				}
				//触发条件
				row.put("triggerTimeText", scheduledTask.getTriggerTimeText());
				//触发方式
				Set<DealMeasure> dealMeasures = scheduledTask.getDealMeasures();
				String dealMeasureStr = "";
				if(null != dealMeasures && dealMeasures.size()>0){
					int i=0;
					for(DealMeasure dealMeasure : dealMeasures){
						if(null != dealMeasure.getDealMeasure()){
							dealMeasureStr += dealMeasure.getDealMeasure().getName();
						}
						if(i != dealMeasures.size()-1){
							dealMeasureStr += " || ";
						}
						i++;
					}
				}
				row.put("dealMeasure", dealMeasureStr);
				//涉及对象
				Set<ScheduledTaskTarget> scheduledTaskTargets = scheduledTask.getScheduledTaskTargets();
				String scheduledTaskEmpStr = "";
				if(null != scheduledTaskTargets && scheduledTaskTargets.size()>0){
					int j=0;
					for(ScheduledTaskTarget scheduledTaskTarget : scheduledTaskTargets){
						if(null != scheduledTaskTarget.getTargetType()){
							scheduledTaskEmpStr += scheduledTaskTarget.getTargetType().getName();
						}
						if(j != scheduledTaskTargets.size()-1){
							scheduledTaskEmpStr += " || ";
						}
						j++;
					}
				}
				row.put("scheduledTaskEmp", scheduledTaskEmpStr);
				if(StringUtils.isNotBlank(scheduledTask.getIsEnabled())){
					if("0".equals(scheduledTask.getIsEnabled())){
						row.put("isEnabled", "未启用");
					}else if("1".equals(scheduledTask.getIsEnabled())){
						row.put("isEnabled", "启用");
					}
				}else{
					row.put("isEnabled", "未启用");
				}
				datas.add(row);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getFullListSize());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		
		return map;
	}
	/**
	 * 新增或修改计划任务跳转页面.
	 * @param model
	 * @param id
	 * @return String
	 */
	@RequestMapping(value = "/sys/sysapp/scheduledTaskAddOrEdit.do")
	public String scheduledTaskAddOrEdit(Model model, String id){
		ScheduledTaskForm scheduledTaskForm = new ScheduledTaskForm();
		if(StringUtils.isNotBlank(id)){
			ScheduledTask scheduledTask = o_scheduledTaskBO.queryScheduledTaskById(id);
			BeanUtils.copyProperties(scheduledTask,scheduledTaskForm);
			//触发类型
			if(null != scheduledTask.getTriggerType()){
				scheduledTaskForm.setType(scheduledTask.getTriggerType().getId());
			}else{
				//时间触发
				scheduledTaskForm.setType("ST_TRIGGER_TYPE_Time");
			}
			//触发方式
			Set<DealMeasure> dealMeasures = scheduledTask.getDealMeasures();
			String dealMeasureStr = "";
			if(null != dealMeasures && dealMeasures.size()>0){
				int i=0;
				for(DealMeasure dealMeasure : dealMeasures){
					if(null != dealMeasure.getDealMeasure()){
						dealMeasureStr += dealMeasure.getDealMeasure().getId();
						//短信模板
						if("ST_DEAL_MEASURE_MSG".equals(dealMeasure.getDealMeasure().getId())){
							scheduledTaskForm.setMsgTemp(dealMeasure.getTemp().getId());
						}
						//邮件模板
						if("ST_DEAL_MEASURE_Mail".equals(dealMeasure.getDealMeasure().getId())){
							scheduledTaskForm.setMailTemp(dealMeasure.getTemp().getId());
						}
						//执行操作(其他)
						if("ST_DEAL_MEASURE_Other".equals(dealMeasure.getDealMeasure().getId())){
							scheduledTaskForm.setOtherMethod(dealMeasure.getDealMeasureDetail());
						}
					}
					if(i != dealMeasures.size()-1){
						dealMeasureStr += ",";
					}
					i++;
				}
			}
			scheduledTaskForm.setMeasure(dealMeasureStr);
			//触发动作
			if(null != scheduledTask.getTriggerSubType()){
				scheduledTaskForm.setSubType(scheduledTask.getTriggerSubType().getId());
			}
			//涉及对象
			Set<ScheduledTaskTarget> scheduledTaskTargets = scheduledTask.getScheduledTaskTargets();
			String targetType = "";
			for(ScheduledTaskTarget scheduledTaskTarget : scheduledTaskTargets){
				int j=0;
				if(null != scheduledTaskTarget.getTargetType()){
					targetType += scheduledTaskTarget.getTargetType().getId();
				}
				if(j != scheduledTaskTargets.size()-1){
					targetType += ",";
				}
				j++;
				scheduledTaskForm.seteType(targetType);
			}
			//触发条件
			scheduledTaskForm.setTriggerTime(scheduledTask.getTriggerTime());
			scheduledTaskForm.setTriggerTimeText(scheduledTask.getTriggerTimeText());
			//提示对象
			Set<ScheduledTaskEmp> scheduledTaskEmps = scheduledTask.getScheduledTaskEmps();
			String emps = "";
			for(ScheduledTaskEmp scheduledTaskEmp : scheduledTaskEmps){
				int j=0;
				if(null != scheduledTaskEmp.getEmp()){
					emps += scheduledTaskEmp.getEmp().getId();
				}
				if(j != scheduledTaskEmps.size()-1){
					emps += ",";
				}
				j++;
				scheduledTaskForm.setEmp(emps);
			}
		}else{
			//时间触发
			scheduledTaskForm.setType("ST_TRIGGER_TYPE_Time");
		}
		//触发类型详细
		model.addAttribute("triggerSubTypeList", o_dictEntryBO.queryEntryByDictTypeId("ST_TRIGGER_SUB_TYPE"));
		//涉及对象
		model.addAttribute("targetTypeList", o_dictEntryBO.queryEntryByDictTypeId("ST_TARGET_TYPE"));
		//执行操作(其他)
		model.addAttribute("dealMeasureDetailList", o_dictEntryBO.queryEntryByDictTypeId("ST_DEAL_MEASURE_DETAIL"));
		//邮件模板
		model.addAttribute("mailTempList", o_scheduledTaskBO.queryScheduledTaskTempByDealMeasure("ST_DEAL_MEASURE_Mail"));
		//短信模板
		model.addAttribute("msgTempList", o_scheduledTaskBO.queryScheduledTaskTempByDealMeasure("ST_DEAL_MEASURE_MSG"));
		//涉及人员树根
		SysOrganization root = o_organizationBO.getRootOrg();
		model.addAttribute("root", root);
		//调度时间标签：开始时间、结束时间
		Calendar calendar = Calendar.getInstance();
		Date startTime = calendar.getTime();
		calendar.setTime(DateUtils.addMonths(calendar.getTime(), 1));
		Date endTime = calendar.getTime();
		scheduledTaskForm.setStartTime(startTime);
		scheduledTaskForm.setEndTime(endTime);
		
		model.addAttribute("scheduledTaskForm", scheduledTaskForm);
		return "sys/sysapp/scheduledTaskAddOrEdit";
	}
	/**
	 * 打开设置调度时间页面.
	 * @author 吴德福	 
	 * @param scheduledTaskForm
	 * @param request
	 * @param hiddenTime
	 * @param displayTime
	 * @param frequency
	 * @param mode
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @param param5
	 * @param startTime
	 * @param range
	 * @param endTime
	 * @return String
	 */
	@RequestMapping(value="/sys/sysapp/setTiming.do", method=RequestMethod.GET)
	public String setTiming(ScheduledTaskForm scheduledTaskForm, HttpServletRequest request, String hiddenTime, String displayTime, String frequency, String mode, String param1, String param2, String param3, String param4, String param5, Date startTime, String range, Date endTime){
		request.setAttribute("hiddenTime", hiddenTime);
		request.setAttribute("displayTime", displayTime);
		request.setAttribute("frequency", frequency);
		request.setAttribute("mode", mode);
		request.setAttribute("param1", param1);
		request.setAttribute("param2", param2);
		request.setAttribute("param3", param3);
		request.setAttribute("param4", param4);
		request.setAttribute("param5", param5);
		request.setAttribute("range", range);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		request.setAttribute("showTime", sdf.format(new Date()));
		//scheduledTaskForm.setShowTime(sdf.format(new Date()));
		if(null == scheduledTaskForm.getStartTime()){
			startTime = calendar.getTime();
		}
		if(null == scheduledTaskForm.getEndTime()){
			calendar.setTime(DateUtils.addMonths(calendar.getTime(), 1));
			endTime = calendar.getTime();
		}
		scheduledTaskForm.setStartTime(startTime);
		scheduledTaskForm.setEndTime(endTime);
		request.setAttribute("scheduledTaskForm", scheduledTaskForm);
		return "sys/sysapp/setTiming";
	}
	/**
	 * 新增或修改计划任务.
	 * @param scheduledTaskForm
	 * @param response
	 */
	@RequestMapping(value = "/sys/sysapp/addScheduledTask.do")
	public void addScheduledTask(ScheduledTaskForm scheduledTaskForm, HttpServletResponse response){
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			o_scheduledTaskBO.saveOrUpdateScheduledTask(scheduledTaskForm);
			out.write("true");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * 根据id集合删除计划任务.
	 * @param ids
	 * @param response
	 */
	@RequestMapping(value = "/sys/sysapp/delScheduledTaskByIds.do")
	public void delScheduledTaskByIds(String ids, HttpServletResponse response){
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			if(StringUtils.isNotBlank(ids)){
				String[] taskIdArray = ids.split(",");
				o_scheduledTaskBO.delScheduledTaskByIds(taskIdArray);
			}
			out.write("true");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * 根据id集合启用计划任务.
	 * @param ids
	 * @param response
	 */
	@RequestMapping(value = "/sys/sysapp/enableScheduledTask.do")
	public void enableScheduledTask(String ids, HttpServletResponse response){
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			if(StringUtils.isNotBlank(ids)){
				String[] taskIdArray = ids.split(",");
				o_scheduledTaskBO.enableScheduledTask(taskIdArray);
			}
			out.write("true");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	@RequestMapping(value = "/sys/sysapp/editScheduledTask.do")
	public void editScheduledTask(HttpServletResponse response){
		
	}

	@RequestMapping(value = "/sys/sysapp/delScheduledTask.do")
	public void delScheduledTask(HttpServletResponse response){
		
	}
	
	@RequestMapping(value = "/sys/sysapp/enabledOrDisableScheduledTask.do")
	public void enabledOrDisableScheduledTask(){
		
	}
}
