package com.fhd.icm.entity.assess;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;


/**
 * 评价标准模板
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-3-29		下午5:21:12
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_ASSESSMENT_GUIDELINES")
public class AssessGuidelines extends IdEntity implements Serializable {

	private static final long serialVersionUID = -4417112423728161261L;
	
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 评价标准名称
	 */
	@Column(name="GUIDELINES_NAME")
	private String name;
	
	/**
	 * 说明
	 */
	@Column(name="EDESC", length = 4000)
	private String comment;
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;
	
	/**
	 * 评价标准模板类型：财报相关缺陷，非财报相关缺陷，样本合格评价标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry type;			
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 评价标准模板对应的评价标准项
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessGuidelines")
	private Set<AssessGuidelinesProperty> assessGuidelinesProperty = new HashSet<AssessGuidelinesProperty>(0);
	
	public AssessGuidelines(){
		
	}
	
	public AssessGuidelines(String id){
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
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

	public Set<AssessGuidelinesProperty> getAssessGuidelinesProperty() {
		return assessGuidelinesProperty;
	}

	public void setAssessGuidelinesProperty(
			Set<AssessGuidelinesProperty> assessGuidelinesProperty) {
		this.assessGuidelinesProperty = assessGuidelinesProperty;
	}
	
}

