package com.fhd.sys.entity.orgstructure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.group.SysGroup;

/**
 * 员工实体类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-9-8 Company FirstHuiDa.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "T_SYS_EMPLOYEE")
public class SysEmployee extends IdEntity implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	/**
	 * 导入时报错信息
	 */
	@Transient
	private String impErrorInfo;
	
	/**
	 * 机构.
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
	/**
	 * 员工编号.
	 */
	@Column(name = "EMP_CODE", length = 30)
	private String empcode;
	/**
	 * 用户id.
	 */
	@Column(name = "USER_ID", length = 32)
	private String userid;
	
	@JoinColumn(name = "USER_ID",insertable = false, updatable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private SysUser sysUser;
	
	/**
	 * 用户名.
	 */
	@Column(name = "USER_NAME", length = 30)
	private String username;
	/**
	 * 员工姓名.
	 */
	@Column(name = "EMP_NAME", length = 50)
	private String empname;
	/**
	 * 真实姓名.
	 */
	@Column(name = "REAL_NAME", length = 50)
	private String realname;
	/**
	 * 性别.
	 */
	@Column(name = "GENDER")
	private String gender;
	/**
	 * 生日.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTH_DATE")
	private Date birthdate;
	/**
	 * 状态.
	 */
	@Column(name = "ESTATUS")
	private String empStatus;
	/**
	 * 证件类型.
	 */
	@Column(name = "CARD_TYPE")
	private String cardtype;
	/**
	 * 证件号码.
	 */
	@Column(name = "CARD_NO")
	private String cardno;
	/**
	 * 公司电话.
	 */
	@Column(name = "OTEL", length = 20)
	private String otel;
	/**
	 * 公司地址.
	 */
	@Column(name = "OADDRESS", length = 500)
	private String oaddress;
	/**
	 * 公司邮编.
	 */
	@Column(name = "OZIPCODE", length = 10)
	private String ozipcode;
	/**
	 * 公司邮箱.
	 */
	@Column(name = "OEMAIL", length = 128)
	private String oemail;
	/**
	 * 传真号码.
	 */
	@Column(name = "FAXNO", length = 20)
	private String faxno;
	/**
	 * 手机号.
	 */
	@Column(name = "MOBILE_NO", length = 14)
	private String mobikeno;
	/**
	 * MSN.
	 */
	@Column(name = "MSN", length = 128)
	private String msn;
	/**
	 * 家庭电话
	 */
	@Column(name = "HTEL", length = 20)
	private String htel;
	/**
	 * 家庭地址.
	 */
	@Column(name = "HADDRESS", length = 500)
	private String haddress;
	/**
	 * 家庭邮编.
	 */
	@Column(name = "HZIPCODE", length = 10)
	private String hzipcode;
	/**
	 * 个人邮箱.
	 */
	@Column(name = "PEMAIL", length = 128)
	private String pemail;
	/**
	 * 政治面貌.
	 */
	@Column(name = "PARTY", length = 100)
	private String party;
	/**
	 * 学位.
	 */
	@Column(name = "DEGREE", length = 100)
	private String degree;
	/**
	 * 专业.
	 */
	@Column(name = "MAJOR", length = 32)
	private String major;
	/**
	 * 特长.
	 */
	@Column(name = "SPECIALTY", length = 1024)
	private String specialty;
	/**
	 * 注册日期.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGDATE")
	private Date regdate;
	/**
	 * 备注.
	 */
	@Column(name = "ECOMMENT", length = 1024)
	private String remark;
	/**
	 * 职务
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DUTY_ID")
	private Duty duty;

	/**
	 * 岗位员工中间表集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysEmployee")
	private Set<SysEmpPosi> sysEmpPosis = new HashSet<SysEmpPosi>(0);
	/**
	 * 机构员工中间表集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysEmployee")
	private Set<SysEmpOrg> sysEmpOrgs = new HashSet<SysEmpOrg>(0);
	/**
	 * 工作组(多对多关系维护).
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sysEmployees")
	private Set<SysGroup> sysGroups = new HashSet<SysGroup>(0);
	// Constructors

	/** default constructor */
	public SysEmployee() {
	}
	
	public SysEmployee(String id) {
		this.setId(id);
	}

	/** full constructor */
	public SysEmployee(SysOrganization sysOrganization, String empcode, String userid, String username, String empname, String realname, String gender, Date birthdate, String empStatus, String cardtype, String cardno, String otel, String oaddress, String ozipcode, String oemail, String faxno,
			String mobikeno, String msn, String htel, String haddress, String hzipcode, String pemail, String party, String degree, String major, String specialty, Date regdate, String remark, Set<SysEmpPosi> sysEmpPosis, Set<SysEmpOrg> sysEmpOrgs) {
		super();
		this.sysOrganization = sysOrganization;
		this.empcode = empcode;
		this.userid = userid;
		this.username = username;
		this.empname = empname;
		this.realname = realname;
		this.gender = gender;
		this.birthdate = birthdate;
		this.empStatus = empStatus;
		this.cardtype = cardtype;
		this.cardno = cardno;
		this.otel = otel;
		this.oaddress = oaddress;
		this.ozipcode = ozipcode;
		this.oemail = oemail;
		this.faxno = faxno;
		this.mobikeno = mobikeno;
		this.msn = msn;
		this.htel = htel;
		this.haddress = haddress;
		this.hzipcode = hzipcode;
		this.pemail = pemail;
		this.party = party;
		this.degree = degree;
		this.major = major;
		this.specialty = specialty;
		this.regdate = regdate;
		this.remark = remark;
		this.sysEmpPosis = sysEmpPosis;
		this.sysEmpOrgs = sysEmpOrgs;
	}


	public Set<SysGroup> getSysGroups() {
		return sysGroups;
	}

	public void setSysGroups(Set<SysGroup> sysGroups) {
		this.sysGroups = sysGroups;
	}

	public SysOrganization getSysOrganization() {
		return this.sysOrganization;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}

	public String getEmpcode() {
		return this.empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmpname() {
		return this.empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getCardtype() {
		return this.cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardno() {
		return this.cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getOtel() {
		return this.otel;
	}

	public void setOtel(String otel) {
		this.otel = otel;
	}

	public String getOaddress() {
		return this.oaddress;
	}

	public void setOaddress(String oaddress) {
		this.oaddress = oaddress;
	}

	public String getOzipcode() {
		return this.ozipcode;
	}

	public void setOzipcode(String ozipcode) {
		this.ozipcode = ozipcode;
	}

	public String getOemail() {
		return this.oemail;
	}

	public void setOemail(String oemail) {
		this.oemail = oemail;
	}

	public String getFaxno() {
		return this.faxno;
	}

	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getMobikeno() {
		return this.mobikeno;
	}

	public void setMobikeno(String mobikeno) {
		this.mobikeno = mobikeno;
	}

	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getHtel() {
		return this.htel;
	}

	public void setHtel(String htel) {
		this.htel = htel;
	}

	public String getHaddress() {
		return this.haddress;
	}

	public void setHaddress(String haddress) {
		this.haddress = haddress;
	}

	public String getHzipcode() {
		return this.hzipcode;
	}

	public void setHzipcode(String hzipcode) {
		this.hzipcode = hzipcode;
	}

	public String getPemail() {
		return this.pemail;
	}

	public void setPemail(String pemail) {
		this.pemail = pemail;
	}

	public String getParty() {
		return this.party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSpecialty() {
		return this.specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Date getRegdate() {
		return this.regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<SysEmpPosi> getSysEmpPosis() {
		return this.sysEmpPosis;
	}

	public void setSysEmpPosis(Set<SysEmpPosi> sysEmpPosis) {
		this.sysEmpPosis = sysEmpPosis;
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

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String getImpErrorInfo() {
		return impErrorInfo;
	}

	public void setImpErrorInfo(String impErrorInfo) {
		this.impErrorInfo = impErrorInfo;
	}

}