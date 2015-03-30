/**
 * PortalController.java
 * com.fhd.fdc.commons.web.controller.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.portal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysRoleBO;
import com.fhd.sys.business.content.ContentPublishBO;
import com.fhd.sys.business.portal.PortalBO;
import com.fhd.sys.business.portal.PortletBO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.content.ContentPublish;
import com.fhd.sys.entity.portal.Portal;
import com.fhd.sys.entity.portal.PortalPortlet;
import com.fhd.sys.entity.portal.Portlet;
import com.fhd.sys.web.form.portal.PortalForm;

/**
 * PortalController类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-20
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = PortalForm.class)
public class PortalControl {

	@Autowired
	private PortalBO o_portalBO;
	@Autowired
	private PortletBO o_portletBO;
	@Autowired
	private ContentPublishBO o_contentPublishBO;
	@Autowired
	private SysRoleBO o_sysRoleBO;
	
	/**
	 * 查询首页portal发布的新闻.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/newsPublish.do")
	public String newsPublish(Model model,HttpServletRequest request){
		List<ContentPublish> newsList = o_contentPublishBO.queryContentsByType("新闻");
		model.addAttribute("newsList", newsList);
		return "sys/portal/news";
	}
	/**
	 * 根据新闻id查询新闻详细.
	 * @author 吴德福
	 * @param id
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/newsInfo.do")
	public String queryNewsInfo(String id,Model model){
		model.addAttribute("news", o_contentPublishBO.getContentById(id));
		return "sys/portal/newsInfo";
	}
	/**
	 * 根据公告id查询公告详细.
	 * @author 吴德福
	 * @param id
	 * @param model
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/advicesInfo.do")
	public String queryAdvicesInfo(String id,Model model){
		model.addAttribute("advices", o_contentPublishBO.getContentById(id));
		return "sys/portal/advicesInfo";
	}
	/**
	 * 查询首页portal发布的公告.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/advicesPublish.do")
	public String advicesPublish(Model model,HttpServletRequest request){
		List<ContentPublish> advicesList = o_contentPublishBO.queryContentsByType("公告");
		model.addAttribute("advicesList", advicesList);
		return "sys/portal/advices";
	}
	/**
	 * 初始化面板布局显示页面。
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sys/portal/queryUse.do")
	public String queryUsePortalByUserid(HttpServletRequest request,HttpServletResponse response){
       	Portal portal = o_portalBO.queryUsePortalsByUserid(UserContext.getUserid());
       	request.setAttribute("portal", portal);
       	if(portal.getCol()==1&&portal!=null&&portal.getSubPortalPortlet()!=null&&portal.getSubPortalPortlet().size()==1){
       		for (PortalPortlet portalPortlet : portal.getSubPortalPortlet()) {
       			if(portalPortlet!=null&&portalPortlet.getPortlet()!=null&&portalPortlet.getPortlet().getUrl()!=null&&portalPortlet.getPortlet().getUrl().length()>0){
       				String url="redirect:"+portalPortlet.getPortlet().getUrl();
       				return url;
       			}
			}
       	}
       	return "sys/portal/portalShow";
	}
	/**
	 * 初始化面板布局管理页面。
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sys/portal/portalManage.do")
	public String portalManage(HttpServletRequest request,HttpServletResponse response){
		List<Portlet> portlets = o_portletBO.queryAllPortlet();
		Portal portal = o_portalBO.queryUsePortalsByUserid(UserContext.getUserid());
		request.setAttribute("portal", portal);
		request.setAttribute("portlets", portlets);
		return "sys/portal/portalManage";
	}
	/**
	 * 更改面板布局。
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sys/portal/edit.do")
	public void editPortal(String portalPortletsJson,String portalsJson,HttpServletResponse response) throws Exception{
		JSONArray portalPortlets=JSONArray.fromObject(portalPortletsJson);
		JSONArray portals=JSONArray.fromObject(portalsJson);
		JSONObject portal1=(JSONObject)portals.get(0);
		Portal portal=new Portal();
		portal.setId(portal1.getString("id"));
		portal.setRow(portal1.getInt("row"));
		portal.setCol(portal1.getInt("col"));
		portal.setUpdateTime(new Date());
		SysUser sysUser=new SysUser();
		sysUser.setId(UserContext.getUserid());
		portal.setSysUser(sysUser);
		portal.setOperator(UserContext.getUsername());
		
		Set<PortalPortlet> subPortalPortlet=new HashSet<PortalPortlet>();
		if(portalPortlets.size()>0){
			PortalPortlet portalPortlet=null;
			JSONObject portalPortletJson=null;
			for(int i=0;i<portalPortlets.size();i++){
				portalPortletJson=(JSONObject)portalPortlets.get(i);
				portalPortlet = new PortalPortlet();
				Portlet portlet=new Portlet();
				portalPortlet.setId(Identities.uuid());
				portlet.setId(portalPortletJson.getString("portletId"));
				portalPortlet.setPortlet(portlet);
				portalPortlet.setOperator(UserContext.getUsername());
				portalPortlet.setColNo(portalPortletJson.getInt("colNo"));
				portalPortlet.setRowNo(portalPortletJson.getInt("rowNo"));
				portalPortlet.setUpdateTime(new Date());
				portalPortlet.setPortal(portal);
				portalPortlet.setOperator(UserContext.getUserid());
				subPortalPortlet.add(portalPortlet);
			}
		}
		portal.setSubPortalPortlet(subPortalPortlet);
		o_portalBO.saveOrUpdatePortal(portal);
	}
	/**
	 * 初始化系统面板布局管理页面。
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sys/portal/sysPortalManage.do")
	public String sysPortalManage(HttpServletRequest request,HttpServletResponse response){
		List<Portlet> portlets = o_portletBO.queryAllPortlet();
		Portal portal = o_portalBO.querySysPortal();
		request.setAttribute("portal", portal);
		request.setAttribute("portlets", portlets);
		return "sys/portal/sysPortalManage";
	}

	/**
	 * 更改系统面板布局。
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sys/portal/editSys.do")
	public void editSysPortal(String portalPortletsJson,String portalsJson,HttpServletResponse response) throws Exception{
		JSONArray portalPortlets=JSONArray.fromObject(portalPortletsJson);
		JSONArray portals=JSONArray.fromObject(portalsJson);
		JSONObject portal1=(JSONObject)portals.get(0);
		Portal portal=o_portalBO.querySysPortal();
		portal.setRow(portal1.getInt("row"));
		portal.setCol(portal1.getInt("col"));
		portal.setUpdateTime(new Date());
		portal.setOperator(UserContext.getUserid());
		
		Set<PortalPortlet> subPortalPortlet=new HashSet<PortalPortlet>();
		if(portalPortlets.size()>0){
			PortalPortlet portalPortlet=null;
			JSONObject portalPortletJson=null;
			for(int i=0;i<portalPortlets.size();i++){
				portalPortletJson=(JSONObject)portalPortlets.get(i);
				portalPortlet = new PortalPortlet();
				Portlet portlet=new Portlet();
				portlet.setId(portalPortletJson.getString("portletId"));
				portalPortlet.setPortlet(portlet);
				portalPortlet.setOperator(UserContext.getUsername());
				portalPortlet.setColNo(portalPortletJson.getInt("colNo"));
				portalPortlet.setRowNo(portalPortletJson.getInt("rowNo"));
				portalPortlet.setUpdateTime(new Date());
				portal.setId(portalPortletJson.getString("portalId"));
				portalPortlet.setPortal(portal);
				subPortalPortlet.add(portalPortlet);
			}
		}
		portal.setSubPortalPortlet(subPortalPortlet);
		o_portalBO.updatePortal(portal);
	}
	/**
	 * 删除用户面板布局
	 * @author 杨鹏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sys/portal/UserPortalRemove.do")
	public void UserPortalRemove(HttpServletRequest request,HttpServletResponse response) throws Exception{
		o_portalBO.removeUserPortal();
	}
	/**
	 * 角色分配首页跳转页面.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/roleAssignPortalManage.do")
	public String roleAssignPortalManage(HttpServletRequest request,String roleId){
		List<Portlet> portlets = o_portletBO.queryAllPortlet();
		//如果该角色下已经配置过首页，则读取配置过的首页portal信息，否则读取系统默认的portal
		Portal portal = new Portal();
		List<Portal> portalList = o_portalBO.queryPortalsByRoleid(roleId);
		if(null != portalList && portalList.size()>0){
			portal = portalList.get(0);
		}else{
			//查询系统默认的portal
			portal = o_portalBO.queryUsePortalsByUserid("");
		}
		request.setAttribute("portal", portal);
		request.setAttribute("portlets", portlets);
		request.setAttribute("roleId", roleId);
		return "sys/portal/roleAssignPortalManage";
	}
	/**
	 * 给角色中每个用户分配portal.
	 * @author 吴德福
	 * @param portalPortletsJson
	 * @param portalsJson
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/portal/roleAssignPortal.do")
	public void roleAssignPortal(String portalPortletsJson,String portalsJson,String roleId,HttpServletResponse response) throws Exception{
		JSONArray portalPortlets=JSONArray.fromObject(portalPortletsJson);
		JSONArray portals=JSONArray.fromObject(portalsJson);
		JSONObject portal1=(JSONObject)portals.get(0);
		
		SysRole role = o_sysRoleBO.queryRoleById(roleId);
		Set<SysUser> users = role.getSysUsers();
		Portal portal = null;
		int count = 1;
		for(SysUser sysUser : users){
			portal = new Portal();
			//查询该用户是否已经存在portal
			Portal isExistPortal = o_portalBO.isExistPortalByUserid(sysUser.getId());
			if(null != isExistPortal){
				portal.setId(isExistPortal.getId());
			}else{
				portal.setId(Identities.uuid());
			}
			portal.setSysRole(role);
			portal.setRow(portal1.getInt("row"));
			portal.setCol(portal1.getInt("col"));
			portal.setUpdateTime(new Date());
			portal.setSysUser(sysUser);
			portal.setOperator(UserContext.getUsername());
			portal.setSort(count);
			count++;
			
			Set<PortalPortlet> subPortalPortlet=new HashSet<PortalPortlet>();
			if(portalPortlets.size()>0){
				PortalPortlet portalPortlet=null;
				JSONObject portalPortletJson=null;
				for(int i=0;i<portalPortlets.size();i++){
					portalPortletJson=(JSONObject)portalPortlets.get(i);
					portalPortlet = new PortalPortlet();
					Portlet portlet=new Portlet();
					portalPortlet.setId(Identities.uuid());
					portlet.setId(portalPortletJson.getString("portletId"));
					portalPortlet.setPortlet(portlet);
					portalPortlet.setOperator(UserContext.getUsername());
					portalPortlet.setColNo(portalPortletJson.getInt("colNo"));
					portalPortlet.setRowNo(portalPortletJson.getInt("rowNo"));
					portalPortlet.setUpdateTime(new Date());
					portalPortlet.setPortal(portal);
					portalPortlet.setSort(i+1);
					subPortalPortlet.add(portalPortlet);
				}
			}
			portal.setSubPortalPortlet(subPortalPortlet);
			o_portalBO.mergePortal(portal);
		}
	}
}

