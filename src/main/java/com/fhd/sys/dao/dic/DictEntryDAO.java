/**
 * DictEntryDAO.java
 * com.fhd.fdc.commons.dao.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.dic;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 数据字典条目DAO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-8-14 
 * @since    Ver 1.1
 * @Date	 2010-8-14		下午12:38:50
 * Company FirstHuiDa.
 * @see 	 
 */

@Repository
@SuppressWarnings("unchecked")
public class DictEntryDAO extends HibernateDao<DictEntry,String>{

	/**
	 * <pre>
	 * queryMaxSortNo:查询当前最大的排列序号，新增时使用.
	 * </pre>
	 * @author 吴德福
	 * @return Integer
	 * @since  fhd　Ver 1.1
	 */
	public Integer queryMaxSortNo(){
		int i = 0;
		List<Integer> sortNoList = new ArrayList<Integer>();
		Query query = null;
		query = this.getSession().createQuery("select max(sortNo) From DictEntry ");
		sortNoList = query.list();
		for(Integer tempI : sortNoList){
			if(null != tempI){
				i = tempI;
			}
		}
		return i;
	}
	/**
	 * <pre>
	 * getUsableDicByType:获取一类有效的
	 * </pre>
	 * @author 万业
	 * @param typeTitle
	 * @return List<DictEntry>
	 */
	public List<DictEntry> getUsableDicByType(String typeTitle){
		
		String dichql="select dic from DictEntry dic where dic.dictType.dictTypeTitle='"+typeTitle+"' and dic.status=1";
		Query query=null;
			query= this.getSession().createQuery(dichql);
		List<DictEntry> listdic= query.list();
		return listdic;
		
	}
	
	/**
	 * <pre>
	 * getDictEntryByValue:由值获取
	 * </pre>
	 * 
	 * @author 金鹏祥
	 * @param value
	 * @return DictEntry
	 * @since  fhd　Ver 1.1
	*/
	public DictEntry getDictEntryByValue(String value){
		String dichql="select dic from DictEntry dic where dic.dictEntryValue='"+value+"' and dic.status=1";
		DictEntry dictEntry = (DictEntry)this.getSession().createQuery(dichql).uniqueResult();
		return dictEntry;
	}
	
}

