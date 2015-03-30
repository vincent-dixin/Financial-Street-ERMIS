package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 维度分值类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		上午11:32:53
 * @see 	 
 */
@Entity
@Table(name = "T_DIM_SCORE_DIC") 
public class Score extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8320880504877247506L;

	/**
	 * 维度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIM_ID")
	private Dimension dimension;
	
	/**
	 * 名称
	 */
	@Column(name = "SCORE_DIC_NAME")
	private String name;
	
	/**
	 * 值
	 */
	@Column(name = "SCORE_DIC_VALUE")
	private Double value;	
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 描述
	 */
	@Column(name = "EDESC",length=4000)
	private String desc;

	//TODO MIN_VALUE //暂时不确定	
	//TODO MAX_VALUE //暂时不确定
		   
	public Score(){
		
	}
	public Score(String id){
		super.setId(id);
	}
	
	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

