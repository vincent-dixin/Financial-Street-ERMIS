package com.fhd.risk.interfaces;

import java.util.List;
import java.util.Set;

import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Score;

/**
 * 维度BO的接口:维度的增删改查相关信息
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-22		上午10:43:14
 *
 * @see 	 
 */
public interface IDimensionBO {
	/**
	 * <pre>
	 * 根据维度ID拷贝一份维度及维度分值数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void saveDimensionById(String dimensionId);
	
	/**
	 * <pre>
	 * 批量更新维度信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的信息
	 * @param companyId 所属公司ID
	 * @since  fhd　Ver 1.1
	*/
	public void mergeDimensionBatch(String jsonString,String companyId);
	/**
	 * <pre>
	 * 批量更新维度分值信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的信息
	 * @param dimensionId 维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void mergeScoreBatch(String jsonString,String dimensionId);
	
	/**
	 * <pre>
	 * 级联删除维度信息，包括该维度下的维度分值
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id 维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeDimensionById(String id);
	
	/**
	 * <pre>
	 * 根据维度分值的ID删除维度分值信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id 维度分值的ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeScoreById(String id);
	
	/**
	 * <pre>
	 * 根据维度ID批量删除维度下所有维度分值数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @since  fhd　Ver 1.1
	*/
	public void removeScoreByDimensionIdBatch(String dimensionId);
	
	/**
	 * <pre>
	 * 根据查询条件模糊匹配编号或名称获得所有的满足条件的维度列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @param deleteStatus 删除状态
	 * @param ingnorDimensionIdSet 忽略掉的维度ID集合
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Dimension> findDimensionBySome(String query, String deleteStatus, Set<String> ingnorDimensionIdSet);
	
	/**
	 * <pre>
	 * 查找ID为dimensionId的维度
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Dimension findDimensionById(String dimensionId);
	
	/**
	 * <pre>
	 * 根据查询条件模糊匹配名称或描述获得所有的满足条件的维度分值列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Score> findScoreBySome(String dimensionId,String query);
}

