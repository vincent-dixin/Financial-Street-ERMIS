/**
 * Messages.java
 * com.fhd.fdc.commons.dao.sql
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-28 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
 */

package com.fhd.fdc.commons.orm.sql;

import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 
 * ClassName:DialectSQL
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2010	2010-8-14		下午12:05:57
 *
 * @see
 */
public final class DialectSQL {

	private static final StringBuilder XML_FILE_NAME = new StringBuilder(
			"namingSql/"); 

	private static Document DOCUMENT = null;
	static {
		String dialect = ResourceBundle.getBundle("application").getString(
				"hibernate.dialect");
		dialect = StringUtils.lowerCase(StringUtils.substringAfterLast(dialect,
				"."));
		if (StringUtils.indexOf(dialect, "oracle") > -1) {
			dialect = "oracle";
		} else if (StringUtils.indexOf(dialect, "sqlserver") > -1) {
			dialect = "sqlserver";
		} else if (StringUtils.indexOf(dialect, "mysql") > -1) {
			dialect = "mysql";
		} else if (StringUtils.indexOf(dialect, "db2") > -1) {
			dialect = "db2";
		} else if (StringUtils.indexOf(dialect, "h2") > -1) {
			dialect = "h2";
		} else if (StringUtils.indexOf(dialect, "hsql") > -1) {
			dialect = "hsql";
		} else if (StringUtils.indexOf(dialect, "sapdb") > -1) {
			dialect = "sapdb";
		} else if (StringUtils.indexOf(dialect, "sysbase") > -1) {
			dialect = "sysbase";
		}
		SAXReader reader = new SAXReader();
		ClassLoader loader = DialectSQL.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(XML_FILE_NAME.append(dialect).append(".xml").toString());
		try {
			DOCUMENT = reader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private DialectSQL() {}

	public static String getString(String key) {
		
		Node node = DOCUMENT.selectSingleNode("//sql[@id='" + key + "']");
		return node.getText();
	}
	

	
}