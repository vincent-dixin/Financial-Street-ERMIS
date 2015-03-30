/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.fdc.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.ResourceBundle;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;

import com.fhd.core.utils.DateUtils;

import sun.misc.BASE64Decoder;

/**
 * ClassName:LicenseControl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-11-14  上午11:33:48
 *
 * @see 	 
 */
@SuppressWarnings("restriction")
//@Component
public class LicenseControl implements InitializingBean {

	/**
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		String decrypt = "";
		try {
			File licenseFile = new File(ResourceBundle.getBundle("application").getString("licensePath") + "/license.key");
			licenseFile.getAbsolutePath();
			decrypt = decrypt(FileUtils.openInputStream(licenseFile));
			if(StringUtils.isEmpty(decrypt)) {
				throw new Exception("license 文件未找到");
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw new Exception("license 文件未找到");
			
		}
		
		
		String[] decrypts = StringUtils.split(decrypt,",");
		Date today = new Date();
		Date startDate = DateUtils.parse(decrypts[0]);
		Date endDate = DateUtils.parse(decrypts[1]);
		if(today.before(startDate)) {
			throw new Exception("license 信息不正确");
		}
		
		if(today.after(endDate)) {
			throw new Exception("license 已过期");
		}
		
	}

	
	/**
	 * 解密的方法
	 */
	public String decrypt(InputStream cryptograph) throws Exception {

		FileReader fr = new FileReader(ResourceBundle.getBundle("application").getString("licensePath") + "/license.dat");
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);// 建立BufferedReader对象，并实例化为

		String getPvKey = "";
		while (true) {
			String aLine = br.readLine();
			if (aLine == null)
				break;
			getPvKey += aLine;
		}
		BASE64Decoder b64d = new BASE64Decoder();
		byte[] keyByte = b64d.decodeBuffer(getPvKey);
		PKCS8EncodedKeySpec s8ek = new PKCS8EncodedKeySpec(keyByte);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(s8ek);

		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b1 = decoder.decodeBuffer(cryptograph);
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}
	
	
}

