package com.fhd.comm.business.formula;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fhd.comm.interfaces.ISystemFunctionBO;

@SuppressWarnings({"rawtypes","unchecked"})
public class ExpFunctionBO extends FunctionCalculateBO implements
		ISystemFunctionBO {
	/**
	 * exp函数计算.
	 * @param cs
	 * @param objs
	 * @return String
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 */
	@Override
	public String calculateRule(Class[] cs, Object[] objs)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		Class cls = Class.forName("java.lang.Math");
		Method m = cls.getMethod("exp", cs);
		return String.valueOf(m.invoke(cls, objs));
	}
}
