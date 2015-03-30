
package com.fhd.test.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.test.dao.TestMvcDAO;
import com.fhd.test.entity.TestMvc;
import com.fhd.test.interfaces.ITestMvcBO;

@SuppressWarnings("unchecked")
@Service
public class TestMvcBO implements ITestMvcBO   {

	
	@Autowired
	private TestMvcDAO o_testMvcDAO;
	
	
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#saveDictEntry(com.fhd.test.entity.testMvc.entity.TestMvc)
	 */
	@Transactional
	public void save(TestMvc testMvc) {
		o_testMvcDAO.merge(testMvc);
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#saveBatch(java.util.Collection)
	 */
	public void saveBatch(Collection<TestMvc> testMvcs) {
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#mergeDictEntry(com.fhd.test.entity.testMvc.entity.TestMvc)
	 */
	
	@Transactional
	public void update(TestMvc testMvc) {
		o_testMvcDAO.merge(testMvc);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#updateBatch(java.util.Collection)
	 */
	public void updateBatch(Collection<TestMvc> testMvcs) {
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#delete(com.fhd.test.entity.testMvc.entity.TestMvc)
	 */
	@Transactional
	public void delete(TestMvc testMvc) {
		o_testMvcDAO.delete(testMvc);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#deleteById(java.lang.String)
	 */
	@Transactional
	public void deleteById(String id) {
		o_testMvcDAO.delete(id);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.fhd.test.interfaces.testMvc.interfaces.ITestMvcBO#deleteBatch(java.util.Collection)
	 */
	public void deleteBatch(Collection<TestMvc> testMvcs) {

	}
	
	/**
	 * <pre>
	 * get:根据ID获得TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public TestMvc get(String id){
		return o_testMvcDAO.get(id);
	}
	
	/**
	 * <pre>
	 * queryTestMvcByTitle:根据title获得非id的TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param title
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public TestMvc queryTestMvcByTitle(String title,String id){
		TestMvc dictType = new TestMvc();
		Criteria criterions = o_testMvcDAO.createCriteria();
		criterions.add(Restrictions.eq("title", title));
		if(StringUtils.isNotBlank(id)){
			criterions.add(Restrictions.not(Restrictions.eq("id",id)));
		}
		criterions.setCacheable(true);
		List<TestMvc> dictTypeList = criterions.list();
		if(null != dictTypeList && dictTypeList.size()>0){
			dictType = dictTypeList.get(0);
		}
		return dictType;
	}
	
	/**
	 * <pre>
	 * queryTestMvcByTitleAndNameAndMyLocaleAndPageAndSortAndDir:根据条件查询TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dictTypeTitle
	 * @param dictEntryName
	 * @param myLocale
	 * @param page
	 * @param sort
	 * @param dir
	 * @return Page<TestMvc>
	 * @since  fhd　Ver 1.1
	*/
	public Page<TestMvc> queryTestMvcByTitleAndNameAndMyLocaleAndPageAndSortAndDir(String title,String name,String myLocale,Page<TestMvc> page,String sort,String dir){
		DetachedCriteria dc = DetachedCriteria.forClass(TestMvc.class);
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(title)){
			dc.add(Restrictions.or(Property.forName("name").like(name, MatchMode.ANYWHERE), Property.forName("title").like(title, MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(myLocale)){
			dc.add(Property.forName("myLocale").eq(myLocale));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_testMvcDAO.findPage(dc, page, false);
	}
	
	/**
	 * <pre>
	 * loadTreeTest:获得TestMvc的树状结构,树形下拉列表使用
	 * </pre>
	 * 
	 * @author 张 雷
	 * @return 
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> loadTestMvcTree(){
		return this.loadTestMvcTree(new ArrayList<TestMvc>(o_testMvcDAO.getAll()), null,null);
	}
	
	public Map<String,Object> loadTestMvcTreeById(String id,String query)
	{
	 
		
		List<TestMvc> list = new ArrayList<TestMvc>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_testMvcDAO.createCriteria();
		Criteria c = o_testMvcDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			c.add(Restrictions.like("name",query,MatchMode.ANYWHERE));
		}
		if(StringUtils.isBlank(id))
		{
			c.add(Restrictions.isNull("parent"));
		}else
		{
			c.add(Restrictions.eq("parent.id", id));
		}
		list = c.list();
		for (TestMvc testMvc : list) {
			String[] idsTemp = testMvc.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		if(idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet));
		}else{
			criteria.add(Restrictions.isNull("id is null"));
		}
		c.addOrder(Order.asc("num"));
		
	 
		return queryTestMvcTreeToMap(criteria.list(),query);
	}
	
	
	/**
	 * <pre>
	 * loadTreeTest:获得TestMvc的树状结构,树形下拉列表使用，带查询条件
	 * </pre>
	 * 
	 * @author 张 雷
	 * @return 
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> loadTestMvcTree(String query){
		List<TestMvc> list=queryTestMvcBySearchName(query);
		Set<TestMvc> set=new HashSet<TestMvc>(list);
		return this.loadTestMvcTree(new ArrayList<TestMvc>(set), null,query);
	}
	
	
	
	/**
	 * <pre>
	 * loadTestMvcTree:根据list和parent获得子节点，递归调用
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param list TestMvc的列表
	 * @param parent 父级记录
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	private Map<String,Object> loadTestMvcTree(List<TestMvc> set,TestMvc parent,String searchName) {
		Map<String,Object> item = new HashMap<String,Object>();
		List<TestMvc> subList = new ArrayList<TestMvc>();
		Set<String> idSet = this.queryTestMvcIdsBySearchName(searchName);
		if(null != set && set.size()>0){
			for (TestMvc entity : set) {
				if(null == parent){
            		if(null == entity.getParent()){
            			if(idSet.contains(entity.getId()))
            			{
            			subList.add(entity);
            			}
            		}
            	}else{
            		if(null != entity.getParent() && entity.getParent().getId().equals(parent.getId())){
            			if(idSet.contains(entity.getId()))
            			{
            			subList.add(entity);
            			}
                	}
            	}
			}
		}
		if(null != parent){
		
			item.put("id", parent.getId());
			item.put("text", parent.getName());
			item.put("title", parent.getTitle());
			item.put("num",parent.getNum());
			item.put("level",parent.getLevel());
			item.put("checked",false);
			item.put("myLocale", parent.getMyLocale());
			if(subList.size()>0){
				item.put("expanded", true);
				item.put("leaf", false);
			}else{
				item.put("leaf", true);
			}
		}
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		for (TestMvc testMvc : subList) {
			Map<String,Object> subItem = new HashMap<String,Object>();
			subItem.put("id", testMvc.getId());
			subItem.put("text", testMvc.getName());
			subItem.put("title", testMvc.getTitle());
			subItem.put("num",testMvc.getNum());
			subItem.put("level",testMvc.getLevel());
			subItem.put("checked",false);
			subItem.put("myLocale", testMvc.getMyLocale());
			if(null != testMvc.getChildren() && testMvc.getChildren().size()>0){
				subItem.put("leaf", false);
				Map<String,Object> map = loadTestMvcTree(new ArrayList<TestMvc>(testMvc.getChildren()),testMvc,searchName);
				if(map!=null)
				{
					children.add(map);
				}
			}else{
				subItem.put("leaf", true);
				children.add(subItem);
			}
			
		}
		if(children.size()>0)
			item.put("children", children);
		return item;
    }
	
	/**
	 * <pre>
	 * queryByParentId:根据parentId获得TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId 父级记录的Id
	 * @return TestMvc的列表
	 * @since  fhd　Ver 1.1
	*/
	public List<TestMvc> queryTestMvcByParentId(String parentId){
		return o_testMvcDAO.find("From TestMvc where parent.id = ?", parentId);
	}
	
	/**
	 * <pre>
	 * isParentIdOK:parentId的合法性验证
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId 父级记录的Id
	 * @param id 当前记录的Id
	 * @return true合法，false不合法
	 * @since  fhd　Ver 1.1
	*/
	public Boolean isParentIdOK(String parentId,String id){
		Boolean flag=false;
		Criteria criteria = o_testMvcDAO.createCriteria();
		criteria.add(Restrictions.idEq(parentId));
		criteria.add(Restrictions.not(Restrictions.like("idSeq", "."+id+".",MatchMode.ANYWHERE)));
		List<TestMvc> list = criteria.list();
		if(list!=null&&list.size()>0){
			flag=true;
		}
		return flag;
	}

	/**
	 * <pre>
	 * updateBatch:通过jsonArray字符串更新
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void updateBatch(String jsonString) {
		JSONArray jsonArray=JSONArray.fromObject(jsonString);
		if(jsonArray.size()==0){
			return;
		}
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String name = jsonObject.getString("name");
			String title = jsonObject.getString("title");
			String parentId = jsonObject.getString("parentId");
			String myLocale = jsonObject.getString("myLocale");
			String numStr = jsonObject.getString("num");
			Double num = null;
			if(StringUtils.isNotBlank(numStr) && !"null".equals(numStr))
				num = Double.valueOf(numStr);
			TestMvc entity = null;
			if(StringUtils.isNotBlank(id)){
				entity = o_testMvcDAO.get(id);
			}else{
				entity = new TestMvc(Identities.uuid());
			}
			entity.setName(name);
			entity.setTitle(title);
			entity.setMyLocale(myLocale);
			if(StringUtils.isNotBlank(parentId))
				entity.setParent(new TestMvc(parentId));
			else
				entity.setParent(null);
			entity.setNum(num);
			o_testMvcDAO.merge(entity);
			setIdSeqAndLevel(entity);
		}
	}
	
	/**
	 * <pre>
	 * setIdSeqAndLevel:设置IdSeq和Level的属性
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param entity
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void setIdSeqAndLevel(TestMvc entity){
		if (null != entity.getParent()) {
			TestMvc parentEntity = o_testMvcDAO.get(entity.getParent().getId());
			entity.setLevel(parentEntity.getLevel() + 1);
			entity.setIdSeq(parentEntity.getIdSeq()+ entity.getId() + ".");
		} else {
			entity.setLevel(1);
			entity.setIdSeq("." + entity.getId() + ".");
			entity.setParent(null);
		}
		o_testMvcDAO.merge(entity);
		Iterator<TestMvc> it = entity.getChildren().iterator();
		while(it.hasNext()){
			TestMvc subEntity = it.next();
			setIdSeqAndLevel(subEntity);
		}
	}
	/**
	 * <pre>
	 * queryTestMvcBySearchName:根据searchName查询
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param searchName
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	protected List<TestMvc> queryTestMvcBySearchName(String searchName){
		Criteria criteria = o_testMvcDAO.createCriteria();
		Set<String> idSet = queryTestMvcIdsBySearchName(searchName);
		if(idSet.size()>0){
			criteria.add(Restrictions.in("id", idSet));
		}else{
			criteria.add(Restrictions.isNull("id"));
		}
		return criteria.list();
		
	}
	/**
	 * <pre>
	 * queryTestMvcIdsBySearchName:查询searchName的相关ID的集合
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param searchName
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	protected Set<String> queryTestMvcIdsBySearchName(String searchName){
		List<TestMvc> list = new ArrayList<TestMvc>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_testMvcDAO.createCriteria();
		if(StringUtils.isNotBlank(searchName)){
			criteria.add(Restrictions.like("name",searchName,MatchMode.ANYWHERE));
		}
		list = criteria.list();
		for (TestMvc testMvc : list) {
			String[] idsTemp = testMvc.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	/**
	 * <pre>
	 * queryTestMvcTree:数列表数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> queryTestMvcTree(String searchName){
		List<TestMvc> list = queryTestMvcBySearchName(searchName);
		Map<String,Object> map = queryTestMvcTree(list,null,searchName);
		return map;
	}

	/**
	 * <pre>
	 * queryTestMvcTree:查询数列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param set
	 * @param parent
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	private Map<String,Object> queryTestMvcTree(List<TestMvc> list,TestMvc parent,String searchName){
		Map<String,Object> item = new TreeMap<String,Object>();
		List<TestMvc> subList = new ArrayList<TestMvc>();
		Set<String> idSet = this.queryTestMvcIdsBySearchName(searchName);
		if(null != list && list.size()>0){
			for (TestMvc entity : list) {
				if(null == parent){
            		if(null == entity.getParent()){
            			if(idSet.contains(entity.getId()))
            			{
            			subList.add(entity);
            			}
            		}
            	}else{
            		if(null != entity.getParent() && entity.getParent().getId().equals(parent.getId())){
            			if(idSet.contains(entity.getId()))
            			{
            			subList.add(entity);
            			}
                	}
            	}
			}
		}
		if(null != parent){
		
			item.put("id", parent.getId());
			item.put("name", parent.getName());
			item.put("title", parent.getTitle());
			item.put("num",parent.getNum());
			item.put("level",parent.getLevel());
			item.put("myLocale", parent.getMyLocale());
			if(subList.size()>0){
				item.put("expanded", true);
				item.put("leaf", false);
			}else{
				item.put("leaf", true);
			}
		}
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		for (TestMvc testMvc : subList) {
			Map<String,Object> subItem = new TreeMap<String,Object>();
			subItem.put("id", testMvc.getId());
			subItem.put("name", testMvc.getName());
			subItem.put("title", testMvc.getTitle());
			subItem.put("num",testMvc.getNum());
			subItem.put("level",testMvc.getLevel());
			subItem.put("myLocale", testMvc.getMyLocale());
			if(null != testMvc.getChildren() && testMvc.getChildren().size()>0){
				subItem.put("leaf", false);
				Map<String,Object> map = queryTestMvcTree(new ArrayList<TestMvc>(testMvc.getChildren()),testMvc,searchName);
				if(map!=null)
				{
					children.add(map);
				}
			}else{
				subItem.put("leaf", true);
				children.add(subItem);
			}
			
		}
		if(children.size()>0)
			item.put("children", children);
		return item;
	}
	
	

	/**
	 * <pre>
	 * queryTestMvcTree:查询数列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param set
	 * @param parent
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	private Map<String,Object> queryTestMvcTreeToMap(List<TestMvc> subList,String query){
		Map<String,Object> item = new HashMap<String,Object>();
  
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		for (TestMvc testMvc : subList) {
			Map<String,Object> subItem = new HashMap<String,Object>();
			subItem.put("id", testMvc.getId());
			subItem.put("text", testMvc.getName());
			subItem.put("title", testMvc.getTitle());
			subItem.put("num",testMvc.getNum());
			subItem.put("level",testMvc.getLevel());
			subItem.put("checked",false);
			subItem.put("myLocale", testMvc.getMyLocale());
			if(null != testMvc.getChildren() && testMvc.getChildren().size()>0){
				boolean leaf=false;
				 for(TestMvc tmpTestMvc :testMvc.getChildren())
				 {
					 if(StringUtils.isNotBlank(query))
					 {	
						 if( tmpTestMvc.getName().contains(query))
						 {
						 leaf=true;
						 break;
						 }
					 }
				 }
				 subItem.put("leaf", leaf);
			}else{
				subItem.put("leaf", true);
				
			}
			children.add(subItem);
			
		}
		if(children.size()>0)
			item.put("children", children);
		return item;
	}
}

