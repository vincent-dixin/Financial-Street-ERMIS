package com.fhd.sys.business.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.dic.DictEntryI18DAO;
import com.fhd.sys.dao.dic.DictTypeDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryI18;
import com.fhd.sys.entity.dic.DictType;
import com.fhd.sys.interfaces.IDictBO;

/**
 * 数据字典业务类
 * @author 张 雷
 * @version
 * @since Ver 1.1
 * @Date 2012-9-18 下午3:42:34
 * 
 * @see
 */
@Service
public class DictBO implements IDictBO{
    @Autowired
    private DictEntryDAO o_dictEntryDAO;

    @Autowired
    private DictTypeDAO o_dictTypeDAO;

    @Autowired
    private DictEntryI18DAO o_dictEntryI18DAO;
    
    /**
     * 
     * saveDictEntry:保存DictEntry
     * 
     * @author 王钊
     * @param dictEntry：数据字典类型id
     * @return
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveDictEntry(DictEntry dictEntry) {
		dictEntry.setStatus("1");//可用
		dictEntry.setIsSystem("0");//不是系统字典
		o_dictEntryDAO.merge(dictEntry);
		mergIdSeqAndLevelForDictEntry(dictEntry, null, true);
    }
    
    /**
     * 
     * removeDictEntry:逻辑删除DictEntry
     * 
     * @author 王钊
     * @param ids ：id字符串，用,号分割
     * @return
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void removeDictEntry(String ids) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
		    DictEntry dictEntry = findDictEntryById(id);
		    removeDictEntry(dictEntry, dictEntry.getChildren());
		}
    }
    
    /**
     * 
     * <pre>
     * removeDictEntry:递归删除字典项节点及子节点
     * </pre>
     * 
     * @author 王 钊
     * @param dictEntry 字典项对象
     * @param list 字典项子对象数组
     * @since fhd　Ver 1.1
     */
    public void removeDictEntry(DictEntry dictEntry, Set<DictEntry> dictEntrySet) {
		if (dictEntrySet != null) {
		    for (DictEntry tempDictEntry: dictEntrySet) {
				if (tempDictEntry.getChildren() != null
					&& tempDictEntry.getChildren().size() > 0) {
				    removeDictEntry(tempDictEntry, tempDictEntry.getChildren());
				} else {
				    removeDictEntry(tempDictEntry, null);
				}
		    }
		}
		if (dictEntry != null) {
		    List<DictEntryI18> dictEntryI18list = findDictEntryI18ByDictEntryId(dictEntry
			    .getId());
		    for (DictEntryI18 dictEntryI18 : dictEntryI18list) {
			o_dictEntryI18DAO.delete(dictEntryI18);
		    }
		    dictEntry.setStatus("0");
		    o_dictEntryDAO.merge(dictEntry);
		}
    }

