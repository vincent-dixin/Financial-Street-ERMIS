/**
 * OldDictEntryBO.java
 * com.fhd.fdc.commons.business.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fhd.core.dao.Page;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.utils.PropertyFilter;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.orm.sql.SqlBuilder;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.dic.DictTypeDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictType;
import com.fhd.sys.web.form.dic.DictEntryForm;

/**
 * 数据字典条目BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-8-14 
 * @since    Ver 1.1
 * @Date	 2010-8-14		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */

@Service
@SuppressWarnings({ "unchecked", "deprecation" })
public class OldDictEntryBO {
	
	@Autowired
	private DictEntryDAO o_dictEntryDAO;

	@Autowired
	private BusinessLogBO o_businessLogBO; 
	
	@Autowired
	private DictTypeDAO o_dictTypeDAO;
	
	/**
	 * <pre>
	 * saveDictEntry:保存数据字典条目.
	 * </pre>
	 * 
	 * @author 吴德福
	 * @param dictEntryForm
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void saveDictEntry(DictEntryForm dictEntryForm){
		DictEntry dictEntry = new DictEntry();
		BeanUtils.copyProperties(dictEntryForm, dictEntry);
		dictEntry.setId(Identities.uuid());
		dictEntry.setDictType(queryDictTypeById(dictEntryForm.getDictTypeId()));
		dictEntry.setSort(queryMaxSortNo()+1);
		
		if(!"".equals(dictEntryForm.getParent().getId()) && !"-*".equals(dictEntryForm.getParent().getId()) && null != dictEntryForm.getParent().getId()){
			dictEntry.setLevel(queryDictEntryById(dictEntryForm.getParent().getId()).getLevel()+1);
			dictEntry.setIdSeq(queryDictEntryById(dictEntryForm.getParent().getId()).getIdSeq()+dictEntry.getId()+".");
		}else{
			dictEntry.setLevel(1);
			dictEntry.setIdSeq("."+dictEntry.getId()+".");
		}
		
		try {
			o_dictEntryDAO.merge(dictEntry);
			o_businessLogBO.saveBusinessLogInterface("新增", "数据字典条目", "成功",dictEntry.getDictType().getId(),dictEntry.getName(),dictEntry.getStatus(),dictEntry.getParent().getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "数据字典条目", "失败",dictEntry.getDictType().getId(),dictEntry.getName(),dictEntry.getStatus(),dictEntry.getParent().getId());
		}
	}
	
	/**
	 * removeDictEntry:删除数据字典条目.
	 * @author 吴德福
	 * @param id 数据字典条目id.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeDictEntry(String id){
		try {
			o_dictEntryDAO.delete(id);
			o_businessLogBO.delBusinessLogInterface("删除", "数据字典条目", "成功",id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "数据字典条目", "失败",id);
		}
	}
	
	/**
	 * updateDictEntry:修改数据字典条目.
	 * @author 吴德福
	 * @param dictEntry 数据字典条目.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public  void updateDictEntry(DictEntry dictEntry){
		try {
			o_dictEntryDAO.merge(dictEntry);
			o_businessLogBO.modBusinessLogInterface("修改", "数据字典条目", "成功",dictEntry.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "数据字典条目", "失败",dictEntry.getId());
		}
	}
	
	/**
	 * <pre>
	 * listMap:根据parentId,dictTypeId,isLeaf获取DictEntry实体
	 * </pre>
	 * 
	 * @author 金鹏祥
	 * @param parentId
	 * @param dictTypeId
	 * @param isLeaf
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String, Object>> listMap(String parentId,String dictTypeId,Boolean isLeaf){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<DictEntry> list = this.findBySome(parentId,dictTypeId,isLeaf);
		for (DictEntry dictEntry : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", dictEntry.getId());
			data.put("dbid", dictEntry.getId());
			data.put("name", dictEntry.getName());
			data.put("value", dictEntry.getValue());
			data.put("isLeaf", dictEntry.getIsLeaf());
			datas.add(data);
		}
		return datas;
	}
	
	/**
	 * 
	 * findBySome:根据sonme查询DictEntry集合实体
	 * 
	 * @author 杨鹏
	 * @param parentId：父Id（如果为空则查询根节点）
	 * @param dictTypeId：数据字典类型id
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> findBySome(String parentId,String dictTypeId,Boolean isLeaf){
		Criteria criteria = o_dictEntryDAO.createCriteria();
		if(StringUtils.isNotBlank(dictTypeId)){
			criteria.add(Restrictions.eq("dictType.id", dictTypeId));
		}
		if(StringUtils.isNotBlank(parentId)){
			criteria.add(Restrictions.eq("parent.id", parentId));
		}else{
			criteria.add(Restrictions.isNull("parent"));
		}
		if(null!=isLeaf){
			criteria.add(Restrictions.eq("isLeaf", isLeaf));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	
	/**
	 * queryDictEntryByParentId:根据parentId查询有该数据字典条目存在.
	 * @author 吴德福
	 * @param parentId 数据字典条目父ID.
	 * @return List<DictType> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> queryDictEntryByParentId(String parentId){
		return o_dictEntryDAO.find(Restrictions.eq("parentId", parentId));
	}
	
	/**
	 * queryDictEntryById:根据id查询数据字典条目.
	 * @author 吴德福
	 * @param id 数据字典条目id.
	 * @return DictEntry 数据字典条目.
	 * @since  fhd　Ver 1.1
	 */
	public DictEntry queryDictEntryById(String id){
		return o_dictEntryDAO.get(id);
	}
	
