package com.fhd.fdc.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ObjectUtil {
	/**
	 * 对象基本属性拷贝到map对象
	 * 
	 * @param record
	 * @param obj
	 * @return void
	 */
	public static void ObjectPropertyToMap(Map<String, String> record, Object obj) {
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = obj.getClass();
			Method[] ms = clazz.getMethods();
			for (Method m : ms) {
				if (m.getName().startsWith("get")) {
					String pName = m.getName().substring(3);
					pName = pName.substring(0, 1).toLowerCase() + pName.substring(1);
					Object o = m.invoke(obj);
					if (m.getReturnType().equals(String.class) || m.getReturnType().equals(Integer.class) || m.getReturnType().equals(int.class) || m.getReturnType().equals(Boolean.class)) {
						record.put(pName, o == null ? "" : o.toString());
					} else if (m.getReturnType().equals(Date.class)) {
						record.put(pName, o == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format((Date) o).toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
