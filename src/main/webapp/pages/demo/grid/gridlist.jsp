<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
/***attribute start***/
var grid,isAdd,formwindow,viewwindow;
var addUrl = 'test/addTestMvc.f';//新增保存url
var updateUrl = 'test/updateTestMvc.f';//更新保存url
var delUrl = 'test/deleteTestMvcs.f'; //删除url
var queryUrl = 'test/queryTestMvcList.f'; //查询url
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
var gridColums =[
	{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'name', sortable: true, width: 60,flex : 1,renderer:function(value,metaData,record,colIndex,store,view) { 
		  /*
		  	data-qtip:设置提示正文内容。
			data-qtitle:设置提示的标题。
			data-qwidth:设置提示的宽度。
			data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
			*/
		metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+10+'"'; 
	    return "<a href=\"javascript:void(0);\" onclick=\"view('" + record.get('id') + "')\">"+value+"</a>";  
	}},
	{header: FHD.locale.get('fhd.pages.test.field.title'), dataIndex: 'title', sortable: true, width: 60,flex : 1},
	{header: FHD.locale.get('fhd.pages.test.field.myLocale'), dataIndex: 'myLocale', sortable: true, width: 60,renderer:function(value){
			var index = fieldstore.find('id',value);
			var record = fieldstore.getAt(index);
			if(record!=null){
				return record.data.name;
			}else{
				return '';
			}
		}},
	{header: 'NUMBER',dataIndex: 'num', sortable: true, width: 60},
	{header: FHD.locale.get('fhd.pages.test.field.parentName'), dataIndex: 'parentId', sortable: true, width: 160,
		renderer:function(value){
			var record = treeComboxStore.getNodeById(value);
			if(record!=null){
				return record.get('text');
			}else{
				return value;
			}
		}},
	{header: FHD.locale.get('fhd.pages.test.field.level'),dataIndex: 'level', sortable: true, width: 60}
];
var tbar =[//菜单项
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',id:'add',handler:edit, scope : this},{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'del', handler:del, disabled : true, scope : this}  
		];
/***attribute end***/
/***function start***/
function edit(button){//新增方法
	var selection = grid.getSelectionModel().getSelection()[0];//得到选中的记录
	formwindow = new Ext.Window({
		layout:'fit',
		iconCls: 'icon-edit',//标题前的图片
		modal:true,//是否模态窗口
		collapsible:true,
		width:500,
		height:350,
		maximizable:true,//（是否增加最大化，默认没有）
		autoLoad:{ url: 'pages/demo/grid/gridedit.jsp',nocache:true,scripts:true}
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
	var selection = grid.getSelectionModel().getSelection();//得到选中的记录
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
							grid.store.load();
						}
					}
				});
			}
		}
	});
}
function setstatus(){//设置你按钮可用状态
	grid.down('#del').setDisabled(grid.getSelectionModel().getSelection().length === 0);
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
		listeners:{itemdblclick:edit}//双击执行修改方法
	});
	grid.store.on('load',setstatus);
	grid.on('selectionchange',setstatus);
	grid.getView().on('render', function(view) {
	    view.tip = Ext.create('Ext.tip.ToolTip', {
	        // The overall target element.
	        target: view.el,
	        // Each grid row causes its own seperate show and hide.
	        delegate: view.itemSelector,
	        // Moving within the row should not hide the tip.
	        trackMouse: true,
	        // Render immediately so that tip.body can be referenced prior to the first show.
	        renderTo: Ext.getBody(),
	        listeners: {
	            // Change content dynamically depending on which element triggered the show.
	            beforeshow: function updateTipBody(tip) {
	                tip.update('Over company "' + view.getRecord(tip.triggerElement).get('company') + '"');
	            }
	        }
	    });
	});
	tree.on('collapse',function(p){
		grid.setWidth(panel.getWidth()-26-5);
	});
	tree.on('expand',function(p){
		grid.setWidth(panel.getWidth()-p.getWidth()-5);
	});
	panel.on('resize',function(p){
		grid.setHeight(p.getHeight()-3);
		if(tree.collapsed){
			grid.setWidth(p.getWidth()-26-5);
		}else{
			grid.setWidth(p.getWidth()-tree.getWidth()-5);
		}
	})
	tree.on('resize',function(p){
		if(p.collapsed){
			grid.setWidth(panel.getWidth()-26-5);
		}else{
			grid.setWidth(panel.getWidth()-p.getWidth()-5);
		}
	});
	//FHD.componentResize(grid,220,5);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度 	
	
});
/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='demo'></div>
</body>
</html>