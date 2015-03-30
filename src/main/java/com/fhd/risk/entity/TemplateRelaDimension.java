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

/**
 * 模板关联维度类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午3:13:33
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DIM_TEMPLATE_RELA_DIM") 
public class TemplateRelaDimension extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7503460532224467431L;
	
	/**
	 * 模板
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID")
	private Template template;
	
	/**
	 * 维度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIM_ID")
	private Dimension dimension;
	
	/**
	 * 上级
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_TEMPLATE_DIM_ID")
	private TemplateRelaDimension parent;
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;	      
	
	/**
	 * 层级
	 */
	@Column(name = "ELEVEL")
	private Integer level;
	
	/**
	 * 主键全路径
	 */
	@Column(name = "ID_SEQ")
	private String idSeq;
	
	/**
	 * 是否是叶子
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	
	/**
	 * 是否计算项
	 */
	@Column(name = "IS_CALCULATE")
	private Boolean isCalculate;
	
	/**
	 * 计算方法
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CALCULATE_METHOD")
	private DictEntry calculateMethod;
	
	/**
	 * 描述
	 */
	@Column(name = "ECOMMENT",length=4000)
	private String desc;
		
	/**
	 * 权重
	 */
	@Column(name = "SCORE_DIM_WEIGHT")
	private Double weight;

	/**
	 * 子集
	 */
	@OrderBy("sort ASC")
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<TemplateRelaDimension> children = new HashSet<TemplateRelaDimension>(0);
	
	@OrderBy("id ASC")
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "templateRelaDimension")
	private Set<ScoreInstance> scoreInstanceSet = new HashSet<ScoreInstance>(0);
	
	public TemplateRelaDimension(){
		
	}
	public TemplateRelaDimension(String id){
		super.setId(id);
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public TemplateRelaDimension getParent() {
		return parent;
	}

	public void setParent(TemplateRelaDimension parent) {
		this.parent = parent;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Boolean getIsCalculate() {
		return isCalculate;
	}

	public void setIsCalculate(Boolean isCalculate) {
		this.isCalculate = isCalculate;
	}

	public DictEntry getCalculateMethod() {
		return calculateMethod;
	}

	public void setCalculateMethod(DictEntry calculateMethod) {
		this.calculateMethod = calculateMethod;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Set<TemplateRelaDimension> getChildren() {
		return children;
	}

	public void setChildren(Set<TemplateRelaDimension> children) {
		this.children = children;
	}
	public Set<ScoreInstance> getScoreInstanceSet() {
		return scoreInstanceSet;
	}
	public void setScoreInstanceSet(Set<ScoreInstance> scoreInstanceSet) {
		this.scoreInstanceSet = scoreInstanceSet;
	}
			      
	      

}

