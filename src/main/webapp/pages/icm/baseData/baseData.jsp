<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<%@ include file="/pages/kpi/strategyMap/processManage.jsp"%>
<%@ include file="/pages/icm/standard/standardManage.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<script>
	
	var process_processManage_panelAndMenu = new process_processManage_panelAndMenu();
	var standard_standardManage_panelAndMenu = new standard_standardManage_panelAndMenu();
	/*定义容器类  */
	Ext.define('baseData_baseDataManage',{
						extend : 'Ext.container.Container',
						layout : 'border',
						defaults : {
							collapsible : true,
							split : true,
							animFloat : false,
							useSplitTips : true,
							collapseMode : 'mini'
						},
						height : FHD.getCenterPanelHeight(),
						renderTo : "FHD.icm.baseData.baseData${param._dc}",
						initComponent : function() {
						var me=this;
						var leftPanelTrees=Ext.create('Ext.panel.Panel', { 
							    title: 'Accordion Layout',     
							    width: 300,     
							    region:'west',
							    collapsible : true,
							    height: FHD.getCenterPanelHeight(),//500, 
							    layout:'accordion',     
							    defaults: { 
							    	collapsible : true,
							    }, 
							    layoutConfig: {        
							        titleCollapse: false,         
							        animate: true      
							    },     
							    items: [
              process_processManage_panelAndMenu.processTreeManage,
              standard_standardManage_panelAndMenu.icmStandardTreeManage,{ 
							        title: 'Panel 3',         
							        html: 'Panel content!'     
							    }]     
							});
						
						
						
						
						
						
						me.items=[leftPanelTrees];
						me.callParent(arguments);
						}
					});
						
	var baseData_baseDataManage=null;		
	Ext.onReady(function() {

		baseData_baseDataManage = Ext.create('baseData_baseDataManage');
		FHD.componentResize(baseData_baseDataManage, 0, 0);
	});
</script>
</head>
<body>
	<div id="FHD.icm.baseData.baseData${param._dc}"></div>
</body>
</html>