    /**
     * 
     * <pre>
     * removeDictEntryI18:物理删除国际化
     * </pre>
     * 
     * @author 王 钊
     * @param ids 国际化记录id，多个id用"," 分开
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void removeDictEntryI18(String ids) {
		if (StringUtils.isEmpty(ids)) {
		    return;
		}
		String[] idArray = ids.split(",");
		for (String id : idArray) {
		    DictEntryI18 dictEntryI18 = o_dictEntryI18DAO.get(id);
		    o_dictEntryI18DAO.delete(dictEntryI18);
		}
    }
    
    /**
     * 
     * mergeDictEntry:更新DictEntry
     * 
     * @author 杨鹏
     * @param dictEntry 字典项对象
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void mergeDictEntry(DictEntry dictEntry) {
    	o_dictEntryDAO.merge(dictEntry);
    	mergIdSeqAndLevelForDictEntry(dictEntry, null, true);
    }
    
    /**
     * 
     * <pre>
     * mergeDictEntryI18Batch:批量更新国际化记录
     * </pre>
     * 
     * @author 王 钊
     * @param jsonStrin 一条或多条国际化记录json数组字符串
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void mergeDictEntryI18Batch(String jsonString, String dictEntryI18Id) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		if (jsonArray.size() == 0) {
		    return;
		}
		for (int i = 0; i < jsonArray.size(); i++) {
		    JSONObject jsonObject = jsonArray.getJSONObject(i);
		    String id = null;
		    if (jsonObject.has("id")) {
			id = jsonObject.getString("id");
		    }
		    String name = jsonObject.getString("name");
		    String myLocal = jsonObject.getString("myLocale");
	
		    DictEntryI18 dictEntryI18 = null;
		    if (StringUtils.isNotBlank(id)) {
		    	dictEntryI18 = o_dictEntryI18DAO.get(id);
		    } else {
		    	dictEntryI18 = new DictEntryI18(Identities.uuid());
		    	dictEntryI18.setDictEntry(o_dictEntryDAO.get(dictEntryI18Id));
		    }
		    dictEntryI18.setName(name);
		    dictEntryI18.setMyLocale(myLocal);
		    o_dictEntryI18DAO.merge(dictEntryI18);
		}
    }
    
    /**
     * 
     * <pre>
     * 查询所有字典类型为st_temp_category(MAP)
     * </pre>
     * 
     * @author 王 钊
     * @param ids 国际化记录id，多个id用"," 分开
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	@Transactional
    public HashMap<String, DictEntry> findDictEntryByDictTypeIdMap() {
    	HashMap<String, DictEntry> map = new HashMap<String, DictEntry>();
    	
    	Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.add(Restrictions.eq("dictType.id", "st_temp_category"));
		List<DictEntry> list = criteria.list();
		for (DictEntry dictEntry : list) {
			map.put(dictEntry.getId(), dictEntry);
		}
		
		return map;
    }
    
    /**
     * 
     * <pre>
     * findDictTypeByTypeId:根据typeId取DictType对象
     * </pre>
     * 
     * @author 王 钊
     * @param dictTypeId 字典类型id
     * @return DictType
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public DictType findDictTypeByTypeId(String dictTypeId) {
		Criteria criteria = o_dictTypeDAO.createCriteria();
		criteria.setCacheable(true);
		criteria.add(Restrictions.eq("id", dictTypeId));
		List<DictType> list = criteria.list();
		if (list.size() == 1) {
		    return list.get(0);
		} else {
		    return null;
		}
    }

    /**
     * 
     * 取DictEntry
     * 
     * @author 王钊
     * @param dictEntryId： 字典项ID
     * @return DictEntry
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public DictEntry findDictEntryById(String dictEntryId) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(dictEntryId)) {
			criteria.add(Restrictions.eq("id", dictEntryId));
		} else {
		    return null;
		}
	
		List<DictEntry> list = criteria.list();
		if (list.size() > 0) {
		    return list.get(0);
		} else {
		    return null;
		}
    }
    
    /**
     * 
     * 根据id取DictEntry实体对象集合
     * 
     * @author 金鹏祥
     * @param id： 字典项ID
     * @return List<DictEntry>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public List<DictEntry> findDictEntryByDictTypeIdAndEStatus(String dictTypeId) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(dictTypeId)) {
			criteria.add(Restrictions.eq("dictType.id", dictTypeId));
		} else {
		    return null;
		}
		criteria.add(Restrictions.eq("status", "1"));
		List<DictEntry> list = criteria.list();
		if (list.size() > 0) {
		    return list;
		} else {
		    return null;
		}
    }
    
    /**
     * 
     * 根据id取DictEntry实体对象集合
     * 
     * @author 金鹏祥
     * @param id： 字典项ID
     * @return List<DictEntry>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public List<DictEntry> findDictEntryByDictTypeId(String dictTypeId) {
		Criteria criteria = o_dictEntryDAO.createCriteria();
		criteria.setCacheable(true);
		if (StringUtils.isNotBlank(dictTypeId)) {
			criteria.add(Restrictions.eq("dictType.id", dictTypeId));
		} else {
		    return null;
		}
	
		List<DictEntry> list = criteria.list();
		if (list.size() > 0) {
		    return list;
		} else {
		    return null;
		}
    }

    /**
     * 
     *  递归设置IdSeq和Level字段
     * 
     * @author 王钊
     * @param sublist： 子节点列表
     * @param entity ： 当前节点对象
     * @return DictEntry
     * @since fhd　Ver 1.1
     */
    public DictEntry mergIdSeqAndLevelForDictEntry(DictEntry dictEntry, List<DictEntry> sublist) {
		if (null != dictEntry.getParent()
			&& !("null").equals(dictEntry.getParent().getId())) {
		    DictEntry parentEntity = o_dictEntryDAO.getDictEntryByValue(dictEntry
			    .getParent().getId());
		    dictEntry.setLevel(parentEntity.getLevel() + 1);
		    dictEntry.setIdSeq(parentEntity.getIdSeq() + dictEntry.getId() + ".");
		} else {
			dictEntry.setLevel(1);
			dictEntry.setIdSeq("." + dictEntry.getId() + ".");
			dictEntry.setParent(null);
		}
		if (dictEntry.getChildren() != null && dictEntry.getChildren().size() > 0) {
	
		}
		return dictEntry;
    }
    
   

