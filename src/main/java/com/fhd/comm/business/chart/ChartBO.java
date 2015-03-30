package com.fhd.comm.business.chart;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fhd.core.utils.encode.JsonBinder;

/**
 * 生成图表xml数据BO.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2013-1-7  下午6:25:44
 * @see
 */
@Service
public class ChartBO {
	
	/**
	 * 生成仪表盘图表xml数据.
	 * @param lowMinValue 仪表盘绿色区间最小值
	 * @param lowMaxValue 仪表盘绿色区间最大值
	 * @param midMinValue 仪表盘黄色区间最小值
	 * @param midMaxValue 仪表盘黄色区间最大值
	 * @param highMinValue 仪表盘红色区间最小值
	 * @param highMaxValue 仪表盘红色区间最大值
	 * @param value 仪表盘指标显示的值
	 * @param name 仪表盘下面显示的名称
	 * @return String 
	 */
	public String generateAngularGaugeXml(String lowMinValue, String lowMaxValue, String midMinValue, String midMaxValue, String highMinValue, String highMaxValue, Double value, String name){
		StringBuilder sb = new StringBuilder();
		sb.append("<chart showBorder='0' bgColor='FFFFFF' manageResize='1' origW='400' origH='250'  manageValueOverlapping='1' autoAlignTickValues='1' upperLimit='100' lowerLimit='0' majorTMNumber='10' majorTMHeight='8' showGaugeBorder='0' gaugeOuterRadius='140' gaugeOriginX='205' gaugeOriginY='206' gaugeInnerRadius='2' formatNumberScale='1' decmials='2' tickMarkDecimals='1' pivotRadius='17' showPivotBorder='1' pivotBorderThickness='5' tickValueDistance='10' >");
			sb.append("<colorRange>");
				//记分卡预警方案
	    		sb.append("<color minValue='").append(lowMinValue).append("' maxValue='").append(lowMaxValue).append("' code='B41527'/>");
	    		sb.append("<color minValue='").append(midMinValue).append("' maxValue='").append(midMaxValue).append("' code='E48739'/>");
	    		sb.append("<color minValue='").append(highMinValue).append("' maxValue='").append(highMaxValue).append("' code='399E38'/>");
			sb.append("</colorRange>");
			sb.append("<dials>");
				//显示值
				sb.append("<dial value='").append(value).append("' borderAlpha='0' baseWidth='28' topWidth='1' radius='130'/>");
			sb.append("</dials>");
			sb.append("<annotations>");
	    		sb.append("<annotationGroup>");
	    			//记分卡名称
	    			sb.append("<annotation type='text' x='200' y='245' label='").append(name).append("' align='center' bold='1' color='#3d7f9f' size='12'/>");
	    		sb.append("</annotationGroup>");
			sb.append("</annotations>");
		sb.append("</chart>");
		Map<String,Object> map = new HashMap<String,Object>();
		return JsonBinder.buildNonNullBinder().toJson(map);
	}
}
