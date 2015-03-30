<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>在线帮助</title>
	
	
</head>
<body style="overflow:hidden;">
	<table width="100%" height="100%">
		<tr>
			<td width="20%">
				<div style="width: 100%;height: 100%" id="helpTree"></div>
			</td>
			<td>
				<iframe id="helpCatalogView" name="helpCatalogView" src="${ctx}/sys/helponline/helpCatalogView.do?catalogid=1" frameborder="0" width="100%" height="100%"></iframe>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		var helpTree;
		Ext.onReady(function() {
			helpTree = new FHD.ext.Tree('helpTree', '知识分类', '1',
					'${ctx}/sys/helponline/helpOnlineViewLoader.do', true);
		});
	</script>
</body>
</html>