/**
 * TriggerEmail.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.business.st.task;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.SpringContextHolder;
import com.fhd.fdc.commons.email.SimpleMailService;
import com.fhd.sys.dao.st.PlanDAO;
import com.fhd.sys.entity.st.Plan;

/**
 * 触发定时任务
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012-11-14 上午09:52:01
 * 
 * @see
 */
@Component
public class TriggerEmailBO extends StatefulMethodInvokingJob  {
	
	private static final Logger log = Logger.getLogger(TriggerEmailBO.class);
	
	/**
	 * 触发定时任务方法
	 * 
	 * @author 金鹏祥
	 * @param job 设置定时任务可取参数
	 * @throws JobExecutionException
	 * @since  fhd　Ver 1.1
	*/
	@Override
	protected void executeInternal(JobExecutionContext job) throws JobExecutionException {
		String planId = job.getJobDetail().getName();//任务ID
		SimpleMailService simpleMailService = (SimpleMailService) job.getJobDetail().getJobDataMap().get("simpleMailService");//EMAIL服务
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();//SPRING-BEAN服务
		PlanDAO o_planDAO = applicationContext.getBean(PlanDAO.class);//任务功能数据服务
		Session session = o_planDAO.getSessionFactory().openSession();//HIBERNATE-SESSION服务
		Plan plan = this.findPlanById(planId, session);//当前任务实体
		String taskName = plan.getName();//任务名称
		String titleName = "";
		String content = "";//EMAIL内容
		if(plan.getCreateBy() != null){
			titleName = plan.getCreateBy().getName();
			content = plan.getCreateBy().getContent();
		}
		String title = titleName + "_任务名称" + taskName;//EMAIL标题
		String value = plan.getCreateBy().getDictEntry().getValue();//类方法反射规则
		session.close();//关闭SESSION
		String emails[] = this.getEmailAddresses("com.fhd.sys.business.st.trigger.Email;getEmailAddresses");//将要发送的EMAIL数组
		boolean isSendMail = false;//是否EMAIL发送成功变量
		boolean isExeFun = this.exeFun(value);
		
//		if(isExeFun){
//			try {
//				if(emails.length != 0){
//					isSendMail = simpleMailService.htmlTxtSendMail(emails, null, null, "jinpengxiang@sina.com", title, content);//发送EMAIL
//					if(isSendMail){
//						log.info("邮件已发送" + "任务名称" + taskName);
//						System.out.println("触发时间:" + this.getTime() + "========任务名称:" + taskName + "====邮件已发送");
//					}else{
//						log.info("邮件发送失败" + "任务名称" + taskName);
//					}
//				}
//			} catch (Exception e) {
//				log.error(e.getMessage());
//			}
//		}	
	}
	
	/**
	 * 通过得到的规则类及方法名称进行类方法反射并等到发送人email数组
	 * 
	 * @author 金鹏祥
	 * @param value 规则(类;方法)
	 * @return String[]
	 * @since  fhd　Ver 1.1
	*/
	private String[] getEmailAddresses(String value) {
		String[] address = null;
		try {
			String values[] = value.split(";");//将规则拆分
			Class<?> addressesClass = Class.forName(values[0].toString());//取类
			Object bean = SpringContextHolder.getBean(addressesClass);
			Method method = addressesClass.getMethod(values[1].toString());//取类方法
			address = (String[]) method.invoke(bean);//取结果
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return address;//返回结果
	}
	
	public boolean exeFun(String value){
		boolean isBool = false;
		try {
			String values[] = value.split(";");//将规则拆分
			Class<?> addressesClass = Class.forName(values[0].toString());//取类
			Object bean = SpringContextHolder.getBean(addressesClass);
			Method method = addressesClass.getMethod(values[1].toString());//取类方法
			isBool = (Boolean)method.invoke(bean);
			log.info("[类:" + values[0] + "] " + "[方法:" + values[1] + "] 执行时间:" + getTime() + ", 方法执行" + isBool);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.fillInStackTrace());
		}
		return isBool;//返回结果
	}

	/**
	 * 通过任务id查询任务实体
	 * 
	 * @author 金鹏祥
	 * @param id 任务ID
	 * @param session DB-Session
	 * @return Plan
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	private Plan findPlanById(String id, Session session) {
		try {
			Criteria c = session.createCriteria(Plan.class);
			List<Plan> list = null;
			
			if (StringUtils.isNotBlank(id)) {
				c.add(Restrictions.eq("id", id));
			} else {
				return null;
			}
			
			list = c.list();
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
	}

	
	/**
	 * 取当前时间,格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @author 金鹏祥
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public String getTime() {
		return DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
	}
}