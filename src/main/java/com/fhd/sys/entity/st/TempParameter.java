/**
 * TempParameter.java
 * com.fhd.sys.entity.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.st;
/**
 * 任务计划模版参数对应实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-11		下午01:04:57
 *
 * @see 	 
 */
public class TempParameter {
	/**
	 * 参数id
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	private String id;
	
	/**
	 * 参数字段
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	private String parameter;
	
	/**
	 * 参数描述 
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	private String describe;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}	
}