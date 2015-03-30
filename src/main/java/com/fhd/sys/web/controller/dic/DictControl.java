/**
 * DictControl.java
 * com.fhd.sys.web.controller.dic
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.dic;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictType;
import com.fhd.sys.web.form.dic.DictEntryForm;

/**
 * ClassName:DictControl 
 * @author 张 雷
 * @version
 * @since Ver 1.1
 * @Date 2012-9-18 下午3:45:25
 * 
 * @see
 */
@Controller
public class DictControl {
    @Autowired
    private DictBO o_dictEntryBO;
    
    /**
     * 
     * <pre>
     * saveDictEntry:新增DictEntry
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param data DictEntry类型的表单对象
     * @return boolean
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/saveDictEntry.f")
    public boolean saveDictEntry(HttpServletRequest request, DictEntryForm data) {
		DictEntry entry = new DictEntry();
		DictEntry parentEntry = null;
		if (data.getParent() != null) {
		    parentEntry = o_dictEntryBO.findDictEntryById(data.getParent().getId());
		}
		entry.setId(data.getNum());
		entry.setName(data.getName());
		entry.setValue(data.getValue());
		entry.setEdesc(data.getEdesc());
		entry.setParent(parentEntry);
		DictType dictType = o_dictEntryBO.findDictTypeByTypeId(data.getDictTypeId());
		entry.setDictType(dictType);
		o_dictEntryBO.saveDictEntry(entry);
		
//		if("st_temp_category".equalsIgnoreCase(dictType.getId())){
//			//保存模版
//			Temp entity = new Temp(Identities.uuid());
//			entity.setDictEntry(entry);
//			entity.setName("");
//			entity.setContent("");
//			entity.setStatus("");
//			o_tempBO.saveTemp(entity);
//		}
		
		return true;
    }

    /**
     * 
     * <pre>
     * removeDictEntryByNum:逻辑删除DictEntry
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param ids 数据项ID，多个ID用“,”分开
     * @return boolean
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/removeDictEntry.f")
    public boolean removeDictEntryByNum(HttpServletRequest request, String ids) {
		if (StringUtils.isNotBlank(ids)) {
//			HashMap<String, DictEntry> dictEntryMap = o_dictEntryBO.findDictEntryByDictTypeIdMap();
//			HashMap<String, Temp> tempMap = o_tempBO.findTempAllMap();
//			HashMap<String, Plan> planMap = o_planBO.findPlanAllMap();
//			String[] idArray = ids.split(",");
		    o_dictEntryBO.removeDictEntry(ids);
//		    
//			for (String id : idArray) {
//			    DictEntry dictEntry = dictEntryMap.get(id);
//			    if(dictEntry != null){
//			    	o_tempBO.deleteTemp(tempMap, ids);
//			    }
//			}
		    return true;
		} else {
		    return true;
		}
    }

    /**
     * 
     * <pre>
     * delI18:删除国际化内容
     * </pre>
     * 
     * @author 王 钊
     * @param ids 国际化ID，多个ID用','隔开
     * @return
     * @since  fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dict/removeDictEntryI18.f")
    public boolean removeDictEntryI18(String ids) {
		o_dictEntryBO.removeDictEntryI18(ids);
		return true;
    }
    
    /**
     * 
     * <pre>
     * mergeDictEntry:更新updateDictEntry
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param data DictEntryForm类型的表单对象
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/mergeDictEntry.f")
    public boolean mergeDictEntry(HttpServletRequest request, DictEntryForm data) {
		DictEntry dictEntry = o_dictEntryBO.findDictEntryById(data.getId());
		DictEntry parentEntry = null;
		if (data.getParent() != null) {
		    parentEntry = o_dictEntryBO.findDictEntryById(data.getParent().getId());
		}
	
		dictEntry.setId(data.getNum());
		dictEntry.setName(data.getName());
		dictEntry.setValue(data.getValue());
		dictEntry.setParent(parentEntry);
		dictEntry.setEdesc(data.getEdesc());
		o_dictEntryBO.mergeDictEntry(dictEntry);
		return true;
    }

    /**
     * 
     * <pre>
     * mergeDictEntryI18:更新国际化信息
     * </pre>
     * 
     * @author 王 钊
     * @param modifiedRecord json数组
     * @param entryId	字典项ID
     * @param response
     * @since  fhd　Ver 1.1
     */
    @RequestMapping(value = "/sys/dict/mergeDictEntryI18.f")
    public void mergeDictEntryI18(String modifiedRecord, String entryId, HttpServletResponse response) {
		PrintWriter out = null;
		String flag = "false";
		try {
		    out = response.getWriter();
		    o_dictEntryBO.mergeDictEntryI18Batch(modifiedRecord, entryId);
		    flag = "true";
		    out.write(flag);
		} catch (Exception e) {
		    e.printStackTrace();
		    out.write(flag);
		} finally {
		    out.close();
		}
    }

    /**
     * 
     * <pre>
     * findDictEntryByNum:取单个dictentry信息
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param num 字典项编号
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/findDictEntryByNum.f")
    public Map<String, Object> findDictEntryByNum(HttpServletRequest request, String num) {
		Map<String, Object> map = new HashMap<String, Object>();
		DictEntry dictEntry = o_dictEntryBO.findDictEntryById(num);
		Map<String, String> inmap = new HashMap<String, String>();
		inmap.put("num", dictEntry.getId());
		inmap.put("id", dictEntry.getId());
		inmap.put("name", dictEntry.getName());
		inmap.put("value", dictEntry.getValue());
		if (dictEntry.getParent() != null) {
		    inmap.put("parent", dictEntry.getParent().getId());
		} else {
		    inmap.put("parent", null);
	
		}
		inmap.put("edesc", dictEntry.getEdesc());
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }

    /**
     * 
     * <pre>
     * checkDictEntryByNum:检查DictEntry编号重复
     * </pre>
     * 
     * @author 王 钊
     * @param num 字典项编号
     * @param orgnum 如果是更新状态，原字典项编号
     * @return boolean
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/checkDictEntryByNum.f")
    public boolean checkDictEntryByNum(String num, String orgnum, String mode) {
		if(StringUtils.isNotBlank(mode)){
			if (mode.equals("false")) {
			    if (num.equals(orgnum)) {
				return true;
			    }
			}
		}
		return o_dictEntryBO.findDictEntryById(num) == null;
    }

    /**
     * 
     * <pre>
     * findDictEntryI18ByEntryId:取国际化信息
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param entryId 字典项id
     * @param sort 排序字段的json数组由前台控件生产
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/findDictEntryI18ByEntryId.f")
    public Map<String, Object> findDictEntryI18ByEntryId(
	    HttpServletRequest request, String entryId, String sort) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datas", o_dictEntryBO.findDictEntryI18(entryId, sort));
		map.put("success", true);
		return map;
    }
    
    @ResponseBody
    @RequestMapping(value = "/sys/dic/listMap.f")
    public List<Map<String,Object>> listMap(String parentId, String dictTypeId,Boolean isLeaf){
    	return o_dictEntryBO.listMap(parentId, dictTypeId, isLeaf);
    }
}