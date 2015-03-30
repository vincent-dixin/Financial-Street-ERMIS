/**
 * TaskThread.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-15 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * TaskThread.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-15        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st.task;

import org.apache.log4j.Logger;

import com.fhd.sys.entity.st.Plan;

/**
 * 初始化定时任务线程
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-15		下午02:52:01
 *
 * @see 	 
 */
public class TaskThread extends Thread {
	
	private static final Logger log = Logger.getLogger(TaskThread.class);
	private Plan plan;
	private QuartzManagerBO o_quartz_BO;
	private TriggerEmailBO o_triggerEmail_BO;
	
	public TaskThread(Plan plan, QuartzManagerBO o_quartz_BO, TriggerEmailBO o_triggerEmail_BO){
		this.plan = plan;
		this.o_quartz_BO = o_quartz_BO;
		this.o_triggerEmail_BO = o_triggerEmail_BO;
	}
	
	@Override
	public void run() {
		try {
			String cronsStr[] = plan.getTriggerDataSet().split(";");//获取CRON表达式
			o_quartz_BO.addJob(plan.getId(), o_triggerEmail_BO, cronsStr[cronsStr.length - 1].toString());//添加定时任务
			System.out.println("定时任务ID:" + plan.getId() + "===================================重新启用");
			log.info("定时任务ID:" + plan.getId() + "重新启用");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}

