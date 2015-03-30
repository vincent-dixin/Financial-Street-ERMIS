package com.fhd.fdc.commons.orm.sql;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 使用Velocity生成sql的工具类.
 * 
 * @author 胡迪新
 */
public class SqlBuilder {

	static {
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("Exception occurs while initialzie the velociy: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <pre>
	 * getSql:根据唯一标识从xml中取出命名SQL
	 * 
	 * Map<String, String> map = new HashMap<String, String>();
	 * map.put("name", "xiaoer");
	 * System.out.println(SqlBuilder.getSql("select_user", map));
	 * 
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param sqlId
	 * @param model
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public static String getSql(String sqlId, Map<String, ?> model) {
		try {
			String sqlTemplate = DialectSQL.getString(sqlId);
			VelocityContext velocityContext = new VelocityContext(model);
			StringWriter result = new StringWriter();
			Velocity.evaluate(velocityContext, result, "", sqlTemplate);
			return result.toString().trim();
		} catch (IOException e) {
			throw new RuntimeException("Parse sql failed", e);
		}
	}
	
	
}
