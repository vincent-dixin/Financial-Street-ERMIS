/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.fdc.commons.exception;

/**
 * ClassName:RiskException
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-19		下午3:06:22
 *
 * @see 	 
 */

public class RiskException extends FHDException {

	/**
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 空构造函数.
	 *
	 */
	public RiskException(){
		super();
	}
	
	/**
	 * 
	 * 包含错误信息的构造函数
	 *
	 * @param errorMsg 错误信息
	 */
	public RiskException(String errorMsg){
		super(errorMsg);
	}
	
	/**
	 * 
	 * 包含错误原因的构造函数.
	 *
	 * @param cause
	 */
	public RiskException(Throwable cause){
		super(cause);
	}
	
	/**
	 * 
	 * 包含错误信息和错误原因的构造函数
	 *
	 * @param errorMsg
	 * @param cause
	 */
	public RiskException(String errorMsg,Throwable cause){
		super(errorMsg,cause);
	}
}

