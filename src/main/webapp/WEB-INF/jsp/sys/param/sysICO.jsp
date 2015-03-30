<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style type="text/css">
		.fhd_form_table {
			border-top-width: 1px;
			border-right-width: 1px;
			border-left-width: 1px;
			border-top-style: solid;
			border-right-style: solid;
			border-bottom-style: solid;
			border-left-style: solid;
			border-right-color: #ccdff0;
			border-left-color: #ccdff0;
		 	border-bottom-width: 1px;
			border-bottom-color: #ccdff0;
			border-top-color: #ccdff0;
		}
	</style>
</head>
<body style="text-align: center;">
	<br/>
	<div style="text-align: center; width: 100%">
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_result">
			<tr>
				<th style="text-align: center; width: 100">项目</th>
				<th style="text-align: center;width: 500" >图片</th>
				<th style="text-align: center;width: 100" >上传文件<br/>名称</th>
				<th style="text-align: center;width: 100" >上传文件<br/>宽*高</th>
				<th style="text-align: center;width: 100" >操作</th>
			</tr>
			<tr height="100">
				<th>公司名称图片</th>
				<td style="text-align: center;"><img width="402" height="39" src="${ctx}/images/sysDyn/name.gif"/></td>
				<td >name.gif</td>
				<td >402*39</td>
				<td>
					<a id="name.gif" href='javascript:void(0)' onclick="showChangePic(this);">上传</a>
				</td>
			</tr>
			<tr height="100">
				<th>公司标志图片</th>
				<td style="text-align: center;"><img width="66" height="66" src="${ctx}/images/sysDyn/logo.gif"/></td>
				<td>logo.gif</td>
				<td >66*66</td>
				<td>
					<a id="logo.gif" href='javascript:void(0)' onclick="showChangePic(this);">上传</a>
				</td>
			</tr>
			<tr height="100">
				<th>首页banner图片</th>
				<td style="text-align: center;"><img width="560" height="23" src="${ctx}/scripts/ext-3.3.0/resources/images/default/banner.jpg"/></td>
				<td>banner.jpg</td>
				<td >1358*60</td>
				<td>
					<a id="banner.jpg" href='javascript:void(0)' onclick="showChangePic(this);">上传</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="text-align: center">
		<h3><font color="red">注：设置将在下次登录时有效</font></h3>
		<h3><font color="red">注：请上传'jpg'、'jpeg'、'gif'格式图片</font></h3>
	</div>
</body>
<script type="text/javascript">
	function showChangePic(dom){
		FHD.openWindow('修改系统图片', 700, 262, '${ctx}/sys/param/showSysPicImport.do?name='+dom.id);
	}
	//刷新页面--弹出窗口调用
	function reloadWin(){
		window.location.reload();		
	}
</script>
</html>