package com.fhd.comm.interfaces;

import org.springframework.stereotype.Service;

/**
 * 自定义函数计算接口.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-27		下午10:47:27
 * @see 	 
 */
@Service
public interface ICustomFunctionBO {
	/**
	 * 自定义公式计算.
	 * @param formulaArray 公式拆分数组
	 * @return String
	 */
	public String calculateRule(String[] formulaArray);
}
