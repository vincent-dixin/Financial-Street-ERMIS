package com.fhd.comm.interfaces;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Service;

/**
 * 系统函数计算接口.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-27		下午10:47:27
 * @see 	 
 */
@Service
@SuppressWarnings("rawtypes")
public interface ISystemFunctionBO {
	/**
	 * 系统公式计算.
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
	public String calculateRule(Class[] cs, Object[] objs) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException;
}
