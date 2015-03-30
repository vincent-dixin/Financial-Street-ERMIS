/**
 * SysUserControl.java
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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DigestUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.ObjectUtil;
import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.web.form.auth.SysUserForm;

/**
 * 用户Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types =SysUserForm.class)
public class SysUserControl {

	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private SysAuthorityBO o_sysAuthorityBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private EmpolyeeBO o_employeeBO;

	
	/**
	 * 查询所有的用户.
	 * @author 吴德福
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/auth/userList.do")
	public String queryAllUser(Model model,String success){
		List<SysUser> userList = new ArrayList<SysUser>();
		userList = o_sysUserBO.queryAllUser();
		model.addAttribute("userList", userList);
		model.addAttribute("success", success);
		return "sys/auth/user/userList";
	}
	/**
	 * 获取用户列表
	 * @author 万业
	 * @param start
	 * @param limit
	 * @param userName
	 * @param realname
	 * @param fromRoleAss 是从为role指定用户所使用的的标记
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sys/auth/userListJSON.do")
	public Map<String,Object> queryUser(Model model,int start,int limit,String sort,String dir,String userName,String realname,String roleId){
		Map<String,Object> reMap = new HashMap<String,Object>();
		Page<SysUser> page = new Page<SysUser>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		String ids="";
		String userIdInRole = o_sysUserBO.queryPage(userName,realname,roleId,page,sort,dir);
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		for (SysUser user : page.getList()) {
			Map<String, String> record = new HashMap<String, String>();
			ObjectUtil.ObjectPropertyToMap(record, user);
			datas.add(record);
			ids=user.getId()+","+ids;
		}
		reMap.put("totalCount", page.getFullListSize());
		reMap.put("datas", datas);
		reMap.put("userIdInRole",userIdInRole);
		return reMap;
	}
	/**
	 * 根据查询条件查询用户.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String 跳转到userList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userByCondition.do")
	public String queryUserByCondition(Model model,HttpServletRequest request){
		model.addAttribute("userList", o_sysUserBO.query(request.getParameter("username")));
		return "sys/auth/user/userList";
	}
	/**
	 * 新增时保存用户Form.
	 * @author 吴德福
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userAdd.do")
	public String addUser(Model model){
		model.addAttribute("sysUserForm", new SysUserForm());
		return "sys/auth/user/userAdd";
	}
	/**
	 * 保存用户.
	 * @author 吴德福
	 * @param sysUserForm 权限Form.
	 * @return String
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/userSave.do")
	public void saveUser(SysUserForm sysUserForm,BindingResult result,HttpServletResponse response) throws IOException{
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(sysUserForm, sysUser);
		sysUser.setId(Identities.uuid());
		sysUser.setErrCount(0);
		sysUser.setPassword(DigestUtils.md5ToHex(sysUserForm.getPassword()));
		o_sysUserBO.saveUser(sysUser);
		response.getWriter().print("true");
		//return "/commons/openReload";
	}
	/**
	 * 修改时保存用户Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userMod.do")
	public String modUser(Model model,HttpServletRequest request){
		String id = request.getParameter("id");
		
		List<SysUser> userList = o_sysUserBO.queryAllUser();
		Map<String,String> parentMap = new HashMap<String,String>();
		for(SysUser sysUser : userList){
			parentMap.put(sysUser.getId(), sysUser.getUsername());
		}
		
		SysUserForm sysUserForm = new SysUserForm();
		
		sysUserForm.setParentMap(parentMap);
		BeanUtils.copyProperties(o_sysUserBO.queryUserById(id), sysUserForm);
		model.addAttribute("sysUserForm", sysUserForm);
		return "sys/auth/user/userMod";
	}
	/**
	 * 修改时保存用户密码Form.
	 * @author 万业
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sys/auth/changePwd.do")
	public String changUserPwd(Model model,HttpServletRequest request){
		String uid=request.getParameter("userid");
		SysUser user=o_sysUserBO.queryUserById(uid);
		
		SysUserForm sysUserForm = new SysUserForm();
		BeanUtils.copyProperties(user, sysUserForm);
		model.addAttribute("sysUserForm", sysUserForm);
		return "sys/auth/user/userChangpwd";
		
	}
	/**
	 * 用户密码重置.
	 * @author 吴德福
	 * @param userIds
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value="/sys/auth/resetPwd.do")
	public void changUserPwd(String userIds, HttpServletResponse response) throws IOException {
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			if(null != userIds && !"".equals(userIds)){
				String[] userIdArray = userIds.split(",");
				for(String userId : userIdArray){
					SysUser sysUser = o_sysUserBO.get(userId);
					sysUser.setPassword(DigestUtils.md5ToHex(sysUser.getUsername()));
					o_sysUserBO.updateUser(sysUser);
				}
			}
			
			flag = "true";
			out.write(flag);
		}catch(Exception e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	/**
	 * 修改用户.
	 * @author 吴德福
	 * @param sysUserForm 模块Form.
	 * @return String 跳转到userList.jsp页面.
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userUpdate.do")
	public void updateUser(SysUserForm sysUserForm,BindingResult result,HttpServletResponse response) throws IOException{
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(sysUserForm, sysUser);
		
		o_sysUserBO.updateUser(sysUser);
		response.getWriter().print("true");
	}
	/**
	 * 修改密码
	 * @author 万业
	 * @param sysUserForm
	 * @param result
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/sys/auth/userUpdatePwd.do")
	public void updateUserPwd(SysUserForm sysUserForm,HttpServletRequest request,HttpServletResponse response) throws IOException{
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(sysUserForm, sysUser);
		sysUser.setPassword(DigestUtils.md5ToHex(sysUserForm.getPassword()));
		o_sysUserBO.updateUser(sysUser);
		response.getWriter().print("true");
	}
	/**
	 * 删除用户.
	 * @author 吴德福
	 * @param id 要删除的权限id数组.
	 * @return String 跳转到userList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/auth/userDel.do")
	public String removeUser(String ids,HttpServletResponse response, HttpServletRequest request)throws Exception{
	
		PrintWriter out = null;
		out = response.getWriter();
				
		try{
			
			String[] userIds = ids.split(",");
			for(String userId : userIds){
				SysUser sysUser = o_sysUserBO.queryUserById(userId);
				//admin用户不可以删除
				if(null != sysUser && !"admin".equals(sysUser.getUsername().trim())){
					SysEmployee employee = o_employeeBO.questEmployeeByUserid(userId);
					if(null != employee){
						employee.setUserid("");
						employee.setUsername(null);
						employee.setRealname(null);
						o_employeeBO.updateEmployee(employee);
					}

					o_sysUserBO.removeUser(userId);
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
	 * 给用户分配权限时保存用户Form.
	 * @author 吴德福
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userAssignAuthority.do")
	public String userAssignAuthority(Model model,HttpServletRequest request){
		
		String userid = request.getParameter("id");		
		SysUserForm sysUserForm = new SysUserForm();
		if (userid != null) {
			SysUser sysUser = o_sysUserBO.queryUserById(userid);
			BeanUtils.copyProperties(sysUser,sysUserForm);
		}
		//进入角色时显示权限列表
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
		model.addAttribute("sysUserForm", sysUserForm);
		
		return "sys/auth/user/userAssignAuthority";
	}
	/**
	 * ajax取得当前选中的用户拥有的权限.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/queryAuthorityByUserAjax.do")
	public void queryAuthorityAjax(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String userid = request.getParameter("uid");
		PrintWriter out = null;
		out = response.getWriter();
		
		try {
			List<String> authorityIds = o_sysUserBO.queryAuthorityByUser(userid);
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
	 * 给用户分配权限.
	 * @author 吴德福
	 * @param status
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userAssignAuthoritySubmit.do")
	public void userAssignAuthoritySubmit(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String userid = request.getParameter("userid");
			String selectIds = request.getParameter("selectIds");
			String[] authorityIds = selectIds.substring(0,selectIds.length()-1).split(",");
			SysUser sysUser = o_sysUserBO.queryUserById(userid);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>(); 
			for(String authorityId : authorityIds) {
				SysAuthority authority = new SysAuthority();
				//authority.setId(authorityId);
				authority = o_sysAuthorityBO.queryAuthorityById(authorityId);//设置级联操作时使用
				authorities.add(authority);
			}
			sysUser.setSysAuthorities(authorities);
			o_sysUserBO.updateUser(sysUser);
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
	 * 给用户分配角色时保存用户Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userAssignRole.do")
	public String userAssignRole(Model model,HttpServletRequest request){
		
		String userid = request.getParameter("id");	
		SysUserForm sysUserForm = new SysUserForm();
		List<SysRole> roleList = o_sysRoleBO.queryAllRole();
		if (userid != null) {
			SysUser sysUser = o_sysUserBO.queryUserById(userid);
			BeanUtils.copyProperties(sysUser,sysUserForm);
			
			String roleIds = "";
			for (SysRole role : sysUser.getSysRoles()) {
				roleIds = roleIds + role.getId()+",";
			}
			sysUserForm.setRoleIds(roleIds);
		}
		
		model.addAttribute("sysUserForm", sysUserForm);
		model.addAttribute("roleList", roleList);
		
		return "sys/auth/user/userAssignRole";
	}
	/**
	 * 给用户分配角色.
	 * @author 吴德福
	 * @param sysUserForm 模块Form.
	 * @return String 跳转到userList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/userAssignRoleSubmit.do")
	public void userAssignRoleSubmit(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String userid = request.getParameter("userid");
			String selectIds = request.getParameter("selectIds");
			String[] roleIds = selectIds.substring(0,selectIds.length()-1).split(",");
			SysUser sysUser = o_sysUserBO.queryUserById(userid);
			Set<SysRole> sysRoles= new HashSet<SysRole>(); 
			for(String roleId : roleIds) {
				SysRole sysRole = new SysRole();
				//sysRole.setId(roleId);
				sysRole = o_sysRoleBO.queryRoleById(roleId);//设置级联操作时使用
				sysRoles.add(sysRole);
			}
			sysUser.setSysRoles(sysRoles);
			o_sysUserBO.updateUser(sysUser);
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
	 * ajax获取用户名过去密码是否正确.
	 * @author 万业
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/auth/queryUserByUsername.do")
	public void queryUserByUsername(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
        try {
            String oldpwd = request.getParameter("oldpwd");
            String uid = request.getParameter("uid");
            response.setContentType("text/html;charset=utf-8");
            String flag = "false";
            out = response.getWriter();
            
            SysUser user = o_sysUserBO.queryUserById(uid);
            if(DigestUtils.md5ToHex(oldpwd).equals(user.getPassword())){
            	flag="true";
            }
            out.write(flag);
           
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            out.close();
        }
	}
	
	
}

