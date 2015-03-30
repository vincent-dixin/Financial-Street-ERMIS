/**
 * FHDException.java
 * com.fhd.fdc.exception
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-5-12 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/



package com.fhd.fdc.commons.exception;
/**
 * 
 * 异常处理类
 * 
 * TODO 未完成
 * 
 * @author	 Vincent
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-5-12		下午01:25:23
 *
 * @see 	 
 */

public class FHDException extends RuntimeException {
	private static final long serialVersionUID = -4336118053280338283L;

	/**
	 * 
	 * 空构造函数.
	 *
	 */
	public FHDException(){
		super();
	}
	
	/**
	 * 
	 * 包含错误信息的构造函数
	 *
	 * @param errorMsg 错误信息
	 */
	public FHDException(String errorMsg){
		super(errorMsg);
	}
	
	/**
	 * 
	 * 包含错误原因的构造函数.
	 *
	 * @param cause
	 */
	public FHDException(Throwable cause){
		super(cause);
	}
	
	/**
	 * 
	 * 包含错误信息和错误原因的构造函数
	 *
	 * @param errorMsg
	 * @param cause
	 */
	public FHDException(String errorMsg,Throwable cause){
		super(errorMsg,cause);
	}
}

