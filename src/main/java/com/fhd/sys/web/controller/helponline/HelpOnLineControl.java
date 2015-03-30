/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.web.controller.helponline;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.compass.core.support.search.CompassSearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.compass.AdvancedSearchCommand;
import com.fhd.fdc.commons.compass.CompassSearchService;
import com.fhd.fdc.commons.exception.FHDException;
import com.fhd.sys.business.helponline.HelpOnLineBO;
import com.fhd.sys.entity.helponline.HelpCatalog;
import com.fhd.sys.entity.helponline.HelpTopic;
import com.fhd.sys.web.form.helponline.HelpCatalogForm;
import com.fhd.sys.web.form.helponline.HelpTopicForm;
import com.lowagie.text.pdf.codec.Base64.OutputStream;

/**
 * 在线帮助
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-20		下午1:35:21
 *
 * @see 	 
 */
@Controller
@RequestMapping("/sys/helponline")
public class HelpOnLineControl {

	@Autowired
	private HelpOnLineBO o_helpOnLineBO;
	
	@Autowired
	private CompassSearchService o_compassSearchService;
	
	/**
	 * 
	 * <pre>
	 * 知识管理跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "helpCatalogTree.do", method = RequestMethod.GET)
	public void g_helpCatalogTree() {}
	
	
	/**
	 * 
	 * <pre>
	 * 知识分类树加载
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "helpCatalogTreeLoader.do", method = RequestMethod.POST)
	public List<Map<String, Object>> p_helpCatalogTreeLoader(String id) {
		Set<HelpCatalog> helpCatalogs = o_helpOnLineBO.getSubHelpCatalogByid(id);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (HelpCatalog helpCatalog : helpCatalogs) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", helpCatalog.getId());
			m.put("text", helpCatalog.getCatalogName());
			m.put("hrefTarget", "helpTopicFrame");
			m.put("href", "helpTopicList.do?catalogid=" + helpCatalog.getId());
			m.put("leaf", helpCatalog.getChildren().size() == 0);
			list.add(m);
		}
		return list;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 添加知识分类跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param parentId
	 * @param model
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "addHelpCatalog.do", method = RequestMethod.GET)
	public String g_addHelpCatalog(@RequestParam("id") String parentId,@RequestParam("name") String parentName,Model model) {
		HelpCatalogForm helpCatalogForm = new HelpCatalogForm();
		HelpCatalog helpCatalog = new HelpCatalog();
		helpCatalog.setId(parentId);
		helpCatalog.setCatalogName(parentName);
		helpCatalogForm.setParent(helpCatalog);
		model.addAttribute("helpCatalogForm", helpCatalogForm);
		return "sys/helponline/helpCatalogAddOrEdit";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 修改知识分类跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @param model
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "editHelpCatalog.do", method = RequestMethod.GET)
	public String g_editHelpCatalog(@RequestParam("id") String id,Model model) {
		HelpCatalogForm helpCatalogForm = new HelpCatalogForm();
		HelpCatalog helpCatalog = o_helpOnLineBO.getHelpCatalogByid(id);
		try {
			BeanUtils.copyProperties(helpCatalogForm, helpCatalog);
		} catch (Exception e) {
			throw new FHDException(e);
		}
		model.addAttribute("helpCatalogForm", helpCatalogForm);
		return "sys/helponline/helpCatalogAddOrEdit";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 新增或修改一个知识分类
	 * 
	 * 根据id判断，是修改还是新增
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param helpCatalogForm
	 * @throws FHDException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "addOrEditHelpCatalog.do", method = RequestMethod.POST)
	public void p_addOrEditHelpCatalog(HelpCatalogForm helpCatalogForm) throws FHDException {
		String id = helpCatalogForm.getId();
		HelpCatalog helpCatalog = new HelpCatalog();
		try {
			BeanUtils.copyProperties(helpCatalog, helpCatalogForm);
		} catch (Exception e) {
			throw new FHDException(e);
		}
		if(StringUtils.isEmpty(id)) {
			HelpCatalog parentHelpCatalog = o_helpOnLineBO.getHelpCatalogByid(helpCatalog.getParent().getId());
			if(null != parentHelpCatalog){
				helpCatalog.setId(Identities.uuid());
				helpCatalog.setIdSeq(parentHelpCatalog.getIdSeq() + helpCatalog.getId() + ".");
				helpCatalog.setLevel(parentHelpCatalog.getLevel()+1);
			}else{
				parentHelpCatalog = new HelpCatalog();
				parentHelpCatalog.setId(helpCatalog.getParent().getId());
				parentHelpCatalog.setCatalogName(helpCatalog.getParent().getCatalogName());
				parentHelpCatalog.setIdSeq("."+helpCatalog.getParent().getId()+".");
				parentHelpCatalog.setLevel(1);
				o_helpOnLineBO.mergeHelpCatalog(parentHelpCatalog);
				helpCatalog.setId(Identities.uuid());
				helpCatalog.setIdSeq(parentHelpCatalog.getIdSeq() + helpCatalog.getId() + ".");
				helpCatalog.setLevel(parentHelpCatalog.getLevel()+1);
			}
			
		}
		
		o_helpOnLineBO.mergeHelpCatalog(helpCatalog);
	}
	
	/**
	 * 
	 * <pre>
	 * 帮助主题跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "helpTopicList.do", method = RequestMethod.GET)
	public void g_helpTopicList(){
		
	}
	
	/**
	 * 
	 * <pre>
	 * 帮助主题列表加载
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param start
	 * @param limit
	 * @param catalogid
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("queryHelpTopic.do")
	public Map<String, Object> g_queryHelpTopic(int start, int limit,String catalogid) {
	
		Page<HelpTopic> page = new Page<HelpTopic>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);
		List<Map<String, Object>> datas=null;
		List<HelpTopic> helpTopics=null;
		Map<String, Object> map =null;
		if (!("1".equals(catalogid))) {
		o_helpOnLineBO.queryHelpTopicByCatalogid(page,catalogid);
		datas = new ArrayList<Map<String, Object>>();
		helpTopics = page.getList();
		for (HelpTopic helpTopic : helpTopics) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", helpTopic.getId());
			row.put("topicCode", helpTopic.getTopicCode());
			row.put("topicName", helpTopic.getTopicName());
			row.put("type", helpTopic.getType());
			String content = helpTopic.getContent();
			if(StringUtils.isNotEmpty(content)) {
				// 用正则去掉全部html标签的内容
				content = content.replaceAll("<[^>]+>", "");
			}
			row.put("content", content);
			datas.add(row);
		}
		map = new HashMap<String, Object>();
		map.put("totalCount",page.getFullListSize());
		map.put("datas", datas);
		return map;			
		}else {
			helpTopics = o_helpOnLineBO.getHelpTopicsById(catalogid);
			datas = new ArrayList<Map<String, Object>>();
			for (HelpTopic helpTopic : helpTopics) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", helpTopic.getId());
				row.put("topicCode", helpTopic.getTopicCode());
				row.put("topicName", helpTopic.getTopicName());
				row.put("type", helpTopic.getType());
				String content = helpTopic.getContent();
				if(StringUtils.isNotEmpty(content)) {
					// 用正则去掉全部html标签的内容
					content = content.replaceAll("<[^>]+>", "");
				}
				row.put("content", content);
				datas.add(row);
			}
			map = new HashMap<String, Object>();
			map.put("totalCount",helpTopics.size());
			map.put("datas", datas);
			return map;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 添加主题跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param model
	 * @param catalogid
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "addHelpTopic.do", method = RequestMethod.GET)
	public String g_addHelpTopic(Model model, String catalogid) {
		HelpTopicForm helpTopicForm = new HelpTopicForm();
		HelpCatalog helpCatalog = new HelpCatalog();
		helpCatalog.setId(catalogid);
		helpTopicForm.setHelpCatalog(helpCatalog);
		model.addAttribute("helpTopicForm", helpTopicForm);
		return "sys/helponline/helpTopicAddOrEdit";
	}
	
	/**
	 * 
	 * <pre>
	 * 修改主题跳转
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @param model
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "editHelpTopic.do", method = RequestMethod.GET)
	public String g_editHelpTopic(@RequestParam("id") String id,Model model) {
		HelpTopicForm helpTopicForm = new HelpTopicForm();
		HelpTopic helpTopic = o_helpOnLineBO.getHelpTopicByid(id);
		try {
			BeanUtils.copyProperties(helpTopicForm, helpTopic);
		} catch (Exception e) {
			throw new FHDException(e);
		}
		model.addAttribute("helpTopicForm", helpTopicForm);
		return "sys/helponline/helpTopicAddOrEdit";
	}
	
	/**
	 * 
	 * <pre>
	 * 新增或修改一个帮助主题
	 * 
	 * 根据id判断，是修改还是新增
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param helpTopicForm
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "addOrEditHelpTopic.do", method = RequestMethod.POST)
	public void p_addOrEditHelpCatalog(HelpTopicForm helpTopicForm,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = helpTopicForm.getId();
		HelpTopic helpTopic = new HelpTopic();
		
		try {
			BeanUtils.copyProperties(helpTopic, helpTopicForm);
			
		} catch (Exception e) {
			throw new FHDException(e);
		}
		if(StringUtils.isEmpty(id)) {
			helpTopic.setId(Identities.uuid());
		}
		String content = helpTopic.getContent();
		
		if(StringUtils.isNotEmpty(content)) {
			// 用正则去掉全部html标签的内容
			content = content.replaceAll("<[^>]+>", "");
			// 去完html标签的内容存进数据库
			helpTopic.setHitContent(content);
		}
		List<MultipartFile> files = null;
		/**
		 * 判断是否有上传附件
		 */
		if (request instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			files = multipartHttpServletRequest.getFiles("file");
		}
		
