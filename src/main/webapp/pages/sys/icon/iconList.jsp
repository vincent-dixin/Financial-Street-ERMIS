<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>icon列表</title>

<script type="text/javascript">
var icongrid;
var iconqueryUrl = 'sys/icon/iconList.f'; //查询url

var icongridColums =[
	{header: FHD.locale.get('fhd.sys.icon.preview'), dataIndex: 'css', sortable: true, width: 34,renderer:function(value,metaData,record,colIndex,store,view) { 
		metaData.tdAttr = 'data-qtip="'+value+'"';  
		var CSS=record.get('css');
	    return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='"+CSS+"'></div>";  
	}},
	{header: FHD.locale.get('fhd.sys.icon.name'), dataIndex: 'css', sortable: true, width: 60,flex : 1},
 	{header: FHD.locale.get('fhd.sys.icon.filename'), dataIndex: 'fileName', sortable: true, width: 60,flex : 1},
 	{header: FHD.locale.get('fhd.sys.icon.path'), dataIndex: 'path', sortable: true, width: 60,flex : 1}
];
Ext.onReady(function(){
	Ext.QuickTips.init(); 
	icongrid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
		renderTo: 'iconDiv', //渲染到id为demo的div里
		url: iconqueryUrl,//调用后台url
		checked:false,
		height:FHD.getCenterPanelHeight(),//高度为：获取center-panel的高度
		cols:icongridColums//cols:为需要显示的列
	});
FHD.componentResize(icongrid,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度	
});
 	
</script>
</head>
<body>

<div id="iconDiv"></div>
</body>
</html>
