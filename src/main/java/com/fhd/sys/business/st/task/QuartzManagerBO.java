/**
 * QuartzManagerBO.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * QuartzManagerBO.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fhd.fdc.commons.email.SimpleMailService;
/**
 * Quartz定时任务工具
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		上午09:50:07
 *
 * @see 	 
 */
@Component
public class QuartzManagerBO {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private SimpleMailService simpleMailService;
	
	private static final Logger log = Logger.getLogger(QuartzManagerBO.class);
	private String JOB_GROUP_NAME = "group1";//任务组
	private String TRIGGER_GROUP_NAME = "trigger1";//触发组
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName 任务名
	 * @param job 任务
	 * @param time 时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 * @author 金鹏祥
	 */	
	public void addJob(String jobName, Job job, String time)
			throws SchedulerException, ParseException {
		JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, job.getClass());// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put("simpleMailService", simpleMailService);
		// 触发器
		CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组		
		trigger.setCronExpression(time);//"0 17 11 29 01 ? 2013");// 触发器时间设定
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			// 启动
			if (!scheduler.isShutdown())
				scheduler.start();
		} catch (Exception e) {
			if(e.getMessage().indexOf("configured schedule, the given trigger will") != -1){
				log.info("任务ID:" + jobName + "小于当前时间,任务永远不会触发");
			}else{
				log.error("定时任务出错,原因未知.");
			}
		}
		
		
	}
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName 任务名
	 * @param job 任务
	 * @param time 时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 * @author 金鹏祥
	 */	
	public void addJob(String jobName, Job job, String time, String startTime, String startformat, String endTime, String endFormat)
			throws SchedulerException, ParseException {
		JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, job.getClass());// 任务名，任务组，任务执行类
		// 触发器
		CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
		trigger.setStartTime(getDateTime(startTime, startformat));
		trigger.setEndTime(getDateTime(endTime, endFormat));
		trigger.setCronExpression(time);// 触发器时间设定
		scheduler.scheduleJob(jobDetail, trigger);
		// 启动
		if (!scheduler.isShutdown())
			scheduler.start();
	}

	/** */
	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @param job 任务
	 * @param time 时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 * @author 金鹏祥
	 */
	public void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName, Job job, String time)
			throws SchedulerException, ParseException {
		JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());// 任务名，任务组，任务执行类
		// 触发器
		CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
		trigger.setCronExpression(time);// 触发器时间设定
		scheduler.scheduleJob(jobDetail, trigger);
		if (!scheduler.isShutdown())
			scheduler.start();
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName 任务名
	 * @param time CRON表达式
	 * @throws SchedulerException
	 * @throws ParseException
	 * @author 金鹏祥
	 */
	public void modifyJobTime(String jobName, Job job, String time)
			throws SchedulerException, ParseException {
         removeJob(jobName);
         addJob(jobName, job, time);
	}

	/** */
	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName 任务名
	 * @throws SchedulerException
	 * @author 金鹏祥
	 */
	public void removeJob(String jobName) throws SchedulerException {
		scheduler.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
		scheduler.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
		scheduler.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
	}

	/** */
	/**
	 * 移除一个任务
	 * 
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @throws SchedulerException
	 * @author 金鹏祥
	 */
	public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {
		scheduler.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
		scheduler.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
		scheduler.deleteJob(jobName, jobGroupName);// 删除任务
		log.info("任务ID:" + jobName + "已被移除");
	}
	
	/**
	 * 获取日期类型
	 * 
	 * @author 金鹏祥
	 * @param time 日期字符串
	 * @return Date
	 * @since fhd　Ver 1.1
	 */
	public  Date getDateTime(String time, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dateTime = sdf.parse(time); // 字符串转时间
			return dateTime;
		} catch (Exception e) {
		}

		return null;
	}
}