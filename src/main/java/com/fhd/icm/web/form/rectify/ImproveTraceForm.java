/**
 * ImproveTraceForm.java
 * com.fhd.icm.web.form.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-20 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.form.rectify;

import com.fhd.icm.entity.rectify.ImproveTrace;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-20		下午3:29:44
 *
 * @see 	 
 */
public class ImproveTraceForm extends ImproveTrace {

	private static final long serialVersionUID = 1001035427979326487L;
	private String improvePlanId;

	public String getImprovePlanId() {
		return improvePlanId;
	}
	public void setImprovePlanId(String improvePlanId) {
		this.improvePlanId = improvePlanId;
	}
	
}

