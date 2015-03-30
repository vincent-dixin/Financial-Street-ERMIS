package com.fhd.sys.web.controller.sysapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.sysapp.ScheduledTaskTempBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.sysapp.ScheduledTaskTemp;

import com.fhd.sys.web.form.sysapp.ScheduledTaskTempForm;



/**
 * 计划任务--模板Controller类.
 * @author  weilunkai
 * @version V1.0  创建时间：2012-5-24
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = ScheduledTaskTempForm.class)
public class ScheduledTaskTempControl {
	@Autowired
	private ScheduledTaskTempBO o_scheduledTaskTempBO;
	
	/**
	 * 添加模版
	 * weilunkai 4.21 
	 */
	
    @RequestMapping(value="/sys/sysapp/scheduledTaskTempAdd.do")
    public String templateAdd(Model model){
    	model.addAttribute("scheduledTaskTempForm",new ScheduledTaskTempForm());
    	return "/sys/sysapp/scheduledTaskTemp/scheduledTaskTempAdd";
    }
	
	/**
	 * 计划任务--模板列表.
	 * @author weilunkai
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/sysapp/scheduledTaskTemplate.do")
	public String queryScheduledTaskTemp(Model model){
		model.addAttribute("scheduledTaskTempForm",new ScheduledTaskTempForm());
		return "/sys/sysapp/scheduledTaskTemp/scheduledTaskTempList";
	}
	/**
	 * 新增计划任务模版.
	 * @author weilunkai
	 * @param scheduledTaskTempForm
	 * @param request
	 * @return String
	 * @throws Exception 
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
    @RequestMapping(value = "/sys/sysapp/scheduledTaskTempSave.do")
	public void saveTemplate(ScheduledTaskTempForm scheduledTaskTempForm,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	ScheduledTaskTemp scheduledTaskTemp = new ScheduledTaskTemp();
    	
		scheduledTaskTemp.setId(Identities.uuid2());
		
		if(scheduledTaskTempForm.getIsEnabled().equals("5BA7324310EDC6FCA40AE8951D315C3F")){
			scheduledTaskTemp.setIsEnabled("1");
		}else if(scheduledTaskTempForm.getIsEnabled().equals("DEFAC7206708FDC56C01488F6CA8A93B")){
			scheduledTaskTemp.setIsEnabled("0");
		}else{
			scheduledTaskTemp.setIsEnabled("");
		}		
		scheduledTaskTemp.setTempName(scheduledTaskTempForm.getTempName());
		DictEntry dealMeasure = new DictEntry();
		dealMeasure.setId(scheduledTaskTempForm.getDictEntryName());
		scheduledTaskTemp.setDealMeasure(dealMeasure);
		
		scheduledTaskTemp.setContent(scheduledTaskTempForm.getContents());
		
		PrintWriter out = null;
		try{
			out=response.getWriter();
			o_scheduledTaskTempBO.saveTemplate(scheduledTaskTemp);
			out.write("true");
		}catch (Exception e) {
			out.write("false");
			e.printStackTrace();
		}finally {
			if(null != out){
				out.close();
			}
		}
	} 
    /**
	 * 修改时保存计划任务模版到Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/sysapp/scheduledTaskTempMod.do",  method = RequestMethod.GET)
	public String contentMod(Model model,HttpServletRequest request){
		String id = request.getParameter("id");
		ScheduledTaskTemp scheduledTaskTemp=o_scheduledTaskTempBO.getTemplateById(id);
		ScheduledTaskTempForm scheduledTaskTempForm = new ScheduledTaskTempForm();
		BeanUtils.copyProperties(scheduledTaskTemp, scheduledTaskTempForm);
		
		model.addAttribute("scheduledTaskTempForm", scheduledTaskTempForm);
		
		return "/sys/sysapp/scheduledTaskTemp/scheduledTaskTempMod";
		
	}
	
	/**
	 * 修改计划任务模版.
	 * @author weilunkai
	 * @param scheduledTaskTempForm
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/sysapp/scheduledTaskTempUpdate.do")
	public void updateTemplate(ScheduledTaskTempForm scheduledTaskTempForm,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ScheduledTaskTemp scheduledTaskTemp = new ScheduledTaskTemp();
		
		scheduledTaskTemp.setId(scheduledTaskTempForm.getId());
		if(scheduledTaskTempForm.getIsEnabled().equals("5BA7324310EDC6FCA40AE8951D315C3F")){
			scheduledTaskTemp.setIsEnabled("1");
		}else if(scheduledTaskTempForm.getIsEnabled().equals("DEFAC7206708FDC56C01488F6CA8A93B")){
			scheduledTaskTemp.setIsEnabled("0");
		}else{
			scheduledTaskTemp.setIsEnabled("");
		}		
		scheduledTaskTemp.setTempName(scheduledTaskTempForm.getTempName());
		DictEntry dealMeasure = new DictEntry();
		dealMeasure.setId(scheduledTaskTempForm.getDictEntryName());
		scheduledTaskTemp.setDealMeasure(dealMeasure);
		scheduledTaskTemp.setContent(scheduledTaskTempForm.getContents());
		
		PrintWriter out = null;
		try{
			out=response.getWriter();
			o_scheduledTaskTempBO.updateTemplate(scheduledTaskTemp);
			out.write("true");
		}catch (Exception e) {
			out.write("false");
			e.printStackTrace();
		}finally {
			if(null != out){
				out.close();
			}
		}
		
	}
    /**
	 * 删除计划任务模版.
	 * @author weilunkai
	 * @param id 要删除的id集合.
	 * @return String
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
    @RequestMapping(value="/sys/sysapp/scheduledTaskTempDel.do")
	public String removeTemplate(String ids,HttpServletResponse response,HttpServletRequest request) throws Exception{
		PrintWriter out=null;
		String flag="false";
		out=response.getWriter();
		try{
			String[] content=ids.split(",");
			for(String id:content){
				if(StringUtils.isNotBlank(id)){
					o_scheduledTaskTempBO.removeTemplate(id);
				}
			}
			flag="true";
			out.write(flag);
		}catch(Exception e){
			
			e.printStackTrace();
			out.write(flag);
		}finally {
			out.close();
		}
		return null;
	
	}
	/**
	 *  weilunkai  4.20 查看所有 计划任务--模板
	 * @param model
	 * @param request
	 * @param response
	 * @param start
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sys/sysapp/AllscheduledTaskTempList.do")
	public Map<String,Object> queryAllTemplate(Model model,HttpServletRequest request,HttpServletResponse response,int start,int limit){
		Map<String, Object> remap = new HashMap<String, Object>();
		Page<ScheduledTaskTemp> page = new Page<ScheduledTaskTemp>();
		page.setObjectsPerPage(20);
		page.setPageNumber((start + limit) / page.getObjectsPerPage());
		
		String isEnabled=null;
		String tempName=request.getParameter("tempName");
		String dictEntryName=request.getParameter("dictEntryName");
		String enabled=request.getParameter("isEnabled");
		
		//System.out.println("+++++++++"+enabled);
		
		if("5BA7324310EDC6FCA40AE8951D315C3F".equals(enabled)){
			isEnabled="1";
		}else if("DEFAC7206708FDC56C01488F6CA8A93B".equals(enabled)){
			isEnabled="0";
		}
		//System.out.println("++++++++++"+isEnabled);
		
		o_scheduledTaskTempBO.getTemplate(page,tempName,dictEntryName,isEnabled);
		List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
		for(ScheduledTaskTemp st:page.getList()){
			Map<String,Object> rows=new HashMap<String,Object>();		
			rows.put("id", st.getId());
			rows.put("tempName", st.getTempName());
			rows.put("dealMeasure", st.getDealMeasure().getName());
			//rows.put("content",st.getContent().toString());	
			if(st.getIsEnabled().equals("1")){
				rows.put("isEnabled", "是");
			}else{
				rows.put("isEnabled", "否");
			}
			
			
			datas.add(rows);
		}
		
		remap.put("totalCount", page.getFullListSize());
	    remap.put("datas", datas);
		remap.put("success", true);

		return remap;

	}
	
}
