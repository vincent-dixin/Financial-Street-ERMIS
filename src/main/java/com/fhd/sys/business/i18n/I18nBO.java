/**
 * I18nBO.java
 * com.fhd.sys.business.i18n
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * I18nBO.java
 * com.fhd.sys.business.i18n
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.sys.dao.i18n.I18nDAO;
import com.fhd.sys.entity.i18n.I18n;
import com.fhd.sys.interfaces.II18nBO;
import com.fhd.sys.web.form.i18n.I18nForm;

/**
 * 国际化业务
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:13:11
 *
 * @see 	 
 */
@Service
public class I18nBO implements II18nBO{

	@Autowired
	private I18nDAO o_i18nDAO;
	
	@Transactional
	public boolean saveI18n(String modifiedRecord) {
		JSONArray jsonArray=JSONArray.fromObject(modifiedRecord);
		if(jsonArray.size()==0){
			return false;
		}
		try {
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id").replaceAll(" ", "");
				String objectType = jsonObject.getString("objectType").replaceAll(" ", "");
				String objectKey = jsonObject.getString("objectKey").replaceAll(" ", "");
				String objectEn = jsonObject.getString("objectEn").replaceAll(" ", "");
				String objectCn = jsonObject.getString("objectCn").replaceAll(" ", "");
				I18n i18n = new I18n();
				
				if(id.equalsIgnoreCase(""))
					id = Identities.uuid();
				
				i18n.setId(id);
				i18n.setObjectType(objectType);
				i18n.setObjectKey(objectKey);
				i18n.setObjectEn(objectEn);
				i18n.setObjectCn(objectCn);
				this.mergeI18n(i18n);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Transactional
	public void deleteI18n(I18n i18n) {
		o_i18nDAO.delete(i18n);
	}
	
	@Transactional
	public boolean deleteI18n(String ids) {
		String idsStr[] = ids.split(",");
		I18n i18n = null;
		try {
			for (String id : idsStr) {
				i18n = this.getI18nByIdMJap().get(id);
				this.deleteI18n(i18n);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 得到全部i18n数据已MAP形式存储
	 * 
	 * @author 金鹏祥
	 * @return HashMap<String, I18n>
	*/
	private HashMap<String, I18n> getI18nByIdMJap(){
		List<I18n> list = this.findI18nAll();
		HashMap<String, I18n> i18nMap = new HashMap<String, I18n>();
		for (I18n i18n : list) {
			i18nMap.put(i18n.getId(), i18n);
		}
		
		return i18nMap;
	}
	
	@Transactional
	public void mergeI18n(I18n i18n) {
		o_i18nDAO.merge(i18n);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAll() {
		Criteria criteria = o_i18nDAO.createCriteria();
		ProjectionList prolist = Projections.projectionList();   
        prolist.add(Projections.groupProperty("objectType"));   
        criteria.setProjection(prolist);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<I18n> findI18nByObjectType(String objectType) {
		Criteria criteria = o_i18nDAO.createCriteria();
		criteria.add(Restrictions.eq("objectType", objectType));
		return criteria.list();
	}


	
	@SuppressWarnings("unchecked")
	public List<String> findI18nByLikeObjectType(String objectType) {
		Criteria criteria = o_i18nDAO.createCriteria();
		criteria.add(Restrictions.like("objectType", objectType, MatchMode.ANYWHERE));
		ProjectionList prolist = Projections.projectionList();   
        prolist.add(Projections.groupProperty("objectType"));   
        criteria.setProjection(prolist);
		return criteria.list();
	}
	
	public Page<I18n> findI18nBySome(String objectKey, Page<I18n> page, String sort, String dir, String objectType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(I18n.class);
		if(objectType != null){
			if(!"quanbu_1".equalsIgnoreCase(objectType)){
				detachedCriteria.add(Restrictions.eq("objectType", objectType));
			}
		}
		
		if(StringUtils.isNotBlank(objectKey)){
			if(objectKey != null){
				detachedCriteria.add(Restrictions.or(Restrictions.like("objectKey", objectKey, MatchMode.ANYWHERE)
						, Restrictions.like("objectCn", objectKey, MatchMode.ANYWHERE)));
			}
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			detachedCriteria.addOrder(Order.asc(sort));
		} else {
			detachedCriteria.addOrder(Order.desc(sort));
		}
		return o_i18nDAO.findPage(detachedCriteria, page, false);
	}
	
	public I18n findI18nById(String id) {
		Criteria criteria = o_i18nDAO.createCriteria();
		criteria.add(Restrictions.eq("id", id));
		return (I18n) criteria.list().get(0);
	}
	
	public Map<String, Object> findI18nAll(String query) {
		List<String> strList = null;
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		Map<String, Object> map = null;
		
		if(query == null){
			strList = this.findAll();
		}
		else{
			strList = this.findI18nByLikeObjectType(query);
			for (String objectType : strList) {
				map = new HashMap<String, Object>();
				map.put("id", objectType);
				map.put("text", objectType);
				map.put("num", objectType);
				map.put("name", objectType);
				map.put("leaf", true);
				map.put("linked", true);
				children.add(map);
			}
			item.put("children", children);
			return item;
		}
		
		
		map = new HashMap<String, Object>();
		map.put("id", "quanbu_1");
		map.put("text", "全部");
		map.put("num", "全部");
		map.put("name", "全部");
		map.put("leaf", true);
		map.put("linked", true);
		children.add(map);
		
//		for (I18n i18n : i18nList) {
//			map = new HashMap<String, Object>();
//			map.put("id", i18n.getObjectType());
//			map.put("text", i18n.getObjectType());
//			map.put("num", i18n.getObjectType());
//			map.put("name", i18n.getObjectType());
//			map.put("leaf", true);
//			map.put("linked", true);
//			children.add(map);
//		}
//		item.put("children", children);
		
		for (String objectType : strList) {
			map = new HashMap<String, Object>();
			map.put("id", objectType);
			map.put("text", objectType);
			map.put("num", objectType);
			map.put("name", objectType);
			map.put("leaf", true);
			map.put("linked", true);
			children.add(map);
		}
		item.put("children", children);
		return item;
	}
	
	public Map<String, Object> findI18nPage(int start, int limit, String query, String sort,String objectType) {
		String property = "";
		String direction = "";
		Page<I18n> page = new Page<I18n>();
		List<I18n> entityList = null;
		List<I18nForm> datas = new ArrayList<I18nForm>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		
		if (sort != null){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                direction = jsobj.getString("direction");
            }
        }else{
        	property = "objectCn";
        	direction = "ASC";
        }
		
		page = this.findI18nBySome(query, page, property, direction, objectType);
		
		entityList = page.getResult();
		
		for(I18n i18n : entityList){
			datas.add(new I18nForm(i18n));
		}
		
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<I18n> findI18nAll() {
		Criteria criteria = o_i18nDAO.createCriteria();
		return criteria.list();
	}
}