	/**
	 * queryAllDictEntry:查询所有的数据字典条目.
	 * @author 吴德福
	 * @return List<DictEntry> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> queryAllDictEntry(){
		return o_dictEntryDAO.createCriteria().setCacheable(true).list();
	}
	
	/**
	 * queryAllDictEntryBySqlServer:查询所有的数据字典条目.SelectTag控件中┠展示.
	 * @author 吴德福
	 * @return List<DictEntry> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<Object[]> queryAllDictEntryBySqlServer(){
		Map<String,String> model = new HashMap<String,String>();
		String sql = SqlBuilder.getSql("select_dictEntryName", model);
		List<Object[]> list = o_dictEntryDAO.createQuery(sql).setCacheable(true).list();
		return list;
	}
	
	/**
	 * queryMaxSortNo:查询当前最大的排列序号，新增时使用.
	 * @author 吴德福
	 * @return Integer 当前最大的排列序号.
	 * @since  fhd　Ver 1.1
	 */
	public Integer queryMaxSortNo(){
		// 排序序号
		int i = 0;
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setProjection(Projections.max("sort"));
		criteria.setCacheable(true);
		List<Integer> sortList = criteria.list();
		for(Integer tempI : sortList){
			if(null != tempI){
				i = tempI;
			}
		}
		return i;
	}
	
