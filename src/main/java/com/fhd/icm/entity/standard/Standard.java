package com.fhd.icm.entity.standard;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 内控标准
 * @author   张 雷
 * @version  
 * @since    var 1.1
 * @Date	 2012-12-6		下午5:49:50
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_CONTROL_STANDARD")
public class Standard extends AuditableEntity implements Serializable {
	private static final long serialVersionUID = 9083727129909223363L;
	
	/**
	 * 所属公司
	 */
	@Column(name = "company_Id")
	private String companyId;
	/**
	 * 标准编号
	 */
	@Column(name="STANDARD_CODE")
	private String code;
	/**
	 * 标准名称
	 */
	@Column(name="STANDARD_NAME")
	private String name;
	/**
	 * 内控标准要求
	 */
	@Column(name="CONTROL_REQUIREMENT")
	private String controlRequirement;
	/**
	 * 标准分类
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	private Standard parent;
	/**
	 * 主键全路径
	 */
	@Column(name="ID_SEQ")
	private String idSeq;

	/**
	 * 层级
	 */
	@Column(name = "ELEVEL")
	private Integer level;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	/**
	 * 是否是页子节点.
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	
	/**
	 * 控制层级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_LEVEL")
	private DictEntry controlLevel;
	/**
	 * 内控要素,多选
	 */
	@Column(name = "CONTROL_POINT")
	private String controlPoint;
	
	/**
	 * 反馈意见
	 */
	@Column(name="FEEDBACK")
	private String feedback;
	
	/**
	 * 业务字段
	 */
	@Column(name="BUSINESS_FIELD")
	private String businessField;
	
	/**
	 * 处理状态:未开始-N;处理中-H;待更新-U;已纳入内控手册运转-O
	 */
	@Column(name="DEAL_STATUS")
	private String dealStatus;
	
    /**
     * 删除状态：已删除-0;已启用-1
     */
    @Column(name = "DELETE_STATUS")
    private Boolean deleteStatus;
	
	/**
	 * 更新期限
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="UPDATE_DEADLINE")
	private Date updateDeadline;
	
	/**
	 * 类型：内控标准：1，内控要求：0
	 */
	@Column(name = "ETYPE")
	private String type;
	
	/**
	 * 状态: 已保存-S;已提交-P
	 */
	@Column(name = "ESTATUS")
	private String status;
	/**
	 * 下级机构
	 */
	@Column(name = "SUB_COMPANY_IDS")
	private String subCompanyids;
	

    /**
	 * 标准集合.
	 */
	@OrderBy("sort ASC")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<Standard> children = new HashSet<Standard>(0);
	
	/**
	 * 标准关联的附件
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "standard")
	private Set<StandardRelaFile> standardRelaFile = new HashSet<StandardRelaFile>(0);;
	
	/**
	 * 标准关联的部门
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "standard")
	private Set<StandardRelaOrg> standardRelaOrg = new HashSet<StandardRelaOrg>(0);;
	
	/**
	 * 标准关联的流程
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "standard")
	private Set<StandardRelaProcessure> standardRelaProcessure = new HashSet<StandardRelaProcessure>(0);;
	
	
	public Standard() {
	}
	
	public Standard(String id) {
		 this.setId(id);
	}
	
	public String getSubCompanyids() {
        return subCompanyids;
    }

    public void setSubCompanyids(String subCompanyids) {
        this.subCompanyids = subCompanyids;
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
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getBusinessField() {
		return businessField;
	}

	public void setBusinessField(String businessField) {
		this.businessField = businessField;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Date getUpdateDeadline() {
		return updateDeadline;
	}

	public void setUpdateDeadline(Date updateDeadline) {
		this.updateDeadline = updateDeadline;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getControlRequirement() {
		return controlRequirement;
	}
	public void setControlRequirement(String controlRequirement) {
		this.controlRequirement = controlRequirement;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Standard getParent() {
		return parent;
	}
	public void setParent(Standard parent) {
		this.parent = parent;
	}
	public String getIdSeq() {
		return idSeq;
	}
	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public DictEntry getControlLevel() {
		return controlLevel;
	}
	public void setControlLevel(DictEntry controlLevel) {
		this.controlLevel = controlLevel;
	}
	
	public String getControlPoint() {
		return controlPoint;
	}

	public void setControlPoint(String controlPoint) {
		this.controlPoint = controlPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Standard> getChildren() {
		return children;
	}
	public void setChildren(Set<Standard> children) {
		this.children = children;
	}

	public Set<StandardRelaFile> getStandardRelaFile() {
		return standardRelaFile;
	}

	public void setStandardRelaFile(Set<StandardRelaFile> standardRelaFile) {
		this.standardRelaFile = standardRelaFile;
	}

	public Set<StandardRelaOrg> getStandardRelaOrg() {
		return standardRelaOrg;
	}

	public void setStandardRelaOrg(Set<StandardRelaOrg> standardRelaOrg) {
		this.standardRelaOrg = standardRelaOrg;
	}

	public Set<StandardRelaProcessure> getStandardRelaProcessure() {
		return standardRelaProcessure;
	}

	public void setStandardRelaProcessure(
			Set<StandardRelaProcessure> standardRelaProcessure) {
		this.standardRelaProcessure = standardRelaProcessure;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
