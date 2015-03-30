package com.fhd.risk.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.risk.entity.TemplateRelaDimension;
import com.fhd.risk.interfaces.ITemplateTreeBO;

/**
 * 模板及模板相关信息的维度树的加载功能
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-15		下午2:38:15
 *
 * @see 	 
 */
@Service
public class TemplateTreeBO implements ITemplateTreeBO{
	
	@Autowired
	private TemplateBO o_templateBO;
	/**
	 * <pre>
	 * 递归调用加载TemplateRelaDimensionTree数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param templateRelaDimensionList 要加载的集合
	 * @param parent 父对象
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	private Map<String,Object> templateRelaDimensionTreeLoader(
			List<TemplateRelaDimension> templateRelaDimensionList,TemplateRelaDimension parent,String query){
		Map<String,Object> item = new TreeMap<String,Object>();
		List<TemplateRelaDimension> subList = new ArrayList<TemplateRelaDimension>();
		Set<String> idSet = o_templateBO.findTemplateRelaDimensionIdSetByQuery(query);
		if(null != templateRelaDimensionList && templateRelaDimensionList.size()>0){
			for (TemplateRelaDimension templateRelaDimension : templateRelaDimensionList) {
				if(null == parent){
            		if(null == templateRelaDimension.getParent()){
            			if(idSet.contains(templateRelaDimension.getId()))
            			{
            			subList.add(templateRelaDimension);
            			}
            		}
            	}else{
            		if(null != templateRelaDimension.getParent() && templateRelaDimension.getParent().getId().equals(parent.getId())){
            			if(idSet.contains(templateRelaDimension.getId()))
            			{
            			subList.add(templateRelaDimension);
            			}
                	}
            	}
			}
		}
		if(null != parent){
			item.put("id", parent.getId());
			item.put("name", parent.getDimension().getName());
			item.put("isCalculate",parent.getIsCalculate());
			if(StringUtils.isNotBlank(parent.getDesc())){
				item.put("desc",parent.getDesc());
			}else{
				item.put("desc","");
			}
			
			if(null != parent.getCalculateMethod()){
				item.put("calculateMethodId",parent.getCalculateMethod().getId());
			}else{
				item.put("calculateMethodId","");
			}
			
			item.put("level",parent.getLevel());
			if(subList.size()>0){
				item.put("expanded", true);
				item.put("leaf", false);
				item.put("iconCls", "icon-scorecards");
			}
		}
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		for (TemplateRelaDimension templateRelaDimension : subList) {
			Map<String,Object> subItem = new TreeMap<String,Object>();
			subItem.put("id", templateRelaDimension.getId());
			subItem.put("name", templateRelaDimension.getDimension().getName());
			if(StringUtils.isNotBlank(templateRelaDimension.getDesc())){
				subItem.put("desc",templateRelaDimension.getDesc());
			}else{
				subItem.put("desc","");
			}
			
			subItem.put("isCalculate",templateRelaDimension.getIsCalculate());
			if(null != templateRelaDimension.getCalculateMethod()){
				subItem.put("calculateMethodId",templateRelaDimension.getCalculateMethod().getId());
			}else{
				subItem.put("calculateMethodId","");
			}
			subItem.put("level",templateRelaDimension.getLevel());
			
			if(null != templateRelaDimension.getChildren() && templateRelaDimension.getChildren().size()>0){
				Map<String,Object> map = templateRelaDimensionTreeLoader(new ArrayList<TemplateRelaDimension>(templateRelaDimension.getChildren()),templateRelaDimension,query);
				if(map!=null)
				{
					children.add(map);
				}
			}else{
				subItem.put("leaf", true);
				subItem.put("iconCls", "icon-scorecard");
				children.add(subItem);
			}
		}
		if(children.size()>0)
			item.put("children", children);
		return item;
	}
	
	public Map<String,Object> templateRelaDimensionTreeLoader(String query, String templateId){
		List<TemplateRelaDimension> templateRelaDimensionList = o_templateBO.findTemplateRelaDimensionBySome(query, templateId);
		Map<String,Object> map = templateRelaDimensionTreeLoader(templateRelaDimensionList,null,query);
		return map;
	}
	
}