		o_helpOnLineBO.mergeHelpTopic(helpTopic,files);
	}
	
	@RequestMapping(value = "delHelpCatalog.do", method = RequestMethod.POST)
	public void g_delHelpCatalog(String catalogid ,HttpServletRequest request, HttpServletResponse response) throws IOException{
		String flag = "false";
		PrintWriter out = response.getWriter();
		try {
			int size = o_helpOnLineBO.queryHelpTopicsByCatalogId(catalogid).size();
			if (size>0) {
				out.write(flag);
			}else {
				o_helpOnLineBO.delHelpCatalogByid(catalogid);
				flag = "true";
				out.write(flag);
			}
		}catch(DataIntegrityViolationException e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	
	}
	
	@RequestMapping(value = "helpOnlineView.do", method = RequestMethod.GET)
	public void g_helpOnlineView() {}
	
	@ResponseBody
	@RequestMapping(value = "helpOnlineViewLoader.do", method = RequestMethod.POST)
	public List<Map<String, Object>> p_helpOnlineViewLoader(String id) {
		Set<HelpCatalog> helpCatalogs = o_helpOnLineBO.getSubHelpCatalogByid(id);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (HelpCatalog helpCatalog : helpCatalogs) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", helpCatalog.getId());
			m.put("text", helpCatalog.getCatalogName());
			m.put("hrefTarget", "helpCatalogView");
			m.put("href", "helpCatalogView.do?catalogid=" + helpCatalog.getId());
			m.put("leaf", helpCatalog.getChildren().size() == 0);
			list.add(m);
		}
		return list;
	}
	
	@RequestMapping(value = "helpCatalogView.do", method = RequestMethod.GET)
	public void g_helpCatalogView(String catalogid,Model model) {
		model.addAttribute("helpCatalog", o_helpOnLineBO.getHelpCatalogByid(catalogid));
	}
	
	@RequestMapping(value = "helpTopicView.do", method = RequestMethod.GET)
	public void g_helpTopicView(String topicid,Model model) {
		model.addAttribute("helpTopic", o_helpOnLineBO.getHelpTopicByid(topicid));
	}
	
	
	@RequestMapping(value = "helpTopicSearch.do")
	public void helpTopicSearch(HttpServletRequest request, Model model) {
        String queryString = request.getParameter("query");
        String page = request.getParameter("page");
        if (StringUtils.isNotBlank(queryString)) {
            AdvancedSearchCommand searchCommand = new AdvancedSearchCommand();
            searchCommand.setQuery(queryString);
            if (StringUtils.isNotBlank(page)) {
            	searchCommand.setPage(Integer.valueOf(page));
            }
            searchCommand.setTypes(HelpTopic.class);
            searchCommand.setHighlightFields(new String[]{"name","content"});

            CompassSearchResults searchResults = o_compassSearchService.search(searchCommand);
            model.addAttribute("searchResults", searchResults);
        }
    }
	
	@RequestMapping(value = "delHelpTopic.do", method = RequestMethod.POST)
	public void p_delHelpTopic(String ids) {
		o_helpOnLineBO.delHelpTopicByid(ids);
	}
	
}

