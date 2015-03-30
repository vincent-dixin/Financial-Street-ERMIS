package com.fhd.fdc.commons.email;

import org.springframework.stereotype.Service;

/**
 * 短信服务类.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Service
public class MsgService {

	/**
	 * 定时发送短信.
	 */
	public void sendMsg(){
		System.out.println("短信定时发送了... ...");
	}
}
