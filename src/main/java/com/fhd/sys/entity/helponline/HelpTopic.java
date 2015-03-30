/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.sys.entity.helponline;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 帮助主题
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-20		下午12:07:34
 *
 * @see 	 
 */
@Entity
@Searchable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_HLP_TOPIC")
public class HelpTopic extends IdEntity {

	
	private static final long serialVersionUID = 1L;

	/**
	 * 帮助目录
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOG_ID")
	private HelpCatalog helpCatalog;
	
	/**
	 * 类别 
	 * 目录帮助：catalog；根据关键词提示：hint
	 */
	@Column(name = "ETYPE")
	private String type;
	
	/**
	 * 主题编号
	 */
	@Column(name = "TOPIC_CODE")
	private String topicCode;
	
	/**
	 * 主题名称
	 */
	@SearchableProperty(name = "name")
	@Column(name = "TOPIC_NAME")
	private String topicName;
	
	/**
	 * 内容
	 */
	@Column(name = "ECONTENT")
	private String content;
	
	/**
	 * 内容 -- 全文搜索
	 */
	@SearchableProperty(name = "content")
	@Column(name = "CONTENT_KEY_WORD")
	private String hitContent;
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 上传附件
	 */
	@ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity fileUpload;

	
	public FileUploadEntity getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEntity fileUpload) {
		this.fileUpload = fileUpload;
	}

	public HelpCatalog getHelpCatalog() {
		return helpCatalog;
	}

	public void setHelpCatalog(HelpCatalog helpCatalog) {
		this.helpCatalog = helpCatalog;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTopicCode() {
		return topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getHitContent() {
		return hitContent;
	}

	public void setHitContent(String hitContent) {
		this.hitContent = hitContent;
	}
	
	
}

