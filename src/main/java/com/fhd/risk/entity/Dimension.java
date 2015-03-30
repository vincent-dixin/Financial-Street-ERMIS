package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 维度类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		上午11:20:35
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DIM_SCORE_DIM") 
public class Dimension extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1831605195472471583L;
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 编号
	 */
	@Column(name = "SCORE_DIM_CODE")
	private String code;
	
	/**
	 * 名称
	 */
	@Column(name = "SCORE_DIM_NAME")
	private String name;
	
	/**
	 * 描述
	 */
	@Column(name = "EDESC",length=4000)
	private String desc;
	
	/**
	 * 是否有效
	 */
	@Column(name = "DELETE_STATUS",length=4000)
	private String deleteStatus;
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;

	//TODO SCORE_DIM_TYPE //暂时不确定
	
	public Dimension(){
		
	}
	public Dimension(String id){
		super.setId(id);
	}
	
	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}

