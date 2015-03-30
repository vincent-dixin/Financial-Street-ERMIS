package com.fhd.sys.business.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fhd.core.utils.Identities;
import com.fhd.sys.dao.menu.MenuManagementDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.interfaces.IDictBO;
import com.fhd.sys.web.form.menu.MenuManagementForm;

@Service
public class MenuManagementBO  {
	
	@Autowired
	private MenuManagementDAO menuManagementDAO;
	@Autowired
	private IDictBO iDictBO;
	/**
	 * @param nodeId 当前节点ID
	 * @desc 通过节点ID 查询某个节点 和 此节点下的第一级子节点的数据
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findMenuByNodeId(String nodeId,String query) {
		List<List<String>> ids = new ArrayList<List<String>>();
		if(null!=query){//查询条件的时候
			
			Criteria forQueryCondition = menuManagementDAO.createCriteria();
			forQueryCondition.add(Restrictions.like("authorityName", query,MatchMode.ANYWHERE));
			List<SysAuthority> querylist = forQueryCondition.list();
			for(SysAuthority sa:querylist){
				List<String> tempList = new ArrayList<String>();
				String[] seqs = sa.getSeqNo().split("\\.");
				for(int i=1;i<seqs.length;i++){
					tempList.add(seqs[i]);
				}
				ids.add(tempList);
			}

		}
		SysAuthority currentRoot ;					//当前root节点的对象
		//获得当前节点对象
		Criteria forCurrentNode = menuManagementDAO.createCriteria();
		//获得当前节点子节点对象
		Criteria forChildrenNode = menuManagementDAO.createCriteria();
		if("root".equals(nodeId)){
			forCurrentNode.add(Restrictions.isNull("parentAuthority"));
		}else{
			forCurrentNode.add(Restrictions.eq("id", nodeId));
		}
		currentRoot = (SysAuthority) forCurrentNode.list().get(0);
		//获得当前节点下 的list对象
		
		forChildrenNode.add(Restrictions.eq("parentAuthority.id", "root".equals(nodeId)?currentRoot.getId():nodeId));
		forChildrenNode.add(Restrictions.eq("etype", "M"));
		List<SysAuthority> list = forChildrenNode.list();
		return this.convertListToMapForTree(currentRoot,list,ids);
	}
	/**
	 * 把list转换成Map 为了tree的格式
	 * @return Map
	 */
	public Map<String,Object> convertListToMapForTree(SysAuthority currentRoot ,List<SysAuthority> list,List<List<String>> ids){
		Map<String,Object> result = new HashMap<String,Object>(0);
		List<Map<String , Object>> secondNodeList = new ArrayList<Map<String , Object>>();
		
		for(SysAuthority sa : list){//遍历子节点
			if(0!=ids.size()){
				for(List<String> listId : ids){//遍历查询结果的seqNO
					if(listId.contains(sa.getId())){
						Map<String,Object> temp = new HashMap<String,Object>();
							temp.put("id", sa.getId());
							temp.put("text", sa.getAuthorityName());
							temp.put("leaf", sa.getIsLeaf()?true:false);
							temp.put("expanded", true);
							secondNodeList.add(temp);
						break;
					}
				}
			}else{
					Map<String,Object> temp = new HashMap<String,Object>();
					temp.put("id", sa.getId());
					temp.put("text", sa.getAuthorityName());
					temp.put("leaf", sa.getIsLeaf()?true:false);
					secondNodeList.add(temp);
			}
		}
		if(currentRoot.getParentAuthority()==null){//第一次加载根节点时候或查询时候
			result.put("text", currentRoot.getAuthorityName());
			result.put("children", secondNodeList);
			result.put("id", currentRoot.getId());
			result.put("expanded", true);
			result.put("leaf", false);
			List<Map<String , Object>> firstNodeList = new ArrayList<Map<String , Object>>();
			firstNodeList.add(result);
			Map<String , Object> map = new  HashMap<String , Object>();
			map.put("children", firstNodeList);
			return map;
		}
		 result.put("children", secondNodeList);
		 return result;
	}
	/**
	 * 删除菜单通过id
	 */
	@Transactional
	public Map<String,Object> delMenuById(String ids){
		Assert.hasText(ids);
		String [] str = ids.split("\\,");
		for(int i=0;i<str.length;i++){
			this.menuManagementDAO.delete(str[i]);
		}
		Map<String,Object> map = new HashMap<String,Object>(0);
		  map.put("success", true);
		  return map;
		
	}
	/**
	 * 保存或者更新数据
	 */
	@Transactional
	public Map<String,Object> saveMenuInfo(String id, MenuManagementForm menuForm,String parentId,String type){
		
		SysAuthority entity = new SysAuthority();
		parentId = "root".equals(parentId)?"":parentId;
		BeanUtils.copyProperties(menuForm, entity);//拷贝属性到实体
		String entityid = true==StringUtils.isNotBlank(id)?id:Identities.uuid();//获得实体id
		//获得父节点对象
		SysAuthority parentEntity = true==StringUtils.isNotBlank(parentId)?this.findMenuInfoById(parentId):null;
		//处理seqNo
		String seqNo = null==parentEntity?"."+entityid+".":parentEntity.getSeqNo()+entityid+".";
		String menuType = StringUtils.isNotBlank(type)?type:"M";
			entity.setId(entityid);//主键
			entity.setParentAuthority(parentEntity);//parentId
			entity.setSeqNo(seqNo);//序列
			entity.setIsLeaf(!"0yn_n".equals(menuForm.getIsLeafs()));//是否叶子
			entity.setEtype(menuType);
		this.menuManagementDAO.merge(entity);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", entity.getId());
		map.put("text", entity.getAuthorityName());
		map.put("leaf", entity.getIsLeaf());
		return map;
	}
	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SysAuthority findMenuInfoById(String id){
		List<SysAuthority> list ;
		if("root".equals(id)){//查询根节点
			Criteria rootNode = menuManagementDAO.createCriteria();
			rootNode.add(Restrictions.isNull("parentAuthority"));
		    list = rootNode.list();
		}else{
			list = this.menuManagementDAO.createCriteria().add(Restrictions.eq("id", id)).list();
		}
		SysAuthority sa = null;
		if(null!=list){
			 sa = list.size()==1?list.get(0):new SysAuthority();
		}
		return sa;
	}
	/**
	 * 根据id查询当前对象下的所有子节点
	 * @param id
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysAuthority> findChildrenListByParentIdAndType(String id,String type){
		List<SysAuthority> list = this.menuManagementDAO.createCriteria().add(Restrictions.eq("parentAuthority.id", id)).add(Restrictions.eq("etype", type)).addOrder(Order.asc("sn")).list();
		return list;
	}
	/**
	 * 根据dic_type_id获得字典实体
	 * @param dictype
	 * @return
	 */
	public Map<String,Object> findDicEntityByDictype(String dictype) {
		List<DictEntry> DictEntrys = iDictBO.findDictEntryByDictTypeId(dictype);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(null!=list){
			for(DictEntry di : DictEntrys){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", di.getId());
				map.put("name", di.getName());
				list.add(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>(0);
		result.put("dictEnties", list);
		return result;
	}
	/**
	 * json转换工具
	 * @param sa
	 * @return map
	 */
	public Map<String,Object> convertObjectToMap(List<SysAuthority> list){
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String,Object> target = null;
		for(SysAuthority sa :list){
			Map<String,Object> map = new HashMap<String,Object>();
				map.put("authorityCode",sa.getAuthorityCode());
				map.put("authorityName", sa.getAuthorityName());
				map.put("url", sa.getUrl());
				map.put("isLeafs", sa.getIsLeaf()?"0yn_y":"0yn_n");
				map.put("sn", sa.getSn());
				map.put("rank", sa.getRank());
				map.put("seqNo", sa.getSeqNo());
				map.put("icon", sa.getIcon());
				map.put("etype", sa.getEtype());
				map.put("currentId", sa.getId());
				if(list.size()>1) {
					listmap.add(map);
				}else{
					target = map;
				}
		}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("data", list.size()>1?listmap:target);
			result.put("success", true);
		return result;
	}
	/**
	 * 通过ID查询对象并转换成MAP
	 * @param id
	 * @return
	 */
	public Map<String,Object> findMenuInfoByidToMap(String id){
		
		List<SysAuthority> list = new ArrayList<SysAuthority>();
		SysAuthority sa = this.findMenuInfoById(id);
		list.add(sa);
		Map<String,Object> map = this.convertObjectToMap(list);
		return map;
	}
	/**
	 * json转换工具
	 * @param sa
	 * @return list
	 */
	public List<Map<String,Object>> convertObjectToList(List<SysAuthority> list){
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(SysAuthority sa :list){
			Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", sa.getId());
				map.put("parentId", sa.getParentAuthority().getId());
				map.put("parentName", sa.getParentAuthority().getAuthorityName());
				map.put("authorityCode",sa.getAuthorityCode());
				map.put("authorityName", sa.getAuthorityName());
				map.put("url", sa.getUrl());
				map.put("isLeafs", false==sa.getIsLeaf()?"0yn_n":"0yn_y");
				map.put("sn", sa.getSn());
				map.put("rank", sa.getRank());
				map.put("seqNo", sa.getSeqNo());
				map.put("icon", sa.getIcon());
				map.put("etype", sa.getEtype());
				listmap.add(map);
		}
		return listmap;
	}
	/**
	 * 表格的保存方法  
	 * @param parentId 父ID
	 * @param modifyRecords 改变的记录
	 */
	@Transactional
	public void saveMenuInfoForGrid(String modifyRecords,String type){
		System.out.println(modifyRecords);
		JSONArray jsonArray=JSONArray.fromObject(modifyRecords);
		int j = jsonArray.size();
		for(int i=0;i<j;i++){
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String id = jsonObj.getString("id");
			String parentId = jsonObj.getString("parentId");
			String code = jsonObj.getString("authorityCode");
			String name = jsonObj.getString("authorityName");
			String url = jsonObj.getString("url");
			String isLeafs = jsonObj.getString("isLeafs");
			String icon = jsonObj.getString("icon");
			Integer rank = jsonObj.getInt("rank"); 
			Integer sn = jsonObj.getInt("sn");
			SysAuthority entity = null;
			if(!StringUtils.isBlank(id)){
				entity = this.menuManagementDAO.get(id);
			}else{
				entity = new SysAuthority();
				entity.setId(Identities.uuid());
			}
			entity.setAuthorityCode(code);
			entity.setAuthorityName(name);
			entity.setEtype(type);//保存不同类型的实体  T B 页签与按钮
			entity.setUrl(url);
			entity.setIsLeaf("0yn_y".equals(isLeafs));
			entity.setIcon(icon);
			entity.setRank(rank);
			entity.setSn(sn);
			SysAuthority parentEntity = this.menuManagementDAO.get(parentId);
			entity.setParentAuthority(parentEntity);
			String seqNo = null==parentEntity?"."+entity.getId()+".":parentEntity.getSeqNo()+entity.getId()+".";
			entity.setSeqNo(seqNo);
			this.menuManagementDAO.merge(entity);
		}
	}
	/**
	 * 根据菜单编号查询对象 判断是否存在
	 * @param menuCodes 编号
	 * @param currentIds记录的ID 新增的记录为""
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean findfindMenuInfoByCodeIfExist(String menuCodes,String currentIds){
		String codes[] = menuCodes.split("\\,");
			boolean flag = false;
			Criteria criteria = this.menuManagementDAO.createCriteria();
			criteria.add(Restrictions.in("authorityCode", codes));
			if(null!=currentIds){
				criteria.add(Restrictions.not(Restrictions.in("id", currentIds.split("\\,"))));
			}
			List<SysAuthority> list = criteria.list();
			if(null!=list && list.size()>0){
				flag = true;
			}
		return flag;
	}
}
