/**
 * FileUploadBean.java
 * com.fhd.fdc.utils.fileupload
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-2-17 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/
/**
 * FileUploadBean.java
 * com.fhd.fdc.utils.fileupload
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-2-17        David
 *
 * Copyright (c) 2011, FirstHuida All Rights Reserved.
*/


package com.fhd.fdc.utils.fileupload;

import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName:FileUploadBean
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-2-17		下午05:07:01
 *
 * @see 	 
 */

public class FileUploadBean {
	private MultipartFile uploadFile;

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
}

