package com.fhd.sys.business.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.VFileUploadEntity;

/**
 * 
 * ClassName:KpiStrategyMapTreeBO:战略目标树BO
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-8-29		上午10:11:00
 *
 * @see
 */
@Service
public class FileUploadGridBO {
	
	@Autowired
	private FileUploadBO o_fileUploadBO;
	

	public Map<String, Object> page(String fileTypeId,String oldFileName, Integer limit, Integer start,List<Map<String, String>> sortList){
		Map<String, Object> map = new HashMap<String, Object>();
		Page<VFileUploadEntity> page = new Page<VFileUploadEntity>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		page = o_fileUploadBO.findPageBySome(page, fileTypeId,oldFileName,sortList);
		List<VFileUploadEntity> list = page.getResult();
		for (VFileUploadEntity vFileUploadEntity : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", vFileUploadEntity.getId());
			data.put("dbid", vFileUploadEntity.getId());
			data.put("oldFileName", vFileUploadEntity.getOldFileName());
			data.put("uploadTime", DateUtils.formatDate(vFileUploadEntity.getUploadTime(),"yyyy-MM-dd HH:mm:ss"));
			DictEntry fileType = vFileUploadEntity.getFileType();
			if(fileType!=null){
				data.put("fileTypeDictEntryName", fileType.getName());
			}
			data.put("countNum", vFileUploadEntity.getCountNum());
			datas.add(data);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		map.put("success", true);
		return map;
	}
}