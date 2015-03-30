<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树选择布局组件</title>
<script type="text/javascript">
/***attribute start***/

/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
    var p1 = Ext.create('Ext.panel.Panel',{
    	navigatorTitle:'基本信息',	//步骤导航的名称，必须
    	border: false,
    	html:'aa',
    	last:function(){
    		alert('save1');
    	}
    });
    var p2 = Ext.create('Ext.panel.Panel',{
    	navigatorTitle:'额外信息',
    	border: false,
    	html:'bb',
    	last:function(){
    		alert('save2');
    	}
    });
    var p3 = Ext.create('Ext.panel.Panel',{
    	navigatorTitle:'更多信息',
    	border: false,
    	html:'cc',
    	last:function(){
    		alert('save3');
    	}
    });

    var basicPanel = Ext.create('FHD.ux.layout.StepNavigator',{
    	renderTo: 'treedemo',
    	items:[p1,p2,p3],
    	undo : function(){
    		alert('返回');
    	}
    });
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='treedemo'></div>
</body>
</html>