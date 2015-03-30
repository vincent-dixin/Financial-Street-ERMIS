/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.business.helponline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fhd.core.dao.support.Page;
import com.fhd.fdc.utils.fileupload.FileUpload;
import com.fhd.sys.dao.helponline.HelpCatalogDAO;
import com.fhd.sys.dao.helponline.HelpTopicDAO;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.helponline.HelpCatalog;
import com.fhd.sys.entity.helponline.HelpTopic;

/**
 * 在线帮助
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-20		下午1:34:20
 *
 * @see 	 
 */
@Service
public class HelpOnLineBO {

	@Autowired
	private HelpCatalogDAO o_helpCatalogDAO;
	
	@Autowired
	private HelpTopicDAO o_helpTopicDAO;
	
	public HelpCatalog getHelpCatalogByid(String id) {
		return o_helpCatalogDAO.get(id);
	}

	public Set<HelpCatalog> getSubHelpCatalogByid(String id) {
		return getHelpCatalogByid(id).getChildren();
	}

	@Transactional
	public void mergeHelpCatalog(HelpCatalog helpCatalog) {
		o_helpCatalogDAO.merge(helpCatalog);
	}

	public void queryHelpTopicByCatalogid(Page<HelpTopic> page, String catalogid) {
		DetachedCriteria dc = DetachedCriteria.forClass(HelpTopic.class);
		dc.add(Restrictions.eq("helpCatalog.id", catalogid));
		o_helpTopicDAO.pagedQuery(dc, page,false);
	}

	@Transactional
	public void mergeHelpTopic(HelpTopic helpTopic,List<MultipartFile> files) throws Exception {
		
		if(files!=null){
			for(MultipartFile file:files){
				if(StringUtils.isNotBlank(file.getOriginalFilename())){
					FileUploadEntity fileUpload = FileUpload.uploadFile("dataBase", file);
					helpTopic.setFileUpload(fileUpload);
				}
			}
		}
		o_helpTopicDAO.merge(helpTopic);
	}

	public HelpTopic getHelpTopicByid(String id) {
		
		return o_helpTopicDAO.get(id);
		
	}

	@Transactional
	public void delHelpTopicByid(String ids) {
		String[] strings = StringUtils.split(ids, ",");
		for (String id : strings) {
			o_helpTopicDAO.removeById(id);
		}
		o_helpTopicDAO.flush();
		
	}

	@Transactional
	public void delHelpCatalogByid(String catalogid) {
		
		o_helpCatalogDAO.removeById(catalogid);
		o_helpCatalogDAO.flush();
	}

	
	public HelpTopic getHelpTopicHintByCode(String helpTopicCode) {
		
		Criteria criteria = o_helpTopicDAO. createCriteria(Restrictions.eq("type", "hint"),Restrictions.eq("topicCode", helpTopicCode));
		return (HelpTopic) criteria.list().get(0);
		
	}
	
	public Set<HelpTopic> queryHelpTopicsByCatalogId(String id) {
		HelpCatalog helpCatalog = o_helpCatalogDAO.get(id);
		Set<HelpTopic> helpTopics = helpCatalog.getHelpTopics();
		return helpTopics;
	}
	/**
	 * @param id rootid
	 * */
	public List<HelpTopic> getHelpTopicsById(String id) {
		List<HelpTopic> helpTopics = new ArrayList<HelpTopic>();
		helpTopics = o_helpTopicDAO.getAll();
		return helpTopics;
	}
}

