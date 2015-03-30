/**
 * ISyncTask.java
 * com.fhd.bpm.interfaces
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-4-12 		胡迪新
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.bpm.interfaces;

import org.springframework.stereotype.Service;


/**
 * ClassName: ISyncBpm
 * 工作流同步接口
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-4-12		上午10:18:17
 *
 * @see 	 
 */
@Service
public interface ISyncBpm {

	public abstract void syncTask();
	
}

