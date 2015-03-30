/**   
* @Title: ClipingControl.java 
* @Package com.fhd.fdc.commons.web.controller.sys.cliping 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 张雷 
* @date 2011-3-22 下午03:22:13 
* @version v1.0 
*/ 
package com.fhd.sys.web.controller.cliping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.cliping.ClipingBO;
import com.fhd.sys.entity.cliping.Cliping;
import com.fhd.sys.web.form.cliping.ClipingForm;

/** 
 * @ClassName: ClipingControl 
 * @Description: 列表和功能Control类
 * @author 张雷
 * @date 2011-3-22 下午03:22:13 
 *  
 */
@Controller
@SessionAttributes(types = ClipingForm.class)
public class ClipingControl {
	/** 
	* @Fields o_clipingBO : 列表和功能BO类
	*/ 
	@Autowired 
	private ClipingBO o_clipingBO;
	
	/**
	 * 查询顶部tab信息
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/cliping/findClipingTab.f")
	public List findClipingTab(){
		return o_clipingBO.findClipingTab();
	}
	
	/**
	 * 
	 * <pre>
	 * 根据顶部tab信息查询功能信息
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param category 顶部tab信息
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/cliping/findClipingByCategory.f")
	public List findClipingByCategory(String category)
	{
	    
	    List l =  o_clipingBO.findClipingByCategory(category);
	    return l;
	}
	
	/**
	 * 
	 * <pre>
	 * 批量更新
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param saveStr 保存信息字符串，格式为id:true,id:false,
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/sys/cliping/saveCategory.f")
	public String saveCliping(String saveStr)
	{
	    String [] str=saveStr.split(",");
	    for(String s:str)
	    {
		if(StringUtils.isNotBlank(s))
		{
		String[] tmp=s.split(":");
		String status="0";
		if(tmp[1].equals("true"))
		{
		    status="1";
		}
		o_clipingBO.saveCliping(tmp[0], status);
		}
	    }
	    return "ok";
	}
	
	
}
