/**   
* @Title: ClipingForm.java 
* @Package com.fhd.fdc.commons.web.form.sys.cliping 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 张雷 
* @date 2011-3-22 下午03:25:43 
* @version v1.0 
*/ 
package com.fhd.sys.web.form.cliping;

import com.fhd.sys.entity.cliping.Cliping;

/** 
 * @ClassName: ClipingForm 
 * @Description: 列表和功能Form类
 * @author 张雷
 * @date 2011-3-22 下午03:25:43 
 *  
 */

public class ClipingForm extends Cliping {
	/** 
	* @Fields serialVersionUID : 序列号ID 
	*/ 
	private static final long serialVersionUID = 4140883560160308710L;
	/** 
	* @Fields parentId : 父ID
	*/ 
	private String parentId;
	/** 
	 * @return parentId 
	 */
	
	public String getParentId() {
		return parentId;
	}
	/** 
	 * @param parentId 要设置的 parentId 
	 */
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
