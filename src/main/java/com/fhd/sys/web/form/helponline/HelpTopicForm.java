/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.web.form.helponline;

import java.util.HashMap;
import java.util.Map;

import com.fhd.sys.entity.helponline.HelpTopic;

/**
 * 帮助主题
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-20		下午2:16:56
 *
 * @see 	 
 */

public class HelpTopicForm extends HelpTopic {

	private static final long serialVersionUID = 1L;

	private Map<String,String> types;

	public HelpTopicForm() {
		types = new HashMap<String, String>();
		types.put("catalog", "目录帮助");
		types.put("hint", "关键词提示");
		setType("catalog");
	}
	
	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}
	
	
	
}

