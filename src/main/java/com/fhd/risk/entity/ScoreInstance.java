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
 * 模板关联维度的分值类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午3:15:17
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DIM_SCORE_DIC_INST") 
public class ScoreInstance extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7631054119096705770L;
	
	/**
	 * 维度分值
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIC_ID")
	private Score score;
		    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_RELA_DIM_ID")
	private TemplateRelaDimension templateRelaDimension;
	
	/**
	 * 名称
	 */
	@Column(name = "SCORE_DIC_NAME")
	private String name;
	
	/**
	 * 描述
	 */
	@Column(name = "EDESC",length=2000)
	private String desc;

	public ScoreInstance(){
		
	}
	public ScoreInstance(String id){
		super.setId(id);
	}
	
	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public TemplateRelaDimension getTemplateRelaDimension() {
		return templateRelaDimension;
	}

	public void setTemplateRelaDimension(TemplateRelaDimension templateRelaDimension) {
		this.templateRelaDimension = templateRelaDimension;
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
	
	
}

