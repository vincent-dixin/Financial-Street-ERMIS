package com.fhd.fdc.web.controller.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fhd.fdc.business.components.TreeBO;

@Controller
public class TreeControl {

	@Autowired
	private TreeBO o_treeBO;
	
	/**
	 * 树选择组件、树选择组件
     * 通用树选择布局组件查询
     * @author 郑军祥
     * @param entityName 树的实体
     * @param node 树传过来的id
     * @param checkable 是否是多选树
     * @param chooseId 树初始化已选的树id  格式：“[{id:'2'},{id:2}]”
     * @param query 查询条件
     * @param queryKey 树查询的属性名称
     * @param parentKey 查询上级节点的key
     * @param relationKey 树查询依据的健值 一般是idSeq
     * @return Map<String, Object>
     */
    @ResponseBody
    @RequestMapping(value = "/components/treeLoader")
    public List<Map<String, Object>> treeLoader(String entityName,String node,Boolean checkable,String chooseId,String query,String queryKey,String parentKey,String relationKey,String parameters) throws Exception {
		Class clz = Class.forName(entityName);
		
		//将request中内容取出放在HashMap中，传入后台进行数据过滤
		JSONObject paraMap = new JSONObject();
		if(parameters!=null && parameters!=""){
			paraMap = JSONObject.fromObject(parameters);
		}
		
    	return o_treeBO.treeLoader(clz,node,checkable,chooseId,query,queryKey,parentKey,relationKey,paraMap);
    }
    
    /**
     * 树选择组件
	 * 数选择布局组件初始化
	 * @author 郑军祥
	*/
	@ResponseBody
    @RequestMapping(value = "/components/initByIds")
    public Map<String,Object> initByIds(String entityName,String ids) throws Exception{
		Class clz = Class.forName(entityName);
		
		Set<String> idSet = new HashSet<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		if(StringUtils.isNotBlank(ids)){
			JSONArray jsonArray=JSONArray.fromObject(ids);
			if(jsonArray.size()==0){
				return null;
			}
			
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				if(StringUtils.isNotBlank(jsonObject.getString("id"))){
					idSet.add(jsonObject.getString("id"));
				}
			}
			list = o_treeBO.findObjectByIdSet(clz,idSet);
			
			map.put("success", true);
			map.put("data", list);
		}
		return map;
	}
	
	
    /**
     * 树列表选择组件、列表选择组件
	 * 树列表选择布局组件初始化
	 * @author 郑军祥
	*/
	@ResponseBody
    @RequestMapping(value = "/components/initListByIds")
    public Map<String,Object> initListByIds(String entityName,String ids) throws Exception{
		Class clz = Class.forName(entityName);
		
		Set<String> idSet = new HashSet<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		if(StringUtils.isNotBlank(ids)){
			JSONArray jsonArray=JSONArray.fromObject(ids);
			if(jsonArray.size()==0){
				return null;
			}
			
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				if(StringUtils.isNotBlank(jsonObject.getString("id"))){
					idSet.add(jsonObject.getString("id"));
				}
			}
			list = o_treeBO.findObjectByIdSet(clz,idSet);
			
			map.put("success", true);
			map.put("data", list);
		}
		return map;
	}
	
	/**
	 * 树列表选择组件
	 * 根据树id查询列表数据
	 * @param id 传过来的tree id
	 * @param foreignKey 列表关联树的外键名称
	 * @param entityName 列表显示的实体
	 * @param query 列表查询的内容
	 * @param queryKey 列表按那个属性查询，前台进行指定
	 * @author 郑军祥
	 */
	@ResponseBody
	@RequestMapping("/components/findListByTreeId")
	public Map<String, Object> findListByTreeId(String id,String foreignKey,String entityName,String query,String queryKey) throws Exception{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		Class clz = Class.forName(entityName);
		list = o_treeBO.findListByTreeId(clz,foreignKey,id,query,queryKey);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", list.size());
		map.put("datas", list);
		
		return map;
	}
	
	
	/**
	 * 列表选择组件
	 * 根据树id查询列表数据
	 * @author 郑军祥
	 */
	@ResponseBody
	@RequestMapping("/components/findListBySome")
	public Map<String, Object> findListBySome(String entityName,int start, int limit, String query, String sort,String queryKey) throws Exception {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		Class clz = Class.forName(entityName);

		//获取数据
		String dir = "DESC";
        String sortColumn = "id";
		if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");//按照哪个字段排序
                dir = jsobj.getString("direction");//排序方向
            }
        }
		//？？1如何添加分页信息,  2排序有问题
        list = o_treeBO.findListBySome(clz,query,queryKey,sortColumn,dir);

		map.put("totalCount", list.size());
		map.put("datas", list);
		
		return map;
	}
}