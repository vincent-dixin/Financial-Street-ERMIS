/**
 * IconBO.java
 * com.fhd.sys.business.icon
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.sys.dao.icon.IconDAO;
import com.fhd.sys.entity.icon.Icon;
import com.fhd.sys.interfaces.IIconBO;
import com.fhd.test.entity.TestMvc;

/**
 * ClassName:IconBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:54:44
 *
 * @see 	 
 */
@Service
public class IconBO implements IIconBO{
	@Autowired
	private IconDAO o_iconDao;

	/**
	 * <pre>
	 * findIconByQuery:查找图标列表
	 * </pre>
	 * 
	 * @author 王 钊
	 * @param query 模糊查询样式名称
	 * @param sort
	 * @param page
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Page<Icon> findIconByQuery(String query,String sort,Page<Icon> page) {
		
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		
		
		DetachedCriteria dc = DetachedCriteria.forClass(Icon.class);
		if(StringUtils.isNotBlank(query))
		{
			dc.add(Restrictions.like("css",query, MatchMode.ANYWHERE));
		}
		
		if(null!=sortList){
			for (Map<String, String> msort : sortList) {
				String property=msort.get("property");
				String direction = msort.get("direction");
				if(StringUtils.isNotBlank(property) && StringUtils.isNotBlank(direction)){
					if("fileTypeDictEntryName".equalsIgnoreCase(property)){
						property="fileType.sort";
					}
					if("desc".equalsIgnoreCase(direction)){
						dc.addOrder(Order.desc(property));
					}else{
						dc.addOrder(Order.asc(property));
					}
				}
			}
		}else{
			dc.addOrder(Order.desc("css"));
		}
		return o_iconDao.findPage(dc, page, false);
	}

}

