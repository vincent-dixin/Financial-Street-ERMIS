package com.fhd.comm.entity.formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 公式计算对象关系表.
 * @author   吴德福
 * @since    fhd Ver 4.5
 * @Date	 2012-12-8  下午20:28:47
 * @see 	 
 */
@Entity
@Table(name = "T_COM_FORMULA_OBJECT_RELATION")
public class FormulaObjectRelation extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 1430338353935260720L;
	/**
	 * 对象id
	 */
	@Column(name = "OWNER_OBJECT_ID")
	private String objectId;
	/**
	 * 对象类型：kpi/risk
	 */
	@Column(name = "OBJECT_TYPE")
	private String objectType;
	/**
	 * 对象所属列
	 * kpi:
	 * targetValueFormula目标值公式
	 * resultValueFormula结果值公式
	 * assessmentValueFormula评估值公式
	 * risk:
	 * impactsFormula影响程度公式
	 * probabilityFormula发生可能性公式
	 */
	@Column(name = "OWNER_OBJECT_COLUMN")
	private String objectColumn;
	/**
	 * 关联对象id
	 */
	@Column(name = "RELA_OBJECT_ID")
	private String relaObjectId;
	/**
	 * 关联对象类型：kpi/risk
	 */
	@Column(name = "RELA_OBJECT_TYPE")
	private String relaObjectType;
	/**
	 * 关联对象所属列
	 * kpi:
	 * targetValueFormula目标值公式
	 * resultValueFormula结果值公式
	 * assessmentValueFormula评估值公式
	 * risk:
	 * impactsFormula影响程度公式
	 * probabilityFormula发生可能性公式
	 */
	@Column(name = "RELA_OBJECT_COLUMN")
	private String relaObjectColumn;
	
	public FormulaObjectRelation() {
		super();
	}

	public FormulaObjectRelation(String objectId, String objectType,
			String objectColumn, String relaObjectId, String relaObjectType,
			String relaObjectColumn) {
		super();
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectColumn = objectColumn;
		this.relaObjectId = relaObjectId;
		this.relaObjectType = relaObjectType;
		this.relaObjectColumn = relaObjectColumn;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectColumn() {
		return objectColumn;
	}

	public void setObjectColumn(String objectColumn) {
		this.objectColumn = objectColumn;
	}

	public String getRelaObjectId() {
		return relaObjectId;
	}

	public void setRelaObjectId(String relaObjectId) {
		this.relaObjectId = relaObjectId;
	}

	public String getRelaObjectType() {
		return relaObjectType;
	}

	public void setRelaObjectType(String relaObjectType) {
		this.relaObjectType = relaObjectType;
	}

	public String getRelaObjectColumn() {
		return relaObjectColumn;
	}

	public void setRelaObjectColumn(String relaObjectColumn) {
		this.relaObjectColumn = relaObjectColumn;
	}
}
