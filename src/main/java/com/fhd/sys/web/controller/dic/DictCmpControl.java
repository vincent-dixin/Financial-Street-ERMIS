/**
 * DictCmpControl.java
 * com.fhd.sys.web.controller.dic
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.sys.business.dic.DictCmpBO;
import com.fhd.sys.web.form.dic.DictEntryForm;

/**
 * ClassName:DictCmpControl
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:43:42
 *
 * @see 	 
 */
@Controller
public class DictCmpControl {
    @Autowired
    private DictCmpBO o_dictCmpBO;
    
    /**
     * 
     * <pre>
     * findDictEntryByTypeId:组件用，取数据字典项
     * </pre>
     * 
     * @author 王 钊
     * @param typeId 字典类型ID
     * @return Map<String,Object>
     * @since  fhd　Ver 1.1
     */
	@ResponseBody
    @RequestMapping(value = "/sys/dic/findDictEntryByTypeId.f")
    public Map<String,Object> findDictEntryByTypeId(String typeId){
		String locale="zh-cn";
		List<DictEntryForm> list=o_dictCmpBO.findDictEntryByTypeId(typeId);
		Map<String,Object> result=new HashMap<String,Object>();
		List<Map<String, String>> l=new ArrayList<Map<String, String>>();
		for(DictEntryForm dictEntryForm:list)
		{
			Map<String,String> map=new HashMap<String,String>();
		    map.put("id", dictEntryForm.getId());
		    String tmpObj=o_dictCmpBO.findDictEntryI18NameBySome(dictEntryForm.getId(), locale);
		    StringBuffer sb = new StringBuffer();
		    for (int i = 1; i < dictEntryForm.getLevel(); i++) {
				sb.append("&nbsp;&nbsp;&nbsp;");
				i++;
			}
		    if(tmpObj !=null ){
		    	map.put("name",sb.append(tmpObj).toString());
		    }else{
		    	map.put("name",sb.append(dictEntryForm.getName()).toString());
		    }
		    map.put("checked", "true");
		    l.add(map);
		}
		result.put("dictEnties", l);
		return result;
    }
	
	
	/**
	 * 通过日期类型、年、季、月查询
	 * 
	 * @author 金鹏祥
	 * @param eType 日期类型
	 * @param eYear年
	 * @param eQuarter 季
	 * @param eMonth 月
	 * @return Map<String,Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping(value = "/sys/dic/findTimePeriodByEtype.f")
    public Map<String,Object> findTimePeriodByEtype(String eType, String eYear, String eQuarter, String eMonthId){
		Map<String,Object> result=new HashMap<String,Object>();
		List<Map<String, Object>> l=new ArrayList<Map<String, Object>>();
		List<TimePeriod> list = o_dictCmpBO.findTimePeriodByEtype(eType, eYear, eQuarter, eMonthId);
		int i = 0;
		for (TimePeriod timePeriod : list) {
			Map<String,Object> map=new HashMap<String,Object>();
			if(timePeriod.getType().equalsIgnoreCase("0frequecy_month")){
				map.put("id", timePeriod.getId() + "---" +timePeriod.getParent().getId());
			}else{
				map.put("id", timePeriod.getId());
			}
			
			map.put("name", timePeriod.getType());
//			map.put("value", timePeriod.getTimePeriodFullName());
			map.put("text", timePeriod.getTimePeriodFullName());
			if(i == 0){
//				map.put("checked", "true");
				map.put("disabled", true);
			}
			i++;
			l.add(map);
		}
		result.put("dictEnties", l);
		System.out.println(result);
		return result;
    }
}