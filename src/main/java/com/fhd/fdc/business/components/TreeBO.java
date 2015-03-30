package com.fhd.fdc.business.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.fhd.fdc.dao.CommonDAO;
import com.fhd.fdc.utils.MapListUtil;
import com.fhd.fdc.utils.UserContext;


/**
 * 布局组件BO类.
 * entity必须有的属性:name,IdSeq
 * @author zhengjunxiang
 * @version V1.0 创建时间：20130-5-3 
 * Company FirstHuiDa.
 */

@Service
@SuppressWarnings("unchecked")
public class TreeBO {
	
	@Autowired
	private CommonDAO o_commonDAO;

	/**
	 * 查询树 zhengjunxiang
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> treeLoader(Class clz,String id,Boolean checkable,String chooseId,String query,String queryKey,String parentKey,String relationKey,Map paraMap){
//		long start  = System.currentTimeMillis();
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
		Set<String> idSet = queryObjectBySearchName(clz,query,queryKey,relationKey,paraMap);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}

		if("root".equals(id)){
			criteria.add(Restrictions.isNull(parentKey));//"parent.id"
		}else{
			criteria.add(Restrictions.eq(parentKey, id));
		}
		//添加前台传入的过滤条件
		Iterator iterator = paraMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next(); 
		    String key = entry.getKey().toString(); 
		    Object value = entry.getValue();
		    criteria.add(Restrictions.eq(key, value));
		}
		//String companyId = UserContext.getUser().getCompanyid();
		criteria.addOrder(Order.asc("id"));
		List<HashMap<String, Object>> treeList = MapListUtil.toMapList(criteria.list());
		
		for (HashMap<String, Object> map : treeList) {
			if(!idSet.contains(map.get("id").toString())){
				continue;
			}
			map.put("text", map.get(queryKey)==null?"":map.get(queryKey).toString());
			map.put("iconCls", "icon-org");
			map.put("cls", "org");
			List<HashMap<String, Object>> children = queryObjectByParent(clz,id,parentKey,paraMap);
			if(children.size()==0){
				map.put("leaf", true);
			}else{
				map.put("leaf", false);
			}
			
			if(checkable) {
				if(StringUtils.isNotEmpty(chooseId)){
					for (String choose : StringUtils.split(chooseId,",")) {
						if(map.get("id").toString().equals(choose)){
							map.put("checked", true);
							break;
						}
						map.put("checked", false);
					}
				}else{
					map.put("checked", false);
				}
			}
			nodes.add(map);
		}
//		long end  = System.currentTimeMillis();
//		System.out.println("树查询时间："+(end-start));
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryObjectBySearchName(Class<?> clz,String query,String queryKey,String relationKey,Map paraMap){
//		long start  = System.currentTimeMillis();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like(queryKey,query,MatchMode.ANYWHERE));
		}
		//添加前台传入的过滤条件
		Iterator iterator = paraMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next(); 
		    String key = entry.getKey().toString(); 
		    Object value = entry.getValue();
		    criteria.add(Restrictions.eq(key, value));
		}
		list = MapListUtil.toMapList(criteria.list());
		
		for (HashMap<String,Object> map : list) {
			String[] idsTemp = map.get(relationKey).toString().split("\\.");//"idSeq"
			idSet.addAll(Arrays.asList(idsTemp));
		}
//		long end  = System.currentTimeMillis();
//		System.out.println("树搜索时间："+(end-start));//874条数据，要9s
		return idSet;
	}
	@SuppressWarnings("unchecked")
	protected List<HashMap<String, Object>> queryObjectByParent(Class<?> clz,String parentId,String parentKey,Map paraMap){
		
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
		criteria.setCacheable(true);
		if("root".equals(parentId)){
			criteria.add(Restrictions.isNull(parentKey));//
		}else{
			criteria.add(Restrictions.eq(parentKey, parentId));
		}
		//添加前台传入的过滤条件
		Iterator iterator = paraMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next(); 
		    String key = entry.getKey().toString(); 
		    Object value = entry.getValue();
		    criteria.add(Restrictions.eq(key, value));
		}
		list = MapListUtil.toMapList(criteria.list());
		
		return list;
	}
	
	/**
	 * 根据Id的Set集合查询列表 zhengjunxiang
	 */
	public List<HashMap<String, Object>> findObjectByIdSet(Class<?> clz,Set<String> idSet) {
		Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
		if(null != idSet && idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet.toArray()));
		}else{
			criteria.add(Restrictions.isNotNull("id"));
		}
		criteria.addOrder(Order.asc("id"));
		return MapListUtil.toMapList(criteria.list());
	}
	
	/**
     * 按树Id查询信息列表
     */
    @SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> findListByTreeId(Class<?> clz,String foreignKey,String treeId,String query,String queryKey)
    {
    	Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
		criteria.add(Restrictions.eq(foreignKey, treeId));
		if(null!=query && StringUtils.isNotEmpty(query)){
			criteria.add(Restrictions.like(queryKey, query,MatchMode.ANYWHERE));
		}
		
		return MapListUtil.toMapList(criteria.list());
    }
    
    /**
     * 查询列表数据Page
     * @param page
     * @param sort
     * @param dir
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> findListBySome(Class<?> clz,String query,String queryKey,String sort, String dir){
		
    	Criteria criteria = o_commonDAO.getSession().createCriteria(clz);
    	if(null!=query && StringUtils.isNotEmpty(query)){
			criteria.add(Restrictions.like(queryKey, query,MatchMode.ANYWHERE));
		}
		//只支持表内属性，复合属性有可能出现问题
        String sortstr = sort;
        
        if ("ASC".equalsIgnoreCase(dir)) {
        	criteria.addOrder(Order.asc(sortstr));
        }
        else {
        	criteria.addOrder(Order.desc(sortstr));
        }
        
        return MapListUtil.toMapList(criteria.list());
	}
}