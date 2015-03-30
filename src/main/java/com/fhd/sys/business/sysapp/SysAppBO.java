package com.fhd.sys.business.sysapp;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.email.SimpleMailService;
import com.fhd.sys.business.param.SystemParamBO;
import com.fhd.sys.dao.auth.SysUserDAO;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.file.FileUploadDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAOold;
import com.fhd.sys.dao.sysapp.RemindDAO;
import com.fhd.sys.entity.app.Remind;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * ClassName:SysAppBO
 * Function: 邮件 短信 BO
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-4-8		上午09:36:07
 *
 */
@Service
public class SysAppBO {
	@Autowired
	private SimpleMailService simpleMailService;
	@Autowired
	private FreeMarkerConfigurationFactoryBean freemarkerConfiguration;
	@Autowired
	private FileUploadDAO o_fileUploadDAO;
	@Autowired
	private SysEmployeeDAOold o_sysEmployeeDAO;
	@Autowired
	private RemindDAO o_remindDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	@Autowired
	private SysUserDAO o_sysUserDAO;
	
	
	
	/**
	 * 
	 * @param ids
	 * @param fromAddress
	 * @param title
	 * @param content
	 * @throws Exception
	 */
	public void sendMail(String[] ids,String fromAddress, String title,String content)throws Exception{
		this.simpleMailService.simpleSendMail(getAddressById(ids), fromAddress, title,content);
	}
	/**
	 * 返回模板列表
	 * @author   万业
	 * @return
	 * @throws Exception
	 */
	public JSONArray getMailJSON() throws Exception{
		
		JSONArray array=new JSONArray();
		//Configuration config = freemarkerConfiguration.createConfiguration();
		//从configuration中获取模板的位置
		File dir = new File(getFtlPath());
		
		File file[] = dir.listFiles(); 
		for (int i = 0; i < file.length; i++) {
			if (!file[i].isDirectory() && this.getExt(file[i].getName()).equals(".ftl")){
				JSONObject result=new JSONObject();
				result.put("filename", file[i].getName());
				result.put("leng", file[i].length());
				result.put("filepixname", this.getName(file[i].getName()));
				for(int j=0; j<file.length;j++){
					if(this.getExt(file[j].getName()).equals(".properties") && this.getName(file[i].getName()).equals(this.getName(file[j].getName()))){
						result.put("datadefine", file[j].getName());
					}
				}
				array.add(result);
				
			}
		}
		
		return array;
	}
	/**
	 * 获取模板名列表
	 * @author   万业
	 * @return
	 * @throws Exception
	 */
	public String[] getFtlnames() throws Exception{
		//Configuration config = freemarkerConfiguration.createConfiguration();
		ArrayList<String> array=new ArrayList<String>();
		//从configuration中获取模板的位置
		File dir = new File(getFtlPath());
		
		File file[] = dir.listFiles(); 
		for (int i = 0; i < file.length; i++) {
			if (!file[i].isDirectory() && this.getExt(file[i].getName()).equals(".ftl")){
				array.add(this.getName(file[i].getName()));
			}
		}
		return (String[])array.toArray(new String[array.size()]);
	}
	/**
	 * 获取文件内容
	 * @author 万业
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String getfileContent(String filename)throws Exception{
		//File dir = new File("E:\\dpt_workspace2\\firsthd-development-center\\src\\email");
		String path=getFtlPath()+filename;
		FileReader fr=null;
		StringBuffer sb = new StringBuffer(""); 
		try{
			fr = new FileReader(path);
			char[] buf = new char[1024];
			int len = 0;
			while ((len = fr.read(buf)) > 0) {
				sb.append(buf,0,len);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fr!=null){
				fr.close();
			}
		}
		return sb.toString();
	}
	/**
	 * 把文件放置到email目录
	 * @author   万业
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public String transferFile(String fileId) throws Exception{
		FileUploadEntity fue = o_fileUploadDAO.get(fileId);
		String sourcepath = fue.getFileAddress();
		String filename=fue.getOldFileName();
		
		String result="";
		
		FileInputStream in=new FileInputStream(sourcepath);
        File file=new File(getFtlPath()+filename);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file);
        int c;
        byte buffer[]=new byte[1024];
        try{
        	
        	while((c=in.read(buffer))!=-1){
        		out.write(buffer,0,c);        
        	}
        	result="true";
        }catch (Exception e) {
        	result="false";
        	throw e;
		}finally{
			
			in.close();
			out.close();
		}

		
		return result;
	}
	/**
	 * 删除文件
	 * @author   万业
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String removeFile(String fileName)throws Exception{
		//String result ="false";
		String path=getFtlPath()+fileName;
		File file=new File(path);
		file.delete();
		return "true";
	}
	
	/**
	 * 读取配置文件，返回由其构成的JSONObject; 
	 * @author 万业
	 * @return JSONObject
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	
	public JSONObject getPropJSON(String ftlDefine) throws IOException
	{
		JSONObject result=new JSONObject();
		Properties properties=new Properties();
		properties.load(SystemParamBO.class.getClassLoader().getResourceAsStream("email/"+ftlDefine+".properties"));
		Set<Object> keys=properties.keySet();
		for(Object item:keys)
		{
			result.put(item.toString(), properties.get(item));
		}
		return result;
	}
	/**
	 * 整合文档
	 * @author   万业
	 * @param ftltype 模板类型 文件名
	 * @param keyArray 属性名
	 * @param valueArray 属性值
	 * @return
	 * @throws IOException
	 */
	public String processFlt(String ftltype, String keyArray, String valueArray) throws IOException,TemplateException {
		String[] keys=keyArray.split(",");
		String[] values=valueArray.split(",");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i=0;i<keys.length;i++)
		{
			map.put(keys[i], values[i]);
		}
		
