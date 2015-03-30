/**
 * I18nForm.java
 * com.fhd.sys.web.form.i18n
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * I18nForm.java
 * com.fhd.sys.web.form.i18n
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.form.i18n;

import com.fhd.sys.entity.i18n.I18n;

/**
 * ClassName:I18nForm
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:17:33
 *
 * @see 	 
 */
/**
 * ClassName:I18nForm
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-11-20		上午10:17:33
 *
 * @see 	 
 */

public class I18nForm extends I18n{
	private static final long serialVersionUID = 1L;
	
	public I18nForm(I18n i18n){
		this.setId(i18n.getId());
		this.setObjectType(i18n.getObjectType());
		this.setObjectKey(i18n.getObjectKey());
		this.setObjectCn(i18n.getObjectCn());
		this.setObjectEn(i18n.getObjectEn());
	}
}

