package com.fhd.sys.business.sysapp;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.email.SimpleMailService;
import com.fhd.fdc.commons.exception.FHDException;
import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAOold;
import com.fhd.sys.dao.sysapp.MailremindTempDAO;
import com.fhd.sys.dao.sysapp.TempUserDAO;
import com.fhd.sys.entity.app.MailremindTemp;
import com.fhd.sys.entity.app.TempUser;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmployee;

import freemarker.template.Template;


/**
 * 
 * ClassName:MailremindTempBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-5-10		下午02:55:47
 *
 */
@Service
@SuppressWarnings("unchecked")
public class MailremindTempBO {
	
	@Autowired
	private MailremindTempDAO o_mailremindTempDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private SysEmployeeDAOold o_sysEmployeeDAO;
	@Autowired
	private TempUserDAO o_tempUserDAO;
	@Autowired
	private FreeMarkerConfigurationFactoryBean freemarkerConfiguration;
	@Autowired
	private SimpleMailService simpleMailService;
	@Autowired
	private SysAppBO o_sysAppBo;
	/**
	 * 邮件提醒模板 条件 分页 列表
	 * @author 万业
	 * @param page
	 * @param tempName
	 * @return
	 */
	public Page<MailremindTemp> queryMailremaindTemp(Page<MailremindTemp> page,String tempName){
		DetachedCriteria dc=DetachedCriteria.forClass(MailremindTemp.class);
		if(StringUtils.isNotBlank(tempName)){
			dc.add(Restrictions.like("tempName", tempName, MatchMode.ANYWHERE));
		}
		
		String companyid=UserContext.getUser().getCompanyid();
		dc.add(Restrictions.eq("company.id", companyid));
		return o_mailremindTempDAO.pagedQuery(dc,page);
	}
	/**
	 * 邮件提醒模板添加
	 * @author 万业
	 * @param mailremindTemp
	 * @throws Exception
	 */
	@Transactional
	public void saveMailremindTemp(MailremindTemp mailremindTemp)throws Exception{
		try {
			this.o_mailremindTempDAO.save(mailremindTemp);
			o_businessLogBO.saveBusinessLogInterface("新增", "邮件提醒模板", "成功", mailremindTemp.getTempName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "邮件提醒模板", "失败", mailremindTemp.getTempName());
			throw e;
		}
	}
	/**
	 * 邮件提醒模板更新
	 * @author 万业
	 * @param mailremindTemp
	 * @throws Exception
	 */
	@Transactional
	public void updateMailremindTemp(MailremindTemp mailremindTemp)throws Exception{
		try {
			this.o_mailremindTempDAO.merge(mailremindTemp);
			o_businessLogBO.saveBusinessLogInterface("修改", "邮件提醒模板", "成功", mailremindTemp.getTempName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("修改", "邮件提醒模板", "失败", mailremindTemp.getTempName());
			throw e;
		}
	}
	/**
	 * 邮件提醒模板删除
	 * @author 万业
	 * @param id
	 */
	@Transactional
	public void removeMRT(String id) throws Exception{
		try {
			o_mailremindTempDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "邮件提醒模板", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "邮件提醒模板", "失败", id);
			throw e;
		}
	}
	
	
	
	/**
	 * 获取模板
	 * @author 万业
	 * @param id
	 * @return
	 */
	public MailremindTemp getMailremindTemp(String id){
		return this.o_mailremindTempDAO.get(id);
	}
	/**
	 * 获取当前用户的提醒设置
	 * @param sysUser
	 * @return
	 */
	public JSONArray getERTSetJSON(SysUser sysUser){
		JSONArray array=new JSONArray();
		SysEmployee sysEmployee = this.o_sysEmployeeDAO.getSysEmployeeByUserId(sysUser.getId());
		
		List<TempUser> list = this.o_tempUserDAO.getTempUserByEmployeeId(sysEmployee.getId());
		
		for (Iterator<TempUser> iterator = list.iterator(); iterator.hasNext();) {
			TempUser tempUser = iterator.next();
			JSONObject result=new JSONObject();
			result.put("id", tempUser.getId());
			result.put("tempName", tempUser.getMailremindTemp().getTempName());
			result.put("tempEmail", tempUser.getRemindEmail());
			result.put("tempMobile", tempUser.getRemindMobile());
			array.add(result);
		}
		return array;
	}
	/**
	 * 初始化
	 * @author 万业
	 * @param sysUser
	 * @return
	 */
	@Transactional
	public JSONArray getNewERTSetJSON(SysUser sysUser){
		SysEmployee sysEmployee = this.o_sysEmployeeDAO.getSysEmployeeByUserId(sysUser.getId());
		this.o_tempUserDAO.removeByEmployeeId(sysEmployee.getId());
		
		List<MailremindTemp> mrts = this.o_mailremindTempDAO.getAll();
		for(MailremindTemp mrt:mrts){
			TempUser tempUser=new TempUser();
			tempUser.setId(Identities.uuid());
			tempUser.setMailremindTemp(mrt);
			tempUser.setSysEmployee(sysEmployee);
			tempUser.setRemindEmail(0);
			tempUser.setRemindMobile(0);
			
			this.o_tempUserDAO.save(tempUser);
		}
		
		return this.getERTSetJSON(sysUser);
		
	}
	
