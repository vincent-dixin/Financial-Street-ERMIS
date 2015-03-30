package com.fhd.sys.entity.orgstructure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.sys.entity.duty.Duty;

/**
 * 机构实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "T_SYS_ORGANIZATION")
public class SysOrganization extends IdEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 导入时报错信息
	 */
	@Transient
	private String impErrorInfo;
	
	/**
	 * 父ID.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private SysOrganization parentOrg;
	/**
	 * 子ID.
	 */
	@OrderBy("sn ASC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentOrg")
	private Set<SysOrganization> childrenOrg;
	/**
	 * 机构编号.
	 */
	@Column(name = "ORG_CODE", length = 30)
	private String orgcode;
	/**
	 * 机构名称.
	 */
	@Column(name = "ORG_NAME", length = 50)
	private String orgname;
	/**
	 * 机构 层级.
	 */
	@Column(name = "ORG_LEVEL")
	private int orgLevel;
	/**
	 * 机构序列.
	 */
	@Column(name = "ID_SEQ", length = 512)
	private String orgseq;
	/**
	 * 机构类型.  
	 * 402881b22afad3b1012afae7520f0007 分公司部门
	 * 402881b22afad3b1012afae799c60008 总公司部门
	 * 402881b22afad3b1012afae5e33d0005 分公司
	 * 402881b22afad3b1012afae5a4200004 总公司
	 */
	@Column(name = "ORG_TYPE", length = 32)
	private String orgType;
	/**
	 * 业务版块.
	 */
	@Column(name = "FORUM",length = 32)
	private String forum;
	/**
	 * 区域.
	 */
	@Column(name = "REGION",length=32)
	private String region;
	/**
	 * 岗位id.
	 */
	@Column(name = "POSI_ID", length = 32)
	private String posiid;
	/**
	 * 员工id.主管人员
	 */
	@Column(name = "EMP_ID", length = 32)
	private String empid;
	/**
	 * 机构地址.
	 */
	@Column(name = "ADDRESS", length = 500)
	private String address;
	/**
	 * 邮政编码.
	 */
	@Column(name = "ZIPCODE", length = 10)
	private String zipcode;
	/**
	 * 联系电话.
	 */
	@Column(name = "LINK_TEL", length = 20)
	private String linkTel;
	/**
	 * 联系人.
	 */
	@Column(name = "LINK_MAN", length = 30)
	private String linkMan;
	/**
	 * 邮件Email.
	 */
	@Column(name = "EMAIL", length = 128)
	private String email;
	/**
	 * 网站地址.
	 */
	@Column(name = "WEBURL", length = 512)
	private String weburl;
	/**
	 * 开始日期.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;
	/**
	 * 结束日期.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;
	/**
	 * 是否是页子结点.
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	/**
	 * 备注.
	 */
	@Column(name = "ECOMMENT", length = 1024)
	private String remark;
	/**
	 * 机构状态.
	 */
	@Column(name = "ESTATUS", length = 32)
	private String orgStatus;
	/**
	 * 排列顺序.
	 */
	@Column(name = "ESORT")
	private int sn;
	/**
	 * 岗位集合（机构维护).
	 */
	@OrderBy("sn ASC")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysOrganization")
	private Set<SysPosition> sysPositions = new HashSet<SysPosition>(0);
	/**
	 * 机构集合（机构维护）.
	 */
	@OrderBy("sn ASC")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentOrg")
	private Set<SysOrganization> subOrg = new HashSet<SysOrganization>(0);
	/**
	 * 员工集合（机构维护).
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysOrganization")
	private Set<SysEmployee> sysEmployees = new HashSet<SysEmployee>(0);
	/**
	 * 职务集合（机构维护).
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Duty> dutys = new HashSet<Duty>(0);
	/**
	 * 机构员工中间表集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysOrganization")
	private Set<SysEmpOrg> sysEmpOrgs = new HashSet<SysEmpOrg>(0);
	
	/**
	 * 部门关联风险
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysOrganization")
	private Set<RiskOrg> orgRisks = new HashSet<RiskOrg>(0);
	// Constructors

	public Set<Duty> getDutys() {
		return dutys;
	}

	public void setDutys(Set<Duty> dutys) {
		this.dutys = dutys;
	}

	/** default constructor */
	public SysOrganization() {
	}
	
	public SysOrganization(String id) {
		this.setId(id);
	}

	/** full constructor */
	public SysOrganization(SysOrganization parentOrg, String orgcode,
			String orgname, int orgLevel, String orgseq, String orgType,
			String forum, String region, String posiid, String empid,
			String address, String zipcode, String linkTel, String linkMan,
			String email, String weburl, Date startDate, Date endDate,
			Boolean isLeaf, String remark, String orgStatus, int sn,
			Set<SysPosition> sysPositions, Set<SysOrganization> subOrg,
			Set<SysEmployee> sysEmployees, Set<SysEmpOrg> sysEmpOrgs,Set<SysOrganization> childrenOrg,Set<Duty> dutys) {
		super();
		this.parentOrg = parentOrg;
		this.orgcode = orgcode;
		this.orgname = orgname;
		this.orgLevel = orgLevel;
		this.orgseq = orgseq;
		this.orgType = orgType;
		this.forum = forum;
		this.region = region;
		this.posiid = posiid;
		this.empid = empid;
		this.address = address;
		this.zipcode = zipcode;
		this.linkTel = linkTel;
		this.linkMan = linkMan;
		this.email = email;
		this.weburl = weburl;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isLeaf = isLeaf;
		this.remark = remark;
		this.orgStatus = orgStatus;
		this.sn = sn;
		this.sysPositions = sysPositions;
		this.subOrg = subOrg;
		this.sysEmployees = sysEmployees;
		this.sysEmpOrgs = sysEmpOrgs;
		this.childrenOrg = childrenOrg;
		this.dutys = dutys;
	}

	// Property accessors


	public SysOrganization getParentOrg() {
		return this.parentOrg;
	}

	public void setParentOrg(SysOrganization parentOrg) {
		this.parentOrg = parentOrg;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public int getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(int orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgseq() {
		return this.orgseq;
	}

	public void setOrgseq(String orgseq) {
		this.orgseq = orgseq;
	}

	public String getForum() {
		return forum;
	}

	public void setForum(String forum) {
		this.forum = forum;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getPosiid() {
		return this.posiid;
	}

	public void setPosiid(String posiid) {
		this.posiid = posiid;
	}

	public String getEmpid() {
		return this.empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getLinkTel() {
		return this.linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkMan() {
		return this.linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeburl() {
		return this.weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<SysPosition> getSysPositions() {
		return this.sysPositions;
	}

	public void setSysPositions(Set<SysPosition> sysPositions) {
		this.sysPositions = sysPositions;
	}

	public Set<SysOrganization> getSubOrg() {
		return this.subOrg;
	}

	public void setSubOrg(Set<SysOrganization> subOrg) {
		this.subOrg = subOrg;
	}

	public Set<SysEmployee> getSysEmployees() {
		return this.sysEmployees;
	}

	public void setSysEmployees(Set<SysEmployee> sysEmployees) {
		this.sysEmployees = sysEmployees;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public Set<SysEmpOrg> getSysEmpOrgs() {
		return sysEmpOrgs;
	}

	public void setSysEmpOrgs(Set<SysEmpOrg> sysEmpOrgs) {
		this.sysEmpOrgs = sysEmpOrgs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Set<SysOrganization> getChildrenOrg() {
		return childrenOrg;
	}
	
	public void setChildrenOrg(Set<SysOrganization> childrenOrg) {
		this.childrenOrg = childrenOrg;
	}


	public String getImpErrorInfo() {
		return impErrorInfo;
	}

	public void setImpErrorInfo(String impErrorInfo) {
		this.impErrorInfo = impErrorInfo;
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public Set<RiskOrg> getOrgRisks() {
		return orgRisks;
	}

	public void setOrgRisks(Set<RiskOrg> orgRisks) {
		this.orgRisks = orgRisks;
	}
	
}