	/**
	 * queryDictEntryByLocale:根据name和locale查询所有的数据字典条目.
	 * @author 吴德福
	 * @param name 数据字典类型名称.
	 * @param locale 本地浏览器语言.
	 * @return List<DictEntry> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> queryDictEntryByLocale(String name,String locale){
		List<DictEntry> resultList = new ArrayList<DictEntry>();
		String dictTypeId = "";
		List<DictType> dictTypeList = queryAllDictType();
		for(DictType dictType : dictTypeList){
			if(name.equals(dictType.getName())){
				dictTypeId = dictType.getId();
			}
		}
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.addOrder(Order.asc("sort"));
		criteria.setCacheable(true);
		List<DictEntry> dictEntryList = criteria.list();
		for(DictEntry dictEntry : dictEntryList){
			if(null!=dictEntry.getDictType()){
				try{
					if(dictTypeId.equals(dictEntry.getDictType().getId())){
						resultList.add(dictEntry);
					}
				}catch(Exception ex){
					
				}
			}
		}
		return resultList;
	}
	
	/**
	 * queryEntryByDictTypeId:根据数据字典类型查询数据字典条目.
	 * @author 吴德福
	 * @param dictTypeId 数据字典类型ID.
	 * @return List<DictEntry> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> queryEntryByDictTypeId(String dictTypeId){
		Criteria criteria = o_dictEntryDAO.createCriteria(Restrictions.eq("dictType.id", dictTypeId));
		criteria.setCacheable(true);
		criteria.addOrder(Order.asc("sort"));
		
		return criteria.list();
	}
	
	/**
	 * <pre>
	 * queryEntryByDictTypeId:根据参数查询实体
	 * </pre>
	 * 
	 * @author 金鹏祥
	 * @param dictTypeId
	 * @param filter
	 * @param filterOp
	 * @param filterStr
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	*/
	public List<DictEntry> queryEntryByDictTypeId(String dictTypeId,Boolean filter,String filterOp,String filterStr) {
		Criteria criteria = o_dictEntryDAO.createCriteria(Restrictions.eq("dictType.id", dictTypeId));
		criteria.setCacheable(true);
		if(filter){
			String[] strings = StringUtils.split(filterStr,",");
			if("in".equals(filterOp)){
				criteria.add(Restrictions.or(Restrictions.in("dictEntryName", strings), Restrictions.in("id", strings)));
			}else if ("!in".equals(filterOp)) {
				criteria.add(Restrictions.and(Restrictions.not(Restrictions.in("dictEntryName", strings)), 
						Restrictions.not(Restrictions.in("id", strings))));
			}
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	
	/**
	 * query:根据查询条件查询数据字典条目.
	 * @author 吴德福
	 * @param filters 页面查询条件传递的参数集合.
	 * @return List<DictEntry> 数据字典条目集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> query(DictEntryForm dictEntryForm) {
		StringBuilder hql = new StringBuilder();
		hql.append("From DictEntry Where 1=1 ");
		String dictTypeId = "";
		String dictEntryName = "";
		if(dictEntryForm != null && !"".equals(dictEntryForm.getDictTypeId()) && dictEntryForm.getDictTypeId() != null){
			dictTypeId = dictEntryForm.getDictTypeId();
			hql.append(" and dictType.id like '%"+dictTypeId+"%'");
		}
		if(dictEntryForm != null && !"".equals(dictEntryForm.getName()) && dictEntryForm.getName() != null){
			dictEntryName = dictEntryForm.getName();
			hql.append(" and dictEntryName like '%"+dictEntryName+"%'");
		}
		
		return o_dictEntryDAO.createQuery(hql.toString()).setCacheable(true).list();
	}
	
	/**
	 * <pre>
	 * getDictEntrysByDictTypeTitle:根据数据字典类型TITLE获取所有数据字典实体
	 * </pre>
	 * 
	 * @author David
	 * @param dictTypeTitle 数据字典类型
	 * @param local 本地方言
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> getDictEntrysByDictTypeTitle(String dictTypeTitle,String local){
		StringBuilder hql = new StringBuilder("from DictEntry de where de.dictType.dictTypeTitle='")
		.append(dictTypeTitle)
		.append("' and de.dictType.myLocale='")
		.append(StringUtils.lowerCase(StringUtils.split(local, ",")[0]))
		.append("' and de.status='1' order by de.sort asc");
		
		return o_dictEntryDAO.createQuery(hql.toString()).setCacheable(true).list();
	}
	
	/**
	 * <pre>
	 * initDictEntry:初始化数据字典
	 * </pre>
	 * 
	 * @author David
	 * @param title 数据字典TITLE属性
	 * @param local 方言
	 * @return Map<String,DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public Map<String,DictEntry> initDictEntry(String title,String local){
		Map<String,DictEntry> dictTypeMap = new HashMap<String,DictEntry>();
		List<DictEntry> des = getDictEntrysByDictTypeTitle(title, local);
		for(DictEntry de:des){
			dictTypeMap.put(de.getName(), de);
		}
		
		return dictTypeMap;
	}
	
	/**
	 * <pre>
	 * getDictEntryByDictName:根据字典名称查找实体
	 * </pre>
	 * 
	 * @author David
	 * @param dictName
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> getDictEntryByDictName(String dictName){
		return o_dictEntryDAO.createQuery("from DictEntry de where de.dictEntryName=?",dictName).setCacheable(true).list();
	}
	
	/**
	 * <pre>
	 * loadDictTree:获取指定数据字典类型的数据字典实体条目(根元素)
	 * </pre>
	 * 
	 * @author David
	 * @param dictTypeTitle
	 * @param local
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadDictTree(String dictTypeTitle,String local) {
		List<Map<String, Object>> olist = new ArrayList<Map<String, Object>>();
		
		List<DictEntry> dicts = new ArrayList<DictEntry>();
		String[] idAndTitle = dictTypeTitle.split(",");
		
		if("root".equals(idAndTitle[0]))
			dicts = getRootDictEntrysByDictTypeTitle(idAndTitle[1], local);
		else
			dicts = getChildDictEntrysByDictTypeTitle(dictTypeTitle);
		
		for (DictEntry dict : dicts) {
			Map<String, Object> map = new HashMap<String, Object>();
			boolean isLeaf = true;
			if(getChildDictEntrysByDictTypeTitle(dict.getId()).size()>0)
				isLeaf = false;
			map.put("id", dict.getId());
			map.put("leaf", isLeaf);
			map.put("text", dict.getName());
			map.put("iconCls", "icon-group");
			map.put("cls", "dict");
			map.put("draggable", false);
			olist.add(map);
		}
		return olist;
	}
	
	/**
	 * <pre>
	 * getRootDictEntrysByDictTypeTitle:根据数据字典类型TITLE获取所有数据字典实体(根)
	 * </pre>
	 * 
	 * @author David
	 * @param dictTypeTitle 数据字典类型
	 * @param local 本地方言
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> getRootDictEntrysByDictTypeTitle(String dictTypeTitle,String local){
		StringBuilder hql = new StringBuilder("from DictEntry de where de.dictType.dictTypeTitle='")
		.append(dictTypeTitle)
		.append("' and de.parentId is null and de.dictType.myLocale='")
		.append(StringUtils.lowerCase(StringUtils.split(local, ",")[0]))
		.append("' and de.status='1' order by de.sort asc");
		
		return o_dictEntryDAO.createQuery(hql.toString()).setCacheable(true).list();
	}
	
	/**
	 * <pre>
	 * getChildDictEntrysByDictTypeTitle:根据数据字典条目ID获取子条目
	 * </pre>
	 * 
	 * @author David
	 * @param dictTypeTitle 数据字典类型
	 * @param local 本地方言
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	public List<DictEntry> getChildDictEntrysByDictTypeTitle(String dictEntryId){
		StringBuilder hql = new StringBuilder("from DictEntry de where de.parentId='")
		.append(dictEntryId)
		.append("' and de.status='1' order by de.sort asc");
		
		return o_dictEntryDAO.createQuery(hql.toString()).setCacheable(true).list();
	}
	
	/**
	 * queryDictEntryByPage:查询所有数据字典
	 * @author 万业
	 * @param dictTypeId
	 * @param dictEntryName
	 * @param page
	 * @return Page<DictEntry>
	 */
	public Page<DictEntry> queryDictEntryByPage(String dictTypeId,String dictEntryName,Page<DictEntry> page,String sort,String dir,String[] dictTypeIds){
	    DetachedCriteria dc = DetachedCriteria.forClass(DictEntry.class);
		if(StringUtils.isNotBlank(dictTypeId)){
			dc.add(Property.forName("dictType.id").eq(dictTypeId));
		}
		if(null!=dictTypeIds&&dictTypeIds.length>0){
			dc.add(Restrictions.in("dictType.id", dictTypeIds));
		}
		if(StringUtils.isNotBlank(dictEntryName)){
			dc.add(Property.forName("dictEntryName").like(dictEntryName, MatchMode.ANYWHERE));
		}
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"parentname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.asc(sort));
			}else if("id".equals(sort)){
				dc.addOrder(Order.asc("dictType"));
			}else{
				dc.addOrder(Order.asc("parentId"));
			}
		} else if("DESC".equalsIgnoreCase(dir)) {
			if(!"parentname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.desc(sort));
			}else if("id".equals(sort)){
				dc.addOrder(Order.asc("dictType"));
			}else{
				dc.addOrder(Order.asc("parentId"));
			}
		}
		return o_dictEntryDAO.findPage(dc,page, Boolean.FALSE);
		
	}
	