	/**
	 * 更新消息提醒设置
	 * @author 万业
	 * @param map
	 * @param userId
	 */
	@Transactional
	public void updateCurrentTempUser(String[] item,String userId){
		SysEmployee sysEmployee = this.o_sysEmployeeDAO.getSysEmployeeByUserId(userId);
		List<TempUser> list = this.o_tempUserDAO.getTempUserByEmployeeId(sysEmployee.getId());
		
		for (Iterator<TempUser> iterator = list.iterator(); iterator.hasNext();) {
			TempUser tempUser = iterator.next();
			tempUser.setRemindEmail(0);
			tempUser.setRemindMobile(0);
			
		}
		
		//设置
		for (int i = 0; i < item.length; i++) {
			if(StringUtils.isBlank(item[i])){
				continue;
			}
			String id = item[i].substring(0, 32);
			String kid=item[i].substring(item[i].indexOf("_")+1, item[i].length());
			for (Iterator<TempUser> iterator = list.iterator(); iterator.hasNext();) {
				TempUser tempUser = iterator.next();
				if(tempUser.getId().equals(id)){
					if(kid.equals("email"))
						tempUser.setRemindEmail(1);
					if(kid.equals("mobile"))
						tempUser.setRemindMobile(1);
				}
				
			}
			
		}
		//保存
		for (Iterator<TempUser> iterator = list.iterator(); iterator.hasNext();) {
			TempUser tempUser = iterator.next();
			this.o_tempUserDAO.update(tempUser);
			
		}
	}
	/**
	 * 默认针对所有用户的设置为过滤发送
	 * @param tempId
	 * @param fromAddress
	 * @param datas
	 * @param content
	 * @param title
	 * @throws Exception
	 */
	public void quartzTriggerSend(String tempId, String fromAddress, HashMap<String,Object> datas ,String content, String title) throws Exception{
		this.quartzTriggerSend(tempId, fromAddress, datas, content, title,null);
	}
	
