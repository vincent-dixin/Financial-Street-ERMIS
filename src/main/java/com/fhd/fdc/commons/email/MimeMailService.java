/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.fdc.commons.email;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



/**
 * 	MIME邮件服务类
 *	演示由Freemarker引擎生成的的html格式邮件, 并带有附件.
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-18		下午1:51:57
 *
 * @see 	 
 */

public class MimeMailService {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(MimeMailService.class);

	private JavaMailSender mailSender;

	private Configuration freemarkerConfiguration;
	
	/**
	 * 发送MIME格式的用户修改通知邮件.
	 */
	public void sendNotificationMail(String templateName,Map<String, Object> context,String[] to,String[] cc,
			String[] bcc,File attachment,String attachmentName) {

		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			helper.setTo(to);
			helper.setCc(cc);
			helper.setBcc(bcc);
			helper.setFrom("springside3.demo@gmail.com");
			helper.setSubject("用户修改通知");

			String content = generateContent(templateName,context);
			helper.setText(content, true);

			
			helper.addAttachment(attachmentName, attachment);

			mailSender.send(msg);
			logger.info("HTML版邮件已发送");
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * 使用Freemarker生成html格式内容.
	 */
	private String generateContent(String templateName,Map<String, Object> context) throws MessagingException {

		try {
			Template template = freemarkerConfiguration.getTemplate(templateName, DEFAULT_ENCODING);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
		} catch (IOException e) {
			logger.error("生成邮件内容失败, FreeMarker模板不存在", e);
			throw new MessagingException("FreeMarker模板不存在", e);
		} catch (TemplateException e) {
			logger.error("生成邮件内容失败, FreeMarker处理失败", e);
			throw new MessagingException("FreeMarker处理失败", e);
		}
	}


	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板.
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
		//根据freemarkerConfiguration的templateLoaderPath载入文件.
		this.freemarkerConfiguration = freemarkerConfiguration;
	}
}

