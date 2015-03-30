package com.fhd.comm.business.formula;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class IntFunctionBO extends FunctionCalculateBO implements ICustomFunctionBO {

	/**
	 * int函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		String reval = "0";
		reval = Math.floor(new Double(calculate(formulaArray[0]))) + "";
		return reval;
	}
}
