package com.fhd.icm.business.defect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.dao.defect.DefectDAO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.entity.dic.DictEntry;

@Service
@SuppressWarnings("unchecked")
public class DefectTreeBO{
	@Autowired
	private DefectDAO o_defectDAO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	public List<Map<String, Object>> defectTreeLoader(String id, Boolean canChecked, String query, String type) {
		Map<String, Object> node = null; // node节点临时对象
		
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();// 返回前台的nodelist
			Criteria criteria = o_defectDAO.createCriteria();
			criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
			criteria.setCacheable(true);
			List<Defect> defectList = criteria.list();
		
			for (Defect entity : defectList) {
				node = new HashMap<String, Object>();	
				if(canChecked){
					 node.put("checked", false);		
				}
							
				node.put("id",entity.getId());
				node.put("dbid",entity.getId());
				node.put("leaf",true);
				node.put("text",entity.getDesc());
				nodes.add(node);
			}
		return nodes;
	}

	public List<Map<String, Object>> defectLevelTreeLoader(String id, Boolean canChecked, String query, String type) {
		Map<String, Object> node = null; // node节点临时对象
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
			Criteria criteria = o_dictEntryDAO.createCriteria();
			criteria.add(Restrictions.eq("dictType.id", "ca_defect_level"));
			criteria.setCacheable(true);
			List<DictEntry> dictEntryList = criteria.list();
		
			for (DictEntry entity : dictEntryList) {
				node = new HashMap<String, Object>();	
				/*if(canChecked){
					 node.put("checked", false);		
				}*/
							
		        node.put("id",entity.getId());
		        node.put("dbid",entity.getId());
		        node.put("leaf",true);
		        node.put("text",entity.getName());
		        nodes.add(node);
			}
		return nodes;
	}
}