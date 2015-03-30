package com.fhd.comm.business.formula;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class StandardDeviationFunctionBO extends FunctionCalculateBO implements ICustomFunctionBO{
	/**
	 * standardDeviation函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		double[] params = new double[formulaArray.length];
		
		for(int i=0; i<formulaArray.length; i++){
			//判断参数数组中的每个参数是否是数字
			if(formulaArray[i].matches("\\d+\\.\\d+") || formulaArray[i].matches("-?\\d+")){
				//参数是数字，直接放到公式中相加
				params[i] = Double.valueOf(formulaArray[i]);
			}else{
				//参数是表达式，递归进行计算
				params[i] = Double.valueOf(calculate(formulaArray[i]));
			}
		}
		return String.valueOf(StatisticFunctionCalculateBO.dtdev(params));
	}
}
