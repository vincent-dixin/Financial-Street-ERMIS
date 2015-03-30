/**
 * PlanForm.java
 * com.fhd.sys.web.form.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * PlanForm.java
 * com.fhd.sys.web.form.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.form.st;

import com.fhd.sys.entity.st.Plan;

/**
 * ClassName:PlanForm
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午03:27:55
 *
 * @see 	 
 */

public class PlanForm extends Plan{
	private static final long serialVersionUID = 1L;
	
	private String lookName;
	
	private String tempName;
	
	private String zq;
	
	private String statusName;
	
	//按时间规则管理
	private String manageTime;
	
	//每小时
	private String everyHour;
	//每天
	private String everyDay;
	
	//按天(单选项)
	private String day;
	
	//按月(单选项)
	private String month;

	//按年(单选项)
	private String year;
	
	//每一天(单选)
	private String everyDayRadio;
	
	//每周
	private String everyWeeks;
	
	//每周几(多选)
	private String everyWeeksCheck;
	
	//每几个月(选项1)
	private String everyWeeks1;
	
	//每几个月的第几天(选项1)
	private String everyMonth1;
	
	private String everyDay1;

	//方式(单次:1,周期:2)
	private String tx;
	
	//每几个月(选项2)
	private String everyMonth2;
	
	//每几个月的第几个(选项2)
	private String everyNum;
	
	//每几个月的第几个星期(选项2)
	private String everyWeeksNum;
	
	//每年几月
	private String monthNum;
	
	//每年几日
	private String dayNum;
	
	//触发方式
	private String rb;
	
	private String monthNum2;
	
	private String everyNum2;
	
	private String everyWeeksNum2;
	
	private String everyDay2;
	
	private String triggerTypes;

	private String planEmpId;
	
	private String timeType;
	
	public PlanForm(){}
	
	public PlanForm(Plan plan, String type, String planEmpId){
		if(type.equalsIgnoreCase("queryPage")){
			this.setPlanEmpId(planEmpId);
			this.setId(plan.getId());
			this.setName(plan.getName());
			this.setTriggerDateText(plan.getTriggerDateText());
			if(plan.isRecycle()){
				this.setZq("周期");
			}else{
				this.setZq("一次");
			}
			this.setRecycle(plan.isRecycle());
			this.setTriggerName(plan.getTriggerType().getName());
			if(plan.getStatus().equalsIgnoreCase("1")){
				this.setStatusName("启用");
			}else{
				this.setStatusName("停用");
			}
			this.setStatus(plan.getStatus());
		}else if(type.equalsIgnoreCase("findPlanById")){
			//任务名称
			this.setName(plan.getName());
			//时间触发
			this.setTriggerTypes(plan.getTriggerType().getId());
			if(plan.getTriggerType().getId().equalsIgnoreCase("st_trigger_type_time")){
				//开始任务
				this.setTriggerName(plan.getTriggerType().getName());
				//发送方式
				String lookTypeName = "";
				if(plan.getLookType().indexOf("email") != -1){
					lookTypeName += "," + "邮件";
				}if(plan.getLookType().indexOf("message") !=-1 ){
					lookTypeName += "," + "短信";
				}
				this.setLookName(lookTypeName.substring(1, lookTypeName.length()));
				//选择模版
				
				this.setTempName(plan.getCreateBy().getDictEntry().getName());
				//单次or周期
				this.setRecycle(plan.isRecycle());
				if((plan.isRecycle())){
					//周期
					//触发时间
					this.setTriggetTime(plan.getTriggetTime());
					//按时间方式
					this.setTriggerDataSet(plan.getTriggerDataSet());
					//按时间方式(书写)
					this.setTriggerDateText(plan.getTriggerDateText());
					
					if(plan.getStartTime() != null && plan.getEndTime() != null){
						//开始日期
						this.setStartTime(plan.getStartTime().split(" ")[0]);
						//结束日期
						this.setEndTime(plan.getEndTime().split(" ")[0]);
						this.setTimeType("1");
					}else{
						this.setTimeType("0");
					}
				}else{
					//单次
					//触发日期
					this.setTriggerData(plan.getTriggerData().split(" ")[0]);
					//触发时间
					this.setTriggetTime(plan.getTriggetTime());
				}
			}else{
				//开始任务
				this.setTriggerName(plan.getTriggerType().getName());
				//发送方式
				String lookTypeName = "";
				if(plan.getLookType().indexOf("email") != -1){
					lookTypeName += "," + "邮件";
				}if(plan.getLookType().indexOf("message") !=-1 ){
					lookTypeName += "," + "短信";
				}
				this.setLookName(lookTypeName.substring(1, lookTypeName.length()));
				//选择模版
				this.setTempName(plan.getCreateBy().getDictEntry().getName());
			}
			
			if(plan.getStatus().equalsIgnoreCase("1")){
				this.setStatusName("启用");
			}else{
				this.setStatusName("停用");
			}
		}
	}
	
