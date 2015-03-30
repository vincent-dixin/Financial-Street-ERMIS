package com.fhd.process.web.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.utils.UserContext;
import com.fhd.process.business.AssessPointBO;
import com.fhd.sys.web.form.dic.DictEntryForm;


/**
 *  评价点信息
 * 
 * @author 宋佳
 * @version
 * @since Ver 1.1
 * @Date 2013  3-13
 */
@Controller
public class AssessPointControl {
@Autowired
private AssessPointBO o_AssessPointBO;
/**
 * 查询出流程中所包含所有的节点，作为数据字典展示
 * @param typeId
 * @author 宋佳
 * @return
 */
@ResponseBody
@RequestMapping("/assess/finddictentrybytypeid.f")
public List<Map<String,String>> findDictEntryByTypeId(String typeId){
	List<DictEntryForm> list=o_AssessPointBO.findDictEntryByTypeId(typeId);
	List<Map<String, String>> pointList=new ArrayList<Map<String, String>>();
	for(DictEntryForm dictEntryForm:list)
	{
		Map<String,String> map=new HashMap<String,String>();
	    map.put("id", dictEntryForm.getId());
	    map.put("name",dictEntryForm.getName());
	    pointList.add(map);
	 }
	return pointList;
}

/**
 * 根据节点ID 找到上级节点和节点进入条件
 * @author 宋佳
 * @param processId
 * @return
 */
@ResponseBody
@RequestMapping("/assess/findassesspointlistbysome.f")
public Map<String,Object> findAssessPointListBySome(String processPointId,String measureId,String processId,String type){
	Map<String,Object> resultMap = new HashMap<String,Object>();
	String companyId = UserContext.getUser().getCompanyid();
	resultMap=o_AssessPointBO.findAssessPointListBySome(processPointId,measureId,type,processId,companyId);
	return resultMap;
}

/**
 * <pre>
 *    删除节点对应评价点
 * </pre>
 * 
 * @author  宋佳
 * @param ProcessPointID
 * @since  fhd　Ver 1.1
 */
@ResponseBody
@RequestMapping("/assesspoint/removeassesspointbyid.f")
public Map<String,Object> removeParentPointById(String ids) throws IOException{
	if(StringUtils.isNotBlank(ids)){
		o_AssessPointBO.removeAssessPointByID(ids);
	}
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("data", "sucess");
	return result;
}
}