    /**
     * 
     * <pre>
     * 递归处理Level和IdSqe字段
     * </pre>
     * 
     * @author 王 钊
     * @param dictEntry 字典项对象
     * @param subList 子对象数组
     * @param find 是否继续递归查找下一集
     * @since fhd　Ver 1.1
     */
    public void mergIdSeqAndLevelForDictEntry(DictEntry dictEntry,List<DictEntry> subList, boolean find) {
		if (dictEntry != null) {
		    if (dictEntry.getParent() != null) {
			DictEntry inDictEntry = findDictEntryById(dictEntry.getParent()
				.getId());
			dictEntry.setLevel(inDictEntry.getLevel() + 1);
			dictEntry.setIdSeq(inDictEntry.getIdSeq() + dictEntry.getId()
				+ ".");
			mergIdSeqAndLevelForDictEntry(inDictEntry, null, false);
		    } else {
			dictEntry.setLevel(1);
			dictEntry.setIdSeq("." + dictEntry.getId() + ".");
		    }
	
		    if (dictEntry.getChildren() != null
			    && dictEntry.getChildren().size() > 0) {
			if (find) {
				mergIdSeqAndLevelForDictEntry(null, new ArrayList<DictEntry>(
				    dictEntry.getChildren()), true);
			}
			dictEntry.setIsLeaf(false);
	
		    } else {
			dictEntry.setIsLeaf(true);
		    }
	
		    o_dictEntryDAO.merge(dictEntry);
		}
		if (subList != null) {
		    for (DictEntry inDictEntry : subList) {
		    	mergIdSeqAndLevelForDictEntry(inDictEntry, null, true);
		    }
		}
    }
    
    /**
     * 
     * <pre>
     * 取国际化信息
     * </pre>
     * 
     * @author 王 钊
     * @param dictEntryId 字典项ID
     * @return List<DictEntryI18>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
	public List<DictEntryI18> findDictEntryI18ByDictEntryId(String dictEntryId) {
		Criteria criteria = o_dictEntryI18DAO.createCriteria();
		criteria.setCacheable(true);
		criteria.add(Restrictions.eq("dictEntry.id", dictEntryId));
		List<DictEntryI18> i18list = criteria.list();
		return i18list;
    }

    /**
     * 
     * <pre>
     * findDictEntryByNum:取DictEntry
     * </pre>
     * 
     * @author 王 钊
     * @param num 字典项编号
     * @return
     * @since fhd　Ver 1.1
     */
    public DictEntry findDictEntryByNum(String num) {
		Criteria dc = o_dictEntryDAO.createCriteria();
		dc.setCacheable(true);
		dc.add(Restrictions.eq("id", num));
	
		@SuppressWarnings("unchecked")
		List<DictEntry> list = dc.list();
		if (list.size() == 0) {
		    return null;
		} else {
		    return list.get(0);
		}
    }

    /**
     * 
     * <pre>
     * findDictEntryI18:查询DictEntryI18表
     * </pre>
     * 
     * @author 王 钊
     * @param entryId 字典项编号
     * @param sort 排序信息json数组，由前台ext控件生产
     * @return List<DictEntryI18>
     * @since fhd　Ver 1.1
     */
    @SuppressWarnings("unchecked")
    public List<DictEntryI18> findDictEntryI18(String entryId, String sort) {
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = binder.fromJson(sort,
			(new ArrayList<HashMap<String, String>>()).getClass());
		
		Criteria criteria = o_dictEntryI18DAO.createCriteria();
		criteria.setCacheable(true);
		criteria.add(Restrictions.eq("dictEntry.id", entryId));
		if (null != sortList) {
		    for (Map<String, String> msort : sortList) {
			String property = msort.get("property");
			String direction = msort.get("direction");
			if (StringUtils.isNotBlank(property)
				&& StringUtils.isNotBlank(direction)) {
			    if ("desc".equalsIgnoreCase(direction)) {
				criteria.addOrder(Order.desc(property));
			    } else {
				criteria.addOrder(Order.asc(property));
			    }
			}
		    }
		} else {
		    criteria.addOrder(Order.asc("name"));
		}
		return criteria.list();
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
	 * @param parentId：父Id（如果为null则查询根节点）
	 * @param dictTypeId：数据字典类型id
	 * @param isLeaf：是否为叶子结点
	 * @return List<DictEntry>
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<DictEntry> findBySome(String parentId,String dictTypeId,Boolean isLeaf){
		Criteria criteria = o_dictEntryDAO.createCriteria();
		if(StringUtils.isNotBlank(dictTypeId)){
			criteria.add(Restrictions.eq("dictType.id", dictTypeId));
		}
		if(StringUtils.isNotBlank(parentId)&&!"null".equals(parentId)){
			criteria.add(Restrictions.eq("parent.id", parentId));
		}else if("null".equals(parentId)){
			criteria.add(Restrictions.isNull("parent"));
		}
		if(null!=isLeaf){
			criteria.add(Restrictions.eq("isLeaf", isLeaf));
		}
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	/**
	 * 根据DictEntry ids（多个）查询 DictEntry
	 * @author 邓广义
	 * @param ids 数据字典实体id
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<DictEntry> findDictEntryByIds(String[] ids){
		Criteria criteria = o_dictEntryDAO.createCriteria();
		if(null!=ids){
			criteria.add(Restrictions.in("id", ids));
		}
		return criteria.list();
	}
}