	/**
	 * 调度 触发 系统发送消息入口 
	 * @author 万业
	 * @param empId 模板ID
	 * @param fromAddress 回信地址
	 * @param datas 数据源
	 * @param content 内容
	 * @param title 邮件标题
	 * @param tempUsers 发送用户的列表
	 */
	public void quartzTriggerSend(String tempId, String fromAddress, HashMap<String,Object> datas ,String content, String title, List<TempUser> tempUsers) throws Exception{
		//SysEmployee empolyee = this.o_sysEmployeeDAO.get(empId[0]);
		CharArrayReader charArrayReader = new CharArrayReader(content.toCharArray());
		CharArrayWriter caw = new CharArrayWriter();
		if(null==tempUsers){
			tempUsers=this.o_tempUserDAO.getTempUserByTempId(tempId);
		}
		//List<TempUser> tempUsers = this.o_tempUserDAO.getTempUserByTempId(tempId);
		Template template= new Template("",charArrayReader,freemarkerConfiguration.createConfiguration());
		SimpleDateFormat allsdfch  =   new  SimpleDateFormat( "yyyy年MM月dd日" );
		SimpleDateFormat allsdfen  =   new  SimpleDateFormat( "yyyy/MM/dd" );
		
		SimpleDateFormat timeallsdfch  =   new  SimpleDateFormat( "HH时mm分" );
		SimpleDateFormat timeallsdfen  =   new  SimpleDateFormat( "HH:mm" );
		if(null==datas)
		{
			datas=new HashMap<String,Object>();
		}
		datas.put("datech", allsdfch.format(new Date()));
		datas.put("dateen", allsdfen.format(new Date()));
		
		datas.put("timech", timeallsdfch.format(new Date()));
		datas.put("timeen", timeallsdfen.format(new Date()));
		
		for(TempUser tempUser:tempUsers){
			if(null!=datas){
				//放入与员工相关的变量
				SysEmployee employee=tempUser.getSysEmployee();
				datas.put("employeename", employee.getRealname()!=null?employee.getRealname():"");
				datas.put("username", employee.getUsername()!=null?employee.getUsername():"");
				datas.put("username", "402881b22b0f8594012b0f87bdb70004".equals(employee.getGender()!=null?employee.getGender():"")?"先生":"女士");
				datas.put("dutyname", employee.getDuty()!=null?employee.getDuty().getDutyName():"");
				datas.put("companyname", employee.getSysOrganization()!=null?employee.getSysOrganization().getOrgname():"");
				
				datas.put("bithdaych", employee.getBirthdate()!=null?allsdfch.format(employee.getBirthdate()):"");
				datas.put("bithdayen", employee.getBirthdate()!=null?allsdfen.format(employee.getBirthdate()):"");
				
			}
			template.process(datas, caw);
			String sendContet = caw.toString();
			if(1==tempUser.getRemindEmail()){
					
					simpleMailService.htmlTxtSendMail(new String[]{tempUser.getSysEmployee().getOemail()}, null, null, fromAddress, title, sendContet);
					//simpleMailService.htmlTxtSendMail(new String[]{"wanye1977@163.com"}, null, null, fromAddress, title, sendContet);
					
					
			}
			if(1==tempUser.getRemindMobile()){
				//发手机 TODO
//				String mobileNumber=tempUser.getSysEmployee().getMobikeno();
				//
			}
			
		}
		
		
	}
	/**
	 * 手动 触发 系统发送消息入口 
	 * @author wanye
	 * @param employeeId 收件员工ID
	 * @param employeesIdBB 抄送人ID null 不传
	 * @param employeesIdBBC 明抄，暗抄 null 不传
	 * @param tempCode 模板代码
	 * @param fromAddress 发送人地址
	 * @param datas 数据源
	 * @param title 邮件标题
	 * @return boolean 
	 */
	public boolean manualTriggerSend(String employeeId, String[] employeesIdBB,String[] employeeIdBBC,String fromAddress,String tempCode,HashMap<String, Object> datas,String title) throws Exception{
		
		boolean result=false;
		
		TempUser tempUser = this.o_tempUserDAO.getTempUserByEmployeeIdAndCode(employeeId,tempCode);
		if(null==tempUser){
			throw new FHDException("用户提醒设置为空:com.fhd.sys.entity.app.TempUser ");
		}
		MailremindTemp mailremindTemp = o_mailremindTempDAO.getMailremindTempByCode(tempCode);
		if(null==mailremindTemp){
			throw new FHDException("模板代码："+tempCode+"对应的模板没找到！");
		}
		if(1==tempUser.getRemindEmail()){
			String toAddress=this.o_sysEmployeeDAO.get(employeeId).getOemail();
			
			String[] bb=null;
			if(null!=employeesIdBB && employeesIdBB.length>0){
				List<String> list=this.o_sysEmployeeDAO.getEmailById(employeesIdBB);
				bb = list.toArray(new String[list.size()]);
			}
			String[] bbc=null;
			if(null!=employeesIdBB && employeesIdBB.length>0){
				List<String> list=this.o_sysEmployeeDAO.getEmailById(employeesIdBB);
				bbc = list.toArray(new String[list.size()]);
			}
			
			HashMap<String, Object> map = this.o_sysAppBo.getArrangeMap(datas);
			
			String content=mailremindTemp.getTempContents();
			CharArrayReader charArrayReader = new CharArrayReader(content.toCharArray());
			CharArrayWriter caw = new CharArrayWriter();
			Template template= new Template("",charArrayReader,freemarkerConfiguration.createConfiguration());
			template.process(map, caw);
			
			simpleMailService.htmlTxtSendMail(new String[]{toAddress},bb,bbc,fromAddress,title,caw.toString());
			
			result=true;
		}
		
		if(1==tempUser.getRemindMobile()){
			// TODO
			result=true;
		}
		
		return result;
	}
	/**
	 * 手动 触发 系统发送消息入口 
	 * @author wanye
	 * @param employeeId 收件员工ID[]
	 * @param employeesIdBB 抄送人ID null 不传
	 * @param employeesIdBBC 明抄，暗抄 null 不传
	 * @param tempCode 模板代码
	 * @param fromAddress 发送人地址
	 * @param datas 数据源
	 * @param title 邮件标题
	 * @return boolean 
	 */
	public boolean manualTriggerSend(String[] employeeId, String[] employeesIdBB,String[] employeeIdBBC,String fromAddress,String tempCode,HashMap<String, Object> datas,String title) throws Exception{
		boolean result=false;
		
		MailremindTemp mailremindTemp = o_mailremindTempDAO.getMailremindTempByCode(tempCode);
		if(null==mailremindTemp){
			throw new FHDException("模板代码："+tempCode+"对应的模板没找到！");
		}
		ArrayList<String> emailSeteds=new ArrayList<String>();
		ArrayList<String> mobileSeteds=new ArrayList<String>();
		for(String empid:employeeId){
			
			TempUser tempUser = this.o_tempUserDAO.getTempUserByEmployeeIdAndCode(empid,tempCode);
			if(null==tempUser){
				throw new FHDException("用户提醒设置为空:com.fhd.sys.entity.app.TempUser ");
			}
			
			if(1==tempUser.getRemindEmail()){
				String toAddress=this.o_sysEmployeeDAO.get(empid).getOemail();
				emailSeteds.add(toAddress);
			}
			if(1==tempUser.getRemindMobile()){
				mobileSeteds.add(empid);
			}
		}
		
		
			
			String[] bb=null;
			if(null!=employeesIdBB && employeesIdBB.length>0){
				List<String> list=this.o_sysEmployeeDAO.getEmailById(employeesIdBB);
				bb = list.toArray(new String[list.size()]);
			}
			String[] bbc=null;
			if(null!=employeesIdBB && employeesIdBB.length>0){
				List<String> list=this.o_sysEmployeeDAO.getEmailById(employeesIdBB);
				bbc = list.toArray(new String[list.size()]);
			}
			
			HashMap<String, Object> map = this.o_sysAppBo.getArrangeMap(datas);
			
			String content=mailremindTemp.getTempContents();
			CharArrayReader charArrayReader = new CharArrayReader(content.toCharArray());
			CharArrayWriter caw = new CharArrayWriter();
			Template template= new Template("",charArrayReader,freemarkerConfiguration.createConfiguration());
			template.process(map, caw);
			
			String[] emailemps=emailSeteds.toArray(new String[emailSeteds.size()]);
			simpleMailService.htmlTxtSendMail(emailemps,bb,bbc,fromAddress,title,caw.toString());
			result=true;
		//短信发送
		//TODO
		
		return result;
	}
	
