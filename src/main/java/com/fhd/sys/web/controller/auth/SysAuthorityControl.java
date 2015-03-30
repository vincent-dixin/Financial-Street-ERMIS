/**
 * SysAuthorityControl.java
 * com.fhd.fdc.commons.web.controller.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.fhd.core.dao.utils.HibernateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.web.form.auth.SysAuthorityForm;

/**
 * 权限Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types =SysAuthorityForm.class)
public class SysAuthorityControl {
	
	@Autowired
	private SysAuthorityBO o_sysAuthorityBO;
	
	/**
	 * 加载权限树.
	 * @author 吴德福
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/authorityStructure.do")
	public void authorityStructure(HttpServletRequest request) {
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
	}
	/**
	 * 构造根结点的全部树结点.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/loadAuthorityTree.do")
	public List<Map<String, Object>> loadAuthorityTree(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("node");
		return o_sysAuthorityBO.loadAuthorityTree(id,request.getContextPath());
	}
	/**
	 * 构造根结点的静态树json数据.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/loadStaticAuthorityTree.do")
	public void loadStaticAuthorityTree(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		try {
			response.setContentType("text/json; charset=utf-8");
			out = response.getWriter();
			
			String authorityString = "";
			String id = request.getParameter("node");
			if(StringUtils.isNotBlank(id)){
				authorityString = o_sysAuthorityBO.loadStaticAuthorityTree(id);
				request.getSession().setAttribute("authorityString", authorityString);
			}
			out.write(authorityString);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/**
	 * 点击菜单，加载tab页面.
	 * @author 吴德福
	 * @param id 权限id.
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/tabs.do")
	public void showTabs(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
	}


	/**
	 * 进入下级功能页面
	 * @author 万业
	 * @param id
	 * @param success
	 * @param request
	 * @param model
	 */
	@RequestMapping(value = "/sys/auth/authority/query.do")
	public void query(String id,String success,HttpServletRequest request,Model model){
		model.addAttribute("id",id);
		model.addAttribute("success", success);
		//model.addAttribute("authorityList", o_sysAuthorityBO.query(id,HibernateUtils.buildPropertyFilters(request, "filter_")));
	}
	/**
	 * 获取功能列表 分页 查询
	 * @author 万业
	 * @param limit
	 * @param start
	 * @param authForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sys/auth/authority/queryList.do")
	public Map<String, Object> authorList(int limit, int start,HttpServletRequest request, String authorityCode, String authorityName, String parentId){
		Page<SysAuthority> page = new Page<SysAuthority>();
		String id=request.getParameter("id");
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		
		if(StringUtils.isBlank(parentId)){
			
			page=o_sysAuthorityBO.queryAuthPage(page,authorityCode, authorityName, id);
		}else{
			
			page=o_sysAuthorityBO.queryAuthPage(page,authorityCode, authorityName, parentId);
		}
		return this.convertsysAuthValues(page);
	}
	/**
	 * 转换数据
	 * @author 万业
	 * @param page
	 * @return
	 */
	public Map<String, Object> convertsysAuthValues(Page<SysAuthority> page){
		List<SysAuthority> sysAuths=page.getList();
		Iterator<SysAuthority> iterator=sysAuths.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysAuthority sysAuth=iterator.next();
			row.put("id", sysAuth.getId());
			row.put("authCode",sysAuth.getAuthorityCode());
			row.put("authName", sysAuth.getAuthorityName());
			row.put("authSn", sysAuth.getSn());
			row.put("authpname",o_sysAuthorityBO.getGuidName(sysAuth.getSeqNo().split("\\.")));
			
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	/**
	 * get方式修改权限.
	 * @author 吴德福
	 * @param id 菜单id.
	 * @param model
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/edit.do", method = RequestMethod.GET)
	public void g_editAuthority(String id,Model model) throws IllegalAccessException, InvocationTargetException {
		SysAuthorityForm sysAuthorityForm = new SysAuthorityForm();
		SysAuthority sysAuthority = o_sysAuthorityBO.queryAuthorityById(id);
		BeanUtils.copyProperties(sysAuthority, sysAuthorityForm);
		model.addAttribute("sysAuthorityForm",sysAuthorityForm);
	}
	/**
	 * post方式修改权限.
	 * @author 吴德福
	 * @param sysAuthorityForm 权限Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/edit.do", method = RequestMethod.POST)
	public String p_editAuthority(SysAuthorityForm sysAuthorityForm,BindingResult result) throws IllegalAccessException, InvocationTargetException {
		SysAuthority sysAuthority = o_sysAuthorityBO.queryAuthorityById(sysAuthorityForm.getId());
		BeanUtils.copyProperties(sysAuthorityForm, sysAuthority, new String[]{"parentAuthority"});
		sysAuthority.setAuthorityCode(sysAuthorityForm.getAuthorityCode().toUpperCase());
		sysAuthority.setUrl(sysAuthorityForm.getUrl());
		o_sysAuthorityBO.updateAuthority(sysAuthority);
		return "redirect:/sys/auth/authority/edit.do?id=" + sysAuthorityForm.getId();
	}
	/**
	 * get方式添加权限.
	 * @author 吴德福
	 * @param id 权限id.
	 * @param model
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	//@ResponseBody
	@RequestMapping(value = "/sys/auth/authority/add.do", method = RequestMethod.GET)
	public String g_add(String id,Model model, HttpServletRequest request) {
		SysAuthorityForm sysAuthorityForm = new SysAuthorityForm();
		sysAuthorityForm.setParentAuthority(o_sysAuthorityBO.queryAuthorityById(id));
		model.addAttribute(sysAuthorityForm);
		model.addAttribute("operation", request.getParameter("operation"));
		return "sys/auth/authority/add-model";
	}
	/**
	 * post方式添加权限.
	 * @author 吴德福
	 * @param sysAuthorityForm 权限Form.
	 * @param result
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/add.do", method = RequestMethod.POST)
	public void p_add(SysAuthorityForm sysAuthorityForm,HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try{
			SysAuthority sysAuthority = new SysAuthority();
			BeanUtils.copyProperties(sysAuthorityForm, sysAuthority);
			sysAuthority.setId(Identities.uuid());
			sysAuthority.setIsLeaf(true);
			sysAuthority.setEtype(sysAuthorityForm.getEtype().toUpperCase());
			sysAuthority.setIcon(sysAuthorityForm.getIcon());
			sysAuthority.setAuthorityCode(sysAuthorityForm.getAuthorityCode().toUpperCase());
			sysAuthority.setParentAuthority(o_sysAuthorityBO.queryAuthorityById(sysAuthorityForm.getParentId()));
		
			sysAuthority.setIsLeaf(true);
			sysAuthority.setUrl(sysAuthorityForm.getUrl());
			if (sysAuthority.getParentAuthority().getIsLeaf()) {
				sysAuthority.getParentAuthority().setIsLeaf(false);
			}
			o_sysAuthorityBO.saveAuthority(sysAuthority);
			flag = "true";
			out.write(flag);
		}catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
		
		
		//return "redirect:/sys/auth/authority/query.do?id=" + sysAuthorityForm.getParentAuthority().getId() + "&success=1";
		
		
		
		
	}
	/**
	 * get方式更新权限.
	 * @author 吴德福
	 * @param id
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/update.do", method = RequestMethod.GET)
	public String g_update(String id,Model model) {
		SysAuthorityForm sysAuthorityForm = new SysAuthorityForm();
		SysAuthority sysAuthority = o_sysAuthorityBO.queryAuthorityById(id);
		BeanUtils.copyProperties(sysAuthority, sysAuthorityForm);
		sysAuthorityForm.setParentId(sysAuthority.getParentAuthority().getId());
		model.addAttribute(sysAuthorityForm);
		return "sys/auth/authority/update-model";
	}
	/**
	 * post方式更新权限.
	 * @author 吴德福
	 * @param menuForm
	 * @param result
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/update.do", method = RequestMethod.POST)
	public void p_update(SysAuthorityForm sysAuthorityForm, HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			SysAuthority sysAuthority = new SysAuthority();
			BeanUtils.copyProperties(sysAuthorityForm, sysAuthority);
			sysAuthority.setAuthorityCode(sysAuthorityForm.getAuthorityCode().toUpperCase());
			sysAuthority.setParentAuthority(o_sysAuthorityBO.queryAuthorityById(sysAuthorityForm.getParentId()));
			sysAuthority.setUrl(sysAuthorityForm.getUrl());
			//获得当前未修改的权限的父权限
			SysAuthority parAuthority = o_sysAuthorityBO.queryAuthorityById(sysAuthorityForm.getParentAuthority().getId());
			//查询该父权限下是否存在权限
			List<SysAuthority> authorityList = new ArrayList<SysAuthority>();
			authorityList = o_sysAuthorityBO.query(parAuthority.getId(),HibernateUtils.buildPropertyFilters(request, "filter_"));
			//不存在则将该节点置为叶子结点
			if (!(authorityList.size()>2 && authorityList!=null)) {
				parAuthority.setIsLeaf(true);
			}
			SysAuthority a = sysAuthority.getParentAuthority();
			if (a.getIsLeaf()) {
				sysAuthority.getParentAuthority().setIsLeaf(false);
			}
			if (!sysAuthority.getParentAuthority().getId().equals(sysAuthority.getId())) {
				o_sysAuthorityBO.updateAuthority(sysAuthority);
				//o_sysAuthorityBO.updateAuthority(parAuthority);
				flag="true";
			}else{
				flag="上级权限设置错误";
			}
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	/**
	 * post方式删除权限.
	 * @author 吴德福
	 * @param id 权限id集合.
	 * @param parentid 父id.
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
/*	@RequestMapping(value = "/sys/auth/authority/delete.do", method = RequestMethod.POST)
	public String p_delete(String[] id,String parentid,HttpServletRequest request){
		parentid = id[0];
		for(int i=1;i<id.length;i++){
			//权限下有子权限存在，不允许删除
			List<SysAuthority> authorityList = new ArrayList<SysAuthority>();
			authorityList = o_sysAuthorityBO.query(id[i],HibernateUtils.buildPropertyFilters(request, "filter_"));
			if(null != authorityList && authorityList.size()>0){
				return "redirect:/sys/auth/authority/query.do?id=" + parentid + "&success=0";
			}
			
			o_sysAuthorityBO.removeAuthority(id[i]);
		}
		return "redirect:/sys/auth/authority/query.do?id=" + parentid + "&success=1";
	}*/
	
	/**
	 * post方式删除权限.
	 * @author 吴德福
	 * @param ids 权限id集合.
	 * @param parentid 父id.
	 * @return boolean 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/authority/delete.do")
	public boolean p_delete(String ids,HttpServletRequest request){
		//parentid = id[0];
		String[] authids=ids.split(",");
		SysAuthority parAuthority = null;
		for(int i=0;i<authids.length;i++){
			if(StringUtils.isNotBlank(authids[i])){
				
				//权限下有子权限存在，不允许删除
				List<SysAuthority> authorityList = new ArrayList<SysAuthority>();
				parAuthority = o_sysAuthorityBO.queryAuthorityById(authids[i]).getParentAuthority();
				authorityList = o_sysAuthorityBO.query(authids[i],HibernateUtils.buildPropertyFilters(request, "filter_"));
				if(null != authorityList && authorityList.size()>0){
					return false;
				}
				o_sysAuthorityBO.removeAuthority(authids[i]);
				List<SysAuthority> authorities = o_sysAuthorityBO.query(parAuthority.getId(), HibernateUtils.buildPropertyFilters(request, "filter_"));
				
				if (!(authorities.size()>0&&authorities!=null)) {
					parAuthority.setIsLeaf(true);
				}
				o_sysAuthorityBO.updateAuthority(parAuthority);
			}
			
		}
		return true;
	}
	
	/**
	 * get方式删除权限.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/delete.do", method = RequestMethod.GET)
	public void g_delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		String aid = request.getParameter("id");
		try {
			//权限下有子权限存在，不允许删除
			List<SysAuthority> sysAuthoritys = new ArrayList<SysAuthority>();
			sysAuthoritys = o_sysAuthorityBO.query(aid,HibernateUtils.buildPropertyFilters(request, "filter_"));
			if(null != sysAuthoritys && sysAuthoritys.size()>0){
				out.write(",权限下有子权限存在,不允许删除");
				return;
			}
			SysAuthority parAuthority = o_sysAuthorityBO.queryAuthorityById(aid).getParentAuthority();
			o_sysAuthorityBO.removeAuthority(aid);	
			List<SysAuthority> authorities = o_sysAuthorityBO.query(parAuthority.getId(), HibernateUtils.buildPropertyFilters(request, "filter_"));
			
			if (!(authorities.size()>0&&authorities!=null)) {
				parAuthority.setIsLeaf(true);
			}
			o_sysAuthorityBO.updateAuthority(parAuthority);
//			SysAuthority parSysAuthority = o_sysAuthorityBO.queryAuthorityById(aid).getParentAuthority();
//			String id = parSysAuthority.getId();
//			List<SysAuthority> pa = o_sysAuthorityBO.query(id,HibernateUtils.buildPropertyFilters(request, "filter_"));

//			//判断被删除的节点的父节点下是否存在节点，不存在则设置为叶子节点
//			sysAuthoritys = o_sysAuthorityBO.query(id,HibernateUtils.buildPropertyFilters(request, "filter_"));
//			if (!(null!=sysAuthoritys && sysAuthoritys.size()>0)) {
//				pa.get(0).getParentAuthority().setIsLeaf(true);
//			}
//			o_sysAuthorityBO.updateAuthority(pa.get(0).getParentAuthority());
			
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	/**
	 * 移动node弹出页面.
	 * @author 吴德福
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/choose.do")
	public String choose(Model model){
		return "sys/auth/authority/choose";
	}
	/**
	 * Ext拖拽移动node.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/move.do")
	public void moveNode(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		
		String flag = "false";
        try {
            String currentId = request.getParameter("currentId");
            String pid = request.getParameter("pid");
            String targetId = request.getParameter("targetId");
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            
			if (o_sysAuthorityBO.moveNode(currentId,pid,targetId)) {
				flag = "true";
				out.write(flag);
			}else{
				out.write(flag);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        	out.write(flag);
        } finally {
            out.close();
        }
	}
	/**
	 * 根据查询条件查询权限.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String 跳转到authorityList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/authority/authorityByCondition.do")
	public String queryAuthorityByCondition(Model model,HttpServletRequest request,SysAuthorityForm sysAuthorityForm,BindingResult result){
		model.addAttribute("authorityList", o_sysAuthorityBO.query(sysAuthorityForm));
		return "sys/auth/authority/query";
	}
	/**
	 * 查询所有的功能.
	 * @author 吴德福
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/auth/functionList.do",method=RequestMethod.GET)
	public String queryAllfunction(Model model){
		model.addAttribute("sysAuthorityForm", new SysAuthorityForm());
		return "sys/menu/queryAuthoritySelect";
	}
	/**
	 * 根据查询条件查询所有的作用于菜单的权限.
	 * @author 吴德福
	 * @param limit
	 * @param start
	 * @param authorityCode
	 * @param authorityName
	 * @return Map<String, Object>
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/functionList.do",method=RequestMethod.POST)
	public Map<String, Object> queryAuthorityList(int limit, int start, String authorityCode,String authorityName) {
		Page<SysAuthority> page = new Page<SysAuthority>();
		page.setPageNumber((limit == 0 ? 0 : start / limit) + 1);
		page.setObjectsPerPage(limit);
		page = o_sysAuthorityBO.queryAuthorityList(page,authorityCode,authorityName);
		List<SysAuthority> sysAuthorityList = page.getList();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (SysAuthority sysAuthority : sysAuthorityList) {
			Map<String, Object> item = new HashMap<String, Object>();
			// 组装JSON对象
			item.put("id", sysAuthority.getId());
			item.put("authorityCode", sysAuthority.getAuthorityCode());
			item.put("authorityName", sysAuthority.getAuthorityName());
			item.put("parentAuthority", sysAuthority.getParentAuthority().getAuthorityName());
				
			datas.add(item);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("datas", datas);
		result.put("totalCount", page.getFullListSize());
		return result;
	}
	/**
	 * 根据查询条件查询功能.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/functionByCondition.do")
	public String queryFunctionByCondition(Model model,HttpServletRequest request,SysAuthorityForm sysAuthorityForm,BindingResult result){
		model.addAttribute("authorityList", o_sysAuthorityBO.query(sysAuthorityForm));
		return "sys/menu/queryAuthority";
	}
}

