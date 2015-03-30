<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
/*定义容器类  */
Ext.define('FHD_icm_assess_assessPlanContainer',{
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
					//width : FHD.getCenterPanelWidth(),
					autoWidth:true,
					renderTo : 'FHD.icm.assess.assessPlanContainerManager${param._dc}',
					items : new Array(),
					leftContainer:null,
					centerContainer: null,//右侧面板
					initComponent : function() {
					 var me = this;
				     me.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
					 me.items = [me.centerContainer];
					 me.callParent(arguments);
					},
					setCenterContainer:function(url){
						var me=this;
						me.centerContainer=Ext.create('Ext.container.Container',{
							region:'center',
							autoLoad:{
								url:url,
								scripts:true,
							}
						});
					}
				
				});
	
	var fhd_icm_assess_assessPlanContainer=null
    Ext.onReady(function() {
    	fhd_icm_assess_assessPlanContainer = Ext.create('FHD_icm_assess_assessPlanContainer');
	  FHD.componentResize(fhd_icm_assess_assessPlanContainer,0,0);
});
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanContainerManager${param._dc}'></div>
</body>
</html>