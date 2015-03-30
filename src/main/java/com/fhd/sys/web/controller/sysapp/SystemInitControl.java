package com.fhd.sys.web.controller.sysapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.dao.sysapp.SysteminitDAO;

/**
 * 
 * ClassName:SystemInitControl Function: 实现系统的数据初始化 Reason:
 * 在产品的安装后，系统要在数据库中装载针对某公司必要数据，来支持系统的运行
 * SystemInitControl中的数据导入方法所获取的数据库联接都是由环境中所得，非JDBC生成，要想实现数据的初化，所有的请求要跳过安全过滤（安全方法自己做验证）
 * 
 * @author 万业
 * @version
 * @since Ver 1.1
 * @Date 2011 2011-6-10 下午03:30:02
 * 
 */
@Controller
public class SystemInitControl {
	
	@Autowired
	private SysteminitDAO o_systeminitDAO;
	
	@RequestMapping(value="/sys/sysapp/systeminit.do")
	public String systeminit(){
		return "/sys/sysapp/systeminit";
	}
	@ResponseBody
	@RequestMapping(value = "/sys/sysapp/systeminit/sqlserver.do")
	public String sendMail(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<String> sqlList = new ArrayList<String>();
	
		InputStream sqlFileIn = SystemInitControl.class.getResourceAsStream("/systeminit/testsysteminit_sqlserver.sql");
		// 将SQL脚本产生为list<String>

		StringBuffer sqlSb = new StringBuffer();
		byte[] buff = new byte[1024];
		int byteRead = 0;
		while ((byteRead = sqlFileIn.read(buff)) != -1) {
			sqlSb.append(new String(buff, 0, byteRead, "utf-8"));
		}

		String[] sqlArr = sqlSb.toString().split(
				"(;\\s*\\r\\n)|(;\\s*\\n)");

		// 替换注释
		    for (int i = 0; i < sqlArr.length; i++) {
		     if (sqlArr[i].indexOf("--") > -1 || sqlArr[i].indexOf("/") > -1) {
		      //sqlArr[i] = sqlArr[i].replaceFirst("--.*$", "").replaceAll("/\\*.*\\*/", "").trim();
		      sqlArr[i] = sqlArr[i].replaceAll("(--.*$)|(--.*\\n)|(--.*\\r\\n)", "").replaceAll("/\\*(.|\\n|\\r\\n)*\\*/", "").trim();
		     }
		    }

		for (int i = 0; i < sqlArr.length; i++) {
				sqlList.add(sqlArr[i]);
		}
		
		o_systeminitDAO.initSystemData(sqlList);


		return "data init successfully!!";
	}



}