		HashMap<String, Object> processMap = this.getArrangeMap(map);
		
		return this.process(ftltype, processMap);
		
		
	}
	
	/**
	 * 把有树关系的单层key value 对转化成树形式的hashmap对象
	 * sample
	 * abc.efg.a="asd"
	 * abc.efg.b=1
	 * abc.efg.c=3
	 * abc.kd="asde"
	 * kfg.asd.d="ased"
	 * kfg.asd.g="asf"
	 * kfg.asd.d="aswd"
	 * 递归转化为map 
	 * @author 万业
	 * @param map
	 * @return
	 */
	public HashMap<String, Object> getArrangeMap(HashMap<String, Object> map){
		Set<String> set = map.keySet();
		HashMap<String, Object> submap=new HashMap<String, Object>();
		boolean exist = false;
		String newkey="";
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if(key.lastIndexOf(".")>0){
				exist = true;
				newkey = key.substring(0,key.lastIndexOf("."));
				break;
			}
		}
		
		if(exist){
			
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				if(key.substring(0, newkey.length()).equals(newkey)){
					submap.put(key.substring(key.lastIndexOf(".")+1,key.length()), map.get(key));
					//map.put(key, null);
					//map.remove(key);
					iterator.remove();
				}
			}
			
			
			map.put(newkey, submap);
			
			return this.getArrangeMap(map);
		}else{
			return map;
		}
	}
	/**
	 * 填充模板
	 * @author 万业
	 * @param ftltype
	 * @param map
	 * @param out
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void process(String ftltype,HashMap<String, Object> map,Writer out)throws IOException,TemplateException{
		Template t = freemarkerConfiguration.createConfiguration().getTemplate(ftltype+".ftl");
		t.process(map, out);
		
	}
	/**
	 * 填充模板
	 * @author 万业
	 * @param ftltype
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String process(String ftltype,HashMap<String, Object> map)throws IOException,TemplateException{
		CharArrayWriter caw = new CharArrayWriter();
		this.process(ftltype, map, caw);
		//StringBuffer result = new StringBuffer("");
		return caw.toString();

	}
	/**
	 * 发送富文本邮件
	 * @author   万业
	 * @param ids
	 * @param fromAddress
	 * @param title
	 * @param content
	 * @param extParam
	 * @throws Exception
	 */
	public void sendHtmlMail(String[] ids,String fromAddress, String title,String content,String extParam)throws Exception{
		this.simpleMailService.htmlSendMail(getAddressById(ids), fromAddress, title, content, extParam);
	}
	/**
	 * 
	 * @author 万业
	 * @param ids employess String[]
	 * @return emailaddress String[]
	 */
	public String[] getAddressById(String[] ids){
		ArrayList<String> mailList=new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			String emailAddress = o_sysEmployeeDAO.get(ids[i]).getPemail();
			if(StringUtils.isNotBlank(emailAddress)){
				mailList.add(emailAddress);
			}
		}
		return mailList.toArray(new String[mailList.size()]);
	}
	/**
	 * 发送短信
	 * @param emailAddress
	 * @param source
	 * @return
	 */
	public String sendMessage(String[] empIds,String source){
		StringBuffer mobileStr=new StringBuffer("");
		for (int i = 0; i < empIds.length; i++) {
			String mobile = o_sysEmployeeDAO.get(empIds[i]).getMobikeno();
			if(StringUtils.isNotBlank(mobile)){
				mobileStr.append(mobile);
				
			}
		}
		
		o_sysEmployeeDAO.get(empIds[0]).getMobikeno();
		String messageURL = ResourceBundle.getBundle("application").getString("messageURL");
		String sendstr=messageURL+"?nomber="+mobileStr.toString()+"&msg="+source;
		return sendstr;
	}
	
	/**
	 * 获取所有该用户的消息提醒设置
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JSONArray getRmaindJSON(SysUser sysUser) throws Exception{
		
		JSONArray array=new JSONArray();

		List<Remind> list = this.saveRemindsByUser(sysUser);
		for (Iterator<Remind> iterator = list.iterator(); iterator.hasNext();) {
			Remind remind = iterator.next();
			JSONObject result=new JSONObject();
			result.put("id", remind.getId());
			result.put("remindItem", remind.getDictEntry().getName());
			result.put("remindftl", remind.getDictEntry().getValue());
			result.put("remindEmail", remind.getRemindEmail());
			result.put("remindMobile", remind.getRemindMobile());
			result.put("remindCode", remind.getDictEntry().getId());
			
			array.add(result);
		}
		
		
		return array;
	}
	/**
	 * 获取最新所有该用户的消息提醒设置
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JSONArray getNewRmaindJSON(SysUser sysUser) throws Exception{
		
		JSONArray array=new JSONArray();

		List<Remind> list = this.resetRemind(sysUser);
		for (Iterator<Remind> iterator = list.iterator(); iterator.hasNext();) {
			Remind remind = iterator.next();
			JSONObject result=new JSONObject();
			result.put("id", remind.getId());
			result.put("remindItem", remind.getDictEntry().getName());
			result.put("remindftl", remind.getDictEntry().getValue());
			result.put("remindEmail", remind.getRemindEmail());
			result.put("remindMobile", remind.getRemindMobile());
			result.put("remindCode", remind.getDictEntry().getId());
			
			array.add(result);
		}
		
		
		return array;
	}
	/**
	 * 获取与个人相关消息提醒
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<Remind> saveRemindsByUser(SysUser user) throws Exception{
		List<Remind> list = this.o_remindDAO.getRemindsByUserid(user.getId());
		List<DictEntry> listdic= o_dictEntryDAO.getUsableDicByType("remind");
		if(list.size()==0){//初始化
			//清空remind数据
			//o_remindDAO.deleteRemindsByUserid(user.getId());
			//添加新设置
			for (Iterator<DictEntry> iterator = listdic.iterator(); iterator.hasNext();) {
				DictEntry dictEntry =  iterator.next();
				
				Remind remind=new Remind();
				remind.setId(Identities.uuid());
				remind.setDictEntry(dictEntry);
				remind.setRemindEmail(0);
				remind.setRemindMobile(0);
				SysUser puser=o_sysUserDAO.get(user.getId());
				remind.setUser(puser);
				
				o_remindDAO.save(remind);
				
			}
			list=this.o_remindDAO.getRemindsByUserid(user.getId());
			
		}
		return list;
	}
	/**
	 * 把与个人相关提醒设置清空，数据初始化
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<Remind>  resetRemind(SysUser user) throws Exception{
		o_remindDAO.deleteRemindsByUserid(user.getId());
		return saveRemindsByUser(user);
		
	}
	/**
	 * 更新设置
	 * @param map
	 * @param userId
	 */
	@Transactional
	public void saveRemind(String[] item,String userId){
		//2清空已有记录
		//this.o_remindDAO.deleteRemindsByUserid(userId);
		List<Remind> list = this.o_remindDAO.getRemindsByUserid(userId);
		for (Iterator<Remind> iterator = list.iterator(); iterator.hasNext();) {
			Remind remind = iterator.next();
			remind.setRemindEmail(0);
			remind.setRemindMobile(0);
			
		}
		
		//设置
		for (int i = 0; i < item.length; i++) {
			if(StringUtils.isBlank(item[i])){
				continue;
			}
			String id = item[i].substring(0, 32);
			String kid=item[i].substring(item[i].indexOf("_")+1, item[i].length());
			for (Iterator<Remind> iterator = list.iterator(); iterator.hasNext();) {
				Remind remind = iterator.next();
				if(remind.getId().equals(id)){
					if(kid.equals("email"))
						remind.setRemindEmail(1);
					if(kid.equals("mobile"))
						remind.setRemindMobile(1);
				}
				
			}
			
		}
		//保存
		for (Iterator<Remind> iterator = list.iterator(); iterator.hasNext();) {
			Remind remind = iterator.next();
			this.o_remindDAO.save(remind);
			
		}
	}
	/**
	 * 系统发送消息入口 
	 * @author 万业
	 * @param empId employeeID
	 * @param dicId 数据字典ID
	 * @param datas 数据源
	 * @bathPath  basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	 */
	public void triggerSend(String[] empId,String dicId,HashMap<String,Object> datas ,String bathPath, String title) throws Exception{
		SysEmployee empolyee = this.o_sysEmployeeDAO.get(empId[0]);
		Remind remind = this.o_remindDAO.getRemindByUserAndDic(empolyee.getUserid(),dicId);
		DictEntry dictEntry = o_dictEntryDAO.get(dicId);
		String ftlName = dictEntry.getValue();//模板名
		HashMap<String, Object> map = getDatafromDefaultFile(ftlName);
		Set<String> set = datas.keySet();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String defineKey = iterator.next();
			map.put(defineKey, datas.get(defineKey));
		}
		String content = this.process(ftlName, map);
		
		if(1==remind.getRemindEmail()){
			
			this.sendHtmlMail(empId, "firsthd", title, content, bathPath);
		}
		if(1==remind.getRemindMobile()){
			this.sendMessage(empId, content);
		}
		
	}
	/**
	 * 系统发送消息入口
	 * @author 万业
	 * @param empId 员工ID
	 * @param dicId 字典条目ID
	 * @param datas
	 * @param bathPath
	 * @param title
	 * @throws Exception
	 */
	public void triggerSendUserByFtl(String userId,String ftl,HashMap<String,Object> datas ,String bathPath, String title) throws Exception{
		DictEntry dictEntry = this.o_dictEntryDAO.getDictEntryByValue(ftl);
		
		this.triggerSendUser(userId, dictEntry.getId(), datas, bathPath, title);
	}
	/**
	 * 系统发送消息入口
	 * @author 万业
	 * @param empId 员工ID
	 * @param dicId 字典条目ID
	 * @param datas
	 * @param bathPath
	 * @param title
	 * @throws Exception
	 */
	public void triggerSend(String empId,String dicId,HashMap<String,Object> datas ,String bathPath, String title) throws Exception{
		this.triggerSend(new String[]{empId}, dicId, datas, bathPath, title);
	}
	/**
	 * 系统发送消息入口
	 * @author 万业
	 * @param userId 用户ID
	 * @param dicId
	 * @param datas
	 * @param bathPath
	 * @param title
	 * @throws Exception
	 */
	public void triggerSendUser(String userId,String dicId,HashMap<String,Object> datas ,String bathPath, String title) throws Exception{
		SysEmployee sysEmployee = this.o_sysEmployeeDAO.getSysEmployeeByUserId(userId);
		this.triggerSend(new String[]{sysEmployee.getId()}, dicId, datas, bathPath, title);
	}
	/**
	 * 根据模板文件对应的数据定义文件中默认值，生成Map树
	 * @param ftltype
	 * @return
	 */
	public HashMap<String, Object> getDatafromDefaultFile(String ftltype)throws IOException, FileNotFoundException{
		String path=getFtlPath()+ftltype+".properties";
		FileInputStream fis=new FileInputStream(path);
		InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
		/*
		FileReader fr=new FileReader(path);
		BufferedReader br=new BufferedReader(fr);
		String line = br.readLine();
		*/
		
		Properties props=new Properties();
		props.load(isr);
		HashMap<String, Object> filedata=new HashMap<String, Object>();
		
		Enumeration<?> em = props.propertyNames();
		while(em.hasMoreElements()){
			String name = em.nextElement().toString();
			filedata.put(name, props.getProperty(name));
		}
		return filedata;
	}
	public Remind getRemindById(String id){
		return this.o_remindDAO.get(id);
		
	}

	public String getExt(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos == -1)
			return "";
		return fileName.substring(pos, fileName.length());
	}

	public String getName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos == -1)
			return fileName;
		else
			return fileName.substring(0, pos);
	}
	//E:\\dpt_workspace2\\firsthd-development-center\\src\\email\\
	public String getFtlPath(){
		String path = SysAppBO.class.getResource("/")+"email\\";
		//return path.replaceFirst("file:/", "");
		return path.substring("file:/".length(),path.length());
	}
	
}
