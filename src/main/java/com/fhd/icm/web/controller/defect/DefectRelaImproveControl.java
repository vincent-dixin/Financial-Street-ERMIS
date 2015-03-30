package com.fhd.icm.web.controller.defect;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.assess.AssessRelaDefectBO;
import com.fhd.icm.business.defect.DefectRelaImproveBO;
import com.fhd.icm.business.rectify.ImproveBO;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.rectify.Improve;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-2		下午7:39:20
 *
 * @see 	 
 */
@Controller
public class DefectRelaImproveControl {
	@Autowired
	private DefectRelaImproveBO o_defectRelaImproveBO;
	@Autowired
	private AssessRelaDefectBO o_assessRelaDefectBO;
	@Autowired
	private ImproveBO o_improveBO;
	/**
	 * <pre>
	 * 批量保存整改关联的缺陷信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param assessPlanIds 评价计划的ID串，多个以逗号“,”隔开
	 * @param defectIds 缺陷的ID串，多个以逗号“,”隔开
	 * @param improveId 整改计划ID
	 * @param request
	 * @return
	 * @throws ParseException
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping("/defect/mergedefectrelaimprovebatch.f")
    public Map<String, Object> mergeDefectRelaImproveBatch(String assessPlanIds, String defectIds, String improveId, HttpServletRequest request) throws ParseException{
        Map<String, Object> result = new HashMap<String, Object>();
        Improve improve = o_improveBO.findImproveById(improveId);
        String[] defectIdArray = null;
        if(StringUtils.isNotBlank(defectIds)&& null != improve){
        	defectIdArray = defectIds.split(",");
        	for (String defectId : defectIdArray) {
            	List<DefectRelaImprove> defectRelaImproveList = o_defectRelaImproveBO.findDefectRelaImproveListBySome(defectId, improveId);
            	if(null !=defectRelaImproveList && defectRelaImproveList.size()>0){//如果存在则不重复添加
            		continue;
            	}
    			Defect defect = new Defect(defectId);
    			DefectRelaImprove defectRelaImprove = new DefectRelaImprove(Identities.uuid2());
    			defectRelaImprove.setDefect(defect);
    			defectRelaImprove.setImprove(improve);
    			o_defectRelaImproveBO.mergeDefectRelaImprove(defectRelaImprove);
    		}
        }else if (StringUtils.isNotBlank(assessPlanIds)&& null != improve){
        	String[] assessPlanIdArray = assessPlanIds.split(",");
        	List<String> assessPlanIdList = new ArrayList<String>();
        	for (String assessPlanId : assessPlanIdArray) {
        		assessPlanIdList.add(assessPlanId);
			}
        	List<AssessRelaDefect> assessRelaDefectList = o_assessRelaDefectBO.findAssessRelaDefectListBySome(assessPlanIdList);
        	Set<String> defectIdSet = new HashSet<String>();
        	for (AssessRelaDefect assessRelaDefect : assessRelaDefectList) {
        		defectIdSet.add(assessRelaDefect.getDefect().getId());
			}
        	for (String defectId : defectIdSet) {
            	List<DefectRelaImprove> defectRelaImproveList = o_defectRelaImproveBO.findDefectRelaImproveListBySome(defectId, improveId);
            	if(null !=defectRelaImproveList && defectRelaImproveList.size()>0){//如果存在则不重复添加
            		continue;
            	}
    			Defect defect = new Defect(defectId);
    			DefectRelaImprove defectRelaImprove = new DefectRelaImprove(Identities.uuid2());
    			defectRelaImprove.setDefect(defect);
    			defectRelaImprove.setImprove(improve);
    			o_defectRelaImproveBO.mergeDefectRelaImprove(defectRelaImprove);
    		}
        }
        result.put("success", true);
        return result;
    }
	
    /**
     * <pre>
     * 批量删除整改涉及的缺陷
     * </pre>
     * 
     * @author 张 雷
     * @param defectRelaImproveIds 整改涉及的缺陷ID串，多个以逗号“,”隔开
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/defect/removedefectrelaimprovebatch.f")
    public Map<String, Object> removeDefectRelaImproveBatch(String defectRelaImproveIds){
        boolean result = true;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] defectRelaImproveIdArray = defectRelaImproveIds.split(",");
        for (String defectRelaImproveId : defectRelaImproveIdArray) {
        	o_defectRelaImproveBO.removeDefectRelaImprove(defectRelaImproveId);
		}
        resultMap.put("success", true);
        resultMap.put("result", result);
        return resultMap;
    }
	
	/**
	 * <pre>
	 * 查询整改关联的缺陷的列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 分页容量
	 * @param start 起始值
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/defect/findrectifydefectbyimproveid.f")
	public Map<String, Object> findRectifyDefectByImproveId(int limit, int start,String improveId) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Page<DefectRelaImprove> page = new Page<DefectRelaImprove>();
		limit = 9999;
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_defectRelaImproveBO.findDefectRelaImprovePageBySome(page, improveId);
		List<DefectRelaImprove> defectRelaImproveList = page.getResult();
		for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
			Defect defect = defectRelaImprove.getDefect();
			Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
			defectRelaImproveMap.put("id", defectRelaImprove.getId());
			defectRelaImproveMap.put("desc", defect.getDesc());
			Set<DefectRelaOrg> defectRelaOrgSet = defect.getDefectRelaOrg();
			for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
				if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
					defectRelaImproveMap.put("orgName", defectRelaOrg.getOrg().getOrgname());
					break;
				}
			}
			defectRelaImproveMap.put("level", null != defect.getLevel()?defect.getLevel().getName():"");
			defectRelaImproveMap.put("type", null != defect.getType()?defect.getType().getName():"");
			resultList.add(defectRelaImproveMap);
		}
		
		//排序
		Collections.sort(resultList, new Comparator<Map<String,Object>>(){
			@Override 
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) { 
				int flag = arg0.get("id").toString().compareTo(arg1.get("id").toString());
				if(null != arg0.get("orgName") && null != arg1.get("orgName")){
					flag = arg0.get("orgName").toString().compareTo(arg1.get("orgName").toString());
					if(flag==0 && null != arg0.get("desc") && null != arg1.get("desc")){
						return arg0.get("desc").toString().compareTo(arg1.get("desc").toString()); 
					}else{
						return flag;
					}
				}else{
					return flag;
				}
				
		 	}
		});
		
		resultMap.put("datas", resultList);
		resultMap.put("totalCount", resultList.size());
		return resultMap;
	}
	
}

