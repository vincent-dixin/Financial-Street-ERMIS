package com.fhd.comm.business.formula;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class CountFunctionBO extends FunctionCalculateBO implements
		ICustomFunctionBO {

	/**
	 * count函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		return String.valueOf(formulaArray.length);
	}
}
