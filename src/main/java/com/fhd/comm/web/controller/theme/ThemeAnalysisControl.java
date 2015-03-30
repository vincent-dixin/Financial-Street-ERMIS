package com.fhd.comm.web.controller.theme;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.theme.AnalysisBO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.comm.entity.theme.LayoutDetailedSet;
import com.fhd.comm.entity.theme.LayoutDetailedSource;
import com.fhd.comm.entity.theme.LayoutInfo;
import com.fhd.comm.entity.theme.LayoutPosition;
import com.fhd.comm.entity.theme.LayoutRelaDetailedSet;
import com.fhd.comm.entity.theme.LayoutRelaType;
import com.fhd.comm.entity.theme.LayoutType;
import com.fhd.comm.entity.theme.LayoutTypeRelaPosition;
import com.fhd.comm.web.form.theme.ThemeLayoutDetailedForm;
import com.fhd.comm.web.form.theme.ThemeLayoutForm;
import com.fhd.comm.web.form.theme.ThemeRecordForm;
import com.fhd.core.dao.Page;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.dynamictable.Doughnut3D;
import com.fhd.kpi.business.dynamictable.MSColumnLine3D;
import com.fhd.kpi.business.dynamictable.MSLine;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.RelaAssessResult;

/**
 * 主题分析Controller
 * 
 *
 * @author   郝静
 * @version  
 * @Date     2013   2013-4-17       下午13:11:34
 *
 * @see
 */

@Controller
public class ThemeAnalysisControl {
	
	@Autowired
	private AnalysisBO o_analysisBO;
    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;
    @Autowired
    private KpiBO o_kpiBO;
    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;
	  /**
     * <pre>
     * 校验布局信息
     * </pre>
     * 
     * @author 郝静
     * @param validateItem 布局校验信息
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/ic/theme/validate.f")
    public Map<String, Object> validate(String validateItem, String id) {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(validateItem);
        String name = jsobj.getString("name");

        if (o_analysisBO.findCountByName(name,id) > 0) {
            successFlag = false;
            result.put("error", "nameRepeat");
            return result;
        }
        result.put("success", successFlag);
        return result;
    }
    
    
    
	/**
     * <pre>
     * 保存布局信息(第一步)
     * </pre>
     * 
     * @author 郝静
     * @return
     * @since  fhd　Ver 1.1
    */
	@ResponseBody
	@RequestMapping(value = "/ic/theme/savelayoutinfo.f")
	public Map<String, Object> saveAnalysisInfo(ThemeLayoutForm themeLayoutForm, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		LayoutInfo layoutInfo = new LayoutInfo();
		
		layoutInfo.setLayoutName(themeLayoutForm.getLayoutName());
		String  id = themeLayoutForm.getLayoutId();
		
		if(StringUtils.isNotBlank(id)&&!"undefined,".equals(id)){
			//更新
			String ids[] = id.split(",");
			layoutInfo.setId(ids[0]);
			layoutInfo = o_analysisBO.findLayoutInfoByLayoutId(layoutInfo.getId());
			String layoutInfoId = o_analysisBO.mergeLayoutInfo(themeLayoutForm,layoutInfo);
			result.put("id", layoutInfoId);
	        result.put("success", "true");
		}else {
			String layoutInfoId = o_analysisBO.saveLayoutInfo(themeLayoutForm);
			result.put("id", layoutInfoId);
	        result.put("success", "true");
		}
		return result;
	}

	
	/**
     * <pre>
     * 保存布局详细信息(第二步)
     * </pre>
     * 
     * @author 郝静
     * @return
     * @since  fhd　Ver 1.1
    */
	@ResponseBody
	@RequestMapping(value = "/ic/theme/savelayoutdetailedset.f")
	public void saveAnalysisDetailInfo(ThemeLayoutDetailedForm themeLayoutDetailedForm, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String isSave = "false";
		
		try{
			String  id = themeLayoutDetailedForm.getLayoutInfoId();
			if(StringUtils.isNotBlank(id)&&!"undefined,".equals(id)){
				//更新
				String ids[] = id.split(",");
				themeLayoutDetailedForm.setLayoutInfoId(ids[0]);
//				o_analysisBO.removeLayInfoDetailsByid(id);
				o_analysisBO.mergeLayoutDetailedInfo(themeLayoutDetailedForm);
				isSave = "true";
			}else{
				o_analysisBO.saveLayoutDetailedInfo(themeLayoutDetailedForm);
				isSave = "true";
			}
			out.write(isSave);
		}finally {
			out.close();
		}
	}
	
