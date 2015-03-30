package com.fhd.test.web.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.fhd.test.entity.TestMvc;
public class TestMvcForm {
	
	private String id;
	
	private String title;
	
	private String name;

	private String myLocale;
	
	private String parentId;

	private Integer level;

	private Double num;
	
	private String parentName;
	
	private String myLocaleName;
	
	private String dept;
	
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMyLocale() {
		return myLocale;
	}

	public void setMyLocale(String myLocale) {
		this.myLocale = myLocale;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	
	public String getMyLocaleName() {
		return myLocaleName;
	}

	public void setMyLocaleName(String myLocaleName) {
		this.myLocaleName = myLocaleName;
	}

	public TestMvcForm(){
	}
	
	public TestMvcForm(TestMvc test){
		this.setId(test.getId());
		this.title = test.getTitle();
		this.name = test.getName();
		this.myLocale = test.getMyLocale();
		if(null != test.getParent()){
			this.parentId = test.getParent().getId();
			this.parentName = test.getParent().getName();
		}else{
			this.parentName = "";
		}
		if("en".equals(test.getMyLocale())){
			this.myLocaleName = "英文";
		}else if("zh-cn".equals(test.getMyLocale())){
			this.myLocaleName = "中文";
		}
		this.level = test.getLevel();
		this.num = null != test.getNum()?test.getNum():0d;
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> map2 = new HashMap<String,Object>();
		map.put("id", "13c2667cdfe444d99c0625cbec215375");
//		map.put("deptno", "HuiDa01.21");
		//map.put("deptname", "转包与民机事业部");
		map2.put("id", "0e5254f249e74d63be576c8b8076c4ca");
//		map2.put("empno", "HuiDa01.04");
		//map2.put("deptname", "数据中心");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		list.add(map2);
		JSONArray json = new JSONArray();  
        json.addAll(list); 
		this.dept =  "[{'deptid':'13c2667cdfe444d99c0625cbec215375','empid':''},{'deptid':'0e5254f249e74d63be576c8b8076c4ca','empid':'ffd6345a24f8468d88212d847dc44538'},{'deptid':'0e5254f249e74d63be576c8b8076c4ca','empid':'3d9b5b4a14654df28fb76d0b429c9378'}]";//json.toString();
	}
}
