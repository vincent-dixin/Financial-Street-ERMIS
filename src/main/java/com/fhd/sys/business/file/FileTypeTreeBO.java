package com.fhd.sys.business.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.business.dic.OldDictEntryBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 
 * ClassName:KpiStrategyMapTreeBO:战略目标树BO
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-8-29		上午10:11:00
 *
 * @see
 */
@Service
public class FileTypeTreeBO {
	
	@Autowired
	private OldDictEntryBO o_dictEntryBO;
	/**
	 * 
	 * treeLoader:读取树节点
	 * 
	 * @author 杨鹏
	 * @param id
	 * @param canChecked
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> treeLoader(String id,Boolean canChecked){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		List<DictEntry> list = o_dictEntryBO.findBySome(id, "0file_type",null);
		for (DictEntry dictEntry : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dictEntry.getId());
			map.put("dbid", dictEntry.getId());
			map.put("text", dictEntry.getName());
			map.put("value", dictEntry.getValue());
			if(dictEntry.getIsLeaf()){
				map.put("iconCls", "icon-page-gear");
			}
			map.put("leaf", dictEntry.getIsLeaf());
			if(canChecked){
				map.put("checked", false);
			}
			nodes.add(map);
		}
		return nodes;
	}
}