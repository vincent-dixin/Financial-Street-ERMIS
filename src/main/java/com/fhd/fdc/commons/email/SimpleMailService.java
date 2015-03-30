package com.fhd.fdc.commons.email;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

/**
 * 纯文本邮件服务类.
 * 
 * @author calvin
 */
public class SimpleMailService {
	private static Log logger = LogFactory.getLog(SimpleMailService.class);

	private JavaMailSender mailSender;

	/**
	 * mailSender
	 * 
	 * @return the mailSender
	 * @since CodingExample Ver 1.0
	 */

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	private String textTemplate;

	/**
	 * 发送纯文本的用户修改通知邮件.
	 */
	public void sendNotificationMail(String userName) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("springside3.demo@gmail.com");
		msg.setTo("springside3.demo@gmail.com");
		msg.setSubject("用户修改通知");
		// 将用户名与当期日期格式化到邮件内容的字符串模板
		String content = String.format(textTemplate, userName, new Date());
		msg.setText(content);

		try {
			mailSender.send(msg);
			logger.info("纯文本邮件已发送至"
					+ StringUtils.arrayToCommaDelimitedString(msg.getTo()));
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * Description: 发送纯文本的用户通知。
	 * 
	 * @author Sword
	 * @param 邮件地址
	 * @param 邮件标题
	 * @param 邮件内容
	 * @since 2011年3月3日 14时06分08秒
	 */
	public boolean simpleSendMail(String emailAddress, String title,
			String content) {
		return this.simpleSendMail(emailAddress, "springside3.demo@gmail.com",
				title, content);
	}

	/**
	 * 文本多地址发送
	 * 
	 * @author 万业
	 * @param emailAddresses
	 * @param fromAddress
	 * @param title
	 * @param content
	 * @return
	 */
	public boolean simpleSendMail(String[] emailAddresses, String fromAddress,
			String title, String content) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			if (emailAddresses == null || emailAddresses.length == 0) {
				return false;
			}
			if (title == null || title.equals("")) {
				msg.setSubject("用户修改通知");
			} else {
				msg.setSubject(title);
			}
			msg.setFrom(fromAddress);
			msg.setTo(emailAddresses);
			msg.setSentDate(new Date());
			msg.setText(content);
			mailSender.send(msg);
			logger.info("纯文本邮件已发送至"
					+ StringUtils.arrayToCommaDelimitedString(msg.getTo()));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送邮件失败", e);
			return false;
		}
	}

	/**
	 * 发送图文邮件
	 * 
	 * @author 万业
	 * @param emailAddresses
	 *            目标地址
	 * @param fromAddress
	 *            本地址
	 * @param title
	 *            标题目
	 * @param content
	 *            内容
	 * @param basePath
	 *            http://111.111.111.111/firsthd
	 * @return
	 */
	public boolean htmlSendMail(String[] emailAddresses, String fromAddress,
			String title, String content, String basePath) throws Exception {
		if (org.apache.commons.lang.StringUtils.isBlank(content)
				|| emailAddresses.length == 0) {
			return false;
		}

		String regxstr = "<input[^<,>]*src=\"/firsthd/userfiles/image/.{1,20}[gif,jpg].{0,5}/>";
		String regxsrc = "src=\"/firsthd/userfiles/image/.*[gif,jpg]\"";
		Pattern pa = Pattern.compile(regxstr, Pattern.MULTILINE);
		Pattern pasrc = Pattern.compile(regxsrc);

		Matcher m = pa.matcher(content);
		// String[] txts=content.split(regxstr);
		ArrayList<String> imgs = new ArrayList<String>();
		while (m.find()) {
			imgs.add(m.group());
		}
		m.reset();

		for (int i = 0; i < imgs.size(); i++) {
			Matcher msrc = pasrc.matcher(imgs.get(i));
			if (msrc.find()) {
				String arrang = msrc.group();
				imgs.remove(i);
				imgs.add(i, arrang.replaceAll("src=", "").replaceAll("\"", ""));
			}
		}

		Properties props = new Properties();
		String smtpserverhost = ResourceBundle.getBundle("application")
				.getString("smtpServer");
		String smtpusername = ResourceBundle.getBundle("application")
				.getString("smtpUserName");
		String smtppassword = ResourceBundle.getBundle("application")
				.getString("smtpPassword");
		String smtpPort = ResourceBundle.getBundle("application").getString(
				"smtpPort");
		props.setProperty("mail.smtp.auth", "true");// 认证
		props.setProperty("mail.transport.protocol", "smtp");// 邮件协议
		props.put("mail.smtp.host", smtpserverhost);// smtp服务主机
		// props.setProperty("mail.smtp.port","25");// smtp协议端口
		props.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);

		Session session = Session.getInstance(props);

		MimeMessage msg = new MimeMessage(session);
		// 从哪里发的邮件
		msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("会达系统邮件")
				+ "\" <" + fromAddress + ">"));
		// 设置邮件标题
		msg.setSubject(title);
		// 发送到目标邮件
		// msg.setRecipients(RecipientType.TO,getAddress(emailAddresses));
		// 抄送的接收者
		// msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse("wang@localhost"));
		// 暗送的接收者
		// msg.setRecipients(Message.RecipientType.BCC,InternetAddress.parse("wang@localhost"));

		// 生成图文内容
		MimeMultipart msgMultipart = this.getMimeMultipart(content, imgs,
				basePath);

		msg.setContent(msgMultipart);
		msg.saveChanges();
		// 邮件输出到本地
		// OutputStream ips = new FileOutputStream("c:\\demo3.eml");
		// msg.writeTo(ips);
		// ips.close();

		Transport transport = session.getTransport();
		transport.connect(smtpusername, smtppassword);
		transport.sendMessage(msg, getAddress(emailAddresses));
		transport.close();

		return true;
	}

	/**
	 * 发送html邮件 万业
	 * 
	 * @param emailAddresses
	 *            收件人地址
	 * @param CCemailAddresses
	 *            抄送人地址
	 * @param BCCemailAddresses
	 *            暗送人地址
	 * @param fromAddress
	 *            发件人地址
	 * @param title
	 *            邮件标题
	 * @param content
	 *            内容
	 * @return
	 * @throws Exception
	 */
	public boolean htmlTxtSendMail(String[] emailAddresses,
			String[] CCemailAddresses, String[] BCCemailAddresses,
			String fromAddress, String title, String content) throws Exception {
		if (org.apache.commons.lang.StringUtils.isBlank(content)
				|| emailAddresses.length == 0) {
			return false;
		}

		Properties props = new Properties();
		String smtpserverhost = ResourceBundle.getBundle("application")
				.getString("smtpServer");
		String smtpusername = ResourceBundle.getBundle("application")
				.getString("smtpUserName");
		String smtppassword = ResourceBundle.getBundle("application")
				.getString("smtpPassword");
		String smtpPort = ResourceBundle.getBundle("application").getString(
				"smtpPort");
		String ssl = ResourceBundle.getBundle("application").getString(
				"smtpSSL");
		props.setProperty("mail.smtp.auth", "true");// 认证
		// props.setProperty("mail.debug", "true");

		props.setProperty("mail.transport.protocol", "smtp");// 邮件协议
		props.put("mail.smtp.host", smtpserverhost);// smtp服务主机
		// props.setProperty("mail.smtp.port","25");// smtp协议端口
		props.setProperty("mail.smtp.port", smtpPort);
		if ("true".equals(ssl)) {// 回密选项
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		}
		PopupAuthenticator authenticator = new PopupAuthenticator();
		authenticator.performCheck(smtpusername, smtppassword);
		Session session = Session.getInstance(props, authenticator);

		Transport transport = session.getTransport();
		transport.connect(smtpusername, smtppassword);

		MimeMessage msg = new MimeMessage(session);
		// 从哪里发的邮件
		msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("会达系统邮件")
				+ "\" <" + fromAddress + ">"));
		// 设置邮件标题
		msg.setSubject(title);
		// 发送到目标邮件
		// msg.setRecipients(RecipientType.TO,getAddress(emailAddresses));
		 msg.addRecipients(javax.mail.internet.MimeMessage.RecipientType.TO,getAddress(emailAddresses));
		// 抄送的接收者
		if (null != CCemailAddresses && CCemailAddresses.length > 0) {
			// msg.setRecipients(Message.RecipientType.CC,getAddress(CCemailAddresses));
			msg.addRecipients(javax.mail.internet.MimeMessage.RecipientType.CC,
					getAddress(CCemailAddresses));
		}
		// 暗送的接收者
		if (null != BCCemailAddresses && BCCemailAddresses.length > 0) {
			// msg.setRecipients(Message.RecipientType.BCC,getAddress(BCCemailAddresses));
			msg.addRecipients(
					javax.mail.internet.MimeMessage.RecipientType.BCC,
					getAddress(BCCemailAddresses));

		}
		// 生成htmltext内容
		MimeMultipart msgMultipart = this.getMimeMultipart(content, null, null);
		msg.setContent(msgMultipart);

		msg.saveChanges();

		// 邮件输出到本地
		// OutputStream ips = new FileOutputStream("c:\\demo3.eml");
		// msg.writeTo(ips);
		// ips.close();

		// transport.s
