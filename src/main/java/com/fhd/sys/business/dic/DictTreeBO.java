package com.fhd.sys.business.dic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.dic.DictTypeDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictType;

/**
 * 数据字典树加载业务类
 * @author 张 雷
 * @version
 * @since Ver 1.1
 * @Date 2012-9-18 下午3:42:47
 * 
 * @see
 */
@Service
public class DictTreeBO {

    @Autowired
    private DictTypeDAO o_dictTypeDAO;
    
    @Autowired
    private DictEntryDAO o_dictEntryDAO;
    
    @Autowired
    private DictBO o_dictBO;

    /**
     * 
     * <pre>
     * findDictTypesBySearchName:查询字典类型
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 所属字典类型id
     * @return List<DictType>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	protected List<DictType> findDictTypesBySearchName(String searchName, String typeId) {
		Criteria criteria = o_dictTypeDAO.createCriteria();
		criteria.setCacheable(true);
		Set<String> idSet = findDictTypesIdsBySearchName(searchName, typeId);
		if (idSet.size() > 0) {
		    criteria.add(Restrictions.in("id", idSet));
		} else {
		    criteria.add(Restrictions.isNull("id"));
		}
		
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
    }

    /**
     * 
     * <pre>
     * findDictEntryBySearchName:查询字典项
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 所属字典类型ID
     * @param entryId 排除的项目ID
     * @return List<DictEntry>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	protected List<DictEntry> findDictEntryBySearchName(String searchName, String typeId,String entryId, String sort, String dir) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		Set<String> idSet = findDictEntryIdsBySearchName(searchName, typeId);
		if (idSet.size() > 0) {
		    criteria.add(Restrictions.in("id", idSet));
		} else {
		    criteria.add(Restrictions.isNull("id"));
		}
		if(StringUtils.isNotBlank(entryId))
		{
		criteria.add(Restrictions.not( Restrictions.eq("id", entryId)));
		}
		
		if(StringUtils.isNotBlank(dir)){
			if("ASC".equalsIgnoreCase(dir)) {
				criteria.addOrder(Order.asc(sort));
			} else {
				criteria.addOrder(Order.desc(sort));
			}
		}
		
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("isSystem"));
	
		return criteria.list();
    }
    
    /**
     * 
     * <pre>
     * findDictEntryBySearchName:查询字典项
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 所属字典类型ID
     * @return List<DictEntry>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	protected List<DictEntry> findDictEntryBySearchName(String searchName, String typeId) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		Set<String> idSet = findDictEntryIdsBySearchName(searchName, typeId);
		if (idSet.size() > 0) {
		    criteria.add(Restrictions.in("id", idSet));
		} else {
		    criteria.add(Restrictions.isNull("id"));
		}
		
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("isSystem"));
	
		return criteria.list();
    }

    /**
     * 
     * <pre>
     * findDictTypesIdsBySearchName:查询字典类型，返回符合条件的ID数组
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @return Set<String>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	protected Set<String> findDictTypesIdsBySearchName(String searchName,String typeId) {
		List<DictType> list = new ArrayList<DictType>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_dictTypeDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(searchName)) {
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(typeId)) {
			criteria.add(Restrictions.eq("id", typeId));
		}
		list = criteria.list();
		for (DictType dictType : list) {
		    String[] idsTemp = dictType.getIdSeq().split("\\.");
		    idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
    }

    /**
     * 
     * <pre>
     * findDictEntryIdsBySearchName:查询字典项，返回符合条件的ID数组
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @return Set<String>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	protected Set<String> findDictEntryIdsBySearchName(String searchName, String typeId) {
		List<DictEntry> list = new ArrayList<DictEntry>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(searchName)) {
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(typeId)) {
			criteria.add(Restrictions.eq("dictType.id", typeId));
		}
		criteria.add(Restrictions.eq("status", "1"));
		list = criteria.list();
		for (DictEntry dictType : list) {
		    String[] idsTemp = dictType.getIdSeq().split("\\.");
		    idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
    }

    /**
     * 
     * <pre>
     * findDictTypeTree:递归查询字段类型树
     * </pre>
     * 
     * @author 王 钊
     * @param dictTypeSet 字典类型数组
     * @param parent 上级字典类型对象
     * @param searchName 查询关键字
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    private Map<String, Object> findDictTypeTree(List<DictType> dictTypeSet, DictType parent, String searchName, String typeId) {
		Map<String, Object> item = new HashMap<String, Object>();
		List<DictType> subList = new ArrayList<DictType>();
		Set<String> idSet = this.findDictTypesIdsBySearchName(searchName,
			typeId);
		if (null != dictTypeSet && dictTypeSet.size() > 0) {
		    for (DictType dictType : dictTypeSet) {
			if (null == parent) {
			    if (null == dictType.getParent()) {
					if (idSet.contains(dictType.getId())) {
					    subList.add(dictType);
					}
			    }
			} else {
			    if (null != dictType.getParent() && dictType.getParent().getId().equals(parent.getId())) {
					if (idSet.contains(dictType.getId())) {
					    subList.add(dictType);
					}
			    }
			}
		    }
		}
		if (null != parent) {
		    item.put("id", parent.getId());
		    item.put("text", parent.getName());
		    if (subList.size() > 0) {
				item.put("expanded", true);
				item.put("leaf", false);
		    } else {
		    	item.put("leaf", true);
		    }
		}
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (DictType dictType : subList) {
		    Map<String, Object> subItem = new HashMap<String, Object>();
		    subItem.put("id", dictType.getId());
		    subItem.put("text", dictType.getName());
		    if (null != dictType.getChildren() && dictType.getChildren().size() > 0) {
				subItem.put("leaf", false);
				Map<String, Object> map = findDictTypeTree(new ArrayList<DictType>(dictType.getChildren()),
					dictType, searchName, typeId);
				if (map != null) {
				    children.add(map);
				}
		    } else {
				subItem.put("leaf", true);
				children.add(subItem);
		    }
	
		}
		if (children.size() > 0)
		    item.put("children", children);
		return item;
    }

    /**
     * 
     * <pre>
     * findDictEntryTree:递归构建字典项树
     * </pre>
     * 
     * @author 王 钊
     * @param dictEntrySet 字典项数组
     * @param parent 上级字典项对象
     * @param searchName 查询关键字
     * @param typeId
     * @return
     * @since fhd　Ver 1.1
     */
    private Map<String, Object> findDictEntryTree(List<DictEntry> dictEntrySet, DictEntry parent, String searchName, String typeId) {
		Map<String, Object> item = new HashMap<String, Object>();
		List<DictEntry> subList = new ArrayList<DictEntry>();
		Set<String> idSet = this.findDictEntryIdsBySearchName(searchName,
			typeId);
		if (null != dictEntrySet && dictEntrySet.size() > 0) {
		    for (DictEntry dictEntry : dictEntrySet) {
			if (null == parent) {
			    if (null == dictEntry.getParent()) {
				if (idSet.contains(dictEntry.getId())) {
				    subList.add(dictEntry);
				}
			    }
			} else {
			    if (null != dictEntry.getParent()
				    && dictEntry.getParent().getId()
					    .equals(parent.getId())) {
				if (idSet.contains(dictEntry.getId())) {
				    subList.add(dictEntry);
				}
			    }
			}
		    }
		}
		if (null != parent) {
	
		    item.put("id", parent.getId());
		    item.put("name", parent.getName());
		    item.put("text", parent.getName());
		    item.put("num", parent.getId());
		    item.put("value", parent.getValue());
		    item.put("sys", parent.getIsSystem());
		    if (subList.size() > 0) {
			item.put("expanded", true);
			item.put("linked", true);
			item.put("leaf", false);
		    } else {
			item.put("leaf", true);
		    }
		    if (o_dictBO.findDictEntryI18ByDictEntryId(parent.getId()).size() > 0) {
			item.put("linked", true);
		    }
		}
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (DictEntry dictEntry : subList) {
		    Map<String, Object> subItem = new HashMap<String, Object>();
		    subItem.put("id", dictEntry.getId());
		    subItem.put("name", dictEntry.getName());
		    subItem.put("text", dictEntry.getName());
		    subItem.put("num", dictEntry.getId());
		    subItem.put("value", dictEntry.getValue());
		    subItem.put("sys", dictEntry.getIsSystem());
		    if (o_dictBO.findDictEntryI18ByDictEntryId(dictEntry.getId())
			    .size() > 0) {
			subItem.put("linked", true);
		    }
		    if (null != dictEntry.getChildren()
			    && dictEntry.getChildren().size() > 0) {
			subItem.put("leaf", false);
			item.put("linked", true);
			Map<String, Object> map = findDictEntryTree(
				new ArrayList<DictEntry>(dictEntry.getChildren()),
				dictEntry, searchName, typeId);
			if (map != null) {
			    children.add(map);
			}
		    } else {
			subItem.put("leaf", true);
			children.add(subItem);
		    }
	
		}
		if (children.size() > 0)
		    item.put("children", children);
		return item;
    }

    /**
     * 
     * <pre>
     * findDictTypesTree:查询字典类型
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 项目类型ID
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    public Map<String, Object> findDictTypesTree(String searchName, String typeId) {
		List<DictType> list = findDictTypesBySearchName(searchName, typeId);
		return findDictTypeTree(list, null, searchName, typeId);
    }

    /**
     * 
     * <pre>
     * findDictEntryTree:查询字典项
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 所属字典类型id
     * @param entryId 排除的字典项ID
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    public Map<String, Object> findDictEntryTree(String searchName, String typeId,String entryId, String sort, String dir) {
    	return findDictEntryTree(new ArrayList<DictEntry>(
    			findDictEntryBySearchName(searchName, typeId,entryId, sort, dir)), null,searchName, typeId);
    }
    
    /**
     * 
     * <pre>
     * findDictEntryTree:查询字典项
     * </pre>
     * 
     * @author 王 钊
     * @param searchName 查询关键字
     * @param typeId 所属字典类型id
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    public Map<String, Object> findDictEntryTree(String searchName, String typeId) {
    	return findDictEntryTree(new ArrayList<DictEntry>(
    			findDictEntryBySearchName(searchName, typeId)), null,searchName, typeId);
    }

}
