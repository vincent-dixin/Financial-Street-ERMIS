package com.fhd.risk.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.utils.UserContext;
import com.fhd.risk.business.DimensionBO;
import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Score;

/**
 * ClassName:DimensionControl 维度控制类
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午3:50:19
 *
 * @see 	 
 */
@Controller
public class DimensionControl {
	
	@Autowired
	private DimensionBO o_dimensionBO;
	/**
	 * <pre>
	 * 根据维度ID拷贝一份维度及维度分值数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value="risk/saveDimensionById.f")
	public void saveDimensionById(String dimensionId,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag="false";
		try{
			out = response.getWriter();
			o_dimensionBO.saveDimensionById(dimensionId);
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 批量更新维度信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的记录的字符串
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value="risk/mergeDimensionBatch.f")
	public void mergeDimensionBatch(String jsonString,HttpServletResponse response) throws IOException {
		PrintWriter out = null;
		String flag="false";
		try{
			out = response.getWriter();
			String companyId = UserContext.getUser().getCompanyid();
			o_dimensionBO.mergeDimensionBatch(jsonString, companyId);
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 批量更新维度分值信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param jsonString 要更新的记录的字符串
	 * @param dimensionId 要更新的维度分值所属的维度ID
	 * @param response
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value="risk/mergeScoreBatch.f")
	public void mergeScoreBatch(String jsonString, String dimensionId, HttpServletResponse response) throws IOException {
		PrintWriter out = null;
		String flag="false";
		try{
			out=response.getWriter();
			o_dimensionBO.mergeScoreBatch(jsonString, dimensionId);
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 根据维度ID物理级联删除维度及该维度下的维度分值数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param dimensionId 维度ID
	 * @param response 
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "risk/removeDimensionById.f")
	public void removeDimensionById(String dimensionId,HttpServletResponse response) throws Exception{
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			o_dimensionBO.removeDimensionById(dimensionId);
			flag = "true";
		} finally {
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 根据维度分值ID物理删除该维度分值数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param scoreId 维度分值ID
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "risk/removeScoreById.f")
	public void removeScoreById(String scoreId,HttpServletResponse response) throws Exception{
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			o_dimensionBO.removeScoreById(scoreId);
			flag = "true";
		} finally {
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 根据查询条件查询所有满足条件的维度数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param start 不可用
	 * @param limit 不可用
	 * @param query 查询条件
	 * @param deleteStatus 删除状态
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "risk/findDimensionList.f")
	public Map<String, Object> findDimensionList(int start,int limit,String query,String deleteStatus) throws Exception {
		List<Dimension> dimensionList = new ArrayList<Dimension>();
		dimensionList = o_dimensionBO.findDimensionBySome(query, deleteStatus, null);
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		for (Dimension dimension : dimensionList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", dimension.getId());
			item.put("name",dimension.getName());
			item.put("code",dimension.getCode());
			item.put("desc",dimension.getDesc());
			item.put("deleteStatus",dimension.getDeleteStatus());
			item.put("sort",dimension.getSort());
			datas.add(item);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", datas.size());
		map.put("datas", datas);
		return map;
	}
	
	/**
	 * <pre>
	 * 根据维度ID和查询条件查询所有满足条件的维度数据
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param start 不可用
	 * @param limit 不可用
	 * @param dimensionId 维度ID
	 * @param query 查询条件
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "risk/findScoreList.f")
	public Map<String, Object> findScoreList(int start,int limit,String dimensionId,String query) throws Exception {
		List<Score> scoreList = new ArrayList<Score>();
		scoreList = o_dimensionBO.findScoreBySome(dimensionId,query);
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		for (Score score : scoreList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", score.getId());
			item.put("name",score.getName());
			item.put("value",score.getValue());
			item.put("desc",score.getDesc());
			item.put("sort",score.getSort());
			datas.add(item);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", datas.size());
		map.put("datas", datas);
		return map;
	}
	
}

