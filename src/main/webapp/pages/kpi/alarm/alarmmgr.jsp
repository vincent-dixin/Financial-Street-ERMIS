<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>告警列表</title>
<script type="text/javascript">
/***attribute start***/
var formwindow;
var queryUrl = __ctxPath+"/kpi/alarm/findalarmplanbysome.f";
var delUrl = __ctxPath+"/kpi/alarm/removealarmplan.f";
var viewRegionsUrl = __ctxPath+"/kpi/alarm/findalarmplanregionstoviewbyid.f";
var grid_panel;
var gridColums =[
	{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'name', sortable: true, flex:1.5,renderer:function(value,metaData,record,colIndex,store,view) { 
		return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";	
	}},
	{header: FHD.locale.get('fhd.common.type'), dataIndex: 'types', sortable: true,flex:1},
	{header: FHD.locale.get('fhd.alarmplan.form.desc'),dataIndex: 'descs', sortable: true, flex:2 }
];

var tbar =[//菜单项
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',id:'alarmadd',handler:edit, scope : this},'-',
			{text : FHD.locale.get('fhd.strategymap.strategymapmgr.edit'),id:'edit',iconCls: 'icon-edit',disabled:true,handler:edit, scope : this},'-'
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'del', handler:del, disabled :true, scope : this}  
		  ];
/***attribute end***/
/***function start***/
/*删除告警*/
function del(){//删除方法
	var selection = alarm_mgr_grid.getSelectionModel().getSelection();//得到选中的记录
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.common.makeSureDelete'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {//确认删除
				var ids = [];
				for(var i=0;i<selection.length;i++){
					ids.push(selection[i].get('id'));
				}
				FHD.ajax({//ajax调用
					url : delUrl,
					params : {
						ids:ids.join(',')
					},
					callback : function(data){
						if(data&&data.success){//删除成功！
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							alarm_mgr_grid.store.load();
						}else{
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.alarmplan.prompt.delerror'));
						}
					}
				});
			}
		}
	});
}

/*编辑告警*/
function edit(button){
	var selections = alarm_mgr_grid.getSelectionModel().getSelection();
	var length = selections.length;
	if(length>=2){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.alarmplan.prompt.editAlone'));
		return ;
	}
	var selection = selections[0];//得到选中的记录
	formwindow = new Ext.Window({
		constrain:true,
		layout:'fit',
		iconCls: 'icon-edit',//标题前的图片
		modal:true,//是否模态窗口
		collapsible:true,
		scroll:'auto',
		width:800,
		height:550,
		maximizable:true,//（是否增加最大化，默认没有）
		autoLoad:{ url: 'pages/kpi/alarm/alarmedit.jsp',nocache:true,scripts:true}
	});
	formwindow.show();
	if(button.id=='alarmadd'){
		formwindow.setTitle(FHD.locale.get('fhd.common.add'));
		isAdd = true;
	}else{
		formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
		formwindow.initialConfig.id = selection.get('id');
		isAdd = false;
	}
}


function setstatus(){//设置你按钮可用状态
	alarm_mgr_grid.down('#del').setDisabled(alarm_mgr_grid.getSelectionModel().getSelection().length === 0);
	alarm_mgr_grid.down('#edit').setDisabled(alarm_mgr_grid.getSelectionModel().getSelection().length === 0);
}

function getRegionsValues(id){
	FHD.ajax({//ajax调用
		url : viewRegionsUrl,
		params : {
			id:id
		},
		callback : function(data){
			if(data&&data.success){
				tpl = Ext.create('Ext.Template', 
						'<table align="center" height="240" valign="middle" width="100%">',
		       			'<tr><td width="30%" style="text-align: center;font-size: 15;border: 2px #99bbe8 solid;">等级</td><td style="text-align: center;font-size: 15;border: 2px #99bbe8 solid;">区间</td></tr>',
		       			'<tr><td width="30%" style="text-align: center;border: 2px #99bbe8 solid;"><img src="'+__ctxPath+'/images/icons/traffic_3_lrg.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;{high}</td></tr>',
		       			'<tr><td style="text-align: center;border: 2px #99bbe8 solid;"><img src="'+__ctxPath+'/images/icons/traffic_5_lrg.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;{mid}</td></tr>',
		       			'<tr><td style="text-align: center;border: 2px #99bbe8 solid;"><img src="'+__ctxPath+'/images/icons/traffic_7_lrg.gif"></td><td style="text-align: center;border: 2px #99bbe8 solid;">&nbsp;{low}</td></tr>',
		       			'</table>'
		            );
				var detailPanel = Ext.getCmp('detailPanel');
				tpl.overwrite(detailPanel.body,data.regions);
			}
		}
	});
}

/***function end***/
/***Ext.onReady start***/

	
Ext.onReady(function(){
	Ext.QuickTips.init(); 
	/*左侧告警grid*/
	
	alarm_mgr_grid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
		border:false,
		region: 'west',
		width:1000,
		url: queryUrl,//调用后台url
		height:FHD.getCenterPanelHeight(),//高度为：获取center-panel的高度
		cols:gridColums,//cols:为需要显示的列
		tbarItems:tbar,
		listeners:{itemdblclick:edit}//双击执行修改方法
		
	});
	alarm_mgr_grid.store.on('load',setstatus);
	alarm_mgr_grid.on('selectionchange',setstatus);
	alarm_mgr_grid.getSelectionModel().on('focuschange', function(model) {
		var selections = model.getSelection();
		if(selections.length>0){
			var id = model.getSelection()[0].get('id');
			getRegionsValues(id);
		}
	});

	/*右侧详细信息面板*/
	center = Ext.create('Ext.panel.Panel',{
			id: 'detailPanel',
			region: 'center',
			width:10,
			height:FHD.getCenterPanelHeight(),
			autoScroll: true,
			bodyPadding: '5 5 0',
			bodyStyle: {
				background: '#ffffff'
			},
			html: '',
			tbar:[{ height:21 ,xtype: 'tbtext', text: FHD.locale.get('fhd.alarmplan.form.alarmrange')}],
			bbar:[{ height:21 ,xtype: 'tbtext'}]
	});

	main = Ext.create('Ext.panel.Panel',{
		renderTo:"alarmmgr${param._dc}",
		height: FHD.getCenterPanelHeight(),
		layout: 'border',
		items: [
			alarm_mgr_grid
			,center
		]
	});
	main.on('resize',function(p){
		alarm_mgr_grid.setWidth(p.getWidth()*0.7);
		center.setWidth(p.getWidth()*0.3);
		alarm_mgr_grid.setHeight(p.getHeight());
		center.setHeight(p.getHeight());
	});
	FHD.componentResize(main,0,0); 	
	
});
/***Ext.onReady end***/

</script>
</head>
<body>
<div id='alarmmgr${param._dc}'></div>
</body>
</html>