	@Transactional
	public void initPersonMindSet(){
		OperatorDetails operatorDetails = UserContext.getUser();
		
		String userId = operatorDetails.getUserid();
		SysEmployee sysEmployee = o_sysEmployeeDAO.getSysEmployeeByUserId(userId);
		List<TempUser> tempusers = new ArrayList<TempUser>();
		if(null != sysEmployee)	{
			tempusers = this.o_tempUserDAO.getTempUserByEmployeeId(sysEmployee.getId());
		}
		
		
		List<MailremindTemp> mailremindTemps = this.o_mailremindTempDAO.getAll();
		
		for(MailremindTemp mailremindTemp:mailremindTemps){
			boolean flage=false;
			for(TempUser tempUser:tempusers){
				if(mailremindTemp.getId().equals(tempUser.getMailremindTemp().getId()))
				{
					flage=true;
					break;
				}
			}
			if(false == flage){
				
				TempUser tempUser=new TempUser();
				tempUser.setId(Identities.uuid());
				tempUser.setMailremindTemp(mailremindTemp);
				tempUser.setSysEmployee(sysEmployee);
				tempUser.setRemindEmail(1);
				tempUser.setRemindMobile(0);
				
				this.o_tempUserDAO.save(tempUser);
				
			}
		}
		
	}
	//临时插入所有数据 以后部署可删除
	@Transactional
	public void insertTempuser(){
		//this.o_tempUserDAO.createQuery("delete from TempUser", null).executeUpdate();
		
		List<SysEmployee> sysEmployees = o_sysEmployeeDAO.getAll();
		List<MailremindTemp> mailremindTemps = this.o_mailremindTempDAO.getAll();
		for(SysEmployee sysEmployee:sysEmployees){
			for(MailremindTemp mailremindTemp:mailremindTemps){
				TempUser tempUser=new TempUser();
				tempUser.setId(Identities.uuid());
				tempUser.setMailremindTemp(mailremindTemp);
				tempUser.setSysEmployee(sysEmployee);
				tempUser.setRemindEmail(1);
				tempUser.setRemindMobile(0);
				
				this.o_tempUserDAO.save(tempUser);
			}
		}
		
	}
	//测试
	public boolean manualTriggerSendtest(String[] emailAddresses,
			String[] CCemailAddresses,
			String[] BCCemailAddresses,
			String fromAddress, 
			String title,
			String content) throws Exception{
		
		this.simpleMailService.htmlTxtSendMail(emailAddresses, CCemailAddresses, BCCemailAddresses, fromAddress, title, content);
		return true;
	}

}
