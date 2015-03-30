package com.fhd.comm.interfaces;

import java.util.List;
import java.util.Map;

public interface IFormulaCalculateBO {
	/**
	 * 分解公式字符串.
	 * @param targetName 对象名称
	 * @param formula 公式
	 * @return List<Map<String, String>> map格式：{name:'',type:''}
	 */
	public List<Map<String, String>> decompositionFormulaString(String targetName, String formula);
	/**
	 * 计算公式.
	 * @param targetName 对象名称
	 * @param type 类型:kpi/risk
	 * @param formula 公式内容
	 * @param timePeriodId 时间区间维id
	 * @return String
	 */
	public String calculate(String targetName, String type, String formula, String timePeriodId);
	/**
	 * java正则表达式判断字符串是否是double或int类型.
	 * @param str
	 * @return boolean
	 */
	public boolean isDoubleOrInteger(String str);
}
