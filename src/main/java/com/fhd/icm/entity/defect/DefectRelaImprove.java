package com.fhd.icm.entity.defect;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.rectify.Improve;

/**
 * 缺陷关联整改结果
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:27:36
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_DEFECT_IMPROVE_PLAN")
public class DefectRelaImprove extends IdEntity implements Serializable {
	private static final long serialVersionUID = -2251682627813832302L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVE_PLAN_ID")
	private Improve improve;

	/**
	 * 评价缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
	
		
			
	public DefectRelaImprove(){
		
	}
	
	public DefectRelaImprove(String id){
		super.setId(id);
	}

	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}

	public Defect getDefect() {
		return defect;
	}

	public void setDefect(Defect defect) {
		this.defect = defect;
	}

}

