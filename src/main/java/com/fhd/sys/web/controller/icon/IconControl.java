/**
 * IconControl.java
 * com.fhd.sys.web.controller.icon
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.icon.Icon;
import com.fhd.sys.interfaces.IIconBO;


/**
 * ClassName:IconControl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:55:33
 *
 * @see 	 
 */
@Controller
public class IconControl {
	
	@Autowired
	IIconBO o_iconBO;
	
	/**
	 * 
	 * <pre>
	 * loadIcon:加载树列表，待查询条件query
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param start
	 * @param limit
	 * @param sort
	 * @param query
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/icon/iconList.f")
	public Map<String,Object> loadIcon(int start,int limit,String sort,String query)
	{
		Page<Icon> page = new Page<Icon>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		Page<Icon> list = o_iconBO.findIconByQuery(query,sort,page);


		List<Icon> entityList = list.getResult();
		List<Icon> datas = new ArrayList<Icon>();
		for(Icon de : entityList){
			datas.add(de);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);

		return map;
	}
}

