package com.fhd.risk.web.form;

import com.fhd.risk.entity.TemplateRelaDimension;

/**
 * 模板关联维度FORM
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-16		下午4:25:46
 *
 * @see 	 
 */
public class TemplateRelaDimensionForm extends TemplateRelaDimension {
	private static final long serialVersionUID = -7566681737906911048L;

	/**
	 * 计算方法ID
	 */
	private String calculateMethodId;
	
	/**
	 * 显示的维度名称
	 */
	private String name;

	public String getCalculateMethodId() {
		return calculateMethodId;
	}

	public void setCalculateMethodId(String calculateMethodId) {
		this.calculateMethodId = calculateMethodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}