	  /**
     * <pre>
     * 根据布局信息ID查找布局对象
     * </pre>
     * 
     * @param id layoutInfoId
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/ic/theme/findLayoutInfoByIdToJson.f")
    public Map<String, Object> findLayoutInfoByIdToJson(String id) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, String> jsonMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
        	LayoutInfo layoutInfo = o_analysisBO.findLayoutInfoByLayoutId(id);
        	LayoutRelaType layoutRelaType = o_analysisBO.findLayoutRelaTypeByLayoutId(id);
        	LayoutType layoutType = o_analysisBO.findLayoutTypeByLayoutTypeId(layoutRelaType.getLayoutType().getId());
            jsonMap.put("layoutName", layoutInfo.getLayoutName());
            jsonMap.put("layoutType", layoutType.getLayoutType());
            dataMap.put("data", jsonMap);
            dataMap.put("success", true);
        }

        return dataMap;
    }
    
    
	  /**
     * <pre>
     * 根据布局信息ID查找布局对象
     * </pre>
     * 
     * @param id layoutInfoId
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/ic/theme/findLayoutDetailedByIdToJson.f")
    public Map<String, Object> findLayoutDetailedByIdToJson(String id,String condItem, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> jsonMap = null;
        List <Map<String, String>> detailedList = new ArrayList<Map<String, String>>();
        
        List<KpiGatherResult> kpiGatherResultList = null;
        String doughnut3D = "";
        
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
        	List<LayoutRelaDetailedSet> list = o_analysisBO.findLayoutRelaDetailedSetByLayoutInfoId(id);
        	if(list.size()>0){
        		for(LayoutRelaDetailedSet obj:list){
        			jsonMap = new HashMap<String, String>();
        		 	LayoutDetailedSet layoutDetail = o_analysisBO.findLayoutDetailedSetByLayoutDetailedSetId(obj.getLayoutDetailedId().getId());
                	LayoutTypeRelaPosition ltrp = o_analysisBO.findLayoutTypeRelaPositionById(obj.getLayoutTypeRelaPosition().getId());
                	LayoutPosition layoutPosition = o_analysisBO.findLayoutPositionById(ltrp.getLayoutPosition().getId());
                	
                	List<LayoutDetailedSource> dlist = o_analysisBO.findLayoutDetailedSourceByDataSourceId(layoutDetail.getDataSource());
                	if(dlist.size()>=1){
                		Map<String,List<KpiGatherResult>> kpis = new HashMap<String,List<KpiGatherResult>>();
                		String kpiids = "";
                		String dataSourceName = "";
                		String xmls ="";
                		if(layoutDetail.getChartType().equals("1")){
                			for(LayoutDetailedSource lds : dlist){
                    			kpiGatherResultList  = o_kpiGatherResultBO.findKpiGatherResultListByKpiId(lds.getObjectId());
                  	        	Kpi kpi = this.o_kpiBO.findKpiById(lds.getObjectId());
                  	        	kpiids += "'"+lds.getObjectId()+"'"+",";
                  	        	kpis.put(kpi.getName(),kpiGatherResultList);
                  	        	dataSourceName = kpi.getName();
                  	        	xmls+=this.getDoughnut3D(condItem,lds.getObjectId());
                    		}
          	        		doughnut3D = Doughnut3D.getAllXml(xmls);
          	        		jsonMap.put("xmlMap", doughnut3D);
          	        		jsonMap.put("positionName", layoutPosition.getName());
                        	jsonMap.put("chartType", "Doughnut3D");
                        	jsonMap.put("dataSource", kpiids.replace("'", ""));
                        	jsonMap.put("dataSourceName",dataSourceName);
                            detailedList.add(jsonMap);
          	        	}else if(layoutDetail.getChartType().equals("0")){
          	        		String kpiid = dlist.get(0).getObjectId();
          	        		Kpi kpi = this.o_kpiBO.findKpiById(kpiid);
          	        		result = this.getMSColumnLine3D(condItem,kpiid);
              	        	jsonMap.put("xmlMap", result.get("xmlMap").toString());
                        	jsonMap.put("positionName", layoutPosition.getName());
                    		jsonMap.put("chartType", "MSColumnLine3D");
                    		jsonMap.put("dataSource", kpiid);
                    		jsonMap.put("dataSourceName",kpi.getName());
                    		detailedList.add(jsonMap);
                        }else if(layoutDetail.getChartType().equals("2")){
                        	String kpiid = dlist.get(0).getObjectId();
                        	RelaAssessResult ras = o_relaAssessResultBO.findRelaAssessResultByObjectId(kpiid);
                    		if(ras!=null){
                    			result = this.getMSLine(condItem,kpiid);
                  	        	jsonMap.put("xmlMap", result.get("xmlMap").toString());
                            	jsonMap.put("positionName", layoutPosition.getName());
                        		jsonMap.put("chartType", "MSLine");
                        		jsonMap.put("dataSource", kpiid);
                    			jsonMap.put("dataSourceName",ras.getObjectName());
                    		}
                    		detailedList.add(jsonMap);
                        }
                	}
      	        	
        		}
              
        	}
        	 dataMap.put("data", detailedList);
             dataMap.put("success", true);
        }

        return dataMap;
    }
    
	
 /**
	 * 查询列表分页
	 * @param start
	 * @param limit
	 * @param query
	 * @param sort
	 * @param themeId	主题id（树节点）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ic/theme/querylayinfoandlaytypepage.f")
	public Map<String, Object> queryLayInfoAndLayTypePage(int start, int limit, String query, String sort,String themeId){
		String property = "";
		String direction = "";
		Page<LayoutInfo> page = new Page<LayoutInfo>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		if (StringUtils.isNotBlank(sort)){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                direction = jsobj.getString("direction");
                
                if(property.equalsIgnoreCase("zq")){
                	property = "isRecycle";
    			}else if(property.equalsIgnoreCase("statusName")){
    				property = "status";
    			}else if(property.equalsIgnoreCase("triggerName")){
    				property = "triggerType";
    			}
            }
        }else{
        	property = "layoutName";
        	direction = "ASC";
        }
		page = o_analysisBO.findLayInfoGridBySomes(query, page, property, direction, themeId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<LayoutInfo> datas = new ArrayList<LayoutInfo>();
		List<LayoutInfo> infoList = page.getResult();
		Set<LayoutRelaType> typeSet = new HashSet<LayoutRelaType>();//布局信息类型关联实体集合
		for(LayoutInfo info : infoList){
			typeSet = info.getLayoutRelaType();
			datas.add(new ThemeRecordForm(info,typeSet));
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}

	/**
	 * 删除布局信息列表
	 * @param request
	 * @param ids
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value = "/ic/theme/removelayinfosbyinfoids.f")
	 public boolean removeLayInfosByInfoIds(HttpServletRequest request, String layInfoIds,String themeId) {
		 List<String> idList = new ArrayList<String>();
		  if (StringUtils.isNotBlank(layInfoIds)) {
			  String[] idArray = layInfoIds.split(",");
			  for (String id : idArray) {
					idList.add(id);
				}
			  o_analysisBO.removeLayInfosBylayIds(idList,themeId);
			   return true;
		  } else {
			   return true;
		  }
	 }
	 /**
		 * 请求柱状图xml
		 * @param ids
		 * @return
		 */
	 private Map<String, Object> getMSColumnLine3D(String condItem,String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(condItem);
        String year = "";
        if (jsobj.get("year") != null) {
            year = jsobj.getString("year");
        }
        String kpiName = "";
        if (jsobj.get("kpiname") != null) {
            kpiName = jsobj.getString("kpiname");
        }

        String eType = "";
        if (jsobj.get("eType") != null) {
            eType = jsobj.getString("eType");
        }
        boolean isNewValue = false;
        if (jsobj.get("isNewValue") != null) {
            isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
        }
        List<KpiGatherResult> kpiGatherResultList = null;
        HashMap<String, TimePeriod> map = o_kpiGatherResultBO.findTimePeriodAllMap();

        if ("0frequecy_all".equalsIgnoreCase(eType)) {
            List<List<KpiGatherResult>> kpiGatherResultLists = new ArrayList<List<KpiGatherResult>>();
            HashMap<String, String> kpiGatherResultMapYearNewValue = new HashMap<String, String>();
            HashMap<String, List<KpiGatherResult>> mapYear = new HashMap<String, List<KpiGatherResult>>();
            String msColumnLine3D = "";
            if (jsobj.get("isNewValue") != null) {
                isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
            }
            String[] kpiIds = id.split(",");

            id = "'"+id+"'";
            String yearsStr = "";
            if (isNewValue) {
                // 年
                kpiGatherResultMapYearNewValue =  this.o_kpiBO.findKpiAndGatherResultYearNewValue(id);
                for (String kpi : kpiIds) {
                    if (kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")) != null) {
                        // 有最新值并且存在年份-保留
                        String years[] = this.getYears(kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")));
                        for (String str : years) {
                            yearsStr += "'" + str + "',";
                        }
                        yearsStr = yearsStr + "|";
                        yearsStr = yearsStr.replace(",|", "");
                        mapYear.put(kpi.replace("'", "").replace("'", ""),
                        		 this.o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr));
                    }
                }

                // 除了年
                kpiGatherResultLists.add(this.o_kpiBO.findKpiAndGatherResult(id, false, null));
            }
            else {

                for (String kpi : kpiIds) {
                    // 有最新值并且存在年份-保留
                    String years[] = this.getYears(year);
                    for (String str : years) {
                        yearsStr += "'" + str + "',";
                    }
                    yearsStr = yearsStr + "|";
                    yearsStr = yearsStr.replace(",|", "");
                    List<KpiGatherResult> list = this.o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr);
                    if (list != null) {
                        mapYear.put(kpi.replace("'", "").replace("'", ""), list);
                    }
                    // }
                }

                // 除年外
                kpiGatherResultLists.add(this.o_kpiBO.findKpiAndGatherResult(id, true, year));
            }

            // 年
            for (String string : kpiIds) {
                if (mapYear.get(string.replace("'", "").replace("'", "")) != null) {
                    msColumnLine3D = MSColumnLine3D.getXml("", kpiName, mapYear.get(string.replace("'", "").replace("'", "")), map);
                }
            }

            // 除年外
            for (String string : kpiIds) {
                for (List<KpiGatherResult> list : kpiGatherResultLists) {
                    kpiGatherResultList = this.getKpiGatherResultListMap(list, string.replace("'", "").replace("'", ""), year);
                    if (kpiGatherResultList.size() != 0) {
                        msColumnLine3D = MSColumnLine3D.getXml("", kpiName, kpiGatherResultList, map);
                    }
                }
            }

            result.put("success", true);
            result.put("xmlMap", msColumnLine3D);
            return result;

        }

