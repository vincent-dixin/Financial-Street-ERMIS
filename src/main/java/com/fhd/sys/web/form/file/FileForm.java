package com.fhd.sys.web.form.file;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {
	private String chooseWay;
	private String newFileName;
	private MultipartFile file;
	public String getChooseWay() {
		return chooseWay;
	}
	public void setChooseWay(String chooseWay) {
		this.chooseWay = chooseWay;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
