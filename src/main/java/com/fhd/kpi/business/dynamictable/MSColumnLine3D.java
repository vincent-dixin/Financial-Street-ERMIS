package com.fhd.kpi.business.dynamictable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.kpi.entity.KpiGatherResult;

/**
 * FUNSIONCHARTS 2D折线图
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2013 2013-1-6 下午12:15:15
 * 
 * @see
 */
public class MSColumnLine3D {

    
	/**
	 * 得到XML
	 * 
	 * @author 金鹏祥
	 * @param title
	 *            主标题
	 * @param timeTitle
	 *            副标题
	 * @param list
	 *            指标结果实体集合
	 * @return String
	 */
	public static String getXml(String title, String title2,
			List<KpiGatherResult> list, HashMap<String, TimePeriod> map) {
		StringBuffer xml = new StringBuffer();
		String name = "";
		
		xml.append("<chart showValues='0' canvasbgColor='ffffff' canvasbgAlpha='0' canvasBaseDepth='1' chartBottomMargin='50' ");
		xml.append("baseFontSize='10' legendPosition='right' legendPosition='right' ");
		xml.append("legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0'>");

		xml.append("<categories>");
		
		for (KpiGatherResult kpiGatherResult : list) {
			if(map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().indexOf("季度") != -1){
				name = map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().split("第")[1].toString();
			}else{
				if(map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().indexOf("月") != -1){
					name = map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().split("年")[1].toString();
					if(name.substring(0, 1).equalsIgnoreCase("0")){
						name = name.replace("0", "");
					}
				}else{
					if(map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().indexOf("周") != -1){
						name = map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().split("年")[1].toString();
					}else{
						name = map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName().toString();
					}
				}
			}
			
			if(name.indexOf("年") != -1){
				name = name.replace("年", "");
			}
			
			if("0frequecy_week".equalsIgnoreCase(kpiGatherResult.getKpi().getGatherFrequence().getId())){
				if(getMonth().equalsIgnoreCase(kpiGatherResult.getTimePeriod().getMonth())){
					xml.append("<category name='"+ name + "' />");
				}
			}else{
				xml.append("<category name='"+ name + "' />");
			}
		}
		xml.append("</categories>");

		xml.append("<dataset seriesName='实际值'>");
		for (KpiGatherResult kpiGatherResult : list) {
			if("0frequecy_week".equalsIgnoreCase(kpiGatherResult.getKpi().getGatherFrequence().getId())){
				if(getMonth().equalsIgnoreCase(kpiGatherResult.getTimePeriod().getMonth())){
					xml.append("<set value='" + kpiGatherResult.getFinishValue() + "' />");
				}
			}else{
				xml.append("<set value='" + kpiGatherResult.getFinishValue() + "' />");
			}
		}
		xml.append("</dataset>");

		xml.append("<dataset seriesname='目标值' renderAs='Line' color='BBDA00' ");
		xml.append("anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' ");
		xml.append("anchorBorderColor='FFFFFF' anchorBorderThickness='2'>");
		for (KpiGatherResult kpiGatherResult : list) {
			if("0frequecy_week".equalsIgnoreCase(kpiGatherResult.getKpi().getGatherFrequence().getId())){
				if(getMonth().equalsIgnoreCase(kpiGatherResult.getTimePeriod().getMonth())){
					xml.append("<set value='" + kpiGatherResult.getTargetValue() + "' />");
				}
			}else{
				xml.append("<set value='" + kpiGatherResult.getTargetValue() + "' />");
			}
		}
		xml.append("</dataset>");
		xml.append("</chart>");
		
		return xml.toString();
	}
	
	
	
