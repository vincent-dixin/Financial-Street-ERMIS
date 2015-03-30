<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<script>
	parentWindow().reloadStore();//重新加载数据
	try{
		FHD.closeWindow();
	}catch(err){}

</script>