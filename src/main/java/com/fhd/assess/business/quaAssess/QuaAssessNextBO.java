package com.fhd.assess.business.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fhd.assess.dao.quaAssess.ScoreResultDAO;
import com.fhd.assess.entity.quaAssess.ScoreResult;
import com.fhd.risk.dao.ScoreDAO;
import com.fhd.risk.dao.TemplateRelaDimensionDAO;
import com.fhd.risk.entity.Score;
import com.fhd.risk.entity.TemplateRelaDimension;
import com.fhd.sys.entity.dic.DictEntry;

@Service
public class QuaAssessNextBO {

	@Autowired
	private ScoreResultDAO o_scoreResultDAO;
	
	@Autowired
	private TemplateRelaDimensionDAO o_templateRelaDimensionDAO;
	
	@Autowired
	private ScoreDAO o_scoreDAO;
	
	/**
	 * 查询维度分值关联全部信息并以sort字段进行排序
	 * @author 金鹏祥
	 * @return List<Score>
	 * */
	@SuppressWarnings("unchecked")
	private List<Score> findScoreAllList(){
		List<Score> arrayList = new ArrayList<Score>();
		Criteria criteria = o_scoreDAO.createCriteria();
		criteria.addOrder(Order.asc("sort"));
		List<Score> list = null;
		list = criteria.list();
		
		for (Score score : list) {
			arrayList.add(score);
		}
		
		return arrayList;
	}
	
	/**
	 * 查询模版关联维度全部信息
	 * @author 金鹏祥
	 * @return List<TemplateRelaDimension>
	 * */
	@SuppressWarnings("unchecked")
	private List<TemplateRelaDimension> findTemplateRelaDimensionAllList(){
		List<TemplateRelaDimension> arrayList = new ArrayList<TemplateRelaDimension>();
		Criteria criteria = o_templateRelaDimensionDAO.createCriteria();
		List<TemplateRelaDimension> list = null;
		list = criteria.list();
		
		for (TemplateRelaDimension templateRelaDimension : list) {
				arrayList.add(templateRelaDimension);
		}
		
		return arrayList;
	}
	
	/**
	 * 查询打分结果全部信息以MAP方式存储
	 * @author 金鹏祥
	 * @return String
	 * */
	@SuppressWarnings("unchecked")
	private HashMap<String, ScoreResult> findScoreResultAllMap(){
		Criteria criteria = o_scoreResultDAO.createCriteria();
		HashMap<String, ScoreResult> map = new HashMap<String, ScoreResult>();
		List<ScoreResult> list = null;
		list = criteria.list();
		
		for (ScoreResult scoreResult : list) {
			map.put(scoreResult.getScore().getId() + "--" + scoreResult.getRangObjectDeptEmpId().getId(), scoreResult);
		}
		
		return map;
	}
	
	/**
	 * 得到匹配当前模板下的所有维度
	 * @param id 模版关联维度ID
	 * @param list 模版关联维度全部信息
	 * @author 金鹏祥
	 * @return List<TemplateRelaDimension>
	 * */
	private List<TemplateRelaDimension> getTemplateRelaDimensionListByTemplateId(String id, List<TemplateRelaDimension> list){
		List<TemplateRelaDimension> arrayList = new ArrayList<TemplateRelaDimension>();
		for (TemplateRelaDimension templateRelaDimension : list) {
			if(templateRelaDimension.getTemplate().getId().equalsIgnoreCase(id)){
				arrayList.add(templateRelaDimension);
			}
		}
		
		return arrayList;
	}
	
	/**
	 * 得到匹配当前维度的下级维度
	 * @param id 模版关联维度ID
	 * @param list 模版关联维度全部信息
	 * @author 金鹏祥
	 * @return List<TemplateRelaDimension>
	 * */
	private List<TemplateRelaDimension> getTemplateRelaDimensionListByParentId(String id, List<TemplateRelaDimension> list){
		List<TemplateRelaDimension> arrayList = new ArrayList<TemplateRelaDimension>();
		for (TemplateRelaDimension templateRelaDimension : list) {
			if(templateRelaDimension.getParent() != null){
				if(templateRelaDimension.getParent().getId().equalsIgnoreCase(id)){
					arrayList.add(templateRelaDimension);
				}
			}
		}
		
		return arrayList;
	}
	
