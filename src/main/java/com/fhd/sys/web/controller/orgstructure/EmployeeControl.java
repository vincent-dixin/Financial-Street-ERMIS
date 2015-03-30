/**
 * EmpManagementController.java
 * com.fhd.fdc.commons.web.controller.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-12 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fhd.core.dao.support.Page;
import com.fhd.core.dao.utils.HibernateUtils;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.DigestUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysAuthorityBO;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.business.orgstructure.PositionBO;
import com.fhd.sys.business.orgstructure.SysEmpOrgBO;
import com.fhd.sys.business.orgstructure.SysEmpPosiBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.web.form.orgstructure.EmpForm;

/**
 * 员工Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-15
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types =EmpForm.class)
public class EmployeeControl {

	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private PositionBO o_positionBO;
	@Autowired
	private SysEmpOrgBO o_sysEmpOrgBO;
	@Autowired
	private SysEmpPosiBO o_sysEmpPosiBO;
	@Autowired
	private SysAuthorityBO o_sysAuthorityBO;
	@Autowired
	private OrganizationBO o_sysOrganizationBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * get方式修改员工.
	 * @author 吴德福
	 * @param id 员工id.
	 * @param model
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/edit.do",method = RequestMethod.GET)
	public void g_editEmp(String id,String success,Model model,HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		EmpForm empForm = new EmpForm();
		SysEmployee employee = o_empolyeeBO.get(id);
		BeanUtils.copyProperties(employee,empForm);
		if(null != employee.getUserid() && !"".equals(employee.getUserid())){
			SysUser user = o_sysUserBO.getSysUserByEmpId(id);
			empForm.setUserid(user.getId());
			empForm.setUserStatus(user.getUserStatus());
			empForm.setPassword(user.getPassword());
			empForm.setLockstate(user.getLockstate());
			empForm.setEnable(user.getEnable());
			empForm.setExpiryDate(user.getExpiryDate());
			empForm.setCredentialsexpiryDate(user.getCredentialsexpiryDate());
		}
		//设置所属公司名称
		empForm.setOrgName(employee.getSysOrganization().getOrgname());
		model.addAttribute("empForm",empForm);
		model.addAttribute("success", request.getParameter("success"));
	}
	
	/**
	 * post方式修改员工.
	 * @author 吴德福
	 * @param empForm 员工Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/edit.do",method = RequestMethod.POST)
	public String p_editEmp(EmpForm empForm,BindingResult result,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String submitfrom = request.getParameter("submitform");
		SysEmployee employee = o_empolyeeBO.get(empForm.getId());
		String userid = empForm.getUserid();
		BeanUtils.copyProperties(empForm, employee);
		if("1".equals(empForm.getCheckOpr())){
			if("".equals(userid) || null==userid){
				SysUser sysUser = new SysUser();
				BeanUtils.copyProperties(empForm, sysUser);
				if(null == sysUser.getId() || "".equals(sysUser.getId())){
					sysUser.setId(Identities.uuid2());
					sysUser.setErrCount(0);
					//sysUser.setEnable(true);
					//sysUser.setLockstate(false);
					String resetpassword = request.getParameter("resetpassword");
					if(StringUtils.isNotBlank(resetpassword)){
						sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
					}else{
						sysUser.setPassword(empForm.getPassword());
					}
					SysUser user = (SysUser)o_sysUserBO.saveUser(sysUser);
					employee.setUserid(user.getId());
				}else{
					sysUser.setErrCount(0);
					//sysUser.setEnable(true);
					//sysUser.setLockstate(false);
					String resetpassword = request.getParameter("resetpassword");
					if(StringUtils.isNotBlank(resetpassword)){
						sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
					}else{
						sysUser.setPassword(empForm.getPassword());
					}
					o_sysUserBO.updateUser(sysUser);
					employee.setUserid(sysUser.getId());
				}
			}else{
				SysUser sysUser = o_sysUserBO.queryUserById(empForm.getUserid());
				BeanUtils.copyProperties(empForm, sysUser);
				sysUser.setId(empForm.getUserid());
				String resetpassword = request.getParameter("resetpassword");
				if(StringUtils.isNotBlank(resetpassword)){
					sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
				}else{
					sysUser.setPassword(empForm.getPassword());
				}
				sysUser.setErrCount(0);
				o_sysUserBO.updateUser(sysUser);
			}
		}else{
			employee.setUserid("");
		}
		
		employee.setSysOrganization(employee.getSysOrganization());
		employee.setEmpcode(empForm.getEmpcode().toUpperCase());
		//String flag = 
			o_empolyeeBO.merge(employee);
		
		if(StringUtils.isNotBlank(submitfrom) && "baseInfo".equals(submitfrom)){
			//基本信息提交
			return "redirect:/sys/orgstructure/emp/edit.do?success=success&id=" + empForm.getId();
		}else{
			PrintWriter out = response.getWriter();
			out.write("true");
			out.close();
			//列表弹出框提交
			return null;
		}
	}
	/**
	 * 打开选择员工树页面.
	 * @author David.Niu
	 * @param request
	 * @param type 打开页面类型；如果为"multiple",打开多选页面；如果为"single",打开单选页面
	 * @param checkNode 是否有复选框
	 * @param onlyLeafCheckable 是否只是叶子节点加复选框
	 * @param checkModel 多选: 'multiple'(默认)、单选: 'single'、级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	 * @param tag 员工属性名称
	 * @param orgName 部门属性名称
	 * @param posiName 岗位属性名称
	 * @param empfilter 查询条件
	 * @param defaultOrg 是否默认显示当前登录部门的员工
	 * @return String 如果输入参数type为为"multiple",打开empSelector_mutiple.jsp；如果为"single",打开empSelector_single.jsp.
	 */
	@RequestMapping(value = "/sys/orgstructure/org/openEmpSelectorTreePage.do")
	public String openSelectorTreePage(HttpServletRequest request, String type,
			String checkNode, String onlyLeafCheckable, String checkModel, String tag, String orgName, String posiName, String empfilter, Boolean defaultOrg) {
		request.setAttribute("orgRoot", o_organizationBO.getRootOrg());
		request.setAttribute("checkNode", checkNode);
		request.setAttribute("onlyLeafCheckable", onlyLeafCheckable);
		request.setAttribute("checkModel", checkModel);
		request.setAttribute("tag", tag);
		request.setAttribute("orgName", orgName);
		request.setAttribute("posiName", posiName);
		request.setAttribute("empfilter", empfilter);//人员过滤
		request.setAttribute("defaultOrg", defaultOrg);//人员过滤
		
		if ("mutiple".equals(type)){
			if(StringUtils.isNotBlank(orgName)){
				return "sys/orgstructure/tag/orgEmpSelector_mutiple";
			}
			return "sys/orgstructure/tag/empSelector_mutiple";
		}else{
			if(StringUtils.isNotBlank(orgName)){
				return "sys/orgstructure/tag/orgEmpSelector_single";
			}
		}
		return "sys/orgstructure/tag/empSelector_single";
	}
	/**
	 * 
	 * @param request
	 * @param type
	 * @param checkNode
	 * @param onlyLeafCheckable
	 * @param checkModel
	 * @param tag
	 * @param empfilter
	 * @return
	 */
	@RequestMapping(value = "/sys/orgstructure/org/openEmpSelectorExTreePage.do")
	public String openSelectorExTreePage(HttpServletRequest request, String type,
			String checkNode, String onlyLeafCheckable, String checkModel,String tag,String empfilter, String selects) {
		request.setAttribute("orgRoot", o_organizationBO.getRootOrg());
		request.setAttribute("checkNode", checkNode);
		request.setAttribute("onlyLeafCheckable", onlyLeafCheckable);
		request.setAttribute("checkModel", checkModel);
		request.setAttribute("tag", tag);
		request.setAttribute("selects", selects);
		
//		request.setAttribute("empfilter", empfilter);//人员过滤
		
		
		SysOrganization org = o_organizationBO.getRootOrg();
		request.setAttribute("root", org);
		//String selects = request.getParameter("selects");
		if("sessioncope".equals(selects)){
			Object object = request.getSession().getAttribute("selectId_session");
			selects=null!=object?object.toString():"";
			
		}
		selects = selects == null ? "" : selects;
		String[] ids = selects.split(",");
		
		//List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		StringBuilder sb=new StringBuilder();
		for(String id:ids){
			if(StringUtils.isBlank(id))
				continue;
			//SysUser user = this.o_sysUserBO.get(id);
			SysEmployee emp= this.o_empolyeeBO.get(id);
			//{"id":"asdf","realname":"asdf"},{}
			if(null==emp)
				continue;
			
			sb.append("{\"id\":\"");
			sb.append(id);
			sb.append("\",\"realname\":\"");
			
			sb.append(null!=emp?(StringUtils.isNotBlank(emp.getRealname())?emp.getRealname():emp.getUsername()):"");
			sb.append("\"},");
			
		}
	//	JSONArray jsonData = JSONArray.fromObject(datas);  
		
		String jsonstr=sb.toString();
		String jsonobj="";
		if(StringUtils.isNotBlank(jsonstr.trim())){
			
			jsonobj="["+jsonstr.substring(0, jsonstr.length()-1)+"]";
		}else{
			jsonobj="[]";
		}
		request.setAttribute("selects", jsonobj);
		
		request.setAttribute("orgRoot", o_organizationBO.getRootOrg());
		
		
		if ("mutiple".equals(type)){
			return "sys/orgstructure/tag/empSelector_mutipleEx";
		}
		return "sys/orgstructure/tag/empSelector_singleEx";
	}
	
	/**
	 * 点击员工，加载tab页面.
	 * @author 胡迪新
	 * @param id
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/sys/orgstructure/emp/tabs.do")
	public String showTabs(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
		SysUser user = o_sysUserBO.getSysUserByEmpId(id);
		if(null == user){
			request.setAttribute("flag", 0);
		}else{
			request.setAttribute("flag", 1);
		}
		return "sys/orgstructure/emp/tabs";
	}
	/**
	 * 查询当前机构下所有的员工--加载本机构员工时使用.
	 * @author 胡迪新
	 * @param id 机构id.
	 * @param model
	 * @param request
	 * @param success
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/query.do")
	public String query(String id, Model model,HttpServletRequest request,String success) {
		model.addAttribute("emps", o_empolyeeBO.queryEmpsByOrgid(id));
		model.addAttribute("success", success);
		return "sys/orgstructure/emp/query";
	}
	
	
	/**
	 * 进入查询机构下的员工列表页面
	 * @author 万业 
	 * @param id 机构ID
	 * @param model
	 * @param request
	 * @param success
	 * @return
	 */
	@RequestMapping(value ="/sys/orgstructure/emp/queryNextEmp.do")
	public String g_queryNextEmp(String id, Model model,HttpServletRequest request,String success) {
		model.addAttribute("id", id);
		return "sys/orgstructure/emp/query";
	}
	/**
	 * 根据机构ID查询员工
	 * @author 万业
	 * @param id 机构ID
	 * @param limit
	 * @param start
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@ResponseBody           
	@RequestMapping(value ="/sys/orgstructure/emp/queryNextonlyEmpList.do")
	public Map<String, Object> queryNextEmpList(String id, int limit, int start, String empcode, String empname)throws Exception {
		SysEmployee sysEmp=new SysEmployee();
		if(StringUtils.isNotBlank(empcode)){
			sysEmp.setEmpcode(empcode);
		}
		if(StringUtils.isNotBlank(empname)){
			sysEmp.setEmpname(empname);
		}
		
		Page<SysEmpOrg> page = new Page<SysEmpOrg>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		page = o_empolyeeBO.querySysEmpByOrgIdPage(page, id, sysEmp);
		return convertsysEmpOrgValues(page);
		
		
	}
	/**
	 * 根据机构包括子机构下ID查询员工
	 * @author 万业
	 * @param id 机构ID
	 * @param limit
	 * @param start
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/sys/orgstructure/emp/queryNextEmpList.do")
	public Map<String, Object> queryNextAllEmpList(String id, int limit, int start, String empcode, String empname)throws Exception {
		SysEmployee sysEmp=new SysEmployee();
		if(StringUtils.isNotBlank(empcode)){
			sysEmp.setEmpcode(empcode);
		}
		if(StringUtils.isNotBlank(empname)){
			sysEmp.setEmpname(empname);
		}
		
		com.fhd.core.dao.Page<SysEmployee> page = new com.fhd.core.dao.Page<SysEmployee>();
		page.setPageNo((limit == 0 ? 0 : start / limit)+1);
		page.setPageSize(limit);
		page = o_empolyeeBO.querySysAllEmployeeByOrgId(page, id, sysEmp);
		
		
		List<SysEmployee> sysEmps=page.getResult();
		Iterator<SysEmployee> iterator=sysEmps.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysEmployee sysEmployee=iterator.next();
			row.put("id", sysEmployee.getId());
			row.put("empcode", sysEmployee.getEmpcode());
			row.put("empname", sysEmployee.getEmpname());
			row.put("orgname", sysEmployee.getSysOrganization().getOrgname());
			row.put("username", sysEmployee.getUsername());
			row.put("mobikeno", sysEmployee.getMobikeno());
			row.put("regdate", DateUtils.formatLongDate(sysEmployee.getRegdate()));
			String empStatus=sysEmployee.getEmpStatus();
			row.put("empStatus", StringUtils.isNotBlank(empStatus)?empStatus:"");
			row.put("userid", sysEmployee.getUserid());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
		
		//return map;
		
		
	}
	/**
	 * 转换机构内员工page to map 
	 * form emporg to emp 
	 * @author 万业
	 * @param page
	 * @return
	 */
	public Map<String, Object>convertsysEmpOrgValues(Page<SysEmpOrg> page){
		List<SysEmpOrg> sysEmps=page.getList();
		Iterator<SysEmpOrg> iterator=sysEmps.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysEmpOrg sysEmpOrg=iterator.next();
			row.put("id", sysEmpOrg.getSysEmployee().getId());
			row.put("empcode", sysEmpOrg.getSysEmployee().getEmpcode());
			row.put("empname", sysEmpOrg.getSysEmployee().getEmpname());
			row.put("orgname", sysEmpOrg.getSysEmployee().getSysOrganization().getOrgname());
			row.put("username", sysEmpOrg.getSysEmployee().getUsername());
			row.put("mobikeno", sysEmpOrg.getSysEmployee().getMobikeno());
			row.put("regdate", DateUtils.formatLongDate(sysEmpOrg.getSysEmployee().getRegdate()));
			String empStatus=sysEmpOrg.getSysEmployee().getEmpStatus();
			row.put("empStatus", StringUtils.isNotBlank(empStatus)?empStatus:"");
			row.put("userid", sysEmpOrg.getSysEmployee().getUserid());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * 查询当前机构下不在当前岗位的所有员工(新增岗位与员工关系时查询跳转页面使用).
	 * @author 胡迪新
	 * @param id 机构id.
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmployeeByOrg.do")
	public String queryEmployeeByOrg(String id, Model model,HttpServletRequest request,String success){
		model.addAttribute("emps", o_empolyeeBO.queryEmployees(id));
		model.addAttribute("success", success);
		return "sys/orgstructure/emp/queryEmployeeByOrg";
	}
	
	/**
	 * 进入查询当前岗位下所有的员工页面
	 * @author 胡迪新	
	 * @author 万业	
	 * @param id 岗位id.
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmpsByPosi.do")
	public String queryEmpsByPosi(String id, Model model){//,HttpServletRequest request,String success) {
		model.addAttribute("id",id);
		
		return "sys/orgstructure/emp/queryEmpsByPosi";
	}
	/**
	 * 根据parentType 查询下属的人员
	 * @author wanye
	 * @param id
	 * @param limit
	 * @param start
	 * @param empcode
	 * @param empname
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody   
	@RequestMapping("/sys/orgstructure/emp/queryEmps.do")
	public Map<String, Object> queryEmpsByOrgorPosi(String id, int limit, int start, String empcode, String empname,HttpServletRequest request)throws Exception{
		String parentType = request.getParameter("parentType");
		if("org".equals(parentType)){
			//return this.queryNextEmpList(id, limit, start, empcode, empname);
			return this.queryNextAllEmpList(id, limit, start, empcode, empname);
		}else if("posi".equals(parentType)){
			return this.queryEmpsByPosi(id, limit, start, empcode, empname);
		}else{
			return null;
		}
	}
	/**
	 * 查询当前岗位下所有的员工的数据 分页
	 * @author 万业
	 * @param id 岗位ID
	 * @param limit
	 * @param start
	 * @param empcode
	 * @param empname
	 * @return
	 */
	@ResponseBody   
	@RequestMapping("sys/orgstructure/emp/queryEmpsByPosiList.do")
	public Map<String, Object> queryEmpsByPosi(String id, int limit, int start, String empcode, String empname)throws Exception{
		SysEmployee sysEmp=new SysEmployee();
		if(StringUtils.isNotBlank(empcode)){
			sysEmp.setEmpcode(empcode);
		}
		if(StringUtils.isNotBlank(empname)){
			sysEmp.setEmpname(empname);
		}
		
		com.fhd.core.dao.Page<SysEmployee> page = new com.fhd.core.dao.Page<SysEmployee>();
		page.setPageNo((limit == 0 ? 0 : start / limit)+1);
		page.setPageSize(limit);
		page = o_empolyeeBO.querySysEmpByPosiIdPage(page, id, sysEmp);
		return convertsysEmpValues(page);
		
	}
	/**
	 * 转换page to Map for Ext.datas
	 * @author 万业
	 * @param page
	 * @return
	 */
	public Map<String, Object>convertsysEmpValues(com.fhd.core.dao.Page<SysEmployee> page){
		List<SysEmployee> sysEmps=page.getResult();
		Iterator<SysEmployee> iterator=sysEmps.iterator();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		while(iterator.hasNext()){
			Map<String, Object> row = new HashMap<String, Object>();
			SysEmployee sysEmp=iterator.next();
			row.put("id", sysEmp.getId());
			row.put("empcode", sysEmp.getEmpcode());
			row.put("empname", sysEmp.getEmpname());
			row.put("orgname", sysEmp.getSysOrganization().getOrgname());
			row.put("username", sysEmp.getUsername());
			row.put("mobikeno", sysEmp.getMobikeno());
			row.put("regdate", DateUtils.formatLongDate(sysEmp.getRegdate()));
			String empStatus=sysEmp.getEmpStatus();
			row.put("empStatus", "1".equals(empStatus)?"启用":"停用");
			row.put("userid", sysEmp.getUserid());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 根据查询条件查询岗位.
	 * @author 胡迪新
	 * @param id 岗位ID
	 * @param filters
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryByCondition.do")
	public String queryEmployeeByOrgid(String id,Model model){
		//model.addAttribute("emps", o_empolyeeBO.queryEmployeeByOrgid(id, HibernateUtils.buildPropertyFilters(request, "filter_")));
		//model.addAttribute("success", success);
		model.addAttribute("id", id);
		return "sys/orgstructure/emp/query";
	}
	/**
	 * 根据查询条件查询员工.
	 * @author 胡迪新
	 * @param id 岗位id.
	 * @param filters
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmpByPosiIdCondition.do")
	public String queryEmpByPosiIdCondition(String id,String success,Model model,HttpServletRequest request){
		model.addAttribute("emps", o_empolyeeBO.queryEmployeeByOrgid(o_positionBO.get(id).getSysOrganization().getId(), HibernateUtils.buildPropertyFilters(request, "filter_")));
		model.addAttribute("success", success);
		return "sys/orgstructure/emp/queryEmployeeByOrg";
	}
	/**
	 * get方式添加员工. from机构下的员工列表
	 * @author 胡迪新
	 * @param id 机构ID
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/add.do", method = RequestMethod.GET)
	public String g_add(String id,Model model,HttpServletRequest request){
		EmpForm empForm = new EmpForm();
		//empForm.setPositionId(positionId);
		String orgNameString = null;
		if (id.length()-4>0) {
			orgNameString = o_sysOrganizationBO.get(id).getOrgname();
		}else {
			orgNameString = o_sysOrganizationBO.get(id).getOrgname();
		}
		empForm.setOrgName(orgNameString);
		model.addAttribute("empForm",empForm);
		model.addAttribute("id", id);
		model.addAttribute("operation", request.getParameter("operation"));
		return "sys/orgstructure/emp/add-model";
	}
	/**
	 * get方式添加员工 from岗位下的员工列表
	 * @param id 岗位ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/fromPosiadd.do", method = RequestMethod.GET)
	public String g_addFromPosiList(String id,Model model,HttpServletRequest request){
		EmpForm empForm = new EmpForm();
		SysPosition posi = null;
		
		//empForm.setPositionId(positionId);
		if (id.length()-4 > 0) {
			posi = o_positionBO.get(id);
		}else{
			posi = o_positionBO.get(id);
		}
		empForm.setOrgName(posi.getSysOrganization().getOrgname());//机构名页面显示
		empForm.setPositionId(id);
		model.addAttribute("id", posi.getSysOrganization().getId());//岗位的机构ID
		model.addAttribute("empForm",empForm);
		model.addAttribute("operation", request.getParameter("operation"));
		return "sys/orgstructure/emp/add-model";
	}
	/**
	 * post方式添加员工.
	 * @author 胡迪新
	 * @param posiForm
	 * @param bindingResult
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/add.do", method = RequestMethod.POST)
	public String p_add(EmpForm empForm, BindingResult result,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		SysEmployee sysEmployee = new SysEmployee();
		SysOrganization sysOrganization = new SysOrganization();
		String id =empForm.getOrgId();
		SysOrganization dept = o_sysOrganizationBO.get(id);
		// 设置所属部门不是叶子结点
		dept.setIsLeaf(false);
		o_sysOrganizationBO.merge(dept);
		sysOrganization = o_sysOrganizationBO.getOrgByOrgtype(o_sysOrganizationBO.get(id));
		BeanUtils.copyProperties(empForm, sysEmployee);
		if("1".equals(empForm.getCheckOpr())){
			SysUser sysUser = new SysUser();
			BeanUtils.copyProperties(empForm, sysUser);
			sysUser.setId(Identities.uuid2());
			sysUser.setErrCount(0);
			sysUser.setEnable(true);
			sysUser.setLockstate(false);
			sysUser.setPassword(DigestUtils.md5ToHex(empForm.getPassword()));
			o_sysUserBO.saveUser(sysUser);
			sysEmployee.setUserid(sysUser.getId());
		}else{
			sysEmployee.setUserid("");
		}
		
		
		sysEmployee.setSysOrganization(sysOrganization);
		sysEmployee.setId(Identities.uuid2());
		sysEmployee.setEmpcode(empForm.getEmpcode().toUpperCase());
		//如果岗位ID不为空是从岗位员工列表来的，添加岗位与员工相关联系
		if(StringUtils.isNotBlank(empForm.getPositionId())){
			o_empolyeeBO.saveEmpO(id,sysEmployee,empForm.getPositionId());
		}else{
			o_empolyeeBO.saveEmpO(id,sysEmployee,null);
		}
		
		PrintWriter out = response.getWriter();
		out.write("true");
		out.close();
		return null;
	}
	
	/**
	 * get方式修改员工.
	 * @author 吴德福
	 * @param id 员工id.
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/update.do", method = RequestMethod.GET)
	public String g_update(String id,Model model) {
		EmpForm empForm = new EmpForm();
		SysEmployee employee = o_empolyeeBO.get(id);
		BeanUtils.copyProperties(employee,empForm);
		if(null != employee.getUserid() && !"".equals(employee.getUserid())){
			SysUser user = o_sysUserBO.getSysUserByEmpId(id);
			empForm.setUserid(user.getId());
			empForm.setUserStatus(user.getUserStatus());
			empForm.setPassword(user.getPassword());
			empForm.setLockstate(user.getLockstate());
			empForm.setEnable(user.getEnable());
			empForm.setExpiryDate(user.getExpiryDate());
			empForm.setCredentialsexpiryDate(user.getCredentialsexpiryDate());
		}
		//设置所属公司名称
		empForm.setOrgName(employee.getSysOrganization().getOrgname());
		model.addAttribute(empForm);
		return "sys/orgstructure/emp/update-model";
	}
	/**
	 * post方式修改员工.
	 * @author 吴德福
	 * @param empForm 员工Form.
	 * @param result
	 * @return String
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/update.do", method = RequestMethod.POST)
	public String p_update(EmpForm empForm,BindingResult result,HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		SysEmployee employee = o_empolyeeBO.get(empForm.getId());
		String userid = empForm.getUserid();
		BeanUtils.copyProperties(empForm, employee);
		if("1".equals(empForm.getCheckOpr())){
			if("".equals(userid) || null==userid){
				SysUser sysUser = new SysUser();
				BeanUtils.copyProperties(empForm, sysUser);
				if(null == sysUser.getId() || "".equals(sysUser.getId())){
					sysUser.setId(Identities.uuid2());
					sysUser.setErrCount(0);
					//sysUser.setEnable(true);
					//sysUser.setLockstate(false);
					String resetpassword = request.getParameter("resetpassword");
					if(StringUtils.isNotBlank(resetpassword)){
						sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
					}else{
						sysUser.setPassword(empForm.getPassword());
					}
					SysUser user = (SysUser)o_sysUserBO.saveUser(sysUser);
					employee.setUserid(user.getId());
				}else{
					sysUser.setErrCount(0);
					//sysUser.setEnable(true);
					//sysUser.setLockstate(false);
					String resetpassword = request.getParameter("resetpassword");
					if(StringUtils.isNotBlank(resetpassword)){
						sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
					}else{
						sysUser.setPassword(empForm.getPassword());
					}
					o_sysUserBO.updateUser(sysUser);
					employee.setUserid(sysUser.getId());
				}
			}else{
				
				
				SysUser sysUser = o_sysUserBO.queryUserById(empForm.getUserid());
				BeanUtils.copyProperties(empForm, sysUser);
				sysUser.setId(empForm.getUserid());
				String resetpassword = request.getParameter("resetpassword");
				if(StringUtils.isNotBlank(resetpassword)){
					sysUser.setPassword(DigestUtils.md5ToHex(resetpassword));
				}else{
					sysUser.setPassword(empForm.getPassword());
				}
				sysUser.setErrCount(0);
				o_sysUserBO.updateUser(sysUser);
				
			}
		}else{
			employee.setUserid("");
		}
		
		employee.setSysOrganization(employee.getSysOrganization());
		employee.setEmpcode(empForm.getEmpcode().toUpperCase());
		o_empolyeeBO.merge(employee);
		return "true";
		//return "redirect:/sys/orgstructure/emp/query.do?id=" + empForm.getSysOrganization().getId() + "&success=1";
	}
	/**
	 * 岗位tab中根据查询条件查询员工.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmpPosiByCondition.do")
	public String queryEmpPosiByCondition(Model model,EmpForm empForm){
		model.addAttribute("emps", o_empolyeeBO.query(empForm));
		return "sys/orgstructure/emp/queryEmpsByPosi";
	}
	
	/**
	 * 添加岗位与员工的关系.
	 * @author 吴德福
	 * @param id 岗位id与选择的员工id集合.
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/addEmpPosi.do")
	public String addEmpPosi(String[] id){
		String posiId = StringUtils.substring(id[0], 0, 32);
		for(int i=1;i<id.length;i++){
			List<SysEmpPosi> resultList = new ArrayList<SysEmpPosi>();
			resultList = o_sysEmpPosiBO.querySysEmpPosiByUnionid(posiId,StringUtils.substring(id[i], 0, 32));
			if(null != resultList && resultList.size()>0){
				continue;
			}else{
				SysEmpPosi sysEmpPosi = new SysEmpPosi();
				sysEmpPosi.setId(Identities.uuid2());
				sysEmpPosi.setSysPosition(o_positionBO.get(posiId));
				sysEmpPosi.setSysEmployee(o_empolyeeBO.get(StringUtils.substring(id[i], 0, 32)));
				sysEmpPosi.setIsmain(false);
				o_sysEmpPosiBO.save(sysEmpPosi);
			}
		}
		return "redirect:/sys/orgstructure/emp/queryEmpsByPosi.do?id="+posiId+"&success=1";
	}
	/**
	 * 删除岗位与员工的关系.
	 * @author 吴德福
	 * @param id 岗位id与选择的员工id集合.
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/deleteEmpPosi.do")
	public boolean deleteEmpPosi(String id, String posiId){
		// 前人留下，可能有用 先不删
		boolean flag = false;
		//String posiId = id[0];
		String[] ids=id.split(",");
		
		for(int i=0;i<ids.length;i++){
			
			if(!flag && StringUtils.isNotBlank(ids[i])){
				o_empolyeeBO.deleteEmpPosi(posiId,ids[i]);
			}
		}
		return true;
		//用true页面的刷新不彻底，左边的树没刷新
		//return "redirect:/sys/orgstructure/emp/queryEmpsByPosi.do?id=" + posiId + "&success=1";
	}
	/**
	 * post方式删除员工.
	 * @author 吴德福 
	 * @author _万业
	 * @param id 员工id集合.
	 * 使用EXT后，改为字符串 395E00E8882A115726A75C2A141489F8,3F83BA0CED0517D0C391EC9F5D212844,
	 * @param orgId 机构ID
	 * @return boolean
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/delete.do", method = RequestMethod.POST)
	public boolean p_delete(String id, String orgId){
		String orgid=StringUtils.substring(orgId, 0, 32);
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			if(!StringUtils.isBlank(ids[i])){
				String empid = StringUtils.substring(ids[i], 0, 32);
				List<SysEmpOrg> empOrgs = new ArrayList<SysEmpOrg>();
				empOrgs = o_sysEmpOrgBO.querySysEmpOrgByEmpid(empid);
				if(null != empOrgs && empOrgs.size()>1){
					//员工在多个机构下，不能删除员工，只删除员工与当前机构的关系
					o_sysEmpOrgBO.removeEmpOrg(orgid, empid);
					//return "redirect:/sys/orgstructure/emp/query.do?id=" + orgid + "&success=1";
					return true;
				}else if(null != empOrgs && empOrgs.size()==1){
					//员工在一个机构下，删除员工与当前机构的关系
					o_sysEmpOrgBO.removeEmpOrg(orgid, empid);
					
					List<SysEmpPosi> empPosis = new ArrayList<SysEmpPosi>();
					empPosis = o_sysEmpPosiBO.querySysEmpPosiByEmpid(empid);
					//员工在岗位下存在，不能删除员工
					if(null != empPosis && empPosis.size()>0){
						//return "redirect:/sys/orgstructure/emp/query.do?id=" + orgid + "&success=1";
						return true;
					}else{
						String userId = o_empolyeeBO.get(empid).getUserid();
						o_empolyeeBO.delete(empid);
						if(StringUtils.isNotBlank(userId))
							o_sysUserBO.removeUser(userId);
					}
				}
			}
		}

		return true;
	}
	/**
	 * get方式删除员工.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/delete.do", method = RequestMethod.GET)
	public void g_delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try {
			String empid = request.getParameter("eid");
			String pid = request.getParameter("pid");
			String type = request.getParameter("type");
			
			if(null != type && !"".equals(type) && ("root".equals(type) || "org".equals(type))){
			
				List<SysEmpOrg> empOrgs = new ArrayList<SysEmpOrg>();
				empOrgs = o_sysEmpOrgBO.querySysEmpOrgByEmpid(empid);
				if(null != empOrgs && empOrgs.size()>1){
					//员工在多个机构下，不能删除员工，只删除员工与当前机构的关系
					o_sysEmpOrgBO.removeEmpOrg(pid, empid);
					return;
				}else if(null != empOrgs && empOrgs.size()==1){
					//员工在一个机构下，删除员工与当前机构的关系
					o_sysEmpOrgBO.removeEmpOrg(pid, empid);
					
					List<SysEmpPosi> empPosis = new ArrayList<SysEmpPosi>();
					empPosis = o_sysEmpPosiBO.querySysEmpPosiByEmpid(empid);
					//员工在岗位下存在，不能删除员工
					if(null != empPosis && empPosis.size()>0){
						return;
					}else{
						String userId = o_empolyeeBO.get(empid).getUserid();
						o_empolyeeBO.delete(empid);
						if(StringUtils.isNotBlank(userId))
							o_sysUserBO.removeUser(userId);
					}
				}
			
			}
			
			if(null != type && !"".equals(type) && "posi".equals(type)){
			
				List<SysEmpPosi> empPosisList = new ArrayList<SysEmpPosi>();
				empPosisList = o_sysEmpPosiBO.querySysEmpPosiByEmpid(empid);
				if(null != empPosisList && empPosisList.size()>1){
					//员工在多个岗位下，不能删除员工，只删除员工与当前岗位的关系
					o_sysEmpPosiBO.removeEmpPosi(pid, empid);
					return;
				}else if(null != empPosisList && empPosisList.size()==1){
					//员工在一个岗位下，删除员工与当前岗位的关系
					o_sysEmpPosiBO.removeEmpPosi(pid, empid);
					
					List<SysEmpOrg> empOrgsList = new ArrayList<SysEmpOrg>();
					empOrgsList = o_sysEmpOrgBO.querySysEmpOrgByEmpid(empid);
					//员工在机构下存在，不能删除员工
					if(null != empOrgsList && empOrgsList.size()>0){
						return;
					}else{
						String userId = o_empolyeeBO.get(empid).getUserid();
						o_empolyeeBO.delete(empid);
						if(StringUtils.isNotBlank(userId))
							o_sysUserBO.removeUser(userId);
					}
				}
			
			}
			
			flag="true";
			out.write(flag);
		}catch(Exception e){
			out.write(flag);
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	/**
	 * 查询员工的所有权限.
	 * @author 吴德福
	 * @param id
	 * @param model
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryroleauth.do")
	public void queryRoleAuth(String id,Model model) {
		SysUser user = o_sysUserBO.getSysUserByEmpId(id);
		model.addAttribute("roles", user.getSysRoles());
		model.addAttribute("auths", user.getSysAuthorities());
	}
	/**
	 * 给员工分配权限时保存员工Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/empAssignAuthority.do")
	public String empAssignAuthority(Model model,HttpServletRequest request){
		
		String empid = request.getParameter("id");
		EmpForm empForm = new EmpForm();
		if (empid != null) {
			SysEmployee sysEmployee = o_empolyeeBO.get(empid);
			BeanUtils.copyProperties(sysEmployee,empForm);
		}
		
		//进入角色时显示权限列表
		request.setAttribute("orgRoot", o_sysAuthorityBO.getRootOrg());
		model.addAttribute("empForm", empForm);
		return "sys/orgstructure/emp/empAssignAuthority";
	}
	/**
	 * ajax取得当前员工拥有的权限.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryAuthorityByEmpAjax.do")
	public void queryAuthorityAjax(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		out = response.getWriter();
		
		try {
			String empid = request.getParameter("eid");
			SysUser sysUser = o_sysUserBO.getSysUserByEmpId(empid);
			if(null != sysUser){
				List<String> authorityIds = o_sysUserBO.queryAuthorityByUser(sysUser.getId());
				String aid = "";
				int size = authorityIds.size();
				for(int i=0;i<authorityIds.size();i++){
					aid += authorityIds.get(i);
					if(i != size){
						aid += ",";
					}
				}
				out.write(aid);
			}else{
				out.write("NO");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.write("NO");
		}finally{
			out.close();
		}
	}
	/**
	 * 给员工分配权限.
	 * @author 吴德福
	 * @param status
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/empAssignAuthoritySubmit.do")
	public void empAssignAuthoritySubmit(SessionStatus status, HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String empid = request.getParameter("empid");
			String selectIds = request.getParameter("selectIds");
			String[] authorityIds = selectIds.substring(0,selectIds.length()-1).split(",");
			SysUser sysUser = o_sysUserBO.getSysUserByEmpId(empid);
			if(null != sysUser){
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
			}else{
				out.write("员工需要设置用户才能新增权限!");
			}
		} catch (DataIntegrityViolationException e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	/**
	 * 给员工分配角色时保存员工Form.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/empAssignRole.do")
	public String empAssignRole(Model model,HttpServletRequest request){
		String empid = request.getParameter("id");
		EmpForm empForm = new EmpForm();
		
		if (empid != null) {
			SysEmployee sysEmployee = o_empolyeeBO.get(empid);
			BeanUtils.copyProperties(sysEmployee,empForm);
			
			SysUser sysUser = o_sysUserBO.getSysUserByEmpId(empid);
			if(null != sysUser){
				String roleIds = "";
				for (SysRole role : sysUser.getSysRoles()) {
					roleIds = roleIds + role.getId()+",";
				}
				empForm.setRoleIds(roleIds);
			}
		}
		model.addAttribute("empForm", empForm);
		return "sys/orgstructure/emp/empAssignRole";
	}
	/**
	 * 给人员分配角色列表，分页
	 * @author 万业
	 * @param limit
	 * @param start
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sys/orgstructure/emp/empAssignRoleList.do")
	public Map<String, Object> empAssignRoleList(int limit, int start,String roleName,String roleCode){
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
		return map;
	}
	/**
	 * 给员工分配角色.
	 * @author 吴德福
	 * @param empForm 员工Form.
	 * @return String.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/empAssignRoleSubmit.do")
	public void empAssignRoleSubmit(SessionStatus status, HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		try {
			String empid = request.getParameter("empid");
			String selectIds = request.getParameter("selectIds");
			if(selectIds.length()>0){
				String[] roleIds = selectIds.substring(0,selectIds.length()-1).split(",");
				SysUser sysUser = o_sysUserBO.getSysUserByEmpId(empid);
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
			}
		} catch (DataIntegrityViolationException e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	/**
	 * ajax获取员工名empname是否可用.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmployeeByEmpname.do")
	public void queryEmployeeByEmpname(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		try {
            String empname = request.getParameter("empname");
            String operateType = request.getParameter("operateType");
            response.setContentType("text/html;charset=utf-8");
            String flag = "false";
            out = response.getWriter();
            
            List<SysEmployee> employeeList = o_empolyeeBO.queryEmployeeByEmpname(empname);
			if (employeeList != null && employeeList.size() > 0) {
				if("1".equals(operateType)){
					out.write(flag);
				}else{
					String empid = request.getParameter("id");
					SysEmployee employee = employeeList.get(0);
					if(empid.equals(employee.getId())){
						flag = "true";
						out.write(flag);
					}else{
						out.write(flag);
					}
				}
			}else{
				flag = "true";
				out.write(flag);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            out.close();
        }
	}
	/**
	 * ajax获取用户名username是否可用.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/queryUserByUsername.do")
	public void queryUserByUsername(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
        try {
            String username = request.getParameter("username");
            String operateType = request.getParameter("operateType");
            response.setContentType("text/html;charset=utf-8");
            String flag = "false";
            out = response.getWriter();
            
            List<SysUser> userList = o_sysUserBO.queryUnique(username);
			if (userList != null && userList.size() > 0) {
				if("3".equals(operateType)){
					out.write(flag);
				}else{
					String userid = request.getParameter("uid");
					if("".equals(userid) || null==userid){
						out.write(flag);
					}else{
						SysUser user = userList.get(0);
						if(userid.equals(user.getId())){
							flag = "true";
							out.write(flag);
						}else{
							out.write(flag);
						}
					}
				}
			}else{
				flag = "true";
				out.write(flag);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            out.close();
        }
	}
	/**
	 * Ext拖拽移动node.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/move.do")
	public void moveNode(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		
		String flag = "false";
        try {
            String currentId = StringUtils.substring(request.getParameter("currentId"), 0, 32);
            String pid = StringUtils.substring(request.getParameter("pid"), 0, 32);
            String targetId = StringUtils.substring(request.getParameter("targetId"), 0, 32);
            String ptype = request.getParameter("ptype");
            String type = request.getParameter("type");
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            
			if (o_empolyeeBO.moveNode(currentId,pid,targetId,ptype,type)) {
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
	 * Ext拖拽复制node.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/copy.do")
	public void copyNode(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out = null;
		 String flag = "false";
        try {
            String currentId = StringUtils.substring(request.getParameter("currentId"), 0, 32);
            String targetId = StringUtils.substring(request.getParameter("targetId"), 0, 32);
            String type = request.getParameter("type");
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            
			if (o_empolyeeBO.copyNode(currentId,targetId,type)) {
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
	 * 设置员工的组织归属.
	 * @author 吴德福
	 * @param id 员工id.
	 * @param success
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/empAssignShip.do")
	public String empAssignShip(String id,String success,Model model){
		EmpForm empForm = new EmpForm();
		List<SysEmpOrg> empOrgList = o_sysEmpOrgBO.querySysEmpOrgByEmpid(id);
		model.addAttribute("empOrgList", empOrgList);
		model.addAttribute("empForm", empForm);
		return "sys/orgstructure/emp/empAssignShip";
	}
	/**
	 * 设置员工组织归属查询员工岗位关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmpPosiJson.do")
	public Map<String, Object> queryEmpPosiJson(HttpServletRequest request, HttpServletResponse response){
		String empid = request.getParameter("empid");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		List<SysEmpPosi> empPosiList = o_sysEmpPosiBO.querySysEmpPosiByEmpid(empid);
		for(SysEmpPosi empPosi : empPosiList){
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", empPosi.getId());
			node.put("positionName", empPosi.getSysPosition().getPosiname());
			if(empPosi.getIsmain()){
				node.put("isMainPosition", "是");
			}else{
				node.put("isMainPosition", "否");
			}
			data.add(node);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datas", data);
		map.put("totalCount", empPosiList.size());
		return map;
	}
	/**
	 * 删除员工岗位关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/delEmpPosi.do")
	public void delEmpPosi(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			String empPosiIds = request.getParameter("empPosiIds");
			for(String empPosiId : empPosiIds.substring(0, empPosiIds.length()-1).split(",")){
				o_sysEmpPosiBO.removeEmpPosiById(empPosiId);
			}
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
	 * 更新员工岗位关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/updateEmpPosi.do")
	public void updateEmpPosi(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			String empPosiIds = request.getParameter("empPosiIds");
			String[] empPosis = empPosiIds.substring(0, empPosiIds.length()-1).split(",");
			for(String empPosi : empPosis){
				String empPosiId = empPosi.split(";")[0];
				String isMainPosition = empPosi.split(";")[1];
				SysEmpPosi sysEmpPosi = o_sysEmpPosiBO.querySysEmpPosiById(empPosiId);
				if("是".equals(isMainPosition)){
					sysEmpPosi.setIsmain(true);
				}else{
					sysEmpPosi.setIsmain(false);
				}
				o_sysEmpPosiBO.updateEmpPosi(sysEmpPosi);
			}
			
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
	 * 设置员工组织归属查询员工机构关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/orgstructure/emp/queryEmpOrg.do")
	public Map<String, Object> queryEmpOrg(HttpServletRequest request, HttpServletResponse response){
		String empid = request.getParameter("empid");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		List<SysEmpOrg> empOrgList = o_sysEmpOrgBO.querySysEmpOrgByEmpid(empid);
		for(SysEmpOrg empOrg : empOrgList){
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", empOrg.getId());
			node.put("orgName", empOrg.getSysOrganization().getOrgname());
			if(empOrg.getIsmain()){
				node.put("isMainOrg", "是");
			}else{
				node.put("isMainOrg", "否");
			}
			node.put("orgType", o_dictEntryBO.queryDictEntryById(empOrg.getSysOrganization().getOrgType()).getName());
			data.add(node);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datas", data);
		map.put("totalCount", empOrgList.size());
		return map;
	}
	/**
	 * 删除员工机构关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/delEmpOrg.do")
	public void delEmpOrg(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			String empOrgIds = request.getParameter("empOrgIds");
			for(String empOrgId : empOrgIds.substring(0, empOrgIds.length()-1).split(",")){
				o_sysEmpOrgBO.removeEmoOrgById(empOrgId);
			}
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
	 * 更新员工机构关系.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/emp/updateEmpOrg.do")
	public void updateEmpOrg(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			
			String empOrgIds = request.getParameter("empOrgIds");
			String[] empOrgs = empOrgIds.substring(0, empOrgIds.length()-1).split(",");
			for(String empOrg : empOrgs){
				String empOrgId = empOrg.split(";")[0];
				String isMainOrg = empOrg.split(";")[1];
				SysEmpOrg sysEmpOrg = o_sysEmpOrgBO.querySysEmpOrgById(empOrgId);
				if("是".equals(isMainOrg)){
					sysEmpOrg.setIsmain(true);
				}else{
					sysEmpOrg.setIsmain(false);
				}
				o_sysEmpOrgBO.updateEmpOrg(sysEmpOrg);
			}
			
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
	 * 根据查询内容得到员工树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @throws IOException
	 * @since fhd Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/emp/getPathsBySearchName.do")
	public String getPathsBySearchName(String searchName,HttpServletRequest request,HttpServletResponse response) throws IOException {
 		 List<String>  pathList=o_empolyeeBO.getPathsBySearchName(searchName);
 		 StringBuilder sbd=new StringBuilder();
 		 for(String path:pathList){
 			 sbd.append(",").append(path);
 		 }
 		 if(sbd.length()==0){
 			return "1";
 		 }
		 return sbd.toString().substring(1);
	}
	
	// XXX 新系统方法
	/**
	 * 
	 * <pre>
	 * 根据组织或岗位id查询员工
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param orgid
	 * @param start
	 * @param limit
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/emp/findempsbyorgid/{orgid}")
	public Map<String, Object> findEmpsByOrgId(@PathVariable("orgid") String orgid,int start, int limit,SysEmployee employee){
		com.fhd.core.dao.Page<SysEmployee> page = new com.fhd.core.dao.Page<SysEmployee>();
		page.setPageNo((limit == 0 ? 0 : start/limit) + 1);
		page.setPageSize(limit);
		//分页性能问题，改为不分页显示
		//o_empolyeeBO.querySysAllEmployeeByOrgId(page , orgid, employee);
		Set<SysEmployee> empList = o_empolyeeBO.findAllEmployeeBySome(orgid,employee);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (SysEmployee emp : empList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", emp.getId());
			row.put("empno", emp.getEmpcode());
			row.put("empname", emp.getEmpname());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * <pre>
	 * 根据组织或岗位id查询员工，无分页
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgid 机构ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/sys/emp/findempsbyorgidwithonpage/{orgid}")
	public Map<String, Object> findEmpsByOrgIdWidthNoPage(@PathVariable("orgid") String orgid){
		Set<SysEmployee> empList = o_empolyeeBO.findAllEmployeeBySome(orgid,null);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (SysEmployee emp : empList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", emp.getId());
			row.put("empno", emp.getEmpcode());
			row.put("empname", emp.getEmpname());
			datas.add(row);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", empList.size());
		map.put("datas", datas);
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping("/sys/emp/findempsbyposiid/{posiid}")
	public Map<String, Object> findEmpsByPosiId(@PathVariable("posiid") String posiid,int start, int limit,SysEmployee employee) {
		
		com.fhd.core.dao.Page<SysEmployee> page = new com.fhd.core.dao.Page<SysEmployee>();
		page.setPageNo((limit == 0 ? 0 : start/limit) + 1);
		page.setPageSize(limit);
		o_empolyeeBO.querySysEmpByPosiIdPage(page , posiid, employee);
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		for (SysEmployee emp : page.getResult()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", emp.getId());
			row.put("empno", emp.getEmpcode());
			row.put("empname", emp.getEmpname());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		
		return map;
	}
		
	
	
	/**
	 * 
	 * <pre>
	 * 根据角色id查询员工
	 * 改为不分页的，填加本公司的过滤条件
	 * </pre>
	 * 
	 * @author 胡迪新,张雷
	 * @param roleid
	 * @param start
	 * @param limit
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/emp/findempsbyroleid/{roleid}")
	public  Map<String, Object> findEmpsByRoleId(@PathVariable("roleid") String roleid,int start, int limit,SysEmployee employee){
		com.fhd.core.dao.Page<SysEmployee> page = new com.fhd.core.dao.Page<SysEmployee>();
		page.setPageNo((limit == 0 ? 0 : start/limit) + 1);
		page.setPageSize(limit);
		//o_empolyeeBO.findEmpByRoleId(page , roleid, employee);
		List<SysEmployee> empList = o_empolyeeBO.findAllEmpByRoleId(UserContext.getUser().getCompanyid(), roleid, employee);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		for (SysEmployee emp : empList) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", emp.getId());
			row.put("empno", emp.getEmpcode());
			row.put("empname", emp.getEmpname());
			datas.add(row);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		
		return map;
	}
	
	
	
	
}

