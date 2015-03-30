<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow: hidden;">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>任务</title>
		<script type="text/javascript">
			var tabPanel;
			var businessId="${businessId}";
			Ext.onReady(function(){
				jQuery("#close").click(function(){
					closeWindow();
				});
				tabPanel = new Ext.TabPanel({
			        renderTo: "tabPanel",
			        activeTab: 0,
			        width:'auto',
			        height:jQuery(window).height()-Ext.get("button").getHeight()*1.5,
			        defaults:{autoScroll: false},
			        items:[
						{
						    title: '执行',
						    html:"<iframe src='${ctx}/${link1}' scrolling='yes' frameBorder='0' height='100%' width='100%'></iframe>"
						},
						{
						    title: '工作流日志',
						    html:"<iframe src='${ctx}/jbpm/processInstance/processInstanceInfoShow.do?id=${businessId}' scrolling='yes' frameBorder='0' height='100%' width='100%'></iframe>"
						},{
						    title: '工作流状态',
						    html:"<iframe src='${ctx}/${link2}' scrolling='yes' frameBorder='0' height='100%' width='100%'></iframe>"
						}
			        ]
			    });
	  			Ext.EventManager.onWindowResize(function(width ,height){
	  				tabPanel.setWidth(width);
	  				tabPanel.setHeight(height-Ext.get("button").getHeight());
				});
			});
		</script>
	</head>
	<body style="overflow: hidden;">
		<div id="tabPanel"></div>
		<table id="button" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th align="center">
					<input id="close" name="close" type="button" value="关闭" class="fhd_btn"/>
				</th>
			</tr>
		</table>
	</body>
</html>