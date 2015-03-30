/**
 * PlanControl.java
 * com.fhd.sys.web.controller.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * PlanControl.java
 * com.fhd.sys.web.controller.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.controller.st;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.business.st.PlanBO;
import com.fhd.sys.business.st.PlanEmpBO;
import com.fhd.sys.business.st.TempBO;
import com.fhd.sys.business.st.task.QuartzManagerBO;
import com.fhd.sys.business.st.task.TriggerEmailBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.st.Plan;
import com.fhd.sys.entity.st.PlanEmp;
import com.fhd.sys.entity.st.Temp;
import com.fhd.sys.web.form.st.PlanForm;

/**
 * 计划任务控制分发
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午03:24:11
 *
 * @see 	 
 */
@Controller
public class PlanControl {
	@Autowired
	private PlanBO o_planBO;
	
	@Autowired
	private DictBO o_dictBO;
	
	@Autowired
	private TempBO o_tempBO;
	
	@Autowired
	private PlanEmpBO o_planEmpBO;
	
	@Autowired
	private QuartzManagerBO o_quartz_BO;
	
	@Autowired
	private TriggerEmailBO o_triggerEmail_BO;
	
	/**
	 * 保存 
	 * @author 金鹏祥
	 * @param data
	 * @param id plan对象实体id
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/sys/st/savePlan.f")
	public void savePlan(PlanForm data,String id, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String isSave = "false";
		String triggerDateText = "";
		String triggerDataSet = "";
		String triggetTime = data.getTriggetTime();
		String triggetData = data.getTriggerData();
		String tx = data.getTx();
		String mangetTime = data.getManageTime();
		Plan plan = new Plan(Identities.uuid());
		int hour = 0;
		int minute = 0;
		String editRule = "";
		int week = 0;
		
		if(StringUtils.isNotBlank(triggetTime)){
			String triggetTimes[] = triggetTime.split(" ");
			if(triggetTimes[1].indexOf("下午") != -1 && triggetTimes[0].indexOf("12") == -1){
				String hours[] = triggetTimes[0].toString().split(":");
				int hourInt = Integer.parseInt(hours[0].toString()) + 12;
				triggetTime = hourInt + ":" + hours[1].toString();
			}else{
				triggetTime = triggetTimes[0];
			}
			String triggerTimes[] = triggetTime.split(":");
			hour = Integer.parseInt(triggerTimes[0].toString());
			minute = Integer.parseInt(triggerTimes[1].toString());
		}
//		data.getCreateBy().getId()
		plan.setName(data.getName());//任务名称
		plan.setTriggerType(data.getTriggerType());//开始任务
		Temp temps = o_tempBO.findTempByCategory(data.getCreateBy().getId());
		plan.setCreateBy(temps);//选择模版
		if(data.getTriggerType().getId().equalsIgnoreCase("st_trigger_type_time")){
			if(tx.equalsIgnoreCase("1")){
				//一次
				String time[] = triggetData.split("-");
				triggerDateText = "单次触发时间:" + triggetData + " " +triggetTime;
				triggerDataSet = "0 " + minute + " " + hour + " " + time[2].toString() 
					+ " " + time[1].toString() + " ? " + time[0].toString();
				plan.setTriggerDateText(triggerDateText);
				plan.setTriggerDataSet(triggerDataSet);
				plan.setRecycle(false);//是否周期
				plan.setTriggerData(data.getTriggerData());
				plan.setTriggetTime(triggetTime);
			}else{
				//周期
				plan.setRecycle(true);//是否周期
				plan.setTriggetTime(triggetTime);
				if(mangetTime.equalsIgnoreCase("st_cycle_mode_hour")){
					//按小时
					triggerDateText = "设置时间:"+ this.getTime() + " " + "每" + data.getEveryHour() + "小时";
					editRule = "st_cycle_mode_hour;" + data.getEveryHour() + ";";
					triggerDataSet = editRule + "0 0 */" + data.getEveryHour() + " * * ?";
				}else if(mangetTime.equalsIgnoreCase("st_cycle_mode_day")){
					//按天
					String day = data.getDay();
					if(day.equalsIgnoreCase("1")){
						//选项1
						triggerDateText = "每" + data.getEveryDay() + "天";
						editRule = "st_cycle_mode_day;1;" + data.getEveryDay() + ";";
						triggerDataSet = editRule + "0 " + minute + " " + hour + " */" + data.getEveryDay() + " * ?";
					}else{
						//选项2(每个工作日)						
						triggerDateText = "每个工作日";
						editRule = "st_cycle_mode_day;2;workDay" + ";";
						triggerDataSet = editRule + "0 " + minute + " " + hour + " ? * MON-FRI";
					}
				}else if(mangetTime.equalsIgnoreCase("st_cycle_mode_week")){
					//按周
					triggerDateText = "每" + data.getEveryWeeks() +"周的" + "星期" + data.getEveryWeeksCheck();
					editRule = "st_cycle_mode_week;" + data.getEveryWeeks() + ";" + data.getEveryWeeksCheck()  + ";";
					String weeks[] = data.getEveryWeeksCheck().split(",");
					String weekStr = "";
					for (String str: weeks) {
						if(str.equalsIgnoreCase("7")){
							weekStr += "1" + ",";
						}else{
							weekStr += (Integer.parseInt(str) - 1) + ",";
						}
					}
					weekStr = weekStr.substring(0, weekStr.length() - 1);
					triggerDataSet = editRule + "0 " + minute + " " + hour + " ? * " + weekStr + "#" + data.getEveryWeeks() + "";
				}else if(mangetTime.equalsIgnoreCase("st_cycle_mode_month")){
					//按月
					String month = data.getMonth();
					if(month.equalsIgnoreCase("1")){
						//选项1
						triggerDateText = "每" + data.getEveryMonth1() + "个月的" + data.getEveryDay2() + "天";
						editRule = "st_cycle_mode_month;1;" + data.getEveryMonth1() + ";" + getNum(data.getEveryDay2()) + ";";
						if(data.getEveryDay2().indexOf("最后") != -1)
							triggerDataSet = editRule + "0 " + minute + " " + hour + " L */" + data.getEveryMonth1() + " ?";
						else
							triggerDataSet = editRule + "0 " + minute + " " + hour + " " + getNum(data.getEveryDay2()) + " */" + data.getEveryMonth1() + " ?";
					}else{
						//选项2
						triggerDateText = "每" + data.getEveryMonth2() + "个月的" + data.getEveryNum() + data.getEveryWeeksNum();
						editRule = "st_cycle_mode_month;2;" + data.getEveryMonth2() + ";" 
							+ getNum(data.getEveryNum()) + ";" + getNum(data.getEveryWeeksNum()) + ";";
						
						if(getNum(data.getEveryWeeksNum()).equalsIgnoreCase("7")){
							week = 1;
						}else{
							week = Integer.parseInt(getNum(data.getEveryWeeksNum())) - 1;
						}
						if(data.getEveryNum().indexOf("最后") != -1)
							triggerDataSet = editRule + "0 " + minute + " " + hour + " ? */" + data.getEveryMonth2() + " " + week + "L";
						else
							triggerDataSet = editRule + "0 " + minute + " " + hour + " ? */" + data.getEveryMonth2() + " " + week + "#" + getNum(data.getEveryNum()) + "";
					}
				}else if(mangetTime.equalsIgnoreCase("st_cycle_mode_year")){
					//按年
					String year = data.getYear();
					if(year.equalsIgnoreCase("1")){
						//选项1
						triggerDateText = "每年" + data.getMonthNum() + data.getDayNum() + "日";
						editRule = "st_cycle_mode_year;1;" + getNum(data.getMonthNum()) + ";" + getNum(data.getDayNum()) + ";";
						if(data.getDayNum().indexOf("最后") != -1)
							triggerDataSet = editRule + "0 " + minute + " " + hour + " L " + getNum(data.getMonthNum()) + " ?";
						else
							triggerDataSet = editRule + "0 " + minute + " " + hour + " " + getNum(data.getDayNum()) + " " + getNum(data.getMonthNum()) + " ?";
					}else{
						//选项2
						triggerDateText = "每年" + data.getMonthNum2() + data.getEveryNum2() + data.getEveryWeeksNum2();
						editRule = "st_cycle_mode_year;2;" + getNum(data.getMonthNum2()) + ";" 
								+ getNum(data.getEveryNum2()) + ";" + getNum(data.getEveryWeeksNum2()) + ";";
						if(getNum(data.getEveryWeeksNum2()).equalsIgnoreCase("7")){
							week = 1;
						}else{
							week = Integer.parseInt(getNum(data.getEveryWeeksNum2())) - 1;
						}
						if(data.getEveryNum2().indexOf("最后") != -1)
							triggerDataSet = editRule + "0 " + minute + " " + hour + " ? " + getNum(data.getMonthNum2()) + " " + week + "L";
						else
							triggerDataSet = editRule + "0 " + minute + " " + hour + " ? " + getNum(data.getMonthNum2()) + " " + week + "#" + getNum(data.getEveryNum2()) + "";
					}
				}
				
				plan.setTriggerDateText(triggerDateText);
				plan.setTriggerDataSet(triggerDataSet);
				plan.setStartTime(data.getStartTime());
				plan.setEndTime(data.getEndTime());
			}
		}
		
		try{
			plan.setDeleteStatus("1");
			plan.setStatus("0");
			plan.setCreateTime(getNowTime());
			plan.setLookType(data.getRb());
			
			if(StringUtils.isNotBlank(id)){
				//更新
				String ids[] = id.split(",");
				plan.setId(ids[0]);
				Plan findPlan = o_planBO.findPlanById(plan.getId());
				
				plan.setStatus(findPlan.getStatus());
				o_planBO.savePlan(plan);
				isSave = "true";
				if(isSave.equalsIgnoreCase("true")){
					Temp temp = plan.getCreateBy();
					PlanEmp planEmp = o_planEmpBO.findPlanEmpByPlanId(plan.getId());
					if(planEmp != null){
						//关联更新
						o_planEmpBO.mergePlanEmp(plan, temp, planEmp.getId());
					}else{
						//关联保存
						o_planEmpBO.mergePlanEmp(plan, temp, Identities.uuid());
					}
					
					if(plan.getStatus().equalsIgnoreCase("1")){
						this.triggerTask("update", findPlan, "email");
					}
				}
			}else{
				//保存
				o_planBO.savePlan(plan);
				isSave = "true";
				if(isSave.equalsIgnoreCase("true")){
					Temp temp = plan.getCreateBy();
					o_planEmpBO.savePlanEmp(plan, temp);
				}
			}
			out.write(isSave);
		}finally {
			out.close();
		}
	}
	
	/**
	 * 移除
	 * @author 金鹏祥
	 * @param ids plan对象实体id集合
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/sys/st/removePlanBatch.f")
	public void removePlanBatch(String ids,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		if(StringUtils.isNotBlank(ids)){
			String[] idArray = ids.split(",");
			for(String id : idArray){
				try{
					Plan plan = o_planBO.findPlanById(id);
					plan.setDeleteStatus("0");
					o_planBO.mergePlan(plan);
					flag = "true";
					out.write(flag);
					this.triggerTask("delete", plan, "email");
				} finally {
					out.close();
				}
			}
		}
	} 
	
	/**
	 * 更新
	 * @author 金鹏祥
	 * @param ids plan对象实体id集合
	 * @param response
	 * @param status
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/sys/st/mergePlanBatch.f")
	public void mergePlanBatch(String ids,HttpServletResponse response, String status) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idArray = ids.split(",");
				for(String id : idArray){
					Plan plan = o_planBO.findPlanById(id);
					plan.setStatus(status);
					o_planBO.mergePlan(plan);
					if(plan.getStatus().equalsIgnoreCase("1")){
						this.triggerTask("add", plan, "email");
					}else{
						this.triggerTask("delete", plan, "email");
					}
				}
			}
			flag = "true";
			out.write(flag);
		}finally {
			out.close();
		}
	} 
	
	/**
	 * 查看
	 * @author 金鹏祥
	 * @param id plan对象实体id
	 * @return PlanForm
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/viewPlan.f",method=RequestMethod.POST)
	public PlanForm viewPlan(String id){
		Plan plan = o_planBO.findPlanById(id);
		PlanForm planForm = new PlanForm(plan,"findPlanById",null);
		return planForm;
	}
	
	/**
	 * 取消
	 * @author 金鹏祥
	 * @param request
	 * @param id id plan对象实体id
	 * @return Map<String, Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping(value = "/sys/st/findPlanById.f")
    public Map<String, Object> findPlanById(HttpServletRequest request, String id) {		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> inmap = new HashMap<String, Object>();
		String ids[] = id.split(",");
		Plan plan = o_planBO.findPlanById(ids[0]);
		if(plan!=null){
			inmap.put("name", plan.getName());
			inmap.put("triggerType", plan.getTriggerType().getId());
			if(plan.getCreateBy() != null){
				inmap.put("createBy", plan.getCreateBy().getDictEntry().getId());
			}
			inmap.put("rb", plan.getLookType());
			if(plan.getTriggerType().getId().equalsIgnoreCase("st_trigger_type_time")){
				//时间触发
				inmap.put("isRecycle", plan.isRecycle());
				if(plan.isRecycle()){
					//周期
					inmap.put("triggerDataSet", plan.getTriggerDataSet());
					if(plan.getStartTime() != null && plan.getEndTime() != null){
						inmap.put("startTime", plan.getStartTime().split(" ")[0]);
						inmap.put("endTime", plan.getEndTime().split(" ")[0]);
						inmap.put("timeType", "1");
					}else{
						inmap.put("timeType", "0");
					}
				}else{
					//单次
					inmap.put("triggerData", plan.getTriggerData().split(" ")[0]);
				}
				inmap.put("triggetTime", plan.getTriggetTime());
			}			
		}
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }
	
	/**
	 * 查询分页
	 * @author 金鹏祥
	 * @param start 开始条数
	 * @param limit 结束条数
	 * @param query 查询条件
	 * @param sort 排序字段及方式
	 * @return Map<String, Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/queryPlanPage.f")
	public Map<String, Object> queryPlanPage(int start, int limit, String query, String sort) throws Exception {
		String property = "";
		String direction = "";
		Page<Plan> page = new Page<Plan>();
		
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		
		if (StringUtils.isNotBlank(sort)){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                direction = jsobj.getString("direction");
                
                if(property.equalsIgnoreCase("zq")){
                	property = "isRecycle";
    			}else if(property.equalsIgnoreCase("statusName")){
    				property = "status";
    			}else if(property.equalsIgnoreCase("triggerName")){
    				property = "triggerType";
    			}
            }
        }else{
        	property = "name";
        	direction = "ASC";
        }
		
		page = o_planBO.findPlanBySome(query, page, property, direction, "1");
		
		List<Plan> entityList = page.getResult();
		List<PlanForm> datas = new ArrayList<PlanForm>();
		PlanEmp planEmp = null;
		for(Plan de : entityList){
			planEmp = o_planEmpBO.findPlanEmpByPlanId(de.getId());
			datas.add(new PlanForm(de,"queryPage",planEmp.getId()));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * 开始任务下拉菜单
	 * @author 金鹏祥
	 * @param response
	 * @param dictTypeId 字典项id
	 * @return Map<String, Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/findDictEntryByDictTypeId.f")
	public Map<String, Object> findDictEntryByDictTypeId(HttpServletResponse response, String dictTypeId) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeId(dictTypeId);
		
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", dictEntry.getId());
			inmap.put("text",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		
		return map;
	}
	
	/**
	 * 选择模版下拉菜单
	 * @author 金鹏祥
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/findTempAll.f")
	public Map<String, Object> findTempAll(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictEntry> dictEntryList = o_dictBO.findDictEntryByDictTypeIdAndEStatus("st_temp_category");
		for (DictEntry dictEntry : dictEntryList) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", dictEntry.getId());
			inmap.put("text",dictEntry.getName());
			list.add(inmap);
		}
		map.put("datas", list);
		
		return map;
	}
	
	/**
	 * 天数下拉菜单
	 * 
	 * @author 金鹏祥
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/dayByMonthStore.f")
	public Map<String, Object> dayByMonthStore(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i < 31 + 1; i++) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", i);
			inmap.put("text", i);
			list.add(inmap);
		}
		inmap.put("id", "endDay");
		inmap.put("text", "最后一");
		map.put("datas", list);
		
		return map;
	}
	
	/**
	 * 月数下拉菜单
	 * 
	 * @author 金鹏祥
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/monthStore.f")
	public Map<String, Object> monthStore(HttpServletResponse response) throws Exception {
		Map<String, Object> inmap = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i < 12 + 1; i++) {
			inmap = new HashMap<String, Object>();
			inmap.put("id", i);
			inmap.put("text", i + "月");
			list.add(inmap);
		}
		map.put("datas", list);
		
		return map;
	}
	
	/**
	 * 过滤月、第、星期将大写数字更改为小写
	 * 
	 * @author 金鹏祥
	 * @param num 过滤字符串
	 * @return  String
	 * @since  fhd　Ver 1.1
	*/
	private static String getNum(String num){
		String numStr[] ={"一","二","三","四","五","六","日"};
		String nums[] ={"1","2","3","4","5","6","7"};
		
		if(num.indexOf("月") != -1){
			num = num.replace("月", "");
		}if(num.indexOf("第") != -1){
			num = num.replace("第", "");
			if(num.indexOf("个") != -1){
				num = num.replace("个", "").toString();
			}
		}else{
			num = num.replace("星期", "").toString();
		}
		
		for (int i = 0; i < numStr.length; i++) {
			if(numStr[i].toString().equalsIgnoreCase(num)){
				return nums[i];
			}
		}
		return num;
	}
	
	/**
	 * 获得当前时间
	 * 
	 * @author 金鹏祥
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private static String getNowTime(){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(new Date().getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(c.getTime());
	}
	
	private void triggerTask(String key, Plan plan, String triggerType){
		try {
			String cronsStr[] = plan.getTriggerDataSet().split(";");
			String job_name = plan.getId();
			
			if(key.equalsIgnoreCase("delete")){
				//删除定时任务
				o_quartz_BO.removeJob(job_name);
			}else if(key.equalsIgnoreCase("add")){
				//添加定时任务
				if(triggerType.equalsIgnoreCase("email")){
					o_quartz_BO.addJob(job_name, o_triggerEmail_BO, cronsStr[cronsStr.length - 1].toString());
				}
			}else if(key.equalsIgnoreCase("update")){
				//修改定时任务
				if(triggerType.equalsIgnoreCase("email")){
					o_quartz_BO.modifyJobTime(job_name, o_triggerEmail_BO, cronsStr[cronsStr.length - 1].toString());
				}
			}
		} catch (Exception e) {
		}
	}
	
	public String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
}