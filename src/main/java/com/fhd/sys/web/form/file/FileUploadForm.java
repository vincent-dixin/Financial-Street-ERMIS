/**
 * FileUploadForm.java
 * com.fhd.fdc.commons.web.form.sys.file
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-8 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.file;

import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 文件Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-8  
 * @since    Ver 1.1
 * @Date	 2010-9-8		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class FileUploadForm extends FileUploadEntity{

	private static final long serialVersionUID = 3930607442407832094L;
	/**
	 * 上传类型.
	 */
	private String chooseType;
	/**
	 * 上传方式.
	 */
	private String chooseWay;
	/**
	 * 新文件名称.
	 */
	private String aliasName;
	/**
	 * 错误提示.
	 */
	private String errorTips;
	/**
	 * 用户名.
	 */
	private String username;
	
	public String getChooseType() {
		return chooseType;
	}
	
	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}
	
	public String getChooseWay() {
		return chooseWay;
	}
	
	public void setChooseWay(String chooseWay) {
		this.chooseWay = chooseWay;
	}
	
	public String getAliasName() {
		return aliasName;
	}
	
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	public String getErrorTips() {
		return errorTips;
	}
	
	public void setErrorTips(String errorTips) {
		this.errorTips = errorTips;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