//		transport.sendMessage(msg, getAddress(emailAddresses));
		Transport.send(msg);
		transport.close();

		return true;
	}

	/**
	 * 装配邮件内容
	 * 
	 * @author 万业
	 * @param txts
	 * @param src
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	public MimeMultipart getMimeMultipart(String content,
			ArrayList<String> src, String basePath) throws Exception {
		/*
		 * 文本demo <div>firsthd<br /> wan ye hreo</div> <p><input type="image"
		 * height="94" width="113"
		 * src="/firsthd/userfiles/image/20101021141358.gif" /></p> <p>文本例子</p>
		 * String regxstr="<.*src=\"/firsthd/userfiles/image/.*[gif,jpg].*>";
		 */
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		MimeBodyPart msgContent = new MimeBodyPart();
		msgMultipart.addBodyPart(msgContent);

		MimeMultipart bodyMultipart = new MimeMultipart("related");
		msgContent.setContent(bodyMultipart);

		MimeBodyPart htmlPart = new MimeBodyPart();
		bodyMultipart.addBodyPart(htmlPart);
		// 正文
		htmlPart.setContent(content, "text/html;charset=gbk");// "text/html;charset=gbk"
		// 图片
		if (null != src) {
			for (String srcstr : src) {
				MimeBodyPart gifPart = new MimeBodyPart();

				DataSource ds = new URLDataSource(new URL(basePath + srcstr));
				DataHandler dh = new DataHandler(ds);
				gifPart.setDataHandler(dh);
				gifPart.setHeader("Content-Location", srcstr);

				bodyMultipart.addBodyPart(gifPart);
			}
		}

		return msgMultipart;

	}

	/**
	 * 单地址发送
	 * 
	 * @author 万业
	 */
	public boolean simpleSendMail(String emailAddress, String fromAddress,
			String title, String content) {
		return this.simpleSendMail(new String[] { emailAddress }, fromAddress,
				title, content);
	}

	/**
	 * Spring的MailSender.
	 * 
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 邮件内容的字符串模板.
	 */
	public void setTextTemplate(String textTemplate) {
		this.textTemplate = textTemplate;
	}

	/**
	 * 字符串转化成Adress类
	 * 
	 * @author 万业
	 * @param addStr
	 * @return
	 * @throws AddressException
	 */
	public Address[] getAddress(String[] addStr) throws AddressException {
		if (addStr.length == 0) {
			return null;
		}
		// Address[] addresses=new Address[addStr.length];
		String str = "";
		for (int i = 0; i < addStr.length; i++) {
			// addresses[i] = new InternetAddress(addStr[i]);
			str += addStr[i] + ",";

		}
		return javax.mail.internet.InternetAddress.parse(str);
		// return addresses;
	}

	public class PopupAuthenticator extends Authenticator {
		String username = null;
		String password = null;

		public PopupAuthenticator() {
		}

		public PasswordAuthentication performCheck(String user, String pass) {
			username = user;
			password = pass;
			return getPasswordAuthentication();
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	public static void main(String[] args) {
		SimpleMailService SimpleMailService = new SimpleMailService();
		try {
			SimpleMailService.htmlTxtSendMail(new String[]{"hudixin@firsthuida.com"}, new String[]{"cashhu@126.com","cashhu@126.com","cashhu@126.com"}, null, "hudixin@firsthuida", "测试", "测试 一下");
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
//		SimpleMailService.simpleSendMail("54767201@qq.com", "测试", "测试 一下");
	}

}
