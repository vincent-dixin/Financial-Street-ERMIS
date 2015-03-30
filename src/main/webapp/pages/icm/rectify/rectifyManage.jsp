<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
Ext.define('rectify_rectifyManage_panelAndMenu',{
	extend : 'Ext.container.Container',
	layout : 'border',
	defaults : {
		collapsible : true,
		split : true,
		animFloat : false,
		useSplitTips : true,
		collapseMode : 'mini'
	},
	items : new Array(),
	height : FHD.getCenterPanelHeight(),
	autoWidth:true,
	renderTo : "FHD.rectify.rectifyManage${param._dc}",
	gridPanel:null,
	initComponent : function(){
		
		var me=this;
		
		me.gridPanel = Ext.create('Ext.container.Container', {
			 region : 'center',
			 height:100,
		     autoLoad : {
			 url : 'pages/icm/rectify/rectifyList.jsp',
			 scripts : true
		     }
		});
		me.items = [me.gridPanel];
		me.callParent(arguments);
	}
	
});

var rectify_ManagerView=null;

Ext.onReady(function() {

	rectify_ManagerView = Ext.create('rectify_rectifyManage_panelAndMenu');
	 FHD.componentResize(rectify_ManagerView, 0, 0);
});

</script>


</head>
<body>
	<div id='FHD.rectify.rectifyManage${param._dc}'></div>
</body>

</html>