	/**
	 * getDictEntrybyId:根据id查询数据字典条目.
	 * @author 吴德福
	 * @param dictEntryId
	 * @return DictEntry
	 * @since  fhd　Ver 1.1
	 */
	public DictEntry getDictEntrybyId(String dictEntryId){
		return o_dictEntryDAO.get(dictEntryId);
	}
	
	/**
	 * getDictEntryByValueAndDicTypeId:根据数据字典条目的值，与数据字典类型ID号，查询数据字典（用数据字典ID号，而不用数据字典名是因为数据字典类型名是可以改变的）
	 * 
	 * @author 陈燕杰
	 * @param value:数据字典条目的值；
	 * @param dicTypeId：数据字典类型的ID号;
	 * @return DictEntry
	 * @since  fhd　Ver 1.1
	 */
	public DictEntry getDictEntryByValueAndDicTypeId(String value,String dicTypeId){
		DictEntry result=null;
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.add(Restrictions.ilike("value", value));
		criteria.add(Restrictions.eq("dictType.id", dicTypeId));
		List<DictEntry> dictEntrys=criteria.setCacheable(true).list();
		if(dictEntrys.size()>0)
		{
			result=dictEntrys.get(0);
		}
		return result;
	}
	
	/**
	 * queryDictTypeById:根据id查询数据字典类型.
	 * @author 吴德福
	 * @param id 数据字典类型ID.
	 * @return DictType 数据字典类型.
	 * @since  fhd　Ver 1.1
	 */
	public DictType queryDictTypeById(String id){
		return o_dictTypeDAO.get(id);
	}
	