	/**
	 * 组织数据供应给前台使用
	 * @param scoreDimId 维度ID
	 * @param list 维度分值关联全部信息
	 * @param dictEntry 求值类型
	 * @param weight 权重
	 * @param rangObjectDeptEmpId 综合ID
	 * @param scoreResultAllMap 打分结果全部信息
	 * @author 金鹏祥
	 * @return ArrayList<ArrayList<String>>
	 * */
	private ArrayList<ArrayList<String>> getScoreList(String scoreDimId, List<Score> list, 
			String dimName, DictEntry dictEntry, Double weight, String parentDimId, String rangObjectDeptEmpId, 
			HashMap<String, ScoreResult>scoreResultAllMap){
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
		for (Score score : list) {
			if(score.getDimension().getId().equalsIgnoreCase(scoreDimId)){
				ArrayList<String> array = new ArrayList<String>();
				
				array.add(score.getId());
				array.add(scoreDimId);
				array.add(dimName);
				array.add(score.getDimension().getName());
				array.add(score.getName());
				if(dictEntry != null){
					array.add(rangObjectDeptEmpId + "--" + score.getValue().toString() + "--" + dictEntry.getId() + "--" + weight + "--" + parentDimId);
				}else{
					array.add(rangObjectDeptEmpId + "--" + score.getValue().toString());
				}
				
				if(scoreResultAllMap.get(score.getId() + "--" + rangObjectDeptEmpId) != null){
					array.add("true");
				}else{
					array.add("false");
				}
				
				arrayList.add(array);
			}
		}
		
		return arrayList;
	}
	
	/**
	 * 得到模板对应维度及分数
	 * @param templateId 模板ID
	 * @return ArrayList<Score>
	 * @author 金鹏祥
	 * */
	public HashMap<Integer, ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> findTemplateRelaDimByTemplateId(String params) {
		List<Score> scoreList = this.findScoreAllList();
		List<TemplateRelaDimension> templateRelaDimensionAllList = this.findTemplateRelaDimensionAllList();//templateId);
		HashMap<String, ScoreResult> scoreResultAllMap = this.findScoreResultAllMap();
		List<TemplateRelaDimension> templateRelaDimensionList = null;
		List<TemplateRelaDimension> templateRelaDimensionList2 = null;
		
		HashMap<Integer, ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> arrayListCount = 
				new HashMap<Integer, ArrayList<ArrayList<ArrayList<ArrayList<String>>>>>();
		
		ArrayList<ArrayList<ArrayList<ArrayList<String>>>> arrayList = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
		
		JSONArray jsonarr = JSONArray.fromObject(params);

		int count = 0;
		for (Object objects : jsonarr) {
			JSONObject jsobjs = (JSONObject) objects;
			
			templateRelaDimensionList = this.getTemplateRelaDimensionListByTemplateId(jsobjs.getString("templateId"), templateRelaDimensionAllList);
			for (TemplateRelaDimension templateRelaDimension : templateRelaDimensionList) {
				
				if(templateRelaDimension.getParent() == null){
					templateRelaDimensionList2 = this.getTemplateRelaDimensionListByParentId(templateRelaDimension.getId(), templateRelaDimensionAllList);
					
					ArrayList<ArrayList<ArrayList<String>>> arrays = 
							new ArrayList<ArrayList<ArrayList<String>>>();
					
					
					if(templateRelaDimensionList2.size() != 0){
						//存在下级
						for (TemplateRelaDimension template : templateRelaDimensionList2) {
							ArrayList<ArrayList<String>> templateList = 
									this.getScoreList(
											template.getDimension().getId(), 
											scoreList, 
											templateRelaDimension.getDimension().getName(), 
											templateRelaDimension.getCalculateMethod(), 
											templateRelaDimension.getWeight(), 
											templateRelaDimension.getDimension().getId(),
											jsobjs.getString("rangObjectDeptEmpId"),
											scoreResultAllMap);
							arrays.add(templateList);
						}
						arrayList.add(arrays);
					}else{
						//不存在下级
						ArrayList<ArrayList<String>> templateList = 
								this.getScoreList(
										templateRelaDimension.getDimension().getId(), 
										scoreList, 
										templateRelaDimension.getDimension().getName(), 
										null, 
										null, 
										null,
										jsobjs.getString("rangObjectDeptEmpId"),
										scoreResultAllMap);
						arrays.add(templateList);
						arrayList.add(arrays);
					}
				}
			}
			arrayListCount.put(count, arrayList);
			arrayList = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
			count++;
		}
		
		return arrayListCount;
	}
}