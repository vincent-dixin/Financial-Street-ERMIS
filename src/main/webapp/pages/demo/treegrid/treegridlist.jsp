<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树treegrid列表</title>
<script type="text/javascript">
/***attribute start***/
var treegrid,isAdd,formwindow,viewwindow;
var addUrl = 'test/addTestMvc.f';//新增保存url
var updateUrl = 'test/updateTestMvc.f';//更新保存url
var delUrl = 'test/deleteTestMvcs.f'; //删除url
var queryUrl = 'test/queryTestMvcTree.f'; //查询url
var fieldstore = Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	data : [{'id' : 'zh-cn','name' : FHD.locale.get('fhd.pages.test.chinese')},{'id' : 'en','name' : FHD.locale.get('fhd.pages.test.english')}]
});
var treeComboxStore = Ext.create('Ext.data.TreeStore', {//treeCombox的store
	fields : ['text', 'id'],
	root : {text : FHD.locale.get('fhd.common.select'),id:'',expanded : true},
	proxy : {type : 'ajax',url : 'test/loadTestMvcTree.f'/*加载tree的url*/},
	autoload:true
});
var treegridColums =[
	{
		hidden:true,
		text: 'ID',
	    dataIndex: 'id'
	},
	{
	    xtype: 'treecolumn', //this is so we know which column will show the tree
	    text: FHD.locale.get('fhd.pages.test.field.name'),
	    flex: 2,
	    dataIndex: 'name',
	    sortable: true,
	    renderer:function(value,metaData,record,colIndex,store,view) { 
			//
			metaData.tdAttr = 'data-qtip="'+value+'"';  
		    return "<a href=\"javascript:void(0);\" onclick=\"view('" + record.get('id') + "')\">"+value+"</a>";  
		}
	},
	{
	    text: FHD.locale.get('fhd.pages.test.field.title'),
	    flex: 1,
	    dataIndex: 'title',
	    sortable: true
	},
	{
	    text: FHD.locale.get('fhd.pages.test.field.myLocale'),
	    dataIndex: 'myLocale',
	    sortable: true,
	    renderer:function(value){
			var index = fieldstore.find('id',value);
			var record = fieldstore.getAt(index);
			if(record!=null){
				return record.data.name;
			}else{
				return '';
			}
		},
	    width: 60
	},
	{
	    text: 'NUMBER',
	    dataIndex: 'num',
	    sortable: true,
	    width: 60
	},
	{
	    text: FHD.locale.get('fhd.pages.test.field.level'),
	    dataIndex: 'level',
	    sortable: true,
	    width: 60
	}
];
var tbar =[//菜单项
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',id:'add',handler:edit},{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.edit'),iconCls: 'icon-edit',id:'edit', handler:edit, disabled : true} ,{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'del', handler:del, disabled : true}  
		];
/***attribute end***/
/***function start***/
function edit(button){//新增方法
	var selection = treegrid.getSelectionModel().getSelection()[0];//得到选中的记录
	formwindow = new Ext.Window({
		layout:'fit',
		iconCls: 'icon-edit',//标题前的图片
		modal:true,//是否模态窗口
		collapsible:true,
		width:400,
		height:240,
		maximizable:true,//（是否增加最大化，默认没有）
		autoLoad:{ url: 'pages/demo/treegrid/treegridedit.jsp',nocache:true,scripts:true}
	});
	formwindow.show();
	if(button.id=='add'){
		formwindow.setTitle(FHD.locale.get('fhd.common.add'));
		isAdd = true;
	}else{
		formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
		formwindow.initialConfig.id = selection.get('id');
		isAdd = false;
	}
} 
function view(id){
	FHD.ajax({//ajax调用
		url : 'test/viewTestMvc.f',
		params : {
			id:id
		},
		callback : function(data){
			viewwindow = new Ext.Window({
				title:'查看',
		    	layout:'fit',
				iconCls: 'icon-report',//标题前的图片
				modal:true,//是否模态窗口
				collapsible:true,
				width:400,
				height:240,
				maximizable:true,//（是否增加最大化，默认没有）
				items:[Ext.create('Ext.panel.Panel',{
            		width: 300,
                    bodyStyle: "padding:5px;font-size:12px;"
            	})],
				listeners: {
	                afterlayout: function() {
	                	var panel = this.down('panel');
                        tpl = Ext.create('Ext.Template', 
                            '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.name')+'</div>: {name}</p>',
                            '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.title')+'</div>: {title}</p>',
                            '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.myLocale')+'</div>: {myLocaleName}</p>',
                            '<p><div style="float:left;width:15%">NUMBER</div>: {num}</p>',
                            '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.parentName')+'</div>: {parentName}</p>',
                            '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.level')+'</div>: {level}</p>'
                        );

	                    tpl.overwrite(panel.body, data);
	                }
	            },
	            buttons:[
	            	{itemId:'viewcancel',text: FHD.locale.get('fhd.common.cancel'),iconCls: 'icon-cancel',handler:close}
	            ]
		    }).show();
		}
	});
	
	
}
function close(){
	viewwindow.close();
}
function del(){//删除方法
	var selection = treegrid.getSelectionModel().getSelection();//得到选中的记录
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
						if(data){//删除成功！
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							treeComboxStore.load();//重新加载treeCombox
							treegrid.store.load();
						}
					}
				});
			}
		}
	});
}
function setstatus(){//设置你按钮可用状态
	var length = treegrid.getSelectionModel().getSelection().length;
	treegrid.down('#edit').setDisabled(length == 0 || length>1);
	treegrid.down('#del').setDisabled(length == 0);
}
/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	Ext.QuickTips.init(); 
	treegrid = Ext.create('FHD.ux.TreeGridPanel',{//实例化一个treegrid列表
        useArrows: true,
        rootVisible: false,
        multiSelect: true,
        rowLines:true,
        singleExpand: false,
        checked: false,
        multiSelect:false,
		renderTo: 'demo', //渲染到id为demo的div里
		url: queryUrl,//调用后台url
		height:FHD.getCenterPanelHeight()-5,//高度为：获取center-panel的高度
		cols:treegridColums,//cols:为需要显示的列
		tbarItems:tbar
	});
	treegrid.store.on('load',setstatus);
	treegrid.on('selectionchange',setstatus);
	tree.on('collapse',function(p){
		treegrid.setWidth(panel.getWidth()-26-5);
	});
	tree.on('expand',function(p){
		treegrid.setWidth(panel.getWidth()-p.getWidth()-5);
	});
	panel.on('resize',function(p){
		treegrid.setHeight(p.getHeight()-5);
		if(tree.collapsed){
			treegrid.setWidth(p.getWidth()-26-5);
		}else{
			treegrid.setWidth(p.getWidth()-tree.getWidth()-5);
		}
	});
	tree.on('resize',function(p){
		if(p.collapsed){
			treegrid.setWidth(panel.getWidth()-26-5);
		}else{
			treegrid.setWidth(panel.getWidth()-p.getWidth()-5);
		}
	});
});
/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='demo'></div>
</body>
</html>