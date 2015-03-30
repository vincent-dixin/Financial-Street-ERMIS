/**
 * TempControl.java
 * com.fhd.sys.web.controller.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.st;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.utils.Identities;
import com.fhd.sys.business.dic.DictTreeBO;
import com.fhd.sys.business.st.TempBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.st.Temp;
import com.fhd.sys.entity.st.TempParameter;
import com.fhd.sys.web.form.st.TempForm;
import com.fhd.sys.web.form.st.TempParameterForm;

/**
 * 计划任务模版控制分发
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-11		上午09:24:04
 *
 * @see 	 
 */
@Controller
public class TempControl {
	@Autowired
    private DictTreeBO o_dictTreeBO;
	
	@Autowired
	private TempBO o_tempBO;
	
	/**
     * 保存、更新模版
     * 
     * @author 金鹏祥
     * @param data form实体
     * @param response
     * @param dictEntryId 字典项ID
     * @param contentEdit 内容
     * @throws Exception
     * @since  fhd　Ver 1.1
    */
    @RequestMapping(value = "/sys/st/saveTemp.f")
	public void saveTemp(TempForm data,HttpServletResponse response ,
			String dictEntryId, String contentEdit) throws Exception {
		PrintWriter out = response.getWriter();
		Temp entity = null;
		String flag = "false";
		try {
			DictEntry dictEntry = o_tempBO.findDictEntryById(dictEntryId);
			if(StringUtils.isNotBlank(data.getId())){
				//更新
				entity = new Temp();
				BeanUtils.copyProperties(data, entity, new String[]{});
				entity.setDictEntry(dictEntry);
				if(StringUtils.isNotBlank(contentEdit)){
					entity.setContent(contentEdit);
				}
				o_tempBO.mergeTemp(entity);
			}else{
				//保存
				entity = new Temp(Identities.uuid());
				BeanUtils.copyProperties(data, entity, new String[]{"id"});
				entity.setDictEntry(dictEntry);
				if(StringUtils.isNotBlank(contentEdit)){
					entity.setContent(contentEdit);
				}
				o_tempBO.saveTemp(entity);
			}
			flag = "true";
			out.write(flag);
		} finally {
			out.close();
		}
	}
	
	/**
     * 
     * 查询字典项
     * 
     * @author 金鹏祥
     * @param request
     * @param dictEntryId 字典项ID
     * @return Map
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/st/findDictEntryBySome.f")
    public Map<String, Object> findDictEntryBySome(HttpServletRequest request, String query, String dictEntryId) {
    	if(dictEntryId.indexOf(",") != -1){
    		dictEntryId = dictEntryId.replace(",", "");
    	}
		Map<String, Object> map = o_dictTreeBO.findDictEntryTree(query, dictEntryId);
		return map;
    }
    
    /**
     * 
     * 字典项对应参数
     * 
     * @author 金鹏祥
     * @param request
     * @return Map
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/st/findDictEntryByType.f")
    public Map<String, Object> findDictEntryByType(HttpServletRequest request) {
    	List<TempParameter> ParameterList = new ArrayList<TempParameter>();
    	List<TempParameterForm> datas = new ArrayList<TempParameterForm>();
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	TempParameter parameter = null;
    	TempParameterForm tempParameterForm = null;
    	String parameterStr[] = {"$user$", "$riskName$", "$processName$", "$createTime$", "$updateTime$" ,"$title$"};
    	String parameteStr[] = {"用户名", "风险名称", "流程名称", "创建时间", "修改时间", "标题"};
    	
    	for (int i = 0; i < parameterStr.length; i++) {
    		parameter = new TempParameter();
    		parameter.setParameter(parameterStr[i]);
    		parameter.setDescribe(parameteStr[i]);
    		ParameterList.add(parameter);
		}
    	
		for(TempParameter de : ParameterList){
			tempParameterForm = new TempParameterForm();
			BeanUtils.copyProperties(de, tempParameterForm, new String[]{});
			datas.add(tempParameterForm);
		}
		
		map.put("totalCount", ParameterList.size());
		map.put("datas", datas);
		return map;
    }
    
    /**
     * 
     * 字典项对应Temp实体
     * 
     * @author 金鹏祥
     * @param request
     * @param dictEntryId 字典项ID
     * @return Map
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping(value = "/sys/st/findTempByCategory.f")
    public Map<String, Object> findTempByCategory(HttpServletRequest request, String dictEntryId) {		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> inmap = new HashMap<String, String>();
		
		Temp temp = o_tempBO.findTempByCategory(dictEntryId);
		if(temp!=null){
			inmap.put("id", temp.getId());
			inmap.put("name", temp.getName());
			inmap.put("content", temp.getContent());
			inmap.put("dictEntryId", dictEntryId);
		}else{
			inmap.put("id", "");
			inmap.put("name", "");
			inmap.put("content", "");
			inmap.put("dictEntryId", dictEntryId);
		}
		map.put("data", inmap);
		map.put("success", true);
		return map;
    }
}