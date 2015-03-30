/**
 * IEmployeeBO.java
 * com.fhd.sys.interfaces
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-3-28 		胡迪新
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.interfaces;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 员工操作接口
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-3-28		上午10:49:27
 *
 * @see 	 
 */
@Service
public interface IEmployeeBO {
	/**
	 * 
	 * <pre>
	 * 根据主键ID获取员工
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee get(Serializable id);	
}