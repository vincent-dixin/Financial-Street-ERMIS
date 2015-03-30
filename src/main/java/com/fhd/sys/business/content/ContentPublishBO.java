/**
 * ContentPublishBO.java
 * com.fhd.fdc.commons.business.sys.content
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-15 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.content;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.content.ContentPublishDAO;
import com.fhd.sys.entity.content.ContentPublish;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.web.form.content.ContentPublishForm;

/**
 * 内容发布BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-10-15 
 * @since    Ver 1.1
 * @Date	 2010-10-15		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */

@Service
@SuppressWarnings("unchecked")
public class ContentPublishBO {

	@Autowired
	private ContentPublishDAO o_contentPublishDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	
	/**
	 * 新增内容发布.
	 * @author 吴德福
	 * @param content
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveContent(ContentPublish content){
		try {
			o_contentPublishDAO.save(content);
			o_businessLogBO.saveBusinessLogInterface("新增", "内容发布", "成功", content.getTitle(),content.getContentType(),content.getIsDeploy());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "内容发布", "失败", content.getTitle(),content.getContentType(),content.getIsDeploy());
		}
		
	}
	/**
	 * 修改内容发布.
	 * @author 吴德福
	 * @param content
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateContent(ContentPublish content){
		try {
			o_contentPublishDAO.merge(content);
			o_businessLogBO.modBusinessLogInterface("修改", "内容发布", "成功", content.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "内容发布", "失败", content.getId());
		}
	}
	/**
	 * 删除内容发布.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeContent(String id){
		try {
			o_contentPublishDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "内容发布", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "内容发布", "失败", id);
		}
	}
	/**
	 * 根据id查询内容发布.
	 * @author 吴德福
	 * @param id
	 * @return ContentPublish
	 * @since  fhd　Ver 1.1
	 */
	public ContentPublish getContentById(String id){
		return o_contentPublishDAO.get(id);
	}
	/**
	 * 查询所有的内容发布信息.
	 * @author 吴德福
	 * @return List<ContentPublish>
	 * @since  fhd　Ver 1.1
	 */
	public List<ContentPublish> getAllContent(){
		return o_contentPublishDAO.getAll();
	}
	/**
	 * 根根据登录用户查询用户所属机构的内容发布信息.
	 * @author 吴德福
	 * @return List<ContentPublish>
	 * @since  fhd　Ver 1.1
	 */
	public List<ContentPublish> getAllContentByUser(String orgid){
		Criteria criteria = o_contentPublishDAO.createCriteria();
		criteria.setFetchMode("sysUser", FetchMode.SELECT);
		criteria.setFetchMode("sysOrganization", FetchMode.SELECT);
		criteria.createAlias("sysUser", "u");
		criteria.createAlias("sysOrganization", "o");
		criteria.add(Restrictions.eq("o.id", orgid));
		return criteria.list();
	}
	/**
	 * 根据内容标题查询新闻或者公告是否存在.
	 * @author 吴德福
	 * @param title
	 * @return List<ContentPublish>
	 * @since  fhd　Ver 1.1
	 */
	public List<ContentPublish> queryContentsByTitle(String title){
		Criteria criteria = o_contentPublishDAO.createCriteria(Restrictions.like("title", title,MatchMode.ANYWHERE));
		return criteria.list();
	}
	/**
	 * 根据查询条件查询内容.
	 * @author 吴德福
	 * @param contentPublishForm
	 * @return List<ContentPublish>
	 * @since  fhd　Ver 1.1
	 */
	public List<ContentPublish> queryContentPublishByConditions(ContentPublishForm contentPublishForm){
		StringBuilder hql = new StringBuilder();
		hql.append("From ContentPublish Where 1=1 ");
		if(contentPublishForm != null && !"".equals(contentPublishForm.getTitle()) && contentPublishForm.getTitle() != null){
			hql.append(" and title like '%"+contentPublishForm.getTitle()+"%'");
		}
		String username = "";
		if(contentPublishForm != null && !"".equals(contentPublishForm.getUsername()) && contentPublishForm.getUsername() != null){
			username = contentPublishForm.getUsername();
			hql.append(" and sysUser.username like '%"+username+"%'");
		}
		if(contentPublishForm != null && !"".equals(contentPublishForm.getContentType()) && contentPublishForm.getContentType() != null){
			hql.append(" and contentType like '%"+contentPublishForm.getContentType()+"%'");
		}
		if(contentPublishForm != null && contentPublishForm.getIsDeploy() != null && contentPublishForm.getIsDeploy() != null){
			hql.append(" and isDeploy like '%"+contentPublishForm.getIsDeploy()+"%'");
		}
		return o_contentPublishDAO.find(hql.toString());
	}
	/**
	 * 根据内容发布的类型查询相关的所有内容.
	 * @author 吴德福
	 * @param name
	 * @return List<ContentPublish>
	 * @since  fhd　Ver 1.1
	 */
	public List<ContentPublish> queryContentsByType(String name){
		String type = "";
		String isDeploy = "";
		List<DictEntry> dictEntryList = o_dictEntryBO.queryAllDictEntry();
		for(DictEntry dictEntry : dictEntryList){
			if(name.equals(dictEntry.getName())){
				type = dictEntry.getId();
			}
			if("是".equals(dictEntry.getName())){
				isDeploy = dictEntry.getId();
			}
		}
		
		Criteria criteria = o_contentPublishDAO.createCriteria(Restrictions.eq("contentType", type),Restrictions.eq("isDeploy", isDeploy));
		return criteria.list();
	}
	
	/**
	 * 
	 * @param page
	 * @param title
	 * @param username
	 * @param contentType
	 * @param isDeploy
	 * @return
	 */
	
	public Page<ContentPublish> getContent(Page<ContentPublish> page,String title, String username, String contentType, String isDeploy) {
		DetachedCriteria dc=DetachedCriteria.forClass(ContentPublish.class,"cp");
		dc.createAlias("sysUser", "User");
		if(StringUtils.isNotBlank(username)){
			dc.add(Restrictions.like("user.username", username,MatchMode.ANYWHERE));
		}
		
		
		if(StringUtils.isNotBlank(title)){
			dc.add(Property.forName("cp.title").like(title,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(contentType)){
			dc.add(Property.forName("cp.contentType").like(contentType,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(isDeploy)){
			dc.add(Property.forName("cp.isDeploy").like(isDeploy,MatchMode.ANYWHERE));
		}
		
		
		
		
		return o_contentPublishDAO.pagedQuery(dc, page);
	}
}

