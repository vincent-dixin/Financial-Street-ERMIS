package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 *  评价发生频率	对应样本数量（年度）		
 *  	                                高风险      中风险      低风险
 *  无规律事项	      1	      1	      1
 *  每年一次	          1	      1	      1
 *  每半年一次	      2	      2	      2
 *  每季度一次	      2	      2	      2
 *  每月一次	          5	      3	      2
 *  每周一次	          15	  10	  5
 *  每日一次	          40	  30	  20
 *  每日多次	          60	  45	  25
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:13:03
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_POINT_FREQUENCE")
public class AssessFrequence extends IdEntity implements Serializable {
	private static final long serialVersionUID = -3165226163288486641L;

	
	/**
	 * 编号:与字典类型为'控制频率'的字典项的各Id一一对应
	 */
	@Column(name="FREQUENCE_CODE")
	private String code;
	/**
	 * 名称:
	 * 无规律事项
	 *	每年一次
	 *	每半年一次
	 *	每季度一次
	 *	每月一次
	 *	每周一次
	 *	每日一次
	 *	每日多次 
	 */
	@Column(name="FREQUENCE_NAME")
	private String name;

	
	/**
	 * 抽样最高比例
	 */
	@Column(name="AMOUNT_MAX")
	private Double amountMax;
	
	/**
	 * 抽样中间比例
	 */
	@Column(name="AMOUNT_MIDDLE")
	private Double amountMiddle;
	/**
	 * 抽样最低比例
	 */
	@Column(name="AMOUNT_MIN")
	private Double amountMin;
	
	public AssessFrequence(){
		
	}
	
	public AssessFrequence(String id){
		super.setId(id);
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

	public void setName(String name) {
		this.name = name;
	}

	public Double getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(Double amountMax) {
		this.amountMax = amountMax;
	}

	public Double getAmountMiddle() {
		return amountMiddle;
	}

	public void setAmountMiddle(Double amountMiddle) {
		this.amountMiddle = amountMiddle;
	}

	public Double getAmountMin() {
		return amountMin;
	}

	public void setAmountMin(Double amountMin) {
		this.amountMin = amountMin;
	}
	
	

}

