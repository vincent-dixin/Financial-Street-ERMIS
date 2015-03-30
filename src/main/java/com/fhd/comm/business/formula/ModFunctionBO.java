package com.fhd.comm.business.formula;

import com.fhd.comm.interfaces.ICustomFunctionBO;

public class ModFunctionBO extends FunctionCalculateBO implements ICustomFunctionBO {

	/**
	 * mod函数计算.
	 * @param formulaArray
	 * @return String
	 */
	@Override
	public String calculateRule(String[] formulaArray) {
		String reval = "0";
		double d2 = new Double(calculate(formulaArray[1]));
		if (d2 == 0)
			return reval;
		double d1 = new Double(calculate(formulaArray[0]));
		reval = (d1 % d2) + "";
		return reval;
	}

}
