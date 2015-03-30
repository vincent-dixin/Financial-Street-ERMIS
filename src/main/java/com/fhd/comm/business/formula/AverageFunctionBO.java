package com.fhd.comm.business.formula;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class AverageFunctionBO extends FunctionCalculateBO implements
		ICustomFunctionBO {
	/**
	 * average函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		String reval = "0";
		for(int i=0; i<formulaArray.length; i++){
			//判断参数数组中的每个参数是否是数字
			if(formulaArray[i].matches("\\d+\\.\\d+") || formulaArray[i].matches("-?\\d+")){
				//参数是数字，直接放到公式中相加
				reval = reval + "+" + formulaArray[i];
			}else{
				//参数是表达式，递归进行计算
				reval = reval + "+" + calculate(formulaArray[i]);
			}
		}
		return String.valueOf(Double.valueOf(szys(reval))/formulaArray.length);
	}
}
