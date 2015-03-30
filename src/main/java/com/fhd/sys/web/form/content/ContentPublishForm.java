/**
 * ContentPublishForm.java
 * com.fhd.fdc.commons.web.form.sys.content
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-15 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.content;

import com.fhd.sys.entity.content.ContentPublish;

/**
 * 内容发布Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-10-15  
 * @since    Ver 1.1
 * @Date	 2010-10-15		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class ContentPublishForm extends ContentPublish{

	private static final long serialVersionUID = 7370672274252612097L;
	/**
	 * 用户名.
	 */
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}

