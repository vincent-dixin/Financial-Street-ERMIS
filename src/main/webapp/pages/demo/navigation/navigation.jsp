<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导航组件</title>
<script type="text/javascript">
/***attribute start***/
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	
	var navigationBar = Ext.create('Ext.scripts.component.NavigationBars');
	
	var nav = {
			xtype:'box',
			height:40,
			style : 'border-left: 1px  #99bce8 solid;',
			html:'<div id="gatherresulttable2NavDiv" class="navigation"></div>',
            listeners : {
            	afterrender: function(){
            		navigationBar.renderHtml('gatherresulttable2NavDiv', 'eda8ffeab0da4159be0ff924108e3883MB0011', '', 'sm');
            	}
            }
		};
	
	Ext.create('Ext.panel.Panel',{
		renderTo: 'navigationDemo',
		height : 500,
		border:false,
		items: [nav]
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='navigationDemo'></div>
</body>
</html>