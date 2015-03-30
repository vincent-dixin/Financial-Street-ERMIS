package com.fhd.sys.web.form.group;

import com.fhd.sys.entity.group.SysGroup;

/**
 * 
 * @ClassName GroupForm.java
 * @Version 1.0
 * @author zhaotao
 * @Date 2011-1-19
 */
public class GroupForm extends SysGroup {
	private String parentId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
