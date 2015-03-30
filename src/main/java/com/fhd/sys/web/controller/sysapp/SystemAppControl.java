package com.fhd.sys.web.controller.sysapp;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.business.sysapp.SysAppBO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 
 * 响应邮件，短信
 * ClassName:SystemAppControl
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-4-8		上午09:33:04
 *
 */
@Controller
public class SystemAppControl {
	@Autowired
	private SysAppBO o_sysAppBO;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	
	/**
	 * @author 万业
	 * 进入发送邮件
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/email.do")
	public String sendMail(Model model, HttpServletResponse response) throws Exception{
		//获取模板列表
		OperatorDetails operatorDetails = UserContext.getUser();
		model.addAttribute("source",this.o_sysAppBO.getFtlnames());
		model.addAttribute("currentUsername", operatorDetails.getUsername());
		return "/sys/sysapp/email";
	}
	/**
	 * 获取邮件模板列表 
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/getftljsonlist.do")
	public void getjsondata(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		PrintWriter pw = response.getWriter();
		pw.write(this.o_sysAppBO.getMailJSON().toString());
		
	}
	/**
	 * 发送邮件
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/dosendmail.do")
	public String doSendMail(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		//String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

		
		String[] emailAddress=request.getParameterValues("emps");//.getParameter("emps");
		String title=request.getParameter("title");
		String source = request.getParameter("mailContent");
		
		OperatorDetails operatorDetails = UserContext.getUser();
		SysEmployee emp = this.o_empolyeeBO.questEmployeeByUserid(operatorDetails.getUserid());

		this.o_sysAppBO.sendHtmlMail(emailAddress,emp.getPemail(), title,source,basePath);
		
		response.getWriter().write("true");
		return "/sys/sysapp/emailSuccess";
		
	}
	/**
	 * 进入模板列表 并把列表内容加载
	 * @author 万业
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/emailftl.do")
	public String ftlList(Model model, HttpServletResponse response) throws Exception{
		//获取模板详细列表
		//model.addAttribute("source",this.o_sysAppBO.getMailJSON().toString());
		JSONArray array = this.o_sysAppBO.getMailJSON();
		model.addAttribute("datas", array);
		
		return "/sys/sysapp/ftl";
	}
	/**
	 * 查看文件内容
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/viewfile.do")
	public String viewFileContent(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		String ext = request.getParameter("type");
		String name = request.getParameter("filename");
		String fileName=name+"."+ext;
		String content = o_sysAppBO.getfileContent(fileName);
		if(ext.equals("ftl")){
			model.addAttribute("type", "模板");
		}
		if(ext.equals("properties")){
			model.addAttribute("type", "数据定义");
		}
		model.addAttribute("fileContent", content);
		
		
		return "/sys/sysapp/viewftl";
	}
	/**
	 * 在服务器上把上传组件得到文件复制到模板目录
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/transferfile.do")
	public void transferFile(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		PrintWriter pw = response.getWriter();
		String fileId=request.getParameter("fileid");
		String reslut = o_sysAppBO.transferFile(fileId);
		pw.write(reslut);
	}
	/**
	 * 删除模板及定义
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/delfile.do")
	public void removeFile(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		String result="false";
		String ids = request.getParameter("ids");
		if(StringUtils.isNotBlank(ids)){
			String[] fileIds = ids.split(",");
			for (int i = 0; i < fileIds.length; i++) {
				result=this.o_sysAppBO.removeFile(fileIds[i]+".ftl");
				result=this.o_sysAppBO.removeFile(fileIds[i]+".properties");
			}
		}
		response.getWriter().write(result);
	}
	/**
	 * 进入模板数据定义赋值页面
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/proProcess.do")
	public String proProcess(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		String ftl = request.getParameter("ftltype");
		JSONObject source = o_sysAppBO.getPropJSON(ftl);
		model.addAttribute("source", source);
		model.addAttribute("ftltype", ftl);
		return "/sys/sysapp/proProcess";
		
	}
	/**
	 * 整合模板与数据定义 生成发送文本
	 * @author 万业
	 * 成功 content
	 * 失败 false
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/process.do")
	public void processFtl(String ftltype,String keyArray,String valueArray,HttpServletResponse response) throws Exception{
		String content = "";
		PrintWriter pw =null;
		try{
			content = o_sysAppBO.processFlt(ftltype,keyArray,valueArray);
			pw = response.getWriter();
			pw.write(content);
			
		}catch(Exception e){
			pw.write("true");
			pw.flush();
		}finally{
			if(pw!=null){
				pw.close();
			}
		}
	}
	/**
	 * 进入短信发送页面
	 * @author 万业
	 * @param model
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/message.do")
	public String presendMessage(Model model, HttpServletResponse response) throws Exception{
		//获取模板列表
		OperatorDetails operatorDetails = UserContext.getUser();
		model.addAttribute("source",this.o_sysAppBO.getFtlnames());
		model.addAttribute("currentUsername", operatorDetails.getUsername());
		return "/sys/sysapp/message";
		
	}
	/**
	 * 根据模板添写短信
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/msgproProcess.do")
	public String msgproProcess(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		String ftl = request.getParameter("ftltype");
		JSONObject source = o_sysAppBO.getPropJSON(ftl);
		model.addAttribute("source", source);
		model.addAttribute("ftltype", ftl);
		return "/sys/sysapp/msgproProcess";
		
	}
	/**
	 * 发送短信
	 * @author 万业
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/sendMsg.do")
	public String sendMessage(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		String[] emailAddress=request.getParameterValues("emps");//.getParameter("emps");
		String source = request.getParameter("mailContent");
		
		String sendstr = this.o_sysAppBO.sendMessage(emailAddress,source);
		request.getRequestDispatcher(sendstr).forward(request, response);
		
		return "/sys/sysapp/emailSuccess";
		
	}
	/**
	 * 进入消信提醒设置页面
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/remind.do")
	public String remind(Model model,HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		return "/sys/sysapp/reminding";
		
	}
	/**
	 * 查询与用户相关消信提醒设置
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/queryRemind.do")
	public void queryRemind(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		OperatorDetails operatorDetails = UserContext.getUser();
		SysUser user=new SysUser();
		user.setId(operatorDetails.getUserid());
		PrintWriter pw = response.getWriter();
		pw.write(this.o_sysAppBO.getRmaindJSON(user).toString());
		
	}
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/queryNewRemind.do")
	public void queryNewRemind(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		OperatorDetails operatorDetails = UserContext.getUser();
		SysUser user=new SysUser();
		user.setId(operatorDetails.getUserid());
		PrintWriter pw = response.getWriter();
		pw.write(this.o_sysAppBO.getNewRmaindJSON(user).toString());
		
	}
	/**
	 * 设置 emaile mobile
	 * @param model
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/sysapp/setUserRemind.do")
	public void setUserRemind(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		PrintWriter out=response.getWriter();
		OperatorDetails operatorDetails = UserContext.getUser();
		String items=request.getParameter("items");
		String[] item = items.split(",");
		if(item.length==0)
			return;
		
		try{
			o_sysAppBO.saveRemind(item,operatorDetails.getUserid());
			out.write("true");
		}catch(Exception e){
			out.write("false");
			throw e;
		}finally{
			out.close();
		}
		
	}
	
	
	/**
	 * 测试邮件发送
	 */
	@RequestMapping(value="/sys/sysapp/testtrigger.do")
	public void testtrigger(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{
		OperatorDetails operatorDetails = UserContext.getUser();
		String userId = operatorDetails.getUserid();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("name", "wanye");
		
		this.o_sysAppBO.triggerSendUserByFtl(userId, "risk_safe", map, basePath, "testtile");
	}
	
	/**
	 * 测试一会就删除
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/test/editgrid.do")
	public String zwytest(Model model, HttpServletResponse response,HttpServletRequest request) throws Exception{

		JSONArray array=new JSONArray();
		//String filename = "plants.xml";
		File file=new File("E:\\dpt_workspace2\\firsthd-development-center\\web\\WEB-INF\\classes\\plants.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(file); 
		List list = document.selectNodes("//plant" );
		Iterator iter=list.iterator();
		int i=0;   
		while(iter.hasNext()){
			   i++;
			   JSONObject result=new JSONObject();
				result.put("id", i);
		      Element element=(Element)iter.next();
		      result.put("common", element.selectSingleNode("common").getText());
				result.put("botanical", element.selectSingleNode("common").getText());
				result.put("light", element.selectSingleNode("common").getText());
				result.put("price", element.selectSingleNode("common").getText());
				result.put("availDate", element.selectSingleNode("common").getText());
				result.put("indoor", element.selectSingleNode("common").getText());
		      
				array.add(result);
		 } 
		
		return array.toString();
		
	}
		
	
}
