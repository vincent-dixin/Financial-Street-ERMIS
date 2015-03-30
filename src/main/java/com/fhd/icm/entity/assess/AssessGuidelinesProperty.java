/**
 * AssessGuidelinesProperty.java
 * com.fhd.icm.entity.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-29 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;


/**
 * 评价标准模板对应的评价标准项
 * 
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-3-29		下午5:33:37
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_GUIDELINES_PROPERTY")
public class AssessGuidelinesProperty extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = -1071287043546439973L;

	/**
	 * 所属评价标准模板
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GUIDELINES_ID")
	private AssessGuidelines assessGuidelines;
	
	/**
	 * 模板类型为缺陷相关时用，缺陷等级：重大缺陷，重要缺陷，一般缺陷，例外事项
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_LEVEL")
	private DictEntry defectLevel;	
	
	/**
	 * 描述
	 */
	@Column(name="CONTENT", length = 4000)
	private String content;
	
	/**
	 * 区间最小值
	 */
	@Column(name="MIN_VALUE")
	private Double minValue;
	
	/**
	 * 区间最大值
	 */
	@Column(name="MAX_VALUE")
	private Double maxValue;
	
	
	/**
	 * 判定值
	 */
	@Column(name="JUDGE_VALUE")
	private Double judgeValue; 
	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	public AssessGuidelinesProperty(){
		
	}
	
	public AssessGuidelinesProperty(String id){
		super.setId(id);
	}

	public AssessGuidelines getAssessGuidelines() {
		return assessGuidelines;
	}

	public void setAssessGuidelines(AssessGuidelines assessGuidelines) {
		this.assessGuidelines = assessGuidelines;
	}

	public DictEntry getDefectLevel() {
		return defectLevel;
	}

	public void setDefectLevel(DictEntry defectLevel) {
		this.defectLevel = defectLevel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getJudgeValue() {
		return judgeValue;
	}

	public void setJudgeValue(Double judgeValue) {
		this.judgeValue = judgeValue;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}

