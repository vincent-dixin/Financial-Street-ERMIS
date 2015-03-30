/**
 * DictTreeControl.java
 * com.fhd.sys.web.controller.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		翟辉
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.autho;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.autho.SysoAuthorityBO;
import com.fhd.sys.business.autho.SysoRoleBO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;

@Controller
public class SysoAuthorityControl {
	@Autowired
	private SysoAuthorityBO o_sysoAuthorityBO;

	@Autowired
	private SysoRoleBO o_sysoRoleBO;
	/**
     * 权限树获得所有节点，除根节点
     * @author 翟辉
     * @param id 节点id
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping("/sys/autho/authorityTreeLoader.f")
    public List<Map<String, Object>> authorityTreeLoader(String node,String query, String id,String authorityType)
    {
        return o_sysoAuthorityBO.treeLoader(node,query,id,authorityType);
    }
    /**
     * 权限树根节点获得
     * @author 翟辉
     * @param id 根节点节点id
     * @param query 查询条件
     * @return
     * @since  fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/sys/autho/findTreeRoot.f")
    public Map<String, Object> findTreeRoot(String node,String query){ 
        Map<String, Object> rootNode = new HashMap<String, Object>();
        Map<String, Object> items = new HashMap<String, Object>(); // 存放
        SysAuthority root = o_sysoAuthorityBO.findAuthorityTreeRoot();
        rootNode.put("id",root.getId());
        rootNode.put("text", root.getAuthorityName());
        rootNode.put("leaf", false);
        //rootNode.put("checked", true);
        rootNode.put("expanded", false);
        items.put("authorty", rootNode);
        return items;
        
    }
    /**
	 * 取得当前选中的角色拥有的权限.
	 * @author 翟辉
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 *//*
	@ResponseBody
	@RequestMapping(value = "/sys/autho/queryAuthorityByRoleAjax.f")
	public Map<String, Object> queryAuthorityAjax(String id){
		Map<String,Object> result = new HashMap<String, Object>();
		String aid = "";
			List<String> authorityIds = o_sysoAuthorityBO.queryAuthorityByRole(id);
			int size = authorityIds.size();
			for(int i=0;i<authorityIds.size();i++){
				aid += authorityIds.get(i);
				if(i != size){
					aid += ",";
				}
			}
		result.put("aid", aid);	
		return result;
	}*/
	/**
	 *  权限树获得所有节点，除根节点权限树显示
	 * @param node 根id
	 * @author 翟辉
	 * @param id   
	 * @param query 查询
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/sys/autho/showTreeLoader.f")
    public List<Map<String, Object>> showTreeLoader(String node,String id,String query,String authorityType)
    {
    	
        return o_sysoAuthorityBO.treeLoaderShow(node,id,query,authorityType);
    }
	
	/**
	 * 给角色分配权限(权限保存)
	 * @author 翟辉
	 * @param id
	 * @param roleItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/roleAssignAuthority.f")
	public Map<String, Object> roleAssignAuthority(String id,String roleItem,String authorityType) {
		Map<String,Object> result = new HashMap<String, Object>();
		JSONArray jsarr = JSONArray.fromObject(roleItem);
		if(authorityType.equals("role")){
			SysRole sysRole = o_sysoRoleBO.queryRoleById(id);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>();
			for (int i = 0; i < jsarr.size(); i++){
				 SysAuthority authority = new SysAuthority();
				 JSONObject jsobj = jsarr.getJSONObject(i);
				 String  authorityId = jsobj.getString("checkId");
				 authority = o_sysoAuthorityBO.queryAuthorityById(authorityId);
				 authorities.add(authority);
				 //role.getSysUsers().add(sysUser);
			}
			Set<SysAuthority> authoritiesOld = sysRole.getSysAuthorities();//得到所有关联数据
			for(SysAuthority authority: authoritiesOld){
				 authorities.add(authority);
			}
			sysRole.setSysAuthorities(authorities);
			o_sysoRoleBO.mergeRole(sysRole);
		}
		if(authorityType.equals("user")){
			SysUser sysUser = o_sysoRoleBO.queryUserById(id);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>();
			for (int i = 0; i < jsarr.size(); i++){
				 SysAuthority authority = new SysAuthority();
				 JSONObject jsobj = jsarr.getJSONObject(i);
				 String  authorityId = jsobj.getString("checkId");
				 authority = o_sysoAuthorityBO.queryAuthorityById(authorityId);
				 authorities.add(authority);
				 //role.getSysUsers().add(sysUser);
			}
			Set<SysAuthority> authoritiesOld = sysUser.getSysAuthorities();//得到所有关联数据
			for(SysAuthority authority: authoritiesOld){
				 authorities.add(authority);
			}
			sysUser.setSysAuthorities(authorities);
			o_sysoRoleBO.mergeUser(sysUser);
		}
		
		String resultOk = "ok";
		result.put("resultOk", resultOk);
		return result;
	}

	/**
	 * 给角色分配权限(权限删除)
	 * @author 翟辉
	 * @param id
	 * @param roleItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/autho/deleteRoleAssignAuthority.f")
	public Map<String, Object> deleteRoleAssignAuthority(String id,String roleItem,String authorityType) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		
		JSONArray jsarr = JSONArray.fromObject(roleItem);
		if(authorityType.equals("role")){
			SysRole sysRole = o_sysoRoleBO.queryRoleById(id);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>();
			Set<SysAuthority> authoritiesOld = sysRole.getSysAuthorities();//得到所有关联数据
			for(SysAuthority authority: authoritiesOld){
				 authorities.add(authority);
			}
			for (int i = 0; i < jsarr.size(); i++){
				 SysAuthority authority = new SysAuthority();
				 JSONObject jsobj = jsarr.getJSONObject(i);
				 String  authorityId = jsobj.getString("checkId");
				 if(o_sysoAuthorityBO.queryAuthorityIdIsTure(authorityId,id,jsarr,authorityType)){
					 authority = o_sysoAuthorityBO.queryAuthorityById(authorityId);
					 authorities.remove(authority);
				 }
				 
			}
			sysRole.setSysAuthorities(authorities);
			o_sysoRoleBO.mergeRole(sysRole);
		}
		if(authorityType.equals("user")){
			SysUser sysUser = o_sysoRoleBO.queryUserById(id);
			Set<SysAuthority> authorities= new HashSet<SysAuthority>();
			Set<SysAuthority> authoritiesOld = sysUser.getSysAuthorities();//得到所有关联数据
			for(SysAuthority authority: authoritiesOld){
				 authorities.add(authority);
			}
			for (int i = 0; i < jsarr.size(); i++){
				 SysAuthority authority = new SysAuthority();
				 JSONObject jsobj = jsarr.getJSONObject(i);
				 String  authorityId = jsobj.getString("checkId");
				 if(o_sysoAuthorityBO.queryAuthorityIdIsTure(authorityId,id,jsarr,authorityType)){
					 authority = o_sysoAuthorityBO.queryAuthorityById(authorityId);
					 authorities.remove(authority);
				 }
				 
			}
			sysUser.setSysAuthorities(authorities);
			o_sysoRoleBO.mergeUser(sysUser);
		}
		String resultOk = "ok";
		result.put("resultOk", resultOk);
		return result;
	}
	
}