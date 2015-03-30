package com.fhd.sys.business.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.core.utils.DateUtils;
import com.fhd.sys.entity.file.VFileUploadEntity;

/**
 * 
 * ClassName:FileBO
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-10		上午11:39:00
 *
 * @see
 */
@Service
public class FileBO {
	
	@Autowired
	private FileUploadBO o_fileUploadBO;
	

	public List<Map<String, Object>> listMap(String fileType,String oldFileName, String[] ids){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<VFileUploadEntity> list = o_fileUploadBO.findBySome(fileType, oldFileName, ids);
		for (VFileUploadEntity vFileUploadEntity : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", vFileUploadEntity.getId());
			data.put("dbid", vFileUploadEntity.getId());
			data.put("oldFileName", vFileUploadEntity.getOldFileName());
			data.put("uploadTime", DateUtils.formatDate(vFileUploadEntity.getUploadTime(),"yyyy-MM-dd HH:mm:ss"));
			data.put("fileType", vFileUploadEntity.getFileType().getName());
			data.put("countNum", vFileUploadEntity.getCountNum());
			datas.add(data);
		}
		return datas;
	}
}