	public String getPlanEmpId() {
		return planEmpId;
	}

	public void setPlanEmpId(String planEmpId) {
		this.planEmpId = planEmpId;
	}
	
	public String getTriggerTypes() {
		return triggerTypes;
	}

	public void setTriggerTypes(String triggerTypes) {
		this.triggerTypes = triggerTypes;
	}
	
	public String getLookName() {
		return lookName;
	}

	public void setLookName(String lookName) {
		this.lookName = lookName;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	private String triggerName;
	
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getZq() {
		return zq;
	}

	public void setZq(String zq) {
		this.zq = zq;
	}
	
	public String getEveryDay2() {
		return everyDay2;
	}

	public void setEveryDay2(String everyDay2) {
		this.everyDay2 = everyDay2;
	}

	public String getMonthNum2() {
		return monthNum2;
	}

	public void setMonthNum2(String monthNum2) {
		this.monthNum2 = monthNum2;
	}

	public String getEveryNum2() {
		return everyNum2;
	}

	public void setEveryNum2(String everyNum2) {
		this.everyNum2 = everyNum2;
	}

	public String getEveryWeeksNum2() {
		return everyWeeksNum2;
	}

	public void setEveryWeeksNum2(String everyWeeksNum2) {
		this.everyWeeksNum2 = everyWeeksNum2;
	}

	public String getEveryDay1() {
		return everyDay1;
	}

	public void setEveryDay1(String everyDay1) {
		this.everyDay1 = everyDay1;
	}
	
	public String getRb() {
		return rb;
	}

	public void setRb(String rb) {
		this.rb = rb;
	}
	
	public String getManageTime() {
		return manageTime;
	}

	public void setManageTime(String manageTime) {
		this.manageTime = manageTime;
	}

	public String getEveryHour() {
		return everyHour;
	}

	public void setEveryHour(String everyHour) {
		this.everyHour = everyHour;
	}

	public String getEveryDay() {
		return everyDay;
	}

	public void setEveryDay(String everyDay) {
		this.everyDay = everyDay;
	}

	public String getEveryDayRadio() {
		return everyDayRadio;
	}

	public void setEveryDayRadio(String everyDayRadio) {
		this.everyDayRadio = everyDayRadio;
	}

	public String getEveryWeeks() {
		return everyWeeks;
	}

	public void setEveryWeeks(String everyWeeks) {
		this.everyWeeks = everyWeeks;
	}

	public String getEveryWeeksCheck() {
		return everyWeeksCheck;
	}

	public void setEveryWeeksCheck(String everyWeeksCheck) {
		this.everyWeeksCheck = everyWeeksCheck;
	}

	public String getEveryWeeks1() {
		return everyWeeks1;
	}

	public void setEveryWeeks1(String everyWeeks1) {
		this.everyWeeks1 = everyWeeks1;
	}

	public String getEveryMonth1() {
		return everyMonth1;
	}

	public void setEveryMonth1(String everyMonth1) {
		this.everyMonth1 = everyMonth1;
	}

	public String getEveryMonth2() {
		return everyMonth2;
	}

	public void setEveryMonth2(String everyMonth2) {
		this.everyMonth2 = everyMonth2;
	}

	public String getEveryNum() {
		return everyNum;
	}

	public void setEveryNum(String everyNum) {
		this.everyNum = everyNum;
	}

	public String getEveryWeeksNum() {
		return everyWeeksNum;
	}

	public void setEveryWeeksNum(String everyWeeksNum) {
		this.everyWeeksNum = everyWeeksNum;
	}

	public String getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(String monthNum) {
		this.monthNum = monthNum;
	}

	public String getDayNum() {
		return dayNum;
	}

	public void setDayNum(String dayNum) {
		this.dayNum = dayNum;
	}
	
	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
}