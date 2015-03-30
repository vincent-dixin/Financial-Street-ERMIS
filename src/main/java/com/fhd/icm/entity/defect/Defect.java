package com.fhd.icm.entity.defect;

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

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 缺陷
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:49:53
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_DEFECT")
public class Defect extends AuditableEntity implements Serializable {
	private static final long serialVersionUID = 636494516683985642L;

	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	/**
	 * 编号
	 */
	@Column(name="DEFECT_CODE")
	private String code;	
	
	/**
	 * 缺陷描述
	 */
	@Column(name="EDESC",length=4000)
	private String desc;	
		
	
	/**
	 * 缺陷等级：重大缺陷，重要缺陷，一般缺陷，例外事项
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_LEVEL")
	private DictEntry level;	
	
	/**
	 * 缺陷类型：
	 * 设计缺陷；执行缺陷；双重缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_TYPE")
	private DictEntry type;	
	
	/**
	 * 设计缺陷：
	 * 无缺陷；
	 * 无制度；
	 * 已过时；
	 * 有制度，
	 * 无更新；
	 * 不完善；
	 * 有待细化
	 */
	@Column(name="DESIGN_DEFECT")
	private String designDefect;		
			
	/**
	 * 执行缺陷：
	 * 无缺陷；
	 * 未实质性执行；
	 * 证据不及时；
	 * 证据不全；
	 * 无证据；
	 * 无依据
	 */
	@Column(name="EXECUTE_DEFECT")
	private String executeDefect;	
	
	/**
	 * 缺陷分析
	 */
	@Column(name="DEFECT_ANALYZE",length=4000)
	private String defectAnalyze;	
		
	/**
	 * 改进建议
	 */
	@Column(name="IMPROVE_ADVICE",length=4000)
	private String improveAdivce;		
				
	/**
	 * 排序
	 */
	@Column(name="ESORT")
	private Integer sort;			
	
	/**
	 * 执行状态
	 */
	@Column(name="DEAL_STATUS")
	private String dealStatus;	
	
	/**
	 * 状态
	 */
	@Column(name="ESTATUS")
	private String status;	
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;
	
	/**
	 * 评价关联的缺陷
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defect")
	private Set<AssessRelaDefect> assessRelaDefect = new HashSet<AssessRelaDefect>(0);
	
	/**
	 * 整改计划关联的缺陷
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defect")
	private Set<DefectRelaImprove> defectRelaImprove = new HashSet<DefectRelaImprove>(0);
	
	/**
	 * 缺陷关联的部门人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defect")
	private Set<DefectRelaOrg> defectRelaOrg = new HashSet<DefectRelaOrg>(0);
	
	/**
	 * 整改方案关联的缺陷
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defect")
	private Set<ImprovePlanRelaDefect> improvePlanRelaDefect = new HashSet<ImprovePlanRelaDefect>(0);
	
	public Defect(){
		
	}
	
	public Defect(String id){
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public DictEntry getLevel() {
		return level;
	}

	public void setLevel(DictEntry level) {
		this.level = level;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}

	public String getDesignDefect() {
		return designDefect;
	}

	public void setDesignDefect(String designDefect) {
		this.designDefect = designDefect;
	}

	public String getExecuteDefect() {
		return executeDefect;
	}

	public void setExecuteDefect(String executeDefect) {
		this.executeDefect = executeDefect;
	}

	public String getDefectAnalyze() {
		return defectAnalyze;
	}

	public void setDefectAnalyze(String defectAnalyze) {
		this.defectAnalyze = defectAnalyze;
	}

	public String getImproveAdivce() {
		return improveAdivce;
	}

	public void setImproveAdivce(String improveAdivce) {
		this.improveAdivce = improveAdivce;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Set<AssessRelaDefect> getAssessRelaDefect() {
		return assessRelaDefect;
	}

	public void setAssessRelaDefect(Set<AssessRelaDefect> assessRelaDefect) {
		this.assessRelaDefect = assessRelaDefect;
	}

	public Set<DefectRelaImprove> getDefectRelaImprove() {
		return defectRelaImprove;
	}

	public void setDefectRelaImprove(Set<DefectRelaImprove> defectRelaImprove) {
		this.defectRelaImprove = defectRelaImprove;
	}

	public Set<DefectRelaOrg> getDefectRelaOrg() {
		return defectRelaOrg;
	}

	public void setDefectRelaOrg(Set<DefectRelaOrg> defectRelaOrg) {
		this.defectRelaOrg = defectRelaOrg;
	}

	public Set<ImprovePlanRelaDefect> getImprovePlanRelaDefect() {
		return improvePlanRelaDefect;
	}

	public void setImprovePlanRelaDefect(
			Set<ImprovePlanRelaDefect> improvePlanRelaDefect) {
		this.improvePlanRelaDefect = improvePlanRelaDefect;
	}
	
}

