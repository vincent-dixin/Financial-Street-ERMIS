package com.fhd.sys.business.sysapp;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.core.utils.reflection.ReflectionUtils;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.sysapp.DealLogDAO;
import com.fhd.sys.dao.sysapp.DealMeasureDAO;
import com.fhd.sys.dao.sysapp.ScheduledTaskDAO;
import com.fhd.sys.dao.sysapp.ScheduledTaskEmpDAO;
import com.fhd.sys.dao.sysapp.ScheduledTaskTargetDAO;
import com.fhd.sys.dao.sysapp.ScheduledTaskTempDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.sysapp.DealLog;
import com.fhd.sys.entity.sysapp.DealMeasure;
import com.fhd.sys.entity.sysapp.ScheduledTask;
import com.fhd.sys.entity.sysapp.ScheduledTaskEmp;
import com.fhd.sys.entity.sysapp.ScheduledTaskTarget;
import com.fhd.sys.entity.sysapp.ScheduledTaskTemp;
import com.fhd.sys.web.form.sysapp.ScheduledTaskForm;

/**
 * 计划任务BO.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Service
@SuppressWarnings({"unchecked"})
public class ScheduledTaskBO {

	@Autowired
	private DealLogDAO o_dealLogDAO;
	@Autowired
	private DealMeasureDAO o_dealMeasureDAO;
	@Autowired
	private ScheduledTaskDAO o_scheduledTaskDAO;
	@Autowired
	private ScheduledTaskEmpDAO o_scheduledTaskEmpDAO;
	@Autowired
	private ScheduledTaskTargetDAO o_scheduledTaskTargetDAO;
	@Autowired
	private ScheduledTaskTempDAO o_scheduledTaskTempDAO;
	
	/**
	 * 根据查询条件查询计划任务--分页查询.
	 * @param page
	 * @param taskName
	 * @param type
	 * @return Page<ScheduledTask>
	 */
	public Page<ScheduledTask> queryScheduledTaskList(Page<ScheduledTask> page, String taskName, String type){
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduledTask.class);
		//任务名称
		if(StringUtils.isNotBlank(taskName)){
			dc.add(Restrictions.like("planName", taskName, MatchMode.ANYWHERE));
		}
		//触发类型
		if(StringUtils.isNotBlank(type)){
			dc.add(Restrictions.eq("triggerType.id", type));
		}
		Date currentDate = new Date();
		//有效期内:当前时间在有效期内+当前时间未到有效期
		dc.add(Restrictions.or(Restrictions.ge("endTime", currentDate), Restrictions.and(Restrictions.le("startTime", currentDate), Restrictions.ge("endTime", currentDate))));
		//有效
		//dc.add(Restrictions.eq("isEnabled", "1"));
		//排序
		dc.addOrder(Order.asc("startTime"));
		return o_scheduledTaskDAO.pagedQuery(dc, page);
	}
	/**
	 * 根据查询条件查询计划任务.
	 * @param taskName
	 * @param type
	 * @return List<ScheduledTask>
	 */
	public List<ScheduledTask> queryScheduledTaskList(String taskName, String type){
		Criteria criteria = o_scheduledTaskDAO.createCriteria();
		criteria.setCacheable(true);
		//任务名称
		if(StringUtils.isNotBlank(taskName)){
			criteria.add(Restrictions.like("", taskName, MatchMode.ANYWHERE));
		}
		//触发类型
		if(StringUtils.isNotBlank(type)){
			criteria.add(Restrictions.eq("tt.id", type));
		}
		Date currentDate = new Date();
		//有效期内:当前时间在有效期内+当前时间未到有效期
		criteria.add(Restrictions.or(Restrictions.ge("endTime", currentDate), Restrictions.and(Restrictions.le("startTime", currentDate), Restrictions.ge("endTime", currentDate))));
		//有效
		criteria.add(Restrictions.eq("isEnabled", "1"));
		//排序
		criteria.addOrder(Order.asc("startTime"));
		return criteria.list();
	}
	/**
	 * 根据id查询计划任务.
	 * @param id
	 * @return ScheduledTask
	 */
	public ScheduledTask queryScheduledTaskById(String id){
		return o_scheduledTaskDAO.get(id);
	}
	/**
	 * 新增或修改计划任务.
	 * @param scheduledTaskForm
	 */
	@Transactional
	public void saveOrUpdateScheduledTask(ScheduledTaskForm scheduledTaskForm){
		if(StringUtils.isBlank(scheduledTaskForm.getId())){
			//新增
			/*****************保存计划任务(1条)*****************/
			ScheduledTask scheduledTask = null;
			scheduledTask = new ScheduledTask();
			BeanUtils.copyProperties(scheduledTaskForm,scheduledTask);
			scheduledTask.setId(Identities.uuid2());
			scheduledTask.setCreateBy(UserContext.getUser().getEmp());
			scheduledTask.setCreateTime(new Date());
			//触发时间
			scheduledTask.setTriggerTime(scheduledTaskForm.getTriggerTime());
			//触发时间文本
			scheduledTask.setTriggerTimeText(scheduledTaskForm.getTriggerTimeText());
			//触发类型
			if(StringUtils.isNotBlank(scheduledTaskForm.getType())){
				DictEntry type = new DictEntry();
				type.setId(scheduledTaskForm.getType());
				scheduledTask.setTriggerType(type);
			}
			//触发类型详细
			if(StringUtils.isNotBlank(scheduledTaskForm.getSubType())){
				DictEntry triggerSubType = new DictEntry();
				triggerSubType.setId(scheduledTaskForm.getSubType());
				scheduledTask.setTriggerSubType(triggerSubType);
			}
			//默认停用
			scheduledTask.setIsEnabled("0");
			o_scheduledTaskDAO.merge(scheduledTask);
			/****************保存信息处理方式(2条)--触发方式与模板一一对应***********/
			DealMeasure dealMeasure = null;
			//触发方式
			if(StringUtils.isNotBlank(scheduledTaskForm.getMeasure())){
				String[] measures = scheduledTaskForm.getMeasure().split(",");
				for(String measureId : measures){
					dealMeasure = new DealMeasure();
					dealMeasure.setId(Identities.uuid2());
					dealMeasure.setTask(scheduledTask);
					DictEntry measure = new DictEntry();
					measure.setId(measureId);
					if("ST_DEAL_MEASURE_MSG".equals(measureId)){
						if(StringUtils.isNotBlank(scheduledTaskForm.getMsgTemp())){
							ScheduledTaskTemp msgTemp = new ScheduledTaskTemp();
							msgTemp.setId(scheduledTaskForm.getMsgTemp());
							dealMeasure.setTemp(msgTemp);
						}
					}else if("ST_DEAL_MEASURE_Mail".equals(measureId)){
						if(StringUtils.isNotBlank(scheduledTaskForm.getMailTemp())){
							ScheduledTaskTemp mailTemp = new ScheduledTaskTemp();
							mailTemp.setId(scheduledTaskForm.getMailTemp());
							dealMeasure.setTemp(mailTemp);
						}
					}else if("ST_DEAL_MEASURE_Other".equals(measureId)){
						if(StringUtils.isNotBlank(scheduledTaskForm.getOtherMethod())){
							dealMeasure.setDealMeasureDetail(scheduledTaskForm.getOtherMethod());
						}
					}
					dealMeasure.setDealMeasure(measure);
					o_dealMeasureDAO.merge(dealMeasure);
				}
			}
			/*************保存提醒对象(n条)**************/
			ScheduledTaskTarget scheduledTaskTarget = null;
			//提醒对象
			if(StringUtils.isNotBlank(scheduledTaskForm.geteType())){
				String[] targets = scheduledTaskForm.geteType().split(",");
				for(String targetId : targets){
					scheduledTaskTarget = new ScheduledTaskTarget();
					scheduledTaskTarget.setId(Identities.uuid2());
					scheduledTaskTarget.setTask(scheduledTask);
					DictEntry targetType = new DictEntry();
					targetType.setId(targetId);
					scheduledTaskTarget.setTargetType(targetType);
					o_scheduledTaskTargetDAO.merge(scheduledTaskTarget);
				}
			}
			/*************保存涉及人员(n条)***********/
			ScheduledTaskEmp scheduledTaskEmp = null;
			if(StringUtils.isNotBlank(scheduledTaskForm.getEmp())){
				String[] emps = scheduledTaskForm.getEmp().split(",");
				for(String empId : emps){
					scheduledTaskEmp = new ScheduledTaskEmp();
					scheduledTaskEmp.setId(Identities.uuid2());
					scheduledTaskEmp.setTask(scheduledTask);
					SysEmployee emp = new SysEmployee();
					emp.setId(empId);
					scheduledTaskEmp.setEmp(emp);
					o_scheduledTaskEmpDAO.merge(scheduledTaskEmp);
					
					/************保存处理记录(1*2*n*n)***********/
					DealLog dealLog = null;
					//触发方式
					if(StringUtils.isNotBlank(scheduledTaskForm.getMeasure())){
						String[] measures = scheduledTaskForm.getMeasure().split(",");
						for(String measureId : measures){
							dealLog = new DealLog();
							dealLog.setId(Identities.uuid2());
							DictEntry measure = new DictEntry();
							measure.setId(measureId);
							dealLog.setDealMeasure(measure);
							dealLog.setDealTime(new Date());
							dealLog.seteStatus("1");
							dealLog.setEmp(scheduledTaskEmp);
							o_dealLogDAO.merge(dealLog);
						}
					}
				}
			}
		}else{
			//修改
			
		}
	}
	/**
	 * 根据id集合删除计划任务.
	 * @param ids
	 */
	@Transactional
	public void delScheduledTaskByIds(String[] ids){
		for(String id : ids){
			ScheduledTask scheduledTask = o_scheduledTaskDAO.get(id);
			scheduledTask.setIsEnabled("0");
			o_scheduledTaskDAO.merge(scheduledTask);
		}
	}
	/**
	 * 根据id集合启用计划任务.
	 * @param ids
	 */
	@Transactional
	public void enableScheduledTask(String[] ids){
		for(String id : ids){
			ScheduledTask scheduledTask = o_scheduledTaskDAO.get(id);
			scheduledTask.setIsEnabled("1");
			o_scheduledTaskDAO.merge(scheduledTask);
		}
	}
	/**
	 * 根据参数生成计划任务--供业务调用接口--参数可能很多****************.
	 * @param scheduledTaskId
	 */
	public void triggerScheduledTask(String scheduledTaskId){
		
	}
	/**
	 * 创建一个任务添加到quartz任务管理器中.
	 * @param scheduledTask
	 * @return Boolean
	 * @throws ParseException 
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	
	// TODO 重写
	/*public Boolean createScheduledTask(ScheduledTask scheduledTask) throws ParseException, SchedulerException, ClassNotFoundException{
		//类名
		String className = "";
		//方法名
		//String methodName = "";
		Set<DealMeasure> dealMeasures = scheduledTask.getDealMeasures();
		for(DealMeasure dealMeasure : dealMeasures){
			if(null != dealMeasure.getDealMeasure()){
				if("ST_DEAL_MEASURE_Mail".equals(dealMeasure.getDealMeasure().getId())){
					//邮件--固定的类和方法(整理添加)********************
					className = "com.fhd.fdc.commons.email.SimpleMailService";
					//methodName = "";
					//添加到quzrtz任务
					Class clazz=Class.forName(className);
					CronTrigger trigger = (CronTrigger)scheduler.getTrigger(scheduledTask.getId()+"_trigger", Scheduler.DEFAULT_GROUP);    
					if (null != trigger) {    
						trigger.setCronExpression(scheduledTask.getTriggerTime());    
			            scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		            }else{
		            	//JobDetail jobDetail = new JobDetail(schedulingJob.getJobId(), schedulingJob.getJobGroup(), QuartzJobBean.class);    
		                //jobDetail.getJobDataMap().put("targetObjectId", schedulingJob.getJobId());
		                JobDetail jobDetail=new JobDetail(scheduledTask.getId()+"_job",Scheduler.DEFAULT_GROUP,clazz);
		                trigger=new CronTrigger(scheduledTask.getId()+"_trigger",Scheduler.DEFAULT_GROUP,scheduledTask.getTriggerTime());
						scheduler.scheduleJob(jobDetail, trigger);
		            }
				}else if("ST_DEAL_MEASURE_MSG".equals(dealMeasure.getDealMeasure().getId())){
					//短信--固定的类和方法(整理添加)********************
					className = "";
					//methodName = "";
					//添加到quzrtz任务
					Class clazz=Class.forName(className);
					CronTrigger trigger = (CronTrigger)scheduler.getTrigger(scheduledTask.getId()+"_trigger", Scheduler.DEFAULT_GROUP);    
					if (null != trigger) {    
						trigger.setCronExpression(scheduledTask.getTriggerTime());    
			            scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		            }else{
		            	//JobDetail jobDetail = new JobDetail(schedulingJob.getJobId(), schedulingJob.getJobGroup(), QuartzJobBean.class);    
		                //jobDetail.getJobDataMap().put("targetObjectId", schedulingJob.getJobId());
		                JobDetail jobDetail=new JobDetail(scheduledTask.getId()+"_job",Scheduler.DEFAULT_GROUP,clazz);
		                trigger=new CronTrigger(scheduledTask.getId()+"_trigger",Scheduler.DEFAULT_GROUP,scheduledTask.getTriggerTime());
						scheduler.scheduleJob(jobDetail, trigger);
		            }
				}else{
					
					//其它
					String dictEntryValue = dealMeasure.getDealMeasure().getDictEntryValue();
					className = dictEntryValue.substring(0, dictEntryValue.lastIndexOf("."));
					//methodName = dictEntryValue.substring(dictEntryValue.lastIndexOf(".")+1,dictEntryValue.length());
					//添加到quzrtz任务
					Class clazz=Class.forName(className);
					CronTrigger trigger = (CronTrigger)scheduler.getTrigger(scheduledTask.getId()+"_trigger", Scheduler.DEFAULT_GROUP);    
					if (null != trigger) {    
						trigger.setCronExpression(scheduledTask.getTriggerTime());    
			            scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		            }else{
		            	//JobDetail jobDetail = new JobDetail(schedulingJob.getJobId(), schedulingJob.getJobGroup(), QuartzJobBean.class);    
		                //jobDetail.getJobDataMap().put("targetObjectId", schedulingJob.getJobId());
		                JobDetail jobDetail=new JobDetail(scheduledTask.getId()+"_job",Scheduler.DEFAULT_GROUP,clazz);
		                trigger=new CronTrigger(scheduledTask.getId()+"_trigger",Scheduler.DEFAULT_GROUP,scheduledTask.getTriggerTime());
						scheduler.scheduleJob(jobDetail, trigger);
		            }
				}
			}
		}
		
		return true;
	}*/
	/**
	 * 根据一个任务id立即执行一个任务.
	 * @author 吴德福
	 * @param scheduledTaskId
	 * @return Boolean
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 * @throws NoSuchMethodException
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Boolean executeScheduledTask(String scheduledTaskId) throws SchedulerException, ParseException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		//类名
		String className = "";
		//方法名
		String methodName = "";
		ScheduledTask scheduledTask = o_scheduledTaskDAO.get(scheduledTaskId);
		//根据计划任务找到需要执行的类和方法
		Set<DealMeasure> dealMeasures = scheduledTask.getDealMeasures();
		for(DealMeasure dealMeasure : dealMeasures){
			if(null != dealMeasure.getDealMeasure()){
				if("ST_DEAL_MEASURE_Mail".equals(dealMeasure.getDealMeasure().getId())){
					//邮件--固定的类和方法(整理添加)********************
					className = "com.fhd.fdc.commons.email.SimpleMailService";
					methodName = "";
					//执行
					Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(Class.forName(className));
					ReflectionUtils.invokeMethod(bean, methodName, null, null);
				}else if("ST_DEAL_MEASURE_MSG".equals(dealMeasure.getDealMeasure().getId())){
					//短信--固定的类和方法(整理添加)********************
					className = "com.fhd.fdc.commons.email.MsgService";
					methodName = "sendMsg";
					//执行
					Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(Class.forName(className));
					ReflectionUtils.invokeMethod(bean, methodName, null, null);
				}else{
					//其它
					String dictEntryValue = dealMeasure.getDealMeasure().getValue();
					className = dictEntryValue.substring(0, dictEntryValue.lastIndexOf("."));
					methodName = dictEntryValue.substring(dictEntryValue.lastIndexOf(".")+1,dictEntryValue.length());
					//执行
					Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(Class.forName(className));
					ReflectionUtils.invokeMethod(bean, methodName, null, null);
				}
			}
		}
		
		return true;
	}
	/**
	 * 根据触发方式查询计划模板.
	 * @param dealMeasure
	 * @return List<ScheduledTaskTemp>
	 */
	public List<ScheduledTaskTemp> queryScheduledTaskTempByDealMeasure(String dealMeasure){
		Criteria criteria = o_scheduledTaskTempDAO.createCriteria();
		criteria.createAlias("dealMeasure", "dm");
		if(StringUtils.isNotBlank(dealMeasure)){
			criteria.add(Restrictions.eq("dm.id", dealMeasure));
		}
		criteria.add(Restrictions.eq("isEnabled", "1"));
		return criteria.list();
	}
}
