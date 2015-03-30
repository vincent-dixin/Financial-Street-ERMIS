/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.fdc.commons.exception;

/**
 * 指标模块异常处理类
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-18		下午4:14:30
 *
 * @see 	 
 */

public class KpiException extends FHDException {

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
	public KpiException(){
		super();
	}
	
	/**
	 * 
	 * 包含错误信息的构造函数
	 *
	 * @param errorMsg 错误信息
	 */
	public KpiException(String errorMsg){
		super(errorMsg);
	}
	
	/**
	 * 
	 * 包含错误原因的构造函数.
	 *
	 * @param cause
	 */
	public KpiException(Throwable cause){
		super(cause);
	}
	
	/**
	 * 
	 * 包含错误信息和错误原因的构造函数
	 *
	 * @param errorMsg
	 * @param cause
	 */
	public KpiException(String errorMsg,Throwable cause){
		super(errorMsg,cause);
	}
	
}