        return result;
	}
	 /**
		 * 请求柱状图xml
		 * @param ids
		 * @return
		 */
	 private String getDoughnut3D(String condItem,String id) {
	        Map<String, Object> result = new HashMap<String, Object>();
	        JSONObject jsobj = JSONObject.fromObject(condItem);
	        String year = "";
	        if (jsobj.get("year") != null) {
	            year = jsobj.getString("year");
	        }

	        String eType = "";
	        if (jsobj.get("eType") != null) {
	            eType = jsobj.getString("eType");
	        }
	        boolean isNewValue = false;
	        if (jsobj.get("isNewValue") != null) {
	            isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
	        }
	        List<KpiGatherResult> kpiGatherResultList = null;
	        String doughnut3D = "";

	        if ("0frequecy_all".equalsIgnoreCase(eType)) {
	            List<List<KpiGatherResult>> kpiGatherResultLists = new ArrayList<List<KpiGatherResult>>();
	            HashMap<String, String> kpiGatherResultMapYearNewValue = new HashMap<String, String>();
	            HashMap<String, List<KpiGatherResult>> mapYear = new HashMap<String, List<KpiGatherResult>>();
	            if (jsobj.get("isNewValue") != null) {
	                isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
	            }
	            String[] kpiIds = id.split(",");

	            id = "'"+id+"'";
	            id = id.replace(",", "");
	            String yearsStr = "";
	            if (isNewValue) {
	                // 年
	                kpiGatherResultMapYearNewValue =  this.o_kpiBO.findKpiAndGatherResultYearNewValue(id);
	                for (String kpi : kpiIds) {
	                    if (kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")) != null) {
	                        // 有最新值并且存在年份-保留
	                        String years[] = this.getYears(kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")));
	                        for (String str : years) {
	                            yearsStr += "'" + str + "',";
	                        }
	                        yearsStr = yearsStr + "|";
	                        yearsStr = yearsStr.replace(",|", "");
	                        mapYear.put(kpi.replace("'", "").replace("'", ""),
	                        		 this.o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr));
	                    }
	                }

	                // 除了年
	                kpiGatherResultLists.add(this.o_kpiBO.findKpiAndGatherResult(id, false, null));
	            }
	            else {

	                for (String kpi : kpiIds) {
	                    // 有最新值并且存在年份-保留
	                    String years[] = this.getYears(year);
	                    for (String str : years) {
	                        yearsStr += "'" + str + "',";
	                    }
	                    yearsStr = yearsStr + "|";
	                    yearsStr = yearsStr.replace(",|", "");
	                    List<KpiGatherResult> list = this.o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr);
	                    if (list != null) {
	                        mapYear.put(kpi.replace("'", "").replace("'", ""), list);
	                    }
	                    // }
	                }

	                // 除年外
	                kpiGatherResultLists.add(this.o_kpiBO.findKpiAndGatherResult(id, true, year));
	            }
	            Kpi kpi = new Kpi();
	            // 年
	            for (String string : kpiIds) {
	                if (mapYear.get(string.replace("'", "").replace("'", "")) != null) {
	                	kpi = o_kpiBO.findKpiById(string);
	                	doughnut3D = Doughnut3D.getXml(kpi.getName(), mapYear.get(string.replace("'", "").replace("'", "")));
	                }
	            }

	            // 除年外
	            for (String string : kpiIds) {
	            	kpi = o_kpiBO.findKpiById(string);
	                for (List<KpiGatherResult> list : kpiGatherResultLists) {
	                    kpiGatherResultList = this.getKpiGatherResultListMap(list, string.replace("'", "").replace("'", ""), year);
	                    if (kpiGatherResultList.size() != 0) {
	                    	doughnut3D = Doughnut3D.getXml(kpi.getName(), kpiGatherResultList);
	                    }
	                }
	            }
	            return doughnut3D;

	        }

	        return doughnut3D;
		}
    private List<KpiGatherResult> getKpiGatherResultListMap(List<KpiGatherResult> kpiGatherResultLists, String kpiId, String year) {
        List<KpiGatherResult> kpiGatherResultList = new ArrayList<KpiGatherResult>();
        for (KpiGatherResult kpiGatherResult : kpiGatherResultLists) {
            if (kpiId.equalsIgnoreCase(kpiGatherResult.getKpi().getId())) {
                if (kpiGatherResult.getKpi().getGatherFrequence().getId().equalsIgnoreCase("0frequecy_year")) {
                    break;
                }
                else {
                    kpiGatherResultList.add(kpiGatherResult);
                }
            }
        }

        return kpiGatherResultList;
    }
    /**
     * 获取当前年度的前4个年度集合
     * 
     * @param String
     *            time 当前年度
     * @return String[]
     * @author 金鹏祥
     * */
    private String[] getYears(String year) {
        int g = 0;
        String[] str = new String[5];
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                str[i] = String.valueOf(year);
                g = Integer.parseInt(year);
            }
            else {
                g = g - 1;
                str[i] = String.valueOf(g);
            }
        }
        return str;
    }  
    
    public Map<String, Object> getMSLine(String condItem ,String kpiid){
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(condItem);
        String yearId = "";
        if (jsobj.get("year") != null) {
            yearId = jsobj.getString("year");
        }
        String monthId = "";
        if (jsobj.get("monthId") != null) {
            monthId = jsobj.getString("monthId");
        }
        boolean isNewValue = false;
        if (jsobj.get("isNewValue") != null) {
            isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
        }
        String dataType = "";
        if (jsobj.get("dataType") != null) {
            dataType = jsobj.getString("dataType");
        }

        List<RelaAssessResult> relaAssessResultByDataTypeList = o_relaAssessResultBO.findRelaAssessResultByDataType(dataType, kpiid, isNewValue,
                yearId, monthId);
        String xml = MSLine.getXml(relaAssessResultByDataTypeList);

        result.put("success", true);
        result.put("xmlMap", xml);
        return result;
    }
}
