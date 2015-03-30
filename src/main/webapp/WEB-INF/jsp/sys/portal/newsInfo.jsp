<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${news.title}</title>
</head>
<body>
	<table width="800px" border="0" cellspacing="0" cellpadding="0" align="center" style="">
		<tr align="center">
			<td height="100px"><font size="4">${news.title}</font></td>
		</tr>
		<tr>
			<td align="left">发布人：${news.sysUser.realname}&nbsp;&nbsp;&nbsp;&nbsp;发布时间：<fmt:formatDate value='${news.addTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
		</tr>
		<tr>
			<td style="height:1px;width:100%;background:#000000;overflow:hidden;"></td>
		</tr>
		<tr>
			<td>${news.contents}</td>
		</tr>
	</table>
</body>
</html>
