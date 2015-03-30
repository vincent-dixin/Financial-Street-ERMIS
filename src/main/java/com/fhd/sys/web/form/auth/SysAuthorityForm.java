/**
 * SysAuthorityForm.java
 * com.fhd.fdc.commons.web.form.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.auth;

import com.fhd.sys.entity.auth.SysAuthority;

/**
 * 权限Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */

public class SysAuthorityForm extends SysAuthority {

	private static final long serialVersionUID = 1603087104140883560L;
	/**
	 * 父id.
	 */
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}

