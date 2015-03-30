package com.fhd.sys.web.controller.param;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fhd.fdc.utils.fileupload.FileUpload;
import com.fhd.fdc.utils.fileupload.FileUploadBean;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.param.SystemParamBO;
import com.fhd.sys.entity.file.FileUploadEntity;
/**
 * 系统基本参数设置Controller;
 * ClassName:SystemParam
 * @author   陈燕杰
 * @Date	 2011	2011-3-24		下午05:37:31
 */
@Controller
public class SystemParamControl {
	public String[] classNameArray=new String[]{"com.fhd.fdc.commons.business.sys.param.AddedJob","com.fhd.fdc.commons.business.sys.param.AddedJob2"};
	int flag=2;
	@Autowired
	private SystemParamBO o_systemParamBO;
	@Autowired
	private FileUploadBO o_fileUploadBO;

	// TODO 重写
	/*@SuppressWarnings("rawtypes")
	@RequestMapping(value="/sys/param/test.do")
	public void test(HttpServletResponse response) throws IOException, SchedulerException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
//		 ContextLoader.getCurrentWebApplicationContext().getBean(StatisticsBO.class);
		org.quartz.impl.StdScheduler scheduler=(org.quartz.impl.StdScheduler)(ContextLoader.getCurrentWebApplicationContext().getBean("sf"));
		Scheduler sdl=(Scheduler)scheduler;
		CronTrigger trigger=(CronTrigger)sdl.getTrigger("testTrigger", Scheduler.DEFAULT_GROUP);
		trigger.setCronExpression("0/3 * * * * ?");
		sdl.rescheduleJob("testTrigger", Scheduler.DEFAULT_GROUP, trigger);
//		sdl.pauseJob("testJobDetail", Scheduler.DEFAULT_GROUP);
		for(int i=0;i<flag;i++)
		{
			Class tempClass=Class.forName(classNameArray[i]);
			Object o=tempClass.newInstance();
			if(!(o instanceof Job))
			{
				continue;
			}
			JobDetail jobDetail=new JobDetail(i+"",tempClass);
			CronTrigger cronTrigger=new CronTrigger(classNameArray[i],Scheduler.DEFAULT_GROUP,"0/3 * * * * ?");
			sdl.scheduleJob(jobDetail, cronTrigger);
		}
	}*/
	/**
	 *  修改系统的属性值；
	 * @author 陈燕杰
	 * @param keyArray:系统属性名；
	 * @param valueArray：系统属性修改后的值；
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/param/modifySysParam.do")
	public void modifySysParam(String keyArray,String valueArray,HttpServletResponse response) throws IOException
	{
		PrintWriter out=response.getWriter();
		String result=this.o_systemParamBO.updateSystemParam(keyArray,valueArray);
		out.write(result);
		out.close();
	}
	/**
	 * 显示系统图片的上传页面； 
	 * @author 陈燕杰
	 * @param name
	 * @param model
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/param/showSysPicImport.do")
	public String showSystemPictureUploadPage(String name,Model model) throws IOException
	{
		model.addAttribute("name", name);
		return "sys/param/sysPictureImport";
	}
	/**
	 * 显示系统参数设置页面; 
	 * @author 陈燕杰
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	
	@RequestMapping(value="sys/paramset.do")
	public String systemParamSet(Model model) throws IOException
	{
		model.addAttribute("source",this.o_systemParamBO.getPropJSON().toString());
		return "sys/param/sysParamSet";
	}
	
	/**
	 * 显示系统参数设置页面; 
	 * @author 万业
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/sysset/sysparam.do")
	public String systemParam(Model model) throws IOException
	{
		model.addAttribute("source",this.o_systemParamBO.getPropJSON().toString());
		return "sys/param/sysParam";
	}
	/**
	 * 显示系统任务设置页面; 
	 * @author 万业
	 * @param model
	 * @return String
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/sysset/sysico.do")
	public String systemICO(Model model) throws IOException{
		return "sys/param/sysICO";
	}
	/**
	 * 显示系统任务设置页面; 
	 * @author 万业
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/sysset/systask.do")
	public String systemTask(){
		return "sys/param/sysTask";
	}
	/**
	 * 系统管理的图片上传； 
	 * @author 陈燕杰
	 * @param fileUploadBean:上传文件包装类
	 * @param result
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/param/systemPictureUpload.do")
    public void uploadSysPic(HttpSession session,FileUploadBean fileUploadBean, BindingResult result, HttpServletResponse response,String name) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try{
			if(fileUploadBean!=null && fileUploadBean.getUploadFile()!=null){
				FileUploadEntity file = FileUpload.uploadFile(fileUploadBean.getUploadFile());
				if(StringUtils.isBlank(file.getUploadErrorMessage())){
					o_fileUploadBO.saveFileUpload(file);
					ServletContext context=session.getServletContext();
					String absolutePath;
					if("banner.jpg".equals(name))
					{
						absolutePath=context.getRealPath("/scripts/ext-3.3.0/resources/images/default/"+name);
					}
					else
					{
						absolutePath=context.getRealPath("/images/sysDyn/"+name);
					}
					File of=new File(absolutePath);
					OutputStream os=new FileOutputStream(of);
					InputStream is=new ByteArrayInputStream(file.getContents());
					IOUtils.copy(is, os);
					os.close();
					is.close();
					flag="true";
				}else
					flag = file.getUploadErrorMessage();//上传的文件有错误
			}
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
    }
}
