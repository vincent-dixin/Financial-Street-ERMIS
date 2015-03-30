/**
 * TempBO.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.business.st;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.st.TempDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.i18n.I18n;
import com.fhd.sys.entity.st.PlanEmp;
import com.fhd.sys.entity.st.Temp;
import com.fhd.sys.interfaces.ITempBO;

/**
 * 计划任务模版业务处理
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012-10-11 下午03:56:44
 * 
 * @see
 */
@Service
public class TempBO implements ITempBO {
	@Autowired
	private TempDAO o_tempDAO;
	
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	
	@Autowired
	private PlanEmpBO o_planEmpBO;

	/**
	 * 
	 * 保存RemindTemplate实体
	 * 
	 * @author 金鹏祥
	 * @param Temp 实体
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveTemp(Temp entry) {
		o_tempDAO.merge(entry);
	}
	
	/**
	 * 
	 * 删除RemindTemplate实体
	 * 
	 * @author 金鹏祥
	 * @param Temp 实体
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public boolean deleteTemp(Map<String, Temp> tempAllMap, String ids) {
		HashMap<String, PlanEmp> planEmpMap = o_planEmpBO.findPlanEmpAllMap();
		String idsStr[] = ids.split(",");
		Temp temp = null;
		try {
			for (String id : idsStr) {
				temp = tempAllMap.get(id);
				if(temp != null){
					o_tempDAO.delete(temp);
					PlanEmp planEmp = planEmpMap.get(temp.getId());
					if(planEmp != null){
						o_planEmpBO.deletePlanEmpBO(planEmp);
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * 更新RemindTemplate实体
	 * 
	 * @author 金鹏祥
	 * @param Temp 实体
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void mergeTemp(Temp entry) {
		o_tempDAO.merge(entry);
	}
	
	/**
	 * 
	 * 查询所有模版MAP
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Temp> findTempAllMap() {
		HashMap<String, Temp> map = new HashMap<String, Temp>();
		Criteria c = o_tempDAO.createCriteria();
		List<Temp> list = null;
		list = c.list();
		for (Temp temp : list) {
			map.put(temp.getDictEntry().getId(), temp);
		}
		return map;
	}
	
	/**
	 * 
	 * 根据字典项ID取DictEntry实体
	 * 
	 * @author 金鹏祥
	 * @param dictEntryId 字典项ID
	 * @return DictEntry
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public DictEntry findDictEntryById(String dictEntryId) {
		Criteria c = o_dictEntryDAO.createCriteria();
		List<DictEntry> list = null;
		
		if (StringUtils.isNotBlank(dictEntryId)) {
			c.add(Restrictions.eq("id", dictEntryId));
		} else {
			return null;
		}
		
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 根据id取Temp实体
	 * 
	 * @author 金鹏祥
	 * @param id TEMP实体ID
	 * @return Temp
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public Temp findTempById(String id) {
		Criteria c = o_tempDAO.createCriteria();
		if (StringUtils.isNotBlank(id)) {
			c.add(Restrictions.eq("id", id));
		} else {
			return null;
		}
		
		List<Temp> list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 根据category取Temp实体
	 * 
	 * @author 金鹏祥
	 * @param dictEntryId 字典项ID
	 * @return Temp
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public Temp findTempByCategory(String dictEntryId) {
		Criteria c = o_tempDAO.createCriteria();
		if (StringUtils.isNotBlank(dictEntryId)) {
			c.add(Restrictions.eq("dictEntry.id", dictEntryId));
		} else {
			return null;
		}
		
		List<Temp> list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 查询temp实体对象集合
	 * 
	 * @author 金鹏祥
	 * @return Temp
	 * @since fhd　Ver 1.1
	 */
	public List<Temp> findTempAll() {
		Criteria c = o_tempDAO.createCriteria();
		@SuppressWarnings("unchecked")
		List<Temp> list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
}