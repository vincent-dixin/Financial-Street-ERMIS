<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程测试</title>
<script type="text/javascript">
	/***attribute start***/
	var grid,isAdd,formwindow,viewwindow;
	var saveUrl = '/test/submitA.f';//新增保存url
	var queryUrl = 'test/testProcessInstanceList.f'; //查询url
	var gridColums =[
		{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'name', sortable: true, width: 60,flex : 1}
	];
	var tbar =[//菜单项
		{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',id:'save',handler:save, scope : this}
	];
	/***attribute end***/
	/***function start***/
	function save(){
		FHD.ajax({
		    url: __ctxPath + saveUrl,
		    callback: function (data) {
		        if (data) {
		            grid.store.load();
		            Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
		        }else{
		        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
		        }
		    }
		});
	}
	function close(){
		viewwindow.close();
	}
	function setStatus(){//设置你按钮可用状态
	}
	/***function end***/
	/***Ext.onReady start***/
	Ext.onReady(function(){
		Ext.QuickTips.init(); 
		grid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
			renderTo: 'demo', //渲染到id为demo的div里
			url: queryUrl,//调用后台url
			height:FHD.getCenterPanelHeight()-3,//高度为：获取center-panel的高度
			cols:gridColums,//cols:为需要显示的列
			tbarItems:tbar,
			listeners:{itemdblclick:save}//双击执行修改方法
		});
		grid.store.on('load',setStatus);
		grid.on('selectionchange',setStatus);
		FHD.componentResize(grid,220,5);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度 	
		
	});
	/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='demo'></div>
</body>
</html>