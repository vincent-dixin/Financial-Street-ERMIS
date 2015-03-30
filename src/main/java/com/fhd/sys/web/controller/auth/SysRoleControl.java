/**
 * SysRoleControl.java
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.ObjectUtil;
import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.auth.SysRoleTreeBO;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.web.form.auth.SysRoleForm;

/**
 * 角色Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types =SysRoleForm.class)
public class SysRoleControl {
	
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private SysAuthorityBO o_sysAuthorityBO;
	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private SysRoleTreeBO o_sysRoleTreeBO;
	
	/**
	 * 进入角色页面
	 * @return
	 */
	@RequestMapping(value="/sys/auth/roleList.do")
	public String queryAllRole(){
		return "sys/auth/role/roleList";
	}
	/**
	 * 查询所有的角色.分页 条件
	 * @author 万业
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value="/sys/auth/role/roleList.do")
	public Map<String,Object> queryAllRole(Model model,int start,int limit,String sort,String dir,String roleName,String roleCode){
		Map<String,Object> reMap = new HashMap<String,Object>();
		Page<SysRole> page = new Page<SysRole>();
		page.setPageNumber((limit == 0 ? 0 : start / limit) + 1);
		page.setObjectsPerPage(limit);
		SysRole sysRole = new SysRole();
		sysRole.setRoleCode(roleCode);
		sysRole.setRoleName(roleName);
		o_sysRoleBO.querySysRoleByPage(page, sysRole,sort,dir);
		
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		for (SysRole role : page.getList()) {
			Map<String, String> record = new HashMap<String, String>();
			ObjectUtil.ObjectPropertyToMap(record, role);
			datas.add(record);
		}
		reMap.put("totalCount", page.getFullListSize());
		reMap.put("datas", datas);
		return reMap;
		
	}
	
	
	/**
	 * 根据查询条件查询角色.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String 跳转到roleList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleByCondition.do")
	public String queryRoleByCondition(Model model,HttpServletRequest request,SysRoleForm sysRoleForm){
		String roleCode = request.getParameter("roleCode");
		String roleName = request.getParameter("roleName");
		List<SysRole> roleList = o_sysRoleBO.query(roleCode,roleName);
		model.addAttribute("roleList", roleList);
		//进入角色时显示右面的权限列表
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
		return "sys/auth/role/roleList";
	}
	
	/**
	 * 新增时保存角色Form.
	 * @author 吴德福
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleAdd.do")
	public String addRole(Model model){
		model.addAttribute("sysRoleForm", new SysRoleForm());
		return "sys/auth/role/roleAdd";
	}
	
	/**
	 * 保存角色.
	 * @author 吴德福
	 * @param sysRoleForm 权限Form.
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleSave.do", method = RequestMethod.POST)
	public void saveRole(SysRoleForm sysRoleForm,Model model,HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		//根据角色编号判断角色是否存在.
		List<SysRole> sysRoleList1 = o_sysRoleBO.isExistByRoleCode(sysRoleForm.getRoleCode());
		if(null != sysRoleList1 && sysRoleList1.size()>0){
			out.write("角色编号已存在，请修改!");
			return;
		}
		//根据角色名称判断角色是否存在.
		List<SysRole> sysRoleList2 =o_sysRoleBO.isExistByRoleName(sysRoleForm.getRoleName());
		if(null != sysRoleList2 && sysRoleList2.size()>0){
			out.write("角色名称已存在，请修改!");
			return;
		}
		
		SysRole sysRole = new SysRole();
		BeanUtils.copyProperties(sysRoleForm, sysRole);
		sysRole.setRoleCode(sysRoleForm.getRoleCode().toUpperCase());
		sysRole.setId(Identities.uuid());
		
		try{
			o_sysRoleBO.saveRole(sysRole);
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
	}
	
	/**
	 * 修改时保存角色Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleMod.do")
	public String modRole(Model model,HttpServletRequest request){
		String id = request.getParameter("id");
		
		List<SysRole> roleList = o_sysRoleBO.queryAllRole();
		Map<String,String> parentMap = new HashMap<String,String>();
		for(SysRole sysRole : roleList){
			parentMap.put(sysRole.getId(), sysRole.getRoleName());
		}
		
		SysRoleForm sysRoleForm = new SysRoleForm();
		
		sysRoleForm.setParentMap(parentMap);
		BeanUtils.copyProperties(o_sysRoleBO.queryRoleById(id), sysRoleForm);
		model.addAttribute("sysRoleForm", sysRoleForm);
		return "sys/auth/role/roleMod";
	}
	
	/**
	 * 修改角色.
	 * @author 吴德福
	 * @param sysRoleForm 模块Form.
	 * @return String 跳转到roleList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleUpdate.do")
	public void updateRole(SysRoleForm sysRoleForm, HttpServletResponse response, HttpServletRequest request)throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		//根据角色编号判断角色是否存在.
		List<SysRole> sysRoleList1 = o_sysRoleBO.isExistByRoleCode(sysRoleForm.getRoleCode());
		if(null != sysRoleList1 && sysRoleList1.size()>1){
			out.write("角色编号已存在，请修改!");
			return;
		}else if(1 == sysRoleList1.size()){
			if(!sysRoleList1.get(0).getId().equals(sysRoleForm.getId())){
				out.write("角色编号已存在，请修改!");
				return;
			}
		}
		
		//根据角色名称判断角色是否存在.
		List<SysRole> sysRoleList2 = o_sysRoleBO.isExistByRoleName(sysRoleForm.getRoleName());
		if(null != sysRoleList2 && sysRoleList2.size()>1){
			out.write("角色名称已存在，请修改!");
			return;
		}else if(1 == sysRoleList2.size()){
			if(!sysRoleList2.get(0).getId().equals(sysRoleForm.getId())){
				out.write("角色名称已存在，请修改!");
				return;
			}
		}
		
		SysRole sysRole = new SysRole();
		BeanUtils.copyProperties(sysRoleForm, sysRole);
		
		try{
			o_sysRoleBO.updateRole(sysRole);		
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
		
	}
	
	/**
	 * 删除角色.
	 * @author 吴德福
	 * @param id 要删除的权限id数组.
	 * @return String 跳转到roleList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/roleDel.do")
	public String removeRole(String ids,HttpServletResponse response, HttpServletRequest request)throws Exception{
		//boolean flag = false;
		PrintWriter out = null;
		out = response.getWriter();
				//角色级联删除
				//if(!flag){
				//o_sysRoleBO.removeRole(roleId);
				//}		
		try{
			
			String[] roleids=ids.split(",");
			for(String roleId:roleids){
				if(StringUtils.isNotBlank(roleId)){
					o_sysRoleBO.removeRole(roleId);
					
				}
			}
			out.write("true");
			
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			out.close();
		}
			
		return null;
		
	}
	/**
	 * 为角色添加用户
	 * @author 万业
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sys/auth/roleAssignUser.do")
	public String roleAssignEmp(Model model,HttpServletRequest request){
		String roleid = request.getParameter("id");		
		SysRoleForm sysRoleForm = new SysRoleForm();
		if (roleid != null) {
			SysRole sysRole = o_sysRoleBO.queryRoleById(roleid);
			BeanUtils.copyProperties(sysRole,sysRoleForm);
		}
		model.addAttribute("sysRoleForm", sysRoleForm);
		//model.addAttribute("roleid", roleid);
		
		return "/sys/auth/role/roleAssignUser";
		
	}
	/**
	 * * Ajax给角色分配用户.
	 * @author 万业
	 * @param model
	 * @param roleid 角色ID
	 * @param userid 用户ID 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/roleAssignUserSubmit.do")
	public void roleAssignEmp(SessionStatus status, HttpServletRequest request,HttpServletResponse response ) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String roleId = request.getParameter("roleid");
			String selectIds = request.getParameter("selectIds");
			String[] selectIdArray=null;
			if(StringUtils.isNotBlank(selectIds.trim())){
				selectIdArray=selectIds.substring(0,selectIds.length()-1).split(",");
				for(int i=0;i<selectIdArray.length;i++){
					SysUser sysUser = o_sysUserBO.get(selectIdArray[i]);
					if(null != sysUser && !"".equals(sysUser.getId())){
						continue;
					}else{
						selectIdArray[i]=o_sysUserBO.getSysUserByEmpId(selectIdArray[i]).getId();
					}
				}
			}
				
			//String allIds=request.getParameter("allIds");
			
			//String[] allIdArray=allIds.substring(0,allIds.length()-1).split(",");
			//this.o_sysRoleBO.addBatchUserRole(roleId,selectIdArray,allIdArray);
			o_sysRoleBO.addBatchUserRole(roleId, selectIdArray);
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
	 * 获取某权限已经分配的人员ID
	 * @author 万业
	 * @param request
	 * @param response
	 * @param id role id
	 */
	@ResponseBody
	@RequestMapping(value="/sys/auth/roleAssignedEmp.do")
	public void getAssignedemp(String id, HttpServletRequest request,HttpServletResponse response)throws Exception{

		if(StringUtils.isBlank(id)){
			return;
		}
		String ids = this.o_sysRoleBO.getRoleEmpIds(id);
		response.getWriter().write(ids);
		
	}
	/**
	 * 获取某权限已经分配的人员ID
	 * @author 万业
	 * @param request
	 * @param response
	 * @param id role id
	 */
	@ResponseBody
	@RequestMapping(value="/sys/auth/roleAssignedUser.do")
	public void getAssigneduser(String id, HttpServletRequest request,HttpServletResponse response)throws Exception{

		if(StringUtils.isBlank(id)){
			return;
		}
		String ids = this.o_sysRoleBO.getRoleUserIds(id);
		request.getSession().setAttribute("selectId_session", ids);
		response.getWriter().write("sessioncope");
		
	}
	/**
	 * 给角色分配权限时保存角色Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleAssignAuthority.do")
	public String userAssignAuthority(Model model,HttpServletRequest request){
		
		String roleid = request.getParameter("id");		
		SysRoleForm sysRoleForm = new SysRoleForm();
		if (roleid != null) {
			SysRole sysRole = o_sysRoleBO.queryRoleById(roleid);
			BeanUtils.copyProperties(sysRole,sysRoleForm);
		}
		//进入角色时显示权限列表
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
		model.addAttribute("sysRoleForm", sysRoleForm);
		
		return "sys/auth/role/roleAssignAuthority";
	}
	
	/**
	 * ajax取得当前选中的角色拥有的权限.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/queryAuthorityByRoleAjax.do")
	public void queryAuthorityAjax(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String roleid = request.getParameter("rid");
		PrintWriter out = null;
		out = response.getWriter();
		
		try {
			List<String> authorityIds = o_sysRoleBO.queryAuthorityByRole(roleid);
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
	 * 给角色分配权限.
	 * @author 吴德福
	 * @param status
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/roleAssignAuthoritySubmit.do")
	public void roleAssignAuthority(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String sysRoleId = request.getParameter("rid");
			String selectIds = request.getParameter("selectIds");
			String[] authorityIds = selectIds.substring(0,selectIds.length()-1).split(",");
			
			SysRole sysRole = o_sysRoleBO.queryRoleById(sysRoleId);
			
			Set<SysAuthority> authorities= new HashSet<SysAuthority>(); 
			for(String authorityId : authorityIds) {
				SysAuthority authority = new SysAuthority();
				//authority.setId(authorityId);
				authority = o_sysAuthorityBO.queryAuthorityById(authorityId);//设置级联操作时使用
				authorities.add(authority);
			}
			sysRole.setSysAuthorities(authorities);
		
			o_sysRoleBO.merge(sysRole);
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
	 * 角色分配首页检测.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/auth/roleAssignIndex.do")
	public void saveAndDelWarningRegion(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		
		try{
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			// 接收参数
			String roleId = request.getParameter("roleId");
			
			// 根据角色id查询该角色下的人员
			Set<SysUser> sysRoleUserList = o_sysRoleBO.queryUsersByRoleId(roleId);
			if(null !=sysRoleUserList && sysRoleUserList.size()>0){
				flag = "true";
			}else{
				flag = "该角色下没有人员，请检查!";
			}
			out.write(flag);
		}catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
	}
	
	
	// XXX 新系统方法
	@ResponseBody
	@RequestMapping(value="/sys/auth/role/treeloader")
	public List<Map<String, Object>> treeLoader(String node, String query){
		return o_sysRoleTreeBO.treeLoader(node,query);
	}
}

