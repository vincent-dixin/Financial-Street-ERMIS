/**
 * FileUploadControl.java
 * com.fhd.fdc.commons.web.controller.sys.file
 *
 * Function： 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-8 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.fdc.commons.security.OperatorDetails;
import com.fhd.fdc.utils.UserContext;
import com.fhd.fdc.utils.fileupload.FileUpload;
import com.fhd.sys.business.file.FileBO;
import com.fhd.sys.business.file.FileTypeTreeBO;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.file.FileUploadGridBO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.web.form.file.FileForm;
import com.fhd.sys.web.form.file.FileUploadForm;

/**
 * 文件Controller类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-9-8 Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = FileUploadForm.class)
public class FileUploadControl {

	@Autowired
	private FileUploadBO o_fileUploadBO;
	@Autowired
	private FileTypeTreeBO o_fileTypeTreeBO;
	@Autowired
	private FileUploadGridBO o_fileUploadGridBO;
	@Autowired
	private FileBO o_fileBO;
	
	/**
	 * 展开树节点
	 * treeLoader:
	 * 
	 * @author 杨鹏
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/fileupload/typeTreeLoader")
	public List<Map<String, Object>> typeTreeLoader(String id,Boolean canChecked){
		return o_fileTypeTreeBO.treeLoader(id, canChecked);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/fileupload/page")
	public Map<String, Object> page(String fileTypeId,String query, Integer limit, Integer start,String sort){
		JsonBinder binder = JsonBinder.buildNonNullBinder();
		List<Map<String, String>> sortList = binder.fromJson(sort, (new ArrayList<HashMap<String, String>>()).getClass());
		
		return o_fileUploadGridBO.page(fileTypeId,query, limit, start, sortList);
	}
	
	@ResponseBody
	@RequestMapping("/fileupload/listMap")
	public List<Map<String, Object>> listMap(String fileType,String oldFileName, String[] ids){
		return o_fileBO.listMap(fileType, oldFileName, ids);
	}

	@RequestMapping("/fileupload/upload")
	public void upload(FileForm form,HttpServletResponse response)throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = null;
		Map<String, Object> data = new HashMap<String, Object>();
		String flag="false";
		try{
			out = response.getWriter();
			String id = o_fileUploadBO.uploadFile(form.getChooseWay(),form.getNewFileName(), form.getFile());
			data.put("id", id);
			flag="true";
		} finally {
			data.put("success", flag);
			String json = JsonBinder.buildNonDefaultBinder().toJson(data);
			out.write(json);
			out.close();
		}
	}
	
	
	/**
	 * 查询所有的上传文件.
	 * @author 吴德福
	 * @param model
	 * @return String
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/fileUploadList.do")
	public String toQueryAllFile(Model model) throws Exception {
		model.addAttribute("fileUploadForm", new FileUploadForm());
		return "sys/file/fileUploadList";
	}
	/**
	 * 查询所有的上传文件. 王龙 4.14
	 * @param model
	 * @param start
	 * @param limit
	 * @param request
	 * @param oldFilename
	 * @param newFilename
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/file/queryfileUploadList.do")
	public Map<String, Object> queryAllFile(Model model, int start, int limit,String sort,String dir,
			HttpServletRequest request, String oldFilename, String newFilename,
			String username) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		Page<FileUploadEntity> page = new Page<FileUploadEntity>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);

		FileUploadEntity fileUploadEntity = new FileUploadEntity();
		fileUploadEntity.setOldFileName(request.getParameter("oldFileName"));
		fileUploadEntity.setNewFileName(request.getParameter("newFileName"));
		fileUploadEntity.setNotes(request.getParameter("username"));// 用户名
		//page = o_fileUploadBO.queryAllFileByCondandPage(page, fileUploadEntity, sort, dir);

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (FileUploadEntity fue : page.getList()) {

			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", fue.getId());
			row.put("oldFileName", fue.getOldFileName());
			row.put("newFileName", fue.getNewFileName());
			row.put("userName", fue.getSysUser().getUsername());
			row.put("uploadTime", DateUtils.formatLongDate(fue.getUploadTime()) );
			row.put("countNum", fue.getCountNum());
		
			datas.add(row);
		}

		reMap.put("totalCount", page.getFullListSize());
		reMap.put("datas", datas);
		return reMap;
	}
	/**
	 * 新增时保存文件Form.
	 * @author 吴德福
	 * @param model
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/fileUploadAdd.do")
	public String addFile(Model model) {
		model.addAttribute("fileUploadForm", new FileUploadForm());
		return "sys/file/fileUploadAdd";
	}
	/**
	 * 保存上传文件 王龙 4.14
	 * @param fileUploadForm
	 * @param file
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/file/fileUploadSave.do")
	public void saveFile(FileUploadForm fileUploadForm,
			@RequestParam MultipartFile file, HttpServletResponse response)
			throws Exception {

		PrintWriter out = null;
		out = response.getWriter();
		if (uploadFile(fileUploadForm.getChooseWay(),
				fileUploadForm.getAliasName(), file, response)) {
			out.write("true");
		} else {
			out.write("false");
		}
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	protected boolean uploadFile(String chooseWay, String aliasName,
			MultipartFile file, HttpServletResponse response) throws Exception {

		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "txt",
				"doc", "mp3", "wma", "m4a", "rar", "zip", "xls" };
		// 设置接收类型
		response.setContentType("text/html");
		// 设置字符编码为UTF-8, 统一编码，处理出现乱码问题
		response.setCharacterEncoding("UTF-8");

		// 初始化参数
		String path = "";
		long size = 0;
		// 当前文件为空
		if (file.getBytes() == null || file.getBytes().length == 0) {
			System.out.println("没有上传文件");
			return false;
		}
		// 得到文件的完整路径
		path = file.getOriginalFilename();
		System.out.println("得到文件的完整路径  path=" + path);
		// 构建文件目录
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		// 得到文件的大小
		// size = fileItem.getSize();
		size = file.getSize();
		System.out.println("得到文件的大小 size=" + size);
		if ("".equals(path) || size == 0) {
			System.out.println("没有上传文件");
			return false;
		}
		// 得到去除路径的文件名
		String t_name = path.substring(path.lastIndexOf("\\") + 1);
		// 得到文件的扩展名(无扩展名时将得到全名)
		String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
		// 拒绝接受规定文件格式之外的文件类型
		int allowFlag = 0;
		int allowedExtCount = allowedExt.length;
		for (; allowFlag < allowedExtCount; allowFlag++) {
			if (allowedExt[allowFlag].equals(t_ext))
				break;
		}
		if (allowFlag == allowedExtCount) {
			for (allowFlag = 0; allowFlag < allowedExtCount; allowFlag++) {
				System.out.println("请上传以下类型的文件:" + "*." + allowedExt[allowFlag]
						+ "   ");
			}
			return false;
		}
		long now = System.currentTimeMillis();
		// 根据系统时间生成上传后保存的文件名
		String prefix = String.valueOf(now);

		// 时间数字文件名称
		// String filename = t_name.substring(0, t_name.length() -
		// t_ext.length() - 1)+ prefix + "." + t_ext;
		String filename = prefix + "." + t_ext;

		// 配置文件读取文件路径：F:\\upload\\
		String fileUploadpath = ResourceBundle.getBundle("application")
				.getString("fileUploadPath");

		if ("fileDir".equals(chooseWay)) {
			System.out.println("----保存到目录开始----");
			try {
				FileOutputStream fout = new FileOutputStream(new File(
						fileUploadpath + filename));
				IOUtils.copy(file.getInputStream(), fout);
				System.out.println(filename
						+ "文件上传成功. 已保存为: "
						+ t_name.substring(0, t_name.length() - t_ext.length()
								- 1) + prefix + "." + t_ext + "   文件大小: "
						+ size + "字节<p />");
				System.out.println("----保存到目录结束----");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 保存文件到数据库中
		System.out.println("----保存到数据库开始----");
		FileUploadEntity fileUpload = new FileUploadEntity();
		fileUpload.setId(Identities.uuid());
		fileUpload.setOldFileName(t_name.substring(0,
				t_name.length() - t_ext.length() - 1)
				+ "." + t_ext);
		if (!"".equals(aliasName) && null != aliasName) {
			fileUpload.setNewFileName(aliasName + "." + t_ext);
		} else {
			fileUpload.setNewFileName(t_name.substring(0, t_name.length()
					- t_ext.length() - 1)
					+ "." + t_ext);
		}
		if ("fileDir".equals(chooseWay)) {
			fileUpload.setFileAddress(fileUploadpath + filename);
		}
		fileUpload.setFileSize(String.valueOf(size));
		DictEntry fileType = new DictEntry(t_ext);
		fileUpload.setFileType(fileType);
		fileUpload.setUploadTime(new Date());
		OperatorDetails user = UserContext.getUser();
		SysUser sysUser = new SysUser();
		sysUser.setId(user.getUserid());
		fileUpload.setSysUser(sysUser);
		fileUpload.setCountNum(0);

		if ("dataBase".equals(chooseWay)) {
			fileUpload.setContents(file.getBytes());
		}

		o_fileUploadBO.saveFileUpload(fileUpload);
		System.out.println("----保存到数据库结束----");

		return true;

	}
	/**
	 * 删除上传文件.
	 * @author 吴德福
	 * @param ids
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/fileUploadDel.do")
	public void fileUploadDel(String ids, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try {
			String[] roleids = ids.split(",");
			for (String fileUploadId : roleids) {
				if (StringUtils.isNotBlank(fileUploadId)) {

					o_fileUploadBO.removeFileById(fileUploadId);

				}
			}
			flag = "true";
			out.write(flag);
		} catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
		}
	}

	/**
	 * 下载文件.
	 * @author 吴德福
	 * @param fileUploadForm
	 * @param request
	 * @param response
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/download.do")
	public void downloadFile(String id, HttpServletResponse response)
			throws Exception {
		FileUploadEntity fileUpload = o_fileUploadBO.queryFileById(id);
		fileUpload.setCountNum(fileUpload.getCountNum() + 1);
		o_fileUploadBO.updateFile(fileUpload);
		String path = "";
		if (!"".equals(fileUpload.getFileAddress())
				&& null != fileUpload.getFileAddress()) {
			path = fileUpload.getFileAddress();
			response.setContentType(fileUpload.getFileType().getName());
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileUpload.getOldFileName(), "UTF-8"));
			// 获取欲下载的文件输入流
			FileInputStream fis = new FileInputStream(path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			FileCopyUtils.copy(bis, response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			response.setContentType(fileUpload.getFileType().getValue());
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileUpload.getOldFileName(), "UTF-8"));
			// 获取数据库中blob数据
			byte[] bytes = fileUpload.getContents();
			// 获取欲下载的文件输入流
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}

	/**
	 * 根据查询条件查询文件信息.
	 * @author 吴德福
	 * @param model
	 * @param fileUploadForm 上传文件form
	 * @param result
	 * @return String
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/fileUploadByCondition.do")
	public String queryFileUploadByCondition(Model model,
			FileUploadForm fileUploadForm, BindingResult result) {
		List<FileUploadEntity> fileUploadList = o_fileUploadBO.fineFileUploadEntityBySome(fileUploadForm);
		model.addAttribute("fileUploadList", fileUploadList);
		model.addAttribute("size", fileUploadList.size());
		return "sys/file/fileUploadList";
	}
	/**
	 * 下载文件模板.
	 * @author 吴德福
	 * @param request
	 * @param response
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/downloadFileTemplate.do")
	public void downloadFileTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = request.getParameter("fileName");
		System.out.println(fileName);
		// 配置文件读取文件路径：F:\\workspace\\firsthd-development-center\\web\\userfiles\\
		String path = ResourceBundle.getBundle("application").getString(
				"fileTemplatePath");

		if (null != fileName && !"".equals(fileName)) {
			// 得到文件的扩展名(无扩展名时将得到全名)
			String subfix = fileName.substring(fileName.lastIndexOf(".") + 1);

			String fileExt = "";
			if (null != subfix && !"".equals(subfix)) {
				if (!"doc".equals(subfix) && !"docx".equals(subfix)
						&& !"xls".equals(subfix) && !"xlsx".equals(subfix)
						&& !"txt".equals(subfix) && !"rar".equals(subfix)
						&& !"zip".equals(subfix)) {
					return;
				} else if ("doc".equals(subfix) || "docx".equals(subfix)) {
					fileExt = "word";
				} else if ("xls".equals(subfix) || "xlsx".equals(subfix)) {
					fileExt = "excel";
				} else if ("txt".equals(subfix)) {
					fileExt = "txt";
				} else if ("rar".equals(subfix) || "zip".equals(subfix)) {
					fileExt = "rar";
				}
			}

			response.setContentType(subfix);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			// 获取欲下载的文件输入流
			FileInputStream fis = new FileInputStream(path + fileExt + "\\"
					+ fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);
			FileCopyUtils.copy(bis, response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	/**
	 * 直接从数据库中下载文件；
	 * @author 陈燕杰
	 * @param id:文件实体在数据库中的ID；
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/downloadfromDB.do")
	public void downloadFileFromDB(String id, HttpServletResponse response)
			throws Exception {
		FileUploadEntity fileUpload = o_fileUploadBO.queryFileById(id);
		fileUpload.setCountNum(fileUpload.getCountNum() + 1);
		o_fileUploadBO.updateFile(fileUpload);
		response.setContentType(fileUpload.getFileType().getName());
		response.setHeader("Content-Disposition", "attachment;filename="
				+ URLEncoder.encode(fileUpload.getOldFileName(), "UTF-8"));
		// 获取数据库中blob数据
		byte[] bytes = fileUpload.getContents();
		// 获取欲下载的文件输入流
		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	
	
	/**
	 * 新增时保存文件Form.
	 * @author 吴德福
	 * @param model
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/file/fileUploadTest.do")
	public String fileUploadTest(Model model) {
		model.addAttribute("fileUploadForm", new FileUploadForm());
		//测试写死2个固定的已存在的id
		model.addAttribute("choosedFileIds", "034530C3413983F4BD802D77F127CAEB,0219B3B21A938FAD6EB8BAB2A53CDF5B");
		return "sys/file/fileUploadTest";
	}
	/**
	 * 测试上传多文件.
	 * @author 吴德福
	 * @param fileUploadForm
	 * @param response
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/file/fileUploadSaveTest.do")
	public void saveFileTest(FileUploadForm fileUploadForm,
			MultipartHttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		try{
			out = response.getWriter();
			List<MultipartFile> files = request.getFiles("file");
			for(MultipartFile file:files){
				if(StringUtils.isNotBlank(file.getOriginalFilename())){
					//this.uploadFile(fileUploadForm.getChooseWay(), fileUploadForm.getAliasName(), file, response);
					FileUpload.uploadFile(fileUploadForm.getChooseWay(), file);
				}
			}
			out.print("true");
		}catch (Exception e) {
			e.printStackTrace();
			out.print("false");
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
}
