/**
 * PositionControl.java
 * com.fhd.fdc.commons.web.controller.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-9 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
 */

package com.fhd.sys.web.controller.orgstructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.business.orgstructure.PositionBO;
import com.fhd.sys.business.orgstructure.SysEmpPosiBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.web.form.orgstructure.PosiForm;

/**
 * 岗位Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-10
 * Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types =PosiForm.class)
public class PositionControl {

	@Autowired
	private PositionBO o_positionBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private SysAuthorityBO o_sysAuthorityBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private SysEmpPosiBO o_sysEmpPosiBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * 构造岗位的全部树结点.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>> 岗位的树结点集合.
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/orgstructure/posi/loadPosiTree.do")
	public List<Map<String, Object>> loadPosiTree(HttpServletRequest request,
			HttpServletResponse response) {
		String node = request.getParameter("node");
		String id = StringUtils.substring(request.getParameter("node"), 0, node.length()-4);//减掉四位随机数
		String empfilter=request.getParameter("empfilter");
		return o_positionBO.loadPosiTree(id,request.getContextPath(),empfilter);
	}
	/**
	 * get方式修改岗位.
	 * @author 吴德福
	 * @param id 岗位id.
	 * @param model
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/edit.do", method = RequestMethod.GET)
	public void g_editPosi(String id,Model model) throws IllegalAccessException, InvocationTargetException {
		PosiForm posiForm = new PosiForm();
		BeanUtils.copyProperties(o_positionBO.get(id),posiForm);
		model.addAttribute(posiForm);
	}
	/**
	 * post方式修改岗位.
	 * @author 吴德福
	 * @param posiForm 岗位Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/edit.do", method = RequestMethod.POST)
	public void p_editPosi(PosiForm posiForm, BindingResult result, HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		SysPosition position = new SysPosition();
		BeanUtils.copyProperties(posiForm, position);
		position.setPosicode(posiForm.getPosicode().toUpperCase());
		
		try{
			o_positionBO.merge(position);
			flag = "true";
			out.write(flag);
		}catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		}finally {
			out.close();
		}
	}
	/**
	 * 点击岗位，加载tab页面.
	 * @author 胡迪新
	 * @param id
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/posi/tabs.do")
	public void showTabs(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
	}
	/**
	 * 查询所有的岗位.
	 * @author 胡迪新
	 * @param id 所属机构id
	 * @param model
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/posi/query.do")
	public String query(String id,String success,Model model,HttpServletRequest request){
		if("1".equals(success)){
			request.setAttribute("success", "操作成功！");
		}
		model.addAttribute("id", id);
		return "/sys/orgstructure/posi/query";
	}
	/**
	 * 查询分页ext 所有岗位
	 * @author 万业
	 * @param id
	 * @param limit
	 * @param start
	 * @param posicode
	 * @param posiname
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/sys/orgstructure/posi/queryNextPosiList.do")
	public Map<String, Object> queryNextPosi(String id, int limit, int start, String posicode, String posiname)throws Exception {
		SysPosition sysPosi=new SysPosition();
		if(StringUtils.isNotBlank(posicode)){
			sysPosi.setPosicode(posicode);
		}
		if(StringUtils.isNotBlank(posiname)){
			sysPosi.setPosiname(posiname);
		}
		Page<SysPosition> page = new Page<SysPosition>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		page=o_positionBO.queryPosiByOrgIdPage(page, id, sysPosi);
		return convertsysPosiValues(page);
		
	}
	public Map<String, Object>convertsysPosiValues(Page<SysPosition> page){
		
		List<SysPosition> sysEmps=page.getList();
		Iterator<SysPosition> iterator=sysEmps.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysPosition sysPosi=iterator.next();
			row.put("id", sysPosi.getId());
			row.put("posicode", sysPosi.getPosicode());
			row.put("posiname", sysPosi.getPosiname());
			row.put("sysOrganization", sysPosi.getSysOrganization().getOrgname());
			row.put("startDate", sysPosi.getStartDate());
			row.put("endDate", sysPosi.getEndDate());
			String posiStatus= sysPosi.getPosiStatus();
			row.put("posiStatus", StringUtils.isNotBlank(posiStatus)?posiStatus:"");
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
		
		
		
	}
	
	/**
	 * get方式添加岗位.
	 * @author 胡迪新
	 * @param id
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/add.do", method = RequestMethod.GET)
	public String g_add(String id,Model model,HttpServletRequest request){
		PosiForm posiForm = new PosiForm();
		posiForm.setSysOrganization(o_organizationBO.get(id));
		model.addAttribute(posiForm);
		model.addAttribute("operation", request.getParameter("operation"));
		return "sys/orgstructure/posi/add-model";
	}
	/**
	 * post方式添加岗位.
	 * @author 胡迪新
	 * @param posiForm
	 * @param bindingResult
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/posi/add.do", method = RequestMethod.POST)
	public String p_add(PosiForm posiForm,BindingResult result, HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		SysPosition position = new SysPosition();
		BeanUtils.copyProperties(posiForm, position);
		String ps_id  =  posiForm.getOrgId();
		if (ps_id.length()-5>0) {
			ps_id=StringUtils.substring(ps_id, 0,32);
		}else {
			ps_id=StringUtils.substring(ps_id, 0,ps_id.length()-4);
		}
		position.setSysOrganization(o_organizationBO.get(ps_id));
		position.setId(Identities.uuid());
		position.setPosicode(posiForm.getPosicode().toUpperCase());
		
		
		try{
			o_positionBO.merge(position);
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
		return null;
	}
	/**
	 * get方式修改岗位.
	 * @author 吴德福
	 * @param id 岗位id.
	 * @param model
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/update.do", method = RequestMethod.GET)
	public String g_update(String id,Model model) {
		PosiForm posiForm = new PosiForm();
		BeanUtils.copyProperties(o_positionBO.get(id),posiForm);
		model.addAttribute(posiForm);
		return "sys/orgstructure/posi/update-model";
	}
	/**
	 * post方式修改岗位.
	 * @author 吴德福
	 * @param posiForm 岗位Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/update.do", method = RequestMethod.POST)
	public String p_update(PosiForm posiForm,BindingResult result, HttpServletResponse response, HttpServletRequest request)throws Exception{
		//bindingresult 填表转码问题，如果没有则出现。。。……&%
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		SysPosition position = new SysPosition();
		BeanUtils.copyProperties(posiForm, position);
		position.setPosicode(posiForm.getPosicode().toUpperCase());
		
		
		try{
			o_positionBO.merge(position);
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
		return null;
	
	}
	/**
	 * post方式删除岗位.
	 * 只有一个能删除就算成功
	 * @author 吴德福
	 * @param id 岗位id集合.
	 * @param parentid 父id.
	 * @return String 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/delete.do", method = RequestMethod.POST)
	public String p_delete(String id,String orgid,HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		String[] ids=id.split(",");
		
		for(int i=0;i<ids.length;i++){
			if(StringUtils.isNotBlank(ids[i])){
				
				List<SysEmpPosi> resultList = new ArrayList<SysEmpPosi>();
				resultList = o_sysEmpPosiBO.querySysEmpPosiByPosiId(ids[i]);
				if(null != resultList && resultList.size()>0){
					//岗位下有员工存在，不允许删除
					break;
				}else{
					flag="true";
					o_positionBO.delete(ids[i]);
				}
			}
		}
		out.write(flag);
		return null;
	}
	/**
	 * get方式删除岗位.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/delete.do", method = RequestMethod.GET)
	public void g_delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try {
			String pid = request.getParameter("id");
			List<SysEmpPosi> resultList = new ArrayList<SysEmpPosi>();
			resultList = o_sysEmpPosiBO.querySysEmpPosiByPosiId(pid);
			if(null != resultList && resultList.size()>0){
				//岗位下有员工存在，不允许删除
				out.write(flag);
			}else{
				o_positionBO.delete(pid);
				flag = "true";
				out.write(flag);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	/**
	 * 给岗位分配权限时保存岗位Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posiAssignAuthority.do")
	public String posiAssignAuthority(Model model,HttpServletRequest request){
		
		String posiId = request.getParameter("id");		
		PosiForm posiForm = new PosiForm();
		if (posiId != null) {
			SysPosition position = o_positionBO.get(posiId);
			BeanUtils.copyProperties(position,posiForm);
		}
		//进入角色时显示权限列表
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
		model.addAttribute("posiForm", posiForm);
		
		return "sys/orgstructure/posi/posiAssignAuthority";
	}
	/**
	 * ajax取得当前岗位拥有的权限.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/queryAuthorityByPosiAjax.do")
	public void queryAuthorityAjax(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String posiid = request.getParameter("pid");
		PrintWriter out = null;
		out = response.getWriter();
		
		try {
			List<String> authorityIds = o_positionBO.queryAuthorityByPosi(posiid);
			String aid = "";
			int size = authorityIds.size();
			for(int i=0;i<authorityIds.size();i++){
				aid += authorityIds.get(i);
				if(i != size){
					aid += ",";
				}
			}
			out.write(aid);
		} catch (Exception e) {
			e.printStackTrace();
			out.write("NO");
		}finally{
			out.close();
		}
	}
	/**
	 * 给岗位分配权限.
	 * @author 吴德福
	 * @param status
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/posi/posiAssignAuthoritySubmit.do")
	public void posiAssignAuthoritySubmit(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String posiid = request.getParameter("posiid");
			String selectIds = request.getParameter("selectIds");
			String[] authorityIds = selectIds.substring(0,selectIds.length()-1).split(",");
			SysPosition position = o_positionBO.get(posiid);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>(); 
			for(String authorityId : authorityIds) {
				SysAuthority authority = new SysAuthority();
				//authority.setId(authorityId);
				authority = o_sysAuthorityBO.queryAuthorityById(authorityId);//设置级联操作时使用
				authorities.add(authority);
			}
			position.setSysAuthorities(authorities);
			o_positionBO.merge(position);
			status.setComplete();
			flag="true";
			out.write(flag);
		} catch (DataIntegrityViolationException e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	
	
	/**
	 * 转向角色列表页面
	 * @author  万业
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sys/orgstructure/posiAssignRole.do")
	public String posiAssignRole(Model model,HttpServletRequest request){
		String posiId = request.getParameter("id");
		PosiForm posiForm = new PosiForm();
		
		if (posiId != null) {
			SysPosition position = o_positionBO.get(posiId);
			BeanUtils.copyProperties(position,posiForm);
			String roleIds = "";
			for (SysRole role : position.getSysRoles()) {
				roleIds = roleIds + role.getId()+",";
			}
			posiForm.setRoleIds(roleIds);
		}
		model.addAttribute("posiForm", posiForm);
		
		return "sys/orgstructure/posi/posiAssignRole";
	}
	/**
	 * 给岗位分配角色列表，分页
	 * @author 万业
	 * @param limit
	 * @param start
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sys/orgstructure/posi/posiAssignRoleList.do")
	public Map<String, Object>  posiAssignRoleList(int limit, int start,String roleName,String roleCode,HttpServletRequest request){
		//String posiId = request.getParameter("id");
		
		//PosiForm posiForm = new PosiForm();
		
		//List<SysRole> roleList = o_sysRoleBO.queryAllRole();

		Page<SysRole> page = new Page<SysRole>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		SysRole role = new SysRole();
		role.setRoleCode(roleCode);
		role.setRoleName(roleName);
		page = o_sysRoleBO.querySysRoleByPage(page,role,"id","ASC");
		
		List<SysRole> sysRoles=page.getList();
		
		Iterator<SysRole> iterator=sysRoles.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysRole sysRole=iterator.next();
			row.put("id", sysRole.getId());
			row.put("roleName", sysRole.getRoleName());
						
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
		
		
	}
	/**
	 * 给岗位分配角色.
	 * @author 吴德福
	 * @param status
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/posi/posiAssignRoleSubmit.do")
	public void posiAssignRoleSubmit(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String posiid = request.getParameter("posiid");
			String selectIds = request.getParameter("selectIds");
			String[] roleIds = selectIds.substring(0,selectIds.length()-1).split(",");
			SysPosition position = o_positionBO.get(posiid);
			Set<SysRole> sysRoles= new HashSet<SysRole>(); 
			for(String roleId : roleIds) {
				SysRole sysRole = new SysRole();
				//sysRole.setId(roleId);
				sysRole = o_sysRoleBO.queryRoleById(roleId);//设置级联操作时使用
				sysRoles.add(sysRole);
			}
			position.setSysRoles(sysRoles);
			o_positionBO.merge(position);
			status.setComplete();
			flag="true";
			out.write(flag);
		} catch (DataIntegrityViolationException e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	
	/**
	 * 根据查询内容得到岗位树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @throws IOException
	 * @since fhd Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/positon/getPathsBySearchName.do")
	public String getPathsBySearchName(String searchName,HttpServletRequest request,HttpServletResponse response) throws IOException {
 		 List<String>  pathList=o_positionBO.getPathsBySearchName(searchName);
 		 StringBuilder sbd=new StringBuilder();
 		 for(String path:pathList){
 			 sbd.append(",").append(path);
 		 }
 		 if(sbd.length()==0){
 			return "1";
 		 }
		 return sbd.toString().substring(1);
	}
}
