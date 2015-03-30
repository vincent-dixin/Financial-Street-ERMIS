package com.fhd.sys.web.controller.group;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.utils.Identities;
import com.fhd.sys.business.group.GroupBO;
import com.fhd.sys.entity.group.SysGroup;
import com.fhd.sys.web.form.group.GroupForm;

/**
 * 工作组
 * 
 * @ClassName GroupControl.java
 * @Version 1.0
 * @author zhaotao
 * @Date 2011-1-19
 */
@Controller
@SessionAttributes(types = GroupForm.class)
public class GroupControl {
	@Autowired
	private GroupBO o_groupBO;

	/**
	 * 进入工作组管理页面
	 * 
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("/sys/group/groupStructure.do")
	public String g_groupStructure(HttpServletRequest request)throws Exception {
		SysGroup root = o_groupBO.getRoot();
		request.setAttribute("root", root);
		return "sys/group/groupStructure";
	}

	/**
	 * 根据ID获取子节点.
	 * @param requet
	 * @param response
	 * @return List<Map<String,Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/group/loadTree.do")
	public List<Map<String, Object>> loadMenuTree(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("node");
		return o_groupBO.loadSysGroupTree(id,request.getContextPath());
	}
	/**
	 * 跳转到下级工作组页面.
	 * @param id
	 * @param request
	 * @return String
	 * @return String
	 */
	@RequestMapping("/sys/group/nextGroup.do")
	public String g_nextGroupList(String id, HttpServletRequest request) {
		request.setAttribute("id", id);
		return "sys/group/nexGroupList";
	}
	/**
	 * 根据ID获取下级工作组数据.
	 * @param id
	 * @param groupName
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/sys/group/nextGroupList.do")
	public Map<String, Object> g_nextGroupListData(String id,String groupName) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		SysGroup sysGroup = o_groupBO.get(id);
		if(null != sysGroup){
			Set<SysGroup> sysGroups = sysGroup.getSubGroups();
			if(null != sysGroups && sysGroups.size()>0){
				for (SysGroup group : sysGroups) {
					if(null!=groupName && group.getGroupName().indexOf(groupName)<0){
						continue;
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Map<String, String> record = new HashMap<String, String>();
					record.put("id", group.getId());
					record.put("groupCode", group.getGroupCode());
					record.put("groupName", group.getGroupName());
					record.put("groupLevel", String.valueOf(group.getGroupLevel()));
					if(null != group.getStartDate()){
						record.put("startDate", sdf.format(group.getStartDate()));
					}
					if(null != group.getEndDate()){
						record.put("endDate", sdf.format(group.getEndDate()));
					}
					if(null != group.getGroupDesc()){
						record.put("groupDesc", group.getGroupDesc());
					}else{
						record.put("groupDesc", "");
					}
					reList.add(record);
				}
			}
		}
		
		returnMap.put("totalCount", reList.size());
		returnMap.put("datas", reList);
		return returnMap;
	}

	/**
	 * 进入工作组管理tabs页面
	 * 
	 * @return
	 * @return String
	 */
	@RequestMapping("/sys/group/tabs.do")
	public String g_tabs() {
		return "sys/group/tabs";
	}

	/**
	 * 进入工作组管理编辑页面.
	 * @param model
	 * @param id
	 * @return String
	 * @return String
	 * @throws ParseException 
	 */
	@RequestMapping("/sys/group/edit.do")
	public String g_edit(Model model, String id) throws ParseException {
		GroupForm form = new GroupForm();
		SysGroup group = o_groupBO.get(id);
		if(null!=group){
			BeanUtils.copyProperties(group, form);
			if(null != group.getParentGroup()){
				model.addAttribute("parentId",group.getParentGroup().getId());
			}
		}
	
		model.addAttribute("groupForm", form);
		return "sys/group/edit";
	}

	/**
	 * 修改保存工作组.
	 * @param form
	 * @param result
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/sys/group/save.do")
	public void p_save(GroupForm form,BindingResult result,HttpServletResponse response){
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			
			SysGroup group=null;
			if(StringUtils.isNotBlank(form.getId())){
				group = o_groupBO.get(form.getId());
				BeanUtils.copyProperties(form, group);
			}else{
				group = new SysGroup();
				BeanUtils.copyProperties(form, group);
				group.setId(Identities.uuid());
			}
			
			o_groupBO.save(group);
			out.write("true");
		}catch(Exception e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}

	@RequestMapping("/sys/group/add.do")
	public String g_add(Model model) {
		GroupForm form = new GroupForm();
		model.addAttribute("groupForm", form);
		return "/sys/group/add";
	}

	/**
	 * 新增保存
	 * 
	 * @param form
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("/sys/group/addSave.do")
	public String p_add(GroupForm form,BindingResult result) {
		o_groupBO.add(form);
		return "true";
	}

	/**
	 * 删除工作组
	 * @param ids
	 */
	@ResponseBody
	@RequestMapping("/sys/group/delete.do")
	public void p_delete(String ids,HttpServletResponse response) {
		PrintWriter out = null;
		String flag = "false";
		String[] idss = ids.split(",");
		try{
			out=response.getWriter();
			if (1==idss.length&&o_groupBO.getNextGroup(idss[0]).size()<1) {
				o_groupBO.delele(idss);
				flag = "true";
			}else if(1<idss.length){
			for (String id : idss) {
				if (o_groupBO.getNextGroup(id).size()<1||o_groupBO.getNextGroup(id).isEmpty()) {
					o_groupBO.deleteSingle(id);
				}
			}
			flag = "true";
			}
			out.write(flag);
		}catch(Exception e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();			
		}
	}

	/**
	 * 工作组相关角色列表
	 * 
	 * @param ids
	 * @return
	 * @return String
	 */
	@RequestMapping("/sys/group/roleListPage.do")
	public String g_roleListPage() {
		return "/sys/group/nexRoleList";
	}

	/**
	 * 工作组相关角色列表-数据
	 * 
	 * @param ids
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("/sys/group/roleList.do")
	public Map<String, Object> g_roleList(String id) {
		return this.o_groupBO.getRelativeRole(id);
	}

	/**
	 * 增加角色
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sys/group/addRoles.do")
	public boolean p_addRoles(String id, String ids) {
		this.o_groupBO.addRelativeRole(id, ids);
		return true;
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sys/group/deleteRoles.do")
	public boolean p_deleteRoles(String id, String ids) {
		this.o_groupBO.deleteRelativeRole(id, ids);
		return true;
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/sys/group/assignAuthority.do")
	public String g_assignAuthority(HttpServletRequest request, String id) {
		String selects = this.o_groupBO.selectedAuthority2String(id);
		request.setAttribute("selectedAuth", selects);
		return "/sys/group/assignAuthority";
	}

	/**
	 * 处理权限添加
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sys/group/saveAuthority.do")
	public boolean p_assignAuthority(HttpServletRequest request, String id, String ids) {
		this.o_groupBO.saveAuthority(id, ids);
		return true;
	}

	@ResponseBody
	@RequestMapping("/sys/group/vcode.do")
	public boolean vcode(String groupCode) {
		if (this.o_groupBO.queryGroupByCode(groupCode) != null)
			return false;
		else
			return true;
	}

	/**
	 * 工作组相关权限树
	 * 
	 * @param ids
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("/sys/group/authTree.do")
	public List<Map<String, Object>> g_authTree(String id) {
		return this.o_groupBO.getAuthority(id);
	}

}
