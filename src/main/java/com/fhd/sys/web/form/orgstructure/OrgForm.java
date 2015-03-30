/**
 * OrgForm.java
 * com.fhd.fdc.commons.web.form.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-11 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.orgstructure;

import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 机构Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-10  
 * @since    Ver 1.1
 * @Date	 2010-9-10		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class OrgForm extends SysOrganization {

	private static final long serialVersionUID = -4472416746710550572L;
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

