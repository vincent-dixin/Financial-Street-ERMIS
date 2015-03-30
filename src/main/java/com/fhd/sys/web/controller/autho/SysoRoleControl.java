/**
 * DictTreeControl.java
 * com.fhd.sys.web.controller.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.autho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.business.autho.SysoRoleBO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.web.form.autho.SysoRoleForm;
import com.fhd.sys.web.form.autho.SysoUserForm;


@Controller
public class SysoRoleControl {
    @Autowired
    private SysoRoleBO o_sysoRoleBO;

    /**
     * findDictTypeBySome:角色树
     * @author 翟辉
     * @param query 查询条件
     * @return Map<String, Object>
     */
    @ResponseBody
    @RequestMapping(value = "/sys/autho/findDictTypeBySome.f")
    public Map<String, Object> findDictTypeBySome(String query) {
    	return o_sysoRoleBO.treeLoader(query);
    }
    
    /**
	 * 初始化Grid(员工列表)
	 * @author 翟辉
	 * @param start 开始页码
	 * @param limit 分页码
	 * @param query 查询条件
	 * @param sort 排序字段
	 * @return Map<String, Object>
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/autho/findrolePage.f")
	public Map<String, Object> findrolePage(int start, int limit, String query, String sort,String roleId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysoUserForm> datas = new ArrayList<SysoUserForm>();
		Page<SysEmployee> page = o_sysoRoleBO.findrolePage(start, limit, query, sort, roleId);
		List<SysEmployee> sysEmployeeList = page.getResult();
		for (SysEmployee sysEmployee : sysEmployeeList) {
			String posiName = "";//岗位
			String deptName = "";//部门
			String companyname = "";//公司
			String username = sysEmployee.getUsername();//用户名
			String userId = sysEmployee.getUserid();//用户Id
			String empname  = sysEmployee.getEmpname();//员工姓名
			if(null!=sysEmployee.getSysOrganization()){
				companyname = sysEmployee.getSysOrganization().getOrgname();
				
			}
			
			Set<SysEmpPosi> sysEmpPosis =  sysEmployee.getSysEmpPosis();
			for (SysEmpPosi sysEmpPosi : sysEmpPosis) {
				posiName = sysEmpPosi.getSysPosition().getPosiname();
			}
			Set<SysEmpOrg> sysEmpOrgs = sysEmployee.getSysEmpOrgs();
			for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
				deptName = sysEmpOrg.getSysOrganization().getOrgname();
			}
			datas.add(new SysoUserForm(posiName,deptName,username,empname,companyname,roleId,userId));
			
		}
		/*List<SysUser> sysUserList = page.getResult();
		List<SysoUserForm> datas = new ArrayList<SysoUserForm>();
		SysRole sysRole = o_sysoRoleBO.queryRoleById(roleId);
		for(SysUser sysUser : sysUserList){
			
			String sysOrganizationName = o_sysoRoleBO.queryOrgByUserId(sysUser);//得到公司机构
			String sysPositionName = o_sysoRoleBO.queryPosiByUserId(sysUser);//得到岗位
			
			
			//String sysOrganizationName = o_sysoRoleBO.queryOrgByUserId(sysUser,allSysEmployeeItem);
			//String sysPositionName="";
			datas.add(new SysoUserForm(sysUser,sysRole,sysOrganizationName,sysPositionName));
		}*/
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
	
		return map;
	}
	
	/**
	 * 添加&修改角色(包括人员添加)
	 * @author 翟辉
	 * @param roleForm 表单
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/saveRole.f")
	public Map<String, Object> saveRole(SysoRoleForm roleForm,String id){	
		Map<String, Object> result = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(id)){//修改
				SysRole sysRoleOld = o_sysoRoleBO.queryRoleById(id);
				sysRoleOld.setRoleCode(roleForm.getRoleCode());
				sysRoleOld.setRoleName(roleForm.getRoleName());
				sysRoleOld.setId(id);
				o_sysoRoleBO.mergeRole(sysRoleOld);
				result.put("success", true);
			}else{//保存(添加)
				SysRole sysRole = new SysRole();
				sysRole.setRoleCode(roleForm.getRoleCode());
				sysRole.setRoleName(roleForm.getRoleName());
				List<SysRole> sysRoleList1 = o_sysoRoleBO.isExistByRoleCode(roleForm.getRoleCode());
				if(null != sysRoleList1 && sysRoleList1.size()>0){
					result.put("addrole", "roleCodeisExist");
					return result;
				}
				//根据角色名称判断角色是否存在.
				List<SysRole> sysRoleList2 =o_sysoRoleBO.isExistByRoleName(roleForm.getRoleName());
				if(null != sysRoleList2 && sysRoleList2.size()>0){
					result.put("addrole", "roleNameisExist");
					return result;
				}
				String makeId = Identities.uuid();
				sysRole.setId(makeId);
				o_sysoRoleBO.saveRole(sysRole);
				/*if(roleArrayObject!=null){
					o_sysoRoleBO.addUserRole(makeId, roleArray);//添加人员
				}*/
				result.put("success", true);
			}        
			return result;	
	}
	
	/**
	 * 删除角色
	 * @author 翟辉
	 * @param roleForm
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/delRole.f")
	public boolean deleteRole(SysoRoleForm roleForm,String roleId) {
		o_sysoRoleBO.removeRole(roleId);
		return true;
	}
	
	/**
	 * 修改角色（前台显示）
	 * @author 翟辉
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/findRoleById.f")
	public Map<String, Object> findRoleById(String id) {
		SysRole sysRole = new SysRole();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> inmap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(id)){	
			sysRole.setId(id);
		}
		SysRole role = o_sysoRoleBO.queryRoleById(sysRole.getId());
		
		inmap.put("roleCode", role.getRoleCode());
		inmap.put("roleName", role.getRoleName());
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }

	/**
	 *删除角色下的人员
	 * @author 翟辉
	 * @param roleItem
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/delUserAndRole.f")
	public Map<String, Object> delUserAndRole(String roleItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		o_sysoRoleBO.deleteRoleUser(roleItem);	
		map.put("success", true);
		return map;
    }
	
	
	/**
	 *	添加角色下的人员
	 * @author 翟辉
	 * @param roleItem
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/addUserAndRole.f")
	public Map<String, Object> addUserAndRole(String id,String roleItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		o_sysoRoleBO.addRoleUser(id,roleItem);
		map.put("success", true);	
		return map;	
    }

}