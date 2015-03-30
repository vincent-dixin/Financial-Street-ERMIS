/**
 * DictTreeControl.java
 * com.fhd.sys.web.controller.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.dic;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.dic.DictTreeBO;


/**
 * ClassName:DictTreeControl
 * 
 * @author 张 雷
 * @version
 * @since Ver 1.1
 * @Date 2012-9-18 下午3:45:50
 * 
 * @see
 */
@Controller
public class DictTreeControl {
    @Autowired
    private DictTreeBO o_dictTreeBO;

    /**
     * 
     * <pre>
     * findDictTypeBySome:查询字典类型
     * </pre>
     * 
     * @author 王 钊
     * @param query 查询条件
     * @param typeId 字典类型id
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/findDictTypeBySome.f")
    public Map<String, Object> findDictTypeBySome(String query, String typeId) {
    	return o_dictTreeBO.findDictTypesTree(query, typeId);
    }

    /**
     * 
     * <pre>
     * findDictEntryBySome:查询字典项
     * </pre>
     * 
     * @author 王 钊
     * @param request
     * @param typeId 字典类型ID
     * @param entryId 字典项ID，查询时排除此对象及子对象
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/dic/findDictEntryBySome.f")
    public Map<String, Object> findDictEntryBySome(HttpServletRequest request, String typeId,String entryId, String sort) {
		String query = request.getParameter("query");
		
		String property = "";
		String direction = "";
		if (StringUtils.isNotBlank(sort)){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                if(property.equalsIgnoreCase("num")){
                	property = "id";
                }
                direction = jsobj.getString("direction");
            }
        }
		
		Map<String, Object> map = o_dictTreeBO.findDictEntryTree(query, typeId, entryId, property, direction);
		return map;
    }
}