<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国际化设置</title>
<script type="text/javascript">
/***attribute start***/

var objectType='';
var loadable=true;//解决重复load冲突的问题
var i18nTree, i18nqueryUrl = 'sys/i18n/findI18nAll.f';
var i18nGridQueryUrl = 'sys/i18n/findI18nPage.f';
var i18nSaveUrl = 'sys/i18n/saveI18n.f';
var i18nDelUrl = 'sys/i18n/deleteI18n.f';
var i18nFormPanel;
var i18nManPanel;
var i18nGrid;

Ext.define('demopanel',{
	extend:'Ext.panel.Panel',
	border:false,
	autoScroll:false,
	height:FHD.getCenterPanelHeight(),
	region: 'center',
	width:100
});
/***attribute end***/

/***function start***/
//解决重复load不同步的问题
function doLoad(records, operation, success){
	loadable=true;
}

function add(){//新增方法
	var r = Ext.create('Test');
	if(objectType == 'quanbu_1'){
		objectType = '';
	}
	r.data.objectType = objectType;
	i18nGrid.store.insert(0, r);
	i18nGrid.editingPlugin.startEditByPosition({row:0,column:0});
} 
function save(){//保存方法
	var rows = i18nGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
		jsonArray.push(item.data);
	});
	FHD.ajax({
		url : i18nSaveUrl,
		params : {
			modifiedRecord:Ext.encode(jsonArray)
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				i18nGrid.store.load();
			}
		}
	})
	i18nGrid.store.commitChanges();
}
function del(){//删除方法
	var selection = i18nGrid.getSelectionModel().getSelection();
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.common.makeSureDelete'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				debugger;
				var ids = [];
				for(var i=0;i<selection.length;i++){
					ids.push(selection[i].get('id'));
				}
				FHD.ajax({
					url : i18nDelUrl,
					params : {
						ids:ids.join(',')
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							i18nGrid.store.load();
						}
					}
				});
			}
		} 
	});
}

function onchange(){//设置你按钮可用状态
	i18nGrid.down('#i18nMaDel').setDisabled(i18nGrid.getSelectionModel().getSelection().length === 0);
	i18nGrid.down('#i18nManSave').setDisabled(i18nGrid.store.getModifiedRecords().length === 0);
}
/***function end***/

/***Ext.onReady start***/
Ext.onReady(function(){

	
	Ext.define('Test', {//定义model
	    extend: 'Ext.data.Model',
	   	fields: [{name: 'id', type: 'string'},
		        {name: 'objectType', type: 'string'},
		        {name: 'objectCn', type:'string'},
		        {name: 'objectEn', type:'string'}]
	});
	
	i18nTree = Ext.create('FHD.ux.TreePanel',{
		useArrows: true,
        rootVisible: false,
        split: true,
        width:220,
        collapsible : true,
        region: 'west',
        multiSelect: true,
        rowLines:false,
        singleExpand: false,
        checked: false,
		url: i18nqueryUrl,//调用后台url
		height:FHD.getCenterPanelHeight(),
		listeners : {
 			'itemclick' : function(view,re){
				var form = i18nFormPanel.getForm();
				objectType = re.data.id;
				i18nGrid.store.url = i18nGridQueryUrl + "?objectType=" + objectType;
				i18nGrid.down('searchfield').setValue("");
				i18nGrid.down('searchfield').triggerCell.item(0).setDisplayed(false);
				i18nGrid.down('searchfield').onTrigger1Click();
	  			i18nGrid.store.load();
				
  			}
 		}
	});

	
	i18nGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		url: i18nGridQueryUrl,
		cols:[
			{header: '所属模块', dataIndex: 'objectType', sortable: true, flex : 1,editor: {allowBlank: false}},
			{header: '关键值', dataIndex: 'objectKey', sortable: true, flex : 1,editor: {allowBlank: false}},
			{header: '中文', dataIndex: 'objectCn', sortable: true, flex : 1,editor:{}},
			{header: '英文', dataIndex: 'objectEn', sortable: true, flex : 1,editor:{}}
			],
		tbarItems:[
				{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add', handler:add, scope : this},{xtype : 'tbspacer'},
				{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',id:'i18nManSave',handler:save, disabled : true, scope : this},{xtype : 'tbspacer'},
				{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'i18nMaDel', handler:del, disabled : true, scope : this}  
			]
	});
	
	i18nGrid.on('selectionchange',onchange);//选择记录发生改变时改变按钮可用状态

	i18nGrid.store.on('beforeload', function (store, options) {
		var new_params = {objectType:objectType};
	    Ext.apply(i18nGrid.store.proxy.extraParams, new_params);
    });

    i18nFormPanel = Ext.create('Ext.form.Panel',{
    	region:'center',
    	layout: {
	        type: 'fit'
	    },
    	items: [ 
			i18nGrid
    	]
	});
	
	var panelDemopanel=Ext.create('demopanel',{
		items:[i18nFormPanel]
	});
	
	i18nManPanel = Ext.create('Ext.panel.Panel',{
	    renderTo: 'i18nMan${param._dc}', 
	    demopanel:panelDemopanel,
        height:FHD.getCenterPanelHeight(),
        border:false,
		layout: {
	        type: 'border',
	        padding: '0 0 5	0'
	    },
	    defaults: {
            border:true
        },
	    items:[i18nTree,i18nFormPanel]
	});
	FHD.componentResize(i18nManPanel,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='i18nMan${param._dc}'></div>
</body>
</html>