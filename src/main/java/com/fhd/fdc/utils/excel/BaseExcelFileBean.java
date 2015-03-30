/**
 * BaseExcelFileBean.java
 * com.fhd.fdc.commons.entity
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-2-22        David
 *
 * Copyright (c) 2011, FirstHuida All Rights Reserved.
*/


package com.fhd.fdc.utils.excel;
/**
 * Excel导入基础类
 * ClassName:BaseExcelFileBean
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-2-22		上午11:43:29
 *
 * @see 	 
 */

public class BaseExcelFileBean {
	private boolean dataCheckStatus = true;//数据验证状态
	private String dataCheckErrorMessage;//数据验证错误信息
	
	public boolean isDataCheckStatus() {
		return dataCheckStatus;
	}
	public void setDataCheckStatus(boolean dataCheckStatus) {
		this.dataCheckStatus = dataCheckStatus;
	}
	public String getDataCheckErrorMessage() {
		return dataCheckErrorMessage;
	}
	public void setDataCheckErrorMessage(String dataCheckErrorMessage) {
		this.dataCheckErrorMessage = dataCheckErrorMessage;
	}
}

