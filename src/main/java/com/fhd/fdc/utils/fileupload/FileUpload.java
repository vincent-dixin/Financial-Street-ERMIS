
/**
 * FileUpload.java
 * com.fhd.fdc.utils
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-2-17        David
 *
 * Copyright (c) 2011, FirstHuida All Rights Reserved.
*/


package com.fhd.fdc.utils.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 文件上传公共类.
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-2-17		下午02:14:18
 * @see 	 
 */
public class FileUpload {
	
	private static Map<String,String> _ALLOWED_FILE_TYPE = new HashMap<String,String>();//允许上传的文件列表
	
	static{
		_ALLOWED_FILE_TYPE.put("jpg", null);
		_ALLOWED_FILE_TYPE.put("jpeg", null);
		_ALLOWED_FILE_TYPE.put("gif", null);
		_ALLOWED_FILE_TYPE.put("txt", null);
		_ALLOWED_FILE_TYPE.put("doc", null);
		_ALLOWED_FILE_TYPE.put("mp3", null);
		_ALLOWED_FILE_TYPE.put("wma", null);
		_ALLOWED_FILE_TYPE.put("m4a", null);
		_ALLOWED_FILE_TYPE.put("rar", null);
		_ALLOWED_FILE_TYPE.put("zip", null);
		_ALLOWED_FILE_TYPE.put("xls", null);
		_ALLOWED_FILE_TYPE.put("docx", null);
		_ALLOWED_FILE_TYPE.put("xlsx", null);
		_ALLOWED_FILE_TYPE.put("ftl", null);
		_ALLOWED_FILE_TYPE.put("properties", null);
		_ALLOWED_FILE_TYPE.put("pdf", null);

	}
	
