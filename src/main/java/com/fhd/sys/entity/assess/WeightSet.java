package com.fhd.sys.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

@Entity
@Table(name = "T_SYS_WEIGHT_SET")
public class WeightSet extends IdEntity implements Serializable {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 金鹏祥
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 责任部门权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "DUTY_DEPT_WEIGHT", nullable = false)
	private String objectKey;
	
	/**
	 * 相关部门权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "RELATED_DEPT_WEIGHT", nullable = false)
	private String relatedDeptWeight;
	
	/**
	 * 辅助部门权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "ASSIST_DEPT_WEIGHT", nullable = false)
	private String assistDeptWeight;
	
	/**
	 * 参与部门权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "PARTAKE_DEPT_WEIGHT", nullable = false)
	private String partakeDeptWeight;
	
	/**
	 * 领导权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "LEAD_WEIGHT", nullable = false)
	private String leadWeight;
	
	/**
	 * 员工权重
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "STAFF_WEIGHT", nullable = false)
	private String staffWeight;

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String getRelatedDeptWeight() {
		return relatedDeptWeight;
	}

	public void setRelatedDeptWeight(String relatedDeptWeight) {
		this.relatedDeptWeight = relatedDeptWeight;
	}

	public String getAssistDeptWeight() {
		return assistDeptWeight;
	}

	public void setAssistDeptWeight(String assistDeptWeight) {
		this.assistDeptWeight = assistDeptWeight;
	}

	public String getPartakeDeptWeight() {
		return partakeDeptWeight;
	}

	public void setPartakeDeptWeight(String partakeDeptWeight) {
		this.partakeDeptWeight = partakeDeptWeight;
	}

	public String getLeadWeight() {
		return leadWeight;
	}

	public void setLeadWeight(String leadWeight) {
		this.leadWeight = leadWeight;
	}

	public String getStaffWeight() {
		return staffWeight;
	}

	public void setStaffWeight(String staffWeight) {
		this.staffWeight = staffWeight;
	}
}
