/**
 * EmailTest.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * EmailTest.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st.trigger;

import org.springframework.stereotype.Service;

/**
 * 自定义email接收人
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		下午02:43:35
 *
 * @see 	 
 */
@Service
public class Email {

	/**
	 * EMAIL收件人
	 * 
	 * @author 金鹏祥
	 * @return String[]
	 * @since  fhd　Ver 1.1
	*/
	public String[] getEmailAddresses(){
		String emailAddresses[] = {"jinpengxiangln@163.com"};
		return emailAddresses;
	}
}

