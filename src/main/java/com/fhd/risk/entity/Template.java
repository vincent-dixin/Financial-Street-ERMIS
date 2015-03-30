package com.fhd.risk.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 模板类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		上午11:03:56
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DIM_TEMPLATE") 
public class Template extends IdEntity implements Serializable{
	private static final long serialVersionUID = -8267861072609143434L;
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 名称
	 */
	@Column(name = "TEMPLATE_NAME")
	private String name;
	
	/**
	 * 描述
	 */
	@Column(name = "TEMPLATE_DESC",length=2000)
	private String desc;
	
	/**
	 * 类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_TYPE")
	private DictEntry type;
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;

	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity file;
	
	/**
	 * 模板关联维度ID
	 */
	@OrderBy("idSeq ASC")
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "template")
	private Set<TemplateRelaDimension> templateRelaDimensionSet = new HashSet<TemplateRelaDimension>(0);
	
	public Template(){
		
	}
	public Template(String id){
		super.setId(id);
	}
	
	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public FileUploadEntity getFile() {
		return file;
	}
	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	public Set<TemplateRelaDimension> getTemplateRelaDimensionSet() {
		return templateRelaDimensionSet;
	}
	public void setTemplateRelaDimensionSet(
			Set<TemplateRelaDimension> templateRelaDimensionSet) {
		this.templateRelaDimensionSet = templateRelaDimensionSet;
	}	
	
	
}

