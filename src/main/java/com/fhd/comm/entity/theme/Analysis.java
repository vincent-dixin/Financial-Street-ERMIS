package com.fhd.comm.entity.theme;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 主题分析实体类
 * @author 王再冉
 *
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_COM_THEME_ANALYSIS")
public class Analysis extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 登录人员的公司ID
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company ;
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String analyname ;
	/**
	 * 父节点
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PARENT_ID")
	private Analysis parentAnaly ;
	/**
	 * 描述
	 */
	@Column(name = "EDESC")
	private String analydesc ;
	/**
	 * 类型
	 */
	@Column(name = "ETYPE")
	private String  analyType;
	/**
	 * 序列
	 */
	@Column(name = "ID_SEQ")
	private String  analyseq;
	/**
	 * 删除状态
	 */
	@Column(name = "DELETE_STATUS")
	private String  deleteStatus;
	/**
	 * 主题布局信息中间表
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "theme")
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@BatchSize(size = 10)
	private Set<ThemeRelaLayout> themeRelaLayout = new HashSet<ThemeRelaLayout>(0);
	
	public Analysis(){
		
	}
	
	public Analysis(SysOrganization company,String analyname,Analysis parentAnaly,
					String analydesc,String  analyType,String  analyseq, String deleteStatus){
		super();
		this.company = company;
		this.analyname = analyname;
		this.parentAnaly = parentAnaly;
		this.analydesc = analydesc;
		this.analyType = analyType;
		this.analyseq = analyseq;
		this.deleteStatus = deleteStatus;
	}
	
	
	
	public SysOrganization getCompany() {
		return company;
	}
	public void setCompany(SysOrganization company) {
		this.company = company;
	}



	public String getAnalyname() {
		return analyname;
	}



	public void setAnalyname(String analyname) {
		this.analyname = analyname;
	}



	public Analysis getParentAnaly() {
		return parentAnaly;
	}



	public void setParentAnaly(Analysis parentAnaly) {
		this.parentAnaly = parentAnaly;
	}



	public String getAnalydesc() {
		return analydesc;
	}



	public void setAnalydesc(String analydesc) {
		this.analydesc = analydesc;
	}



	public String getAnalyType() {
		return analyType;
	}



	public void setAnalyType(String analyType) {
		this.analyType = analyType;
	}



	public String getAnalyseq() {
		return analyseq;
	}



	public void setAnalyseq(String analyseq) {
		this.analyseq = analyseq;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Set<ThemeRelaLayout> getThemeRelaLayout() {
		return themeRelaLayout;
	}

	public void setThemeRelaLayout(Set<ThemeRelaLayout> themeRelaLayout) {
		this.themeRelaLayout = themeRelaLayout;
	}
	
	
	
	
}




