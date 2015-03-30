/**
 * PortletControl.java
 * com.fhd.fdc.commons.web.controller.sys.portal
 *
 * Function： 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.portal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.portal.PortletBO;
import com.fhd.sys.entity.portal.Portlet;
import com.fhd.sys.web.form.portal.PortletForm;

/**
 * PortletController类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-20
 * Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = PortletForm.class)
public class PortletControl {
	
	@Autowired
	private PortletBO o_portletBO;
	
	/**
	 * 跳转到面板管理页面
	 * @author 杨鹏
	 * @param portletForm
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.0
	 */
	@RequestMapping(value = "/sys/portlet/portletManage.do")
	public String portletManage(String title,String url,HttpServletRequest request,HttpServletResponse response){
		return "sys/portlet/portletManage";
	}
	/**
	 * 根据条件查询所有面板
	 * @author 杨鹏
	 * @param portletForm
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.0
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/portlet/portletSelect.do")
	public Map<String, Object> queryPortletsBySome(String title,String url,int limit,int start,HttpServletRequest request,HttpServletResponse response){
		Page<Portlet> page = new Page<Portlet>();
		page.setPageNumber((limit == 0 ? 0 : start / limit) + 1);
		page.setObjectsPerPage(limit);
		page = o_portletBO.queryPortletsBySome(page,title, url);
		return this.convertValues(page);
	}
	
	/**
	 * 更新面板信息准备
	 * @author 杨鹏
	 * @param id
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.0
	 */
	@RequestMapping(value = "/sys/portlet/updateShow.do")
	public String queryPortletsById(String id,HttpServletRequest request,HttpServletResponse response){
		if(StringUtils.isNotBlank(id)){
			Portlet portlet = o_portletBO.queryPortletById(id);
			request.setAttribute("portlet", portlet);
		}
		return "sys/portlet/portletUpdate";
	}
	/**
	 * 更新面板信息
	 * @author 杨鹏
	 * @param id
	 * @param title
	 * @param height
	 * @param url
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @since  fhd　Ver 1.0
	 */
	@RequestMapping(value = "/sys/portlet/update.do")
	public void updatePortlet(String id,String title,String height,String url,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String operator=UserContext.getUser().getRealname();
		if(StringUtils.isBlank(id)){
			Portlet portlet=new Portlet();
			portlet.setId(Identities.uuid());
			portlet.setTitle(title);
			portlet.setHeight(height);
			portlet.setUrl(url);
			portlet.setOperator(operator);
			portlet.setUpdateTime(new Date());
			o_portletBO.savePortlet(portlet);
		}else{
			Portlet portlet=o_portletBO.queryPortletById(id);
			portlet.setTitle(title);
			portlet.setHeight(height);
			portlet.setUrl(url);
			portlet.setOperator(operator);
			portlet.setUpdateTime(new Date());
			o_portletBO.updatePortlet(portlet);
		}
	}
	/**
	 * 删除面板信息
	 * @author 杨鹏
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @since  fhd　Ver 1.0
	 */
	@RequestMapping(value = "/sys/portlet/delete.do")
	public void deletePortletsById(String id,HttpServletRequest request,HttpServletResponse response) throws Exception{
		o_portletBO.removePortlet(id);
	}
	/**
	 * 面板对象映射为map
	 * @author 杨鹏
	 * @param page
	 * @return
	 */
	public Map<String, Object> convertValues(Page<Portlet> page){
		List<Portlet> portlets = page.getList();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Map<String, Object> row =null;
		for (Portlet portlet : portlets) {
			row = new HashMap<String, Object>();
			String id = portlet.getId();
			String title = portlet.getTitle();
			String height = portlet.getHeight();
			String url = portlet.getUrl();
			String operator = portlet.getOperator();
			Date updateTime = portlet.getUpdateTime();
			String formatString="yyyy年MM月dd日 HH:mm:ss";
			String updateTimeStr = DateUtils.formatDate(updateTime, formatString);
			row.put("id", id);
			row.put("title", title);
			row.put("height", height);
			row.put("url", url);
			row.put("operator", operator);
			row.put("updateTime", updateTimeStr);
			datas.add(row);
		}
		Map<String, Object> map = new Hashtable<String, Object>();
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
}

