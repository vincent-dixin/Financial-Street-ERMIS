<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>计划任务日志列表</title>
<script type="text/javascript">
/***attribute start***/
var param = formwindow.initialConfig;//父窗口属性
var mainPanel;
var planLogGrid;
var queryUrl = 'sys/st/queryPlanDealLogPage.f?planEmpId=' + param.id; //查询分页url
var viewUrl = 'sys/st/viewPlan.f'//查看url


/***attribute start***/
var planLogGridColums =[
	{header: FHD.locale.get('fhd.sys.planEdit.name'), dataIndex: 'planName', sortable: true, flex : 1},
	{header: FHD.locale.get('fhd.sys.planDealLogMan.log'), dataIndex: 'dealMeasure', sortable: true, flex : 1},
	{header: FHD.locale.get('fhd.sys.planDealLogMan.time'), dataIndex: 'dealTime', sortable: true, flex : 1},
	{header: FHD.locale.get('fhd.sys.planEdit.status'), dataIndex: 'estatus', sortable: true, flex : 1,
		renderer:function(value,metaData,record,colIndex,store,view) { 
		if(record.get('status') == 1){
			//启用
			return "<image src='images/icons/start.ico' width='16' height='16'/></a>";
					
		}else{
			//停用
			return "<image src='images/icons/stop.ico' width='16' height='16'/></a>";
		}
	}},
	{header: FHD.locale.get('fhd.sys.planEdit.createBy'), dataIndex: 'planTempName', sortable: true, flex : 1}
];

/***Ext.onReady start***/
Ext.onReady(function(){
	Ext.QuickTips.init(); 
	planLogGrid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
		url: queryUrl,//调用后台url
		cols:planLogGridColums,//cols:为需要显示的列
		checked:false
	});
	
	mainPanel = Ext.create('Ext.panel.Panel',{
	    renderTo: 'planDealLog${param._dc}', 
	    width:formwindow.width - 10,
		height:formwindow.height - 40,
		layout: {
	        type: 'fit'
	    },
	    items:[planLogGrid]
	});
	
	formwindow.on('resize',function(me){
		mainPanel.setWidth(me.getWidth()-10);
		mainPanel.setHeight(me.getHeight()-40);
	})
});
FHD.componentResize(mainPanel,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='planDealLog${param._dc}'></div>
</body>
</html>