	/**
	 * <pre>
	 * uploadFile:上传文件
	 * </pre>
	 * 
	 * @author David
	 * @param file
	 * @param response
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public static FileUploadEntity uploadFile(MultipartFile file) throws Exception{
		FileUploadEntity fileUploadEntity = new FileUploadEntity();
		// 初始化参数
		String path = "";
		long size = 0;
		// 当前文件为空
		if (file.getBytes() == null || file.getBytes().length == 0) {
			fileUploadEntity.setUploadErrorMessage("当前文件不能为空!");
			return fileUploadEntity;
		}
		// 得到文件的完整路径
		path = file.getOriginalFilename();

		// 构建文件目录
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		// 得到文件的大小
		size = file.getSize();
		if ("".equals(path) || size == 0) {
			fileUploadEntity.setUploadErrorMessage("文件有错误:大小为0!");
			return fileUploadEntity;
		}

		// 得到去除路径的文件名
		String t_name = path.substring(path.lastIndexOf("\\") + 1);
		// 得到文件的扩展名(无扩展名时将得到全名)
		String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
		
		if(!_ALLOWED_FILE_TYPE.containsKey(t_ext)){
			fileUploadEntity.setUploadErrorMessage("请上传以下类型的文件:<br>" + _ALLOWED_FILE_TYPE.keySet().toString());
			return fileUploadEntity;
		}

		long now = System.currentTimeMillis();
		// 根据系统时间生成上传后保存的文件名
		String prefix = String.valueOf(now);
		
		//时间数字文件名称
		String filename = prefix + "." + t_ext;
		FileOutputStream out = null;
		
		// 配置文件读取文件路径：F:\\upload\\
		String fileUploadpath = ResourceBundle.getBundle("application").getString("fileUploadPath");

		try {
			//保存到目录开始
			File tempPath = new File(fileUploadpath);
			if (!tempPath.exists()) {
				tempPath.mkdirs();
			}
			out = new FileOutputStream(new File(fileUploadpath + filename));
			IOUtils.copy(file.getInputStream(), out);
			
			//生成文件实体
			fileUploadEntity.setId(Identities.uuid());
			fileUploadEntity.setOldFileName(t_name.substring(0,t_name.length() - t_ext.length() - 1)+ "." + t_ext);
//			fileUploadEntity.setNewFileName(t_name.substring(0, t_name.length()- t_ext.length() - 1)+ "." + t_ext);
			fileUploadEntity.setNewFileName(filename);
			fileUploadEntity.setFileAddress(fileUploadpath + filename);
			fileUploadEntity.setFileSize(String.valueOf(size));
			DictEntry fileType = new DictEntry(t_ext);
			fileUploadEntity.setFileType(fileType);
			fileUploadEntity.setUploadTime(new Date());
			OperatorDetails user = UserContext.getUser();
			SysUser sysUser = new SysUser();
			sysUser.setId(user.getUserid());
			fileUploadEntity.setSysUser(sysUser);
			fileUploadEntity.setCountNum(0);
			fileUploadEntity.setContents(file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(out!=null)
				out.close();
		}

		return fileUploadEntity;
	}
	/**
	 * 文件上传组件公共类.
	 * @author 吴德福
	 * @param chooseWay 上传到目录或者数据库
	 * @param file 要上传的文件
	 * @return FileUploadEntity
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public static FileUploadEntity uploadFile(String chooseWay, MultipartFile file) throws Exception{
		FileUploadEntity fileUploadEntity = new FileUploadEntity();
		// 初始化参数
		String path = "";
		long size = 0;
		// 当前文件为空
		if (file.getBytes() == null || file.getBytes().length == 0) {
			fileUploadEntity.setUploadErrorMessage("当前文件不能为空!");
			return fileUploadEntity;
		}
		// 得到文件的完整路径
		path = file.getOriginalFilename();

		// 构建文件目录
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		// 得到文件的大小
		size = file.getSize();
		if ("".equals(path) || size == 0) {
			fileUploadEntity.setUploadErrorMessage("文件有错误:大小为0!");
			return fileUploadEntity;
		}

		// 得到去除路径的文件名
		String t_name = path.substring(path.lastIndexOf("\\") + 1);
		// 得到文件的扩展名(无扩展名时将得到全名)
		String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
		
		if(!_ALLOWED_FILE_TYPE.containsKey(t_ext)){
			fileUploadEntity.setUploadErrorMessage("请上传以下类型的文件:<br>" + _ALLOWED_FILE_TYPE.keySet().toString());
			return fileUploadEntity;
		}

		long now = System.currentTimeMillis();
		// 根据系统时间生成上传后保存的文件名
		String prefix = String.valueOf(now);
		
		//时间数字文件名称
		String filename = prefix + "." + t_ext;
		FileOutputStream out = null;
		
		// 配置文件读取文件路径：F:\\upload\\
		String fileUploadpath = ResourceBundle.getBundle("application").getString("fileUploadPath");

		try {
			//保存到目录开始
			if ("fileDir".equals(chooseWay)) {
				File tempPath = new File(fileUploadpath);
				if (!tempPath.exists()) {
					tempPath.mkdirs();
				}
				out = new FileOutputStream(new File(fileUploadpath + filename));
				IOUtils.copy(file.getInputStream(), out);
			}
			
			//生成文件实体
			fileUploadEntity.setId(Identities.uuid());
			fileUploadEntity.setOldFileName(t_name.substring(0,t_name.length() - t_ext.length() - 1)+ "." + t_ext);
			fileUploadEntity.setNewFileName(filename);
			if ("fileDir".equals(chooseWay)) {
				fileUploadEntity.setFileAddress(fileUploadpath + filename);
			}
			fileUploadEntity.setFileSize(String.valueOf(size));
			DictEntry fileType = new DictEntry(t_ext);
			fileUploadEntity.setFileType(fileType);
			fileUploadEntity.setUploadTime(new Date());
			OperatorDetails user = UserContext.getUser();
			SysUser sysUser = new SysUser();
			sysUser.setId(user.getUserid());
			fileUploadEntity.setSysUser(sysUser);
			fileUploadEntity.setCountNum(0);
			if ("dataBase".equals(chooseWay)) {
				fileUploadEntity.setContents(file.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(null != out){
				out.close();
			}
		}

		return fileUploadEntity;
	}
}

