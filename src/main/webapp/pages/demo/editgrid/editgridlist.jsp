<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可编辑的grid列表</title>
<script type="text/javascript">
/***attribute start***/
var grid;
var saveUrl = 'test/updateTestMvcBatch.f';//保存的url
var delUrl = 'test/deleteTestMvcs.f';//删除的url
var queryUrl = 'test/queryTestMvcList.f';//查询的url
var fieldstore = Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	data : [{'id' : 'zh-cn','name' : FHD.locale.get('fhd.pages.test.chinese')},{'id' : 'en','name' : FHD.locale.get('fhd.pages.test.english')}]
});
var treeComboxStore = Ext.create('Ext.data.TreeStore', {//treeCombox的store
	fields : ['text', 'id'],
	root : {text : FHD.locale.get('fhd.common.select'),id:'',expanded : true},
	proxy : {type : 'ajax',url : 'test/loadTestMvcTree.f'},
	autoload:false
});
/***attribute end***/
/***function start***/
function add(){//新增方法
	var r = Ext.create('Test');
    grid.store.insert(0, r);
    grid.editingPlugin.startEditByPosition({row:0,column:0}); 
} 
function save(){//保存方法
	var rows = grid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
		jsonArray.push(item.data);
	});
	FHD.ajax({
		url : saveUrl,
		params : {
			modifiedRecord:Ext.encode(jsonArray)
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				treeComboxStore.load();
				grid.store.load();
			}
		}
	})
	grid.store.commitChanges(); 
}
function del(){//删除方法
	var selection = grid.getSelectionModel().getSelection();
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.common.makeSureDelete'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				var ids = [];
				for(var i=0;i<selection.length;i++){
					ids.push(selection[i].get('id'));
				}
				FHD.ajax({
					url : delUrl,
					params : {
						ids:ids.join(',')
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							treeComboxStore.load();
							grid.store.load();
							
						}
					}
				});
			}
		}
	});
}
function cancel(){//取消方法
	formWin.hide();
}
function onchange(){//设置你按钮可用状态
	grid.down('#del').setDisabled(grid.getSelectionModel().getSelection().length === 0);
	grid.down('#save').setDisabled(grid.store.getModifiedRecords().length === 0);
}
/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	/***grid start***/
	Ext.define('Test', {//定义model
	    extend: 'Ext.data.Model',
	   	fields: [{name: 'id', type: 'string'},
		        {name: 'title', type: 'string'},
		        {name: 'name', type:'string'},
		        {name: 'myLocale', type:'string'},
		        {name: 'parentId',type: 'string'},
		        {name: 'level',type: 'int'}]
	});
	grid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'demo', 
		url: queryUrl,
		height:FHD.getCenterPanelHeight()-5,
		cols:[{dataIndex:'id',id:'id',width:0},
			{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'name', sortable: true, flex : 1,editor: {allowBlank: false}},
			{header: FHD.locale.get('fhd.pages.test.field.title'), dataIndex: 'title', sortable: true, flex : 1,editor: {vtype:'uniqueTitle',allowBlank: false}},
			{header: FHD.locale.get('fhd.pages.test.field.myLocale'), dataIndex: 'myLocale', sortable: true, width: 60,editor:
				new Ext.form.ComboBox({
					store :fieldstore,
					valueField : 'id',
					displayField : 'name',
					selectOnTab: true,
					lazyRender: true,
					typeAhead: true,
					allowBlank : false,
					editable : false
				}),
				renderer:function(value){
					var index = fieldstore.find('id',value);
					var record = fieldstore.getAt(index);
					if(record!=null){
						return record.data.name;
					}else{
						return '';
					}
				}
			},
			{header: 'NUMBER',dataIndex: 'num', sortable: true, width: 60,editor:{
				xtype:'numberfield',
				maxValue: 92,  
				minValue: 0,  
				allowDecimals: true, // 允许小数点 
				nanText:'请输入数字',  
				//hideTrigger: true,  //隐藏上下递增箭头
				//keyNavEnabled: true,  //键盘导航
				//mouseWheelEnabled: true,  //鼠标滚轮
				step:0.5
			}},
			{header: FHD.locale.get('fhd.pages.test.field.parentName'), dataIndex: 'parentId', sortable: true, width: 160,editor:{
					xtype : 'treecombox',
					valueField : 'id',
					displayField : 'text',
					vtype:'treeNode',
					labelWidth:0,
					maxPickerHeight : 160,
					maxPickerWidth : 160,
					store :treeComboxStore
				},
				renderer:function(value){
					var record = treeComboxStore.getNodeById(value);
					if(record!=null){
						return record.get('text');
					}else{
						return '';
					}
				}
			},
			{header: FHD.locale.get('fhd.pages.test.field.level'),dataIndex: 'level', sortable: true, width: 60}],
		tbarItems:[
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',handler:add, scope : this},{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',id:'save',handler:save, disabled : true, scope : this},{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'del', handler:del, disabled : true, scope : this}  
		]
	});
	grid.store.on('load',onchange);//执行store.load()时改变按钮可用状态
	grid.on('selectionchange',onchange);//选择记录发生改变时改变按钮可用状态
	grid.store.on('update',onchange);//修改时改变按钮可用状态
	tree.on('collapse',function(p){
		grid.setWidth(panel.getWidth()-26-5);
	});
	tree.on('expand',function(p){
		grid.setWidth(panel.getWidth()-p.getWidth()-5);
	});
	panel.on('resize',function(p){
		grid.setHeight(p.getHeight()-5);
		if(tree.collapsed){
			grid.setWidth(p.getWidth()-26-5);
		}else{
			grid.setWidth(p.getWidth()-tree.getWidth()-5);
		}
		
	});
	tree.on('resize',function(p){
		if(p.collapsed){
			grid.setWidth(panel.getWidth()-26-5);
		}else{
			grid.setWidth(panel.getWidth()-p.getWidth()-5);
		}
	});
	/***grid end***/
	treeComboxStore.load();
});
/***Ext.onReady end***/
/***CustomValidator start***/
Ext.apply(Ext.form.field.VTypes, {
    //1.验证title不重复
    uniqueTitle:  function(val,field) {
    	var id = field.up("grid").getSelectionModel().getSelection()[0].get('id');
        var flag=false;
        FHD.ajax({
        	url: 'test/checkTitle.f', //验证title唯一性url,
            async:false,//这一项必须加上，否则后台返回true,flag的值也为false
            params : {
					title:val,
					id:id
				},
			callback : function(data){
				if(data){
					flag = true;
				}
			}
        })
        return flag;
    },
    uniqueTitleText: FHD.locale.get('fhd.pages.test.uniqueMsg'),
    //2.验证parentId的合法性
    treeNode: function(val,field){
    	var flag=false;
    	var selection = field.up("grid").getSelectionModel().getSelection()[0];
    	var parentId = field.value;
    	var id = selection.get('id');
    	if(parentId==null || parentId == ''){
    		flag = true;
    	}
    	FHD.ajax({
	        	url: 'test/checkParent.f',//验证parentId的合法性的url,
	            async:false,//这一项必须加上，否则后台返回true,flag的值也为false
	            params : {
						parentId:parentId,
						id:id
					},
				callback : function(data){
					if(data){
						flag = true;
					}
				}
	        })
        return flag;
    },
    treeNodeText: FHD.locale.get('fhd.pages.test.treeNodeMsg')
});
/***CustomValidator end***/

</script>
</head>
<body>
	<div id='demo'></div>
</body>
</html>