	/**创建图表的XML
	 * @param map 图表数据
	 * @return
	 */
	public static Map<String,String> createChartXml(Map<String,List<Object[]>> map){
	    Map<String,String> resultMap = new HashMap<String, String>();
	    if(null!=map){
	        for (Map.Entry<String, List<Object[]>> m : map.entrySet()) {
	            String key = m.getKey();
	            String[] arr = key.split("@");
	            String kpiid = arr[0];
	            String frequence = arr[1];
	            List<Object[]> datas = m.getValue();
	            StringBuffer xmlbuf = new StringBuffer();
	            xmlbuf.append("<chart showValues='0' canvasbgColor='ffffff' canvasbgAlpha='0' canvasBaseDepth='1'  baseFontSize='12' legendPosition='right' legendPosition='right' legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0'>");
	            StringBuffer categoriesBuf = new StringBuffer();
                StringBuffer finishBuf = new StringBuffer();
                StringBuffer targetBuf = new StringBuffer();
                categoriesBuf.append("<categories>");
                targetBuf.append("<dataset seriesname='目标值' renderAs='Line' color='BBDA00' anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2'>");
                finishBuf.append("<dataset seriesName='实际值'>");
	            if(null!=datas){
	                if("0frequecy_month".equals(frequence)){
	                    for(int i=0;i<datas.size();i++){
	                        categoriesBuf.append("<category label='").append(i+1).append("月'").append(" /> ");
	                        Object[] values = datas.get(i);
	                        targetBuf.append("<set value='").append(values[2]==null?"":values[2]).append("' />");
	                        finishBuf.append("<set value='").append(values[3]==null?"":values[3]).append("' />");
	                    }
	                }else if("0frequecy_quarter".equals(frequence)){
	                    for(int i=0;i<datas.size();i++){
	                        categoriesBuf.append("<category label='").append(i+1).append("季度'").append(" /> ");
	                        Object[] values = datas.get(i);
	                        targetBuf.append("<set value='").append(values[2]==null?"":values[2]).append("' />");
	                        finishBuf.append("<set value='").append(values[3]==null?"":values[3]).append("' />");
	                    }
	                    
	                }else if("0frequecy_halfyear".equals(frequence)){
	                    for(int i=0;i<datas.size();i++){
	                        categoriesBuf.append("<category label='").append(i==0?"上半年'":"下半年'").append(" /> ");
	                        Object[] values = datas.get(i);
	                        targetBuf.append("<set value='").append(values[2]==null?"":values[2]).append("' />");
	                        finishBuf.append("<set value='").append(values[3]==null?"":values[3]).append("' />");
	                    }
	                }else if("0frequecy_week".equals(frequence)){
	                    for(int i=0;i<datas.size();i++){
	                        categoriesBuf.append("<category label='第").append(i+1).append("周'").append(" /> ");
	                        Object[] values = datas.get(i);
                            targetBuf.append("<set value='").append(values[2]==null?"":values[2]).append("' />");
                            finishBuf.append("<set value='").append(values[3]==null?"":values[3]).append("' />");
	                    }
	                    
	                }
	                
	            }else{
	                
	                if("0frequecy_month".equals(frequence)){
                        for(int i=0;i<12;i++){
                            categoriesBuf.append("<category label='").append(i+1).append("月'").append(" /> ");
                        }
                        
                    }else if("0frequecy_quarter".equals(frequence)){
                        for(int i=0;i<4;i++){
                            categoriesBuf.append("<category label='").append(i+1).append("季度'").append(" /> ");
                        }
                        
                    }else if("0frequecy_halfyear".equals(frequence)){
                        for(int i=0;i<2;i++){
                            categoriesBuf.append("<category label='").append(i==0?"上半年'":"下半年'").append(" /> ");
                        }
                    }else if("0frequecy_week".equals(frequence)){
                        for(int i=0;i<4;i++){
                            categoriesBuf.append("<category label='第").append(i+1).append("周'").append(" /> ");
                        }
                    }
	            }
	            targetBuf.append("</dataset>");
                finishBuf.append("</dataset>");
                categoriesBuf.append(" </categories> ");
                xmlbuf.append(categoriesBuf).append(finishBuf).append(targetBuf);
                xmlbuf.append("</chart>");
                resultMap.put(kpiid, xmlbuf.toString());
	        }
	    }
        return resultMap;
	}
	
	private static String getMonth(){
		Calendar cal = Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH) + 1;
	    return String.valueOf(month);
	}
}