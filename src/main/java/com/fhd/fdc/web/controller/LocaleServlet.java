package com.fhd.fdc.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fhd.fdc.utils.CookieUtil;
import com.fhd.sys.dao.i18n.I18nDAO;
import com.fhd.sys.entity.i18n.I18n;

public class LocaleServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(LocaleServlet.class);
	private static final long serialVersionUID = 1L;

	/**
	 * 查询i18n实体
	 * 
	 * @author 金鹏祥
	 * @param session DB-Session
	 * @return I18n
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	private List<I18n> findI18nAll(Session session) {
		try {
			Criteria c = session.createCriteria(I18n.class);
			List<I18n> list = null;
			
			list = c.list();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}finally{
			session.close();
		}
		
		return null;
	}
	
	/**
	 * 取COOKIES文件KEY为i18n的value,如果没有则默认取中文
	 * 
	 * @author 金鹏祥
	 * @param req
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	private String getJson(HttpServletRequest req){
		String value = CookieUtil.getCookieValue("i18n", req);
		String json = "";
		if(value.equals(""))
			json = convert2Json("zh");
		else
			json = convert2Json(value);
		
		return json;
	}
	
	private String convert2Json(String locale) {
		int i = 0;
		String value = "";
		List<I18n> i18nList = null;
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();//SPRING-BEAN服务
		I18nDAO o_planDAO = applicationContext.getBean(I18nDAO.class);//任务功能数据服务
		Session session = o_planDAO.getSessionFactory().openSession();//HIBERNATE-SESSION服务
		i18nList = this.findI18nAll(session);
		StringBuilder sb = new StringBuilder();
		
		sb.append("GajaxLocale={\n");
		sb.append("    'LOCALE':'").append(locale).append("',\n");
		for (int l = i18nList.size(); i < l; ++i) {
			String key = i18nList.get(i).getObjectKey();
			if(locale.equals("zh")){
				value = i18nList.get(i).getObjectCn();
			}else if (locale.equals("en")){
				value = i18nList.get(i).getObjectEn();
			}
			
			String suffix = (i == l - 1) ? "};" : ",";

			if ((null == value) || ("".equals(value.trim()))
					|| ("null".equalsIgnoreCase(value.trim())))
				sb.append("    '").append(key).append("'").append(":")
						.append("null").append(suffix).append("\n");
			else
				sb.append("    '").append(key).append("'").append(":")
						.append("'").append(value).append("'").append(suffix)
						.append("\n");

		}

		return sb.toString();
	}
	
	private void printResource(HttpServletResponse response,
			String resourcesJson) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(resourcesJson);
		writer.flush();
		writer.close();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String json = this.getJson(req);
			System.out.println(json);
			printResource(resp, json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
