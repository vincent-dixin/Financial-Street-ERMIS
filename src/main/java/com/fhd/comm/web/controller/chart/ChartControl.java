package com.fhd.comm.web.controller.chart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.utils.excel.util.ExcelUtil;

/**
 * 图表control.
 * @author 吴德福
 */
@Controller
@SuppressWarnings({"unchecked","rawtypes"})
public class ChartControl {

	/**
	 * 一维图表xml.
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/chart/findOneDimensionalChartXml.f")
	public Map<String, Object> findOneDimensionalChartXml() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		Map<String, Object> row = new HashMap<String, Object>();
			
		row.put("id", "id");
		row.put("name", "name");
		row.put("value", decimalFormat.format(Math.random()*100));
		
		datas.add(row);
		
		map.put("datas", datas);
		map.put("totalCount", datas.size());
		return map;
	}
	
	/**
	 * 二维图表xml.
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/chart/findTwoDimensionalChartXml.f")
	public Map<String, Object> findTwoDimensionalChartXml() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		Map<String, Object> row = null;
		for(int i=0;i<=5;i++){
			row = new HashMap<String, Object>();
			
			row.put("id", "id"+i);
			row.put("name", "name"+i);
			row.put("value", decimalFormat.format(i+Math.random()*100));
			
			datas.add(row);
		}
		
		map.put("datas", datas);
		map.put("totalCount", datas.size());
		return map;
	}
	
	/**
	 * 三维图表xml.
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping("/chart/findThreeDimensionalChartXml.f")
	public Map<String, Object> findThreeDimensionalChartXml(String id, String name) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		DecimalFormat decimalFormat = new DecimalFormat("0");
		
		Map<String, Object> row = null;
		for(int i=1;i<=5;i++){
			row = new HashMap<String, Object>();
			
			row.put("id", "id"+i);
			row.put("name", "name"+i);
			row.put("value1", decimalFormat.format(i+Math.random()*100));
			row.put("value2", decimalFormat.format(i+Math.random()*100));
			row.put("value3", decimalFormat.format(i+Math.random()*100));
			row.put("value4", decimalFormat.format(i+Math.random()*100));

			datas.add(row);
		}
		
		map.put("datas", datas);
		map.put("totalCount", datas.size());
		return map;
	}
	
	/**
	 * grid数据转换成excel.
	 * @param datas
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/gridExportXls.f")
	public void gridExportXls(String datas, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		datas = datas.replaceAll("\\n"," ");
		JSONArray jsonArray = JSONArray.fromObject(datas);
        JSONObject jsonObject;

        List<Object[]> list = new ArrayList<Object[]>();
        
        List<String> l = new ArrayList<String>();
        
        for ( int i = 0 ; i<jsonArray.size(); i++){
            jsonObject = jsonArray.getJSONObject(i);
			Iterator keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String,Object> valueMap = new HashMap<String,Object>();
            List<Object> data = new ArrayList<Object>();
            while( keyIter.hasNext()) {
                key = (String)keyIter.next();
                if(!"undefined".equals(key)){
                	 l.add(key);
                     value = jsonObject.get(key);
                     valueMap.put(key, value);
                     data.add(value);
                }
            }
            list.add(data.toArray());
        }
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
        	String element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        String[] fieldTitle = new String[newList.size()];
        int i = 0;
        for (String string : newList) {
        	fieldTitle[i] = string;
			i++;
		}
        session.setAttribute("list", list);
        session.setAttribute("fieldTitle", fieldTitle);
	}

	/**
	 * 下载excel.
	 * @param exportFileName
	 * @param sheetName
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/downloadXls.f")
	public void downloadXls(String exportFileName, String sheetName, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		List<Object[]> list = (List<Object[]>) session.getAttribute("list");
		String[] fieldTitle = (String[]) session.getAttribute("fieldTitle");
		if(StringUtils.isBlank(exportFileName)){
			exportFileName = "导出数据.xls";
		}else{
			exportFileName += ".xls";
		}
		if(StringUtils.isBlank(sheetName)){
			sheetName = "全面风险管理信息系统";
		}
		ExcelUtil.write(list, fieldTitle, exportFileName, sheetName, 0, request, response);
	}
	
	/**
	 * 下载文件.
	 * @param path
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadFile.f")
	public void downloadFile(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isNotBlank(path)){
			response.reset();
			
			//String fileType = path.substring(path.lastIndexOf(".")+1, path.length());
			//response.setContentType("application/pdf");
			response.setContentType("application/x­msd ownload");
			response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(path.substring(path.lastIndexOf("/")+1, path.length()), "UTF-8"));
			
			String filename=URLEncoder.encode(path.substring(path.lastIndexOf("/")+1, path.length()), "UTF-8");
			
			String filePath = ResourceBundle.getBundle("fusioncharts_export").getString("SAVEPATH")+filename;
			//String filePath = request.getServletContext().getRealPath("/") + "images\\fcexport\\"+filename;

			File file = new File(filePath);
			//读出文件到 i/o流
			FileInputStream  fis=new  FileInputStream(file);
			BufferedInputStream  buff=new  BufferedInputStream(fis);
			byte[]  b=new byte[1024];
			//相当于我们的缓 存
			long  k=0;
			//该值用于计算当前实际下载了多 少字节 
			//从 response对象中得到输出流,准备下载 
			OutputStream  out=response.getOutputStream();
			//开始循环下载
			while(k<file.length()){
				int  j=buff.read(b,0,1024);
				k+=j;
				//将 b 中的数据写到客户端的内存
				out.write(b,0,j);
			}
			out.flush();
			out.close();
		}
	}
}