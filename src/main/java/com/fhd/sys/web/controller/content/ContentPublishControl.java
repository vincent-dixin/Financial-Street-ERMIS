/**
 * ContentPublishControl.java
 * com.fhd.fdc.commons.web.controller.sys.content
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-15 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.content;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.content.ContentPublishBO;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.content.ContentPublish;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.web.form.content.ContentPublishForm;

/**
 * 内容发布Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-15
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = ContentPublishForm.class)
public class ContentPublishControl {
	
	@Autowired
	private ContentPublishBO o_contentPublishBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * 查询所有的内容发布.
	 * @author 吴德福
	 * @param model
	 * @param success
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
/*	@RequestMapping(value = "/sys/content/contentPublishList.do")
	public String queryAllContent(Model model,String success){
		List<ContentPublish> contentPublishList = new ArrayList<ContentPublish>();
		SysEmployee employee = o_employeeBO.getEmployee(UserContext.getUserid());
		if(null != employee){
			SysOrganization org = employee.getSysOrganization();
			contentPublishList = o_contentPublishBO.getAllContentByUser(org.getId());
		}else{
			contentPublishList = o_contentPublishBO.getAllContent();
		}
		
		model.addAttribute("contentPublishList", contentPublishList);
		model.addAttribute("contentPublishForm", new ContentPublishForm());
		model.addAttribute("success", success);
		return "sys/content/contentPublishList";
	}*/
	/**
	 * 新增时保存内容发布Form.
	 * @author 吴德福
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
/*	@RequestMapping(value = "/sys/content/contentPublishAdd.do")
	public void contentAdd(Model model){
		model.addAttribute("contentPublishForm", new ContentPublishForm());
		
	}*/
	/**
	 * 王龙 4.21 
	 */
	
    @RequestMapping(value="/sys/content/contentPublishAdd.do")
    public String contentAdd(Model model){
    	
    	model.addAttribute("contentPublishForm",new ContentPublishForm());
    	return "/sys/content/contentPublishAdd";
    }
	
	/**
	 * 新增内容发布.
	 * @author 吴德福
	 * @param contentPublishForm
	 * @param request
	 * @return String
	 * @throws Exception 
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
    @ResponseBody
     @RequestMapping(value = "/sys/content/contentPublishSave.do")
	public String saveContent(Model model,ContentPublishForm contentPublishForm,HttpServletRequest request) throws Exception{
		ContentPublish contentPublish = new ContentPublish();
		BeanUtils.copyProperties(contentPublishForm,contentPublish);
		contentPublish.setId(Identities.uuid());
		contentPublish.setAddTime(new Date());
		OperatorDetails user = UserContext.getUser();
		SysUser sysUser = new SysUser();
		sysUser.setId(user.getUserid());
		contentPublish.setSysUser(sysUser);
		SysEmployee employee = o_employeeBO.getEmployee(sysUser.getId());
		if(null != employee){
			contentPublish.setSysOrganization(employee.getSysOrganization());
		}
		String resultStr="";
		try{
			o_contentPublishBO.saveContent(contentPublish);
			resultStr="true";
		}catch (Exception e) {
			resultStr="false";
		}
		return resultStr;
		
	}  
   
    /*@RequestMapping(value="/sys/content/contentPublishMod.do")
    public void contentMod(ContentPublishForm contentPublishForm,HttpServletRequest request,HttpServletResponse response) throws Exception{
	    PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		ContentPublish contentPublish = new ContentPublish();
		BeanUtils.copyProperties(contentPublishForm, contentPublish);
		o_contentPublishBO.updateContent(contentPublish);	
		
		try{
			o_contentPublishBO.saveContent(contentPublish);
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
    }*/



	/**
	 * 修改时保存内容发布Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/content/contentPublishMod.do",  method = RequestMethod.GET)
	public String contentMod(Model model,HttpServletRequest request){
		String id = request.getParameter("id");
		ContentPublishForm contentPublishForm = new ContentPublishForm();
		BeanUtils.copyProperties(o_contentPublishBO.getContentById(id), contentPublishForm);
		model.addAttribute("contentPublishForm", contentPublishForm);
		return "sys/content/contentPublishMod";
	}
	/**
	 * 修改内容发布.
	 * @author 吴德福
	 * @param contentPublishForm
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/content/contentPublishUpdate.do")
	public String updateContent(ContentPublishForm contentPublishForm){
		ContentPublish contentPublish = new ContentPublish();
		BeanUtils.copyProperties(contentPublishForm, contentPublish);
		String resultStr="";
		try{
			
			o_contentPublishBO.updateContent(contentPublish);
			resultStr="true";
		}catch (Exception e) {
			resultStr="false";
		}
		return resultStr;
		
	}
  
	
	
	
	/**
	 * 删除内容发布.
	 * @author 吴德福
	 * @param id 要删除的id集合.
	 * @return String
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
/*	@RequestMapping(value = "/sys/content/contentPublishDel.do")
	    public String removeContent(String[] id){
		boolean flag = false;
		for(String contentid : id){
			if(!flag){
				o_contentPublishBO.removeContent(contentid);
			}			
		}
		return "redirect:/sys/content/contentPublishList.do?success=" + (flag ? 0 : 1);
	}*/
	
	@RequestMapping(value="/sys/content/contentPublishDel.do")
	public String removeContent(String ids,HttpServletResponse response,HttpServletRequest request) throws Exception{
		PrintWriter out=null;
		String flag="false";
		out=response.getWriter();
		try{
			String[] content=ids.split(",");
			for(String id:content){
				if(StringUtils.isNotBlank(id)){
					o_contentPublishBO.removeContent(id);
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
	 * 根据查询条件查询内容.
	 * @author 吴德福
	 * @param model
	 * @param contentPublishForm
	 * @param result
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/content/contentPublishByCondition.do")
	public String queryContentPublishByCondition(Model model,ContentPublishForm contentPublishForm,BindingResult result){
		List<ContentPublish> contentPublishList = o_contentPublishBO.queryContentPublishByConditions(contentPublishForm);
		model.addAttribute("contentPublishList", contentPublishList);
		return "sys/content/contentPublishList";
	}
	
	/**
	 * 内容发布列表.
	 * @author 王龙 
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/content/contentPublishList.do")
	public String queryContent(Model model){
		model.addAttribute("contentPublishForm",new ContentPublishForm());
		return "sys/content/contentPublishList";
	}
	
	/**
	 *   王龙  4.20 查看内容  
	 * @param model
	 * @param request
	 * @param response
	 * @param start
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sys/content/AllcontentPublishList.do")
	public Map<String,Object> queryAllContent(Model model,HttpServletRequest request,HttpServletResponse response,int start,int limit){
		Map<String, Object> remap = new HashMap<String, Object>();
		Page<ContentPublish> page = new Page<ContentPublish>();
		page.setObjectsPerPage(20);
		page.setPageNumber((start + limit) / page.getObjectsPerPage());
		
		String title=request.getParameter("title");
		String username=request.getParameter("username");
		String  contentType=request.getParameter("contentType");
		String isDeploy =request.getParameter("isDeploy");
		
		o_contentPublishBO.getContent(page,title,username,contentType,isDeploy);
		List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
		for(ContentPublish cp:page.getList()){
			Map<String,Object> rows=new HashMap<String,Object>();		
			rows.put("id", cp.getId());
			rows.put("title", cp.getTitle());
			//rows.put("contents", cp.getContents());
			rows.put("contents", o_dictEntryBO.getDictEntrybyId(cp.getContentType()).getName());
			rows.put("isDeploy", o_dictEntryBO.getDictEntrybyId(cp.getIsDeploy()).getName());
			rows.put("username", cp.getSysUser().getRealname());
			rows.put("orgname", cp.getSysOrganization().getOrgname());	
			rows.put("addTime", DateUtils.formatDate(cp.getAddTime(),"yyyy年MM月dd日"));
			datas.add(rows);
		}
	
		
		remap.put("totalCount", page.getFullListSize());
	    remap.put("datas", datas);
		remap.put("success", true);

		return remap;

	}
	
}
