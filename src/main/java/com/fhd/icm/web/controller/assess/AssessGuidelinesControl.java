package com.fhd.icm.web.controller.assess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.icm.business.assess.AssessGuidelinesBO;
import com.fhd.icm.entity.assess.AssessGuidelines;
import com.fhd.icm.entity.assess.AssessGuidelinesProperty;
/**
 * 评价标准模板control
 * @author 邓广义
 *
 */
@Controller
public class AssessGuidelinesControl {
	
	@Autowired
	private AssessGuidelinesBO assessGuidelinesBO;
	/**
	 * 通过ID查询实体
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/findAssessGuidelinesById.f")
	public Map<String,Object> findAssessGuidelinesById(String id){
		 this.assessGuidelinesBO.findAssessGuidelinesById(id);
		return null;
	}
	/**
	 * 获得评价标准模板列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/findAssessGuidelinesBySome.f")
	public List<Map<String,Object>> findAssessGuidelinesBySome(){
		List<AssessGuidelines> list = this.assessGuidelinesBO.findAssessGuidelinesBySome();
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(AssessGuidelines ag : list){
			Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", ag.getName());
				map.put("comment", ag.getComment());
				map.put("sort", ag.getSort());
				map.put("dictype", ag.getType().getId());
				map.put("id", ag.getId());
			listmap.add(map);
		}
		return listmap;
	}
	/**
	 * 根据ID删除实体
	 *  *逻辑删除*
	 * @param id
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/delAssessGuidelinesById.f")
	public Map<String,Object> delAssessGuidelinesById(String id){
		 boolean b = this.assessGuidelinesBO.delAssessGuidelinesById(id);
		 Map<String,Object> map = new HashMap<String,Object>(0);
		 map.put("success", b);
		 return map;
	}
	/**
	 * 保存实体的方法
	 * @param modifyRecords
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/saveAssessGuidelines.f")
	public Map<String,Object> saveAssessGuidelines(String modifyRecords){
		boolean b = this.assessGuidelinesBO.saveAssessGuidelines(modifyRecords);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("success", b);
		return map;
	}
	/**
	 * 通过评价标准模板ID查询该模板对应的评价标准项
	 * @param AssessGuidelinesId
	 * @return List<AssessGuidelinesProperty>
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/findAssessGuidelinesPropertiesByAGId.f")
	public List<Map<String,Object>> findAssessGuidelinesPropertiesByAGId(String assessGuidelinesId){
		List<AssessGuidelinesProperty> list = this.assessGuidelinesBO.findAssessGuidelinesPropertiesByAGId(assessGuidelinesId);
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(AssessGuidelinesProperty agp : list){
			Map<String,Object> map = new HashMap<String,Object>();
				map.put("minValue", agp.getMinValue());
				map.put("maxValue", agp.getMaxValue());
				map.put("judgeValue", agp.getJudgeValue());
				map.put("sort", agp.getSort());
				map.put("content", agp.getContent());
				map.put("dictype", agp.getDefectLevel().getId());
				map.put("parentName", agp.getAssessGuidelines().getName());
				map.put("parentId", agp.getAssessGuidelines().getId());
				map.put("id", agp.getId());
			listmap.add(map);
		}
		return listmap;
	}
	/**
	 * 评价标准项保存实体的方法
	 * @param modifyRecords
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/saveAssessGuidelinesProperty.f")
	public Map<String,Object> saveAssessGuidelinesProperty(String modifyRecords){
		boolean b = this.assessGuidelinesBO.saveAssessGuidelinesProperty(modifyRecords);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("success", b);
		return map;
	}
	/**
	 * 根据ID删除实体(评价标准项删除)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/baseset/delAssessGuidelinesPropertyById.f")
	public Map<String,Object> delAssessGuidelinesPropertyById(String ids){
		boolean b = this.assessGuidelinesBO.delAssessGuidelinesPropertyById(ids);
		Map<String,Object> map = new HashMap<String,Object>(0);
		map.put("success", b);
		return map;
	}
}
