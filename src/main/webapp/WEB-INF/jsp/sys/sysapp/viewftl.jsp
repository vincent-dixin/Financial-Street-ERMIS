<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
</head>
<body>

<div>
<h3>
&nbsp;&nbsp;${type}
</h3>
</div>
<hr/>
	<div style="text-align: center">
	<textarea rows="25" cols="2" disabled="disabled" >${fileContent}</textarea>
	</div>
	<hr/>
	<div align="right">
	<input type="button" value="关闭窗口" onclick="closeWindow();" class="fhd_btn"/>
	</div>
</body>
<script type="text/javascript">	

	</script>
</html>