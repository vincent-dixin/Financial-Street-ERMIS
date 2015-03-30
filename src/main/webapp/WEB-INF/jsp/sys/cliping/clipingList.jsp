<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能裁剪</title>
<script type="text/javascript">
Ext.onReady(function(){
	var center = new Ext.TabPanel({
		renderTo: Ext.getBody(),
		region: 'center', // a center region is ALWAYS required for border layout
		id:'center-panel',
        height:430,
		minSize: 100,
		maxSize: 200,
		margins: '0 0 0 0',
		xtype: 'tabpanel',
		activeTab: 0,
		tabWidth:60,
		enableTabScroll : true,
		resizeTabs: true,
		frame:false,
		minTabWidth: 60,
		border: false,
		//plugins: new Ext.ux.TabCloseMenu(),
		items : [{
			id:'指标',
			title: '<span style="text-align:center;width:80%;">指标</span>',
			layout:'fit',
			autoWidth:true,
			iconCls: 'icon-storm',
			html:"<iframe width='100%' height='100%' scrolling='no' stype='overflow-y:hidden;' noresize='noresize' src='功能裁剪-基本信息.html' frameborder='0'></iframe>",
			closable:false
		},{
			id:'变量',
			title: '<span style="text-align:center;width:80%;">变量</span>',
			layout:'fit',
			autoWidth:true,
			iconCls: 'icon-brick',
			html:"<iframe width='100%' height='100%' scrolling='auto' stype='overflow-y:hidden;' noresize='noresize' src='' frameborder='0'></iframe>",
			closable:false
		},{
			id:'流程',
			title: '<span style="text-align:center;width:80%;">流程</span>',
			layout:'fit',
			autoWidth:true,
			iconCls: 'liucheng',
			html:"<iframe width='100%' height='100%' scrolling='no' stype='overflow-y:hidden;' noresize='noresize' src='' frameborder='0'></iframe>",
			closable:false
		},{
			id:'组织',
			title: '<span style="text-align:center;width:80%;">组织</span>',
			layout:'fit',
			autoWidth:true,
			iconCls: 'icon-group',
			html:"<iframe width='100%' height='100%' scrolling='no' stype='overflow-y:hidden;' noresize='noresize' src='' frameborder='0'></iframe>",
			closable:false
		},{
			id:'风险',
			title: '<span style="text-align:center;width:80%;">风险</span>',
			layout:'fit',
			autoWidth:true,
			iconCls: 'icon-stop',
			html:"<iframe width='100%' height='100%' scrolling='no' stype='overflow-y:hidden;' noresize='noresize' src='' frameborder='0'></iframe>",
			closable:false
		}]
	});
});

</script>
</head>
<body>
	
</body>
</html>