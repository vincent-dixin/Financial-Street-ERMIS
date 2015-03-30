<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>

<%
	//数据初始化
	init();

    //获取传递的url参数
	String fun = request.getParameter("fun");
    String node = request.getParameter("node");
    String ids = request.getParameter("ids");

    String str = "";
	if(fun.equals("treeLoader")){
		str = treeLoader(node);
	}else if(fun.equals("findByIds")){
		str = findByIds(ids);
	}else{
	}
	out.write(str);
%>
<%!
	//全局数据属性
    private List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
    
    //初始化方法
    public void init(){
    	if(datas.size()==0){	//初始化只执行一次
    		Map<String, Object> map1 = new HashMap<String, Object>();
        	map1.put("id", 1);
        	map1.put("text", "辽宁省");
        	map1.put("cls", "org");
        	map1.put("leaf", false);
        	map1.put("checked", false);
        	map1.put("iconCls", "icon-org");
        	map1.put("title", "liaoning");
        	map1.put("name", "辽宁省");
        	map1.put("parentId", "");
        	map1.put("idSeq", ".1.");
        	datas.add(map1);
        	
        	Map<String, Object> map2 = new HashMap<String, Object>();
        	map2.put("id", 2);
        	map2.put("text", "大连市");
        	map2.put("cls", "org");
        	map2.put("leaf", true);
        	map2.put("checked", false);
        	map2.put("iconCls", "icon-org");
        	map2.put("title", "dalian");
        	map2.put("name", "大连市");
        	map2.put("parentId", "1");
        	map2.put("idSeq", ".1.2.");
        	datas.add(map2);
        	
        	Map<String, Object> map3 = new HashMap<String, Object>();
        	map3.put("id", 3);
        	map3.put("text", "北京市");
        	map3.put("cls", "org");
        	map3.put("leaf", true);
        	map3.put("checked", false);
        	map3.put("iconCls", "icon-org");
        	map3.put("title", "beijing");
        	map3.put("name", "北京市");
        	map3.put("parentId", "");
        	map3.put("idSeq", ".3.");
        	datas.add(map3);
    	}
    }
    
	//获取树
	private String treeLoader(String node){
		if(node == null || "".equals(node)){
			return "";
		}
		
		List<Map<String, Object>> selecteddatas = new ArrayList<Map<String, Object>>();
		for(int i=0;i<datas.size();i++){
			Map<String, Object> item = datas.get(i);
			String parentId = item.get("parentId").toString();
			if("root".equals(node)){
				if("".equals(parentId)){
					selecteddatas.add(item);
				}
			}else{
				if(parentId.equals(node)){
					selecteddatas.add(item);
				}
			}
		}
		JSONArray array = JSONArray.fromObject(selecteddatas);
		
		return array.toString();
	}
	
	//初始化方法
	private String findByIds(String ids){
		JSONObject data = new JSONObject();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		JSONArray idArray = JSONArray.fromObject(ids);
		//获取初始值的详细信息
		for(int j=0;j<idArray.size();j++){
			JSONObject obj = (JSONObject)idArray.get(j);
			String id = obj.get("id").toString();
			for(int i=0;i<datas.size();i++){
				Map<String, Object> item = datas.get(i);
				String mid = item.get("id").toString();
				if(id.equals(mid)){
					list.add(item);
				}
			}
		}
		JSONArray datas = JSONArray.fromObject(list);
		
		data.put("success", true);
		data.put("data", datas);
		return data.toString();
	}
%>