	/**
	 * 根据标题查询数据字典类型.
	 * @author 吴德福
	 * @param title
	 * @return DictType
	 * @since  fhd　Ver 1.1
	 */
	public DictType queryDictTypeByTitle(String title){
		DictType dictType = new DictType();
		Criteria dc = o_dictTypeDAO.createCriteria();
		dc.add(Restrictions.eq("dictTypeTitle", title));
		List<DictType> dictTypeList = dc.list();
		if(null != dictTypeList && dictTypeList.size()>0){
			dictType = dictTypeList.get(0);
		}
		return dictType;
	}
	
	/**
	 * queryAllDictType:查询所有数据字典类型.
	 * @author 吴德福
	 * @return List<DictType> 数据字典类型集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictType> queryAllDictType(){
		return o_dictTypeDAO.getAll();
	}
	
	/**
	 * queryDictTypeByLocale:根据传入的浏览器语言，查询对应的所有对象，实现国际化.
	 * @author 吴德福
	 * @param locale 本地浏览器语言.
	 * @return List<DictType> 数据字典类型集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictType> queryDictTypeByLocale(String locale){
		return o_dictTypeDAO.find("From DictType where myLocale = ?", locale);
	}
	
	/**
	 * queryAllDictTypeByLocale:根据传入的浏览器语言，查询对应的所有对象，实现国际化.SelectTag控件中┠展示.
	 * @author 吴德福
	 * @param locale 本地浏览器语言.
	 * @return List<DictType> 数据字典类型集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictType> queryAllDictTypeByLocale(String locale){
		Map<String,String> model = new HashMap<String,String>();
		String sql = SqlBuilder.getSql("select_dictTypeName", model);
		return o_dictTypeDAO.find(sql);
	}
	
	/**
	 * queryEntryByParentId:根据parentId查询有该数据字典类型存在.
	 * @author 吴德福
	 * @param parentId 数据字典类型父ID.
	 * @return List<DictType> 数据字典类型集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictType> queryEntryByParentId(String parentId){
		return o_dictTypeDAO.find("From DictType where parentId = ?", parentId);
	}
	
	/**
	 * query:根据查询条件数据字典类型.
	 * @author 吴德福
	 * @param filters 页面查询条件传递的参数集合.
	 * @return List<DictType> 数据字典类型集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<DictType> query(List<PropertyFilter> filters) {
	    
	    Criteria dc = o_dictTypeDAO.createCriteria();
		
	     for(PropertyFilter f : filters)
	     {
		 dc.add(Restrictions.eq(f.getPropertyName(), f.getPropertyType()));
	     }
		return dc.list();
	}
	
	/**
	 * queryDictTypesByFields:根据id,dictTypeName,seqNo判断数据字典类型是否存在.
	 * @author 吴德福
	 * @param dictType
	 * @return Boolean
	 * @since  fhd　Ver 1.1
	 */
	public Boolean queryDictTypesByFields(DictType dictType){
		StringBuilder hql = new StringBuilder();
		hql.append("From DictType where 1=1 ");
		if(null != dictType.getId().trim() || !"".equals(dictType.getId().trim())){
			hql.append(" and id='"+dictType.getId().trim()+"'");
		}
		if(null != dictType.getName().trim() || !"".equals(dictType.getName().trim())){
			hql.append(" or name='"+dictType.getName().trim()+"'");
		}
		if(null != dictType.getIdSeq().trim() || !"".equals(dictType.getIdSeq().trim())){
			hql.append(" or idSeq='"+dictType.getIdSeq().trim()+"'");
		}
		List<DictType> list = new ArrayList<DictType>();
		list = o_dictTypeDAO.find(hql.toString());
		if(null != list && list.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * queryDictTypeByPage:查询数据字典类型
	 * @author 万业
	 * @param dictTypeTitle
	 * @param dictEntryName
	 * @param myLocale
	 * @param page
	 * @return Page<DictType>
	 */
	public Page<DictType> queryDictTypeByPage(String dictTypeTitle,String dictEntryName,String myLocale,Page<DictType> page,String sort,String dir){
	    DetachedCriteria dc = DetachedCriteria.forClass(DictType.class);
		if(StringUtils.isNotBlank(dictTypeTitle)){
			dc.add(Property.forName("dictTypeTitle").like(dictTypeTitle, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(dictEntryName)){
			dc.add(Property.forName("dictTypeName").like(dictEntryName, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(myLocale)){
			dc.add(Property.forName("myLocale").eq(myLocale));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_dictTypeDAO.findPage(dc, page, false);
	}
}

